package com.photochange.model;

/**
 * 导出响应
 */
public class ExportResponse {
    private String exportId;
    private String transformationId;
    private String status; // processing, completed, failed
    private String message;
    private Integer estimatedTime; // 预估处理时间（秒）

    public ExportResponse() {
    }

    public ExportResponse(String exportId, String transformationId, String status, String message, Integer estimatedTime) {
        this.exportId = exportId;
        this.transformationId = transformationId;
        this.status = status;
        this.message = message;
        this.estimatedTime = estimatedTime;
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
}