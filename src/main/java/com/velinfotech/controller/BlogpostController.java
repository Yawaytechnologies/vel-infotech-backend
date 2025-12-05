package com.velinfotech.controller;

import com.velinfotech.dto.BlogpostRequest;
import com.velinfotech.dto.BlogpostResponse;
import com.velinfotech.service.BlogpostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/blogposts")
@RequiredArgsConstructor
public class BlogpostController {

    private final BlogpostService blogpostService;

    @GetMapping("/ping")
    public String ping() {
        return "Blog API is up ✅";
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BlogpostResponse> create(@Valid @RequestBody BlogpostRequest request) {
        BlogpostResponse saved = blogpostService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BlogpostResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BlogpostRequest request
    ) {
        BlogpostResponse updated = blogpostService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BlogpostResponse> getById(@PathVariable Long id) {
        BlogpostResponse dto = blogpostService.getById(id);
        return ResponseEntity.ok(dto);
    }

    // LIST WITH PAGINATION (optional, you can adjust params)
    @GetMapping
    public Page<BlogpostResponse> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return blogpostService.listAll(page, size, sortBy, direction);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogpostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
