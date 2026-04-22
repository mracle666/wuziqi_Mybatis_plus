# 使用 Java 17 运行环境
FROM openjdk:17-jre-slim

# 设置工作目录
WORKDIR /app

# 复制 JAR 文件（注意：这里的 *.jar 是占位符，实际构建时需要有文件）
COPY target/wuziqi-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
