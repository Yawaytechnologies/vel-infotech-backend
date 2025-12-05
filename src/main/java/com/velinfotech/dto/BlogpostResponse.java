package com.velinfotech.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlogpostResponse {

    private Long id;
    private String title;
    private String content;
    private String excerpt;
    private String imageBase64;
}
