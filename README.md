# 🛒 S-Mart - Sports E-commerce Platform

**S-Mart** là một hệ thống thương mại điện tử chuyên biệt cho đồ thể thao, được xây dựng với mục tiêu mang lại trải nghiệm mua sắm hiện đại và bảo mật. Dự án là sự kết hợp giữa sức mạnh xử lý của **Spring Boot** và tính linh hoạt của **ReactJS**.

## 🌟 Tính năng chính

### 🛡️ Hệ thống bảo mật & Xác thực

- **JWT Authentication:** Cơ chế đăng nhập an toàn với JSON Web Token.
- **Email Verification:** Kích hoạt tài khoản người dùng thông qua Gmail SMTP Server để đảm bảo người dùng thật.
- **Role-based Authorization:** Phân quyền người dùng (Admin/User) chặt chẽ.

### 📦 Quản lý sản phẩm (Admin)

- **CRUD Products:** Quản lý danh mục và thông tin sản phẩm thể thao.
- **Image Upload:** Hệ thống tải lên và quản lý hình ảnh sản phẩm thực tế trên Server.
- **Product Variants:** Quản lý biến thể sản phẩm theo kích thước (Size) và màu sắc.

## 🛠️ Công nghệ sử dụng

### Backend

- **Language:** Java 25
- **Framework:** Spring Boot 3
- **Security:** Spring Security & JWT
- **Database:** MySQL & Spring Data JPA

### Frontend (Upcoming)

- **Library:** ReactJS (Vite)
- **Styling:** Tailwind CSS / Bootstrap

## 🚀 Hướng dẫn cài đặt & Chạy dự án

### Yêu cầu hệ thống

- Java JDK 25+
- Node.js 18+
- MySQL 8.0+

### Các bước thực hiện

1. **Clone dự án:**

   ```bash
   git clone [https://github.com/your-username/sport-shop-pbl5.git](https://github.com/your-username/sport-shop-pbl5.git)
   cd sport-shop-pbl5

   ```

2. \*\*Cấu hình Backend:

   Truy cập backend/src/main/resources/application.properties.

   Cập nhật thông tin spring.datasource và spring.mail (App Password Gmail).

3. Chạy Backend:

   Bash

   cd backend
   mvn spring-boot:run

4. Chạy Frontend:

   Bash

   cd frontend
   npm install
   npm run dev
