package com.mj.tcs.algorithms;

import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.TransportOrder;

/**
 * This interface declares the methods a dispatcher module for the MJTCS
 * kernel must implement.
 * <p>
 * A dispatcher manages the distribution of transport orders among the vehicles
 * in a system. It is basically event-driven, where an event can be a new
 * transport order being introduced into the system or a vehicle becoming
 * available for processing existing orders.
 * </p>
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface Dispatcher {

    /**
     * Notifies the dispatcher that the given vehicle may now be dispatched.
     *
     * @param vehicle The dispatchable vehicle.
     */
    void dispatch(Vehicle vehicle);

    /**
     * Notifies the dispatcher that the given transport order may now be
     * dispatched.
     *
     * @param order The dispatchable order.
     */
    void dispatch(TransportOrder order);

    /**
     * Notifies the dispatcher that any order a given vehicle might be processing
     * is to be withdrawn and the vehicle stopped.
     *
     * @param vehicle The vehicle whose order is withdrawn.
     * @param disableVehicle Whether to set the vehicle's processing state to
     * UNAVAILABLE after withdrawing the order to prevent the vehicle being
     * dispatched for now.
     */
    void withdrawOrder(Vehicle vehicle,
                       boolean disableVehicle);

    /**
     * Initializes the dispatcher before first use.
     */
    void initialize();

    /**
     * Terminates the dispatcher and frees all resources it might have allocated.
     */
    void terminate();

    /**
     * Returns a human readable text describing this router's internal state.
     *
     * @return A human readable text describing this router's internal state.
     */
    String getInfo();
}
