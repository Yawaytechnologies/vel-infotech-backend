package com.velinfotech.controller;

import com.velinfotech.exception.ResourceNotFoundException;
import com.velinfotech.model.BlogImage;
import com.velinfotech.repository.BlogImageRepository;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class BlogImageController {

    private final BlogImageRepository blogImageRepository;

    public BlogImageController(BlogImageRepository blogImageRepository) {
        this.blogImageRepository = blogImageRepository;
    }

    /**
     * Serves the bytes. Rows are immutable — a changed picture is a new row with a
     * new id — so these can be cached hard and forever, which is the whole point of
     * moving them out of the HTML.
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> get(@PathVariable Long id) {
        BlogImage image = blogImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic().immutable())
                .eTag(image.getSha256())
                .contentLength(image.getSizeBytes())
                .body(image.getData());
    }
}
