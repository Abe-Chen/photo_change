# 项目结构设计

## 目录结构

```
photo_change/
├── frontend/                  # 前端项目
│   ├── public/                # 静态资源
│   ├── src/                   # 源代码
│   │   ├── assets/           # 图片、字体等资源
│   │   ├── components/       # 可复用组件
│   │   │   ├── common/       # 通用组件
│   │   │   ├── upload/       # 上传相关组件
│   │   │   ├── editor/       # 编辑器相关组件
│   │   │   └── templates/    # 姿势模板组件
│   │   ├── pages/            # 页面组件
│   │   ├── services/         # API服务
│   │   ├── utils/            # 工具函数
│   │   ├── hooks/            # 自定义Hooks
│   │   ├── store/            # Redux状态管理
│   │   ├── models/           # 数据模型
│   │   ├── App.tsx           # 应用入口组件
│   │   └── index.tsx         # 入口文件
│   ├── package.json          # 依赖配置
│   └── tsconfig.json         # TypeScript配置
│
├── backend/                   # 后端项目
│   ├── src/                   # 源代码
│   │   ├── main/             # 主代码
│   │   │   ├── java/         # Java代码
│   │   │   │   └── com/photochange/
│   │   │   │       ├── config/       # 配置类
│   │   │   │       ├── controller/   # 控制器
│   │   │   │       ├── service/      # 服务层
│   │   │   │       ├── repository/   # 数据访问层
│   │   │   │       ├── model/        # 数据模型
│   │   │   │       ├── util/         # 工具类
│   │   │   │       ├── exception/    # 异常处理
│   │   │   │       └── PhotoChangeApplication.java  # 应用入口
│   │   │   └── resources/   # 资源文件
│   │   └── test/            # 测试代码
│   ├── pom.xml              # Maven配置
│   └── Dockerfile           # Docker配置
│
├── models/                   # 人体姿态估计模型
│   ├── pose_detection/      # 姿态检测模型
│   └── pose_transformation/ # 姿态变换模型
│
├── templates/                # 预设姿势模板
│
├── docs/                     # 项目文档
│   ├── api/                  # API文档
│   ├── user-guide/           # 用户指南
│   └── developer-guide/      # 开发者指南
│
├── scripts/                  # 脚本文件
│   ├── setup.sh              # 环境设置脚本
│   └── deploy.sh             # 部署脚本
│
├── docker-compose.yml        # Docker Compose配置
├── .gitignore                # Git忽略文件
└── README.md                 # 项目说明
```

## 前端组件设计

### 核心组件

1. **ImageUploader**
   - 负责图片上传和预览
   - 支持拖拽上传和文件选择
   - 处理图片格式验证和大小限制

2. **PoseDetector**
   - 集成人体姿态估计模型
   - 检测并显示人物关键点
   - 提供关键点数据给其他组件

3. **TemplateSelector**
   - 展示预设姿势模板
   - 支持模板预览和选择
   - 提供模板搜索和分类功能

4. **PoseEditor**
   - 提供关键点编辑界面
   - 支持拖拽调整关键点位置
   - 实时预览编辑效果

5. **TransformationPreview**
   - 显示姿势变换结果
   - 支持对比原图和结果
   - 提供下载和分享功能

### 状态管理

使用Redux管理全局状态，主要包括：

1. **imageState**：管理上传图片和处理后图片
2. **poseState**：管理检测到的关键点和选择的模板
3. **editorState**：管理编辑器状态和历史记录
4. **uiState**：管理界面状态和用户偏好

## 后端API设计

### RESTful API

1. **图片上传与处理**
   - `POST /api/images/upload`：上传图片
   - `GET /api/images/{id}`：获取图片
   - `DELETE /api/images/{id}`：删除图片

2. **姿态检测**
   - `POST /api/pose/detect`：检测图片中的人物姿态
   - `GET /api/pose/{id}`：获取检测结果

3. **姿势模板**
   - `GET /api/templates`：获取所有模板
   - `GET /api/templates/{id}`：获取特定模板
   - `POST /api/templates`：创建自定义模板
   - `PUT /api/templates/{id}`：更新模板
   - `DELETE /api/templates/{id}`：删除模板

4. **姿势变换**
   - `POST /api/transform`：执行姿势变换
   - `GET /api/transform/{id}`：获取变换结果

5. **用户管理**
   - `POST /api/users/register`：注册用户
   - `POST /api/users/login`：用户登录
   - `GET /api/users/profile`：获取用户信息
   - `PUT /api/users/profile`：更新用户信息

### 数据模型

1. **User**
   - id: String
   - username: String
   - email: String
   - password: String (加密)
   - createdAt: Date
   - updatedAt: Date

2. **Image**
   - id: String
   - userId: String
   - originalUrl: String
   - processedUrl: String
   - metadata: Object
   - createdAt: Date
   - updatedAt: Date

3. **PoseData**
   - id: String
   - imageId: String
   - keypoints: Array
   - confidence: Number
   - createdAt: Date

4. **Template**
   - id: String
   - name: String
   - category: String
   - keypoints: Array
   - previewUrl: String
   - isPublic: Boolean
   - userId: String
   - createdAt: Date
   - updatedAt: Date

5. **Transformation**
   - id: String
   - userId: String
   - sourceImageId: String
   - templateId: String
   - resultImageUrl: String
   - parameters: Object
   - createdAt: Date

## 数据流设计

### 图片上传与处理流程

1. 用户上传图片到前端
2. 前端进行初步验证和预处理
3. 图片上传到后端服务器
4. 后端存储原始图片并返回ID
5. 前端请求姿态检测
6. 后端执行姿态检测并返回关键点数据
7. 前端显示检测结果和关键点

### 姿势变换流程

1. 用户选择姿势模板或手动调整关键点
2. 前端发送变换请求到后端
3. 后端执行姿势变换算法
4. 后端生成结果图片并返回URL
5. 前端显示变换结果
6. 用户可下载或进一步编辑

## 安全设计

1. **认证与授权**
   - 使用JWT进行用户认证
   - 基于角色的访问控制
   - API请求限流

2. **数据安全**
   - HTTPS加密传输
   - 敏感数据加密存储
   - 定期数据备份

3. **输入验证**
   - 前后端双重验证
   - 防XSS和SQL注入
   - 文件上传安全检查

## 性能优化策略

1. **前端优化**
   - 代码分割和懒加载
   - 图片压缩和渐进式加载
   - 缓存策略优化

2. **后端优化**
   - 数据库索引优化
   - API响应缓存
   - 异步处理长时间任务

3. **算法优化**
   - 模型量化和优化
   - WebAssembly加速计算
   - 并行计算利用多核CPU

## 扩展性设计

1. **模块化架构**
   - 松耦合组件设计
   - 插件化功能扩展

2. **微服务考虑**
   - 核心服务分离
   - 独立扩展和部署

3. **API版本控制**
   - 支持多版本API
   - 平滑升级策略