package com.mj.tcs;

import com.mj.tcs.algorithms.Scheduler;
import com.mj.tcs.data.base.TCSResource;
import com.mj.tcs.data.order.Rejection;
import com.mj.tcs.data.user.CredentialsException;
import com.mj.tcs.drivers.CommunicationAdapter;
import com.mj.tcs.drivers.CommunicationAdapterRegistry;
import com.mj.tcs.drivers.LoadHandlingDevice;
import com.mj.tcs.data.ObjectUnknownException;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.base.Triple;
import com.mj.tcs.data.order.OrderSequence;
import com.mj.tcs.data.order.TransportOrder;

import java.util.List;
import java.util.Set;

/**
 * Declares the methods the MJTCS kernel must provide which are not accessible
 * to remote peers.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface LocalKernel extends Kernel {

    /**
     * Initializes this local kernel.
     */
    void initialize();

    /**
     * Waits for the local kernels shutdown sequence to finish.
     */
    void waitForTermination();

//    /**
//     * Returns the kernel's vehicle manager pool.
//     *
//     * @return The kernel's vehicle manager pool.
//     */
//    VehicleManagerPool getVehicleManagerPool();
//
//    /**
//     * Returns the kernel's vehicle controller pool.
//     *
//     * @return The kernel's vehicle controller pool.
//     */
//    VehicleControllerPool getVehicleControllerPool();

    /**
     * Returns the kernel's communication adapter registry.
     *
     * @return The kernel's communication adapter registry.
     */
    CommunicationAdapterRegistry getCommAdapterRegistry();

    /**
     * Returns the kernel's scheduler.
     *
     * @return The kernel's scheduler.
     */
    Scheduler getScheduler();

    /**
     * Sets a vehicle's energy level.
     *
     * @param vehicle A reference to the vehicle to be modified.
     * @param energyLevel The vehicle's new energy level.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleEnergyLevel(Vehicle vehicle, int energyLevel)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's recharge operation.
     *
     * @param vehicle A reference to the vehicle to be modified.
     * @param rechargeOperation The vehicle's new recharge action.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleRechargeOperation(Vehicle vehicle,
                                     String rechargeOperation)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's load handling devices.
     *
     * @param vehicle The vehicle to be modified.
     * @param devices The vehicle's new load handling devices.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleLoadHandlingDevices(Vehicle vehicle,
                                       List<LoadHandlingDevice> devices)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's maximum velocity.
     *
     * @param vehicle The vehicle to be modified.
     * @param velocity The vehicle's new maximum velocity.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleMaxVelocity(Vehicle vehicle, int velocity)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's maximum velocity.
     *
     * @param vehicle The vehicle to be modified.
     * @param velocity The vehicle's new maximum velocity.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleMaxReverseVelocity(Vehicle vehicle,
                                      int velocity)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's state.
     *
     * @param vehicle The vehicle to be modified.
     * @param newState The vehicle's new state.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleState(Vehicle vehicle, Vehicle.State newState)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's processing state.
     *
     * @param vehicle The vehicle to be modified.
     * @param newState The vehicle's new processing state.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleProcState(Vehicle vehicle,
                             Vehicle.ProcState newState)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's communication adapter's state.
     *
     * @param vehicle The vehicle to be modified.
     * @param newState The vehicle's communication adapter's new state.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleAdapterState(Vehicle vehicle,
                                CommunicationAdapter.State newState)
            throws ObjectUnknownException;

    /**
     * Places a vehicle on a point.
     *
     * @param vehicle The vehicle to be modified.
     * @param point The point on which the vehicle is to be
     * placed.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehiclePosition(Vehicle vehicle,
                            Point point)
            throws ObjectUnknownException;

    /**
     * Sets the point which a vehicle is expected to occupy next.
     *
     * @param vehicle The vehicle to be modified.
     * @param point The point which the vehicle is expected to
     * occupy next.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleNextPosition(Vehicle vehicle,
                                Point point)
            throws ObjectUnknownException;

    /**
     * Sets the vehicle's current precise position in mm.
     *
     * @param vehicle The vehicle to be modified.
     * @param newPosition The vehicle's precise position in mm.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehiclePrecisePosition(Vehicle vehicle,
                                   Triple newPosition)
            throws ObjectUnknownException;

    /**
     * Sets the vehicle's current orientation angle (-360..360�, or Double.NaN, if
     * the vehicle doesn't provide an angle).
     *
     * @param vehicle The vehicle to be modified.
     * @param angle The vehicle's orientation angle.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleOrientationAngle(Vehicle vehicle,
                                    double angle)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's transport order.
     *
     * @param vehicle The vehicle to be modified.
     * @param order The transport order the vehicle processes.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleTransportOrder(Vehicle vehicle,
                                  TransportOrder order)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's order sequence.
     *
     * @param vehicle The vehicle to be modified.
     * @param seq The order sequence the vehicle processes.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleOrderSequence(Vehicle vehicle,
                                 OrderSequence seq)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's index of the last route step travelled for the current
     * drive order of its current transport order.
     *
     * @param vehicle The vehicle to be modified.
     * @param index The new index.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     */
    void setVehicleRouteProgressIndex(Vehicle vehicle,
                                      int index)
            throws ObjectUnknownException;

    /**
     * Adds a rejection to a transport order.
     *
     * @param transportOrder The transport order to be modified.
     * @param newRejection The rejection to be added.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     */
    void addTransportOrderRejection(TransportOrder transportOrder,
                                    Rejection newRejection)
            throws ObjectUnknownException;

    /**
     * Sets a transport order's state.
     *
     * @param transportOrder The transport order to be modified.
     * @param newState The transport order's new state.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     */
    void setTransportOrderState(TransportOrder transportOrder,
                                TransportOrder.State newState)
            throws ObjectUnknownException;

    /**
     * Sets a transport order's processing vehicle.
     *
     * @param order The transport order to be modified.
     * @param vehicle The vehicle processing the order.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     */
    void setTransportOrderProcessingVehicle(
            TransportOrder order,
            Vehicle vehicle)
            throws ObjectUnknownException;

    /**
     * Sets a transport order's initial drive order.
     * Makes the first of the future drive orders the current one for the given
     * transport order. Fails if there already is a current drive order or if the
     * list of future drive orders is empty.
     *
     * @param transportOrder The transport order to be modified.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws IllegalStateException If there already is a current drive order or
     * if the list of future drive orders is empty.
     */
    void setTransportOrderInitialDriveOrder(
            TransportOrder transportOrder)
            throws ObjectUnknownException, IllegalStateException;

    /**
     * Updates a transport order's current drive order.
     * Marks the current drive order as finished, adds it to the list of past
     * drive orders and sets the current drive order to the next one of the list
     * of future drive orders (or <code>null</code>, if that list is empty).
     * If the current drive order is <code>null</code> because all drive orders
     * have been finished already or none has been started, yet, nothing happens.
     *
     * @param transportOrder The transport order to be modified.
     * @throws ObjectUnknownException If the referenced transport order is not
     * in this pool.
     */
    void setTransportOrderNextDriveOrder(TransportOrder transportOrder)
            throws ObjectUnknownException;

    /**
     * Sets the order sequence a transport order belongs to.
     *
     * @param order The transport order to be modified.
     * @param seq The sequence the transport order belongs to.
     * @throws ObjectUnknownException If any of the referenced objects do not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTransportOrderWrappingSequence(
            TransportOrder order,
            OrderSequence seq)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a transport order's <em>dispensable</em> flag.
     *
     * @param order The transport order to be modified.
     * @param dispensable The new dispensable flag.
     * @throws ObjectUnknownException If any of the referenced objects do not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTransportOrderDispensable(TransportOrder order,
                                      boolean dispensable)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's finished index.
     *
     * @param seq The order sequence to be modified.
     * @param index The sequence's new finished index.
     * @throws ObjectUnknownException If the referenced transport order is not
     * in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceFinishedIndex(OrderSequence seq,
                                       int index)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's finished flag.
     *
     * @param seq The order sequence to be modified.
     * @throws ObjectUnknownException If the referenced transport order is not
     * in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceFinished(OrderSequence seq)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's processing vehicle.
     *
     * @param seq The order sequence to be modified.
     * @param vehicle The vehicle processing the order sequence.
     * @throws ObjectUnknownException If the referenced transport order is not
     * in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceProcessingVehicle(OrderSequence seq,
                                           Vehicle vehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Expands a set of resources <em>A</em> to a set of resources <em>B</em>.
     * <em>B</em> contains the resources in <em>A</em> with blocks expanded to
     * their actual members.
     * The given set is not modified.
     *
     * @param resources The set of resources to be expanded.
     * @return The given set with resources expanded.
     * @throws ObjectUnknownException If any of the referenced objects does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Set<TCSResource> expandResources(Set<TCSResource> resources)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Adds a <code>KernelExtension</code> to this kernel.
     *
     * @param newExtension The extension to be added.
     */
    void addKernelExtension(KernelExtension newExtension);

    /**
     * Removes a <code>KernelExtension</code> from this kernel.
     *
     * @param rmExtension The extension to be removed.
     */
    void removeKernelExtension(KernelExtension rmExtension);
}
