package com.example.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.shop.repository.ProductRepository;

@Controller
public class ProductController {
	private final ProductRepository productRepo;

	public ProductController(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}

	@GetMapping({ "/", "/products" })
	public String list(Model model) {
		model.addAttribute("products", productRepo.findAll());
		return "products";
	}
}
