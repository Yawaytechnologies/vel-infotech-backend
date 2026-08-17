package com.velinfotech.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Bytes for a blog image, served from /api/images/{id} instead of being inlined
 * into the page as a base64 data URI.
 *
 * Deliberately a plain byte[] mapped to bytea rather than @Lob: @Lob on PostgreSQL
 * goes through large-object storage, which cannot be read outside a transaction and
 * already caused a production failure on this codebase.
 */
@Entity
@Table(name = "blog_images")
public class BlogImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "data", nullable = false, columnDefinition = "bytea")
    private byte[] data;

    /** Content hash, so re-uploading the same picture reuses the existing row. */
    @Column(name = "sha256", nullable = false, unique = true, length = 64)
    private String sha256;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    // Null when the dimensions could not be read; lets the page reserve space.
    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
