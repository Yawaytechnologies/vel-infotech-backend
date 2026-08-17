package com.velinfotech.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A candidate application against a {@link ConsultingJobPost}.
 *
 * Like {@link JobApplication}, the parent is referenced by a bare {@code jobId}
 * column rather than a JPA relationship, so deleting a job post has to clean up
 * its applications explicitly.
 */
@Entity
@Table(name = "consulting_job_applications")
public class ConsultingJobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_type", nullable = false, length = 20)
    private CandidateType candidateType;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 10)
    private String phone;

    @Column(nullable = false, length = 100)
    private String qualification;

    @Column(name = "passing_year", nullable = false)
    private Integer passingYear;

    @Column(name = "applying_for_position", nullable = false, length = 100)
    private String applyingForPosition;

    @Column(columnDefinition = "TEXT")
    private String skills;

    // The next four are only collected for Experienced candidates.
    @Column(name = "total_experience")
    private Double totalExperience;

    @Column(name = "relevant_experience")
    private Double relevantExperience;

    @Column(name = "notice_period", length = 50)
    private String noticePeriod;

    @Column(name = "current_ctc")
    private Double currentCtc;

    // Free-text "Short Message / Profile Summary" from the public apply form.
    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ---------- Lifecycle hooks ----------

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public CandidateType getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(CandidateType candidateType) {
        this.candidateType = candidateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(Integer passingYear) {
        this.passingYear = passingYear;
    }

    public String getApplyingForPosition() {
        return applyingForPosition;
    }

    public void setApplyingForPosition(String applyingForPosition) {
        this.applyingForPosition = applyingForPosition;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Double getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Double totalExperience) {
        this.totalExperience = totalExperience;
    }

    public Double getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(Double relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    public String getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(String noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public Double getCurrentCtc() {
        return currentCtc;
    }

    public void setCurrentCtc(Double currentCtc) {
        this.currentCtc = currentCtc;
    }

    public String getProfileSummary() {
        return profileSummary;
    }

    public void setProfileSummary(String profileSummary) {
        this.profileSummary = profileSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
