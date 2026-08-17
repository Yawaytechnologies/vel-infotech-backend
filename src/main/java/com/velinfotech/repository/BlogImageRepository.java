package com.velinfotech.repository;

import com.velinfotech.model.BlogImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogImageRepository extends JpaRepository<BlogImage, Long> {

    Optional<BlogImage> findBySha256(String sha256);
}
