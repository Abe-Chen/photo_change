# 人物姿势改变应用 API 文档

## 概述

本文档详细说明了人物姿势改变应用的API接口设计，包括请求方法、参数、返回值和错误处理等信息，供前后端开发人员参考。

## 基础信息

- **基础URL**: `https://api.photochange.com/v1`
- **认证方式**: Bearer Token
- **数据格式**: JSON
- **版本控制**: 在URL中包含版本号（v1）

## 认证

### 获取访问令牌

```
POST /auth/token
```

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

#### 响应

```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

### 刷新令牌

```
POST /auth/refresh
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 响应

```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

## 图片处理

### 上传图片

```
POST /images/upload
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | multipart/form-data |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| image | file | 是 | 要上传的图片文件 |
| name | string | 否 | 图片名称 |

#### 响应

```json
{
  "image_id": "img_123456789",
  "url": "https://storage.photochange.com/images/img_123456789.jpg",
  "name": "my_photo",
  "width": 1200,
  "height": 800,
  "format": "jpg",
  "size": 1024000,
  "created_at": "2023-06-15T10:30:00Z"
}
```

### 获取图片信息

```
GET /images/{image_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| image_id | string | 图片ID |

#### 响应

```json
{
  "image_id": "img_123456789",
  "url": "https://storage.photochange.com/images/img_123456789.jpg",
  "name": "my_photo",
  "width": 1200,
  "height": 800,
  "format": "jpg",
  "size": 1024000,
  "created_at": "2023-06-15T10:30:00Z",
  "detection_status": "completed",
  "has_person": true
}
```

### 删除图片

```
DELETE /images/{image_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| image_id | string | 图片ID |

#### 响应

```json
{
  "success": true,
  "message": "Image deleted successfully"
}
```

## 姿势检测

### 检测人物姿势

```
POST /poses/detect
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | application/json |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| image_id | string | 是 | 图片ID |

#### 响应

```json
{
  "detection_id": "det_123456789",
  "image_id": "img_123456789",
  "status": "processing",
  "estimated_time": 5
}
```

### 获取检测结果

```
GET /poses/detect/{detection_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| detection_id | string | 检测ID |

#### 响应

```json
{
  "detection_id": "det_123456789",
  "image_id": "img_123456789",
  "status": "completed",
  "keypoints": [
    {"id": "nose", "x": 600, "y": 200},
    {"id": "left_shoulder", "x": 500, "y": 300},
    {"id": "right_shoulder", "x": 700, "y": 300},
    {"id": "left_elbow", "x": 400, "y": 400},
    {"id": "right_elbow", "x": 800, "y": 400},
    {"id": "left_wrist", "x": 300, "y": 500},
    {"id": "right_wrist", "x": 900, "y": 500},
    {"id": "left_hip", "x": 550, "y": 500},
    {"id": "right_hip", "x": 650, "y": 500},
    {"id": "left_knee", "x": 550, "y": 650},
    {"id": "right_knee", "x": 650, "y": 650},
    {"id": "left_ankle", "x": 550, "y": 750},
    {"id": "right_ankle", "x": 650, "y": 750}
  ],
  "segments": {
    "torso": [[500, 300], [700, 300], [650, 500], [550, 500]],
    "left_arm": [[500, 300], [400, 400], [300, 500]],
    "right_arm": [[700, 300], [800, 400], [900, 500]],
    "left_leg": [[550, 500], [550, 650], [550, 750]],
    "right_leg": [[650, 500], [650, 650], [650, 750]]
  },
  "confidence": 0.95,
  "created_at": "2023-06-15T10:35:00Z",
  "completed_at": "2023-06-15T10:35:05Z"
}
```

## 姿势模板

### 获取模板列表

```
GET /templates
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 查询参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| category | string | 否 | 模板类别（standing, sitting, action, special） |
| page | integer | 否 | 页码，默认为1 |
| limit | integer | 否 | 每页数量，默认为20，最大为50 |

#### 响应

```json
{
  "templates": [
    {
      "template_id": "tpl_123456789",
      "name": "站立姿势1",
      "category": "standing",
      "thumbnail_url": "https://storage.photochange.com/templates/tpl_123456789_thumb.jpg",
      "preview_url": "https://storage.photochange.com/templates/tpl_123456789_preview.jpg",
      "keypoints": [...],
      "popularity": 4.8
    },
    {
      "template_id": "tpl_987654321",
      "name": "跑步姿势",
      "category": "action",
      "thumbnail_url": "https://storage.photochange.com/templates/tpl_987654321_thumb.jpg",
      "preview_url": "https://storage.photochange.com/templates/tpl_987654321_preview.jpg",
      "keypoints": [...],
      "popularity": 4.5
    }
  ],
  "total": 42,
  "page": 1,
  "limit": 20,
  "pages": 3
}
```

### 获取模板详情

```
GET /templates/{template_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| template_id | string | 模板ID |

#### 响应

```json
{
  "template_id": "tpl_123456789",
  "name": "站立姿势1",
  "category": "standing",
  "description": "自然站立姿势，双手放在身体两侧",
  "thumbnail_url": "https://storage.photochange.com/templates/tpl_123456789_thumb.jpg",
  "preview_url": "https://storage.photochange.com/templates/tpl_123456789_preview.jpg",
  "keypoints": [
    {"id": "nose", "x": 600, "y": 200},
    {"id": "left_shoulder", "x": 550, "y": 300},
    {"id": "right_shoulder", "x": 650, "y": 300},
    {"id": "left_elbow", "x": 500, "y": 400},
    {"id": "right_elbow", "x": 700, "y": 400},
    {"id": "left_wrist", "x": 450, "y": 500},
    {"id": "right_wrist", "x": 750, "y": 500},
    {"id": "left_hip", "x": 575, "y": 500},
    {"id": "right_hip", "x": 625, "y": 500},
    {"id": "left_knee", "x": 575, "y": 650},
    {"id": "right_knee", "x": 625, "y": 650},
    {"id": "left_ankle", "x": 575, "y": 800},
    {"id": "right_ankle", "x": 625, "y": 800}
  ],
  "segments": {...},
  "popularity": 4.8,
  "created_at": "2023-01-15T08:30:00Z"
}
```

## 姿势变换

### 创建姿势变换任务

```
POST /transformations
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | application/json |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| image_id | string | 是 | 图片ID |
| template_id | string | 是 | 模板ID |
| custom_keypoints | array | 否 | 自定义关键点位置 |

#### 响应

```json
{
  "transformation_id": "trans_123456789",
  "image_id": "img_123456789",
  "template_id": "tpl_123456789",
  "status": "processing",
  "estimated_time": 15,
  "created_at": "2023-06-15T11:00:00Z"
}
```

### 获取变换任务状态

```
GET /transformations/{transformation_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| transformation_id | string | 变换任务ID |

#### 响应

```json
{
  "transformation_id": "trans_123456789",
  "image_id": "img_123456789",
  "template_id": "tpl_123456789",
  "status": "completed",
  "result_url": "https://storage.photochange.com/results/trans_123456789.jpg",
  "thumbnail_url": "https://storage.photochange.com/results/trans_123456789_thumb.jpg",
  "width": 1200,
  "height": 800,
  "created_at": "2023-06-15T11:00:00Z",
  "completed_at": "2023-06-15T11:01:30Z"
}
```

### 更新变换任务

```
PUT /transformations/{transformation_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | application/json |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| transformation_id | string | 变换任务ID |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| custom_keypoints | array | 是 | 自定义关键点位置 |

#### 响应

```json
{
  "transformation_id": "trans_123456789",
  "status": "processing",
  "estimated_time": 10
}
```

### 取消变换任务

```
DELETE /transformations/{transformation_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| transformation_id | string | 变换任务ID |

#### 响应

```json
{
  "success": true,
  "message": "Transformation cancelled successfully"
}
```

## 结果导出

### 导出结果图片

```
POST /exports
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | application/json |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| transformation_id | string | 是 | 变换任务ID |
| format | string | 否 | 导出格式（jpg, png），默认为jpg |
| quality | string | 否 | 图片质量（low, medium, high），默认为high |
| width | integer | 否 | 自定义宽度 |
| height | integer | 否 | 自定义高度 |

#### 响应

```json
{
  "export_id": "exp_123456789",
  "transformation_id": "trans_123456789",
  "status": "processing",
  "estimated_time": 5
}
```

### 获取导出状态

```
GET /exports/{export_id}
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 路径参数

| 参数名 | 类型 | 描述 |
|--------|------|------|
| export_id | string | 导出ID |

#### 响应

```json
{
  "export_id": "exp_123456789",
  "transformation_id": "trans_123456789",
  "status": "completed",
  "download_url": "https://storage.photochange.com/exports/exp_123456789.jpg",
  "format": "jpg",
  "quality": "high",
  "width": 1200,
  "height": 800,
  "size": 1536000,
  "created_at": "2023-06-15T11:10:00Z",
  "completed_at": "2023-06-15T11:10:10Z",
  "expires_at": "2023-06-22T11:10:10Z"
}
```

## 用户管理

### 获取用户信息

```
GET /users/me
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |

#### 响应

```json
{
  "user_id": "usr_123456789",
  "username": "john_doe",
  "email": "john.doe@example.com",
  "created_at": "2023-01-01T00:00:00Z",
  "subscription": {
    "plan": "premium",
    "status": "active",
    "expires_at": "2023-12-31T23:59:59Z"
  },
  "usage": {
    "transformations": {
      "used": 45,
      "limit": 100
    },
    "storage": {
      "used": 256000000,
      "limit": 1073741824
    }
  }
}
```

### 更新用户信息

```
PUT /users/me
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | application/json |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | string | 否 | 新用户名 |
| email | string | 否 | 新电子邮件 |
| password | string | 否 | 新密码 |
| current_password | string | 是（如果更改密码） | 当前密码 |

#### 响应

```json
{
  "success": true,
  "message": "User information updated successfully"
}
```

## 错误处理

所有API错误响应都遵循以下格式：

```json
{
  "error": {
    "code": "error_code",
    "message": "Error message description",
    "details": {...}
  }
}
```

### 常见错误代码

| 错误代码 | HTTP状态码 | 描述 |
|----------|------------|------|
| authentication_required | 401 | 需要认证 |
| invalid_credentials | 401 | 无效的凭据 |
| access_denied | 403 | 访问被拒绝 |
| resource_not_found | 404 | 资源未找到 |
| validation_error | 422 | 请求参数验证失败 |
| rate_limit_exceeded | 429 | 超出请求频率限制 |
| server_error | 500 | 服务器内部错误 |

## 速率限制

为了确保API的稳定性和可用性，我们对API请求实施了速率限制。限制根据用户的订阅计划而有所不同：

| 计划 | 请求限制 |
|------|----------|
| 免费 | 60次/小时 |
| 基础 | 300次/小时 |
| 高级 | 1000次/小时 |
| 企业 | 5000次/小时 |

当达到速率限制时，API将返回429状态码和以下响应：

```json
{
  "error": {
    "code": "rate_limit_exceeded",
    "message": "Rate limit exceeded. Please try again later.",
    "details": {
      "limit": 60,
      "remaining": 0,
      "reset_at": "2023-06-15T12:00:00Z"
    }
  }
}
```

## Webhook通知

为了支持异步处理，API提供了Webhook通知功能，可以在特定事件发生时向指定的URL发送通知。

### 配置Webhook

```
POST /webhooks
```

#### 请求头

| 参数名 | 描述 |
|--------|------|
| Authorization | Bearer {access_token} |
| Content-Type | application/json |

#### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| url | string | 是 | Webhook接收URL |
| events | array | 是 | 订阅的事件类型 |
| secret | string | 否 | 用于验证Webhook请求的密钥 |

#### 响应

```json
{
  "webhook_id": "wh_123456789",
  "url": "https://example.com/webhook",
  "events": ["transformation.completed", "export.completed"],
  "created_at": "2023-06-15T12:00:00Z"
}
```

### 支持的事件类型

| 事件类型 | 描述 |
|----------|------|
| detection.completed | 姿势检测完成 |
| transformation.processing | 姿势变换开始处理 |
| transformation.completed | 姿势变换完成 |
| transformation.failed | 姿势变换失败 |
| export.completed | 导出完成 |
| export.failed | 导出失败 |

### Webhook请求格式

```json
{
  "event": "transformation.completed",
  "created_at": "2023-06-15T11:01:30Z",
  "data": {
    "transformation_id": "trans_123456789",
    "image_id": "img_123456789",
    "template_id": "tpl_123456789",
    "status": "completed",
    "result_url": "https://storage.photochange.com/results/trans_123456789.jpg"
  }
}
```

## 版本历史

| 版本 | 发布日期 | 描述 |
|------|----------|------|
| v1 | 2023-06-01 | 初始版本 |

## 附录

### 关键点ID列表

| ID | 描述 |
|----|------|
| nose | 鼻子 |
| left_eye | 左眼 |
| right_eye | 右眼 |
| left_ear | 左耳 |
| right_ear | 右耳 |
| left_shoulder | 左肩 |
| right_shoulder | 右肩 |
| left_elbow | 左肘 |
| right_elbow | 右肘 |
| left_wrist | 左手腕 |
| right_wrist | 右手腕 |
| left_hip | 左髋 |
| right_hip | 右髋 |
| left_knee | 左膝 |
| right_knee | 右膝 |
| left_ankle | 左踝 |
| right_ankle | 右踝 |

### 模板类别

| 类别 | 描述 |
|------|------|
| standing | 站立姿势 |
| sitting | 坐姿 |
| action | 动作姿势（跑步、跳跃等） |
| special | 特殊姿势（舞蹈、瑜伽等） |
| custom | 用户自定义姿势 |