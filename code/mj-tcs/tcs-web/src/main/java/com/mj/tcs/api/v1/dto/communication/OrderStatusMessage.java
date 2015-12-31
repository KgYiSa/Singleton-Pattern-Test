package com.mj.tcs.api.v1.dto.communication;

import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * A status message containing details about a transport order.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class OrderStatusMessage {

    /**
     * The transport order's name.
     */
    private String orderName = "";
    private String executingVehicleName;
    /**
     * The transport order's state.
     */
    private TransportOrder.State orderState;
    /**
     * The list of destinations making up the transport order.
     */
    private List<Destination> destinations = new LinkedList<>();

    /**
     * Creates a new instance.
     */
    public OrderStatusMessage() {
        // Do nada.
    }

    /**
     * Returns the transport order's name.
     *
     * @return The transport order's name.
     */
    public String getOrderName() {
        return orderName;
    }

    /**
     * Sets the transport order's name.
     *
     * @param orderName The transport order's name.
     */
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getExecutingVehicleName() {
        return executingVehicleName;
    }

    public void setExecutingVehicleName(TCSObjectReference<Vehicle> executingVehicle) {
        if (executingVehicle != null) {
            this.executingVehicleName = executingVehicle.getName();
        }
    }

    /**
     * Returns the transport order's current state.
     *
     * @return The transport order's current state.
     */
    public TransportOrder.State getOrderState() {
        return orderState;
    }

    /**
     * Sets the transport order's current state.
     *
     * @param orderState The transport order's current state.
     */
    public void setOrderState(TransportOrder.State orderState) {
        this.orderState = orderState;
    }

    /**
     * Returns the transport order's destinations.
     *
     * @return The transport order's destinations.
     */
    public List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * Sets the transport order's destinations.
     *
     * @param destinations The transport order's destinations.
     */
    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * A {@link com.mj.tcs.data.order.DriveOrder DriveOrder}'s destination.
     */
    public static class Destination {

        /**
         * The name of the destination location.
         */
        private String locationName = "";
        /**
         * The operation to be executed at the destination location.
         */
        private String operation = "";
        /**
         * The <code>DriveOrder</code>'s state.
         */
        private DriveOrder.State state;

        /**
         * Creates a new instance.
         */
        public Destination() {
            // Do nada.
        }

        /**
         * Returns the name of the destination location.
         *
         * @return The name of the destination location.
         */
        public String getLocationName() {
            return locationName;
        }

        /**
         * Sets the name of the destination location.
         *
         * @param name The name of the destination location.
         */
        public void setLocationName(String name) {
            if (name == null) {
                throw new NullPointerException("name is null");
            }
            this.locationName = name;
        }

        /**
         * Returns the operation to be executed at the destination location.
         *
         * @return The operation to be executed at the destination location.
         */
        public String getOperation() {
            return operation;
        }

        /**
         * Sets the operation to be executed at the destination location.
         *
         * @param operation The operation to be executed at the destination
         * location.
         */
        public void setOperation(String operation) {
            if (operation == null) {
                throw new NullPointerException("operation is null");
            }
            this.operation = operation;
        }

        /**
         * Returns the <code>DriveOrder</code>'s state.
         *
         * @return The <code>DriveOrder</code>'s state.
         */
        public DriveOrder.State getState() {
            return state;
        }

        /**
         * Sets the <code>DriveOrder</code>'s state.
         *
         * @param state The <code>DriveOrder</code>'s state.
         */
        public void setState(DriveOrder.State state) {
            if (state == null) {
                throw new NullPointerException("state is null");
            }
            this.state = state;
        }
    }
}
