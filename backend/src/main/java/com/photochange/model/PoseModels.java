package com.photochange.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

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

/**
 * 姿势检测请求
 */
public class PoseDetectionRequest {
    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}

/**
 * 姿势检测响应
 */
public class PoseDetectionResponse {
    private String detectionId;
    private String imageId;
    private String status; // processing, completed, failed
    private String message;
    private Integer estimatedTime; // 预估处理时间（秒）

    public PoseDetectionResponse(String detectionId, String imageId, String status, String message, Integer estimatedTime) {
        this.detectionId = detectionId;
        this.imageId = imageId;
        this.status = status;
        this.message = message;
        this.estimatedTime = estimatedTime;
    }

    public String getDetectionId() {
        return detectionId;
    }

    public void setDetectionId(String detectionId) {
        this.detectionId = detectionId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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

/**
 * 关键点
 */
public class Keypoint {
    private String id;
    private float x;
    private float y;
    private float confidence;

    public Keypoint() {
    }

    public Keypoint(String id, float x, float y, float confidence) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.confidence = confidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }
}

/**
 * 姿势检测结果
 */
public class PoseDetectionResult {
    private String detectionId;
    private String imageId;
    private String status; // processing, completed, failed
    private List<Keypoint> keypoints;
    private Map<String, List<List<Float>>> segments;
    private Float confidence;
    private Long createdAt;
    private Long completedAt;
    private String error;

    public PoseDetectionResult() {
    }

    public PoseDetectionResult(String detectionId, String imageId, String status, List<Keypoint> keypoints,
                              Map<String, List<List<Float>>> segments, Float confidence, Long createdAt, String error) {
        this.detectionId = detectionId;
        this.imageId = imageId;
        this.status = status;
        this.keypoints = keypoints;
        this.segments = segments;
        this.confidence = confidence;
        this.createdAt = createdAt;
        this.error = error;
    }

    public String getDetectionId() {
        return detectionId;
    }

    public void setDetectionId(String detectionId) {
        this.detectionId = detectionId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
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

/**
 * 模板列表响应
 */
public class TemplateListResponse {
    private List<PoseTemplate> templates;
    private int total;
    private int page;
    private int limit;
    private int pages;

    public TemplateListResponse() {
    }

    public TemplateListResponse(List<PoseTemplate> templates, int total, int page, int limit, int pages) {
        this.templates = templates;
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.pages = pages;
    }

    public List<PoseTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<PoseTemplate> templates) {
        this.templates = templates;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

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

/**
 * 导出请求
 */
public class ExportRequest {
    private String transformationId;
    private String format; // jpg, png
    private String quality; // low, medium, high
    private Integer width;
    private Integer height;

    public String getTransformationId() {
        return transformationId;
    }

    public void setTransformationId(String transformationId) {
        this.transformationId = transformationId;
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
}

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