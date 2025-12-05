package com.velinfotech.repository;

import com.velinfotech.model.Blogpost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogpostRepository extends JpaRepository<Blogpost, Long> {

    // Search by title (still useful for listing/filtering posts)
    Page<Blogpost> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );
}
