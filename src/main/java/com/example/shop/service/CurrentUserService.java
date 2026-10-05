package com.example.shop.service;

import org.springframework.stereotype.Service;
import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;

/**
 * Trả về người dùng hiện tại. Đang dùng tài khoản demo "user".
 * Nếu project của bạn đã có đăng nhập, sửa hàm này để lấy user từ session / Spring Security.
 */
@Service
public class CurrentUserService {
	private final UserRepository userRepo;

	public CurrentUserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public User getCurrentUser() {
		return userRepo.findByUsername("user").orElseThrow();
	}
}
