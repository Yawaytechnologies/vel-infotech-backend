package com.velinfotech.controller;

import com.velinfotech.model.Internship;
import com.velinfotech.service.InternshipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    @Autowired
    private InternshipServiceImpl internshipService;

    // CREATE
    @PostMapping
    public ResponseEntity<Internship> createInternship(@RequestBody Internship internship) {
        Internship saved = internshipService.createInternship(internship);
        return ResponseEntity.ok(saved);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Internship>> getAllInternships() {
        return ResponseEntity.ok(internshipService.getAllInternships());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Internship> getInternshipById(@PathVariable Long id) {
        return ResponseEntity.ok(internshipService.getInternshipById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Internship> updateInternship(@PathVariable Long id, @RequestBody Internship internship) {
        return ResponseEntity.ok(internshipService.updateInternship(id, internship));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInternship(@PathVariable Long id) {
        internshipService.deleteInternship(id);
        return ResponseEntity.ok("Internship deleted successfully!");
    }
}
