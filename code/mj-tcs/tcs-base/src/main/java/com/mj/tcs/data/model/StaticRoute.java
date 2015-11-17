/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A named static route that can be used as an alternative to dynamically
 * computing a route for a vehicle.
 *
 * @author Wang Zhen
 */
@Entity
@Table(name = "tcs_model_static_route", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class StaticRoute extends BaseEntity implements Cloneable {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private Scene scene;

    /**
     * The sequence of points this route consists of.
     */
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_static_route_hops")
    private List<Point> hops = new LinkedList<>();

    public StaticRoute() {
    }

    public StaticRoute(String name) {
        setName(name);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Returns the first element of the list of hops in this route, or
     * <code>null</code>, if the list of hops is empty.
     *
     * @return The first element of the list of hops in this route, or
     * <code>null</code>, if the list of hops is empty.
     */
    public Point getSourcePoint() {
        if (hops.isEmpty()) {
            return null;
        }
        else {
            return hops.get(0);
        }
    }

    /**
     * Returns the final element of the list of hops in this route, or
     * <code>null</code>, if the list of hops is empty.
     *
     * @return The final element of the list of hops in this route, or
     * <code>null</code>, if the list of hops is empty.
     */
    public Point getDestinationPoint() {
        if (hops.isEmpty()) {
            return null;
        }
        else {
            return hops.get(hops.size() - 1);
        }
    }

    /**
     * Returns the sequence of points this route consists of.
     *
     * @return The sequence of points this route consists of.
     */
    public List<Point> getHops() {
        return hops;
    }

    public void setHops(List<Point> hops) {
        this.hops = hops;
    }

    /**
     * Adds a hop to the end of this route.
     *
     * @param newHop The hop to be added.
     */
    public void addHop(Point newHop) {
        Objects.requireNonNull(newHop, "newHop");
        hops.add(newHop);
    }

    /**
     * Removes all hops from this route.
     */
    public void clearHops() {
        hops.clear();
    }

    /**
     * Checks whether this static route is valid or not.
     * A static route is valid if it has at least two hops.
     *
     * @return <code>true</code> if, and only if, this static route is valid.
     */
    public boolean isValid() {
        return hops.size() >= 2;
    }

    @Override
    public StaticRoute clone() throws IllegalStateException {
        StaticRoute clone = null;
        clone = (StaticRoute) super.clone();
        clone.hops = new LinkedList<>();
        if (hops != null) {
            for (Point curPoint : hops) {
                clone.hops.add(curPoint);
            }
        }

        return clone;
    }
}
