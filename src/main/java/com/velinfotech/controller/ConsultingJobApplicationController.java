package com.velinfotech.controller;

import com.velinfotech.dto.ConsultingJobApplicationRequest;
import com.velinfotech.dto.ConsultingJobApplicationResponse;
import com.velinfotech.service.ConsultingJobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consulting")
@CrossOrigin(origins = "*")
public class ConsultingJobApplicationController {

    private final ConsultingJobApplicationService applicationService;

    public ConsultingJobApplicationController(ConsultingJobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /* ====================== CREATE ====================== */

    // Public apply form on /consult/careers/{jobId}
    @PostMapping("/job-posts/{jobId}/apply")
    public ResponseEntity<ConsultingJobApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody ConsultingJobApplicationRequest request
    ) {
        ConsultingJobApplicationResponse response = applicationService.applyForJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /* ======================= READ ======================= */

    // Admin list — every consulting application, newest first
    @GetMapping("/applications")
    public ResponseEntity<List<ConsultingJobApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    // Applications for one job post
    @GetMapping("/job-posts/{jobId}/applications")
    public ResponseEntity<List<ConsultingJobApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<ConsultingJobApplicationResponse> getApplicationById(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(applicationService.getApplicationById(applicationId));
    }

    /* ====================== DELETE ====================== */

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}
