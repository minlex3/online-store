package com.store.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cart")
public class Cart {

    @Id
    @Column("id")
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("quantity")
    private int quantity;

    @Column
    private Long userId;

    public Cart() {
    }

    public Cart(Long id, Long productId, int quantity, Long userId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
