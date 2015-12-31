package com.mj.tcs.access.status;

import com.mj.tcs.data.model.Vehicle;

/**
 * A status message containing information about a vehicle.
 *
 */
public class VehicleStatusMessage extends StatusMessage {

    /**
     * The vehicle's name.
     */
    private String vehicleUUID = "";
    /**
     * Precise position of the vehicle.
     */
    private PrecisePosition precisePosition;
    /**
     * Position of the vehicle.
     */
    private String positionUUID;
    /**
     * State of the vehicle.
     */
    private Vehicle.State state;
    /**
     * Processing state of the vehicle.
     */
    private Vehicle.ProcState procState;

    private double energyLevel;

    /**
     * Creates a new instance.
     */
    public VehicleStatusMessage(long sceneId) {
        super(sceneId);
    }

    /**
     * Returns the vehicle's name.
     *
     * @return The vehicle's name.
     */
    public String getVehicleUUID() {
        return vehicleUUID;
    }

    /**
     * Sets the vehicle's name.
     *
     * @param vehicleUUID The vehicle's name.
     */
    public void setVehicleUUID(String vehicleUUID) {
        this.vehicleUUID = vehicleUUID;
    }

    /**
     * Returns the position UUID of the vehicle.
     *
     * @return The vehicle's position UUID.
     */
    public String getPositionUUID() {
        return positionUUID;
    }

    /**
     * Sets the position UUID of the vehicle.
     *
     * @param positionUUID The vehicle's position UUID.
     */
    public void setPositionUUID(String positionUUID) {
        this.positionUUID = positionUUID;
    }

    /**
     * Returns the precise position of the vehicle.
     *
     * @return The precise position.
     */
    public PrecisePosition getPrecisePosition() {
        return precisePosition;
    }

    /**
     * Sets the precise position.
     *
     * @param precisePosition The precise position.
     */
    public void setPrecisePosition(PrecisePosition precisePosition) {
        this.precisePosition = precisePosition;
    }

    /**
     * Returns the vehicle's state.
     *
     * @return The current vehicle state.
     */
    public Vehicle.State getState() {
        return state;
    }

    /**
     * Sets the vehicle's state.
     *
     * @param state The vehicle state.
     */
    public void setState(Vehicle.State state) {
        this.state = state;
    }

    /**
     * Returns the vehicle's processing state.
     *
     * @return The current vehicle processing state.
     */
    public Vehicle.ProcState getProcState() {
        return procState;
    }

    /**
     * Sets the vehicle's processing state.
     *
     * @param procState The vehicle processing state.
     */
    public void setProcState(Vehicle.ProcState procState) {
        this.procState = procState;
    }

    public double getEngergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(double energyLevel) {
        this.energyLevel = energyLevel;
    }

    /**
     * A precise position of a vehicle.
     */
    public static final class PrecisePosition {

        /**
         * X coordinate of the position.
         */
        private long x;
        /**
         * Y coordinate of the position.
         */
        private long y;
        /**
         * Z coordinate of the position.
         */
        private long z;

        /**
         * Creates a new instance.
         */
        public PrecisePosition() {
            // Do nada.
        }

        /**
         * Creates a new instance.
         * @param x x value
         * @param y y value
         * @param z z value
         */
        public PrecisePosition(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Get the x coordinate value of the precise position.
         * @return x value
         */
        public long getX() {
            return x;
        }

        /**
         * Set the x coordinate value of the precise position.
         * @param x x value
         */
        public void setX(long x) {
            this.x = x;
        }

        /**
         * Get the y coordinate value of the precise position.
         * @return y value
         */
        public long getY() {
            return y;
        }

        /**
         * Set the y coordinate value of the precise position.
         * @param y y value
         */
        public void setY(long y) {
            this.y = y;
        }

        /**
         * Get the z coordinate value of the precise position.
         * @return z value
         */
        public long getZ() {
            return z;
        }

        /**
         * Set the z coordinate value of the precise position.
         * @param z z value
         */
        public void setZ(long z) {
            this.z = z;
        }
    }
}
