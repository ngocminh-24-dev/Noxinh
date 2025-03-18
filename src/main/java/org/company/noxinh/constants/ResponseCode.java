package org.company.noxinh.constants;

import lombok.Getter;

/**
 * The enum Response code.
 */
@Getter
public enum ResponseCode {
    /**
     * C 001 response code.
     */
    C001(200, "SUCCESSFULLY"),
    /**
     * C 201 response code.
     */
    C002(500, "FAILED"),
    /**
     * The C 203.
     */
    C003(404, "NOT FOUND"),
    /**
     * C 204 response code.
     */
    C004(409, "EXISTED"),
    /**
     * The C 205.
     */
    C005(400, "BAD REQUEST"),

    /**
     * C 206 response code.
     */
    C006(200, "CREATED"),

    /**
     * C 207 response code.
     */
    C007(500, "INTERNAL_SERVER_ERROR"),

    /**
     * C 208 response code.
     */
    C008(500, "UNSUPPORTED OPERATION"),
    /**
     * C 209 response code.
     */
    C009(403, "UNAUTHORIZED"),

    /**
     * The C 210.
     */
    C210(500, "TOO MANY REQUEST");

    private final int code;
    private final String message;

    /**
     * Instantiates a new Response code.
     *
     * @param code    the code
     * @param message the message
     */
    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
