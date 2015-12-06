package com.mj.tcs.drivers;

import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.base.Triple;
import com.mj.tcs.util.CyclicTask;
import com.mj.tcs.util.eventsystem.Message;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * The base class that all communication adapters must extend.
 *
 * <p>
 * Synchronization:
 * </p>
 * <ul>
 * <li>Accessing the command queue/sent queue from outside should always be
 * protected by synchronization on the BasicCommunicationAdapter instance.</li>
 * </ul>
 *
 * @author Wang Zhen
 */
public abstract class BasicCommunicationAdapter implements CommunicationAdapter, VelocityListener {
    /**
     * This class's Logger.
     */
    private static final Logger log =
            Logger.getLogger(BasicCommunicationAdapter.class.getName());
    /**
     * This communication adapter's name.
     */
    private final String name;
    /**
     * This communication adapter's velocity listeners.
     */
    private final Set<VelocityListener> velocityListeners = new LinkedHashSet<>();
    /**
     * A set of views observing this communication adapter.
     */
    // TODO:
//    private final Set<CommunicationAdapterView> adapterViews = new HashSet<>();
    /**
     * This adapter's <em>enabled</em> flag.
     */
    private boolean enabled;
    /**
     * This adapter's current command dispatcher task.
     */
    private CyclicTask commandDispatcherTask;
    /**
     * Energy level of the vehicle controlled by this communication adapter.
     */
    private int vehicleEnergyLevel = 100;
    /**
     * Load handling devices of the vehicle controlled by this driver.
     */
    private List<LoadHandlingDevice> loadHandlingDevices = new LinkedList<>();
    /**
     * State of the vehicle controlled by this communication adapter.
     */
    private Vehicle.State vehicleState = Vehicle.State.UNKNOWN;
    /**
     * This communication adapter's <code>VehicleModel</code>.
     */
    private VehicleModel vehicleModel;
    /**
     * This adapter's command queue.
     */
    private final Queue<MovementCommand> commandQueue = new LinkedList<>();
    /**
     * This adapter's command queue's capacity.
     */
    private int commandQueueCapacity = 1;
    /**
     * Contains the orders which have been sent to the vehicle but which haven't
     * been executed by it, yet.
     */
    private final Queue<MovementCommand> sentQueue = new LinkedList<>();
    /**
     * The maximum number of orders to be sent to a vehicle.
     */
    private int sentQueueCapacity = 1;
    /**
     * The operation this adapter interprets as a command to recharge the vehicle.
     */
    private String rechargeOperation = "CHARGE";

    /**
     * Creates a new BasicCommunicationAdapter.
     *
     * @param adapterName The new communication adapter's name. Mainly used for
     * identifying the adapter in log messages.
     */
    public BasicCommunicationAdapter(final String adapterName) {
        log.info("method entry");
        name = Objects.requireNonNull(adapterName, "adapterName is null");
    }

    // Implementation of interface CommunicationAdapter starts here.
    @Override
    public final synchronized boolean addCommand(AdapterCommand newCommand) {
        log.info("method entry");
        Objects.requireNonNull(newCommand, "newCommand is null");

        boolean commandAdded = false;
        if (newCommand instanceof MovementCommand) {
            if (commandQueue.size() < commandQueueCapacity) {
                log.info("Adding command: " + newCommand);
                commandQueue.add((MovementCommand) newCommand);
                commandAdded = true;
            }
        }
        else {
            log.warn("Unknown AdapterCommand implementation: "
                    + newCommand.getClass());
        }
        if (commandAdded) {
            this.notifyAll();
        }
        return commandAdded;
    }

    @Override
    public synchronized void clearCommandQueue() {
        log.info("method entry");
        if (!commandQueue.isEmpty()) {
            commandQueue.clear();
            this.notifyAll();
        }
        sentQueue.clear();
    }

    @Override
    public final synchronized int getVehicleEnergyLevel() {
        return vehicleEnergyLevel;
    }

    @Override
    public synchronized List<LoadHandlingDevice> getVehicleLoadHandlingDevices() {
        return loadHandlingDevices;
    }

    @Override
    public synchronized Vehicle.State getVehicleState() {
        return vehicleState;
    }

    @Override
    public void processMessage(Object message) {
        // Do nada.
    }

    /**
     * Returns the current vehicle position.
     *
     * @return Current position
     */
    public synchronized String getVehiclePosition() {
        return vehicleModel.getPosition();
    }

    // Implementation of interface VelocityListener starts here.
    /**
     * Convenience implementation that delegates to all registered
     * <code>VelocityListener</code>s.
     *
     * @param velocityValue The new velocity value.
     */
    @Override
    public final synchronized void addVelocityValue(int velocityValue) {
        for (VelocityListener curListener : velocityListeners) {
            curListener.addVelocityValue(velocityValue);
        }
    }

    // Implementation of class-specific methods starts here.
    /**
     * Starts this communication adapter after it has been initialized.
     * Note that overriding methods should always call this implementation as it
     * does some pretty important stuff.
     */
    public synchronized boolean enable() {
        log.info("method entry");
        if (!enabled) {
            if (!connectVehicle()) {
                return false;
            }
            commandDispatcherTask = new CommandDispatcherTask();
            Thread commandDispatcherThread =
                    new Thread(commandDispatcherTask, getName() + "-commandDispatcher");
            commandDispatcherThread.start();
            enabled = true;
            setCommandQueueCapacity(commandQueueCapacity);
            // Re-send the vehicle state.
            setVehicleRechargeOperation(rechargeOperation);
            setVehiclePrecisePosition(null);
            setVehicleOrientationAngle(Double.NaN);
            setVehicleEnergyLevel(vehicleEnergyLevel);
            setVehicleLoadHandlingDevices(loadHandlingDevices);
            setVehicleState(getVehicleState());
        }
        else {
            log.warn("Already enabled, doing nothing.");
        }

        return true;
    }

    /**
     * Disables this communication adapter.
     * Note that overriding methods should always call this implementation as it
     * does some pretty important stuff.
     */
    public synchronized void disable() {
        log.info("method entry");
        if (enabled) {
            disconnectVehicle();
            commandDispatcherTask.terminate();
            commandDispatcherTask = null;
            enabled = false;
            // Update the vehicle's state for the rest of the system.
            setVehicleState(Vehicle.State.UNKNOWN);
        }
        else {
            log.info("Not enabled, doing nothing.");
        }
    }

    /**
     * Checks whether this communication adapter is enabled.
     *
     * @return <code>true</code> if, and only if, this communication adapter is
     * enabled.
     */
    public synchronized boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns this communication adapter's name.
     *
     * @return This communication adapter's name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets this communication adapter's <code>VehicleModel</code>.
     *
     * @param newModel The new <code>VehicleStatusUpdater</code>.
     */
    public final synchronized void setVehicleModel(VehicleModel newModel) {
        log.info("method entry");
        this.vehicleModel = newModel;
    }

    /**
     * Registers a new velocity listener with this communication adapter.
     *
     * @param l The listener to be added.
     */
    public final synchronized void addVelocityListener(VelocityListener l) {
        log.info("method entry");
        Objects.requireNonNull(l, "l is null");
        velocityListeners.add(l);
    }

    /**
     * Unregisters a velocity listener from this communication adapter.
     *
     * @param l The listener to be removed.
     */
    public final synchronized void removeVelocityListener(VelocityListener l) {
        log.info("method entry");
        Objects.requireNonNull(l, "l is null");
        velocityListeners.remove(l);
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * position.
     *
     * @param position The vehicle's new position.
     */
    public final synchronized void setVehiclePosition(String position) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehiclePosition(position);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * position on path.
     *
     * @param pathName The path on which the vehicle moved.
     * @param pathOffset The path offset.
     */
    public final synchronized void setVehiclePositionOnPath(String pathName, long pathOffset) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehiclePositionOnPath(pathName, pathOffset);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Get the vehicle's precise position.
     *
     * @return Precise position in mm. null if precise position not set.
     */
    public final Triple getVehiclePrecisePosition() {
        log.info("method entry");
        if (vehicleModel != null) {
            return vehicleModel.getVehiclePrecisePosition();
        }
        else {
            log.info("vehicleModel is null, not called");
            return null;
        }
    }

    public final Vehicle getVehicle() {
        log.info("method entry");
        if (vehicleModel != null) {
            return vehicleModel.getVehicle();
        }
        else {
            log.info("vehicleModel is null, not called");
            return null;
        }
    }

    /**
     * Sets the vehicle's precise position in mm. (May be <code>null</code> to
     * indicate that the vehicle does not provide coordinates.)
     *
     * @param position The position.
     */
    public final synchronized void setVehiclePrecisePosition(Triple position) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehiclePrecisePosition(position);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Get the vehicle's current orientation angle.
     *
     * @return Current orientation angle in deg [-360�,360�].
     *          Double.NaN if orientation angle not set.
     */
    public double getVehicleOrientationAngle() {
        return vehicleModel.getVehicleOrientationAngle();
    }

    /**
     * Sets the vehicle's orientation angle (-360..360�). May be Double.NaN if the
     * vehicle doesn't provide an angle.
     *
     * @param angle The angle.
     */
    public final synchronized void setVehicleOrientationAngle(double angle) {
        log.info("method entry");
        final double validAngle;
        if (!Double.isNaN(angle) && (angle > 360.0 || angle < -360.0)) {
            validAngle = angle % 360.0;
            log.warn("Angle not with [-360..360]: " + angle + ", normalized to "
                    + validAngle);
        }
        else {
            validAngle = angle;
        }
        if (vehicleModel != null) {
            vehicleModel.setVehicleOrientationAngle(validAngle);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * energy level.
     *
     * @param newLevel The vehicle's new energy level.
     */
    public final synchronized void setVehicleEnergyLevel(int newLevel) {
        log.info("method entry");
        final int validLevel;
        // If the reported value is out of [0..100], limit it to 0 or 100.
        if (newLevel < 0 || newLevel > 100) {
            validLevel = Math.min(100, Math.max(0, newLevel));
            log.warn("Energy level not within [0..100]: " + newLevel
                    + ", limited to " + validLevel);
        }
        else {
            validLevel = newLevel;
        }
        if (vehicleModel != null) {
            vehicleModel.setVehicleEnergyLevel(validLevel);
            vehicleEnergyLevel = validLevel;
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * recharge operation.
     *
     * @param newOperation The vehicle's new recharge operation.
     */
    public final synchronized void setVehicleRechargeOperation(
            String newOperation) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehicleRechargeOperation(rechargeOperation);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
        rechargeOperation = newOperation;
    }

    /**
     * Notifies the <code>VehicleModel</code> about the vehicle's load handling
     * devices.
     *
     * @param devices The vehicle's load handling devices.
     */
    public final synchronized void setVehicleLoadHandlingDevices(
            List<LoadHandlingDevice> devices) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehicleLoadHandlingDevices(devices);
            loadHandlingDevices = devices;
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * maximum velocity.
     *
     * @param newVelocity The vehicle's new maximum velocity.
     */
    public final synchronized void setVehicleMaxVelocity(int newVelocity) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehicleMaxVelocity(newVelocity);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * maximum reverse velocity.
     *
     * @param newVelocity The vehicle's new maximum reverse velocity.
     */
    public final synchronized void setVehicleMaxReverseVelocity(int newVelocity) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.setVehicleMaxReverseVelocity(newVelocity);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Sets a property of the vehicle.
     * If the given value is <code>null</code>, the property is removed from the
     * vehicle.
     *
     * @param key The property's key.
     * @param value The property's (new) value. If <code>null</code>, the property
     * is removed from the vehicle.
     */
    public final synchronized void setVehicleProperty(String key, String value) {
        log.info("method entry");
        vehicleModel.setVehicleProperty(key, value);
    }

    /**
     * Notifies the <code>VehicleStatusUpdater</code> about the vehicle's new
     * state.
     *
     * @param newState The vehicle's new state.
     */
    public final synchronized void setVehicleState(Vehicle.State newState) {
        log.info("method entry");
        if (!vehicleState.equals(newState)) {
            if (!Vehicle.State.ERROR.equals(vehicleState)
                    && Vehicle.State.ERROR.equals(newState)) {
                logMessage("Vehicle state changed to ERROR", Message.Type.INFO);
            }
            else if (Vehicle.State.ERROR.equals(vehicleState)
                    && !Vehicle.State.ERROR.equals(newState)) {
                logMessage("Vehicle state is no longer ERROR", Message.Type.INFO);
            }
            vehicleState = newState;
            if (vehicleModel != null) {
                vehicleModel.setVehicleState(newState);
            }
            else {
                log.info("vehicleModel is null, not called");
            }
        }
    }

    /**
     * Sets a property of the transport order the vehicle is currently processing.
     * If the given value is <code>null</code>, the property is removed from the
     * order.
     *
     * @param key The property's key.
     * @param value The property's (new) value. If <code>null</code>, the property
     * is removed from the order.
     */
    public final synchronized void setOrderProperty(String key, String value) {
        log.info("method entry");
        vehicleModel.setOrderProperty(key, value);
    }

    /**
     * Confirms that a given command has been successfully executed by the
     * communication adapter/vehicle.
     *
     * @param executedCommand The command that has been successfully executed.
     */
    protected final synchronized void commandExecuted(
            AdapterCommand executedCommand) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.commandExecuted(executedCommand);
        }
        else {
            log.warn("vehicleModel is null, not called");
        }
        // Notify the command dispatcher task so it can send the next order if one
        // is available.
        this.notify();
    }

    /**
     * Returns this adapter's command queue.
     *
     * @return This adapter's command queue.
     */
    public final synchronized Queue<MovementCommand> getCommandQueue() {
        return commandQueue;
    }

    /**
     * Returns this adapter's command queue's capacity.
     *
     * @return This adapter's command queue's capacity.
     */
    public final synchronized int getCommandQueueCapacity() {
        return commandQueueCapacity;
    }

    /**
     * Informs the vehicle manager about the capacity of the communication
     * adapter's command queue.
     *
     * @param capacity The communication adapter's command queue capacity.
     */
    public final synchronized void setCommandQueueCapacity(int capacity) {
        log.info("method entry");
        commandQueueCapacity = capacity;
        if (vehicleModel != null) {
            vehicleModel.setAdapterCommandQueueCapacity(capacity);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Returns a queue containing the commands that this adapter has sent to the
     * vehicle already but which have not yet been processed by it.
     *
     * @return A queue containing the commands that this adapter has sent to the
     * vehicle already but which have not yet been processed by it.
     */
    public final synchronized Queue<MovementCommand> getSentQueue() {
        return sentQueue;
    }

    /**
     * Returns the capacity of this adapter's <em>sent queue</em>.
     *
     * @return The capacity of this adapter's <em>sent queue</em>.
     */
    public final synchronized int getSentQueueCapacity() {
        return sentQueueCapacity;
    }

    /**
     * Sets the capacity of this adapter's <em>sent queue</em>.
     *
     * @param newCapacity The new capacity of this adapter's <em>sent
     * queue</em>.
     */
    public final synchronized void setSentQueueCapacity(int newCapacity) {
        sentQueueCapacity = newCapacity;
    }

    /**
     * Logs a message from the communication adapter.
     *
     * @param message The message to be logged.
     */
    protected final synchronized void logMessage(Message message) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.logMessage(message);
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Logs a message from the communication adapter.
     *
     * @param message The message to be logged.
     * @param msgType The type of the message.
     */
    protected final synchronized void logMessage(String message,
                                                 Message.Type msgType) {
        log.info("method entry");
        if (vehicleModel != null) {
            vehicleModel.logMessage(new Message(message, msgType));
        }
        else {
            log.info("vehicleModel is null, not called");
        }
    }

    /**
     * Checks whether a new command can be sent to the vehicle.
     * The default implementation of this method returns <code>true</code> only if
     * the number of commands sent already is less than the vehicle's capacity and
     * there is at least one command in the queue that is waiting to be sent.
     *
     * @return <code>true</code> if, and only if, a new command can be sent to the
     * vehicle.
     */
    protected synchronized boolean canSendNextCommand() {
        return (getSentQueue().size() < sentQueueCapacity)
                && !getCommandQueue().isEmpty();
    }

    // Abstract methods start here.
    /**
     * Initiates a communication channel to the vehicle.
     * This method should not block, i.e. it should not wait for the actual
     * connection to be established, as the vehicle could be temporarily absent
     * or not responding at all. If that's the case, the communication adapter
     * should continue trying to establish a connection until successful or until
     * <code>disconnectVehicle</code> is called.
     */
    protected abstract boolean connectVehicle();

    /**
     * Closes the communication channel to the vehicle.
     */
    protected abstract void disconnectVehicle();

    /**
     * Checks whether the communication channel to the vehicle is open.
     * <p>
     * Note that the return value of this method does <em>not</em> indicate
     * whether communication with the vehicle is currently alive and/or if the
     * vehicle is considered to be working/responding correctly. For that
     * information, see <code>isVehicleAlive()</code>.
     * </p>
     *
     * @return <code>true</code> if, and only if, the communication channel to the
     * vehicle is open.
     */
    protected abstract boolean isVehicleConnected();

    /**
     * Checks whether communication with the vehicle is considered to still be
     * alive/working.
     *
     * @return <code>true</code> if, and only if, the implementing communication
     * adapter considers communication with the vehicle to still be alive.
     */
    public abstract boolean isVehicleAlive();

    /**
     * Converts the given command to something the vehicle can understand and
     * sends the resulting data to the vehicle.
     *
     * @param cmd The command to be sent.
     */
    public abstract void sendCommand(MovementCommand cmd);

    /**
     * The task processing the command queue.
     */
    private final class CommandDispatcherTask
            extends CyclicTask {

        /**
         * Creates a new CommandDispatcherTask.
         */
        private CommandDispatcherTask() {
            super(0);
        }

        @Override
        protected void runActualTask() {
            MovementCommand curCmd;
            synchronized (BasicCommunicationAdapter.this) {
                // Wait until we're terminated or we can send the next command.
                while (!isTerminated() && !canSendNextCommand()) {
                    try {
                        // Wait at most one second so we can still regularly check if this
                        // task has been terminated.
                        BasicCommunicationAdapter.this.wait(1000);
                    }
                    catch (InterruptedException exc) {
                        log.error("Unexpectedly interrupted", exc);
                    }
                }
                if (!isTerminated()) {
                    curCmd = getCommandQueue().poll();
                    if (curCmd != null) {
                        sendCommand(curCmd);
                        // Remember that we sent this command to the vehicle.
                        sentQueue.add(curCmd);
                    }
                }
            }
        }
    }
}
