package org.company.noxinh.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    /**
     * Delivering order status.
     */
    DELIVERING("Đang vận chuyển"),
    /**
     * canceled order status.
     */
    CANCELED("Đã huỷ"),
    /**
     * Complete order status.
     */
    COMPLETE("Hoàn thành");

    private final String name;

    OrderStatus(final String name) {this.name = name;}
}
