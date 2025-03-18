package org.company.noxinh.constants;

import lombok.Getter;

@Getter
public enum ComboStatus {
    /**
     * Activate combo status.
     */
    ACTIVE("Đang kích hoạt"),
    /**
     * Inactive combo status.
     */
    INACTIVE("Không được kích hoạt");

    private final String name;

    ComboStatus(final String name) {this.name = name;}
}
