/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.*;
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
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@UUID")
@Dto
@Entity(name = "tcs_model_path")
//@Table(name = "tcs_model_path", uniqueConstraints =
//    @UniqueConstraint(columnNames = {"name", "scene"})
//)
public class PathDto extends BaseEntityDto {
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @JsonProperty("source_point")
//    @JsonBackReference("outgoing_paths")
    @JsonIgnoreProperties({"version", "auditor", "properties", "position", "type", "display_position_x", "display_position_y", "label_offset_x", "label_offset_y", "vehicle_orientation_angle", "incoming_paths", "outgoing_paths", "attached_links"})
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "source_point")
//    @Column(name = "sourcePoint")
    private PointDto sourcePointDto;

    @JsonProperty("destination_point")
//    @JsonBackReference("incoming_paths")
    @JsonIgnoreProperties({"version", "auditor", "properties", "position", "type", "display_position_x", "display_position_y", "label_offset_x", "label_offset_y", "vehicle_orientation_angle", "incoming_paths", "outgoing_paths", "attached_links"})
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "destination_point")
//    @Column(name = "destinationPoint")
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
    private double maxVelocity;

    @DtoField
    @Column
    private double maxReverseVelocity;

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

    public List<TripleDto> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<TripleDto> controlPoints) {
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

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getMaxReverseVelocity() {
        return maxReverseVelocity;
    }

    public void setMaxReverseVelocity(double maxReverseVelocity) {
        this.maxReverseVelocity = maxReverseVelocity;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
