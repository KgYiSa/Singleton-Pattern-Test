/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class StaticRouteDto extends BaseEntityAuditDto {
    @DtoField
    private String name;

    private List<Long> hops;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the first element of the list of hops in this route, or
     * <code>null</code>, if the list of hops is empty.
     *
     * @return The first element of the list of hops in this route, or
     * <code>null</code>, if the list of hops is empty.
     */
    public Long getSourcePointId() {
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
    public Long getDestinationPointId() {
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
    public List<Long> getHops() {
        return new LinkedList<>(hops);
    }

    public void setHops(List<Long> hops) {
        this.hops = hops;
    }

    /**
     * Adds a hop to the end of this route.
     *
     * @param newHop The hop to be added.
     */
    public void addHopId(Long newHop) {
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
        return hops != null || hops.size() >= 2;
    }
}
