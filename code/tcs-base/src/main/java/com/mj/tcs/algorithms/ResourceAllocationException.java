package com.mj.tcs.algorithms;

import com.mj.tcs.KernelException;
import com.mj.tcs.algorithms.ResourceHolder;

/**
 * Thrown when allocating resources for a {@link ResourceHolder resourceHolder} is
 * impossible.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public final class ResourceAllocationException extends KernelException {

    /**
     * Creates a new ResourceAllocationException with the given detail message.
     *
     * @param message The detail message.
     */
    public ResourceAllocationException(String message) {
        super(message);
    }

    /**
     * Creates a new ResourceAllocationException with the given detail message and
     * cause.
     *
     * @param message The detail message.
     * @param cause The cause.
     */
    public ResourceAllocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
