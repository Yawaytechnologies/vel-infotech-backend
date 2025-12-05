// src/main/java/com/velinfotech/jobs/controller/JobApplicationController.java
package com.velinfotech.controller;

import com.velinfotech.dto.JobApplicationRequest;
import com.velinfotech.dto.JobApplicationResponse;
import com.velinfotech.service.JobApplicationService; // ⬅️ ADD THIS
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*") // adjust for your frontend
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    /* ====================== CREATE ====================== */

    // Existing endpoint – keep it so your current frontend keeps working
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse response = jobApplicationService.applyForJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // REST-style create endpoint (optional but cleaner)
    @PostMapping("/{jobId}/applications")
    public ResponseEntity<JobApplicationResponse> createApplication(
            @PathVariable Long jobId,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse response = jobApplicationService.applyForJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /* ====================== READ ====================== */

    // Get all applications for a specific job
    @GetMapping("/{jobId}/applications")
    public ResponseEntity<List<JobApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId
    ) {
        List<JobApplicationResponse> applications =
                jobApplicationService.getApplicationsForJob(jobId);
        return ResponseEntity.ok(applications);
    }

    // Get a specific application by job + application id
    @GetMapping("/{jobId}/applications/{applicationId}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(
            @PathVariable Long jobId,
            @PathVariable Long applicationId
    ) {
        JobApplicationResponse response =
                jobApplicationService.getApplicationById(jobId, applicationId);
        return ResponseEntity.ok(response);
    }

    /* ====================== UPDATE ====================== */

    // Full update of an application
    @PutMapping("/{jobId}/applications/{applicationId}")
    public ResponseEntity<JobApplicationResponse> updateApplication(
            @PathVariable Long jobId,
            @PathVariable Long applicationId,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse updated =
                jobApplicationService.updateApplication(jobId, applicationId, request);
        return ResponseEntity.ok(updated);
    }

    /* ====================== DELETE ====================== */

    @DeleteMapping("/{jobId}/applications/{applicationId}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long jobId,
            @PathVariable Long applicationId
    ) {
        jobApplicationService.deleteApplication(jobId, applicationId);
        return ResponseEntity.noContent().build();
    }
}
