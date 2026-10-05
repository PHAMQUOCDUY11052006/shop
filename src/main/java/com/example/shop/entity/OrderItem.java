package com.example.shop.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	private CustomerOrder order;
	@ManyToOne(optional = false)
	private Product product;
	private String productName;
	@Column(precision = 18, scale = 0)
	private BigDecimal price;
	private int quantity;

	public BigDecimal getSubtotal() {
		return price.multiply(BigDecimal.valueOf(quantity));
	}
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public CustomerOrder getOrder() { return order; }
	public void setOrder(CustomerOrder order) { this.order = order; }
	public Product getProduct() { return product; }
	public void setProduct(Product product) { this.product = product; }
	public String getProductName() { return productName; }
	public void setProductName(String productName) { this.productName = productName; }
	public BigDecimal getPrice() { return price; }
	public void setPrice(BigDecimal price) { this.price = price; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
}
