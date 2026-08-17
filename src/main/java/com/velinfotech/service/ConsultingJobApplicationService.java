package com.velinfotech.service;

import com.velinfotech.dto.ConsultingJobApplicationRequest;
import com.velinfotech.dto.ConsultingJobApplicationResponse;

import java.util.List;

public interface ConsultingJobApplicationService {

    ConsultingJobApplicationResponse applyForJob(Long jobId, ConsultingJobApplicationRequest request);

    List<ConsultingJobApplicationResponse> getAllApplications();

    List<ConsultingJobApplicationResponse> getApplicationsForJob(Long jobId);

    ConsultingJobApplicationResponse getApplicationById(Long applicationId);

    void deleteApplication(Long applicationId);
}
