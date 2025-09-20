package com.photochange.service;

import com.photochange.model.PoseTemplate;
import com.photochange.model.TemplateListResponse;

import java.util.List;

/**
 * 姿势模板服务接口
 * 负责管理姿势模板相关的业务逻辑
 */
public interface TemplateService {

    /**
     * 获取所有姿势模板列表
     * @param page 页码（从1开始）
     * @param limit 每页数量
     * @param category 分类（可选）
     * @param sortBy 排序字段（可选，如popularity, createdAt）
     * @return 模板列表响应
     */
    TemplateListResponse getTemplates(int page, int limit, String category, String sortBy);

    /**
     * 获取指定ID的模板详情
     * @param templateId 模板ID
     * @return 模板详情
     */
    PoseTemplate getTemplateById(String templateId);

    /**
     * 获取推荐模板列表
     * @param limit 数量限制
     * @return 推荐模板列表
     */
    List<PoseTemplate> getRecommendedTemplates(int limit);

    /**
     * 获取热门模板列表
     * @param limit 数量限制
     * @return 热门模板列表
     */
    List<PoseTemplate> getPopularTemplates(int limit);

    /**
     * 获取最新模板列表
     * @param limit 数量限制
     * @return 最新模板列表
     */
    List<PoseTemplate> getLatestTemplates(int limit);

    /**
     * 搜索模板
     * @param keyword 关键词
     * @param page 页码
     * @param limit 每页数量
     * @return 模板列表响应
     */
    TemplateListResponse searchTemplates(String keyword, int page, int limit);

    /**
     * 创建新模板（管理员功能）
     * @param template 模板信息
     * @return 创建的模板
     */
    PoseTemplate createTemplate(PoseTemplate template);

    /**
     * 更新模板信息（管理员功能）
     * @param templateId 模板ID
     * @param template 更新的模板信息
     * @return 更新后的模板
     */
    PoseTemplate updateTemplate(String templateId, PoseTemplate template);

    /**
     * 删除模板（管理员功能）
     * @param templateId 模板ID
     * @return 是否删除成功
     */
    boolean deleteTemplate(String templateId);
}