# --- Stage 1: Build (Dùng để build file .jar) ---
# Dùng image Maven kèm JDK 17 (hoặc 21 tùy project của bạn)
FROM maven:3.9.6-amazoncorretto-17 AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy file pom.xml trước để cache các dependencies
COPY pom.xml .
# Tải dependencies về (bước này sẽ được cache nếu pom.xml không đổi)
RUN mvn dependency:go-offline

# Copy toàn bộ source code vào
COPY src ./src

# Build ra file .jar (Skip test để build nhanh hơn, test nên chạy ở CI/CD)
RUN mvn clean

# --- Stage 2: Run (Dùng để chạy ứng dụng) ---
# Dùng image Amazon Corretto siêu nhẹ chỉ chứa JRE
FROM amazoncorretto:17-alpine-jdk

# Thêm biến môi trường nếu cần (Optional)
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Copy file .jar từ Stage 1 sang Stage 2
# Lưu ý: check lại đường dẫn target/ tên file jar của bạn
COPY --from=build /app/target/*.jar app.jar

# Expose port mà ứng dụng chạy (thường là 8080)
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]