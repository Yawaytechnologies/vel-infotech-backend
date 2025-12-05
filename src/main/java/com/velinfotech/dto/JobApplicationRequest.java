// src/main/java/com/velinfotech/dto/JobApplicationRequest.java
package com.velinfotech.dto;

import com.velinfotech.model.CandidateType;
import jakarta.validation.constraints.*;

public class JobApplicationRequest {

    @NotNull(message = "Candidate type is required")
    private CandidateType candidateType;

    @NotBlank(message = "Name is required")
    @Size(max = 80, message = "Name must be at most 80 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 120, message = "Email must be at most 120 characters")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone must be exactly 10 digits")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must contain only digits")
    private String phone;

    @NotBlank(message = "Qualification is required")
    @Size(max = 100, message = "Qualification must be at most 100 characters")
    private String qualification;

    @NotNull(message = "Passing year is required")
    @Min(value = 1900, message = "Passing year must be a valid year")
    @Max(value = 2100, message = "Passing year must be a valid year")
    private Integer passingYear;

    @NotBlank(message = "Applying for position is required")
    @Size(max = 100, message = "Applying for position must be at most 100 characters")
    private String applyingForPosition;

    // Optional text field
    private String skills;

    // For Fresher this can be 0 or null
    @PositiveOrZero(message = "Total experience must be zero or positive")
    private Double totalExperience;

    @PositiveOrZero(message = "Relevant experience must be zero or positive")
    private Double relevantExperience;

    @Size(max = 50, message = "Notice period must be at most 50 characters")
    private String noticePeriod;

    @PositiveOrZero(message = "Current CTC must be zero or positive")
    private Double currentCtc;

    /* ===== Getters & Setters ===== */

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
}
