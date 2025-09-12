package com.velinfotech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedbacks", indexes = {
        @Index(name = "idx_feedbacks_created_at", columnList = "created_at")
})
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional
    @Column(length = 120)
    private String name;

    // Optional but validated if present
    @Email
    @Column(length = 160)
    private String email;

    // Required
    @NotBlank(message = "Course title is required")
    @Column(name = "course_title", nullable = false, length = 160)
    private String courseTitle;

    // Required
    @NotBlank(message = "Trainer name is required")
    @Column(name = "trainer_name", nullable = false, length = 160)
    private String trainerName;

    // Required (1..5)
    @NotNull(message = "Course rating is required")
    @Min(value = 1) @Max(value = 5)
    @Column(name = "course_rating", nullable = false)
    private Integer courseRating;

    // Required (1..5)
    @NotNull(message = "Trainer rating is required")
    @Min(value = 1) @Max(value = 5)
    @Column(name = "trainer_rating", nullable = false)
    private Integer trainerRating;

    // Required: radio Yes/No → true/false
    @NotNull(message = "Recommendation is required")
    @Column(nullable = false)
    private Boolean recommend;

    // Optional
    @Column(length = 2000)
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
