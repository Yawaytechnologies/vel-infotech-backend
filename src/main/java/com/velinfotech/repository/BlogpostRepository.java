package com.velinfotech.repository;

import com.velinfotech.model.Blogpost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogpostRepository extends JpaRepository<Blogpost, Long> {

    // Search by title (still useful for listing/filtering posts)
    Page<Blogpost> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    Optional<Blogpost> findBySlug(String slug);

    boolean existsBySlug(String slug);

    /** Posts written before the slug column existed. Used by the startup backfill. */
    List<Blogpost> findBySlugIsNullOrSlugEquals(String slug);
}
