package com.example.shop.service;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;

/** Tạo dữ liệu mẫu lần chạy đầu tiên */
@Component
public class DataInitializer implements CommandLineRunner {
	private final UserRepository userRepo;
	private final ProductRepository productRepo;

	public DataInitializer(UserRepository userRepo, ProductRepository productRepo) {
		this.userRepo = userRepo;
		this.productRepo = productRepo;
	}

	@Override
	public void run(String... args) {
		if (userRepo.count() == 0) {
			User u = new User();
			u.setUsername("user");
			u.setFullName("Nguyễn Văn A");
			u.setPhone("0901234567");
			u.setAddress("Thuận An, Bình Dương");
			userRepo.save(u);
		}
		if (productRepo.count() == 0) {
			productRepo.save(new Product("Áo thun basic", new BigDecimal("150000"), 50));
			productRepo.save(new Product("Quần jean slim", new BigDecimal("350000"), 30));
			productRepo.save(new Product("Giày sneaker", new BigDecimal("690000"), 20));
			productRepo.save(new Product("Balo laptop", new BigDecimal("420000"), 15));
			productRepo.save(new Product("Mũ lưỡi trai", new BigDecimal("90000"), 100));
			productRepo.save(new Product("Tai nghe Bluetooth", new BigDecimal("550000"), 8));
		}
	}
}
