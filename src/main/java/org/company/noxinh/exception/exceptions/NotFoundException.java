package org.company.noxinh.exception.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Data
public class NotFoundException extends RuntimeException {
    private Map<String, String> errors;

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Data existed exception.
     *
     * @param message the message
     * @param errors  the errors
     */
    public NotFoundException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
