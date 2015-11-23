/*
 * MJTCS copyright information:
 * Copyright (c) 2012 Fraunhofer IML
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package com.mj.tcs.data.order;

import com.mj.tcs.data.base.IdentifiableEntity;
import com.mj.tcs.data.model.Vehicle;

import java.io.Serializable;

/**
 * Describes the rejection of a transport order by a vehicle, and the reason
 * given for the vehicle rejecting the order.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class Rejection extends IdentifiableEntity implements Serializable {

    /**
     * The vehicle that rejected the transport order.
     * May not be <code>null</code>.
     */
    private final Vehicle vehicle;
    /**
     * The reason given for rejecting the transport order.
     * May not be <code>null</code>.
     */
    private final String reason;
    /**
     * The point of time at which the transport order was rejected/this Rejection
     * was created.
     */
    private final long timestamp;

    /**
     * Creates a new Rejection.
     *
     * @param vehicle The vehicle that rejected the transport order.
     * @param reason The reason given for rejecting the transport order.
     */
    public Rejection(Vehicle vehicle, String reason) {
        if (vehicle == null) {
            throw new NullPointerException("vehicle is null");
        }
        if (reason == null) {
            throw new NullPointerException("reason is null");
        }
        this.vehicle = vehicle;
        this.reason = reason;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the reason given for rejecting the transport order.
     *
     * @return The reason given for rejecting the transport order.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the point of time at which the transport order was rejected/this
     * Rejection was created.
     *
     * @return The point of time at which the transport order was rejected.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the vehicle that rejected the transport order.
     *
     * @return The vehicle that rejected the transport order.
     */
    public Vehicle getVehicle() {
        return vehicle;
    }
}
