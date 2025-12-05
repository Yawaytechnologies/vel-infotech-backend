// src/main/java/com/velinfotech/service/JobApplicationService.java
package com.velinfotech.service;

import com.velinfotech.dto.JobApplicationRequest;
import com.velinfotech.dto.JobApplicationResponse;

import java.util.List;

public interface JobApplicationService {

    // CREATE
    JobApplicationResponse applyForJob(Long jobId, JobApplicationRequest request);

    // READ
    List<JobApplicationResponse> getApplicationsForJob(Long jobId);

    JobApplicationResponse getApplicationById(Long jobId, Long applicationId);

    // UPDATE
    JobApplicationResponse updateApplication(Long jobId, Long applicationId, JobApplicationRequest request);

    // DELETE
    void deleteApplication(Long jobId, Long applicationId);
}
