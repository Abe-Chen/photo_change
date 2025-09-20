package com.photochange.service.impl;

import com.photochange.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图片存储服务实现类
 */
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

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
}