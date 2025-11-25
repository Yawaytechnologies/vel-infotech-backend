package com.velinfotech.service;

import com.velinfotech.dto.BlogpostRequest;
import com.velinfotech.dto.BlogpostResponse;
import com.velinfotech.model.Blogpost;
import com.velinfotech.repository.BlogpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
        entity.setFeaturedImageUrl(request.getFeaturedImageUrl());

        Blogpost saved = blogpostRepository.save(entity);
        return toResponse(saved);
    }

    public BlogpostResponse update(Long id, BlogpostRequest request) {
        Blogpost entity = blogpostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found: " + id));

        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setExcerpt(request.getExcerpt());
        entity.setFeaturedImageUrl(request.getFeaturedImageUrl());

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
        // we no longer have createdAt, so sort by id (or title) instead
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
        dto.setFeaturedImageUrl(entity.getFeaturedImageUrl());
        return dto;
    }
}
