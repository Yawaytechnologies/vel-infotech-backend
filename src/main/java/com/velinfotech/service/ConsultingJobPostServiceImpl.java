package com.velinfotech.service;

import com.velinfotech.dto.ConsultingJobPostRequest;
import com.velinfotech.dto.ConsultingJobPostResponse;
import com.velinfotech.exception.ResourceNotFoundException;
import com.velinfotech.model.ConsultingJobPost;
import com.velinfotech.repository.ConsultingJobApplicationRepository;
import com.velinfotech.repository.ConsultingJobApplicationRepository.JobApplicationCount;
import com.velinfotech.repository.ConsultingJobPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsultingJobPostServiceImpl implements ConsultingJobPostService {

    private final ConsultingJobPostRepository jobPostRepository;
    private final ConsultingJobApplicationRepository applicationRepository;

    public ConsultingJobPostServiceImpl(ConsultingJobPostRepository jobPostRepository,
                                        ConsultingJobApplicationRepository applicationRepository) {
        this.jobPostRepository = jobPostRepository;
        this.applicationRepository = applicationRepository;
    }

    /* ===================== CREATE ===================== */

    @Override
    public ConsultingJobPostResponse createJobPost(ConsultingJobPostRequest request) {
        ConsultingJobPost entity = new ConsultingJobPost();
        applyRequest(entity, request);

        ConsultingJobPost saved = jobPostRepository.save(entity);

        // Brand new post, so it cannot have applications yet.
        return mapToResponse(saved, 0L);
    }

    /* ====================== READ ====================== */

    @Override
    @Transactional(readOnly = true)
    public List<ConsultingJobPostResponse> getAllJobPosts() {
        Map<Long, Long> countsByJobId = loadApplicationCounts();

        return jobPostRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> mapToResponse(post, countsByJobId.getOrDefault(post.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultingJobPostResponse getJobPostById(Long id) {
        ConsultingJobPost post = findOrThrow(id);
        return mapToResponse(post, applicationRepository.countByJobId(id));
    }

    /* ===================== UPDATE ===================== */

    @Override
    public ConsultingJobPostResponse updateJobPost(Long id, ConsultingJobPostRequest request) {
        ConsultingJobPost existing = findOrThrow(id);
        applyRequest(existing, request);

        ConsultingJobPost updated = jobPostRepository.save(existing);

        return mapToResponse(updated, applicationRepository.countByJobId(id));
    }

    /* ===================== DELETE ===================== */

    @Override
    public void deleteJobPost(Long id) {
        ConsultingJobPost existing = findOrThrow(id);

        // No JPA relationship between the two, so orphans have to be cleared by hand.
        applicationRepository.deleteByJobId(id);
        jobPostRepository.delete(existing);
    }

    /* ===================== HELPERS ===================== */

    private ConsultingJobPost findOrThrow(Long id) {
        return jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consulting job post not found with id: " + id));
    }

    private Map<Long, Long> loadApplicationCounts() {
        Map<Long, Long> counts = new HashMap<>();

        for (JobApplicationCount row : applicationRepository.countGroupedByJobId()) {
            counts.put(row.getJobId(), row.getTotal());
        }

        return counts;
    }

    private void applyRequest(ConsultingJobPost entity, ConsultingJobPostRequest request) {
        entity.setJobTitle(request.getJobTitle());
        entity.setDepartment(request.getDepartment());
        entity.setExperience(request.getExperience());
        entity.setLocation(request.getLocation());
        entity.setWorkMode(request.getWorkMode());
        entity.setSalaryRange(request.getSalaryRange());
        entity.setQualification(request.getQualification());
        entity.setJobDescription(request.getJobDescription());
        entity.setResponsibilities(request.getResponsibilities());
        entity.setSkills(request.getSkills());
    }

    private ConsultingJobPostResponse mapToResponse(ConsultingJobPost entity, long applicationCount) {
        ConsultingJobPostResponse dto = new ConsultingJobPostResponse();

        dto.setId(entity.getId());
        dto.setJobTitle(entity.getJobTitle());
        dto.setDepartment(entity.getDepartment());
        dto.setExperience(entity.getExperience());
        dto.setLocation(entity.getLocation());
        dto.setWorkMode(entity.getWorkMode());
        dto.setSalaryRange(entity.getSalaryRange());
        dto.setQualification(entity.getQualification());
        dto.setJobDescription(entity.getJobDescription());
        dto.setResponsibilities(entity.getResponsibilities());
        dto.setSkills(entity.getSkills());
        dto.setApplicationCount(applicationCount);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
