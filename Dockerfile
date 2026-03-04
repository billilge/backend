# Dockerfile

# jdk21 Image Start
FROM eclipse-temurin:21-jre

# jar 파일 복제
COPY build/libs/*.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-Dcom.amazonaws.sdk.disableDefaultMetrics=true", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod", "-jar", "app.jar"]