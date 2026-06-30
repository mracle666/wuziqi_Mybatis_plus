FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY wuziqi-1.0.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
