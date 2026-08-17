package com.velinfotech.repository;

import com.velinfotech.model.ConsultingJobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsultingJobApplicationRepository
        extends JpaRepository<ConsultingJobApplication, Long> {

    List<ConsultingJobApplication> findAllByOrderByCreatedAtDesc();

    List<ConsultingJobApplication> findByJobIdOrderByCreatedAtDesc(Long jobId);

    Optional<ConsultingJobApplication> findByIdAndJobId(Long id, Long jobId);

    long countByJobId(Long jobId);

    void deleteByJobId(Long jobId);

    /**
     * All application counts in one query, so listing N job posts does not fire
     * N count queries.
     */
    @Query("SELECT a.jobId AS jobId, COUNT(a) AS total "
            + "FROM ConsultingJobApplication a GROUP BY a.jobId")
    List<JobApplicationCount> countGroupedByJobId();

    /** Projection for {@link #countGroupedByJobId()}. */
    interface JobApplicationCount {
        Long getJobId();

        long getTotal();
    }
}
