import React, { useState } from 'react';
import { Card, Button, Spin, Empty, Tooltip, message } from 'antd';
import { DownloadOutlined, ShareAltOutlined, UndoOutlined, RedoOutlined, FullscreenOutlined } from '@ant-design/icons';
import './ResultPreview.css';

/**
 * 结果预览组件
 * 展示姿势变换后的图片结果，提供下载、分享等功能
 */
const ResultPreview = ({
  resultImage,
  originalImage,
  loading = false,
  onReset,
  onRetry,
}) => {
  // 状态管理
  const [downloading, setDownloading] = useState(false);
  const [sharing, setSharing] = useState(false);
  const [fullscreen, setFullscreen] = useState(false);
  
  // 处理下载结果图片
  const handleDownload = async () => {
    if (!resultImage) return;
    
    setDownloading(true);
    try {
      // 创建一个链接并触发下载
      const link = document.createElement('a');
      link.href = resultImage;
      link.download = `pose_transformed_${new Date().getTime()}.png`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      
      message.success('图片下载成功');
    } catch (error) {
      console.error('下载失败:', error);
      message.error('图片下载失败，请重试');
    } finally {
      setDownloading(false);
    }
  };
  
  // 处理分享结果图片
  const handleShare = async () => {
    if (!resultImage) return;
    
    setSharing(true);
    try {
      // 检查Web Share API是否可用
      if (navigator.share) {
        // 先将图片转换为Blob
        const response = await fetch(resultImage);
        const blob = await response.blob();
        const file = new File([blob], 'pose_transformed.png', { type: 'image/png' });
        
        await navigator.share({
          title: '我的姿势变换结果',
          text: '查看我使用姿势变换应用创建的图片！',
          files: [file]
        });
        
        message.success('分享成功');
      } else {
        // 如果Web Share API不可用，复制图片链接到剪贴板
        await navigator.clipboard.writeText(resultImage);
        message.success('图片链接已复制到剪贴板');
      }
    } catch (error) {
      console.error('分享失败:', error);
      message.error('分享失败，请重试');
    } finally {
      setSharing(false);
    }
  };
  
  // 处理全屏预览
  const handleFullscreen = () => {
    setFullscreen(!fullscreen);
  };
  
  // 渲染结果内容
  const renderResultContent = () => {
    if (loading) {
      return (
        <div className="result-loading">
          <Spin size="large" />
          <p>正在生成结果...</p>
        </div>
      );
    }
    
    if (!resultImage) {
      return (
        <Empty
          image={Empty.PRESENTED_IMAGE_SIMPLE}
          description="尚未生成结果"
          className="result-empty"
        >
          <p className="result-empty-hint">
            上传图片并选择姿势模板后，点击"生成"按钮来查看结果
          </p>
        </Empty>
      );
    }
    
    return (
      <div className="result-image-container">
        <img 
          src={resultImage} 
          alt="变换后的姿势" 
          className="result-image"
          onClick={handleFullscreen}
        />
      </div>
    );
  };
  
  // 渲染操作按钮
  const renderActionButtons = () => {
    if (!resultImage || loading) return null;
    
    return (
      <div className="result-actions">
        <Tooltip title="下载结果">
          <Button 
            type="primary" 
            icon={<DownloadOutlined />} 
            onClick={handleDownload}
            loading={downloading}
          >
            下载
          </Button>
        </Tooltip>
        
        <Tooltip title="分享结果">
          <Button 
            icon={<ShareAltOutlined />} 
            onClick={handleShare}
            loading={sharing}
          >
            分享
          </Button>
        </Tooltip>
        
        <Tooltip title="重新生成">
          <Button 
            icon={<RedoOutlined />} 
            onClick={onRetry}
          >
            重试
          </Button>
        </Tooltip>
        
        <Tooltip title="重新开始">
          <Button 
            icon={<UndoOutlined />} 
            onClick={onReset}
          >
            重置
          </Button>
        </Tooltip>
      </div>
    );
  };
  
  // 渲染全屏预览模态框
  const renderFullscreenPreview = () => {
    if (!fullscreen || !resultImage) return null;
    
    return (
      <div className="fullscreen-preview" onClick={handleFullscreen}>
        <div className="fullscreen-content" onClick={(e) => e.stopPropagation()}>
          <img src={resultImage} alt="变换后的姿势" className="fullscreen-image" />
          
          <div className="fullscreen-actions">
            <Button 
              type="primary" 
              icon={<DownloadOutlined />} 
              onClick={handleDownload}
              loading={downloading}
            >
              下载
            </Button>
            
            <Button 
              icon={<ShareAltOutlined />} 
              onClick={handleShare}
              loading={sharing}
            >
              分享
            </Button>
            
            <Button 
              icon={<FullscreenOutlined />} 
              onClick={handleFullscreen}
            >
              关闭
            </Button>
          </div>
        </div>
      </div>
    );
  };
  
  // 渲染对比预览
  const renderComparisonPreview = () => {
    if (!resultImage || !originalImage || loading) return null;
    
    return (
      <div className="comparison-preview">
        <h4>对比预览</h4>
        <div className="comparison-container">
          <div className="comparison-item">
            <p>原始图片</p>
            <div className="comparison-image-container">
              <img src={originalImage} alt="原始图片" className="comparison-image" />
            </div>
          </div>
          
          <div className="comparison-item">
            <p>变换结果</p>
            <div className="comparison-image-container">
              <img src={resultImage} alt="变换结果" className="comparison-image" />
            </div>
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="result-preview">
      <Card 
        title="结果预览" 
        className="result-card"
        extra={
          resultImage && !loading ? (
            <Tooltip title="全屏查看">
              <Button 
                type="text" 
                icon={<FullscreenOutlined />} 
                onClick={handleFullscreen}
              />
            </Tooltip>
          ) : null
        }
      >
        {renderResultContent()}
        {renderActionButtons()}
      </Card>
      
      {renderComparisonPreview()}
      {renderFullscreenPreview()}
    </div>
  );
};

export default ResultPreview;