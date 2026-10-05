package com.example.shop.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.CustomerOrder;
import com.example.shop.entity.OrderStatus;
import com.example.shop.entity.User;
import com.example.shop.service.CartService;
import com.example.shop.service.CurrentUserService;
import com.example.shop.service.OrderService;

@Controller
public class OrderController {
	private final OrderService orderService;
	private final CartService cartService;
	private final CurrentUserService currentUser;

	public OrderController(OrderService orderService, CartService cartService, CurrentUserService currentUser) {
		this.orderService = orderService;
		this.cartService = cartService;
		this.currentUser = currentUser;
	}

	@GetMapping("/checkout")
	public String checkoutForm(Model model, RedirectAttributes ra) {
		User u = currentUser.getCurrentUser();
		List<CartItem> items = cartService.getItems(u);
		if (items.isEmpty()) {
			ra.addFlashAttribute("error", "Giỏ hàng đang trống");
			return "redirect:/cart";
		}
		model.addAttribute("user", u);
		model.addAttribute("items", items);
		model.addAttribute("total", cartService.total(items));
		return "checkout";
	}

	@PostMapping("/checkout")
	public String checkout(@RequestParam String receiverName, @RequestParam String phone,
			@RequestParam String address, @RequestParam(required = false) String note, RedirectAttributes ra) {
		try {
			CustomerOrder order = orderService.checkoutCOD(currentUser.getCurrentUser(), receiverName, phone, address, note);
			ra.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn #" + order.getId() + " (thanh toán khi nhận hàng)");
			return "redirect:/orders";
		} catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
			return "redirect:/checkout";
		}
	}

	@GetMapping("/orders")
	public String history(@RequestParam(required = false) OrderStatus status, Model model) {
		model.addAttribute("orders", orderService.history(currentUser.getCurrentUser(), status));
		model.addAttribute("statuses", OrderStatus.values());
		model.addAttribute("selected", status);
		return "orders";
	}

	@PostMapping("/orders/{id}/cancel")
	public String cancel(@PathVariable Long id, RedirectAttributes ra) {
		try {
			orderService.cancel(currentUser.getCurrentUser(), id);
			ra.addFlashAttribute("success", "Đã hủy đơn #" + id);
		} catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/orders";
	}
}
