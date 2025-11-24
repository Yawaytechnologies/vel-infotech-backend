package com.velinfotech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "blog_posts")
public class Blogpost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Core content
    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 500)
    private String excerpt;

    @Column(length = 255)
    private String featuredImageUrl;

    // no lifecycle hooks, no extra meta/SEO/status fields
}
