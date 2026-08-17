package com.velinfotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ConsultingJobPostRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title must be at most 150 characters")
    private String jobTitle;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must be at most 100 characters")
    private String department;

    @NotBlank(message = "Experience is required")
    @Size(max = 50, message = "Experience must be at most 50 characters")
    private String experience;

    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location must be at most 100 characters")
    private String location;

    @NotBlank(message = "Work mode is required")
    @Size(max = 20, message = "Work mode must be at most 20 characters")
    private String workMode;

    @NotBlank(message = "Salary range is required")
    @Size(max = 50, message = "Salary range must be at most 50 characters")
    private String salaryRange;

    @NotBlank(message = "Qualification is required")
    @Size(max = 150, message = "Qualification must be at most 150 characters")
    private String qualification;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    @NotBlank(message = "Responsibilities are required")
    private String responsibilities;

    @NotBlank(message = "Skills are required")
    private String skills;

    /* ===== Getters & Setters ===== */

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
