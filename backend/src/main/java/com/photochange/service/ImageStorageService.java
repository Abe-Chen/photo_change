package com.photochange.service;

import com.photochange.model.ImageUploadResponse;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 图片存储服务接口
 * 负责处理图片上传、存储和检索的业务逻辑
 */
public interface ImageStorageService {

    /**
     * 上传图片
     * @param file 上传的图片文件
     * @return 上传响应，包含图片ID和访问URL
     */
    ImageUploadResponse uploadImage(MultipartFile file);

    /**
     * 保存图片数据
     * @param imageData 图片数据流
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 上传响应，包含图片ID和访问URL
     */
    ImageUploadResponse saveImage(InputStream imageData, String fileName, String contentType);

    /**
     * 保存结果图片
     * @param imageData 图片数据流
     * @param transformationId 变换任务ID
     * @param contentType 内容类型
     * @return 结果图片URL
     */
    String saveResultImage(InputStream imageData, String transformationId, String contentType);

    /**
     * 生成缩略图
     * @param imageId 图片ID
     * @param width 宽度
     * @param height 高度
     * @return 缩略图URL
     */
    String generateThumbnail(String imageId, int width, int height);

    /**
     * 获取图片URL
     * @param imageId 图片ID
     * @return 图片URL
     */
    String getImageUrl(String imageId);

    /**
     * 获取图片数据流
     * @param imageId 图片ID
     * @return 图片数据流
     */
    InputStream getImageData(String imageId);

    /**
     * 删除图片
     * @param imageId 图片ID
     * @return 是否删除成功
     */
    boolean deleteImage(String imageId);

    /**
     * 检查图片是否存在
     * @param imageId 图片ID
     * @return 是否存在
     */
    boolean imageExists(String imageId);

    /**
     * 获取图片元数据
     * @param imageId 图片ID
     * @return 图片元数据（宽度、高度、格式等）
     */
    ImageMetadata getImageMetadata(String imageId);

    /**
     * 图片元数据类
     */
    class ImageMetadata {
        private int width;
        private int height;
        private String format;
        private long size;
        private String contentType;

        public ImageMetadata(int width, int height, String format, long size, String contentType) {
            this.width = width;
            this.height = height;
            this.format = format;
            this.size = size;
            this.contentType = contentType;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getFormat() {
            return format;
        }

        public long getSize() {
            return size;
        }

        public String getContentType() {
            return contentType;
        }
    }
}