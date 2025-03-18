package org.company.noxinh.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.company.noxinh.constants.ResponseCode;

import java.util.Map;

/**
 * The type Response data.
 *
 * @param <T> the type parameter
 */
@Data
public class ResponseData<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     */
// PUT, PATCH, DELETE
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     */
    // GET, POST
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     * @param errors  the errors
     */
    public ResponseData(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static <T> ResponseData<T> ok(T data, String message) {
        return new ResponseData<T>(ResponseCode.C001.getCode(), message, data);
    }

    public static <T> ResponseData<T> ok(String message) {
        return new ResponseData<T>(ResponseCode.C001.getCode(), message);
    }

    public static <T> ResponseData<T> internalServer(String message, Map<String, String> errors) {
        return new ResponseData<T>(ResponseCode.C007.getCode(), message, errors);
    }

    public static <T> ResponseData<T> internalServer(String message) {
        return new ResponseData<T>(ResponseCode.C007.getCode(), message);
    }

    public static <T> ResponseData<T> badRequest(String message, Map<String, String> errors) {
        return new ResponseData<T>(ResponseCode.C005.getCode(), message, errors);
    }

    public static <T> ResponseData<T> badRequest(String message) {
        return new ResponseData<T>(ResponseCode.C005.getCode(), message);
    }

    public static <T> ResponseData<T> notFound(String message, Map<String, String> errors) {
        return new ResponseData<T>(ResponseCode.C003.getCode(), message, errors);
    }

    public static <T> ResponseData<T> notFound(String message) {
        return new ResponseData<T>(ResponseCode.C003.getCode(), message);
    }

    public static <T> ResponseData<T> failed(String message, Map<String, String> errors) {
        return new ResponseData<T>(ResponseCode.C002.getCode(), message, errors);
    }

    public static <T> ResponseData<T> failed(String message) {
        return new ResponseData<T>(ResponseCode.C002.getCode(), message);
    }
}
