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
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Scene;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity
@Table(name = "tcs_model_path", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class PathDto extends BaseEntityDto {
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto scene;

    @DtoField
    @Column
    private String name;

    @Column
    private long sourcePoint;

    @Column
    private long destinationPoint;

    @DtoField
    @Column
    private long length = 1L;

    @DtoField
    @Column
    private long routingCost = 1L;

    @DtoField
    @Column
    private int maxVelocity;

    @DtoField
    @Column
    private int maxReverseVelocity;

    @DtoField
    @Column
    private boolean locked;

    public PathDto() {}

    public PathDto(PointDto srcPoint, PointDto destPoint) {
        Objects.requireNonNull(srcPoint, "srcPointDto");
        Objects.requireNonNull(destPoint, "destPointDto");

        sourcePoint = srcPoint.getId();
        destinationPoint = destPoint.getId();
    }

    public SceneDto getScene() {
        return scene;
    }

    public void setScene(SceneDto scene) {
        this.scene = scene;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSourcePoint() {
        return sourcePoint;
    }

    public void setSourcePoint(long sourcePoint) {
        this.sourcePoint = sourcePoint;
    }

    public long getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(long destinationPoint) {
        this.destinationPoint = destinationPoint;
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
