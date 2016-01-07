package com.mj.tcs.drivers;

import com.mj.tcs.data.order.DriveOrder;

import java.util.List;
import java.util.Map;

/**
 * Provides high-level methods for the system to control a vehicle.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface VehicleController {

    /**
     * Checks whether this controller is currently enabled or not.
     *
     * @return <code>true</code> if this controller is currently enabled, else
     * <code>false</code>.
     */
    boolean isEnabled();

    /**
     * Enable this controller.
     */
    void enable();

    /**
     * Disable this controller.
     */
    void disable();

    /**
     * Sets the current drive order for the vehicle associated with this
     * controller.
     *
     * @param newOrder The new drive order.
     * @param orderProperties Properties of the transport order the new drive
     * order is part of.
     */
    void setDriveOrder(DriveOrder newOrder, Map<String, String> orderProperties);

    /**
     * Notifies the controller that the current drive order is to be aborted.
     * After receiving this notification, the controller should not send any
     * further movement commands to the vehicle.
     */
    void abortDriveOrder();

    /**
     * Clears the associated vehicle's command queue.
     */
    void clearCommandQueue();

    /**
     * Checks if the vehicle would be able to process the given sequence of
     * operations, taking into account its current state.
     *
     * @param operations A sequence of operations that might appear in future
     * commands.
     * @return A <code>Processability</code> telling if the vehicle would be able
     * to process every single operation in the list (in the given order).
     */
    Processability canProcess(List<String> operations);

    /**
     * Delivers a generic message to the communication adapter.
     *
     * @param message The message to be delivered.
     */
    void sendCommAdapterMessage(Object message);
}
