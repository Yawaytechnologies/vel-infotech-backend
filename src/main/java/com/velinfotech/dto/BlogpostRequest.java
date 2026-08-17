package com.velinfotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BlogpostRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    /**
     * Optional. Left blank, the slug is generated from the title on create and
     * preserved untouched on update. Supply a value only to deliberately change
     * a post's URL.
     */
    @Size(max = 120)
    private String slug;

    @Size(max = 60)
    private String category;

    @NotBlank
    private String content;

    @Size(max = 500)
    private String excerpt;

    private String imageBase64;

}
