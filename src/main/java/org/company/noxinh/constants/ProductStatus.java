package org.company.noxinh.constants;

import lombok.Getter;

/**
 * The enum Product status.
 */
@Getter
public enum ProductStatus {
    /**
     * Active product status.
     */
    ACTIVE("Hoạt động", true),
    /**
     * Active product status.
     */
    INACTIVE("Không hoạt động", false);

    private final String name;
    private final Boolean booleanStatus;

    ProductStatus(final String name, final Boolean booleanStatus) {
        this.name = name;
        this.booleanStatus = booleanStatus;
    }
}
