# 1. 选择基础镜像：这里选用 Java 17
# 使用 slim 版本体积更小，适合生产环境
FROM eclipse-temurin:17-jre-slim

# 2. 设置作者信息 (可选)
MAINTAINER GuangWen

# 3. 设置工作目录
WORKDIR /app

# 4. 复制打包好的 jar 包
# 注意：已经把项目打包好了，文件名叫 yiyang-admin.jar

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} yiyang-admin.jar

# 5. 暴露端口 (假设你的后端运行在 8080)
EXPOSE 8080

# 6. 启动命令
# -Djava.security.egd=file:/dev/./urandom 可以加快随机数生成，加快启动速度
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "yiyang-admin.jar"]