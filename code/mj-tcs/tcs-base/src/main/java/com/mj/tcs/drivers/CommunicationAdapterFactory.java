package com.mj.tcs.drivers;

import com.mj.tcs.LocalKernel;
import com.mj.tcs.data.model.Vehicle;

/**
 * Provides communication adapter instances for vehicles to be controlled.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface CommunicationAdapterFactory {

    /**
     * Sets a reference to the kernel.
     *
     * @param kernel The kernel.
     */
    void setKernel(LocalKernel kernel);

    /**
     * Returns a string describing the factory/the adapters provided.
     * This should be a short string that can be displayed e.g. as a menu item for
     * choosing between multiple factories/adapter types for a vehicle.
     *
     * @return A string describing the factory/the adapters created.
     */
    String getAdapterDescription();

    /**
     * Checks whether this factory can provide a communication adapter for the
     * given vehicle.
     *
     * @param vehicle The vehicle to check for.
     * @return <code>true</code> if, and only if, this factory can provide a
     * communication adapter to control the given vehicle.
     */
    boolean providesAdapterFor(Vehicle vehicle);

    /**
     * Returns a communication adapter for controlling the given vehicle.
     *
     * @param vehicle The vehicle to be controlled.
     * @return A communication adapter for controlling the given vehicle, or
     * <code>null</code>, if this factory cannot provide an adapter for it.
     */
    BasicCommunicationAdapter getAdapterFor(Vehicle vehicle);
}
