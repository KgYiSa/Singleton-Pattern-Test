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
import java.util.*;

/**
 * A point is a node/position in the scene graph.
 *
 * @author liumin
 * @author Wang Zhen
 */
public class Point
        extends TCSResource<Point>
        implements Serializable, Cloneable {

    /**
     * This point's coordinates in mm.
     */
    private Triple position = new Triple();

    /**
     * The vehicle's (assumed) orientation angle (-360°..360°) when it is at
     * this position. May be Double.NaN if an orientation angle is not defined
     * for this point.
     */
    private double vehicleOrientationAngle = 0d/*Double.NaN*/;//Can NOT be NaN, will cause issue: java.sql.SQLException: 'NaN' is not a valid numeric or approximate numeric value

    /**
     * The vehicle occupying this point.
     */
    private TCSObjectReference<Vehicle> occupyingVehicle;

    /**
     * This point's type.
     */
    private Type type = Type.HALT_POSITION;

    private Set<TCSObjectReference<Path>> incomingPaths = new LinkedHashSet<>();

    private Set<TCSObjectReference<Path>> outgoingPaths = new LinkedHashSet<>();

    private Set<Location.Link> attachedLinks = new LinkedHashSet<>();

    /**
     * Creates a new point with the given name.
     *
     * @param objectUUID This point's object UUID.
     * @param name This point's name.
     */
    public Point(String objectUUID, String name) {
        super(objectUUID, name);
    }

    // Methods not declared in any interface start here

    /**
     * Returns the physical coordinates of this point in mm.
     *
     * @return The physical coordinates of this point in mm.
     */
    public Triple getPosition() {
        return position;
    }

    /**
     * Sets the physical coordinates of this point in mm.
     *
     * @param newPosition The new physical coordinates of this point. May not be
     * <code>null</code>.
     */
    public void setPosition(Triple newPosition) {
        if (newPosition == null) {
            throw new NullPointerException("newPosition is null");
        }
        position = newPosition;
    }

    /**
     * Returns a vehicle's orientation angle at this position. (-360..360, or
     * <code>Double.NaN</code>, if an orientation angle is not specified for
     * this point.)
     *
     * @return The vehicle's orientation angle when it's at this position.
     */
    public double getVehicleOrientationAngle() {
        return vehicleOrientationAngle;
    }

    /**
     * Sets the vehicle's (assumed) orientation angle when it's at this
     * position. Allowed value range: [-360..360], or <code>Double.NaN</code> to
     * indicate that there is no specific orientation angle for this point.
     *
     * @param angle The new angle.
     */
    public void setVehicleOrientationAngle(double angle) {
        if (!Double.isNaN(angle) && (angle > 360.0 || angle < -360.0)) {
            throw new IllegalArgumentException("angle not in [-360..360]: " + angle);
        }
        vehicleOrientationAngle = angle;
    }

    public TCSObjectReference<Vehicle> getOccupyingVehicle() {
        return occupyingVehicle;
    }

    public void setOccupyingVehicle(TCSObjectReference<Vehicle> occupyingVehicle) {
        this.occupyingVehicle = occupyingVehicle;
    }

    /**
     * Returns this point's type.
     *
     * @return This point's type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets this point's type.
     *
     * @param newType This point's new type.
     */
    public void setType(Type newType) {
        if (newType == null) {
            throw new NullPointerException("newType is null");
        }

        type = newType;
    }

    /**
     * Checks whether parking a vehicle on this point is allowed.
     * <p>
     * This method is a convenience method; its return value is equal to
     * <code>getType().equals(Point.Type.PARK_POSITION)</code>.
     * </p>
     *
     * @return <code>true</code> if, and only if, parking is allowed on this
     * point.
     */
    public boolean isParkingPosition() {
        return Type.PARK_POSITION.equals(type);
    }

    /**
     * Checks whether halting on this point is allowed.
     * <p>
     * This method is a convenience method; its return value is equal to      <code>getType().equals(Point.Type.PARK_POSITION) ||
     * getType().equals(Point.Type.HALT_POSITION)</code>.
     * </p>
     *
     * @return <code>true</code> if, and only if, halting is allowed on this
     * point.
     */
    public boolean isHaltingPosition() {
        return Type.PARK_POSITION.equals(type) || Type.HALT_POSITION.equals(type);
    }

    public Set<TCSObjectReference<Path>> getIncomingPaths() {
        return incomingPaths;
    }

    public void setIncomingPaths(Set<TCSObjectReference<Path>> incomingPaths) {
        this.incomingPaths = incomingPaths;
    }

    public Set<TCSObjectReference<Path>> getOutgoingPaths() {
        return outgoingPaths;
    }

    public void setOutgoingPaths(Set<TCSObjectReference<Path>> outgoingPaths) {
        this.outgoingPaths = outgoingPaths;
    }

    /**
     * Adds a path ending in this point.
     * If the path is already among the incoming paths of this point,
     * nothing happens.
     *
     * @param newPath The path to be added.
     */
    public void addIncomingPath(TCSObjectReference<Path> newPath) {
        if (newPath == null) {
            throw new NullPointerException("newPath is null");
        }
        incomingPaths.add(newPath);
    }

    /**
     * Removes a path ending in this point.
     * If the path is not among the incoming paths of this point,
     * nothing happens.
     *
     * @param rmPath The path to be removed.
     */
    public void removeIncomingPath(TCSObjectReference<Path> rmPath) {
        if (rmPath == null) {
            throw new NullPointerException("rmPath is null");
        }
        incomingPaths.remove(rmPath);
    }

    /**
     * Adds a path originating in this point.
     * If the path is already among the outgoing paths of this point,
     * nothing happens.
     *
     * @param newPath The path to be added.
     */
    public void addOutgoingPath(TCSObjectReference<Path> newPath) {
        if (newPath == null) {
            throw new NullPointerException("newPath is null");
        }
        outgoingPaths.add(newPath);
    }

    /**
     * Removes a path originating in this point.
     * If the path is not among the outgoing paths of this point,
     * nothing happens.
     *
     * @param rmPath A reference to the path to be removed.
     */
    public void removeOutgoingPath(TCSObjectReference<Path> rmPath) {
        if (rmPath == null) {
            throw new NullPointerException("rmPath is null");
        }
        outgoingPaths.remove(rmPath);
    }

    public Set<Location.Link> getAttachedLinks() {
        return attachedLinks;
    }

    public void setAttachedLinks(Set<Location.Link> attachedLinks) {
        this.attachedLinks = attachedLinks;
    }

    public boolean attachLink(Location.Link link) {
        Objects.requireNonNull(link, "link is null");
        if (!getUUID().equals(link.getPoint().getUUID())) {
            throw new IllegalArgumentException("point end of link is not this point");
        }
        return attachedLinks.add(link);
    }

    public boolean detachLink(Location location) {
        Objects.requireNonNull(location, "location is null");
        Iterator<Location.Link> linkIterator = attachedLinks.iterator();
        while (linkIterator.hasNext()) {
            Location.Link curLink = linkIterator.next();
            if (location.equals(curLink.getLocation())) {
                linkIterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public Point clone() {
        Point clone = null;
        clone = (Point) super.clone();
        clone.position = (position == null) ? null : position.clone();
        clone.occupyingVehicle = (occupyingVehicle == null) ? null : occupyingVehicle.clone();
        clone.incomingPaths = new LinkedHashSet<>(incomingPaths);
        clone.outgoingPaths = new LinkedHashSet<>(outgoingPaths);
        clone.attachedLinks = new LinkedHashSet<>(attachedLinks);
        return clone;
    }

    /**
     * The elements of this enumeration describe the various types of positions
     * known in openTCS scene.
     */
    public enum Type {

        /**
         * Indicates a position at which a vehicle is expected to report in.
         * Halting or even parking at such a position is not allowed.
         */
        REPORT_POSITION("REPORT"),
        /**
         * Indicates a position at which a vehicle may halt temporarily, e.g.
         * for executing an operating. The vehicle is also expected to report in
         * when it arrives at such a position. It may not park here for longer
         * than necessary, though.
         */
        HALT_POSITION("HALT"),
        /**
         * Indicates a position at which a vehicle may halt for longer periods
         * of time when it is not processing orders. The vehicle is also
         * expected to report in when it arrives at such a position.
         */
        PARK_POSITION("PARK");

        private String text;

        Type(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static Type fromString(String text) {
            Optional<Type> type = Arrays.stream(Type.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (type.isPresent()) {
                return type.get();
            }

            throw new IllegalArgumentException("The Point.Type enum type is no recognizable [text=" + text + "]");
        }
    }
}
