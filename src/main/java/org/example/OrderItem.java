package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double priceAtPurchase; // Фиксируем цену на момент покупки

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore // Чтобы не было бесконечного цикла в JSON
    private Order order;

    public OrderItem() {}
}
