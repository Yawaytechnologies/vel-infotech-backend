package com.velinfotech.repository;

import com.velinfotech.model.Registration;
import com.velinfotech.model.LearningMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Optional helpers (use in controller if you prefer DB-side filtering)
    List<Registration> findByModeOrderByCreatedAtDesc(LearningMode mode);

    List<Registration> findByCourseContainingIgnoreCaseOrderByCreatedAtDesc(String course);

    List<Registration> findByModeAndCourseContainingIgnoreCaseOrderByCreatedAtDesc(
            LearningMode mode, String course
    );
}
