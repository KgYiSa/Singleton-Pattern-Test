package com.mj.tcs.algorithms;

import com.mj.tcs.data.model.BaseLocation;
import com.mj.tcs.data.model.Vehicle;

/**
 * A strategy for finding locations suitable for recharging vehicles.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface RechargeStrategy {

    /**
     * Returns a suitable location for recharging the given vehicle.
     *
     * @param vehicle The vehicle to be recharged.
     * @return A suitable location for recharging the given vehicle.
     */
    BaseLocation getRechargeLocation(Vehicle vehicle);
}
