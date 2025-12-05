// src/main/java/com/velinfotech/service/impl/JobApplicationServiceImpl.java
package com.velinfotech.service.impl;

import com.velinfotech.dto.JobApplicationRequest;
import com.velinfotech.dto.JobApplicationResponse;
import com.velinfotech.model.JobApplication;
import com.velinfotech.repository.JobApplicationRepository;
import com.velinfotech.service.JobApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    /* ===================== CREATE ===================== */

    @Override
    public JobApplicationResponse applyForJob(Long jobId, JobApplicationRequest request) {
        JobApplication application = mapToEntity(request);

        // from your entity
        application.setJobId(jobId);
        application.setCreatedAt(LocalDateTime.now());

        JobApplication saved = jobApplicationRepository.save(application);
        return mapToResponse(saved);
    }

    /* ====================== READ ====================== */

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplicationsForJob(Long jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobId(jobId);

        return applications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getApplicationById(Long jobId, Long applicationId) {
        JobApplication application = jobApplicationRepository
                .findByIdAndJobId(applicationId, jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Application not found with id: " + applicationId + " for job: " + jobId
                ));

        return mapToResponse(application);
    }

    /* ===================== UPDATE ===================== */

    @Override
    public JobApplicationResponse updateApplication(Long jobId, Long applicationId, JobApplicationRequest request) {
        JobApplication existing = jobApplicationRepository
                .findByIdAndJobId(applicationId, jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Application not found with id: " + applicationId + " for job: " + jobId
                ));

        // DON'T change jobId or createdAt on update
        updateEntityFromRequest(existing, request);

        JobApplication updated = jobApplicationRepository.save(existing);
        return mapToResponse(updated);
    }

    /* ===================== DELETE ===================== */

    @Override
    public void deleteApplication(Long jobId, Long applicationId) {
        JobApplication existing = jobApplicationRepository
                .findByIdAndJobId(applicationId, jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Application not found with id: " + applicationId + " for job: " + jobId
                ));

        jobApplicationRepository.delete(existing);
    }

    /* ===================== MAPPERS ===================== */

    /**
     * Map DTO -> Entity using your exact entity fields:
     *
     *  - jobId (set separately)
     *  - candidateType
     *  - name
     *  - email
     *  - phone
     *  - qualification
     *  - passingYear
     *  - applyingForPosition
     *  - skills
     *  - totalExperience
     *  - relevantExperience
     *  - noticePeriod
     *  - currentCtc
     *  - createdAt (set on create)
     */

    private JobApplication mapToEntity(JobApplicationRequest request) {
        JobApplication entity = new JobApplication();

        entity.setCandidateType(request.getCandidateType());
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setQualification(request.getQualification());
        entity.setPassingYear(request.getPassingYear());
        entity.setApplyingForPosition(request.getApplyingForPosition());
        entity.setSkills(request.getSkills());
        entity.setTotalExperience(request.getTotalExperience());
        entity.setRelevantExperience(request.getRelevantExperience());
        entity.setNoticePeriod(request.getNoticePeriod());
        entity.setCurrentCtc(request.getCurrentCtc());

        // createdAt + jobId set in service
        return entity;
    }

    private void updateEntityFromRequest(JobApplication entity, JobApplicationRequest request) {
        entity.setCandidateType(request.getCandidateType());
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setQualification(request.getQualification());
        entity.setPassingYear(request.getPassingYear());
        entity.setApplyingForPosition(request.getApplyingForPosition());
        entity.setSkills(request.getSkills());
        entity.setTotalExperience(request.getTotalExperience());
        entity.setRelevantExperience(request.getRelevantExperience());
        entity.setNoticePeriod(request.getNoticePeriod());
        entity.setCurrentCtc(request.getCurrentCtc());
        // DO NOT touch jobId or createdAt here
    }

    private JobApplicationResponse mapToResponse(JobApplication entity) {
        JobApplicationResponse dto = new JobApplicationResponse();

        dto.setId(entity.getId());
        dto.setJobId(entity.getJobId());
        dto.setCandidateType(entity.getCandidateType());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setQualification(entity.getQualification());
        dto.setPassingYear(entity.getPassingYear());
        dto.setApplyingForPosition(entity.getApplyingForPosition());
        dto.setSkills(entity.getSkills());
        dto.setTotalExperience(entity.getTotalExperience());
        dto.setRelevantExperience(entity.getRelevantExperience());
        dto.setNoticePeriod(entity.getNoticePeriod());
        dto.setCurrentCtc(entity.getCurrentCtc());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
