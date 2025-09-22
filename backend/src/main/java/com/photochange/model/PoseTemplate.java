package com.photochange.model;

import java.util.List;
import java.util.Map;

/**
 * 姿势模板
 */
public class PoseTemplate {
    private String templateId;
    private String name;
    private String category;
    private String description;
    private String thumbnailUrl;
    private String previewUrl;
    private List<Keypoint> keypoints;
    private Map<String, List<List<Float>>> segments;
    private Float popularity;
    private Long createdAt;

    public PoseTemplate() {
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public List<Keypoint> getKeypoints() {
        return keypoints;
    }

    public void setKeypoints(List<Keypoint> keypoints) {
        this.keypoints = keypoints;
    }

    public Map<String, List<List<Float>>> getSegments() {
        return segments;
    }

    public void setSegments(Map<String, List<List<Float>>> segments) {
        this.segments = segments;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}