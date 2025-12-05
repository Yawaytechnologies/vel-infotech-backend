package com.velinfotech.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "job_application")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_type", nullable = false, length =20)
    private CandidateType candidateType;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 10)
    private String phone;

    @Column(nullable = false, length = 100)
    private String qualification;

    @Column(name = "passing_Year", nullable = false)
    private Integer passingYear;

    @Column(name = "applying_for_position", length = 100, nullable = false)
    private String applyingForPosition;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "total_experience")
    private Double totalExperience;

    @Column(name = "relevant_experience")
    private Double relevantExperience;

    @Column(name = "notice_period", length = 50)
    private String noticePeriod;

    @Column(name = "current_ctc")
    private Double currentCtc;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /* ----------- Getters & Setters -----------*/

    public void setId(Long id) {this.id = id; }

    public void setJobId(Long jobId) {this.jobId = jobId; }

    public void setCandidateType(CandidateType candidateType) {this.candidateType = candidateType; }

    public void setName(String name) {this.name = name; }

    public void setEmail(String email) {this.email = email; }

    public void setPhone(String phone) {this.phone = phone; }

    public void setQualification(String qualification) {this.qualification = qualification; }

    public void setPassingYear(Integer passingYear) {this.passingYear = passingYear; }

    public void setApplyingForPosition(String applyingForPosition) {this.applyingForPosition = applyingForPosition; }

    public void setSkills(String skills) {this.skills = skills; }

    public void setTotalExperience(Double totalExperience) {this.totalExperience = totalExperience; }

    public void setRelevantExperience(Double relevantExperience) {this.relevantExperience = relevantExperience; }

    public void setNoticePeriod(String noticePeriod) {this.noticePeriod = noticePeriod; }

    public void setCurrentCtc(Double currentCtc) {this.currentCtc = currentCtc; }

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt; }


    public void setJobPost(JobPost jobPost) {
    }
}
