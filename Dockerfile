# Stage 1: Build source code bằng Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml và tải dependencies trước để tận dụng cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy toàn bộ mã nguồn và build ra file WAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng trên Tomcat
FROM tomcat:10.1-jdk17-temurin
WORKDIR /usr/local/tomcat

# Xóa các app mặc định của Tomcat
RUN rm -rf webapps/*

# Copy file war vừa build vào Tomcat (đặt tên là ROOT.war để truy cập trực tiếp qua root path /)
COPY --from=build /app/target/*.war webapps/ROOT.war

# Copy script khởi động linh hoạt cổng $PORT cho Render
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# Render sử dụng biến PORT (mặc định 8080)
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["docker-entrypoint.sh"]
