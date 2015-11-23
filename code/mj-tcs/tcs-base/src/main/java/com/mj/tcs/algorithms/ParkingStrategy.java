package com.mj.tcs.algorithms;

import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Vehicle;

/**
 * A strategy for finding parking positions for vehicles.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface ParkingStrategy {

    /**
     * Returns a suitable parking position for the given vehicle.
     *
     * @param vehicle The vehicle to find a parking position for.
     * @return A parking position for the given vehicle, or <code>null</code>, if
     * no suitable parking position is available.
     */
    Point getParkingPosition(Vehicle vehicle);
}