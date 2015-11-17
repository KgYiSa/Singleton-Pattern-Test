package com.mj.tcs.drivers;

import com.mj.tcs.data.model.BaseLocation;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.Route;

import java.util.Map;
import java.util.Objects;

/**
 * A command for moving a step.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public final class MovementCommand implements AdapterCommand {

    /**
     * A constant indicating there is no operation to be executed after moving.
     */
    public static final String NO_OPERATION = DriveOrder.Destination.OP_NOP;
    /**
     * A constant indicating the vehicle should basically just move to a point
     * without a location associated to it.
     */
    public static final String MOVE_OPERATION = DriveOrder.Destination.OP_MOVE;
    /**
     * A constant for parking the vehicle. (Again, basically doing nothing at the
     * destination.)
     */
    public static final String PARK_OPERATION = DriveOrder.Destination.OP_PARK;
    /**
     * The step describing the movement.
     */
    private final Route.Step step;
    /**
     * The operation to be executed after moving.
     */
    private final String operation;
    /**
     * The location at which the operation is to be executed. (May be
     * <code>null</code> if <em>operation</em> is <code>NO_OPERATION</code>.)
     */
    private final BaseLocation opLocation;
    /**
     * The destination position of the whole drive order.
     */
    private final Point finalDestination;
    /**
     * The operation to be executed at the destination position.
     */
    private final String finalOperation;
    /**
     * Properties of the order this command is part of.
     */
    private final Map<String, String> properties;

    private boolean executedSuccessfully = true;

    /**
     * Creates a new MovementCommand.
     *
     * @param newStep The step describing the movement.
     * @param newOperation The operation to be executed after moving.
     * @param newOpLocation The location at which the operation is to be executed.
     * May be <code>null</code> if <em>newOperation</em> is
     * <code>NO_OPERATION</code>.)
     * @param newDestination The destination position of the whole drive order.
     * @param newDestOperation The operation to be executed at the destination
     * position.
     * @param newProperties Properties of the order this command is part of.
     */
    public MovementCommand(Route.Step newStep,
                           String newOperation,
                           BaseLocation newOpLocation,
                           Point newDestination,
                           String newDestOperation,
                           Map<String, String> newProperties) {
        step = Objects.requireNonNull(newStep, "newStep is null");
        operation = Objects.requireNonNull(newOperation, "newOperation is null");
        finalDestination = Objects.requireNonNull(newDestination, "newDestination is null");
        finalOperation = Objects.requireNonNull(newDestOperation, "newDestOperation is null");
        properties = Objects.requireNonNull(newProperties, "newProperties is null");
        if (newOpLocation == null && !isEmptyOperation(newOperation)) {
            throw new NullPointerException("newOpLocation is null");
        }
        opLocation = newOpLocation;
    }

    /**
     * Returns the step describing the movement.
     *
     * @return The step describing the movement.
     */
    public Route.Step getStep() {
        return step;
    }

    /**
     * Returns the step's index in the route it belongs to.
     *
     * @return The step's index in the route it belongs to.
     * @deprecated Use getStep().getRouteIndex() instead.
     */
    @Deprecated
    public int getStepIndex() {
        return getStep().getRouteIndex();
    }

    /**
     * Returns the operation to be executed after moving.
     *
     * @return The operation to be executed after moving.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Checks whether an operation is to be executed in addition to moving or not.
     *
     * @return <code>true</code> if, and only if, no operation is to be executed.
     */
    public boolean isWithoutOperation() {
        return isEmptyOperation(operation);
    }

    /**
     * Returns the location at which the operation is to be executed. (May be
     * <code>null</code> if <em>operation</em> is <code>NO_OPERATION</code>.)
     *
     * @return The location at which the operation is to be executed.
     */
    public BaseLocation getOpLocation() {
        return opLocation;
    }

    /**
     * Returns the final destination of the drive order this MovementCommand was
     * created for.
     *
     * @return The final destination of the drive order this MovementCommand was
     * created for.
     */
    public Point getFinalDestination() {
        return finalDestination;
    }

    /**
     * Returns the operation to be executed at the <em>final</em> destination
     * position.
     *
     * @return The operation to be executed at the <em>final</em> destination
     * position.
     */
    public String getFinalOperation() {
        return finalOperation;
    }

    /**
     * Returns the properties of the order this command is part of.
     *
     * @return The properties of the order this command is part of.
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    public boolean isExecutedSuccessfully() {
        return executedSuccessfully;
    }

    public void setExecutedSuccessfully(boolean executedSuccessfully) {
        this.executedSuccessfully = executedSuccessfully;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MovementCommand) {
            MovementCommand other = (MovementCommand) o;
            return step.equals(other.step) && operation.equals(other.operation);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return step.hashCode() ^ operation.hashCode();
    }

    @Override
    public String toString() {
        return "movcmd:" + step.toString() + ";"
                + (NO_OPERATION.equals(operation) ? "<no operation>" : operation);
    }

    /**
     * Checks whether an operation means something is to be done in addition to
     * moving or not.
     *
     * @param operation The operation to be checked.
     * @return <code>true</code> if, and only if, the vehicle should only move
     * with the given operation.
     */
    private static boolean isEmptyOperation(String operation) {
        return NO_OPERATION.equals(operation)
                || MOVE_OPERATION.equals(operation)
                || PARK_OPERATION.equals(operation);
    }
}
