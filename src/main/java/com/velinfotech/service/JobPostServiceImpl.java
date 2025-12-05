package com.velinfotech.service.impl;

import com.velinfotech.model.JobPost;
import com.velinfotech.repository.JobPostRepository;
import com.velinfotech.service.JobPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;

    public JobPostServiceImpl(JobPostRepository jobPostRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    @Override
    public JobPost createJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    @Override
    public List<JobPost> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    @Override
    public JobPost getJobPostById(Long id) {
        return jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Post not found with id: " + id));
    }

    @Override
    public JobPost updateJobPost(Long id, JobPost jobPost) {
        JobPost existing = getJobPostById(id);

        existing.setExperience(jobPost.getExperience());
        existing.setJobDescription(jobPost.getJobDescription());
        existing.setJobTitle(jobPost.getJobTitle());
        existing.setDepartment(jobPost.getDepartment());
        existing.setLocation(jobPost.getLocation());
        existing.setQualification(jobPost.getQualification());
        existing.setResponsibilities(jobPost.getResponsibilities());
        existing.setSalaryRange(jobPost.getSalaryRange());
        existing.setSkills(jobPost.getSkills());
        existing.setWorkMode(jobPost.getWorkMode());

        return jobPostRepository.save(existing);
    }

    @Override
    public void deleteJobPost(Long id) {
        jobPostRepository.deleteById(id);
    }
}
