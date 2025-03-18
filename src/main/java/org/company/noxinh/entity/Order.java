package org.company.noxinh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.constants.OrderStatus;
import org.company.noxinh.dto.order.request.CreateOrderRequest;
import org.company.noxinh.entity.sub_entity.OrderProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "[order]")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "province_id", nullable = false)
    private Integer provinceId;

    @Column(name = "district_id", nullable = false)
    private Integer districtId;

    @Column(name = "ward_id", nullable = false)
    private Integer wardId;

    @Column(name = "shipping_fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "specific_address", nullable = false, length = 100)
    private String specificAddress;

    @Column(name = "total_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    public Order(CreateOrderRequest request, Integer userId) {
        this.userId = userId;
        this.recipientName = request.getRecipientName();
        this.recipientPhone = request.getRecipientPhone();
        this.provinceId = request.getProvinceId();
        this.districtId = request.getDistrictId();
        this.wardId = request.getWardId();
        if (request.getShippingFee() != null)
            this.shippingFee = request.getShippingFee();
        else
            this.shippingFee = BigDecimal.ZERO;
        this.specificAddress = request.getSpecificAddress();
        this.totalPrice = BigDecimal.ZERO;
        this.status = OrderStatus.DELIVERING;
        this.createTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
        this.completeTime = null;
    }
}