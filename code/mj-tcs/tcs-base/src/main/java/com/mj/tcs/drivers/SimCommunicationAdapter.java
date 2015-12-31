package com.mj.tcs.drivers;

/**
 * This interface declares methods that a vehicle driver intended for simulation
 * must implement.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface SimCommunicationAdapter
    extends CommunicationAdapter {

  /**
   * Sets a time factor for the simulation.
   * Note that 1.0 is considered to be normal/real time, values lower than 1.0
   * slow motion and values higher than 1.0 accelerated motion. Values of 0.0 or
   * smaller are not allowed.
   *
   * @param factor The time factor.
   * @throws IllegalArgumentException If the given value is 0.0 or smaller.
   */
  void setSimTimeFactor(double factor)
      throws IllegalArgumentException;
  
  /**
   * Sets an initial vehicle position.
   * This method should not be called while the communication adapter is
   * simulating order execution for the attached vehicle; the resulting
   * behaviour is undefined.
   *
   * @param newPosUUID The UUID of the new position.
   */
  void initVehiclePosition(String newPosUUID);
}
