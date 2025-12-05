// src/main/java/com/velinfotech/repository/JobApplicationRepository.java
package com.velinfotech.repository;

import com.velinfotech.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Uses the jobId column in your entity
    List<JobApplication> findByJobId(Long jobId);

    Optional<JobApplication> findByIdAndJobId(Long id, Long jobId);
}
