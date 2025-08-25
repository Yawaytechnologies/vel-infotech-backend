//package com.velinfotech.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public  class Internship {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "Full Name is required")
//    @Size(max = 120, message = "Full Name must not exceed 120 characters")
//    @Column(nullable = false, length = 120)
//    private String fullName;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    @Size(max = 160, message = "Email must not exceed 160 characters")
//    @Column(nullable = false, length = 160, unique = true)
//    private String email;
//
//    @NotBlank(message = "Phone number is required")
//    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
//    @Column(nullable = false, length = 10)
//    private String phoneNumber;
//
//    @NotBlank(message = "Course / Domain is required")
//    @Size(max = 160, message = "Course must not exceed 160 characters")
//    @Column(nullable = false, length = 160)
//    private String course;
//
//    @Size(max = 500, message = "Message must not exceed 500 characters")
//    @Column(columnDefinition = "TEXT")
//    private String message;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//
//}