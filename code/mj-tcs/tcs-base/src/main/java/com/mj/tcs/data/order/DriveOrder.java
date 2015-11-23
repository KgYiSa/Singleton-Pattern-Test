/*
 * MJTCS copyright information:
 * Copyright (c) 2006 Fraunhofer IML
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package com.mj.tcs.data.order;

import com.mj.tcs.data.base.IdentifiableEntity;
import com.mj.tcs.data.model.BaseLocation;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a list of movement steps plus an optional operation at the end of
 * this list that a vehicle is supposed to execute.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public final class DriveOrder extends IdentifiableEntity implements Serializable, Cloneable {

    /**
     * This drive order's destination.
     */
    private Destination destination;
    /**
     * A back-reference to the transport order this drive order belongs to.
     */
    private TransportOrder transportOrder;
    /**
     * This drive order's route.
     */
    private Route route;
    /**
     * This drive order's current state.
     */
    private State state = State.PRISTINE;

    /**
     * Creates a new DriveOrder.
     *
     * @param orderDestination This drive order's destination.
     */
    public DriveOrder(Destination orderDestination) {
        if (orderDestination == null) {
            throw new NullPointerException("orderDestination is null");
        }
        destination = orderDestination;
    }

    /**
     * Returns this drive order's destination.
     *
     * @return This drive order's destination.
     */
    public Destination getDestination() {
        return destination;
    }

    /**
     * Returns a reference to the transport order this drive order belongs to.
     *
     * @return A reference to the transport order this drive order belongs to.
     */
    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    /**
     * Sets a reference to the transport order this drive order belongs to.
     *
     * @param transportOrder A reference to the transport order.
     */
    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    /**
     * Returns this drive order's route.
     *
     * @return This drive order's route. May be <code>null</code> if this drive
     * order's route hasn't been calculated, yet.
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Sets this drive order's route.
     *
     * @param newRoute This drive order's new route.
     */
    public void setRoute(Route newRoute) {
        route = newRoute;
    }

    /**
     * Returns this drive order's state.
     *
     * @return This drive order's state.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets this drive order's state.
     *
     * @param newState This drive order's new state.
     */
    public void setState(State newState) {
        if (newState == null) {
            throw new NullPointerException("newState is null");
        }
        state = newState;
    }

    @Override
    public String toString() {
        return route + " -> " + destination;
    }

    @Override
    public DriveOrder clone() {
        DriveOrder clone = (DriveOrder) super.clone();
        clone.destination = destination.clone();
        return clone;
    }

    /**
     * A pair consisting of a location and an operation to be performed at that
     * location.
     */
    public static final class Destination extends IdentifiableEntity implements Serializable, Cloneable {

        /**
         * An operation constant for doing nothing.
         */
        public static final String OP_NOP = "NOP";
        /**
         * An operation constant for parking the vehicle.
         */
        public static final String OP_PARK = "PARK";
        /**
         * An operation constant for sending the vehicle to a point without a
         * location associated to it.
         */
        public static final String OP_MOVE = "MOVE";
        /**
         * The destination location.
         */
        private BaseLocation location;
        /**
         * The operation to be performed at the destination location.
         */
        private String operation;
        /**
         * Properties of this destination.
         * May contain parameters for the operation, for instance.
         */
        private Map<String, String> properties;

        /**
         * Creates a new Destination.
         *
         * @param destLocation The destination location.
         * @param destOperation The operation to be performed at the destination
         * location.
         * @param destProperties A set of destProperties. May contain parameters for
         * the operation, for instance, or anything else that might be interesting
         * for the executing vehicle driver.
         */
        public Destination(BaseLocation destLocation,
                           String destOperation,
                           Map<String, String> destProperties) {
            location = Objects.requireNonNull(destLocation, "destLocation is null");
            operation = Objects.requireNonNull(destOperation,
                    "destOperation is null");
            properties = Collections.unmodifiableMap(new TreeMap<>(
                    Objects.requireNonNull(destProperties, "properties is null")));
        }

        /**
         * Creates a new Destination.
         *
         * @param destLocation The destination location.
         * @param destOperation The operation to be performed at the destination
         * location.
         */
        public Destination(BaseLocation destLocation,
                           String destOperation) {
            this(destLocation, destOperation, new HashMap<String, String>());
        }

        /**
         * Returns the destination location.
         *
         * @return The destination location.
         */
        public BaseLocation getLocation() {
            return location;
        }

        /**
         * Returns the operation to be performed at the destination location.
         *
         * @return The operation to be performed at the destination location.
         */
        public String getOperation() {
            return operation;
        }

        /**
         * Returns the properties of this destination.
         *
         * @return The properties of this destination.
         */
        public Map<String, String> getProperties() {
            return properties;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Destination) {
                Destination other = (Destination) o;
                return location.equals(other.location)
                        && operation.equals(other.operation)
                        && properties.equals(other.properties);
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return location.hashCode() ^ operation.hashCode();
        }

        @Override
        public Destination clone() {
            Destination clone = (Destination) super.clone();
            clone.location = location.clone();
            return clone;
        }

        @Override
        public String toString() {
            return location.getName() + ":" + operation;
        }
    }

    /**
     * This enumeration defines the various states a DriveOrder may be in.
     */
    public enum State {

        /**
         * A DriveOrder's initial state, indicating it being still untouched/not
         * being processed.
         */
        PRISTINE("PRISTINE"),
        /**
         * Indicates a DriveOrder is part of a TransportOrder.
         */
        ACTIVE("ACTIVE"),
        /**
         * Indicates this drive order being processed at the moment.
         */
        TRAVELLING("TRAVELLING"),
        /**
         * Indicates the vehicle processing an order is currently executing an
         * operation.
         */
        OPERATING("OPERATING"),
        /**
         * Marks a DriveOrder as successfully completed.
         */
        FINISHED("FINISHED"),
        /**
         * Marks a DriveOrder as failed.
         */
        FAILED("FAILED");

        private String text;

        State(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static State fromString(String text) {
            Optional<State> type = Arrays.stream(State.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (type.isPresent()) {
                return type.get();
            }

            throw new IllegalArgumentException("The Point.Type enum type is no recognizable [text=" + text + "]");
        }
    }
}
