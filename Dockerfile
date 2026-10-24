# --- Giai đoạn 1: Build file WAR bằng Maven ---
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy Java Web App trên Tomcat ---
FROM tomcat:11.0-jdk17-temurin

WORKDIR /usr/local/tomcat

# Dọn dẹp các ứng dụng mặc định của Tomcat
RUN rm -rf webapps/*

# Copy file WAR đã build từ giai đoạn 1 vào webapps và đổi tên thành ROOT.war
COPY --from=build /build/target/SportShopWeb-1.0-SNAPSHOT.war webapps/ROOT.war

# Cấu hình cổng Tomcat từ 8080 sang 10000 theo yêu cầu của Render
RUN sed -i 's/port="8080"/port="10000"/' conf/server.xml

# Vô hiệu hóa cổng shutdown 8005 để tránh xung đột health check với Render
RUN sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/' conf/server.xml

EXPOSE 10000

# Khởi chạy Tomcat
CMD ["catalina.sh", "run"]