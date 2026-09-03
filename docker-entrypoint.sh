#!/bin/sh
set -e

# 1. Gán cổng mặc định 8080 nếu biến PORT chưa được Render cấp
PORT=${PORT:-8080}
echo "🚀 Khởi động Tomcat SportShop trên cổng PORT: $PORT"

# 2. Vô hiệu hóa cổng Shutdown (8005) để tránh Health Check gửi HEAD / gây sập Tomcat
sed -i 's/<Server port="8005"/<Server port="-1"/g' /usr/local/tomcat/conf/server.xml

# 3. Cập nhật cổng HTTP Connector của Tomcat theo biến $PORT của Render
sed -i "s/port=\"8080\"/port=\"$PORT\"/g" /usr/local/tomcat/conf/server.xml

# 4. Khởi chạy Tomcat
exec catalina.sh run
