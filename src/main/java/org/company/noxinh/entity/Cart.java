package org.company.noxinh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.entity.sub_entity.id.CartId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @EmbeddedId
    private CartId id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    public Cart(CartId id, Integer amount) {
        this.id = id;
        this.createTime = LocalDateTime.now();
        this.amount = amount;
    }

    public Cart(CartId id) {
        this.id = id;
        this.createTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
    }
}