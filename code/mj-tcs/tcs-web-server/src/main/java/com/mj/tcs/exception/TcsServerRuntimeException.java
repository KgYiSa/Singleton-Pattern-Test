package com.mj.tcs.exception;

import java.io.Serializable;

/**
 * A runtime exception thrown by the mj-tcs-server.
 *
 * @author Wang Zhen
 */
public class TcsServerRuntimeException extends RuntimeException implements Serializable {

    /**
     * Constructs a new instance with no detail message.
     */
    public TcsServerRuntimeException() {
        super();
    }

    /**
     * Constructs a new instance with the specified detail message.
     *
     * @param message The detail message.
     */
    public TcsServerRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause The exception's cause.
     */
    public TcsServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance with the specified cause and a detail
     * message of <code>(cause == null ? null : cause.toString())</code> (which
     * typically contains the class and detail message of <code>cause</code>).
     *
     * @param cause The exception's cause.
     */
    public TcsServerRuntimeException(Throwable cause) {
        super(cause);
    }

}
