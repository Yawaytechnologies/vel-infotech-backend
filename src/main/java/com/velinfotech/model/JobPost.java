package com.velinfotech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posts")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Experience (e.g., "0-1 years", "2-4 years")
    @Column(nullable = false)
    private String experience;

    @Column(name = "job_description", nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String responsibilities;

    @Column(name = "salary_range", nullable = false)
    private String salaryRange;

    // You can store as comma-separated string: "Java, Spring Boot, React"
    @Column(nullable = false)
    private String skills;

    @Column(name = "work_mode", nullable = false)
    private String workMode; // e.g., "On-site", "Remote", "Hybrid"

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ---------- Constructors ----------

    public JobPost() {
    }

    public JobPost(String experience, String jobDescription, String jobTitle,
                   String department, String location, String qualification,
                   String responsibilities, String salaryRange, String skills,
                   String workMode) {
        this.experience = experience;
        this.jobDescription = jobDescription;
        this.jobTitle = jobTitle;
        this.department = department;
        this.location = location;
        this.qualification = qualification;
        this.responsibilities = responsibilities;
        this.salaryRange = salaryRange;
        this.skills = skills;
        this.workMode = workMode;
    }

    // ---------- Lifecycle hooks ----------

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
