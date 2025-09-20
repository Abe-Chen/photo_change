#!/bin/bash

# 人物姿势改变应用启动脚本

# 颜色定义
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
RED="\033[0;31m"
NC="\033[0m" # No Color

echo -e "${GREEN}===== 人物姿势改变应用启动脚本 =====${NC}"

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo -e "${RED}错误: 未找到Java，请安装Java 11或更高版本${NC}"
    exit 1
fi

# 检查Node.js是否安装
if ! command -v node &> /dev/null; then
    echo -e "${RED}错误: 未找到Node.js，请安装Node.js 14或更高版本${NC}"
    exit 1
fi

# 检查npm是否安装
if ! command -v npm &> /dev/null; then
    echo -e "${RED}错误: 未找到npm，请安装npm${NC}"
    exit 1
fi

# 创建日志目录
mkdir -p logs

# 启动后端服务
start_backend() {
    echo -e "${YELLOW}正在启动后端服务...${NC}"
    cd backend
    
    # 检查Maven是否安装
    if command -v mvn &> /dev/null; then
        echo "使用Maven构建和启动后端..."
        mvn spring-boot:run > ../logs/backend.log 2>&1 &
        BACKEND_PID=$!
        echo "后端服务已启动，PID: $BACKEND_PID"
        echo $BACKEND_PID > ../logs/backend.pid
    else
        echo -e "${RED}错误: 未找到Maven，无法启动后端服务${NC}"
        exit 1
    fi
    
    cd ..
}

# 启动前端服务
start_frontend() {
    echo -e "${YELLOW}正在启动前端服务...${NC}"
    cd frontend
    
    # 检查是否需要安装依赖
    if [ ! -d "node_modules" ]; then
        echo "安装前端依赖..."
        npm install
    fi
    
    # 启动前端开发服务器
    npm start > ../logs/frontend.log 2>&1 &
    FRONTEND_PID=$!
    echo "前端服务已启动，PID: $FRONTEND_PID"
    echo $FRONTEND_PID > ../logs/frontend.pid
    
    cd ..
}

# 启动服务
start_services() {
    start_backend
    start_frontend
    
    echo -e "${GREEN}所有服务已启动${NC}"
    echo -e "${GREEN}后端服务地址: http://localhost:8080${NC}"
    echo -e "${GREEN}前端服务地址: http://localhost:3000${NC}"
    echo -e "${YELLOW}日志文件位于 logs/ 目录${NC}"
}

# 停止服务
stop_services() {
    echo -e "${YELLOW}正在停止服务...${NC}"
    
    # 停止后端
    if [ -f "logs/backend.pid" ]; then
        BACKEND_PID=$(cat logs/backend.pid)
        if ps -p $BACKEND_PID > /dev/null; then
            echo "停止后端服务 (PID: $BACKEND_PID)"
            kill $BACKEND_PID
        else
            echo "后端服务已经停止"
        fi
        rm logs/backend.pid
    fi
    
    # 停止前端
    if [ -f "logs/frontend.pid" ]; then
        FRONTEND_PID=$(cat logs/frontend.pid)
        if ps -p $FRONTEND_PID > /dev/null; then
            echo "停止前端服务 (PID: $FRONTEND_PID)"
            kill $FRONTEND_PID
        else
            echo "前端服务已经停止"
        fi
        rm logs/frontend.pid
    fi
    
    echo -e "${GREEN}所有服务已停止${NC}"
}

# 显示状态
show_status() {
    echo -e "${YELLOW}服务状态:${NC}"
    
    # 检查后端状态
    if [ -f "logs/backend.pid" ]; then
        BACKEND_PID=$(cat logs/backend.pid)
        if ps -p $BACKEND_PID > /dev/null; then
            echo -e "后端服务: ${GREEN}运行中${NC} (PID: $BACKEND_PID)"
        else
            echo -e "后端服务: ${RED}已停止${NC}"
        fi
    else
        echo -e "后端服务: ${RED}未启动${NC}"
    fi
    
    # 检查前端状态
    if [ -f "logs/frontend.pid" ]; then
        FRONTEND_PID=$(cat logs/frontend.pid)
        if ps -p $FRONTEND_PID > /dev/null; then
            echo -e "前端服务: ${GREEN}运行中${NC} (PID: $FRONTEND_PID)"
        else
            echo -e "前端服务: ${RED}已停止${NC}"
        fi
    else
        echo -e "前端服务: ${RED}未启动${NC}"
    fi
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo "选项:"
    echo "  start    启动所有服务"
    echo "  stop     停止所有服务"
    echo "  restart  重启所有服务"
    echo "  status   显示服务状态"
    echo "  help     显示此帮助信息"
}

# 主逻辑
case "$1" in
    start)
        start_services
        ;;
    stop)
        stop_services
        ;;
    restart)
        stop_services
        sleep 2
        start_services
        ;;
    status)
        show_status
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        echo -e "${RED}未知选项: $1${NC}"
        show_help
        exit 1
        ;;
esac

exit 0