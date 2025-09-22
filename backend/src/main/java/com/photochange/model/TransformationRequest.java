package com.photochange.model;

import java.util.List;

/**
 * 姿势变换请求
 */
public class TransformationRequest {
    private String imageId;
    private String templateId;
    private List<Keypoint> customKeypoints;

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

    public List<Keypoint> getCustomKeypoints() {
        return customKeypoints;
    }

    public void setCustomKeypoints(List<Keypoint> customKeypoints) {
        this.customKeypoints = customKeypoints;
    }
}