# 人物姿势改变应用项目结构

## 项目概述

本项目是一个人物姿势改变应用，允许用户上传图片，自动识别人物姿势，并提供多种预设姿势模板供用户选择，实现人物姿势的自动调整和手动微调。

## 目录结构

```
photo_change/
├── docs/                           # 项目文档
│   ├── development_plan.md         # 开发方案
│   ├── project_structure.md        # 项目结构说明
│   └── user_guide.md               # 用户指南
│
├── frontend/                       # 前端应用
│   ├── public/                     # 静态资源
│   │   ├── index.html              # 主HTML文件
│   │   ├── favicon.ico             # 网站图标
│   │   └── assets/                 # 静态资源文件夹
│   │       ├── images/             # 图片资源
│   │       └── pose_templates/     # 姿势模板图片
│   │
│   ├── src/                        # 源代码
│   │   ├── components/             # React组件
│   │   │   ├── App.js              # 应用主组件
│   │   │   ├── Header.js           # 头部组件
│   │   │   ├── ImageUploader.js    # 图片上传组件
│   │   │   ├── PoseTemplates.js    # 姿势模板组件
│   │   │   ├── PoseEditor.js       # 姿势编辑器组件
│   │   │   ├── AdjustmentTools.js  # 调整工具组件
│   │   │   └── ResultViewer.js     # 结果查看组件
│   │   │
│   │   ├── services/               # 服务
│   │   │   ├── api.js              # API调用服务
│   │   │   └── imageProcessor.js   # 图像处理服务
│   │   │
│   │   ├── store/                  # Redux状态管理
│   │   │   ├── actions/            # Redux actions
│   │   │   ├── reducers/           # Redux reducers
│   │   │   └── index.js            # Store配置
│   │   │
│   │   ├── utils/                  # 工具函数
│   │   │   ├── imageUtils.js       # 图像处理工具
│   │   │   └── poseUtils.js        # 姿势处理工具
│   │   │
│   │   ├── styles/                 # 样式文件
│   │   │   ├── App.css             # 应用样式
│   │   │   └── components/         # 组件样式
│   │   │
│   │   ├── index.js                # 应用入口
│   │   └── App.test.js             # 应用测试
│   │
│   ├── package.json                # 依赖配置
│   └── README.md                   # 前端说明文档
│
├── backend/                        # 后端服务
│   ├── src/                        # 源代码
│   │   ├── main/                   # 主代码
│   │   │   ├── java/               # Java代码
│   │   │   │   └── com/photochange/
│   │   │   │       ├── controller/ # 控制器
│   │   │   │       │   ├── ImageController.java       # 图片上传控制器
│   │   │   │       │   ├── PoseController.java        # 姿势处理控制器
│   │   │   │       │   └── TemplateController.java    # 模板控制器
│   │   │   │       │
│   │   │   │       ├── service/    # 服务层
│   │   │   │       │   ├── ImageService.java          # 图片服务
│   │   │   │       │   ├── PoseDetectionService.java  # 姿势检测服务
│   │   │   │       │   ├── PoseTransformService.java  # 姿势变换服务
│   │   │   │       │   └── TemplateService.java       # 模板服务
│   │   │   │       │
│   │   │   │       ├── model/      # 数据模型
│   │   │   │       │   ├── Image.java                 # 图片模型
│   │   │   │       │   ├── Pose.java                  # 姿势模型
│   │   │   │       │   ├── PoseKeypoint.java          # 姿势关键点模型
│   │   │   │       │   └── Template.java              # 模板模型
│   │   │   │       │
│   │   │   │       ├── repository/ # 数据访问层
│   │   │   │       │   ├── ImageRepository.java       # 图片仓库
│   │   │   │       │   └── TemplateRepository.java    # 模板仓库
│   │   │   │       │
│   │   │   │       ├── util/       # 工具类
│   │   │   │       │   ├── ImageUtil.java             # 图像处理工具
│   │   │   │       │   └── PoseUtil.java              # 姿势处理工具
│   │   │   │       │
│   │   │   │       ├── config/     # 配置类
│   │   │   │       │   ├── AppConfig.java             # 应用配置
│   │   │   │       │   └── MediaPipeConfig.java       # MediaPipe配置
│   │   │   │       │
│   │   │   │       └── PhotoChangeApplication.java    # 应用入口
│   │   │   │
│   │   │   └── resources/          # 资源文件
│   │   │       ├── application.properties             # 应用配置
│   │   │       ├── static/                            # 静态资源
│   │   │       └── templates/                         # 模板文件
│   │   │
│   │   └── test/                   # 测试代码
│   │       └── java/
│   │           └── com/photochange/
│   │               ├── controller/                    # 控制器测试
│   │               ├── service/                       # 服务测试
│   │               └── util/                          # 工具测试
│   │
│   ├── pom.xml                     # Maven配置
│   └── README.md                   # 后端说明文档
│
├── pose_service/                   # 姿势估计服务
│   ├── src/                        # 源代码
│   │   ├── main.py                 # 主程序
│   │   ├── pose_detection.py       # 姿势检测
│   │   ├── pose_transform.py       # 姿势变换
│   │   └── utils/                  # 工具函数
│   │       ├── image_utils.py      # 图像处理工具
│   │       └── pose_utils.py       # 姿势处理工具
│   │
│   ├── models/                     # 预训练模型
│   ├── requirements.txt            # 依赖配置
│   └── README.md                   # 服务说明文档
│
├── docker/                         # Docker配置
│   ├── frontend/                   # 前端Docker配置
│   │   └── Dockerfile              # 前端Dockerfile
│   ├── backend/                    # 后端Docker配置
│   │   └── Dockerfile              # 后端Dockerfile
│   ├── pose_service/               # 姿势服务Docker配置
│   │   └── Dockerfile              # 姿势服务Dockerfile
│   └── docker-compose.yml          # Docker Compose配置
│
├── scripts/                        # 脚本文件
│   ├── setup.sh                    # 环境设置脚本
│   ├── build.sh                    # 构建脚本
│   └── deploy.sh                   # 部署脚本
│
├── .gitignore                      # Git忽略文件
├── README.md                       # 项目说明文档
└── LICENSE                         # 许可证文件
```

## 技术栈

### 前端

- **框架**：React.js
- **UI组件库**：Material-UI
- **状态管理**：Redux
- **图像处理**：Canvas API
- **拖拽功能**：react-draggable

### 后端

- **语言**：Java
- **框架**：Spring Boot
- **数据库**：MongoDB（存储图片和姿势数据）
- **图像处理**：OpenCV

### 姿势估计服务

- **语言**：Python
- **框架**：Flask（提供API服务）
- **姿势估计**：MediaPipe
- **图像变形**：As-Rigid-As-Possible (ARAP) 算法

### 部署

- **容器化**：Docker
- **编排**：Docker Compose
- **CI/CD**：Jenkins

## 数据流

1. 用户通过前端上传图片
2. 图片发送到后端服务
3. 后端服务调用姿势估计服务识别人物关键点
4. 用户选择姿势模板
5. 后端服务调用姿势变换服务调整人物姿势
6. 结果返回给前端展示
7. 用户可进行手动微调
8. 最终结果生成并提供下载

## 接口设计

### 图片上传接口

```
POST /api/images
Content-Type: multipart/form-data

Response: {
  "id": "image_id",
  "url": "image_url",
  "width": 800,
  "height": 600
}
```

### 姿势检测接口

```
GET /api/poses/detect?imageId=image_id

Response: {
  "keypoints": [
    { "id": 0, "name": "nose", "x": 400, "y": 100 },
    { "id": 1, "name": "left_eye", "x": 420, "y": 90 },
    ...
  ]
}
```

### 模板列表接口

```
GET /api/templates

Response: {
  "templates": [
    { "id": "template_id_1", "name": "站立", "thumbnail": "url" },
    { "id": "template_id_2", "name": "坐下", "thumbnail": "url" },
    ...
  ]
}
```

### 姿势变换接口

```
POST /api/poses/transform
Content-Type: application/json

Request: {
  "imageId": "image_id",
  "templateId": "template_id"
}

Response: {
  "resultId": "result_id",
  "resultUrl": "result_url"
}
```

### 姿势微调接口

```
POST /api/poses/adjust
Content-Type: application/json

Request: {
  "imageId": "image_id",
  "keypoints": [
    { "id": 0, "x": 410, "y": 105 },
    { "id": 1, "x": 425, "y": 95 },
    ...
  ]
}

Response: {
  "resultId": "result_id",
  "resultUrl": "result_url"
}
```

## 开发环境设置

1. 克隆项目仓库
2. 运行 `scripts/setup.sh` 设置开发环境
3. 前端开发：进入 `frontend` 目录，运行 `npm install` 和 `npm start`
4. 后端开发：进入 `backend` 目录，运行 `mvn spring-boot:run`
5. 姿势服务开发：进入 `pose_service` 目录，运行 `pip install -r requirements.txt` 和 `python src/main.py`

## 部署流程

1. 运行 `scripts/build.sh` 构建项目
2. 运行 `scripts/deploy.sh` 部署项目
3. 访问应用：`http://localhost:3000`