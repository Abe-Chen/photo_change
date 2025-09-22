package com.photochange.model;

/**
 * 姿势变换结果
 */
public class TransformationResult {
    private String transformationId;
    private String imageId;
    private String templateId;
    private String status; // processing, completed, failed
    private String resultUrl;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Long createdAt;
    private Long completedAt;
    private String error;

    public TransformationResult() {
    }

    public TransformationResult(String transformationId, String imageId, String templateId, String status,
                               String resultUrl, String thumbnailUrl, Integer width, Integer height, String error) {
        this.transformationId = transformationId;
        this.imageId = imageId;
        this.templateId = templateId;
        this.status = status;
        this.resultUrl = resultUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.width = width;
        this.height = height;
        this.error = error;
    }

    public String getTransformationId() {
        return transformationId;
    }

    public void setTransformationId(String transformationId) {
        this.transformationId = transformationId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}