package com.velinfotech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LearningMode mode; // CLASS_ROOM or ONLINE

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 120)
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, length = 160)
    private String email;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile must be 10 digits")
    @Column(nullable = false, length = 10)
    private String mobile;

    @NotBlank(message = "Course is required")
    @Column(nullable = false, length = 160)
    private String course;

    @Column(columnDefinition = "text")
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
