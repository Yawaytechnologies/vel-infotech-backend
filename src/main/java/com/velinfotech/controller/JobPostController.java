package com.velinfotech.controller;

import com.velinfotech.model.JobPost;
import com.velinfotech.service.JobPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-posts")
@CrossOrigin(origins = "*")
public class JobPostController {

    private final JobPostService jobPostService;

    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<JobPost> createJobPost(@RequestBody JobPost jobPost) {
        JobPost created = jobPostService.createJobPost(jobPost);
        return ResponseEntity.ok(created);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<JobPost>> getAllJobPosts() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<JobPost> getJobPostById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostService.getJobPostById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<JobPost> updateJobPost(@PathVariable Long id,
                                                 @RequestBody JobPost jobPost) {
        JobPost updated = jobPostService.updateJobPost(id, jobPost);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable Long id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.noContent().build();
    }
}
