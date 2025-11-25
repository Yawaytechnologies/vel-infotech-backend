package com.velinfotech.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class BlogpostResponse {

    private Long id;
    private String title;
    private String content;
    private String excerpt;
    private String featuredImageUrl;

    // Getters & setters


}
