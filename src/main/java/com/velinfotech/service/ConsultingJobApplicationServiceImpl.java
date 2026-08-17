package com.velinfotech.service;

import com.velinfotech.dto.ConsultingJobApplicationRequest;
import com.velinfotech.dto.ConsultingJobApplicationResponse;
import com.velinfotech.exception.ResourceNotFoundException;
import com.velinfotech.model.CandidateType;
import com.velinfotech.model.ConsultingJobApplication;
import com.velinfotech.repository.ConsultingJobApplicationRepository;
import com.velinfotech.repository.ConsultingJobPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsultingJobApplicationServiceImpl implements ConsultingJobApplicationService {

    private final ConsultingJobApplicationRepository applicationRepository;
    private final ConsultingJobPostRepository jobPostRepository;

    public ConsultingJobApplicationServiceImpl(ConsultingJobApplicationRepository applicationRepository,
                                               ConsultingJobPostRepository jobPostRepository) {
        this.applicationRepository = applicationRepository;
        this.jobPostRepository = jobPostRepository;
    }

    /* ===================== CREATE ===================== */

    @Override
    public ConsultingJobApplicationResponse applyForJob(Long jobId,
                                                        ConsultingJobApplicationRequest request) {
        if (!jobPostRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Consulting job post not found with id: " + jobId);
        }

        ConsultingJobApplication entity = new ConsultingJobApplication();
        applyRequest(entity, request);

        entity.setJobId(jobId);
        entity.setCreatedAt(LocalDateTime.now());

        return mapToResponse(applicationRepository.save(entity));
    }

    /* ====================== READ ====================== */

    @Override
    @Transactional(readOnly = true)
    public List<ConsultingJobApplicationResponse> getAllApplications() {
        return applicationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultingJobApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobIdOrderByCreatedAtDesc(jobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultingJobApplicationResponse getApplicationById(Long applicationId) {
        return mapToResponse(findOrThrow(applicationId));
    }

    /* ===================== DELETE ===================== */

    @Override
    public void deleteApplication(Long applicationId) {
        applicationRepository.delete(findOrThrow(applicationId));
    }

    /* ===================== HELPERS ===================== */

    private ConsultingJobApplication findOrThrow(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consulting job application not found with id: " + applicationId));
    }

    private void applyRequest(ConsultingJobApplication entity,
                              ConsultingJobApplicationRequest request) {
        entity.setCandidateType(request.getCandidateType());
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setQualification(request.getQualification());
        entity.setPassingYear(request.getPassingYear());
        entity.setApplyingForPosition(request.getApplyingForPosition());
        entity.setSkills(request.getSkills());
        entity.setProfileSummary(request.getProfileSummary());

        // Freshers have no experience details; drop anything sent so the admin view
        // does not show stale numbers next to a "Fresher" badge.
        if (request.getCandidateType() == CandidateType.Experienced) {
            entity.setTotalExperience(request.getTotalExperience());
            entity.setRelevantExperience(request.getRelevantExperience());
            entity.setNoticePeriod(request.getNoticePeriod());
            entity.setCurrentCtc(request.getCurrentCtc());
        } else {
            entity.setTotalExperience(null);
            entity.setRelevantExperience(null);
            entity.setNoticePeriod(null);
            entity.setCurrentCtc(null);
        }
    }

    private ConsultingJobApplicationResponse mapToResponse(ConsultingJobApplication entity) {
        ConsultingJobApplicationResponse dto = new ConsultingJobApplicationResponse();

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
        dto.setProfileSummary(entity.getProfileSummary());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
