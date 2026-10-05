-- 1) Tạo database (chạy 1 lần trong SSMS trước khi chạy project)
CREATE DATABASE ShopDB;
GO

-- 2) Sau khi đặt vài đơn trên web, đổi trạng thái để demo (thay 1 bằng id đơn)
-- Giá trị hợp lệ: NEW, CONFIRMED, PREPARING, SHIPPING, DELIVERING, DELIVERED, CANCELLED, RETURNED
USE ShopDB;
GO
SELECT id, status, receiver_name, total FROM orders;

UPDATE orders SET status = 'CONFIRMED'  WHERE id = 1;
UPDATE orders SET status = 'PREPARING'  WHERE id = 1;
UPDATE orders SET status = 'SHIPPING'   WHERE id = 1;
UPDATE orders SET status = 'DELIVERING' WHERE id = 1;
UPDATE orders SET status = 'DELIVERED'  WHERE id = 1;
UPDATE orders SET status = 'CANCELLED'  WHERE id = 1;
UPDATE orders SET status = 'RETURNED'   WHERE id = 1;
