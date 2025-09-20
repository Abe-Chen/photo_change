import React, { useState, useRef } from 'react';
import { Upload, Button, Card, Spin, message, Tooltip } from 'antd';
import { UploadOutlined, InboxOutlined, DeleteOutlined, EyeOutlined, LoadingOutlined } from '@ant-design/icons';
import './ImageUploader.css';

/**
 * 图片上传组件
 * 允许用户上传包含人物的图片用于姿势变换
 */
const ImageUploader = ({
  onImageUpload,
  onImageRemove,
  loading = false,
}) => {
  // 状态管理
  const [imageUrl, setImageUrl] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [previewVisible, setPreviewVisible] = useState(false);
  const [dragOver, setDragOver] = useState(false);
  const uploadRef = useRef(null);
  
  // 处理图片上传前的验证
  const beforeUpload = (file) => {
    // 检查文件类型
    const isValidType = ['image/jpeg', 'image/png', 'image/jpg', 'image/webp'].includes(file.type);
    if (!isValidType) {
      message.error('只支持 JPG/JPEG/PNG/WEBP 格式的图片！');
      return Upload.LIST_IGNORE;
    }
    
    // 检查文件大小（限制为10MB）
    const isLessThan10M = file.size / 1024 / 1024 < 10;
    if (!isLessThan10M) {
      message.error('图片大小不能超过10MB！');
      return Upload.LIST_IGNORE;
    }
    
    // 自定义上传逻辑
    handleUpload(file);
    return false; // 阻止默认上传行为
  };
  
  // 处理图片上传
  const handleUpload = async (file) => {
    setUploading(true);
    
    try {
      // 读取文件为DataURL
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const imageDataUrl = reader.result;
        setImageUrl(imageDataUrl);
        
        // 检查图片是否包含人物（模拟）
        setTimeout(() => {
          // 这里应该调用后端API进行人物检测
          // 模拟检测成功
          const detectionSuccess = true;
          
          if (detectionSuccess) {
            if (onImageUpload) {
              onImageUpload({
                file,
                dataUrl: imageDataUrl,
                fileName: file.name,
                fileSize: file.size,
                fileType: file.type,
              });
            }
            message.success('图片上传成功！');
          } else {
            setImageUrl(null);
            message.error('未检测到人物，请上传包含清晰人物的图片！');
          }
          
          setUploading(false);
        }, 1500); // 模拟检测延迟
      };
      
      reader.onerror = (error) => {
        console.error('读取文件失败:', error);
        message.error('读取文件失败，请重试！');
        setUploading(false);
      };
    } catch (error) {
      console.error('上传失败:', error);
      message.error('上传失败，请重试！');
      setUploading(false);
    }
  };
  
  // 处理图片删除
  const handleRemove = () => {
    setImageUrl(null);
    if (onImageRemove) {
      onImageRemove();
    }
    message.success('图片已移除');
  };
  
  // 处理图片预览
  const handlePreview = () => {
    setPreviewVisible(true);
  };
  
  // 处理预览关闭
  const handlePreviewClose = () => {
    setPreviewVisible(false);
  };
  
  // 处理拖拽状态
  const handleDragState = (isDragging) => {
    setDragOver(isDragging);
  };
  
  // 渲染上传区域
  const renderUploadArea = () => {
    if (imageUrl) {
      return (
        <div className="image-preview-container">
          <img src={imageUrl} alt="上传的图片" className="uploaded-image" />
          
          <div className="image-actions">
            <Tooltip title="预览">
              <Button 
                icon={<EyeOutlined />} 
                onClick={handlePreview}
                disabled={uploading}
              />
            </Tooltip>
            
            <Tooltip title="删除">
              <Button 
                danger 
                icon={<DeleteOutlined />} 
                onClick={handleRemove}
                disabled={uploading || loading}
              />
            </Tooltip>
          </div>
          
          {(uploading || loading) && (
            <div className="image-loading-overlay">
              <Spin indicator={<LoadingOutlined style={{ fontSize: 24 }} spin />} />
              <p>{uploading ? '正在处理图片...' : '正在分析姿势...'}</p>
            </div>
          )}
        </div>
      );
    }
    
    return (
      <Upload.Dragger
        name="file"
        multiple={false}
        showUploadList={false}
        beforeUpload={beforeUpload}
        className={`upload-dragger ${dragOver ? 'drag-over' : ''}`}
        onDragEnter={() => handleDragState(true)}
        onDragLeave={() => handleDragState(false)}
        onDrop={() => handleDragState(false)}
        ref={uploadRef}
        disabled={uploading || loading}
      >
        <p className="ant-upload-drag-icon">
          <InboxOutlined />
        </p>
        <p className="ant-upload-text">点击或拖拽图片到此区域上传</p>
        <p className="ant-upload-hint">
          支持 JPG/JPEG/PNG/WEBP 格式，文件大小不超过10MB<br />
          请上传包含清晰人物的图片以获得最佳效果
        </p>
        
        <Button 
          type="primary" 
          icon={<UploadOutlined />} 
          className="upload-button"
          disabled={uploading || loading}
        >
          选择图片
        </Button>
      </Upload.Dragger>
    );
  };
  
  // 渲染图片预览模态框
  const renderImagePreview = () => {
    if (!previewVisible || !imageUrl) return null;
    
    return (
      <div className="image-preview-modal" onClick={handlePreviewClose}>
        <div className="preview-content" onClick={(e) => e.stopPropagation()}>
          <img src={imageUrl} alt="预览图片" className="preview-image" />
          <Button onClick={handlePreviewClose} className="preview-close-button">
            关闭预览
          </Button>
        </div>
      </div>
    );
  };

  return (
    <div className="image-uploader">
      <Card title="上传图片" className="upload-card">
        {renderUploadArea()}
      </Card>
      {renderImagePreview()}
    </div>
  );
};

export default ImageUploader;