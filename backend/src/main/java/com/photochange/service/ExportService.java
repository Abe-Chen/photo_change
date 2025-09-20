package com.photochange.service;

import com.photochange.model.ExportRequest;
import com.photochange.model.ExportResponse;
import com.photochange.model.ExportResult;

import java.util.concurrent.CompletableFuture;

/**
 * 导出服务接口
 * 负责处理结果图片导出相关的业务逻辑
 */
public interface ExportService {

    /**
     * 创建导出任务
     * @param request 导出请求
     * @return 导出响应，包含任务ID和状态
     */
    ExportResponse createExport(ExportRequest request);

    /**
     * 获取导出结果
     * @param exportId 导出任务ID
     * @return 导出结果，包含下载URL
     */
    ExportResult getExportResult(String exportId);

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
    CompletableFuture<ExportResult> processExport(String transformationId, String format, 
                                                String quality, Integer width, Integer height, String exportId);

    /**
     * 取消导出任务
     * @param exportId 导出任务ID
     * @return 是否成功取消
     */
    boolean cancelExport(String exportId);

    /**
     * 检查导出任务状态
     * @param exportId 导出任务ID
     * @return 任务状态
     */
    String checkExportStatus(String exportId);

    /**
     * 获取导出文件的下载URL
     * @param exportId 导出任务ID
     * @return 下载URL
     */
    String getDownloadUrl(String exportId);

    /**
     * 删除导出文件
     * @param exportId 导出任务ID
     * @return 是否删除成功
     */
    boolean deleteExport(String exportId);
}