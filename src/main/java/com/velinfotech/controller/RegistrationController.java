package com.velinfotech.controller;

import com.velinfotech.model.Registration;
import com.velinfotech.model.LearningMode;
import com.velinfotech.repository.RegistrationRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin // adjust origins if needed
public class RegistrationController {

    private final RegistrationRepository repo;

    public RegistrationController(RegistrationRepository repo) {
        this.repo = repo;
    }

    // Create a registration
    @PostMapping
    public ResponseEntity<Registration> create(@Valid @RequestBody Registration body) {
        Registration saved = repo.save(body);
        return ResponseEntity
                .created(URI.create("/api/registrations/" + saved.getId()))
                .body(saved);
    }

    // List all (newest first)
    @GetMapping
    public List<Registration> all() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Registration> byId(@PathVariable Long id) {
        return ResponseEntity.of(repo.findById(id));
    }

    // Optional: search by mode and/or course via query params
    // /api/registrations/search?mode=ONLINE&course=Java
    @GetMapping("/search")
    public List<Registration> search(
            @RequestParam(required = false) LearningMode mode,
            @RequestParam(required = false) String course
    ) {
        List<Registration> all = repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return all.stream()
                .filter(r -> mode == null || r.getMode() == mode)
                .filter(r -> course == null || (r.getCourse() != null &&
                        r.getCourse().toLowerCase().contains(course.toLowerCase())))
                .toList();
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Basic validation error handler (400 with field -> message)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
        return errors;
    }
}
