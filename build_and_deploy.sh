#!/bin/bash

# 人物姿势改变应用一键构建与部署脚本

set -e

# 颜色定义
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
RED="\033[0;31m"
NC="\033[0m" # No Color

# 打印带颜色的信息
info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
    exit 1
}

# 检查必要的软件
check_requirements() {
    info "检查部署环境..."
    
    # 检查Java
    if ! command -v java &> /dev/null; then
        error "未找到Java，请安装Java 11或更高版本"
    fi
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    info "Java版本: $java_version"
    
    # 检查Maven
    if ! command -v mvn &> /dev/null; then
        error "未找到Maven，请安装Maven 3.6或更高版本"
    fi
    mvn_version=$(mvn --version | head -n 1)
    info "Maven版本: $mvn_version"
    
    # 检查Node.js
    if ! command -v node &> /dev/null; then
        error "未找到Node.js，请安装Node.js 14或更高版本"
    fi
    node_version=$(node --version)
    info "Node.js版本: $node_version"
    
    # 检查npm
    if ! command -v npm &> /dev/null; then
        error "未找到npm，请安装npm 6或更高版本"
    fi
    npm_version=$(npm --version)
    info "npm版本: $npm_version"
    
    # 检查Docker（可选）
    if command -v docker &> /dev/null; then
        docker_version=$(docker --version)
        info "Docker版本: $docker_version"
        has_docker=true
    else
        warn "未找到Docker，将跳过Docker部署选项"
        has_docker=false
    fi
    
    # 检查Docker Compose（可选）
    if command -v docker-compose &> /dev/null; then
        docker_compose_version=$(docker-compose --version)
        info "Docker Compose版本: $docker_compose_version"
        has_docker_compose=true
    else
        warn "未找到Docker Compose，将跳过Docker Compose部署选项"
        has_docker_compose=false
    fi
    
    info "环境检查完成"
}

# 构建后端应用
build_backend() {
    info "开始构建后端应用..."
    cd backend
    mvn clean package -DskipTests
    cd ..
    info "后端应用构建完成"
}

# 构建前端应用
build_frontend() {
    info "开始构建前端应用..."
    cd frontend
    npm install
    npm run build
    cd ..
    info "前端应用构建完成"
}

# 创建部署包
create_deployment_package() {
    info "创建部署包..."
    
    # 创建部署目录
    deploy_dir="photo_change_deploy"
    rm -rf "$deploy_dir"
    mkdir -p "$deploy_dir/backend"
    mkdir -p "$deploy_dir/frontend"
    mkdir -p "$deploy_dir/data/uploads"
    mkdir -p "$deploy_dir/data/templates"
    mkdir -p "$deploy_dir/logs"
    
    # 复制后端文件
    cp backend/target/photo-change-*.jar "$deploy_dir/backend/app.jar"
    cp backend/src/main/resources/application.properties "$deploy_dir/backend/"
    
    # 修改配置文件中的路径
    sed -i.bak 's|app.image.storage.path=.*|app.image.storage.path=../data/uploads|g' "$deploy_dir/backend/application.properties"
    sed -i.bak 's|app.template.storage.path=.*|app.template.storage.path=../data/templates|g' "$deploy_dir/backend/application.properties"
    rm "$deploy_dir/backend/application.properties.bak"
    
    # 复制前端文件
    cp -r frontend/build/* "$deploy_dir/frontend/"
    
    # 复制Docker文件（如果有Docker）
    if [ "$has_docker" = true ] && [ "$has_docker_compose" = true ]; then
        cp docker-compose.yml "$deploy_dir/"
        cp backend/Dockerfile "$deploy_dir/backend/"
        cp frontend/Dockerfile "$deploy_dir/frontend/"
        cp frontend/nginx.conf "$deploy_dir/frontend/"
    fi
    
    # 创建启动脚本
    cat > "$deploy_dir/start.sh" << 'EOF'
#!/bin/bash

# 颜色定义
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
RED="\033[0;31m"
NC="\033[0m" # No Color

# 打印带颜色的信息
info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
    exit 1
}

# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# 启动后端
start_backend() {
    info "启动后端服务..."
    cd "$SCRIPT_DIR/backend"
    nohup java -jar app.jar --spring.config.location=file:./application.properties > "$SCRIPT_DIR/logs/backend.log" 2>&1 &
    echo $! > "$SCRIPT_DIR/backend.pid"
    info "后端服务已启动，PID: $(cat "$SCRIPT_DIR/backend.pid")"
}

# 检查后端状态
check_backend() {
    if [ -f "$SCRIPT_DIR/backend.pid" ]; then
        pid=$(cat "$SCRIPT_DIR/backend.pid")
        if ps -p "$pid" > /dev/null; then
            info "后端服务正在运行，PID: $pid"
            return 0
        else
            warn "后端服务已停止运行"
            rm "$SCRIPT_DIR/backend.pid"
            return 1
        fi
    else
        warn "后端服务未启动"
        return 1
    fi
}

# 停止后端
stop_backend() {
    if [ -f "$SCRIPT_DIR/backend.pid" ]; then
        pid=$(cat "$SCRIPT_DIR/backend.pid")
        if ps -p "$pid" > /dev/null; then
            info "停止后端服务，PID: $pid"
            kill "$pid"
            sleep 2
            if ps -p "$pid" > /dev/null; then
                warn "后端服务未能正常停止，强制终止"
                kill -9 "$pid"
            fi
        else
            warn "后端服务已经停止"
        fi
        rm "$SCRIPT_DIR/backend.pid"
    else
        warn "后端服务未启动"
    fi
}

# 显示使用帮助
show_help() {
    echo "使用方法: $0 [选项]"
    echo "选项:"
    echo "  start    启动服务"
    echo "  stop     停止服务"
    echo "  restart  重启服务"
    echo "  status   查看服务状态"
    echo "  help     显示此帮助信息"
}

# 主函数
main() {
    case "$1" in
        start)
            mkdir -p "$SCRIPT_DIR/logs"
            start_backend
            info "服务已启动，请访问 http://localhost:8080 使用应用"
            ;;
        stop)
            stop_backend
            info "服务已停止"
            ;;
        restart)
            stop_backend
            sleep 2
            start_backend
            info "服务已重启"
            ;;
        status)
            check_backend
            ;;
        *)
            show_help
            ;;
    esac
}

# 执行主函数
main "$1"
EOF
    
    # 设置启动脚本可执行权限
    chmod +x "$deploy_dir/start.sh"
    
    # 创建README文件
    cat > "$deploy_dir/README.md" << 'EOF'
# 人物姿势改变应用

## 部署说明

### 方法一：直接运行

1. 启动应用：

   ```bash
   ./start.sh start
   ```

2. 停止应用：

   ```bash
   ./start.sh stop
   ```

3. 重启应用：

   ```bash
   ./start.sh restart
   ```

4. 查看状态：

   ```bash
   ./start.sh status
   ```

5. 访问应用：
   - 后端API：http://localhost:8080
   - 前端页面：将frontend目录部署到Web服务器

### 方法二：使用Docker（如果有Docker文件）

1. 构建并启动容器：

   ```bash
   docker-compose up -d
   ```

2. 停止容器：

   ```bash
   docker-compose down
   ```

3. 访问应用：
   - 前端：http://localhost
   - 后端API：http://localhost:8080

## 目录结构

- `backend/` - 后端应用
- `frontend/` - 前端应用
- `data/` - 数据目录
  - `uploads/` - 上传的图片
  - `templates/` - 模板文件
- `logs/` - 日志文件

## 故障排除

如果遇到问题，请查看日志文件：

- 后端日志：`logs/backend.log`

## 注意事项

- 首次启动时，系统会自动创建必要的目录
- 请确保服务器有足够的磁盘空间和内存
- 建议使用现代浏览器访问前端应用
EOF
    
    # 创建ZIP包
    zip_file="photo_change_deploy.zip"
    rm -f "$zip_file"
    zip -r "$zip_file" "$deploy_dir"
    
    info "部署包已创建: $zip_file"
    info "您可以将此ZIP文件上传到服务器，解压后运行 start.sh 启动应用"
}

# Docker部署（如果有Docker）
docker_deploy() {
    if [ "$has_docker" = true ] && [ "$has_docker_compose" = true ]; then
        info "开始Docker部署..."
        docker-compose up -d --build
        info "Docker部署完成，应用已启动"
        info "前端访问地址: http://localhost"
        info "后端API地址: http://localhost:8080"
    else
        warn "跳过Docker部署（未安装Docker或Docker Compose）"
    fi
}

# 主函数
main() {
    info "人物姿势改变应用一键构建与部署脚本"
    
    # 检查环境
    check_requirements
    
    # 询问部署方式
    echo "请选择部署方式："
    echo "1. 创建部署包（推荐）"
    if [ "$has_docker" = true ] && [ "$has_docker_compose" = true ]; then
        echo "2. 使用Docker部署（本地）"
        echo "3. 两者都执行"
    fi
    echo "0. 退出"
    
    read -p "请输入选项 [1]: " choice
    choice=${choice:-1}
    
    case $choice in
        1)
            build_backend
            build_frontend
            create_deployment_package
            ;;
        2)
            if [ "$has_docker" = true ] && [ "$has_docker_compose" = true ]; then
                build_backend
                build_frontend
                docker_deploy
            else
                error "未安装Docker或Docker Compose，无法使用Docker部署"
            fi
            ;;
        3)
            if [ "$has_docker" = true ] && [ "$has_docker_compose" = true ]; then
                build_backend
                build_frontend
                create_deployment_package
                docker_deploy
            else
                error "未安装Docker或Docker Compose，无法使用Docker部署"
            fi
            ;;
        0)
            info "已取消部署"
            exit 0
            ;;
        *)
            error "无效的选项: $choice"
            ;;
    esac
    
    info "部署脚本执行完成"
}

# 执行主函数
main