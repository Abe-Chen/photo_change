package com.photochange.service.impl;

import com.photochange.model.ExportResult;
import com.photochange.model.ImageUploadResponse;
import com.photochange.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 图片存储服务实现类
 */
@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    
    private final Map<String, ExportResult> exportResults = new HashMap<>();

    @Value("${app.image.storage.path}")
    private String storageLocation;

    /**
     * 初始化存储目录
     */
    public void init() {
        try {
            Path storagePath = Paths.get(storageLocation);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法初始化存储目录", e);
        }
    }

    /**
     * 存储图片
     * @param file 图片文件
     * @param imageId 图片ID
     * @return 图片URL
     */
    @Override
    public String storeImage(MultipartFile file, String imageId) {
        try {
            // 确保存储目录存在
            init();
            
            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // 构建文件路径
            String filename = imageId + extension;
            Path targetPath = Paths.get(storageLocation).resolve(filename);
            
            // 保存文件
            Files.copy(file.getInputStream(), targetPath);
            
            // 返回图片URL
            return "/api/v1/images/" + filename;
            
        } catch (IOException e) {
            throw new RuntimeException("存储图片失败", e);
        }
    }

    /**
     * 获取图片文件
     * @param imageId 图片ID
     * @return 图片文件
     */
    @Override
    public File getImageFile(String imageId) {
        try {
            // 查找所有可能的扩展名
            File directory = new File(storageLocation);
            File[] files = directory.listFiles((dir, name) -> name.startsWith(imageId + "."));
            
            if (files != null && files.length > 0) {
                return files[0];
            }
            
            return null;
        } catch (Exception e) {
            throw new RuntimeException("获取图片文件失败", e);
        }
    }

    /**
     * 检查图片是否存在
     * @param imageId 图片ID
     * @return 是否存在
     */
    @Override
    public boolean imageExists(String imageId) {
        File directory = new File(storageLocation);
        File[] files = directory.listFiles((dir, name) -> name.startsWith(imageId + "."));
        return files != null && files.length > 0;
    }

    /**
     * 删除图片
     * @param imageId 图片ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteImage(String imageId) {
        try {
            File imageFile = getImageFile(imageId);
            if (imageFile != null && imageFile.exists()) {
                return imageFile.delete();
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("删除图片失败", e);
        }
    }
    
    /**
     * 上传图片
     * @param file 上传的图片文件
     * @return 上传响应，包含图片ID和访问URL
     */
    @Override
    public ImageUploadResponse uploadImage(MultipartFile file) {
        try {
            String imageId = "img_" + java.util.UUID.randomUUID().toString().replace("-", "");
            String imageUrl = storeImage(file, imageId);
            return new ImageUploadResponse(imageId, imageUrl, "图片上传成功");
        } catch (Exception e) {
            return new ImageUploadResponse(null, null, "图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 保存图片数据
     * @param imageData 图片数据流
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 上传响应，包含图片ID和访问URL
     */
    @Override
    public ImageUploadResponse saveImage(InputStream imageData, String fileName, String contentType) {
        try {
            String imageId = "img_" + java.util.UUID.randomUUID().toString().replace("-", "");
            String extension = getExtensionFromContentType(contentType);
            Path targetPath = Paths.get(storageLocation, imageId + extension);

            try (FileOutputStream outputStream = new FileOutputStream(targetPath.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = imageData.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            String imageUrl = "/api/v1/images/" + imageId;
            return new ImageUploadResponse(imageId, imageUrl, "图片上传成功");
        } catch (Exception e) {
            return new ImageUploadResponse(null, null, "图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 保存结果图片
     * @param imageData 图片数据流
     * @param transformationId 变换任务ID
     * @param contentType 内容类型
     * @return 结果图片URL
     */
    @Override
    public String saveResultImage(InputStream imageData, String transformationId, String contentType) {
        try {
            String extension = getExtensionFromContentType(contentType);
            Path targetPath = Paths.get(storageLocation, "results", transformationId + extension);
            
            // 确保目录存在
            Files.createDirectories(targetPath.getParent());

            try (FileOutputStream outputStream = new FileOutputStream(targetPath.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = imageData.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return "/api/v1/results/" + transformationId;
        } catch (Exception e) {
            throw new RuntimeException("保存结果图片失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成缩略图
     * @param imageId 图片ID
     * @param width 宽度
     * @param height 高度
     * @return 缩略图URL
     */
    @Override
    public String generateThumbnail(String imageId, int width, int height) {
        // 简化实现，实际应使用图像处理库生成缩略图
        return getImageUrl(imageId) + "?width=" + width + "&height=" + height;
    }

    /**
     * 获取图片URL
     * @param imageId 图片ID
     * @return 图片URL
     */
    @Override
    public String getImageUrl(String imageId) {
        return "/api/v1/images/" + imageId;
    }

    /**
     * 获取图片数据流
     * @param imageId 图片ID
     * @return 图片数据流
     */
    @Override
    public InputStream getImageData(String imageId) {
        try {
            File file = getImageFile(imageId);
            if (file != null && file.exists()) {
                return Files.newInputStream(file.toPath());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("获取图片数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取图片元数据
     * @param imageId 图片ID
     * @return 图片元数据（宽度、高度、格式等）
     */
    @Override
    public ImageMetadata getImageMetadata(String imageId) {
        // 简化实现，实际应读取图片文件获取元数据
        return new ImageMetadata(800, 600, "jpg", 1024 * 1024, "image/jpeg");
    }
    
    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return ".jpg";
        }
        
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
            case "image/jpg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            default:
                return ".jpg";
        }
    }
    
    /**
     * 异步导出图片
     * @param exportId 导出ID
     * @param transformationId 变换任务ID
     * @param format 格式
     * @param quality 质量
     * @param width 宽度
     * @param height 高度
     */
    public void exportImageAsync(String exportId, String transformationId, 
                                String format, String quality, Integer width, Integer height) {
        CompletableFuture.runAsync(() -> {
            try {
                // 模拟导出处理
                Thread.sleep(2000);
                
                // 创建导出结果
                ExportResult result = new ExportResult(
                        exportId,
                        transformationId,
                        "completed",
                        "/api/v1/exports/" + exportId + "/download",
                        format != null ? format : "jpg",
                        quality != null ? quality : "high",
                        width != null ? width : 800,
                        height != null ? height : 600,
                        1024L * 1024L,
                        System.currentTimeMillis(),
                        null
                );
                
                result.setCompletedAt(System.currentTimeMillis());
                result.setExpiresAt(System.currentTimeMillis() + 86400000); // 24小时后过期
                
                // 保存结果
                exportResults.put(exportId, result);
            } catch (Exception e) {
                ExportResult errorResult = new ExportResult(
                        exportId,
                        transformationId,
                        "failed",
                        null,
                        format,
                        quality,
                        width,
                        height,
                        null,
                        System.currentTimeMillis(),
                        e.getMessage()
                );
                exportResults.put(exportId, errorResult);
            }
        });
    }

    /**
     * 获取导出结果
     * @param exportId 导出ID
     * @return 导出结果
     */
    public ExportResult getExportResult(String exportId) {
        return exportResults.get(exportId);
    }
}