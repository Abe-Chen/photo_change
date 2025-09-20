package com.photochange.service;

import com.photochange.model.Keypoint;
import com.photochange.model.TransformationRequest;
import com.photochange.model.TransformationResponse;
import com.photochange.model.TransformationResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 姿势变换服务接口
 * 负责处理人体姿势变换相关的业务逻辑
 */
public interface PoseTransformationService {

    /**
     * 创建姿势变换任务
     * @param request 变换请求，包含源图片ID和目标模板ID
     * @return 变换响应，包含任务ID和状态
     */
    TransformationResponse createTransformation(TransformationRequest request);

    /**
     * 获取变换结果
     * @param transformationId 变换任务ID
     * @return 变换结果，包含结果图片URL
     */
    TransformationResult getTransformationResult(String transformationId);

    /**
     * 异步处理姿势变换任务
     * @param imageId 源图片ID
     * @param templateId 目标模板ID
     * @param customKeypoints 自定义关键点（可选）
     * @param transformationId 变换任务ID
     * @return 异步任务
     */
    CompletableFuture<TransformationResult> processTransformation(String imageId, String templateId, 
                                                               List<Keypoint> customKeypoints, String transformationId);

    /**
     * 更新变换任务的自定义关键点
     * @param transformationId 变换任务ID
     * @param customKeypoints 自定义关键点列表
     * @return 更新后的变换响应
     */
    TransformationResponse updateTransformation(String transformationId, List<Keypoint> customKeypoints);

    /**
     * 取消变换任务
     * @param transformationId 变换任务ID
     * @return 是否成功取消
     */
    boolean cancelTransformation(String transformationId);

    /**
     * 检查变换任务状态
     * @param transformationId 变换任务ID
     * @return 任务状态
     */
    String checkTransformationStatus(String transformationId);
}