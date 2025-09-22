#!/bin/bash

# 人物姿势改变应用部署脚本

# 颜色定义
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
RED="\033[0;31m"
NC="\033[0m" # No Color

echo -e "${GREEN}===== 人物姿势改变应用部署脚本 =====${NC}"

# 检查必要的软件
check_requirements() {
    echo -e "${YELLOW}检查部署环境...${NC}"
    
    # 检查Java
    if ! command -v java &> /dev/null; then
        echo -e "${RED}错误: 未找到Java，请安装Java 11或更高版本${NC}"
        exit 1
    fi
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo "Java版本: $java_version"
    
    # 检查Maven
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}错误: 未找到Maven，请安装Maven 3.6或更高版本${NC}"
        exit 1
    fi
    mvn_version=$(mvn --version | head -n 1)
    echo "$mvn_version"
    
    # 检查Node.js
    if ! command -v node &> /dev/null; then
        echo -e "${RED}错误: 未找到Node.js，请安装Node.js 14或更高版本${NC}"
        exit 1
    fi
    node_version=$(node --version)
    echo "Node.js版本: $node_version"
    
    # 检查npm
    if ! command -v npm &> /dev/null; then
        echo -e "${RED}错误: 未找到npm，请安装npm${NC}"
        exit 1
    fi
    npm_version=$(npm --version)
    echo "npm版本: $npm_version"
    
    echo -e "${GREEN}环境检查完成${NC}"
}

# 构建后端
build_backend() {
    echo -e "${YELLOW}构建后端应用...${NC}"
    cd backend
    
    echo "清理并打包项目..."
    mvn clean package
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}后端构建失败${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}后端构建成功${NC}"
    cd ..
}

# 构建前端
build_frontend() {
    echo -e "${YELLOW}构建前端应用...${NC}"
    cd frontend
    
    echo "安装依赖..."
    npm install
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}前端依赖安装失败${NC}"
        exit 1
    fi
    
    echo "构建前端..."
    npm run build
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}前端构建失败${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}前端构建成功${NC}"
    cd ..
}

# 创建部署目录
create_deploy_dir() {
    echo -e "${YELLOW}创建部署目录...${NC}"
    
    # 创建部署根目录
    DEPLOY_DIR="./deploy"
    mkdir -p "$DEPLOY_DIR"
    
    # 创建后端部署目录
    BACKEND_DEPLOY_DIR="$DEPLOY_DIR/backend"
    mkdir -p "$BACKEND_DEPLOY_DIR"
    
    # 创建前端部署目录
    FRONTEND_DEPLOY_DIR="$DEPLOY_DIR/frontend"
    mkdir -p "$FRONTEND_DEPLOY_DIR"
    
    # 创建数据目录
    mkdir -p "$DEPLOY_DIR/data/uploads"
    mkdir -p "$DEPLOY_DIR/data/templates"
    mkdir -p "$DEPLOY_DIR/logs"
    
    echo -e "${GREEN}部署目录创建完成${NC}"
}

# 复制构建文件到部署目录
copy_files() {
    echo -e "${YELLOW}复制文件到部署目录...${NC}"
    
    # 复制后端JAR文件
    cp backend/target/photo-change-*.jar "$BACKEND_DEPLOY_DIR/"
    
    # 复制后端配置文件
    cp backend/src/main/resources/application.properties "$BACKEND_DEPLOY_DIR/"
    
    # 修改配置文件中的路径
    sed -i.bak "s|app.image.storage.path=.*|app.image.storage.path=../data/uploads|g" "$BACKEND_DEPLOY_DIR/application.properties"
    sed -i.bak "s|app.template.storage.path=.*|app.template.storage.path=../data/templates|g" "$BACKEND_DEPLOY_DIR/application.properties"
    rm "$BACKEND_DEPLOY_DIR/application.properties.bak"
    
    # 复制前端构建文件
    cp -r frontend/build/* "$FRONTEND_DEPLOY_DIR/"
    
    # 复制启动脚本
    cp start.sh "$DEPLOY_DIR/"
    
    # 创建部署说明文件
    cat > "$DEPLOY_DIR/README.md" << EOF
# 人物姿势改变应用部署包

## 目录结构

- backend/ - 后端应用
- frontend/ - 前端应用
- data/ - 数据目录
  - uploads/ - 上传的图片
  - templates/ - 姿势模板
- logs/ - 日志目录
- start.sh - 启动脚本

## 启动应用

运行以下命令启动应用：

\`\`\`bash
chmod +x start.sh
./start.sh start
\`\`\`

## 停止应用

运行以下命令停止应用：

\`\`\`bash
./start.sh stop
\`\`\`

## 查看状态

运行以下命令查看应用状态：

\`\`\`bash
./start.sh status
\`\`\`

## 访问应用

- 前端: http://localhost:3000
- 后端API: http://localhost:8080

## 部署到服务器

请参考 docs/deployment_guide.md 文件获取详细的服务器部署指南。
EOF
    
    # 复制部署指南
    mkdir -p "$DEPLOY_DIR/docs"
    cp docs/deployment_guide.md "$DEPLOY_DIR/docs/"
    
    echo -e "${GREEN}文件复制完成${NC}"
}

# 创建部署包
create_deploy_package() {
    echo -e "${YELLOW}创建部署包...${NC}"
    
    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    PACKAGE_NAME="photo_change_deploy_$TIMESTAMP.zip"
    
    # 创建ZIP包
    cd deploy
    zip -r "../$PACKAGE_NAME" .
    cd ..
    
    echo -e "${GREEN}部署包创建成功: $PACKAGE_NAME${NC}"
}

# 主函数
main() {
    check_requirements
    build_backend
    build_frontend
    create_deploy_dir
    copy_files
    create_deploy_package
    
    echo -e "${GREEN}===== 部署准备完成 =====${NC}"
    echo -e "您可以将 $PACKAGE_NAME 部署包上传到服务器，解压后按照README.md中的说明运行应用。"
    echo -e "详细的服务器部署指南请参考 docs/deployment_guide.md 文件。"
}

# 执行主函数
main