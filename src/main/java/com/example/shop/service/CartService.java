package com.example.shop.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;

@Service
public class CartService {
	/** Giới hạn tối đa mỗi sản phẩm trong giỏ */
	public static final int MAX_PER_ITEM = 10;

	private final CartItemRepository cartRepo;
	private final ProductRepository productRepo;

	public CartService(CartItemRepository cartRepo, ProductRepository productRepo) {
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
	}

	public List<CartItem> getItems(User user) {
		return cartRepo.findByUserOrderByIdAsc(user);
	}

	public BigDecimal total(List<CartItem> items) {
		return items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private void checkQuantity(int qty, Product p) {
		if (qty < 1) throw new IllegalArgumentException("Số lượng tối thiểu là 1");
		if (qty > MAX_PER_ITEM) throw new IllegalArgumentException("Tối đa " + MAX_PER_ITEM + " sản phẩm cho mỗi mặt hàng");
		if (qty > p.getStock()) throw new IllegalArgumentException("Chỉ còn " + p.getStock() + " sản phẩm \"" + p.getName() + "\" trong kho");
	}

	@Transactional
	public void add(User user, Long productId, int qty) {
		Product p = productRepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
		CartItem item = cartRepo.findByUserAndProduct(user, p).orElseGet(() -> {
			CartItem c = new CartItem();
			c.setUser(user);
			c.setProduct(p);
			c.setQuantity(0);
			return c;
		});
		int newQty = item.getQuantity() + qty;
		checkQuantity(newQty, p);
		item.setQuantity(newQty);
		cartRepo.save(item);
	}

	@Transactional
	public void update(User user, Long itemId, int qty) {
		CartItem item = findOwned(user, itemId);
		checkQuantity(qty, item.getProduct());
		item.setQuantity(qty);
		cartRepo.save(item);
	}

	@Transactional
	public void remove(User user, Long itemId) {
		cartRepo.delete(findOwned(user, itemId));
	}

	private CartItem findOwned(User user, Long itemId) {
		CartItem item = cartRepo.findById(itemId)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ"));
		if (!item.getUser().getId().equals(user.getId()))
			throw new IllegalArgumentException("Không có quyền thao tác");
		return item;
	}
}
