package com.photochange.service.impl;

import com.photochange.model.ExportRequest;
import com.photochange.model.ExportResponse;
import com.photochange.model.ExportResult;
import com.photochange.model.TransformationResult;
import com.photochange.service.ExportService;
import com.photochange.service.ImageStorageService;
import com.photochange.service.PoseTransformationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 导出服务实现类
 */
@Service
public class ExportServiceImpl implements ExportService {

    private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    private final Map<String, ExportResult> exportResults = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<ExportResult>> runningTasks = new ConcurrentHashMap<>();

    @Autowired
    private ImageStorageService imageStorageService;
    
    @Autowired
    private PoseTransformationService transformationService;

    /**
     * 创建导出任务
     * @param request 导出请求
     * @return 导出响应，包含任务ID和状态
     */
    @Override
    public ExportResponse createExport(ExportRequest request) {
        logger.info("创建导出任务，变换ID: {}, 格式: {}", request.getTransformationId(), request.getFormat());

        // 验证变换任务是否存在且已完成
        TransformationResult transformationResult = transformationService.getTransformationResult(request.getTransformationId());
        if (transformationResult == null) {
            logger.error("变换任务不存在，变换ID: {}", request.getTransformationId());
            throw new IllegalArgumentException("变换任务不存在");
        }

        if (!"completed".equals(transformationResult.getStatus())) {
            logger.error("变换任务未完成，变换ID: {}, 状态: {}", request.getTransformationId(), transformationResult.getStatus());
            throw new IllegalStateException("变换任务未完成，无法导出");
        }

        // 生成导出任务ID
        String exportId = UUID.randomUUID().toString();

        // 创建初始导出结果
        ExportResult initialResult = new ExportResult(
                exportId,
                request.getTransformationId(),
                "processing",
                null,
                request.getFormat() != null ? request.getFormat() : "jpg",
                request.getQuality() != null ? request.getQuality() : "high",
                request.getWidth(),
                request.getHeight(),
                null,
                Instant.now().toEpochMilli(),
                null
        );
        exportResults.put(exportId, initialResult);

        // 异步处理导出任务
        CompletableFuture<ExportResult> future = processExport(
                request.getTransformationId(),
                request.getFormat(),
                request.getQuality(),
                request.getWidth(),
                request.getHeight(),
                exportId
        );
        runningTasks.put(exportId, future);

        // 返回响应
        return new ExportResponse(
                exportId,
                request.getTransformationId(),
                "processing",
                "导出任务已创建，正在处理中",
                5 // 预估处理时间（秒）
        );
    }

    /**
     * 获取导出结果
     * @param exportId 导出任务ID
     * @return 导出结果，包含下载URL
     */
    @Override
    public ExportResult getExportResult(String exportId) {
        ExportResult result = exportResults.get(exportId);
        if (result == null) {
            logger.error("导出结果不存在，导出ID: {}", exportId);
            throw new IllegalArgumentException("导出结果不存在");
        }
        return result;
    }

    /**
     * 异步处理导出任务
     * @param transformationId 变换任务ID
     * @param format 导出格式
     * @param quality 导出质量
     * @param width 宽度（可选）
     * @param height 高度（可选）
     * @param exportId 导出任务ID
     * @return 异步任务
     */
    @Override
    @Async
    public CompletableFuture<ExportResult> processExport(String transformationId, String format, 
                                                      String quality, Integer width, Integer height, String exportId) {
        logger.info("开始处理导出任务，变换ID: {}, 导出ID: {}", transformationId, exportId);

        try {
            // 调用图片存储服务进行导出处理
            imageStorageService.exportImageAsync(exportId, transformationId, format, quality, width, height);
            
            // 等待导出完成
            Thread.sleep(3000); // 模拟处理时间
            
            // 获取导出结果
            ExportResult result = imageStorageService.getExportResult(exportId);
            if (result == null) {
                throw new IllegalStateException("导出处理失败，无法获取结果");
            }
            
            // 更新导出结果
            exportResults.put(exportId, result);
            logger.info("导出任务完成，导出ID: {}", exportId);

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            logger.error("导出任务失败，导出ID: {}, 错误: {}", exportId, e.getMessage(), e);

            // 更新导出结果为失败状态
            ExportResult result = exportResults.get(exportId);
            result.setStatus("failed");
            result.setError(e.getMessage());
            result.setCompletedAt(Instant.now().toEpochMilli());

            exportResults.put(exportId, result);

            CompletableFuture<ExportResult> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        } finally {
            runningTasks.remove(exportId);
        }
    }

    /**
     * 取消导出任务
     * @param exportId 导出任务ID
     * @return 是否成功取消
     */
    @Override
    public boolean cancelExport(String exportId) {
        logger.info("取消导出任务，导出ID: {}", exportId);

        CompletableFuture<ExportResult> future = runningTasks.get(exportId);
        if (future != null && !future.isDone()) {
            future.cancel(true);
            runningTasks.remove(exportId);

            // 更新导出结果状态
            ExportResult result = exportResults.get(exportId);
            if (result != null) {
                result.setStatus("cancelled");
                result.setCompletedAt(Instant.now().toEpochMilli());
                exportResults.put(exportId, result);
            }

            return true;
        }

        return false;
    }

    /**
     * 检查导出任务状态
     * @param exportId 导出任务ID
     * @return 任务状态
     */
    @Override
    public String checkExportStatus(String exportId) {
        ExportResult result = exportResults.get(exportId);
        if (result == null) {
            throw new IllegalArgumentException("导出任务不存在");
        }
        return result.getStatus();
    }

    /**
     * 获取导出文件的下载URL
     * @param exportId 导出任务ID
     * @return 下载URL
     */
    @Override
    public String getDownloadUrl(String exportId) {
        ExportResult result = exportResults.get(exportId);
        if (result == null) {
            throw new IllegalArgumentException("导出任务不存在");
        }
        
        if (!"completed".equals(result.getStatus())) {
            throw new IllegalStateException("导出任务未完成，无法获取下载链接");
        }
        
        return result.getDownloadUrl();
    }

    /**
     * 删除导出文件
     * @param exportId 导出任务ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteExport(String exportId) {
        logger.info("删除导出任务，导出ID: {}", exportId);
        
        // 从结果集中移除
        ExportResult result = exportResults.remove(exportId);
        if (result == null) {
            return false;
        }
        
        // TODO: 实际项目中应删除导出文件
        
        return true;
    }
    
    /**
     * 获取支持的导出格式
     * @return 支持的格式列表
     */
    public String[] getSupportedFormats() {
        return new String[]{"jpg", "png", "webp"};
    }
    
    /**
     * 导出图片（旧方法，保留兼容性）
     * @param imageId 图片ID
     * @param format 导出格式（jpg, png等）
     * @return 导出的文件路径
     */
    public String exportImage(String imageId, String format) {
        try {
            // 获取原始图片文件
            File sourceFile = imageStorageService.getImageFile(imageId);
            if (sourceFile == null) {
                throw new RuntimeException("图片不存在: " + imageId);
            }
            
            // 创建导出目录
            Path exportDir = Paths.get("./exports");
            if (!Files.exists(exportDir)) {
                Files.createDirectories(exportDir);
            }
            
            // 构建导出文件路径
            String exportFilename = "export_" + imageId + "." + format.toLowerCase();
            Path exportPath = exportDir.resolve(exportFilename);
            
            // 复制文件（实际应用中可能需要格式转换）
            Files.copy(sourceFile.toPath(), exportPath, StandardCopyOption.REPLACE_EXISTING);
            
            return exportPath.toString();
            
        } catch (IOException e) {
            throw new RuntimeException("导出图片失败", e);
        }
    }
}