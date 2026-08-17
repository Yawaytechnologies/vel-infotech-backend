package com.velinfotech.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BlogpostResponse {

    private Long id;
    private String title;
    private String slug;
    private String category;
    private String content;
    private String excerpt;
    private String imageBase64;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
