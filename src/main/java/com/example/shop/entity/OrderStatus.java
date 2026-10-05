package com.example.shop.entity;

public enum OrderStatus {
	NEW("Đơn mới"),
	CONFIRMED("Đã xác nhận"),
	PREPARING("Chuẩn bị hàng"),
	SHIPPING("Vận chuyển"),
	DELIVERING("Giao hàng"),
	DELIVERED("Đã giao"),
	CANCELLED("Đơn hủy"),
	RETURNED("Đơn hoàn");

	private final String label;

	OrderStatus(String label) { this.label = label; }

	public String getLabel() { return label; }
}
