package com.velinfotech.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A consulting career opening, managed from the admin console and shown on
 * /consult/careers. Deliberately separate from {@link JobPost} so the training
 * job board and the consulting job board never bleed into each other.
 */
@Entity
@Table(name = "consulting_job_posts")
public class ConsultingJobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String department;

    // Experience range, e.g. "1-3 Years"
    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String location;

    @Column(name = "work_mode", nullable = false)
    private String workMode; // "On-site", "Hybrid", "Remote"

    @Column(name = "salary_range", nullable = false)
    private String salaryRange;

    @Column(nullable = false)
    private String qualification;

    @Column(name = "job_description", nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    // Comma-separated: "Candidate sourcing, Interview coordination"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String responsibilities;

    // Comma-separated: "Recruitment, Communication, Client Handling"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String skills;

    // Drives the "Posted: dd/MM/yyyy" badge on the public careers cards.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ---------- Lifecycle hooks ----------

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
