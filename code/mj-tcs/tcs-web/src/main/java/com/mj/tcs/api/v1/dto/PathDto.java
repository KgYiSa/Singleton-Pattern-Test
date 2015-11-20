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
import com.mj.tcs.api.v1.dto.base.TripleDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private PointDto sourcePointDto;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private PointDto destinationPointDto;


    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_path_control_points")
    private List<TripleDto> controlPoints = new ArrayList<>();

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
        sourcePointDto = Objects.requireNonNull(srcPoint, "srcPointDto");;
        destinationPointDto = Objects.requireNonNull(destPoint, "destPointDto");
    }

    public SceneDto getSceneDto() {
        return sceneDto;
    }

    public void setSceneDto(SceneDto sceneDto) {
        this.sceneDto = sceneDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PointDto getSourcePointDto() {
        return sourcePointDto;
    }

    public void setSourcePointDto(PointDto sourcePointDto) {
        this.sourcePointDto = sourcePointDto;
    }

    public PointDto getDestinationPointDto() {
        return destinationPointDto;
    }

    public void setDestinationPointDto(PointDto destinationPointDto) {
        this.destinationPointDto = destinationPointDto;
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
