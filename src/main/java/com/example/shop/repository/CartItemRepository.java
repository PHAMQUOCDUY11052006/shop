package com.example.shop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	List<CartItem> findByUserOrderByIdAsc(User user);
	Optional<CartItem> findByUserAndProduct(User user, Product product);
}
