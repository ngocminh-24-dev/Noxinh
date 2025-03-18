package org.company.noxinh.entity.sub_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.entity.sub_entity.id.OrderProductId;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_product")
@NoArgsConstructor
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    public OrderProduct(OrderProductId id, BigDecimal price, Integer amount) {
        this.id = id;
        this.price = price;
        this.amount = amount;
    }
}