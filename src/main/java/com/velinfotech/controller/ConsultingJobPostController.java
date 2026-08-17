package com.velinfotech.controller;

import com.velinfotech.dto.ConsultingJobPostRequest;
import com.velinfotech.dto.ConsultingJobPostResponse;
import com.velinfotech.service.ConsultingJobPostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consulting/job-posts")
@CrossOrigin(origins = "*")
public class ConsultingJobPostController {

    private final ConsultingJobPostService jobPostService;

    public ConsultingJobPostController(ConsultingJobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ConsultingJobPostResponse> createJobPost(
            @Valid @RequestBody ConsultingJobPostRequest request
    ) {
        ConsultingJobPostResponse created = jobPostService.createJobPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ ALL — newest first, each carrying its application count
    @GetMapping
    public ResponseEntity<List<ConsultingJobPostResponse>> getAllJobPosts() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ConsultingJobPostResponse> getJobPostById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostService.getJobPostById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ConsultingJobPostResponse> updateJobPost(
            @PathVariable Long id,
            @Valid @RequestBody ConsultingJobPostRequest request
    ) {
        return ResponseEntity.ok(jobPostService.updateJobPost(id, request));
    }

    // DELETE — also removes the applications belonging to this post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable Long id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.noContent().build();
    }
}
