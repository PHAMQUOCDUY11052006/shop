# Shop - Giỏ hàng, thanh toán COD, lịch sử đơn hàng

Spring Boot 3 + Thymeleaf + Spring Data JPA + SQL Server.

## Chạy trong STS
1. SSMS: chạy phần 1 của `sql/setup.sql` để tạo database `ShopDB`.
2. Bật TCP/IP cho SQL Server (SQL Server Configuration Manager, port 1433).
3. Sửa `src/main/resources/application.properties`: username, password.
4. STS: File > Import > Maven > Existing Maven Projects, chọn thư mục `shop`.
5. Chuột phải project > Run As > Spring Boot App.
6. Mở http://localhost:8080 (lần chạy đầu tự tạo bảng và dữ liệu mẫu).

## Chức năng
- `/products`: danh sách, thêm vào giỏ
- `/cart`: thêm, xóa, sửa số lượng (min 1, max = min(tồn kho, 10))
- `/checkout`: đặt hàng COD
- `/orders`: lịch sử, lọc theo trạng thái (`?status=NEW`...), hủy đơn khi còn "Đơn mới"

## Demo đổi trạng thái
Dùng các câu UPDATE trong `sql/setup.sql`, rồi F5 trang `/orders`.
