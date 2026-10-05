package com.example.shop.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.User;
import com.example.shop.service.CartService;
import com.example.shop.service.CurrentUserService;

@Controller
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;
	private final CurrentUserService currentUser;

	public CartController(CartService cartService, CurrentUserService currentUser) {
		this.cartService = cartService;
		this.currentUser = currentUser;
	}

	@GetMapping
	public String view(Model model) {
		User u = currentUser.getCurrentUser();
		List<CartItem> items = cartService.getItems(u);
		model.addAttribute("items", items);
		model.addAttribute("total", cartService.total(items));
		model.addAttribute("maxPerItem", CartService.MAX_PER_ITEM);
		return "cart";
	}

	@PostMapping("/add")
	public String add(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity,
			RedirectAttributes ra) {
		try {
			cartService.add(currentUser.getCurrentUser(), productId, quantity);
			ra.addFlashAttribute("success", "Đã thêm vào giỏ hàng");
		} catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/products";
	}

	@PostMapping("/update")
	public String update(@RequestParam Long itemId, @RequestParam int quantity, RedirectAttributes ra) {
		try {
			cartService.update(currentUser.getCurrentUser(), itemId, quantity);
			ra.addFlashAttribute("success", "Đã cập nhật số lượng");
		} catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/cart";
	}

	@PostMapping("/remove")
	public String remove(@RequestParam Long itemId, RedirectAttributes ra) {
		try {
			cartService.remove(currentUser.getCurrentUser(), itemId);
			ra.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ");
		} catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/cart";
	}
}
