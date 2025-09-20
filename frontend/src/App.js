import React, { useState, useEffect } from 'react';
import { Layout, Steps, Button, message, Modal, Divider, Typography } from 'antd';
import { UploadOutlined, AppstoreOutlined, EditOutlined, CheckOutlined } from '@ant-design/icons';
import ImageUploader from './components/ImageUploader';
import TemplateSelector from './components/TemplateSelector';
import KeypointEditor from './components/KeypointEditor';
import ResultPreview from './components/ResultPreview';
import './App.css';

const { Header, Content, Footer } = Layout;
const { Step } = Steps;
const { Title, Paragraph } = Typography;

function App() {
  // 状态管理
  const [currentStep, setCurrentStep] = useState(0);
  const [uploadedImage, setUploadedImage] = useState(null);
  const [selectedTemplate, setSelectedTemplate] = useState(null);
  const [detectedKeypoints, setDetectedKeypoints] = useState(null);
  const [customKeypoints, setCustomKeypoints] = useState(null);
  const [resultImage, setResultImage] = useState(null);
  const [loading, setLoading] = useState(false);
  const [processingStatus, setProcessingStatus] = useState('');
  const [helpModalVisible, setHelpModalVisible] = useState(false);
  
  // 监听上传图片变化，模拟检测关键点
  useEffect(() => {
    if (uploadedImage) {
      detectPose(uploadedImage);
    } else {
      // 重置状态
      setDetectedKeypoints(null);
      setCustomKeypoints(null);
      setResultImage(null);
      setSelectedTemplate(null);
    }
  }, [uploadedImage]);
  
  // 监听模板选择变化，生成预览结果
  useEffect(() => {
    if (selectedTemplate && detectedKeypoints) {
      generatePreview();
    }
  }, [selectedTemplate, detectedKeypoints]);
  
  // 监听自定义关键点变化，更新结果
  useEffect(() => {
    if (customKeypoints && selectedTemplate) {
      generateResult();
    }
  }, [customKeypoints]);
  
  // 模拟检测姿势关键点
  const detectPose = async (image) => {
    setLoading(true);
    setProcessingStatus('正在检测人物姿势...');
    
    try {
      // 模拟API请求延迟
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // 模拟检测结果
      const mockKeypoints = generateMockKeypoints();
      setDetectedKeypoints(mockKeypoints);
      setCustomKeypoints(null); // 重置自定义关键点
      
      message.success('人物姿势检测成功');
      // 自动进入下一步
      if (currentStep === 0) {
        setCurrentStep(1);
      }
    } catch (error) {
      console.error('姿势检测失败:', error);
      message.error('姿势检测失败，请重试');
    } finally {
      setLoading(false);
      setProcessingStatus('');
    }
  };
  
  // 模拟生成预览结果
  const generatePreview = async () => {
    if (!selectedTemplate || !detectedKeypoints) return;
    
    setLoading(true);
    setProcessingStatus('正在生成预览...');
    
    try {
      // 模拟API请求延迟
      await new Promise(resolve => setTimeout(resolve, 1500));
      
      // 模拟预览结果（使用模板的预览图）
      setResultImage(selectedTemplate.previewUrl);
      
      // 自动进入下一步
      if (currentStep === 1) {
        setCurrentStep(2);
      }
    } catch (error) {
      console.error('生成预览失败:', error);
      message.error('生成预览失败，请重试');
    } finally {
      setLoading(false);
      setProcessingStatus('');
    }
  };
  
  // 模拟生成最终结果
  const generateResult = async () => {
    if (!selectedTemplate || !customKeypoints) return;
    
    setLoading(true);
    setProcessingStatus('正在生成最终结果...');
    
    try {
      // 模拟API请求延迟
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // 模拟最终结果（这里仍使用模板的预览图，实际应该是基于自定义关键点生成的新图像）
      setResultImage(selectedTemplate.previewUrl);
      
      message.success('姿势变换成功');
      // 自动进入最后一步
      setCurrentStep(3);
    } catch (error) {
      console.error('生成结果失败:', error);
      message.error('生成结果失败，请重试');
    } finally {
      setLoading(false);
      setProcessingStatus('');
    }
  };
  
  // 处理图片上传
  const handleImageUpload = (imageData) => {
    setUploadedImage(imageData);
  };
  
  // 处理图片移除
  const handleImageRemove = () => {
    setUploadedImage(null);
    setCurrentStep(0);
  };
  
  // 处理模板选择
  const handleTemplateSelect = (template) => {
    setSelectedTemplate(template);
  };
  
  // 处理关键点编辑
  const handleKeypointsEdit = (editedKeypoints) => {
    setCustomKeypoints(editedKeypoints);
  };
  
  // 处理重新生成
  const handleRetry = () => {
    if (customKeypoints) {
      generateResult();
    } else if (selectedTemplate) {
      generatePreview();
    }
  };
  
  // 处理重置
  const handleReset = () => {
    setUploadedImage(null);
    setSelectedTemplate(null);
    setDetectedKeypoints(null);
    setCustomKeypoints(null);
    setResultImage(null);
    setCurrentStep(0);
  };
  
  // 处理步骤变化
  const handleStepChange = (step) => {
    // 只允许跳转到已完成的步骤或下一步
    if (step <= currentStep || step === currentStep + 1) {
      // 检查前置条件
      if (step === 1 && !uploadedImage) {
        message.warning('请先上传图片');
        return;
      }
      if (step === 2 && !selectedTemplate) {
        message.warning('请先选择姿势模板');
        return;
      }
      if (step === 3 && !customKeypoints && !detectedKeypoints) {
        message.warning('请先完成关键点调整');
        return;
      }
      
      setCurrentStep(step);
    }
  };
  
  // 处理下一步
  const handleNext = () => {
    if (currentStep < 3) {
      handleStepChange(currentStep + 1);
    }
  };
  
  // 处理上一步
  const handlePrev = () => {
    if (currentStep > 0) {
      handleStepChange(currentStep - 1);
    }
  };
  
  // 处理帮助按钮点击
  const handleHelpClick = () => {
    setHelpModalVisible(true);
  };
  
  // 渲染步骤内容
  const renderStepContent = () => {
    switch (currentStep) {
      case 0:
        return (
          <ImageUploader
            onImageUpload={handleImageUpload}
            onImageRemove={handleImageRemove}
            loading={loading}
          />
        );
      case 1:
        return (
          <TemplateSelector
            onSelectTemplate={handleTemplateSelect}
            selectedTemplateId={selectedTemplate?.id}
            loading={loading}
          />
        );
      case 2:
        return (
          <KeypointEditor
            imageUrl={uploadedImage?.dataUrl}
            keypoints={detectedKeypoints}
            onKeypointsChange={handleKeypointsEdit}
            loading={loading}
          />
        );
      case 3:
        return (
          <ResultPreview
            resultImage={resultImage}
            originalImage={uploadedImage?.dataUrl}
            loading={loading}
            onReset={handleReset}
            onRetry={handleRetry}
          />
        );
      default:
        return null;
    }
  };
  
  // 渲染帮助模态框
  const renderHelpModal = () => {
    return (
      <Modal
        title="使用帮助"
        open={helpModalVisible}
        onCancel={() => setHelpModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setHelpModalVisible(false)}>
            关闭
          </Button>
        ]}
        width={700}
      >
        <Typography>
          <Title level={4}>如何使用姿势变换应用</Title>
          
          <Paragraph>
            本应用可以帮助您将图片中的人物姿势变换为其他姿势。使用过程分为四个简单步骤：
          </Paragraph>
          
          <Divider orientation="left">步骤 1: 上传图片</Divider>
          <Paragraph>
            <ul>
              <li>点击上传区域或拖拽图片到上传区域</li>
              <li>支持 JPG、JPEG、PNG 和 WEBP 格式的图片</li>
              <li>图片大小不超过 10MB</li>
              <li>确保图片中有清晰的人物，以便系统准确识别</li>
            </ul>
          </Paragraph>
          
          <Divider orientation="left">步骤 2: 选择姿势模板</Divider>
          <Paragraph>
            <ul>
              <li>浏览并选择您想要的目标姿势模板</li>
              <li>可以按类别筛选或搜索特定姿势</li>
              <li>点击模板卡片进行选择</li>
            </ul>
          </Paragraph>
          
          <Divider orientation="left">步骤 3: 调整关键点</Divider>
          <Paragraph>
            <ul>
              <li>系统会自动识别原图中的人物关键点</li>
              <li>您可以手动调整关键点位置以获得更精确的结果</li>
              <li>使用工具栏中的功能进行缩放、重置或撤销操作</li>
              <li>完成调整后点击"应用"按钮</li>
            </ul>
          </Paragraph>
          
          <Divider orientation="left">步骤 4: 查看结果</Divider>
          <Paragraph>
            <ul>
              <li>系统会生成变换后的图片结果</li>
              <li>您可以下载或分享结果图片</li>
              <li>如需重新调整，可以点击"重试"按钮</li>
              <li>如需重新开始，可以点击"重置"按钮</li>
            </ul>
          </Paragraph>
          
          <Divider />
          <Paragraph>
            如果您在使用过程中遇到任何问题，请点击页面底部的"联系我们"获取帮助。
          </Paragraph>
        </Typography>
      </Modal>
    );
  };

  return (
    <Layout className="app-layout">
      <Header className="app-header">
        <div className="logo">
          <img src="/logo.png" alt="姿势变换" />
          <h1>姿势变换</h1>
        </div>
        <Button type="text" onClick={handleHelpClick} className="help-button">
          使用帮助
        </Button>
      </Header>
      
      <Content className="app-content">
        <div className="steps-container">
          <Steps current={currentStep} onChange={handleStepChange}>
            <Step title="上传图片" icon={<UploadOutlined />} />
            <Step title="选择模板" icon={<AppstoreOutlined />} />
            <Step title="调整关键点" icon={<EditOutlined />} />
            <Step title="查看结果" icon={<CheckOutlined />} />
          </Steps>
        </div>
        
        <div className="step-content-container">
          {renderStepContent()}
        </div>
        
        {processingStatus && (
          <div className="processing-status">
            {processingStatus}
          </div>
        )}
        
        <div className="steps-action">
          <Button 
            onClick={handlePrev} 
            disabled={currentStep === 0 || loading}
          >
            上一步
          </Button>
          
          {currentStep < 3 && (
            <Button 
              type="primary" 
              onClick={handleNext}
              disabled={loading || 
                (currentStep === 0 && !uploadedImage) || 
                (currentStep === 1 && !selectedTemplate) || 
                (currentStep === 2 && !detectedKeypoints)}
            >
              下一步
            </Button>
          )}
          
          {currentStep === 2 && (
            <Button 
              type="primary" 
              onClick={() => generateResult()}
              disabled={loading || !detectedKeypoints}
            >
              应用调整
            </Button>
          )}
        </div>
      </Content>
      
      <Footer className="app-footer">
        <div className="footer-links">
          <a href="#">关于我们</a>
          <a href="#">使用条款</a>
          <a href="#">隐私政策</a>
          <a href="#">联系我们</a>
        </div>
        <div className="copyright">
          © {new Date().getFullYear()} 姿势变换应用 版权所有
        </div>
      </Footer>
      
      {renderHelpModal()}
    </Layout>
  );
}

// 生成模拟关键点数据
const generateMockKeypoints = () => {
  // 模拟17个人体关键点（COCO格式）
  return [
    { id: 0, name: '鼻子', x: 0.5, y: 0.2 },
    { id: 1, name: '左眼', x: 0.45, y: 0.18 },
    { id: 2, name: '右眼', x: 0.55, y: 0.18 },
    { id: 3, name: '左耳', x: 0.4, y: 0.2 },
    { id: 4, name: '右耳', x: 0.6, y: 0.2 },
    { id: 5, name: '左肩', x: 0.35, y: 0.3 },
    { id: 6, name: '右肩', x: 0.65, y: 0.3 },
    { id: 7, name: '左肘', x: 0.3, y: 0.45 },
    { id: 8, name: '右肘', x: 0.7, y: 0.45 },
    { id: 9, name: '左腕', x: 0.25, y: 0.6 },
    { id: 10, name: '右腕', x: 0.75, y: 0.6 },
    { id: 11, name: '左髋', x: 0.4, y: 0.65 },
    { id: 12, name: '右髋', x: 0.6, y: 0.65 },
    { id: 13, name: '左膝', x: 0.4, y: 0.8 },
    { id: 14, name: '右膝', x: 0.6, y: 0.8 },
    { id: 15, name: '左踝', x: 0.4, y: 0.95 },
    { id: 16, name: '右踝', x: 0.6, y: 0.95 },
  ];
};

export default App;