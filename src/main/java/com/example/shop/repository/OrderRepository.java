package com.example.shop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.entity.CustomerOrder;
import com.example.shop.entity.OrderStatus;
import com.example.shop.entity.User;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
	List<CustomerOrder> findByUserOrderByCreatedAtDesc(User user);
	List<CustomerOrder> findByUserAndStatusOrderByCreatedAtDesc(User user, OrderStatus status);
}
