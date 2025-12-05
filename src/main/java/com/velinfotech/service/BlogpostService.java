package com.velinfotech.service;

import com.velinfotech.dto.BlogpostRequest;
import com.velinfotech.dto.BlogpostResponse;
import com.velinfotech.model.Blogpost;
import com.velinfotech.repository.BlogpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@Service
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


        String imageValue = toBase64IfUrl(request.getImageBase64());
        entity.setFeaturedImageUrl(imageValue);

        Blogpost saved = blogpostRepository.save(entity);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!blogpostRepository.existsById(id)) {
            throw new RuntimeException("Blog post not found: " + id);
        }
        blogpostRepository.deleteById(id);
    }

    public BlogpostResponse getById(Long id) {
        Blogpost entity = blogpostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found: " + id));
        return toResponse(entity);
    }

    public Page<BlogpostResponse> listAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Blogpost> result = blogpostRepository.findAll(pageable);
        return result.map(this::toResponse);
    }

    public Page<BlogpostResponse> searchByTitle(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Blogpost> result = blogpostRepository.findByTitleContainingIgnoreCase(q, pageable);
        return result.map(this::toResponse);
    }

    // --- internal helpers ---

    private BlogpostResponse toResponse(Blogpost entity) {
        BlogpostResponse dto = new BlogpostResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setExcerpt(entity.getExcerpt());


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
