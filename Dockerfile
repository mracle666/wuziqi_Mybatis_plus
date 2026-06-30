# 第一阶段：构建 JAR
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# 第二阶段：运行
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder /app/target/wuziqi-1.0.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
