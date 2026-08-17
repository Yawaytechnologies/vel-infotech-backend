package com.velinfotech.service;

import com.velinfotech.dto.ConsultingJobPostRequest;
import com.velinfotech.dto.ConsultingJobPostResponse;

import java.util.List;

public interface ConsultingJobPostService {

    ConsultingJobPostResponse createJobPost(ConsultingJobPostRequest request);

    List<ConsultingJobPostResponse> getAllJobPosts();

    ConsultingJobPostResponse getJobPostById(Long id);

    ConsultingJobPostResponse updateJobPost(Long id, ConsultingJobPostRequest request);

    void deleteJobPost(Long id);
}
