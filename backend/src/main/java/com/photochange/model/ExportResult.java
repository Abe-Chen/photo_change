package com.photochange.model;

/**
 * 导出结果
 */
public class ExportResult {
    private String exportId;
    private String transformationId;
    private String status; // processing, completed, failed
    private String downloadUrl;
    private String format;
    private String quality;
    private Integer width;
    private Integer height;
    private Long size;
    private Long createdAt;
    private Long completedAt;
    private Long expiresAt;
    private String error;

    public ExportResult() {
    }

    public ExportResult(String exportId, String transformationId, String status, String downloadUrl,
                       String format, String quality, Integer width, Integer height, Long size,
                       Long createdAt, String error) {
        this.exportId = exportId;
        this.transformationId = transformationId;
        this.status = status;
        this.downloadUrl = downloadUrl;
        this.format = format;
        this.quality = quality;
        this.width = width;
        this.height = height;
        this.size = size;
        this.createdAt = createdAt;
        this.error = error;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    public String getTransformationId() {
        return transformationId;
    }

    public void setTransformationId(String transformationId) {
        this.transformationId = transformationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
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

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}