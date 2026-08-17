package com.velinfotech.repository;

import com.velinfotech.model.ConsultingJobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultingJobPostRepository extends JpaRepository<ConsultingJobPost, Long> {

    List<ConsultingJobPost> findAllByOrderByCreatedAtDesc();
}
