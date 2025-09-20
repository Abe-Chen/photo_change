import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Tabs, Spin, Empty, Pagination, Input, Button } from 'antd';
import { SearchOutlined, StarOutlined, HistoryOutlined, FireOutlined } from '@ant-design/icons';
import './TemplateSelector.css';

const { TabPane } = Tabs;
const { Search } = Input;

/**
 * 姿势模板选择器组件
 * 展示多种预设姿势模板供用户选择
 */
const TemplateSelector = ({
  onSelectTemplate,
  selectedTemplateId,
  loading = false,
}) => {
  // 状态管理
  const [templates, setTemplates] = useState([]);
  const [filteredTemplates, setFilteredTemplates] = useState([]);
  const [searchText, setSearchText] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(8);
  const [activeTab, setActiveTab] = useState('all');
  const [loadingTemplates, setLoadingTemplates] = useState(false);
  
  // 模拟从API获取模板数据
  useEffect(() => {
    const fetchTemplates = async () => {
      setLoadingTemplates(true);
      try {
        // 模拟API请求延迟
        await new Promise(resolve => setTimeout(resolve, 800));
        
        // 模拟模板数据
        const mockTemplates = generateMockTemplates();
        setTemplates(mockTemplates);
        filterTemplates(mockTemplates, activeTab, searchText);
      } catch (error) {
        console.error('获取模板失败:', error);
      } finally {
        setLoadingTemplates(false);
      }
    };
    
    fetchTemplates();
  }, []);
  
  // 根据标签和搜索文本过滤模板
  const filterTemplates = (allTemplates, tab, search) => {
    let filtered = [...allTemplates];
    
    // 根据标签过滤
    if (tab === 'popular') {
      filtered = filtered.filter(t => t.popularity > 0.7);
    } else if (tab === 'recent') {
      filtered = filtered.sort((a, b) => b.createdAt - a.createdAt);
    } else if (tab !== 'all') {
      filtered = filtered.filter(t => t.category === tab);
    }
    
    // 根据搜索文本过滤
    if (search) {
      const searchLower = search.toLowerCase();
      filtered = filtered.filter(t => 
        t.name.toLowerCase().includes(searchLower) || 
        t.description.toLowerCase().includes(searchLower)
      );
    }
    
    setFilteredTemplates(filtered);
    setCurrentPage(1); // 重置到第一页
  };
  
  // 处理标签切换
  const handleTabChange = (tab) => {
    setActiveTab(tab);
    filterTemplates(templates, tab, searchText);
  };
  
  // 处理搜索
  const handleSearch = (value) => {
    setSearchText(value);
    filterTemplates(templates, activeTab, value);
  };
  
  // 处理分页变化
  const handlePageChange = (page, pageSize) => {
    setCurrentPage(page);
    setPageSize(pageSize);
  };
  
  // 处理模板选择
  const handleSelectTemplate = (template) => {
    if (onSelectTemplate) {
      onSelectTemplate(template);
    }
  };
  
  // 计算当前页的模板
  const getCurrentPageTemplates = () => {
    const startIndex = (currentPage - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    return filteredTemplates.slice(startIndex, endIndex);
  };
  
  // 渲染模板卡片
  const renderTemplateCard = (template) => {
    const isSelected = template.id === selectedTemplateId;
    
    return (
      <Col xs={24} sm={12} md={8} lg={6} key={template.id}>
        <Card
          hoverable
          className={`template-card ${isSelected ? 'selected' : ''}`}
          cover={
            <div className="template-image-container">
              <img 
                alt={template.name} 
                src={template.thumbnailUrl} 
                className="template-image"
              />
              {template.popularity > 0.8 && (
                <span className="template-badge popular">
                  <FireOutlined /> 热门
                </span>
              )}
              {new Date().getTime() - template.createdAt < 7 * 24 * 60 * 60 * 1000 && (
                <span className="template-badge new">
                  NEW
                </span>
              )}
            </div>
          }
          onClick={() => handleSelectTemplate(template)}
        >
          <Card.Meta
            title={template.name}
            description={template.description}
          />
          <div className="template-category">{template.category}</div>
        </Card>
      </Col>
    );
  };
  
  // 渲染模板列表
  const renderTemplateList = () => {
    const currentTemplates = getCurrentPageTemplates();
    
    if (loadingTemplates) {
      return (
        <div className="templates-loading">
          <Spin size="large" />
          <p>加载模板中...</p>
        </div>
      );
    }
    
    if (currentTemplates.length === 0) {
      return (
        <Empty
          description="没有找到匹配的模板"
          image={Empty.PRESENTED_IMAGE_SIMPLE}
        />
      );
    }
    
    return (
      <>
        <Row gutter={[16, 16]} className="templates-grid">
          {currentTemplates.map(renderTemplateCard)}
        </Row>
        
        <div className="templates-pagination">
          <Pagination
            current={currentPage}
            pageSize={pageSize}
            total={filteredTemplates.length}
            onChange={handlePageChange}
            showSizeChanger={false}
            showTotal={(total) => `共 ${total} 个模板`}
          />
        </div>
      </>
    );
  };

  return (
    <div className="template-selector">
      <div className="template-selector-header">
        <h3>选择姿势模板</h3>
        <Search
          placeholder="搜索模板"
          allowClear
          enterButton={<SearchOutlined />}
          onSearch={handleSearch}
          className="template-search"
        />
      </div>
      
      <Tabs activeKey={activeTab} onChange={handleTabChange}>
        <TabPane tab="全部" key="all" />
        <TabPane 
          tab={
            <span>
              <FireOutlined /> 热门
            </span>
          } 
          key="popular" 
        />
        <TabPane 
          tab={
            <span>
              <HistoryOutlined /> 最新
            </span>
          } 
          key="recent" 
        />
        <TabPane tab="站立" key="standing" />
        <TabPane tab="坐姿" key="sitting" />
        <TabPane tab="运动" key="sports" />
        <TabPane tab="舞蹈" key="dance" />
        <TabPane tab="其他" key="other" />
      </Tabs>
      
      <div className="templates-container">
        {renderTemplateList()}
      </div>
      
      {loading && (
        <div className="template-selector-loading">
          <Spin size="large" />
        </div>
      )}
    </div>
  );
};

// 生成模拟模板数据
const generateMockTemplates = () => {
  const categories = ['standing', 'sitting', 'sports', 'dance', 'other'];
  const templates = [];
  
  // 站立姿势
  templates.push({
    id: 'template-1',
    name: '标准站姿',
    description: '自然站立，双手放在身体两侧',
    category: 'standing',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=标准站姿',
    previewUrl: 'https://via.placeholder.com/400x600?text=标准站姿',
    popularity: 0.9,
    createdAt: new Date().getTime() - 30 * 24 * 60 * 60 * 1000,
  });
  
  templates.push({
    id: 'template-2',
    name: '双手交叉站姿',
    description: '站立，双手在胸前交叉',
    category: 'standing',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=双手交叉站姿',
    previewUrl: 'https://via.placeholder.com/400x600?text=双手交叉站姿',
    popularity: 0.85,
    createdAt: new Date().getTime() - 45 * 24 * 60 * 60 * 1000,
  });
  
  // 坐姿
  templates.push({
    id: 'template-3',
    name: '标准坐姿',
    description: '端正坐姿，双手放在膝盖上',
    category: 'sitting',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=标准坐姿',
    previewUrl: 'https://via.placeholder.com/400x600?text=标准坐姿',
    popularity: 0.8,
    createdAt: new Date().getTime() - 15 * 24 * 60 * 60 * 1000,
  });
  
  templates.push({
    id: 'template-4',
    name: '休闲坐姿',
    description: '放松坐姿，一只手支撑',
    category: 'sitting',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=休闲坐姿',
    previewUrl: 'https://via.placeholder.com/400x600?text=休闲坐姿',
    popularity: 0.75,
    createdAt: new Date().getTime() - 60 * 24 * 60 * 60 * 1000,
  });
  
  // 运动姿势
  templates.push({
    id: 'template-5',
    name: '跑步姿势',
    description: '动态跑步姿势，展现活力',
    category: 'sports',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=跑步姿势',
    previewUrl: 'https://via.placeholder.com/400x600?text=跑步姿势',
    popularity: 0.95,
    createdAt: new Date().getTime() - 5 * 24 * 60 * 60 * 1000,
  });
  
  templates.push({
    id: 'template-6',
    name: '投篮姿势',
    description: '篮球投篮动作，准确定格',
    category: 'sports',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=投篮姿势',
    previewUrl: 'https://via.placeholder.com/400x600?text=投篮姿势',
    popularity: 0.7,
    createdAt: new Date().getTime() - 20 * 24 * 60 * 60 * 1000,
  });
  
  // 舞蹈姿势
  templates.push({
    id: 'template-7',
    name: '芭蕾基本姿势',
    description: '优雅的芭蕾舞基本站姿',
    category: 'dance',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=芭蕾基本姿势',
    previewUrl: 'https://via.placeholder.com/400x600?text=芭蕾基本姿势',
    popularity: 0.65,
    createdAt: new Date().getTime() - 10 * 24 * 60 * 60 * 1000,
  });
  
  templates.push({
    id: 'template-8',
    name: '现代舞姿势',
    description: '富有表现力的现代舞姿势',
    category: 'dance',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=现代舞姿势',
    previewUrl: 'https://via.placeholder.com/400x600?text=现代舞姿势',
    popularity: 0.6,
    createdAt: new Date().getTime() - 2 * 24 * 60 * 60 * 1000,
  });
  
  // 其他姿势
  templates.push({
    id: 'template-9',
    name: '瑜伽姿势',
    description: '平衡与力量的瑜伽姿势',
    category: 'other',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=瑜伽姿势',
    previewUrl: 'https://via.placeholder.com/400x600?text=瑜伽姿势',
    popularity: 0.85,
    createdAt: new Date().getTime() - 25 * 24 * 60 * 60 * 1000,
  });
  
  templates.push({
    id: 'template-10',
    name: '模特展示姿势',
    description: '专业模特展示产品的姿势',
    category: 'other',
    thumbnailUrl: 'https://via.placeholder.com/200x300?text=模特展示姿势',
    previewUrl: 'https://via.placeholder.com/400x600?text=模特展示姿势',
    popularity: 0.75,
    createdAt: new Date().getTime() - 40 * 24 * 60 * 60 * 1000,
  });
  
  // 添加更多模拟数据
  for (let i = 11; i <= 20; i++) {
    const category = categories[Math.floor(Math.random() * categories.length)];
    templates.push({
      id: `template-${i}`,
      name: `姿势模板 ${i}`,
      description: `这是姿势模板 ${i} 的描述文本`,
      category: category,
      thumbnailUrl: `https://via.placeholder.com/200x300?text=模板${i}`,
      previewUrl: `https://via.placeholder.com/400x600?text=模板${i}`,
      popularity: Math.random(),
      createdAt: new Date().getTime() - Math.floor(Math.random() * 90) * 24 * 60 * 60 * 1000,
    });
  }
  
  return templates;
};

export default TemplateSelector;