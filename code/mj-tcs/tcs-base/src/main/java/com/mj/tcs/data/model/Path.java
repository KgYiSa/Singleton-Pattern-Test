/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tcs.data.model;

import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.base.TCSResource;
import com.mj.tcs.data.base.Triple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author liumin
 * @author Wang Zhen
 */
public class Path
        extends TCSResource<Path>
        implements Serializable, Cloneable {

    /**
     * A reference to the point which this point originates in.
     */
    private TCSObjectReference<Point> sourcePoint;

    /**
     * A reference to the point which this point ends in.
     */
    private TCSObjectReference<Point> destinationPoint;

    private List<Triple> controlPoints = new ArrayList<>();

    /**
     * The length of this path (in mm).
     */
    private long length = 1L;

    /**
     * An explicit (unitless) weight that can be used to influence routing. The
     * higher the value, the more travelling this path costs.
     */
    private long routingCost = 1L;

    /**
     * The absolute maximum allowed forward velocity on this path (in mm/s). A
     * value of 0 (default) means forward movement is not allowed on this path.
     */
    private int maxVelocity;

    /**
     * The absolute maximum allowed reverse velocity on this path (in mm/s). A
     * value of 0 (default) means reverse movement is not allowed on this path.
     */
    private int maxReverseVelocity;

    /**
     * A flag for marking this path as locked (i.e. to prevent vehicles from
     * using it).
     */
    private boolean locked;

    /**
     * Creates a new Path.
     *
     * @param objectID The new path's object ID.
     * @param name The new path's name.
     * @param srcPoint A reference to this path's starting point.
     * @param destPoint A reference to this path's destination point.
     */
    public Path(int objectID, String name, TCSObjectReference<Point> srcPoint,
                TCSObjectReference<Point> destPoint) {
        super(objectID, name);
        sourcePoint = Objects.requireNonNull(srcPoint, "srcPoint is null");
        destinationPoint = Objects.requireNonNull(destPoint, "destPoint is null");
    }

    public TCSObjectReference<Point> getSourcePoint() {
        return sourcePoint;
    }

    public void setSourcePoint(TCSObjectReference<Point> sourcePoint) {
        this.sourcePoint = sourcePoint;
    }

    public TCSObjectReference<Point> getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(TCSObjectReference<Point> destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public List<Triple> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<Triple> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getRoutingCost() {
        return routingCost;
    }

    public void setRoutingCost(long routingCost) {
        this.routingCost = routingCost;
    }

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public int getMaxReverseVelocity() {
        return maxReverseVelocity;
    }

    public void setMaxReverseVelocity(int maxReverseVelocity) {
        this.maxReverseVelocity = maxReverseVelocity;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Checks whether this path is navigable in forward direction.
     *
     * @return <code>true</code> if, and only if, this path is not locked and its
     * maximum forward velocity is not zero.
     */
    public boolean isNavigableForward() {
        return !locked && maxVelocity != 0;
    }

    /**
     * Checks whether this path is navigable in backward/reverse direction.
     *
     * @return <code>true</code> if, and only if, this path is not locked and its
     * maximum reverse velocity is not zero.
     */
    public boolean isNavigableReverse() {
        return !locked && maxReverseVelocity != 0;
    }

    /**
     * Checks whether this path is navigable towards the given point.
     *
     * @param navPoint The point.
     * @return If <code>navPoint</code> is this path's destination point, returns
     * <code>isNavigableForward()</code>; if <code>navPoint</code> is this path's
     * source point, returns <code>isNavigableReverse()</code>.
     * @throws IllegalArgumentException If the given point is neither the source
     * point nor the destination point of this path.
     */
    public boolean isNavigableTo(Point navPoint)
            throws IllegalArgumentException {
        if (Objects.equals(navPoint, destinationPoint)) {
            return isNavigableForward();
        }
        else if (Objects.equals(navPoint, sourcePoint)) {
            return isNavigableReverse();
        }
        else {
            throw new IllegalArgumentException(
                    navPoint.getName() + " is not an end point of " + this);
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public Path clone() {
        Path clone = null;
        clone = (Path) super.clone();
        clone.sourcePoint = sourcePoint.clone();
        clone.destinationPoint = destinationPoint.clone();
        return clone;
    }
}
