package org.company.noxinh.constants;

import lombok.Getter;

/**
 * The enum Category status.
 */
@Getter
public enum CategoryStatus {
    /**
     * Active category status.
     */
    ACTIVE("Hoạt động"),
    /**
     * The Inactive.
     */
    INACTIVE("Không hoạt động");

    private final String name;

    CategoryStatus(String name) {
        this.name = name;
    }
}
