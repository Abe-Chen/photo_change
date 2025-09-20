package com.photochange.service;

import com.photochange.model.PoseDetectionRequest;
import com.photochange.model.PoseDetectionResponse;
import com.photochange.model.PoseDetectionResult;

import java.util.concurrent.CompletableFuture;

/**
 * 姿势检测服务接口
 * 负责处理人体姿势检测相关的业务逻辑
 */
public interface PoseDetectionService {

    /**
     * 异步检测图片中的人体姿势
     * @param request 姿势检测请求
     * @return 姿势检测响应，包含任务ID和状态
     */
    PoseDetectionResponse detectPose(PoseDetectionRequest request);

    /**
     * 获取姿势检测结果
     * @param detectionId 检测任务ID
     * @return 姿势检测结果，包含关键点和分割信息
     */
    PoseDetectionResult getDetectionResult(String detectionId);

    /**
     * 异步处理姿势检测任务
     * @param imageId 图片ID
     * @param detectionId 检测任务ID
     * @return 异步任务
     */
    CompletableFuture<PoseDetectionResult> processPoseDetection(String imageId, String detectionId);

    /**
     * 取消姿势检测任务
     * @param detectionId 检测任务ID
     * @return 是否成功取消
     */
    boolean cancelDetection(String detectionId);

    /**
     * 检查姿势检测任务状态
     * @param detectionId 检测任务ID
     * @return 任务状态
     */
    String checkDetectionStatus(String detectionId);
}