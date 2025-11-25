package com.velinfotech.controller;

import com.velinfotech.model.Blogpost;
import com.velinfotech.repository.BlogpostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogposts")
@RequiredArgsConstructor
public class BlogpostController {

    private final BlogpostRepository blogpostRepository;

    // 🔔 Quick test endpoint – just to see API is alive
    @GetMapping("/ping")
    public String ping() {
        return "Blog API is up ✅";
    }

    // ➕ Create blog post
    @PostMapping
    public ResponseEntity<Blogpost> create(@RequestBody Blogpost input) {
        // no status/visibility/slug – just save what client sends
        Blogpost saved = blogpostRepository.save(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 📜 Get all posts
    @GetMapping
    public List<Blogpost> getAll() {
        return blogpostRepository.findAll();
    }

    // 🔍 Get by id
    @GetMapping("/{id}")
    public ResponseEntity<Blogpost> getById(@PathVariable Long id) {
        return blogpostRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🗑️ Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!blogpostRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        blogpostRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
