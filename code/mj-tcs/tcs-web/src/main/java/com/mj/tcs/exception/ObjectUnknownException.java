package com.mj.tcs.exception;

/**
 * Thrown when an object was supposed to be returned/removed/modified, but could
 * not be found.
 *
 * @author Wang Zhen
 */
public class ObjectUnknownException extends TCSServerRuntimeException {

    /**
     * Creates a new ObjectExistsException with the given detail message.
     *
     * @param message The detail message.
     */
    public ObjectUnknownException(String message) {
        super(message);
    }

    /**
     * Creates a new ObjectExistsException for the given object reference.
     *
     * @param object The object reference.
     */
    public ObjectUnknownException(Object object) {
        super("Object unknown: " + (object == null ? "<null>" : object.toString()));
    }

    /**
     * Creates a new ObjectExistsException with the given detail message and
     * cause.
     *
     * @param message The detail message.
     * @param cause The cause.
     */
    public ObjectUnknownException(String message, Throwable cause) {
        super(message, cause);
    }
}
