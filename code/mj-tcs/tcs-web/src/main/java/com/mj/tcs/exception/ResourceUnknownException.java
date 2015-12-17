package com.mj.tcs.exception;

/**
 * Thrown when an object is not a resource object.
 *
 * @author Wang Zhen
 */
public class ResourceUnknownException extends TcsServerRuntimeException {

    /**
     * Creates a new ObjectExistsException with the given detail message.
     *
     * @param message The detail message.
     */
    public ResourceUnknownException(String message) {
        super(message);
    }

    /**
     * Creates a new ObjectExistsException for the given object reference.
     *
     * @param object The object reference.
     */
    public ResourceUnknownException(Object object) {
        super("Resource unknown: " + (object == null ? "<null>" : object.toString()));
    }

    /**
     * Creates a new ObjectExistsException with the given detail message and
     * cause.
     *
     * @param message The detail message.
     * @param cause The cause.
     */
    public ResourceUnknownException(String message, Throwable cause) {
        super(message, cause);
    }
}
