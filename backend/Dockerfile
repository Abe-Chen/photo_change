FROM openjdk:11-jdk-slim

WORKDIR /app

# 创建目录
RUN mkdir -p /app/uploads /app/templates /app/logs

# 复制JAR文件
COPY target/photo-change-*.jar app.jar

# 复制配置文件
COPY src/main/resources/application.properties /app/application.properties

# 修改配置文件中的路径
RUN sed -i 's|app.image.storage.path=.*|app.image.storage.path=/app/uploads|g' /app/application.properties \
    && sed -i 's|app.template.storage.path=.*|app.template.storage.path=/app/templates|g' /app/application.properties

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.properties"]