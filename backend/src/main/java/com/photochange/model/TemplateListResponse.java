package com.photochange.model;

import java.util.List;

/**
 * 模板列表响应
 */
public class TemplateListResponse {
    private List<PoseTemplate> templates;
    private int total;
    private int page;
    private int limit;
    private int pages;

    public TemplateListResponse() {
    }

    public TemplateListResponse(List<PoseTemplate> templates, int total, int page, int limit, int pages) {
        this.templates = templates;
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.pages = pages;
    }

    public List<PoseTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<PoseTemplate> templates) {
        this.templates = templates;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}