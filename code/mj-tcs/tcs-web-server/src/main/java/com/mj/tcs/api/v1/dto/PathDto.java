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

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class PathDto extends BaseEntityAuditDto {
    @DtoField
    private String name;

    private long sourcePointId;
    private long destinationPointId;

    @DtoField
    private long length;

    @DtoField
    private long routingCost;

    @DtoField
    private int maxVelocity;

    @DtoField
    private int maxReverseVelocity;

    @DtoField
    private boolean locked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSourcePointId() {
        return sourcePointId;
    }

    public void setSourcePointId(long sourcePointId) {
        this.sourcePointId = sourcePointId;
    }

    public long getDestinationPointId() {
        return destinationPointId;
    }

    public void setDestinationPointId(Long destinationPointId) {
        this.destinationPointId = destinationPointId;
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
}
