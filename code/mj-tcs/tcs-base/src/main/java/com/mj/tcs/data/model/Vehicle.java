package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.base.Triple;
import com.mj.tcs.data.order.OrderSequence;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.drivers.CommunicationAdapter;
import com.mj.tcs.drivers.LoadHandlingDevice;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Wang Zhen
 */
public class Vehicle extends BaseEntity implements Cloneable {

    private Scene scene;

    /**
     * A value indicating that no route steps have been travelled for the current
     * drive order, yet.
     */
    public final int ROUTE_INDEX_DEFAULT = -1;
    /**
     * The key for a property to store the class name of the preferred
     * communication adapter (factory) for this vehicle.
     */
    public final String PREFERRED_ADAPTER = "tcs:preferredAdapterClass";
    /**
     * This vehicle's current length (in mm).
     */
    private int length = 1000;
    /**
     * This vehicle's remaining energy (in percent of the maximum).
     */
    private int energyLevel = 100;
    /**
     * The energy level value at/below which the vehicle should be recharged.
     */
    private int energyLevelCritical = 30;
    /**
     * The energy level value at/above which the vehicle can be dispatched again
     * when charging.
     */
    private int energyLevelGood = 90;
    /**
     * The operation the vehicle's current communication adapter accepts as a
     * command to recharge the vehicle.
     */
    private String rechargeOperation = "CHARGE";
    /**
     * The current (state of the) load handling devices of this vehicle.
     */
    private List<LoadHandlingDevice> loadHandlingDevices;
    /**
     * This vehicle's maximum velocity (in mm/s).
     */
    private int maxVelocity = 1000;
    /**
     * This vehicle's maximum reverse velocity (in mm/s).
     */
    private int maxReverseVelocity = 1000;
    /**
     * This vehicle's current state.
     */
    private State state = State.UNKNOWN;
    /**
     * This vehicle's current processing state.
     */
    private ProcState procState = ProcState.UNAVAILABLE;
    /**
     * The current state of the communication adapter controlling the physical
     * vehicle represented by this <code>Vehicle</code> instance.
     */
    private CommunicationAdapter.State adapterState =
            CommunicationAdapter.State.UNKNOWN;
    /**
     * A reference to the transport order this vehicle is currently processing.
     */
    private TransportOrder transportOrder;
    /**
     * A reference to the order sequence this vehicle is currently processing.
     */
    private OrderSequence orderSequence;
    /**
     * The index of the last route step travelled for the current drive order of
     * the current transport order.
     */
    private int routeProgressIndex = ROUTE_INDEX_DEFAULT;
    /**
     * A reference to the point which this vehicle currently occupies.
     */
    private Point currentPosition;
    /**
     * Accumulated distance from currentPosition to nextPosition
     */
    private int accumulatedDistance;
    /**
     * A reference to the point which this vehicle is expected to be seen at next.
     */
    private Point nextPosition;
    /**
     * The vehicle's precise position in world coordinates [mm], independent from
     * logical positions/point names.
     * Set to <code>null</code> if the vehicle hasn't provided a precise position.
     */
    private Triple precisePosition;
    /**
     * The vehicle's current orientation angle (-360..360�).
     * Set to <code>Double.NaN</code> if the vehicle hasn't provided an
     * orientation angle.
     */
    private double orientationAngle = 0.0d;

    /**
     * Creates a new vehicle.
     *
     */
    public Vehicle(){
        //do nothing
    }

    @Override
    public void clearId() {
        setId(null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String inName) {
        this.name = Objects.requireNonNull(inName, "name is null");
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    // Methods not declared in any interface start here

    /**
     * Returns this vehicle's remaining energy (in percent of the maximum).
     *
     * @return This vehicle's remaining energy.
     */
    public int getEnergyLevel() {
        return energyLevel;
    }

    /**
     * Sets this vehicle's remaining energy (in percent of the maximum).
     *
     * @param newEnergyLevel The new energy level. Must not be smaller than 0 or
     * greater than 100.
     */
    public void setEnergyLevel(int newEnergyLevel) {
        if (newEnergyLevel < 0 || newEnergyLevel > 100) {
            throw new IllegalArgumentException("newEnergyLevel not in [0..100]: "
                    + newEnergyLevel);
        }
        energyLevel = newEnergyLevel;
    }

    /**
     * Checks whether the vehicle's energy level is critical.
     *
     * @return <code>true</code> if, and only if, the vehicle's energy level is
     * critical.
     */
    public boolean isEnergyCriticalLevel() {
        return energyLevel <= energyLevelCritical;
    }

    /**
     * Checks whether the vehicle's energy level is degraded (not <em>good</em>
     * any more.
     *
     * @return <code>true</code> if, and only if, the vehicle's energy level is
     * degraded.
     */
    public boolean isEnergyDegradedLevel() {
        return energyLevel <= energyLevelGood;
    }

    /**
     * Checks whether the vehicle's energy level is good.
     *
     * @return <code>true</code> if, and only if, the vehicle's energy level is
     * good.
     */
    public boolean isEnergyGoodLevel() {
        return energyLevel > energyLevelGood;
    }

    /**
     * Returns this vehicle's critical energy level (in percent of the maximum).
     * The critical energy level is the one at/below which the vehicle should be
     * recharged.
     *
     * @return This vehicle's critical energy level.
     */
    public int getEnergyLevelCritical() {
        return energyLevelCritical;
    }

    /**
     * Sets this vehicle's critical energy level (in percent of the maximum).
     * The critical energy level is the one at/below which the vehicle should be
     * recharged.
     *
     * @param newEnergyLevel The new critical energy level. Must not be smaller
     * than 0 or greater than 100.
     */
    public void setEnergyLevelCritical(int newEnergyLevel) {
        if (newEnergyLevel < 0 || newEnergyLevel > 100) {
            throw new IllegalArgumentException("newEnergyLevel not in [0..100]: "
                    + newEnergyLevel);
        }
        energyLevelCritical = newEnergyLevel;
    }

    /**
     * Returns this vehicle's good energy level (in percent of the maximum).
     * The good energy level is the one at/above which the vehicle can be
     * dispatched again when charging.
     *
     * @return This vehicle's good energy level.
     */
    public int getEnergyLevelGood() {
        return energyLevelGood;
    }

    /**
     * Sets this vehicle's good energy level (in percent of the maximum).
     * The good energy level is the one at/above which the vehicle can be
     * dispatched again when charging.
     *
     * @param newEnergyLevel The new good energy level. Must not be smaller than 0
     * or greater than 100.
     */
    public void setEnergyLevelGood(int newEnergyLevel) {
        if (newEnergyLevel < 0 || newEnergyLevel > 100) {
            throw new IllegalArgumentException("newEnergyLevel not in [0..100]: "
                    + newEnergyLevel);
        }
        energyLevelGood = newEnergyLevel;
    }

    /**
     * Returns the operation that the vehicle's current communication adapter
     * accepts as a command to recharge the vehicle.
     *
     * @return The operation that the vehicle's current communication adapter
     * accepts as a command to recharge the vehicle.
     */
    public String getRechargeOperation() {
        return rechargeOperation;
    }

    /**
     * Sets the operation that the vehicle's current communication adapter accepts
     * as a command to recharge the vehicle.
     *
     * @param rechargeOperation The recharge operation.
     */
    public void setRechargeOperation(String rechargeOperation) {
        this.rechargeOperation = Objects.requireNonNull(rechargeOperation,
                "rechargeOperation is null");
    }

    /**
     * Returns the current (state of the) load handling devices of this vehicle.
     *
     * @return The current (state of the) load handling devices of this vehicle.
     */
    public List<LoadHandlingDevice> getLoadHandlingDevices() {
        return loadHandlingDevices;
    }

    /**
     * Sets the current (state of the) load handling devices of this vehicle.
     *
     * @param newDevices The (state of the) load handling devices.
     */
    public void setLoadHandlingDevices(List<LoadHandlingDevice> newDevices) {
        loadHandlingDevices = Objects.requireNonNull(newDevices,
                "newDevices is null");
    }

    /**
     * Returns this vehicle's maximum velocity (in mm/s).
     *
     * @return This vehicle's maximum velocity (in mm/s).
     */
    public int getMaxVelocity() {
        return maxVelocity;
    }

    /**
     * Sets this vehicle's maximum velocity (in mm/s).
     *
     * @param newVelocity The new velocity.
     */
    public void setMaxVelocity(int newVelocity) {
        if (newVelocity < 0) {
            throw new IllegalArgumentException("newVelocity < 0: " + newVelocity);
        }
        maxVelocity = newVelocity;
    }

    /**
     * Returns this vehicle's maximum reverse velocity (in mm/s).
     *
     * @return This vehicle's maximum reverse velocity (in mm/s).
     */
    public int getMaxReverseVelocity() {
        return maxReverseVelocity;
    }

    /**
     * Sets this vehicle's maximum reverse velocity (in mm/s).
     *
     * @param newVelocity The new velocity.
     */
    public void setMaxReverseVelocity(int newVelocity) {
        if (newVelocity < 0) {
            throw new IllegalArgumentException("newVelocity < 0: " + newVelocity);
        }
        maxReverseVelocity = newVelocity;
    }

    /**
     * Returns this vehicle's current state.
     *
     * @return This vehicle's current state.
     */
    public State getState() {
        return state;
    }

    /**
     * Checks if this vehicle's current state is equal to the given one.
     *
     * @param otherState The state to compare to this vehicle's one.
     * @return <code>true</code> if, and only if, the given state is equal to this
     * vehicle's one.
     */
    public boolean hasState(State otherState) {
        return state.equals(otherState);
    }

    /**
     * Sets this vehicle's new state.
     *
     * @param newState This vehicle's new state.
     */
    public void setState(State newState) {
        state = Objects.requireNonNull(newState, "newState is null");
    }

    /**
     * Returns this vehicle's current processing state.
     *
     * @return This vehicle's current processing state.
     */
    public ProcState getProcState() {
        return procState;
    }

    /**
     * Checks if this vehicle's current processing state is equal to the given
     * one.
     *
     * @param otherState The state to compare to this vehicle's one.
     * @return <code>true</code> if, and only if, the given state is equal to this
     * vehicle's one.
     */
    public boolean hasProcState(ProcState otherState) {
        return procState.equals(otherState);
    }

    /**
     * Sets this vehicle's new processing state.
     *
     * @param newState This vehicle's new processing state.
     */
    public void setProcState(ProcState newState) {
        procState = Objects.requireNonNull(newState, "newState is null");
    }

    /**
     * Returns the current state of the communication adapter controlling the
     * physical vehicle represented by this <code>Vehicle</code> instance.
     *
     * @return The current state of this vehicle's communication adapter.
     */
    public CommunicationAdapter.State getAdapterState() {
        return adapterState;
    }

    /**
     * Sets the state of the communication adapter controlling the physical
     * vehicle represented by this <code>Vehicle</code> instance.
     *
     * @param newState The communication adapter new state.
     */
    public void setAdapterState(CommunicationAdapter.State newState) {
        adapterState = Objects.requireNonNull(newState, "newState is null");
    }

    /**
     * Returns this vehicle's current length.
     *
     * @return this vehicle's current length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets this vehicle's current length.
     *
     * @param newLength This vehicle's current length. Must be at least 1.
     * @throws IllegalArgumentException If <code>newLength</code> is less than 1.
     */
    public void setLength(int newLength) {
        if (length < 1) {
            throw new IllegalArgumentException("newLength is less than 1");
        }
        length = newLength;
    }

    /**
     * Returns a reference to the transport order this vehicle is currently
     * processing.
     *
     * @return A reference to the transport order this vehicle is currently
     * processing, or <code>null</code>, if it is not processing any transport
     * order at the moment.
     */
    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    /**
     * Assigns a transport order to this vehicle.
     *
     * @param newOrder A reference to the transport order this vehicle will be
     * processing from now on. If <code>null</code>, this vehicle will not be
     * processing any transport order.
     */
    public void setTransportOrder(TransportOrder newOrder) {
        transportOrder = newOrder;
    }

    /**
     * Returns a reference to the order sequence this vehicle is currently
     * processing.
     *
     * @return A reference to the order sequence this vehicle is currently
     * processing, or <code>null</code>, if it is not processing any order
     * sequence at the moment.
     */
    public OrderSequence getOrderSequence() {
        return orderSequence;
    }

    /**
     * Assigns an order sequence to this vehicle.
     *
     * @param newSeq A reference to the order sequence this vehicle will be
     * processing from now on. If <code>null</code>, this vehicle will not be
     * processing any order sequence.
     */
    public void setOrderSequence(OrderSequence newSeq) {
        orderSequence = newSeq;
    }

    /**
     * Returns the index of the last route step travelled for the current drive
     * order of the current transport order.
     *
     * @return The index of the last route step travelled for the current drive
     * order of the current transport order.
     */
    public int getRouteProgressIndex() {
        return routeProgressIndex;
    }

    /**
     * Sets the index of the last route step travelled for the current drive order
     * of the current transport order.
     *
     * @param newIndex The new index.
     */
    public void setRouteProgressIndex(int newIndex) {
        routeProgressIndex = newIndex;
    }

    /**
     * Returns a reference to the point this vehicle currently occupies.
     *
     * @return A reference to the point this vehicle currently occupies, or
     * <code>null</code>, if this vehicle's position is unknown or the vehicle is
     * currently not in the system.
     */
    public Point getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Sets this vehicle's current position.
     *
     * @param newPosition A reference to the new position.
     */
    public void setCurrentPosition(Point newPosition) {
        currentPosition = newPosition;
    }

    /**
     * Returns a reference to the point this vehicle is expected to be seen at
     * next.
     *
     * @return A reference to the point this vehicle is expected to be seen at
     * next, or <code>null</code>, if this vehicle's next position is unknown.
     */
    public Point getNextPosition() {
        return nextPosition;
    }

    /**
     * Sets this vehicle's next position.
     *
     * @param newPosition A reference to the new position.
     */
    public void setNextPosition(Point newPosition) {
        nextPosition = newPosition;
    }

    /**
     * Returns the vehicle's position in world coordinates [mm], independent
     * from logical positions/point names. May be <code>null</code> if the vehicle
     * hasn't provided a precise position.
     *
     * @return The vehicle's precise position in mm.
     */
    public Triple getPrecisePosition() {
        return precisePosition;
    }

    /**
     * Sets the vehicle's position in world coordinates [mm], independent from
     * logical positions/point names.
     *
     * @param precisePosition The vehicle's precise position in mm. May be
     * <code>null</code> to indicate that the vehicle hasn't provided a precise
     * position.
     */
    public void setPrecisePosition(Triple precisePosition) {
        this.precisePosition = precisePosition;
    }

    /**
     * Returns the vehicle's current orientation angle (-360..360�).
     * May be <code>Double.NaN</code> if the vehicle hasn't provided an
     * orientation angle.
     *
     * @return The vehicle's current orientation angle.
     */
    public double getOrientationAngle() {
        return orientationAngle;
    }

    /**
     * Sets the vehicle's current orientation angle, which may a value be between
     * -360 and 360�, or <code>Double.NaN</code> to indicate that the vehicle
     * hasn't provided an orientation angle.
     *
     * @param orientationAngle The vehicle's orientation angle, or
     * <code>Double.NaN</code>.
     */
    public void setOrientationAngle(double orientationAngle) {
        if (!Double.isNaN(orientationAngle)
                && (orientationAngle < -360.0
                || orientationAngle > 360.0)) {
            throw new IllegalArgumentException("Illegal angle: " + orientationAngle);
        }
        this.orientationAngle = orientationAngle;
    }

    /**
     * Checks if this vehicle is currently processing any transport order.
     *
     * @return <code>true</code> if, and only if, this vehicle is currently
     * processing a transport order.
     */
    public boolean isProcessingOrder() {
        return transportOrder != null;
    }


    public int getAccumulatedDistance() {
        return accumulatedDistance;
    }

    public void setAccumulatedDistance(int accumulatedDistance) {
        this.accumulatedDistance = accumulatedDistance;
    }

    @Override
    public Vehicle clone() {
        Vehicle clone = null;
            clone = (Vehicle) super.clone();
            clone.transportOrder = (transportOrder == null) ? null : transportOrder.clone();
            clone.orderSequence = (orderSequence == null) ? null : orderSequence.clone();
            clone.currentPosition = (currentPosition == null) ? null : currentPosition.clone();
            clone.precisePosition = (precisePosition == null) ? null : precisePosition;
            clone.accumulatedDistance = accumulatedDistance;

        return clone;
    }

    /**
     * The elements of this enumeration describe the various possible states of a
     * vehicle.
     */
    public enum State {

        /**
         * The vehicle's current state is unknown, e.g. because communication with
         * it is currently not possible for some reason.
         */
        UNKNOWN("UNKNOWN"),
        /**
         * The vehicle's state is known and it's not in an error state, but it is
         * not available for receiving orders.
         */
        UNAVAILABLE("UNAVAILABLE"),
        /**
         * There is a problem with the vehicle.
         */
        ERROR("ERROR"),
        /**
         * The vehicle is currently idle/available for processing movement orders.
         */
        IDLE("IDLE"),
        /**
         * The vehicle is processing a movement order.
         */
        EXECUTING("EXECUTING"),
        /**
         * The vehicle is currently recharging its battery/refilling fuel.
         */
        CHARGING("CHARGING");

        private String text;

        State(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static State fromString(String text) {
            Optional<State> state = Arrays.stream(State.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (state.isPresent()) {
                return state.get();
            }

            return UNAVAILABLE;
        }
    }

    /**
     * A vehicle's processing state as seen by the dispatcher.
     */
    public enum  ProcState {

        /**
         * The vehicle is currently unavailable for order processing and cannot be
         * dispatched. This is a vehicle's initial state.
         */
        UNAVAILABLE("UNAVAILABLE"),
        /**
         * The vehicle is currently not processing a transport order.
         */
        IDLE("IDLE"),
        /**
         * The vehicle is currently processing a transport order and is waiting for
         * the next drive order to be assigned to it.
         */
        AWAITING_ORDER("AWAITING_ORDER"),
        /**
         * The vehicle is currently processing a drive order.
         */
        PROCESSING_ORDER("PROCESSING_ORDER");

        private String text;

        ProcState(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static ProcState fromString(String text) {
            Optional<ProcState> procState = Arrays.stream(ProcState.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (procState.isPresent()) {
                return procState.get();
            }

            return UNAVAILABLE;
        }
    }

    /**
     * The elements of this enumeration represent the possible orientations of a
     * vehicle.
     */
    public enum Orientation {

        /**
         * Indicates that the vehicle is driving/standing oriented towards its
         * front.
         */
        FORWARD("FORWARD"),
        /**
         * Indicates that the vehicle is driving/standing oriented towards its
         * back.
         */
        BACKWARD("BACKWARD"),
        /**
         * Indicates that the vehicle's orientation is undefined/unknown.
         */
        UNDEFINED("UNDEFINED");

        private String text;

        Orientation(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static Orientation fromString(String text) {
            Optional<Orientation> orientation = Arrays.stream(Orientation.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (orientation.isPresent()) {
                return orientation.get();
            }

            return UNDEFINED;
        }
    }
}
