package com.mj.tcs.drivers;

/**
 * Produces <code>VehicleManager</code>s for remote
 * <code>CommunicationAdapter</code>s.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface VehicleManagerPool {

    /**
     * Returns a <code>VehicleManager</code> instance for a named vehicle and
     * associates the given <code>CommunicationAdapter</code> with it.
     *
     * @param vehicleUUID The uuid of the vehicle for which to return the vehicle
     * manager.
     * @param commAdapter The communication adapter that is going to control the
     * physical vehicle.
     * @return A <code>VehicleManager</code> instance for the named vehicle.
     * @throws IllegalArgumentException If a vehicle with the given name does not
     * exist.
     */
    VehicleManager getVehicleManager(String vehicleUUID,
                                     CommunicationAdapter commAdapter)
            throws IllegalArgumentException;

    /**
     * Disassociates a <code>VehicleManager</code> and
     * <code>CommunicationAdapter</code> from a vehicle and removes all references
     * to them.
     *
     * @param vehicleUUID The id of the vehicle from which to detach the manager
     * and communication adapter.
     * @throws IllegalArgumentException If a vehicle with the given name does not
     * exist or if it is not associated with a vehicle manager/communication
     * adapter pair.
     */
    void detachVehicleManager(String vehicleUUID)
            throws IllegalArgumentException;
}
