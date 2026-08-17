package com.velinfotech.service;

import com.velinfotech.dto.BlogpostRequest;
import com.velinfotech.dto.BlogpostResponse;
import com.velinfotech.exception.ResourceNotFoundException;
import com.velinfotech.model.Blogpost;
import com.velinfotech.repository.BlogpostRepository;
import com.velinfotech.util.SlugGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

/**
 * Class-level @Transactional is load-bearing, not decoration: content, excerpt and
 * featuredImageUrl are @Lob columns, and PostgreSQL refuses to read large objects in
 * auto-commit mode ("Large Objects may not be used in auto-commit mode"). With
 * spring.jpa.open-in-view=false there is no ambient session, so every read that
 * touches a LOB needs its own transaction.
 */
@Service
@Transactional
public class BlogpostService {

    private final BlogpostRepository blogpostRepository;

    @Autowired
    public BlogpostService(BlogpostRepository blogpostRepository) {
        this.blogpostRepository = blogpostRepository;
    }

    // --- public API ---

    public BlogpostResponse create(BlogpostRequest request) {
        Blogpost entity = new Blogpost();

        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setExcerpt(request.getExcerpt());
        entity.setCategory(normalizeCategory(request.getCategory()));

        // Honour an explicit slug, otherwise derive one from the title.
        String requested = hasText(request.getSlug()) ? request.getSlug() : request.getTitle();
        entity.setSlug(SlugGenerator.uniqueSlug(requested, blogpostRepository::existsBySlug));

        // 🔁 convert URL -> base64 if needed
        String imageValue = toBase64IfUrl(request.getImageBase64());
        entity.setFeaturedImageUrl(imageValue);

        Blogpost saved = blogpostRepository.save(entity);
        return toResponse(saved);
    }

    public BlogpostResponse update(Long id, BlogpostRequest request) {
        Blogpost entity = blogpostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found: " + id));

        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setExcerpt(request.getExcerpt());
        entity.setCategory(normalizeCategory(request.getCategory()));

        // The slug is intentionally sticky: retitling a post must not silently
        // change its URL and orphan every link already pointing at it. It moves
        // only when a new slug is explicitly supplied.
        if (hasText(request.getSlug())) {
            String candidate = SlugGenerator.slugify(request.getSlug());

            if (!candidate.equals(entity.getSlug())) {
                entity.setSlug(SlugGenerator.uniqueSlug(candidate, blogpostRepository::existsBySlug));
            }
        } else if (!hasText(entity.getSlug())) {
            // Post predates the slug column and the backfill has not reached it.
            entity.setSlug(SlugGenerator.uniqueSlug(request.getTitle(), blogpostRepository::existsBySlug));
        }

        String imageValue = toBase64IfUrl(request.getImageBase64());
        entity.setFeaturedImageUrl(imageValue);

        Blogpost saved = blogpostRepository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public BlogpostResponse getBySlug(String slug) {
        Blogpost entity = blogpostRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found: " + slug));
        return toResponse(entity);
    }

    public void delete(Long id) {
        if (!blogpostRepository.existsById(id)) {
            throw new RuntimeException("Blog post not found: " + id);
        }
        blogpostRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BlogpostResponse getById(Long id) {
        Blogpost entity = blogpostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found: " + id));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<BlogpostResponse> listAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Blogpost> result = blogpostRepository.findAll(pageable);
        return result.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BlogpostResponse> searchByTitle(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Blogpost> result = blogpostRepository.findByTitleContainingIgnoreCase(q, pageable);
        return result.map(this::toResponse);
    }

    // --- internal helpers ---

    /** Category shown when a post has none — matches the mock-up's default box. */
    public static final String DEFAULT_CATEGORY = "Articles";

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String normalizeCategory(String category) {
        return hasText(category) ? category.trim() : DEFAULT_CATEGORY;
    }

    private BlogpostResponse toResponse(Blogpost entity) {
        BlogpostResponse dto = new BlogpostResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setSlug(entity.getSlug());
        dto.setCategory(normalizeCategory(entity.getCategory()));
        dto.setContent(entity.getContent());
        dto.setExcerpt(entity.getExcerpt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // expose base64 as imageBase64
        dto.setImageBase64(entity.getFeaturedImageUrl());

        return dto;
    }

    private String toBase64IfUrl(String imageField) {
        if (imageField == null || imageField.isBlank()) {
            return imageField;
        }

        String lower = imageField.toLowerCase();

        if (lower.startsWith("http://") || lower.startsWith("https://")) {
            try {
                return downloadImageAsBase64(imageField);
            } catch (Exception e) {
                e.printStackTrace();
                // if conversion fails, at least keep the original
                return imageField;
            }
        }

        // assume it's already base64 or a data URL
        return imageField;
    }

    /**
     * Downloads the image bytes and returns a data URL:
     * data:image/*;base64,AAAA...
     */
    private String downloadImageAsBase64(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            byte[] bytes = baos.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(bytes);

            return "data:image/*;base64," + base64;
        }
    }
}
