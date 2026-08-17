package com.velinfotech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blogpost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * URL segment for /blog/{slug}. Generated from the title on create and then
     * left alone, so editing a title does not silently break indexed links.
     *
     * Nullable at the database level on purpose: ddl-auto=update has to be able to
     * add this column to a table that already holds rows. BlogpostSlugBackfill
     * fills those in on the next boot.
     */
    @Size(max = 120)
    @Column(unique = true, length = 120)
    private String slug;

    /** Drives the sidebar's Categories box. Defaults to "Articles". */
    @Size(max = 60)
    @Column(length = 60)
    private String category;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String excerpt;

    @Lob
    @Column(name = "featured_image_url", columnDefinition = "TEXT")
    private String featuredImageUrl;

    // Null for posts that predate this column; see BlogpostSlugBackfill.
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (this.createdAt == null) this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
