package com.photochange.service.impl;

import com.photochange.service.ExportService;
import com.photochange.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 导出服务实现类
 */
@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ImageStorageService imageStorageService;

    /**
     * 导出图片
     * @param imageId 图片ID
     * @param format 导出格式（jpg, png等）
     * @return 导出的文件路径
     */
    @Override
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

    /**
     * 获取支持的导出格式
     * @return 支持的格式列表
     */
    @Override
    public String[] getSupportedFormats() {
        return new String[]{"jpg", "png", "webp"};
    }
}