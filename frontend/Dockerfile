# 构建阶段
FROM node:14 as build

WORKDIR /app

# 复制package.json和package-lock.json
COPY package*.json ./

# 安装依赖
RUN npm install

# 复制源代码
COPY . .

# 修改API地址（如果需要）
RUN if [ -f src/services/api.js ]; then \
    sed -i 's|http://localhost:8080|http://backend:8080|g' src/services/api.js; \
    fi

# 构建应用
RUN npm run build

# 部署阶段
FROM nginx:stable-alpine

# 复制构建结果到Nginx目录
COPY --from=build /app/build /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]