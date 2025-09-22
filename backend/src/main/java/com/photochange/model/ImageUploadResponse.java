package com.photochange.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 图片上传响应
 */
public class ImageUploadResponse {
    private String imageId;
    private String url;
    private String message;

    public ImageUploadResponse(String imageId, String url, String message) {
        this.imageId = imageId;
        this.url = url;
        this.message = message;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}