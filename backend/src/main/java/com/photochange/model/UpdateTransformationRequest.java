package com.photochange.model;

import java.util.List;

/**
 * 更新变换请求
 */
public class UpdateTransformationRequest {
    private List<Keypoint> customKeypoints;

    public List<Keypoint> getCustomKeypoints() {
        return customKeypoints;
    }

    public void setCustomKeypoints(List<Keypoint> customKeypoints) {
        this.customKeypoints = customKeypoints;
    }
}