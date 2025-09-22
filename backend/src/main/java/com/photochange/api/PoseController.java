package com.photochange.api;

import com.photochange.model.*;
import com.photochange.service.ExportService;
import com.photochange.service.PoseDetectionService;
import com.photochange.service.PoseTransformationService;
import com.photochange.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 人物姿势处理API控制器
 * 提供图片上传、姿势检测、姿势变换等功能
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PoseController {

    @Autowired
    private PoseDetectionService poseDetectionService;

    @Autowired
    private PoseTransformationService poseTransformationService;

    @Autowired
    private ImageStorageService imageStorageService;
    
    @Autowired
    private ExportService exportService;

    /**
     * 上传图片
     * @param file 上传的图片文件
     * @return 上传结果，包含图片ID和URL
     */
    @PostMapping("/images/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "name", required = false) String name) {
        
        try {
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(new ImageUploadResponse(
                        null, null, "不支持的文件类型，请上传图片文件"));
            }
            
            // 验证文件大小
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                return ResponseEntity.badRequest().body(new ImageUploadResponse(
                        null, null, "文件过大，最大支持10MB"));
            }
            
            // 生成图片ID
            String imageId = "img_" + UUID.randomUUID().toString().replace("-", "");
            
            // 存储图片
            String imageUrl = imageStorageService.storeImage(file, imageId);
            
            // 构建响应
            ImageUploadResponse response = new ImageUploadResponse(
                    imageId,
                    imageUrl,
                    "图片上传成功"
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImageUploadResponse(null, null, "图片上传失败: " + e.getMessage()));
        }
    }

    /**
     * 检测图片中的人物姿势
     * @param request 包含图片ID的请求
     * @return 检测任务ID和状态
     */
    @PostMapping("/poses/detect")
    public ResponseEntity<PoseDetectionResponse> detectPose(
            @RequestBody PoseDetectionRequest request) {
        
        try {
            // 验证图片ID
            String imageId = request.getImageId();
            if (imageId == null || imageId.isEmpty()) {
                return ResponseEntity.badRequest().body(new PoseDetectionResponse(
                        null, null, "processing", "图片ID不能为空", null));
            }
            
            // 检查图片是否存在
            if (!imageStorageService.imageExists(imageId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PoseDetectionResponse(
                        null, imageId, "failed", "图片不存在", null));
            }
            
            // 创建检测任务
            String detectionId = "det_" + UUID.randomUUID().toString().replace("-", "");
            
            // 异步执行姿势检测
            poseDetectionService.detectPoseAsync(imageId, detectionId);
            
            // 构建响应
            PoseDetectionResponse response = new PoseDetectionResponse(
                    detectionId,
                    imageId,
                    "processing",
                    "姿势检测任务已创建，正在处理中",
                    5 // 预估处理时间（秒）
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PoseDetectionResponse(null, request.getImageId(), "failed", 
                            "姿势检测失败: " + e.getMessage(), null));
        }
    }

    /**
     * 获取姿势检测结果
     * @param detectionId 检测任务ID
     * @return 检测结果，包含关键点信息
     */
    @GetMapping("/poses/detect/{detectionId}")
    public ResponseEntity<PoseDetectionResult> getDetectionResult(
            @PathVariable String detectionId) {
        
        try {
            // 获取检测结果
            PoseDetectionResult result = poseDetectionService.getDetectionResult(detectionId);
            
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new PoseDetectionResult(detectionId, null, "not_found", null, null, null, null, null));
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PoseDetectionResult(detectionId, null, "error", null, null, null, null, 
                            "获取检测结果失败: " + e.getMessage()));
        }
    }

    /**
     * 获取姿势模板列表
     * @param category 模板类别（可选）
     * @param page 页码（可选，默认1）
     * @param limit 每页数量（可选，默认20）
     * @return 模板列表
     */
    @GetMapping("/templates")
    public ResponseEntity<TemplateListResponse> getTemplates(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        
        try {
            // 验证参数
            if (limit > 50) {
                limit = 50; // 限制最大返回数量
            }
            
            // 获取模板列表
            List<PoseTemplate> templates = poseTransformationService.getTemplates(category, page, limit);
            int total = poseTransformationService.countTemplates(category);
            int pages = (int) Math.ceil((double) total / limit);
            
            // 构建响应
            TemplateListResponse response = new TemplateListResponse(
                    templates,
                    total,
                    page,
                    limit,
                    pages
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TemplateListResponse(null, 0, page, limit, 0));
        }
    }

    /**
     * 获取姿势模板详情
     * @param templateId 模板ID
     * @return 模板详情
     */
    @GetMapping("/templates/{templateId}")
    public ResponseEntity<PoseTemplate> getTemplateDetail(
            @PathVariable String templateId) {
        
        try {
            // 获取模板详情
            PoseTemplate template = poseTransformationService.getTemplateById(templateId);
            
            if (template == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            
            return ResponseEntity.ok(template);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 创建姿势变换任务
     * @param request 变换请求，包含图片ID、模板ID和自定义关键点
     * @return 变换任务ID和状态
     */
    @PostMapping("/transformations")
    public ResponseEntity<TransformationResponse> createTransformation(
            @RequestBody TransformationRequest request) {
        
        try {
            // 验证参数
            if (request.getImageId() == null || request.getImageId().isEmpty()) {
                return ResponseEntity.badRequest().body(new TransformationResponse(
                        null, null, null, "failed", "图片ID不能为空", null, null));
            }
            
            if (request.getTemplateId() == null || request.getTemplateId().isEmpty()) {
                return ResponseEntity.badRequest().body(new TransformationResponse(
                        null, request.getImageId(), null, "failed", "模板ID不能为空", null, null));
            }
            
            // 检查图片和模板是否存在
            if (!imageStorageService.imageExists(request.getImageId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TransformationResponse(
                        null, request.getImageId(), request.getTemplateId(), "failed", "图片不存在", null, null));
            }
            
            if (poseTransformationService.getTemplateById(request.getTemplateId()) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TransformationResponse(
                        null, request.getImageId(), request.getTemplateId(), "failed", "模板不存在", null, null));
            }
            
            // 创建变换任务
            String transformationId = "trans_" + UUID.randomUUID().toString().replace("-", "");
            
            // 异步执行姿势变换
            poseTransformationService.transformPoseAsync(
                    transformationId,
                    request.getImageId(),
                    request.getTemplateId(),
                    request.getCustomKeypoints()
            );
            
            // 构建响应
            TransformationResponse response = new TransformationResponse(
                    transformationId,
                    request.getImageId(),
                    request.getTemplateId(),
                    "processing",
                    "姿势变换任务已创建，正在处理中",
                    15, // 预估处理时间（秒）
                    System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransformationResponse(null, request.getImageId(), request.getTemplateId(), 
                            "failed", "姿势变换失败: " + e.getMessage(), null, null));
        }
    }

    /**
     * 获取变换任务状态
     * @param transformationId 变换任务ID
     * @return 变换任务状态和结果
     */
    @GetMapping("/transformations/{transformationId}")
    public ResponseEntity<TransformationResult> getTransformationResult(
            @PathVariable String transformationId) {
        
        try {
            // 获取变换结果
            TransformationResult result = poseTransformationService.getTransformationResult(transformationId);
            
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new TransformationResult(transformationId, null, null, "not_found", 
                                null, null, null, null, null));
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransformationResult(transformationId, null, null, "error", 
                            null, null, null, null, "获取变换结果失败: " + e.getMessage()));
        }
    }

    /**
     * 更新变换任务（调整关键点）
     * @param transformationId 变换任务ID
     * @param request 更新请求，包含自定义关键点
     * @return 更新后的状态
     */
    @PutMapping("/transformations/{transformationId}")
    public ResponseEntity<Map<String, Object>> updateTransformation(
            @PathVariable String transformationId,
            @RequestBody UpdateTransformationRequest request) {
        
        try {
            // 验证参数
            if (request.getCustomKeypoints() == null || request.getCustomKeypoints().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "自定义关键点不能为空",
                        "transformation_id", transformationId,
                        "status", "failed"
                ));
            }
            
            // 检查变换任务是否存在
            TransformationResult existingResult = poseTransformationService.getTransformationResult(transformationId);
            if (existingResult == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "变换任务不存在",
                        "transformation_id", transformationId,
                        "status", "not_found"
                ));
            }
            
            // 更新变换任务
            poseTransformationService.updateTransformation(transformationId, request.getCustomKeypoints());
            
            // 构建响应
            return ResponseEntity.ok(Map.of(
                    "transformation_id", transformationId,
                    "status", "processing",
                    "estimated_time", 10
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "更新变换任务失败: " + e.getMessage(),
                    "transformation_id", transformationId,
                    "status", "error"
            ));
        }
    }

    /**
     * 取消变换任务
     * @param transformationId 变换任务ID
     * @return 取消结果
     */
    @DeleteMapping("/transformations/{transformationId}")
    public ResponseEntity<Map<String, Object>> cancelTransformation(
            @PathVariable String transformationId) {
        
        try {
            // 检查变换任务是否存在
            TransformationResult existingResult = poseTransformationService.getTransformationResult(transformationId);
            if (existingResult == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "变换任务不存在",
                        "success", false
                ));
            }
            
            // 取消变换任务
            boolean cancelled = poseTransformationService.cancelTransformation(transformationId);
            
            if (cancelled) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "变换任务已成功取消"
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "变换任务无法取消，可能已完成或失败"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "取消变换任务失败: " + e.getMessage(),
                    "success", false
            ));
        }
    }

    /**
     * 导出结果图片
     * @param request 导出请求，包含变换任务ID和导出选项
     * @return 导出任务ID和状态
     */
    @PostMapping("/exports")
    public ResponseEntity<ExportResponse> exportResult(
            @RequestBody ExportRequest request) {
        
        try {
            // 验证参数
            if (request.getTransformationId() == null || request.getTransformationId().isEmpty()) {
                return ResponseEntity.badRequest().body(new ExportResponse(
                        null, null, "failed", "变换任务ID不能为空", null));
            }
            
            // 调用导出服务创建导出任务
            ExportResponse response = exportService.createExport(request);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ExportResponse(
                    null, request.getTransformationId(), "failed", e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ExportResponse(
                    null, request.getTransformationId(), "failed", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ExportResponse(null, request.getTransformationId(), "failed", 
                            "导出失败: " + e.getMessage(), null));
        }
    }

    /**
     * 获取导出状态
     * @param exportId 导出任务ID
     * @return 导出状态和下载链接
     */
    @GetMapping("/exports/{exportId}")
    public ResponseEntity<ExportResult> getExportResult(
            @PathVariable String exportId) {
        
        try {
            // 获取导出结果
            ExportResult result = exportService.getExportResult(exportId);
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExportResult(exportId, null, "not_found", e.getMessage(), 
                            null, null, null, null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ExportResult(exportId, null, "error", null, null, null, 
                            null, null, null, null, "获取导出结果失败: " + e.getMessage()));
        }
    }
    
    /**
     * 取消导出任务
     * @param exportId 导出任务ID
     * @return 取消结果
     */
    @DeleteMapping("/exports/{exportId}")
    public ResponseEntity<Map<String, Object>> cancelExport(
            @PathVariable String exportId) {
        
        try {
            boolean cancelled = exportService.cancelExport(exportId);
            
            if (cancelled) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "导出任务已取消"
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "导出任务无法取消，可能已完成或不存在"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "取消导出任务失败: " + e.getMessage()
                    ));
        }
    }
    
    /**
     * 获取导出文件下载链接
     * @param exportId 导出任务ID
     * @return 下载链接
     */
    @GetMapping("/exports/{exportId}/download")
    public ResponseEntity<Map<String, Object>> getDownloadUrl(
            @PathVariable String exportId) {
        
        try {
            String downloadUrl = exportService.getDownloadUrl(exportId);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "download_url", downloadUrl
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "获取下载链接失败: " + e.getMessage()
                    ));
        }
    }
}