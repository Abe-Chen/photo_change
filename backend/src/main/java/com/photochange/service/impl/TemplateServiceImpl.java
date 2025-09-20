package com.photochange.service.impl;

import com.photochange.model.PoseTemplate;
import com.photochange.service.TemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 姿势模板服务实现类
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Value("${app.template.storage.path}")
    private String templateStoragePath;

    // 模拟模板数据
    private final Map<String, PoseTemplate> templates = new HashMap<>();

    // 初始化一些模板数据
    public TemplateServiceImpl() {
        // 站立姿势
        PoseTemplate standing = new PoseTemplate();
        standing.setId("tpl_standing_01");
        standing.setName("基本站立姿势");
        standing.setCategory("standing");
        standing.setThumbnailUrl("/templates/standing_01.jpg");
        standing.setDescription("自然站立的姿势，适合各种场景");
        Map<String, List<Double>> standingKeypoints = new HashMap<>();
        // 模拟关键点数据
        standingKeypoints.put("nose", List.of(0.5, 0.2));
        standingKeypoints.put("left_shoulder", List.of(0.4, 0.3));
        standingKeypoints.put("right_shoulder", List.of(0.6, 0.3));
        standingKeypoints.put("left_elbow", List.of(0.3, 0.4));
        standingKeypoints.put("right_elbow", List.of(0.7, 0.4));
        standingKeypoints.put("left_wrist", List.of(0.3, 0.5));
        standingKeypoints.put("right_wrist", List.of(0.7, 0.5));
        standingKeypoints.put("left_hip", List.of(0.45, 0.6));
        standingKeypoints.put("right_hip", List.of(0.55, 0.6));
        standingKeypoints.put("left_knee", List.of(0.45, 0.75));
        standingKeypoints.put("right_knee", List.of(0.55, 0.75));
        standingKeypoints.put("left_ankle", List.of(0.45, 0.9));
        standingKeypoints.put("right_ankle", List.of(0.55, 0.9));
        standing.setKeypoints(standingKeypoints);
        templates.put(standing.getId(), standing);

        // 坐姿
        PoseTemplate sitting = new PoseTemplate();
        sitting.setId("tpl_sitting_01");
        sitting.setName("基本坐姿");
        sitting.setCategory("sitting");
        sitting.setThumbnailUrl("/templates/sitting_01.jpg");
        sitting.setDescription("自然坐姿，适合办公、学习场景");
        Map<String, List<Double>> sittingKeypoints = new HashMap<>();
        // 模拟关键点数据
        sittingKeypoints.put("nose", List.of(0.5, 0.2));
        sittingKeypoints.put("left_shoulder", List.of(0.4, 0.3));
        sittingKeypoints.put("right_shoulder", List.of(0.6, 0.3));
        sittingKeypoints.put("left_elbow", List.of(0.3, 0.4));
        sittingKeypoints.put("right_elbow", List.of(0.7, 0.4));
        sittingKeypoints.put("left_wrist", List.of(0.3, 0.5));
        sittingKeypoints.put("right_wrist", List.of(0.7, 0.5));
        sittingKeypoints.put("left_hip", List.of(0.45, 0.6));
        sittingKeypoints.put("right_hip", List.of(0.55, 0.6));
        sittingKeypoints.put("left_knee", List.of(0.4, 0.7));
        sittingKeypoints.put("right_knee", List.of(0.6, 0.7));
        sittingKeypoints.put("left_ankle", List.of(0.35, 0.8));
        sittingKeypoints.put("right_ankle", List.of(0.65, 0.8));
        sitting.setKeypoints(sittingKeypoints);
        templates.put(sitting.getId(), sitting);

        // 举手姿势
        PoseTemplate handRaised = new PoseTemplate();
        handRaised.setId("tpl_hand_raised_01");
        handRaised.setName("举手姿势");
        handRaised.setCategory("action");
        handRaised.setThumbnailUrl("/templates/hand_raised_01.jpg");
        handRaised.setDescription("举起一只手的姿势，适合互动场景");
        Map<String, List<Double>> handRaisedKeypoints = new HashMap<>();
        // 模拟关键点数据
        handRaisedKeypoints.put("nose", List.of(0.5, 0.2));
        handRaisedKeypoints.put("left_shoulder", List.of(0.4, 0.3));
        handRaisedKeypoints.put("right_shoulder", List.of(0.6, 0.3));
        handRaisedKeypoints.put("left_elbow", List.of(0.3, 0.4));
        handRaisedKeypoints.put("right_elbow", List.of(0.7, 0.2));
        handRaisedKeypoints.put("left_wrist", List.of(0.3, 0.5));
        handRaisedKeypoints.put("right_wrist", List.of(0.7, 0.1));
        handRaisedKeypoints.put("left_hip", List.of(0.45, 0.6));
        handRaisedKeypoints.put("right_hip", List.of(0.55, 0.6));
        handRaisedKeypoints.put("left_knee", List.of(0.45, 0.75));
        handRaisedKeypoints.put("right_knee", List.of(0.55, 0.75));
        handRaisedKeypoints.put("left_ankle", List.of(0.45, 0.9));
        handRaisedKeypoints.put("right_ankle", List.of(0.55, 0.9));
        handRaised.setKeypoints(handRaisedKeypoints);
        templates.put(handRaised.getId(), handRaised);
    }

    /**
     * 获取模板列表
     * @param category 类别（可选）
     * @param page 页码
     * @param limit 每页数量
     * @return 模板列表
     */
    @Override
    public List<PoseTemplate> getTemplates(String category, int page, int limit) {
        List<PoseTemplate> filteredTemplates;
        
        if (category != null && !category.isEmpty()) {
            filteredTemplates = templates.values().stream()
                    .filter(template -> category.equals(template.getCategory()))
                    .collect(Collectors.toList());
        } else {
            filteredTemplates = new ArrayList<>(templates.values());
        }
        
        // 分页
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, filteredTemplates.size());
        
        if (start >= filteredTemplates.size()) {
            return new ArrayList<>();
        }
        
        return filteredTemplates.subList(start, end);
    }

    /**
     * 获取模板详情
     * @param templateId 模板ID
     * @return 模板详情
     */
    @Override
    public PoseTemplate getTemplate(String templateId) {
        return templates.get(templateId);
    }

    /**
     * 统计模板数量
     * @param category 类别（可选）
     * @return 模板数量
     */
    @Override
    public int countTemplates(String category) {
        if (category != null && !category.isEmpty()) {
            return (int) templates.values().stream()
                    .filter(template -> category.equals(template.getCategory()))
                    .count();
        } else {
            return templates.size();
        }
    }
}