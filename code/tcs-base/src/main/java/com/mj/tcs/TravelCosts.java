/*
 * MJTCS copyright information:
 * Copyright (c) 2012 Fraunhofer IML
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package com.mj.tcs;

import com.mj.tcs.data.model.BaseLocation;

import java.io.Serializable;

/**
 * This class is used for calculating the costs to travel from one location
 * to another. *
 * @author Philipp Seifert (Fraunhofer IML)
 */
public final class TravelCosts implements Comparable<TravelCosts>, Serializable {

    /**
     * The destination reference.
     */
    private final BaseLocation location;
    /**
     * The costs to travel to this location.
     */
    private final long costs;

    /**
     * Creates new travel costs.
     *
     * @param location A reference to the destination
     * @param costs The costs
     */
    public TravelCosts(BaseLocation location, long costs) {
        this.location = location;
        this.costs = costs;
    }

    @Override
    public int compareTo(TravelCosts t) {
        if (costs < t.costs) {
            return -1;
        }
        else if (costs > t.costs) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Returns the reference to the destination.
     *
     * @return The destination's reference
     */
    public BaseLocation getLocation() {
        return location;
    }

    /**
     * Returns the costs.
     *
     * @return The costs
     */
    public long getCosts() {
        return costs;
    }
}
