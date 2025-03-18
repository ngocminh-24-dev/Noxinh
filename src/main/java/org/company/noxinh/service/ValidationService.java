package org.company.noxinh.service;

import org.company.noxinh.exception.exceptions.DataExistedException;

import java.util.function.Consumer;

/**
 * The interface Validation service.
 */
public interface ValidationService {
    /**
     * Validate register boolean.
     *
     * @param phone the phone
     * @param email the email
     * @throws DataExistedException the data existed exception
     */
    void validateRegister(String phone, String email) throws DataExistedException;

    /**
     * Update if changed boolean.
     *
     * @param <T>      the type parameter
     * @param newValue the new value
     * @param oldValue the old value
     * @param setter   the setter
     * @return the boolean
     */
    static <T> boolean updateIfChanged(T newValue, T oldValue, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    /**
     * Validate phone number boolean.
     *
     * @param phone the phone
     * @throws DataExistedException the data existed exception
     */
    void validatePhoneNumber(String phone) throws DataExistedException;
}
