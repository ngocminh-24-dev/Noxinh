package org.company.noxinh.constants;

import lombok.Getter;

/**
 * The enum Role.
 */
@Getter
public enum Role {
    /**
     * The User.
     */
    USER("USER"),

    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
