package com.photochange.model;

/**
 * 姿势变换响应
 */
public class TransformationResponse {
    private String transformationId;
    private String imageId;
    private String templateId;
    private String status; // processing, completed, failed
    private String message;
    private Integer estimatedTime; // 预估处理时间（秒）
    private Long createdAt;

    public TransformationResponse() {
    }

    public TransformationResponse(String transformationId, String imageId, String templateId, String status,
                                 String message, Integer estimatedTime, Long createdAt) {
        this.transformationId = transformationId;
        this.imageId = imageId;
        this.templateId = templateId;
        this.status = status;
        this.message = message;
        this.estimatedTime = estimatedTime;
        this.createdAt = createdAt;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}