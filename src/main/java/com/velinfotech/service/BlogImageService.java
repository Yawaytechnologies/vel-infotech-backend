package com.velinfotech.service;

import com.velinfotech.model.BlogImage;
import com.velinfotech.repository.BlogImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

/**
 * Moves image bytes out of the page and behind a URL.
 *
 * Featured images were stored as base64 data URIs, which meant every byte was
 * inlined into the HTML on every request: one article weighed 641KB. Data URIs are
 * also uncacheable, invisible to Google Images, and rejected as og:image by every
 * social crawler. Storing the bytes once and serving them from /api/images/{id}
 * fixes all of that at once.
 */
@Service
public class BlogImageService {

    private static final Logger log = LoggerFactory.getLogger(BlogImageService.class);

    /** Path images are served from; also how stored references are recognised. */
    public static final String IMAGE_PATH = "/api/images/";

    private final BlogImageRepository blogImageRepository;

    public BlogImageService(BlogImageRepository blogImageRepository) {
        this.blogImageRepository = blogImageRepository;
    }

    /**
     * Turns a base64 data URI into a stored image and returns its path. Values that
     * are already a URL, or blank, are returned untouched — a pasted https:// image
     * is fine as it is, and used to be downloaded and re-encoded as base64, which
     * was precisely backwards.
     */
    @Transactional
    public String toServableUrl(String value) {
        if (value == null || value.isBlank()) return value;
        if (!value.startsWith("data:")) return value;

        try {
            return IMAGE_PATH + store(value).getId();
        } catch (Exception e) {
            // Keep the original rather than losing the picture over a bad payload.
            log.warn("Could not store image, leaving the value as-is: {}", e.getMessage());
            return value;
        }
    }

    /** Parses a data URI, reusing an existing row when the bytes already exist. */
    @Transactional
    public BlogImage store(String dataUrl) {
        int comma = dataUrl.indexOf(',');
        if (comma < 0) throw new IllegalArgumentException("Malformed data URI");

        String header = dataUrl.substring(5, comma);            // e.g. image/jpeg;base64
        String payload = dataUrl.substring(comma + 1);

        String contentType = header.contains(";")
                ? header.substring(0, header.indexOf(';'))
                : header;

        // "image/*" was written by the old download path and is not a real type.
        if (contentType.isBlank() || contentType.endsWith("/*")) {
            contentType = "image/jpeg";
        }

        byte[] bytes = Base64.getDecoder().decode(payload.replaceAll("\\s", ""));
        String sha = sha256(bytes);

        Optional<BlogImage> existing = blogImageRepository.findBySha256(sha);
        if (existing.isPresent()) return existing.get();

        BlogImage image = new BlogImage();
        image.setContentType(contentType);
        image.setData(bytes);
        image.setSha256(sha);
        image.setSizeBytes(bytes.length);

        readDimensions(bytes, image);

        return blogImageRepository.save(image);
    }

    /**
     * Expands a stored path into an absolute URL, which og:image and JSON-LD both
     * require. Falls back to the stored value outside a request (the migration).
     */
    public String toAbsolute(String value) {
        if (value == null || !value.startsWith(IMAGE_PATH)) return value;

        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(value)
                    .toUriString();
        } catch (IllegalStateException e) {
            return value;
        }
    }

    private void readDimensions(byte[] bytes, BlogImage image) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            BufferedImage buffered = ImageIO.read(in);

            if (buffered != null) {
                image.setWidth(buffered.getWidth());
                image.setHeight(buffered.getHeight());
            }
        } catch (Exception e) {
            // Dimensions are a nicety; the image is still perfectly servable.
            log.debug("Could not read image dimensions: {}", e.getMessage());
        }
    }

    private static String sha256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(bytes));
        } catch (Exception e) {
            // Cannot happen: SHA-256 is required of every JVM.
            throw new IllegalStateException(e);
        }
    }
}
