package com.velinfotech.service;

import com.velinfotech.model.JobPost;

import java.util.List;

public interface JobPostService {

    JobPost createJobPost(JobPost jobPost);

    List<JobPost> getAllJobPosts();

    JobPost getJobPostById(Long id);

    JobPost updateJobPost(Long id, JobPost jobPost);

    void deleteJobPost(Long id);
}
