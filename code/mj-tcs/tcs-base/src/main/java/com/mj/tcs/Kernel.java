package com.mj.tcs;

import com.mj.tcs.algorithms.TravelCosts;
import com.mj.tcs.data.ObjectUnknownException;
import com.mj.tcs.data.base.TCSObject;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.Path;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.OrderSequence;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.data.user.CredentialsException;
import com.mj.tcs.util.eventsystem.EventSource;
import com.mj.tcs.util.eventsystem.Message;
import com.mj.tcs.util.eventsystem.TCSEvent;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Declares the methods the MJTCS kernel must implement which are accessible
 * both to internal components and remote peers (like graphical user
 * interfaces).
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface Kernel extends EventSource<TCSEvent> {
    
    /**
     * Returns the name of the scene that is saved in the kernel.
     *
     * @return The name of the model or <code>null</code> if there is no model.
     * @throws IOException If getting the model name was not possible.
     */
    String getSceneName()
            throws IOException;


    public long getSceneID();

    /**
     * Returns a single TCSObjectReference<?> of the given class.
     *
     * @param <T> The TCSObjectReference<?>'s actual type.
     * @param clazz The class of the object to be returned.
     * @param uuid The uuid of the object to be returned.
     * @return A copy of the referenced object, or <code>null</code> if no such
     * object exists or if an object exists but is not an instance of the given
     * class.
     */
    <T extends TCSObject<T>> T getTCSObject(Class<T> clazz,
                                                     String uuid);

    /**
     * Returns all existing TcsModels of the given class.
     *
     * @param <T> The TcsModels' actual type.
     * @param clazz The class of the objects to be returned.
     * @return Copies of all existing objects of the given class.
     */
    <T extends TCSObject<T>> Set<T> getTCSObjects(Class<T> clazz);

    /**
     * Returns a single TCSObject of the given class.
     *
     * @param <T> The TCSObject's actual type.
     * @param clazz The class of the object to be returned.
     * @param ref A reference to the object to be returned.
     * @return A copy of the referenced object, or <code>null</code> if no such
     * object exists or if an object exists but is not an instance of the given
     * class.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    <T extends TCSObject<T>> T getTCSObject(Class<T> clazz,
                                            TCSObjectReference<T> ref);

    /**
     * Sets an object's property.
     *
     * @param ref A reference to the object to be modified.
     * @param key The property's key.
     * @param value The property's (new) value. If <code>null</code>, removes the
     * property from the object.
     * @throws ObjectUnknownException If the referenced object does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTCSObjectProperty(TCSObjectReference<?> ref, String key, String value)
            throws ObjectUnknownException;

    /**
     * Creates a new message with the given content and type.
     *
     * @param message The message's content.
     * @param type The message's type.
     * @return A copy of the newly created message.
     */
    Message publishMessage(String message, Message.Type type);

    /**
     * Locks/Unlocks a path.
     *
     * @param ref A reference to the path to be modified.
     * @param locked Indicates whether the path is to be locked
     * (<code>true</code>) or unlocked (<code>false</code>).
     * @throws ObjectUnknownException If the referenced path does not exist.
     */
    void setPathLocked(TCSObjectReference<Path> ref, boolean locked)
            throws ObjectUnknownException;

    /**
     * Sets a vehicle's critical energy level.
     *
     * @param ref A reference to the vehicle to be modified.
     * @param energyLevel The vehicle's new critical energy level.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setVehicleEnergyLevelCritical(TCSObjectReference<Vehicle> ref,
                                       int energyLevel)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a vehicle's good energy level.
     *
     * @param ref A reference to the vehicle to be modified.
     * @param energyLevel The vehicle's new good energy level.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setVehicleEnergyLevelGood(TCSObjectReference<Vehicle> ref,
                                   int energyLevel)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a transport order's deadline.
     *
     * @param ref A reference to the transport order to be modified.
     * @param deadline The transport order's new deadline.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTransportOrderDeadline(TCSObjectReference<TransportOrder> ref,
                                   long deadline)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Activates a transport order and makes it available for processing by a
     * vehicle.
     *
     * @param ref A reference to the transport order to be modified.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void activateTransportOrder(TCSObjectReference<TransportOrder> ref)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a transport order's intended vehicle.
     *
     * @param orderRef A reference to the transport order to be modified.
     * @param vehicleRef A reference to the vehicle intended to process the order.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTransportOrderIntendedVehicle(
            TCSObjectReference<TransportOrder> orderRef,
            TCSObjectReference<Vehicle> vehicleRef)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Copies drive order data from a list of drive orders to the given transport
     * order's future drive orders.
     *
     * @param orderRef A reference to the transport order to be modified.
     * @param newOrders The drive orders containing the data to be copied into
     * this transport order's drive orders.
     * @throws ObjectUnknownException If the referenced transport order is not
     * in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IllegalArgumentException If the destinations of the given drive
     * orders do not match the destinations of the drive orders in this transport
     * order.
     */
    void setTransportOrderFutureDriveOrders(
            TCSObjectReference<TransportOrder> orderRef,
            List<DriveOrder> newOrders)
            throws ObjectUnknownException, CredentialsException, IllegalArgumentException;

    /**
     * Adds a dependency to a transport order on another transport order.
     *
     * @param orderRef A reference to the order that the dependency is to be added
     * to.
     * @param newDepRef A reference to the order that is the new dependency.
     * @throws ObjectUnknownException If any of the referenced transport orders
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void addTransportOrderDependency(TCSObjectReference<TransportOrder> orderRef,
                                     TCSObjectReference<TransportOrder> newDepRef)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes a dependency from a transport order on another transport order.
     *
     * @param orderRef A reference to the order that the dependency is to be
     * removed from.
     * @param rmDepRef A reference to the order that is not to be depended on any
     * more.
     * @throws ObjectUnknownException If any of the referenced transport orders
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeTransportOrderDependency(
            TCSObjectReference<TransportOrder> orderRef,
            TCSObjectReference<TransportOrder> rmDepRef)
            throws ObjectUnknownException, CredentialsException;

//    /**
//     * Creates a new order sequence.
//     * A new order sequence is created with a generated unique ID and name. A copy
//     * of the newly created order sequence is then returned.
//     *
//     * @return A copy of the newly created order sequence.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    OrderSequence createOrderSequence()
//            throws CredentialsException;

    /**
     * Adds a transport order to an order sequence.
     *
     * @param seqRef A reference to the order sequence to be modified.
     * @param orderRef A reference to the transport order to be added.
     * @throws ObjectUnknownException If the referenced order sequence or
     * transport order is not in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IllegalArgumentException If the sequence is already marked as
     * <em>complete</em>, if the sequence already contains the given order or
     * if the given transport order has already been activated.
     */
    void addOrderSequenceOrder(TCSObjectReference<OrderSequence> seqRef,
                               TCSObjectReference<TransportOrder> orderRef)
            throws ObjectUnknownException, CredentialsException, IllegalArgumentException;

    /**
     * Removes a transport order from an order sequence.
     *
     * @param seqRef A reference to the order sequence to be modified.
     * @param orderRef A reference to the transport order to be removed.
     * @throws ObjectUnknownException If the referenced order sequence or
     * transport order is not in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeOrderSequenceOrder(TCSObjectReference<OrderSequence> seqRef,
                                  TCSObjectReference<TransportOrder> orderRef)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's complete flag.
     *
     * @param seqRef A reference to the order sequence to be modified.
     * @throws ObjectUnknownException If the referenced order sequence does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceComplete(TCSObjectReference<OrderSequence> seqRef)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's <em>failureFatal</em> flag.
     *
     * @param seqRef A reference to the order sequence to be modified.
     * @param fatal The sequence's new <em>failureFatal</em> flag.
     * @throws ObjectUnknownException If the referenced order sequence does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceFailureFatal(TCSObjectReference<OrderSequence> seqRef,
                                      boolean fatal)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's intended vehicle.
     *
     * @param seqRef A reference to the order sequence to be modified.
     * @param vehicleRef A reference to the vehicle intended to process the order
     * sequence.
     * @throws ObjectUnknownException If the referenced order sequence or vehicle
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceIntendedVehicle(TCSObjectReference<OrderSequence> seqRef,
                                         TCSObjectReference<Vehicle> vehicleRef)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Withdraw the referenced order, set its state to FAILED and stop the vehicle
     * that might be processing it.
     * Calling this method once will initiate the withdrawal, leaving the
     * transport order assigned to the vehicle until it has finished the movements
     * that it has already been ordered to execute. The transport order's state
     * will change to WITHDRAWN. Calling this method a second time for the same
     * vehicle/order will withdraw the order from the vehicle without further
     * waiting.
     *
     * @param ref A reference to the transport order to be withdrawn.
     * @param disableVehicle Whether setting the processing state of the vehicle
     * currently processing the transport order to
     * {@link com.mj.tcs.data.model.Vehicle.ProcState#UNAVAILABLE} to prevent
     * immediate redispatching of the vehicle.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void withdrawTransportOrder(TCSObjectReference<TransportOrder> ref,
                                boolean disableVehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Withdraw any order that a vehicle might be processing, set its state to
     * FAILED and stop the vehicle.
     * Calling this method once will initiate the withdrawal, leaving the
     * transport order assigned to the vehicle until it has finished the movements
     * that it has already been ordered to execute. The transport order's state
     * will change to WITHDRAWN. Calling this method a second time for the same
     * vehicle/order will withdraw the order from the vehicle without further
     * waiting.
     *
     * @param vehicleRef A reference to the vehicle to be modified.
     * @param disableVehicle Whether setting the processing state of the vehicle
     * currently processing the transport order to
     * {@link com.mj.tcs.data.model.Vehicle.ProcState#UNAVAILABLE} to prevent
     * immediate redispatching of the vehicle.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void withdrawTransportOrderByVehicle(TCSObjectReference<Vehicle> vehicleRef,
                                         boolean disableVehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Explicitly trigger dispatching of the referenced idle vehicle.
     *
     * @param vehicleRef A reference to the vehicle to be dispatched.
     * @param setIdleIfUnavailable Whether to set the vehicle's processing state
     * to IDLE before dispatching if it is currently UNAVAILABLE. If the vehicle's
     * processing state is UNAVAILABLE and this flag is not set, an
     * IllegalArgumentException will be thrown.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IllegalArgumentException If the referenced vehicle is not in a
     * dispatchable state (IDLE or, if the corresponding flag is set, UNAVAILABLE).
     */
    void dispatchVehicle(TCSObjectReference<Vehicle> vehicleRef,
                         boolean setIdleIfUnavailable)
            throws ObjectUnknownException, CredentialsException, IllegalArgumentException;

    /**
     * Sends a message to the communication adapter associated with the referenced
     * vehicle.
     * This method provides a generic one-way communication channel to the
     * communication adapter of a vehicle. Note that there is no return value and
     * no guarantee that the communication adapter will understand the message;
     * clients cannot even know which communication adapter is attached to a
     * vehicle, so it's entirely possible that the communication adapter receiving
     * the message does not understand it.
     *
     * @param vehicleRef The vehicle whose communication adapter shall receive the
     * message.
     * @param message The message to be delivered.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void sendCommAdapterMessage(TCSObjectReference<Vehicle> vehicleRef,
                                Object message)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates a new transport order.
     * A new transport order is created with a generated unique UUID and name,
     * containing the given <code>DriveOrder</code>s and with all other attributes
     * set to their default values. A copy of the newly created transport order
     * is then returned.
     *
     * @param destinations The list of destinations that have to be travelled to
     * when processing this transport order.
     * @return A copy of the newly created transport order.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    TransportOrder createTransportOrder(List<DriveOrder.Destination> destinations)
            throws CredentialsException;

    /**
     * Creates a new transport order.
     * A new transport order is created with a generated unique UUID and name,
     * containing the given <code>DriveOrder</code>s and with all other attributes
     * set to their default values. A copy of the newly created transport order
     * is then returned.
     *
     * @param orderUUID
     * @param orderName
     * @param destinations The list of destinations that have to be travelled to
     * when processing this transport order.
     * @return A copy of the newly created transport order.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    TransportOrder createTransportOrder(String orderUUID,
                                       String orderName,
                                       List<DriveOrder.Destination> destinations)
            throws CredentialsException;

    /**
     * Removes a transport order from the kernel, if it does NOT exists, then do nothing.
     *
     * @param orderRef The transport order reference to be removed.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeTransportOrder(TCSObjectReference<TransportOrder> orderRef)
            throws CredentialsException;

    /**
     * Creates and returns a list of transport orders defined in a script file.
     *
     * @param fileName The name of the script file defining the transport orders
     * to be created.
     * @return The list of transport orders created. If none were created, the
     * returned list is empty.
     * @throws ObjectUnknownException If any object referenced in the script file
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IOException If there was a problem reading or parsing the file with
     * the given name.
     */
    List<TransportOrder> createTransportOrdersFromScript(String fileName)
            throws ObjectUnknownException, CredentialsException, IOException;

    /**
     * Returns the costs for travelling from one location to a given set of
     * others.
     *
     * @param vRef A reference to the vehicle that shall be used for calculating
     * the costs. If it's <code>null</code> a random vehicle will be used.
     * @param srcRef A reference to the source Location
     * @param destRefs A set containing all destination locations
     * @return A list containing tuples of a location and the costs to travel there
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws ObjectUnknownException If something is not known.
     */
    List<TravelCosts> getTravelCosts(TCSObjectReference<Vehicle> vRef,
                                     TCSObjectReference<Location> srcRef,
                                     Set<TCSObjectReference<Location>> destRefs)
            throws CredentialsException, ObjectUnknownException;

//    /**
//     * Returns the result of the query defined by the given class.
//     *
//     * @param <T> The result's actual type.
//     * @param clazz Defines the query and the class of the result to be returned.
//     * @return The result of the query defined by the given class, or
//     * <code>null</code>, if the defined query is not supported in the kernel's
//     * current state.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    <T extends Query<T>> T query(Class<T> clazz)
//            throws CredentialsException;

    /**
     * Returns the current time factor for simulation.
     *
     * @return The current time factor for simulation.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    double getSimulationTimeFactor()
            throws CredentialsException;

    /**
     * Sets a time factor for simulation.
     *
     * @param factor The new time factor.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setSimulationTimeFactor(double factor)
            throws CredentialsException;

//    /**
//     * Returns all configuration items existing in the kernel.
//     *
//     * @return All configuration items existing in the kernel.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    Set<ConfigurationItemTO> getConfigurationItems()
//            throws CredentialsException;
//
//    /**
//     * Sets a single configuration item in the kernel.
//     *
//     * @param itemTO The configuration item to be set.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void setConfigurationItem(ConfigurationItemTO itemTO)
//            throws CredentialsException;

    /**
     * The various states a kernel instance may be running in.
     */
    public enum State {
        STARTED,
        /**
         * The normal mode of operation in which transport orders may be accepted
         * and dispatched to vehicles.
         */
        RUNNING,
        /**
         * A transitional state the kernel is in while shutting down.
         */
        SHUTDOWN
    }
}
