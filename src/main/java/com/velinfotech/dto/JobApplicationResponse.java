// src/main/java/com/velinfotech/dto/JobApplicationResponse.java
package com.velinfotech.dto;

import com.velinfotech.model.CandidateType;

import java.time.LocalDateTime;

public class JobApplicationResponse {

    private Long id;

    private Long jobId;

    private CandidateType candidateType;

    private String name;

    private String email;

    private String phone;

    private String qualification;

    private Integer passingYear;

    private String applyingForPosition;

    private String skills;

    private Double totalExperience;

    private Double relevantExperience;

    private String noticePeriod;

    private Double currentCtc;

    private LocalDateTime createdAt;

    /* ===== Getters & Setters ===== */

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
