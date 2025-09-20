import React, { useState, useRef, useEffect } from 'react';
import { Button, Slider, Select, message, Tooltip } from 'antd';
import { UndoOutlined, RedoOutlined, SaveOutlined, ReloadOutlined } from '@ant-design/icons';
import './KeypointEditor.css';

const { Option } = Select;

/**
 * 关键点编辑器组件
 * 允许用户拖拽和调整人体姿势的关键点
 */
const KeypointEditor = ({
  imageUrl,
  keypoints,
  onKeypointsChange,
  onSave,
  width = 600,
  height = 600,
  disabled = false,
}) => {
  // 状态管理
  const [selectedKeypoint, setSelectedKeypoint] = useState(null);
  const [dragging, setDragging] = useState(false);
  const [history, setHistory] = useState([keypoints]); // 历史记录，用于撤销/重做
  const [historyIndex, setHistoryIndex] = useState(0); // 当前历史记录索引
  const [zoom, setZoom] = useState(1); // 缩放比例
  const [currentKeypoints, setCurrentKeypoints] = useState(keypoints);
  const [showLabels, setShowLabels] = useState(true); // 是否显示关键点标签
  
  const canvasRef = useRef(null);
  const containerRef = useRef(null);
  
  // 关键点颜色映射
  const keypointColors = {
    nose: '#FF0000',
    left_eye: '#FF7F00',
    right_eye: '#FFFF00',
    left_ear: '#00FF00',
    right_ear: '#0000FF',
    left_shoulder: '#4B0082',
    right_shoulder: '#9400D3',
    left_elbow: '#FF1493',
    right_elbow: '#00FFFF',
    left_wrist: '#FF00FF',
    right_wrist: '#FFD700',
    left_hip: '#32CD32',
    right_hip: '#8A2BE2',
    left_knee: '#FF6347',
    right_knee: '#40E0D0',
    left_ankle: '#7FFF00',
    right_ankle: '#FF4500',
  };
  
  // 关键点连接关系（用于绘制骨架线）
  const connections = [
    ['left_eye', 'nose'],
    ['right_eye', 'nose'],
    ['left_ear', 'left_eye'],
    ['right_ear', 'right_eye'],
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

  // 当keypoints属性变化时更新状态
  useEffect(() => {
    setCurrentKeypoints(keypoints);
    setHistory([...history.slice(0, historyIndex + 1), keypoints]);
    setHistoryIndex(historyIndex + 1);
  }, [keypoints]);

  // 绘制画布
  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    
    // 清空画布
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // 绘制背景图片
    if (imageUrl) {
      const img = new Image();
      img.src = imageUrl;
      img.onload = () => {
        // 计算图片缩放比例，保持宽高比
        const scale = Math.min(canvas.width / img.width, canvas.height / img.height);
        const imgWidth = img.width * scale * zoom;
        const imgHeight = img.height * scale * zoom;
        const x = (canvas.width - imgWidth) / 2;
        const y = (canvas.height - imgHeight) / 2;
        
        // 绘制图片
        ctx.drawImage(img, x, y, imgWidth, imgHeight);
        
        // 绘制骨架连接线
        drawConnections(ctx, currentKeypoints, connections, keypointColors, zoom);
        
        // 绘制关键点
        drawKeypoints(ctx, currentKeypoints, keypointColors, selectedKeypoint, zoom, showLabels);
      };
    } else {
      // 无图片时，仅绘制关键点和连接线
      drawConnections(ctx, currentKeypoints, connections, keypointColors, zoom);
      drawKeypoints(ctx, currentKeypoints, keypointColors, selectedKeypoint, zoom, showLabels);
    }
  }, [currentKeypoints, selectedKeypoint, zoom, imageUrl, showLabels]);

  // 绘制关键点
  const drawKeypoints = (ctx, keypoints, colors, selected, zoom, showLabels) => {
    keypoints.forEach(keypoint => {
      const { id, x, y, confidence } = keypoint;
      
      // 根据置信度调整透明度
      const alpha = Math.max(0.3, confidence || 0.5);
      
      // 设置关键点样式
      ctx.fillStyle = colors[id] || '#FF0000';
      ctx.strokeStyle = '#FFFFFF';
      ctx.lineWidth = 2;
      
      // 绘制关键点圆形
      ctx.beginPath();
      const radius = id === selected ? 8 : 6;
      ctx.arc(x * zoom, y * zoom, radius, 0, 2 * Math.PI);
      ctx.globalAlpha = alpha;
      ctx.fill();
      ctx.globalAlpha = 1;
      ctx.stroke();
      
      // 绘制关键点标签
      if (showLabels) {
        ctx.font = '12px Arial';
        ctx.fillStyle = '#FFFFFF';
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 1;
        ctx.textAlign = 'center';
        ctx.textBaseline = 'bottom';
        ctx.strokeText(id, x * zoom, y * zoom - 10);
        ctx.fillText(id, x * zoom, y * zoom - 10);
      }
    });
  };

  // 绘制骨架连接线
  const drawConnections = (ctx, keypoints, connections, colors, zoom) => {
    // 创建关键点ID到对象的映射
    const keypointMap = {};
    keypoints.forEach(kp => {
      keypointMap[kp.id] = kp;
    });
    
    // 绘制连接线
    connections.forEach(([fromId, toId]) => {
      const from = keypointMap[fromId];
      const to = keypointMap[toId];
      
      if (from && to) {
        // 计算线条颜色（两端关键点颜色的混合）
        const fromColor = colors[fromId] || '#FF0000';
        const toColor = colors[toId] || '#FF0000';
        
        // 创建渐变
        const gradient = ctx.createLinearGradient(
          from.x * zoom, from.y * zoom,
          to.x * zoom, to.y * zoom
        );
        gradient.addColorStop(0, fromColor);
        gradient.addColorStop(1, toColor);
        
        // 设置线条样式
        ctx.strokeStyle = gradient;
        ctx.lineWidth = 3;
        ctx.globalAlpha = 0.7;
        
        // 绘制线条
        ctx.beginPath();
        ctx.moveTo(from.x * zoom, from.y * zoom);
        ctx.lineTo(to.x * zoom, to.y * zoom);
        ctx.stroke();
        ctx.globalAlpha = 1;
      }
    });
  };

  // 处理鼠标按下事件
  const handleMouseDown = (e) => {
    if (disabled) return;
    
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const x = (e.clientX - rect.left) / zoom;
    const y = (e.clientY - rect.top) / zoom;
    
    // 查找点击的关键点
    const clickedKeypoint = currentKeypoints.find(kp => {
      const dx = kp.x - x;
      const dy = kp.y - y;
      return Math.sqrt(dx * dx + dy * dy) < 10 / zoom; // 10px 半径内视为点击
    });
    
    if (clickedKeypoint) {
      setSelectedKeypoint(clickedKeypoint.id);
      setDragging(true);
    } else {
      setSelectedKeypoint(null);
    }
  };

  // 处理鼠标移动事件
  const handleMouseMove = (e) => {
    if (disabled || !dragging || !selectedKeypoint) return;
    
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const x = (e.clientX - rect.left) / zoom;
    const y = (e.clientY - rect.top) / zoom;
    
    // 更新关键点位置
    const updatedKeypoints = currentKeypoints.map(kp => {
      if (kp.id === selectedKeypoint) {
        return { ...kp, x, y };
      }
      return kp;
    });
    
    setCurrentKeypoints(updatedKeypoints);
  };

  // 处理鼠标释放事件
  const handleMouseUp = () => {
    if (disabled || !dragging) return;
    
    setDragging(false);
    
    // 保存历史记录
    const newHistory = [...history.slice(0, historyIndex + 1), currentKeypoints];
    setHistory(newHistory);
    setHistoryIndex(newHistory.length - 1);
    
    // 通知父组件关键点变化
    if (onKeypointsChange) {
      onKeypointsChange(currentKeypoints);
    }
  };

  // 处理鼠标离开事件
  const handleMouseLeave = () => {
    if (dragging) {
      handleMouseUp();
    }
  };

  // 撤销操作
  const handleUndo = () => {
    if (historyIndex > 0) {
      setHistoryIndex(historyIndex - 1);
      setCurrentKeypoints(history[historyIndex - 1]);
      
      if (onKeypointsChange) {
        onKeypointsChange(history[historyIndex - 1]);
      }
    }
  };

  // 重做操作
  const handleRedo = () => {
    if (historyIndex < history.length - 1) {
      setHistoryIndex(historyIndex + 1);
      setCurrentKeypoints(history[historyIndex + 1]);
      
      if (onKeypointsChange) {
        onKeypointsChange(history[historyIndex + 1]);
      }
    }
  };

  // 重置关键点
  const handleReset = () => {
    setCurrentKeypoints(keypoints);
    setHistory([...history.slice(0, historyIndex + 1), keypoints]);
    setHistoryIndex(historyIndex + 1);
    
    if (onKeypointsChange) {
      onKeypointsChange(keypoints);
    }
    
    message.success('关键点已重置');
  };

  // 保存关键点
  const handleSave = () => {
    if (onSave) {
      onSave(currentKeypoints);
      message.success('关键点已保存');
    }
  };

  // 选择关键点
  const handleSelectKeypoint = (id) => {
    setSelectedKeypoint(id);
  };

  // 调整关键点位置
  const handleAdjustKeypoint = (axis, value) => {
    if (!selectedKeypoint) return;
    
    const updatedKeypoints = currentKeypoints.map(kp => {
      if (kp.id === selectedKeypoint) {
        return { ...kp, [axis]: value };
      }
      return kp;
    });
    
    setCurrentKeypoints(updatedKeypoints);
    
    // 通知父组件关键点变化
    if (onKeypointsChange) {
      onKeypointsChange(updatedKeypoints);
    }
  };

  return (
    <div className="keypoint-editor" ref={containerRef}>
      <div className="editor-toolbar">
        <div className="toolbar-left">
          <Tooltip title="撤销">
            <Button 
              icon={<UndoOutlined />} 
              onClick={handleUndo} 
              disabled={disabled || historyIndex <= 0}
            />
          </Tooltip>
          <Tooltip title="重做">
            <Button 
              icon={<RedoOutlined />} 
              onClick={handleRedo} 
              disabled={disabled || historyIndex >= history.length - 1}
            />
          </Tooltip>
          <Tooltip title="重置">
            <Button 
              icon={<ReloadOutlined />} 
              onClick={handleReset} 
              disabled={disabled}
            />
          </Tooltip>
        </div>
        
        <div className="toolbar-center">
          <span>缩放: </span>
          <Slider 
            min={0.5} 
            max={2} 
            step={0.1} 
            value={zoom} 
            onChange={setZoom} 
            style={{ width: 100 }} 
          />
        </div>
        
        <div className="toolbar-right">
          <Tooltip title="显示标签">
            <Button 
              type={showLabels ? 'primary' : 'default'}
              onClick={() => setShowLabels(!showLabels)}
            >
              标签
            </Button>
          </Tooltip>
          <Tooltip title="保存">
            <Button 
              type="primary" 
              icon={<SaveOutlined />} 
              onClick={handleSave} 
              disabled={disabled}
            >
              保存
            </Button>
          </Tooltip>
        </div>
      </div>
      
      <div className="editor-main">
        <div className="canvas-container">
          <canvas
            ref={canvasRef}
            width={width}
            height={height}
            onMouseDown={handleMouseDown}
            onMouseMove={handleMouseMove}
            onMouseUp={handleMouseUp}
            onMouseLeave={handleMouseLeave}
            className={disabled ? 'disabled' : ''}
          />
        </div>
        
        <div className="keypoint-controls">
          <div className="keypoint-selector">
            <h4>选择关键点</h4>
            <Select
              value={selectedKeypoint}
              onChange={handleSelectKeypoint}
              style={{ width: '100%' }}
              disabled={disabled}
            >
              {currentKeypoints.map(kp => (
                <Option key={kp.id} value={kp.id}>
                  <span 
                    className="color-dot" 
                    style={{ backgroundColor: keypointColors[kp.id] || '#FF0000' }}
                  />
                  {kp.id}
                </Option>
              ))}
            </Select>
          </div>
          
          {selectedKeypoint && (
            <div className="keypoint-position">
              <h4>调整位置</h4>
              <div className="position-control">
                <span>X: </span>
                <Slider
                  min={0}
                  max={width}
                  value={currentKeypoints.find(kp => kp.id === selectedKeypoint)?.x || 0}
                  onChange={(value) => handleAdjustKeypoint('x', value)}
                  disabled={disabled}
                />
              </div>
              <div className="position-control">
                <span>Y: </span>
                <Slider
                  min={0}
                  max={height}
                  value={currentKeypoints.find(kp => kp.id === selectedKeypoint)?.y || 0}
                  onChange={(value) => handleAdjustKeypoint('y', value)}
                  disabled={disabled}
                />
              </div>
            </div>
          )}
          
          <div className="keypoint-info">
            <h4>操作说明</h4>
            <ul>
              <li>点击关键点进行选择</li>
              <li>拖拽关键点调整位置</li>
              <li>使用滑块微调位置</li>
              <li>撤销/重做支持多步操作</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default KeypointEditor;