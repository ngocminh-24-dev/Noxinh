package org.company.noxinh.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GlobalException extends RuntimeException {
    private Map<String, String> errors;

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public GlobalException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Data existed exception.
     *
     * @param message the message
     * @param errors  the errors
     */
    public GlobalException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}