# 人物姿势改变应用部署指南

本文档提供了多种部署人物姿势改变应用的方法，您可以根据自己的需求选择合适的部署方式。

## 方法一：使用部署脚本（推荐）

这是最简单的部署方式，适合大多数用户。

### 步骤：

1. 确保您的系统已安装以下软件：
   - Java 11+
   - Maven 3.6+
   - Node.js 14+
   - npm 6+

2. 运行部署脚本：

   ```bash
   chmod +x deploy.sh
   ./deploy.sh
   ```

3. 脚本将自动构建前后端应用，并创建一个部署包（ZIP文件）。

4. 将生成的ZIP文件上传到您的服务器。

5. 在服务器上解压部署包，并按照包中的README.md文件指示运行应用。

## 方法二：使用Docker部署

如果您熟悉Docker，可以使用Docker Compose进行容器化部署。

### 步骤：

1. 确保您的系统已安装Docker和Docker Compose。

2. 将项目文件复制到服务器。

3. 在项目根目录下运行：

   ```bash
   docker-compose up -d
   ```

4. Docker将自动构建并启动前后端容器。

5. 访问应用：
   - 前端：http://服务器IP
   - 后端API：http://服务器IP:8080

## 方法三：手动部署

如果您需要更精细的控制，可以按照以下步骤手动部署。

### 后端部署：

1. 构建后端应用：

   ```bash
   cd backend
   mvn clean package
   ```

2. 将生成的JAR文件（`target/photo-change-0.1.0.jar`）和配置文件（`src/main/resources/application.properties`）复制到服务器。

3. 在服务器上运行：

   ```bash
   java -jar photo-change-0.1.0.jar
   ```

### 前端部署：

1. 构建前端应用：

   ```bash
   cd frontend
   npm install
   npm run build
   ```

2. 将生成的`build`目录内容复制到Web服务器（如Nginx）的静态文件目录。

3. 配置Web服务器，将API请求代理到后端服务。

## 详细部署指南

如需更详细的部署说明，请参考：

- [完整部署指南](docs/deployment_guide.md) - 包含详细的服务器配置和部署步骤
- [Docker部署](docker-compose.yml) - Docker Compose配置文件

## 部署后验证

无论使用哪种部署方式，部署完成后请验证：

1. 前端应用能够正常访问
2. 能够上传图片并进行姿势检测
3. 能够选择模板并进行姿势变换
4. 能够导出处理后的图片

如有任何问题，请查看应用日志进行排查。