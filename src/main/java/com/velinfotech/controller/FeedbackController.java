package com.velinfotech.controller;

import com.velinfotech.model.Feedback;
import com.velinfotech.repository.FeedbackRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin // set allowed origins for prod
public class FeedbackController {

    private final FeedbackRepository repo;

    public FeedbackController(FeedbackRepository repo) {
        this.repo = repo;
    }

    /** Create a new feedback (201 + Location) */
    @PostMapping
    public ResponseEntity<Feedback> create(@Valid @RequestBody Feedback body) {
        Feedback saved = repo.save(body);
        return ResponseEntity
                .created(URI.create("/api/feedbacks/" + saved.getId()))
                .body(saved);
    }

    /** List feedbacks (pageable, newest first) */
    @GetMapping
    public Page<Feedback> list(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return repo.findAll(pageable);
    }

    /** Get single feedback by id */
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> get(@PathVariable @Min(1) Long id) {
        return ResponseEntity.of(repo.findById(id));
    }

    /** Delete feedback by id */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Return {field: message} on validation errors (400) */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return errors;
    }
}
