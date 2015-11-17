/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseResource;
import com.mj.tcs.data.base.Triple;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author liumin
 * @author Wang Zhen
 */
@Entity
@Table(name = "tcs_model_path", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class Path extends BaseResource implements Cloneable {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private Scene scene;

    /**
     * A reference to the point which this point originates in.
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Point sourcePoint;

    /**
     * A reference to the point which this point ends in.
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Point destinationPoint;

    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_path_control_point")
    private List<Triple> controlPoints = new ArrayList<>();

    /**
     * The length of this path (in mm).
     */
    @Column(name = "path_length")
    private long length = 1L;

    /**
     * An explicit (unitless) weight that can be used to influence routing. The
     * higher the value, the more travelling this path costs.
     */
    @Column(name = "routing_cost")
    private long routingCost = 1L;

    /**
     * The absolute maximum allowed forward velocity on this path (in mm/s). A
     * value of 0 (default) means forward movement is not allowed on this path.
     */
    @Column(name = "max_velocity")
    private int maxVelocity;

    /**
     * The absolute maximum allowed reverse velocity on this path (in mm/s). A
     * value of 0 (default) means reverse movement is not allowed on this path.
     */
    @Column(name = "max_reverse_velocity")
    private int maxReverseVelocity;

    /**
     * A flag for marking this path as locked (i.e. to prevent vehicles from
     * using it).
     */
    private boolean locked;

    public Path() {
    }

    public Path(Point srcPoint, Point destPoint) {
        setSourcePoint(srcPoint);
        setDestinationPoint(destPoint);
    }

    /**
     * Clear its ID only
     */
    @Override
    public void clearId() {
        setId(null);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Point getSourcePoint() {
        return sourcePoint;
    }

    public void setSourcePoint(Point sourcePoint) {
        this.sourcePoint = sourcePoint;
    }

    public Point getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(Point destinationPoint) {
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
