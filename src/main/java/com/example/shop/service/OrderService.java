package com.example.shop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.entity.*;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.OrderRepository;

@Service
public class OrderService {
	private final OrderRepository orderRepo;
	private final CartItemRepository cartRepo;

	public OrderService(OrderRepository orderRepo, CartItemRepository cartRepo) {
		this.orderRepo = orderRepo;
		this.cartRepo = cartRepo;
	}

	/** Đặt hàng, thanh toán khi nhận hàng (COD) */
	@Transactional
	public CustomerOrder checkoutCOD(User user, String name, String phone, String address, String note) {
		if (isBlank(name) || isBlank(phone) || isBlank(address))
			throw new IllegalArgumentException("Vui lòng nhập đầy đủ họ tên, số điện thoại và địa chỉ");
		if (!phone.trim().matches("\\d{9,11}"))
			throw new IllegalArgumentException("Số điện thoại không hợp lệ (9-11 chữ số)");

		List<CartItem> cart = cartRepo.findByUserOrderByIdAsc(user);
		if (cart.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống");

		CustomerOrder order = new CustomerOrder();
		order.setUser(user);
		order.setCreatedAt(LocalDateTime.now());
		order.setStatus(OrderStatus.NEW);
		order.setReceiverName(name.trim());
		order.setPhone(phone.trim());
		order.setAddress(address.trim());
		order.setNote(note);
		order.setPaymentMethod("COD");

		BigDecimal total = BigDecimal.ZERO;
		for (CartItem c : cart) {
			Product p = c.getProduct();
			if (c.getQuantity() > p.getStock())
				throw new IllegalArgumentException("Sản phẩm \"" + p.getName() + "\" chỉ còn " + p.getStock() + " trong kho");
			p.setStock(p.getStock() - c.getQuantity());

			OrderItem oi = new OrderItem();
			oi.setOrder(order);
			oi.setProduct(p);
			oi.setProductName(p.getName());
			oi.setPrice(p.getPrice());
			oi.setQuantity(c.getQuantity());
			order.getItems().add(oi);
			total = total.add(oi.getSubtotal());
		}
		order.setTotal(total);
		orderRepo.save(order);
		cartRepo.deleteAll(cart);
		return order;
	}

	/** Lịch sử đơn hàng, lọc theo trạng thái (status = null thì lấy tất cả) */
	public List<CustomerOrder> history(User user, OrderStatus status) {
		if (status == null) return orderRepo.findByUserOrderByCreatedAtDesc(user);
		return orderRepo.findByUserAndStatusOrderByCreatedAtDesc(user, status);
	}

	/** Chỉ cho hủy khi đơn còn ở trạng thái "Đơn mới" */
	@Transactional
	public void cancel(User user, Long orderId) {
		CustomerOrder o = orderRepo.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
		if (!o.getUser().getId().equals(user.getId()))
			throw new IllegalArgumentException("Không có quyền thao tác");
		if (o.getStatus() != OrderStatus.NEW)
			throw new IllegalArgumentException("Chỉ hủy được đơn ở trạng thái \"Đơn mới\"");
		for (OrderItem oi : o.getItems()) {
			Product p = oi.getProduct();
			p.setStock(p.getStock() + oi.getQuantity());
		}
		o.setStatus(OrderStatus.CANCELLED);
		orderRepo.save(o);
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
