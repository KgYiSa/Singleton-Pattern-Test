package com.mj.tcs.data;

/**
 * Defines some reserved/commonly used property keys and values.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface ObjectPropConstants {

    /**
     * A property key for the orientation of a vehicle on a path.
     * <p>
     * Type String (any string - details currently not specified)
     * </p>
     */
    String PATH_TRAVEL_ORIENTATION = "tcs:travelOrientation";

    /**
     * A property key for {@link com.mj.tcs.data.model.Vehicle} instances used to
     * provide the amount of energy (in W) the vehicle consumes during movement to
     * the loopback driver.
     * <p>
     * Type: Double
     * </p>
     */
    String VIRTUAL_VEHICLE_MOVEMENT_ENERGY = "tcs:virtualVehicleMovementEnergy";
    /**
     * A property key for {@link com.mj.tcs.data.model.Vehicle} instances used to
     * provide the amount of energy (in W) the vehicle consumes during operations
     * to the loopback driver.
     * <p>
     * Type: Double
     * </p>
     */
    String VIRTUAL_VEHICLE_OPERATION_ENERGY = "tcs:virtualVehicleOperationEnergy";
    /**
     * A property key for {@link com.mj.tcs.data.model.Vehicle} instances
     * used to provide the amount of energy (in W) the vehicle consumes when idle
     * to the loopback driver.
     * <p>
     * Type: Double
     * </p>
     */
    String VIRTUAL_VEHICLE_IDLE_ENERGY = "tcs:virtualVehicleIdleEnergy";
}