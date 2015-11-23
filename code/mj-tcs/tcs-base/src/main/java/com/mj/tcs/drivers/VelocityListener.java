package com.mj.tcs.drivers;

/**
 * Declares the method that is called to notify a listener about a new velocity
 * value.
 *
 * @author Iryna Felko (Fraunhofer IML)
 */
public interface VelocityListener {
    /**
     * Called when a new velocity value (in mm/s) has been computed.
     *
     * @param velocityValue The new velocity value that has been computed.
     */
    void addVelocityValue(int velocityValue);
}
