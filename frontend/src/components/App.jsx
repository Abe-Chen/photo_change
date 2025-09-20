import React, { useState, useRef } from 'react';
import './App.css';
import { Button, Upload, message, Spin, Tabs, Card, Slider, Tooltip } from 'antd';
import { UploadOutlined, SaveOutlined, UndoOutlined, RedoOutlined, SettingOutlined } from '@ant-design/icons';

const { TabPane } = Tabs;

// 模拟姿势模板数据
const poseTemplates = {
  standing: [
    { id: 'stand1', name: '自然站姿', thumbnail: '/templates/stand1.png' },
    { id: 'stand2', name: '双手叉腰', thumbnail: '/templates/stand2.png' },
    { id: 'stand3', name: '双臂交叉', thumbnail: '/templates/stand3.png' },
    { id: 'stand4', name: '单手指向', thumbnail: '/templates/stand4.png' },
  ],
  sitting: [
    { id: 'sit1', name: '标准坐姿', thumbnail: '/templates/sit1.png' },
    { id: 'sit2', name: '翘腿坐姿', thumbnail: '/templates/sit2.png' },
    { id: 'sit3', name: '前倾坐姿', thumbnail: '/templates/sit3.png' },
  ],
  action: [
    { id: 'action1', name: '跑步姿势', thumbnail: '/templates/action1.png' },
    { id: 'action2', name: '跳跃姿势', thumbnail: '/templates/action2.png' },
    { id: 'action3', name: '舞蹈姿势', thumbnail: '/templates/action3.png' },
  ],
  special: [
    { id: 'special1', name: '瑜伽姿势', thumbnail: '/templates/special1.png' },
    { id: 'special2', name: '武术姿势', thumbnail: '/templates/special2.png' },
  ],
};

function App() {
  const [loading, setLoading] = useState(false);
  const [processing, setProcessing] = useState(false);
  const [originalImage, setOriginalImage] = useState(null);
  const [resultImage, setResultImage] = useState(null);
  const [selectedTemplate, setSelectedTemplate] = useState(null);
  const [detectedKeypoints, setDetectedKeypoints] = useState(null);
  const [adjustedKeypoints, setAdjustedKeypoints] = useState(null);
  const [activeTab, setActiveTab] = useState('upload');
  const canvasRef = useRef(null);
  
  // 上传图片前的验证
  const beforeUpload = (file) => {
    const isImage = file.type.startsWith('image/');
    if (!isImage) {
      message.error('只能上传图片文件!');
    }
    const isLt10M = file.size / 1024 / 1024 < 10;
    if (!isLt10M) {
      message.error('图片大小不能超过10MB!');
    }
    return isImage && isLt10M;
  };

  // 处理图片上传
  const handleUpload = (info) => {
    if (info.file.status === 'uploading') {
      setLoading(true);
      return;
    }
    if (info.file.status === 'done') {
      // 获取上传的图片
      getBase64(info.file.originFileObj, (imageUrl) => {
        setOriginalImage(imageUrl);
        setLoading(false);
        // 模拟检测人物姿势
        detectPose(imageUrl);
      });
    }
  };

  // 将文件转换为Base64
  const getBase64 = (file, callback) => {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(file);
  };

  // 模拟检测人物姿势
  const detectPose = (imageUrl) => {
    setLoading(true);
    message.info('正在检测人物姿势...');
    
    // 模拟API调用延迟
    setTimeout(() => {
      // 模拟检测结果
      const mockKeypoints = [
        { id: 'nose', x: 250, y: 100 },
        { id: 'left_shoulder', x: 220, y: 150 },
        { id: 'right_shoulder', x: 280, y: 150 },
        { id: 'left_elbow', x: 180, y: 200 },
        { id: 'right_elbow', x: 320, y: 200 },
        { id: 'left_wrist', x: 150, y: 250 },
        { id: 'right_wrist', x: 350, y: 250 },
        { id: 'left_hip', x: 230, y: 250 },
        { id: 'right_hip', x: 270, y: 250 },
        { id: 'left_knee', x: 230, y: 320 },
        { id: 'right_knee', x: 270, y: 320 },
        { id: 'left_ankle', x: 230, y: 390 },
        { id: 'right_ankle', x: 270, y: 390 },
      ];
      
      setDetectedKeypoints(mockKeypoints);
      setAdjustedKeypoints(mockKeypoints);
      setLoading(false);
      message.success('人物姿势检测完成!');
      setActiveTab('template');
      
      // 在Canvas上绘制关键点
      drawKeypoints(mockKeypoints);
    }, 2000);
  };

  // 在Canvas上绘制关键点
  const drawKeypoints = (keypoints) => {
    const canvas = canvasRef.current;
    if (!canvas || !originalImage) return;
    
    const ctx = canvas.getContext('2d');
    const img = new Image();
    img.src = originalImage;
    
    img.onload = () => {
      // 设置Canvas尺寸与图片一致
      canvas.width = img.width;
      canvas.height = img.height;
      
      // 绘制图片
      ctx.drawImage(img, 0, 0, img.width, img.height);
      
      // 绘制关键点
      keypoints.forEach(point => {
        ctx.beginPath();
        ctx.arc(point.x, point.y, 5, 0, 2 * Math.PI);
        ctx.fillStyle = 'red';
        ctx.fill();
      });
      
      // 绘制连接线
      drawConnections(ctx, keypoints);
    };
  };

  // 绘制关键点之间的连接线
  const drawConnections = (ctx, keypoints) => {
    // 定义关键点之间的连接关系
    const connections = [
      ['left_shoulder', 'right_shoulder'],
      ['left_shoulder', 'left_elbow'],
      ['right_shoulder', 'right_elbow'],
      ['left_elbow', 'left_wrist'],
      ['right_elbow', 'right_wrist'],
      ['left_shoulder', 'left_hip'],
      ['right_shoulder', 'right_hip'],
      ['left_hip', 'right_hip'],
      ['left_hip', 'left_knee'],
      ['right_hip', 'right_knee'],
      ['left_knee', 'left_ankle'],
      ['right_knee', 'right_ankle'],
    ];
    
    // 将关键点数组转换为以ID为键的对象，便于查找
    const keypointMap = {};
    keypoints.forEach(point => {
      keypointMap[point.id] = point;
    });
    
    // 绘制连接线
    ctx.strokeStyle = 'blue';
    ctx.lineWidth = 2;
    
    connections.forEach(([from, to]) => {
      const fromPoint = keypointMap[from];
      const toPoint = keypointMap[to];
      
      if (fromPoint && toPoint) {
        ctx.beginPath();
        ctx.moveTo(fromPoint.x, fromPoint.y);
        ctx.lineTo(toPoint.x, toPoint.y);
        ctx.stroke();
      }
    });
  };

  // 选择姿势模板
  const handleSelectTemplate = (template) => {
    setSelectedTemplate(template);
    message.info(`已选择模板: ${template.name}`);
    
    // 模拟应用模板
    applyTemplate(template);
  };

  // 模拟应用姿势模板
  const applyTemplate = (template) => {
    setProcessing(true);
    message.info('正在应用姿势模板...');
    
    // 模拟API调用延迟
    setTimeout(() => {
      // 模拟调整后的关键点
      const mockAdjustedKeypoints = [
        // 这里应该是根据选择的模板调整关键点位置
        // 为简化示例，这里使用随机偏移
        ...detectedKeypoints.map(point => ({
          ...point,
          x: point.x + (Math.random() * 40 - 20),
          y: point.y + (Math.random() * 40 - 20),
        })),
      ];
      
      setAdjustedKeypoints(mockAdjustedKeypoints);
      
      // 模拟生成结果图片
      // 在实际应用中，这应该是从后端API获取的处理结果
      setResultImage('/results/sample_result.jpg');
      
      setProcessing(false);
      message.success('姿势模板应用完成!');
      setActiveTab('adjust');
      
      // 更新Canvas上的关键点
      drawKeypoints(mockAdjustedKeypoints);
    }, 3000);
  };

  // 处理关键点拖拽调整
  const handleKeypointDrag = (id, newX, newY) => {
    // 更新调整后的关键点
    const updatedKeypoints = adjustedKeypoints.map(point => {
      if (point.id === id) {
        return { ...point, x: newX, y: newY };
      }
      return point;
    });
    
    setAdjustedKeypoints(updatedKeypoints);
    
    // 重新绘制关键点
    drawKeypoints(updatedKeypoints);
  };

  // 生成最终结果
  const handleGenerateResult = () => {
    setProcessing(true);
    message.info('正在生成最终结果...');
    
    // 模拟API调用延迟
    setTimeout(() => {
      // 模拟生成结果图片
      // 在实际应用中，这应该是从后端API获取的处理结果
      setResultImage('/results/final_result.jpg');
      
      setProcessing(false);
      message.success('最终结果生成完成!');
      setActiveTab('result');
    }, 3000);
  };

  // 下载结果图片
  const handleDownload = () => {
    // 创建一个临时链接并触发下载
    const link = document.createElement('a');
    link.href = resultImage;
    link.download = 'pose_changed_image.jpg';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    
    message.success('图片下载已开始!');
  };

  // 渲染模板列表
  const renderTemplates = (category) => {
    return poseTemplates[category].map(template => (
      <Card
        key={template.id}
        hoverable
        style={{ width: 120, margin: '0 8px 16px 8px' }}
        cover={
          <img 
            alt={template.name} 
            src={template.thumbnail} 
            style={{ height: 120, objectFit: 'cover' }}
          />
        }
        onClick={() => handleSelectTemplate(template)}
        className={selectedTemplate?.id === template.id ? 'template-selected' : ''}
      >
        <Card.Meta title={template.name} style={{ textAlign: 'center' }} />
      </Card>
    ));
  };

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>人物姿势改变应用</h1>
      </header>
      
      <main className="app-content">
        <Tabs activeKey={activeTab} onChange={setActiveTab}>
          <TabPane tab="上传图片" key="upload">
            <div className="upload-container">
              <Upload
                name="image"
                listType="picture-card"
                className="image-uploader"
                showUploadList={false}
                action="https://api.photochange.com/v1/images/upload"
                beforeUpload={beforeUpload}
                onChange={handleUpload}
                disabled={loading}
              >
                {originalImage ? (
                  <img src={originalImage} alt="原图" style={{ width: '100%' }} />
                ) : (
                  <div>
                    {loading ? <Spin /> : <UploadOutlined />}
                    <div className="ant-upload-text">上传图片</div>
                    <div className="upload-hint">支持JPG、PNG格式，最大10MB</div>
                  </div>
                )}
              </Upload>
              {originalImage && (
                <div className="upload-info">
                  <p>图片已上传，正在分析人物姿势...</p>
                </div>
              )}
            </div>
          </TabPane>
          
          <TabPane tab="选择模板" key="template" disabled={!detectedKeypoints}>
            <div className="template-container">
              <Tabs defaultActiveKey="standing">
                <TabPane tab="站立姿势" key="standing">
                  <div className="template-list">
                    {renderTemplates('standing')}
                  </div>
                </TabPane>
                <TabPane tab="坐姿" key="sitting">
                  <div className="template-list">
                    {renderTemplates('sitting')}
                  </div>
                </TabPane>
                <TabPane tab="动作姿势" key="action">
                  <div className="template-list">
                    {renderTemplates('action')}
                  </div>
                </TabPane>
                <TabPane tab="特殊姿势" key="special">
                  <div className="template-list">
                    {renderTemplates('special')}
                  </div>
                </TabPane>
              </Tabs>
              
              <div className="preview-container">
                <h3>预览</h3>
                <div className="canvas-container">
                  <canvas ref={canvasRef} className="pose-canvas" />
                  {processing && (
                    <div className="processing-overlay">
                      <Spin size="large" />
                      <p>正在处理...</p>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </TabPane>
          
          <TabPane tab="调整姿势" key="adjust" disabled={!selectedTemplate}>
            <div className="adjust-container">
              <div className="adjust-tools">
                <h3>调整工具</h3>
                <div className="tool-buttons">
                  <Tooltip title="撤销">
                    <Button icon={<UndoOutlined />} />
                  </Tooltip>
                  <Tooltip title="重做">
                    <Button icon={<RedoOutlined />} />
                  </Tooltip>
                  <Tooltip title="设置">
                    <Button icon={<SettingOutlined />} />
                  </Tooltip>
                </div>
                
                <div className="adjustment-controls">
                  <div className="control-group">
                    <span>平滑度</span>
                    <Slider defaultValue={50} />
                  </div>
                  <div className="control-group">
                    <span>变形强度</span>
                    <Slider defaultValue={70} />
                  </div>
                </div>
                
                <div className="keypoint-list">
                  <h4>关键点</h4>
                  <ul>
                    {adjustedKeypoints && adjustedKeypoints.map(point => (
                      <li key={point.id}>
                        {point.id.replace('_', ' ')}
                        <Button size="small">定位</Button>
                      </li>
                    ))}
                  </ul>
                </div>
                
                <Button 
                  type="primary" 
                  onClick={handleGenerateResult}
                  loading={processing}
                >
                  生成结果
                </Button>
              </div>
              
              <div className="adjust-preview">
                <div className="canvas-container">
                  <canvas ref={canvasRef} className="pose-canvas" />
                  {processing && (
                    <div className="processing-overlay">
                      <Spin size="large" />
                      <p>正在处理...</p>
                    </div>
                  )}
                </div>
                <p className="adjust-hint">拖拽关键点进行精细调整</p>
              </div>
            </div>
          </TabPane>
          
          <TabPane tab="查看结果" key="result" disabled={!resultImage}>
            <div className="result-container">
              <div className="result-preview">
                <h3>最终结果</h3>
                {resultImage ? (
                  <img src={resultImage} alt="结果图片" className="result-image" />
                ) : (
                  <div className="empty-result">
                    <p>尚未生成结果</p>
                  </div>
                )}
              </div>
              
              <div className="result-actions">
                <Button 
                  type="primary" 
                  icon={<SaveOutlined />} 
                  onClick={handleDownload}
                  disabled={!resultImage}
                >
                  下载图片
                </Button>
                
                <div className="export-options">
                  <h4>导出选项</h4>
                  <div className="option-group">
                    <span>格式</span>
                    <select defaultValue="jpg">
                      <option value="jpg">JPG</option>
                      <option value="png">PNG</option>
                    </select>
                  </div>
                  <div className="option-group">
                    <span>质量</span>
                    <select defaultValue="high">
                      <option value="low">低</option>
                      <option value="medium">中</option>
                      <option value="high">高</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </TabPane>
        </Tabs>
      </main>
      
      <footer className="app-footer">
        <p>© 2023 人物姿势改变应用 | <a href="/help">帮助</a> | <a href="/feedback">反馈</a></p>
      </footer>
    </div>
  );
}

export default App;