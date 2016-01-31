package com.mj.tcs.access.status;

import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A status message containing details about a transport order.
 *
 */
public class OrderStatusMessage extends StatusMessage {

    /**
     * The transport order's name.
     */
    private String orderName = "";
    private String orderUUID = "";
    private String executingVehicleUUID;
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
    public OrderStatusMessage(long sceneId) {
        super(sceneId);
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    /**
     * Returns the transport order's UUID.
     *
     * @return The transport order's UUID.
     */
    public String getOrderUUID() {
        return orderUUID;
    }

    /**
     * Sets the transport order's UUID.
     *
     * @param orderUUID The transport order's UUID.
     */
    public void setOrderUUID(String orderUUID) {
        this.orderUUID = orderUUID;
    }

    public String getExecutingVehicleUUID() {
        return executingVehicleUUID;
    }

    public void setExecutingVehicle(TCSObjectReference<Vehicle> executingVehicle) {
        if (executingVehicle != null) {
            this.executingVehicleUUID = executingVehicle.getUUID();
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
         * The UUID of the destination location.
         */
        private String locationUUID = "";
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
         * Returns the UUID of the destination location.
         *
         * @return The UUID of the destination location.
         */
        public String getLocationUUID() {
            return locationUUID;
        }

        /**
         * Sets the UUID of the destination location.
         *
         * @param uuid The UUID of the destination location.
         */
        public void setLocationUUID(String uuid) {
            Objects.requireNonNull(uuid);

            this.locationUUID = uuid;
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
