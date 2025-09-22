# 人物姿势改变应用部署指南

本文档提供了人物姿势改变应用的完整部署步骤，包括前端和后端的构建与部署方法。

## 系统要求

### 服务器要求
- 操作系统：Linux（推荐Ubuntu 20.04或更高版本）
- CPU：至少2核
- 内存：至少4GB RAM
- 存储：至少20GB可用空间

### 软件要求
- Java 11或更高版本
- Node.js 14或更高版本
- Maven 3.6或更高版本
- Nginx（用于前端部署和反向代理）

## 部署准备

1. 确保服务器已安装所需软件：

```bash
# 更新软件包列表
sudo apt update

# 安装Java
sudo apt install openjdk-11-jdk

# 安装Node.js和npm
curl -fsSL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt install -y nodejs

# 安装Maven
sudo apt install maven

# 安装Nginx
sudo apt install nginx
```

2. 克隆或上传项目代码到服务器

```bash
# 如果使用Git
git clone <项目仓库URL> /path/to/photo_change

# 或者直接上传项目文件夹到服务器
```

## 后端部署

### 1. 构建后端应用

```bash
cd /path/to/photo_change/backend
mvn clean package
```

成功构建后，将在`target`目录下生成JAR文件（如`photo-change-0.1.0.jar`）。

### 2. 配置后端应用

创建配置文件目录：

```bash
sudo mkdir -p /etc/photo-change
```

创建生产环境配置文件：

```bash
sudo nano /etc/photo-change/application-prod.properties
```

添加以下内容（根据实际情况修改）：

```properties
# 服务器配置
server.port=8080
server.servlet.context-path=/api

# 跨域配置
spring.mvc.cors.allowed-origins=http://your-domain.com
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.mvc.cors.allowed-headers=*
spring.mvc.cors.allow-credentials=true
spring.mvc.cors.max-age=3600

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# 日志配置
logging.level.root=INFO
logging.level.com.photochange=INFO
logging.file.name=/var/log/photo-change/application.log

# 图片存储路径
app.image.storage.path=/var/lib/photo-change/uploads

# 模板存储路径
app.template.storage.path=/var/lib/photo-change/templates
```

创建存储目录：

```bash
sudo mkdir -p /var/lib/photo-change/uploads
sudo mkdir -p /var/lib/photo-change/templates
sudo mkdir -p /var/log/photo-change

# 设置适当的权限
sudo chown -R $USER:$USER /var/lib/photo-change
sudo chown -R $USER:$USER /var/log/photo-change
```

### 3. 创建系统服务

创建服务文件：

```bash
sudo nano /etc/systemd/system/photo-change.service
```

添加以下内容：

```ini
[Unit]
Description=Photo Change Application
After=network.target

[Service]
User=<your-username>
WorkingDirectory=/path/to/photo_change/backend
ExecStart=/usr/bin/java -jar target/photo-change-0.1.0.jar --spring.config.additional-location=file:/etc/photo-change/application-prod.properties
Type=simple
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启用并启动服务：

```bash
sudo systemctl daemon-reload
sudo systemctl enable photo-change
sudo systemctl start photo-change
```

检查服务状态：

```bash
sudo systemctl status photo-change
```

## 前端部署

### 1. 构建前端应用

修改前端API配置：

```bash
cd /path/to/photo_change/frontend/src
```

编辑API配置文件（如`services/api.js`），将API基础URL更改为生产环境URL：

```javascript
// 修改前
const API_BASE_URL = 'http://localhost:8080';

// 修改后
const API_BASE_URL = 'http://your-domain.com/api';
```

构建前端应用：

```bash
cd /path/to/photo_change/frontend
npm install
npm run build
```

成功构建后，将在`build`目录下生成静态文件。

### 2. 配置Nginx

创建Nginx配置文件：

```bash
sudo nano /etc/nginx/sites-available/photo-change
```

添加以下内容：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/photo_change/frontend/build;
        try_files $uri $uri/ /index.html;
        expires 30d;
    }

    # 后端API代理
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 上传文件访问
    location /uploads/ {
        alias /var/lib/photo-change/uploads/;
    }

    # 错误页面
    error_page 404 /index.html;
    error_page 500 502 503 504 /50x.html;
    location = /50x.html {
        root /usr/share/nginx/html;
    }
}
```

启用站点配置：

```bash
sudo ln -s /etc/nginx/sites-available/photo-change /etc/nginx/sites-enabled/
sudo nginx -t  # 测试配置是否有效
sudo systemctl restart nginx
```

## 使用Docker部署（可选）

如果您希望使用Docker进行容器化部署，可以按照以下步骤操作：

### 1. 创建后端Dockerfile

在`backend`目录下创建`Dockerfile`：

```dockerfile
FROM openjdk:11-jdk-slim

WORKDIR /app

COPY target/photo-change-0.1.0.jar app.jar
COPY src/main/resources/application.properties /app/application.properties

RUN mkdir -p /app/uploads /app/templates

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.properties"]
```

### 2. 创建前端Dockerfile

在`frontend`目录下创建`Dockerfile`：

```dockerfile
# 构建阶段
FROM node:14 as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# 部署阶段
FROM nginx:stable-alpine

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

在`frontend`目录下创建`nginx.conf`：

```nginx
server {
    listen 80;
    server_name localhost;

    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 3. 创建Docker Compose配置

在项目根目录创建`docker-compose.yml`：

```yaml
version: '3'

services:
  backend:
    build: ./backend
    container_name: photo-change-backend
    ports:
      - "8080:8080"
    volumes:
      - photo_uploads:/app/uploads
      - photo_templates:/app/templates
    environment:
      - SPRING_PROFILES_ACTIVE=prod

  frontend:
    build: ./frontend
    container_name: photo-change-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  photo_uploads:
  photo_templates:
```

### 4. 构建和启动容器

```bash
cd /path/to/photo_change
docker-compose up -d
```

## 验证部署

1. 访问前端应用：http://your-domain.com
2. 测试上传图片和姿势变换功能
3. 检查后端日志：
   ```bash
   sudo journalctl -u photo-change
   ```

## 故障排除

### 后端服务无法启动

1. 检查日志文件：
   ```bash
   sudo journalctl -u photo-change
   ```

2. 确认Java版本：
   ```bash
   java -version
   ```

3. 检查配置文件权限：
   ```bash
   ls -la /etc/photo-change/
   ```

### 前端无法访问后端API

1. 检查Nginx配置：
   ```bash
   sudo nginx -t
   ```

2. 检查后端服务是否运行：
   ```bash
   sudo systemctl status photo-change
   ```

3. 检查防火墙设置：
   ```bash
   sudo ufw status
   ```

## 更新应用

### 更新后端

```bash
cd /path/to/photo_change/backend
git pull  # 如果使用Git管理代码
mvn clean package
sudo systemctl restart photo-change
```

### 更新前端

```bash
cd /path/to/photo_change/frontend
git pull  # 如果使用Git管理代码
npm install
npm run build
```

## 备份与恢复

### 备份数据

```bash
# 备份上传的图片和模板
sudo tar -czvf photo-change-data-$(date +%Y%m%d).tar.gz /var/lib/photo-change

# 备份配置文件
sudo tar -czvf photo-change-config-$(date +%Y%m%d).tar.gz /etc/photo-change
```

### 恢复数据

```bash
# 恢复数据文件
sudo tar -xzvf photo-change-data-YYYYMMDD.tar.gz -C /

# 恢复配置文件
sudo tar -xzvf photo-change-config-YYYYMMDD.tar.gz -C /

# 重启服务
sudo systemctl restart photo-change
```

## 结论

按照本指南完成部署后，您的人物姿势改变应用应该已经成功运行在服务器上。如果遇到任何问题，请参考故障排除部分或联系技术支持团队。