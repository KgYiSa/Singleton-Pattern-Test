/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_location_link")
//@Table(name = "tcs_model_location_link", uniqueConstraints =
//@UniqueConstraint(columnNames = {"name", "scene"})
//)
public class LocationLinkDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;
    /**
     * A reference to the locationDto end of this link.
     */
    @JsonProperty("locationDto")
    @JsonBackReference
    // Convert outside in case of stack overflow
    @ManyToOne
    private LocationDto locationDto;

    /**
     * A reference to the pointDto end of this link.
     */
    @JsonProperty("pointDto")
//    @DtoField(value = "pointDto",
//            dtoBeanKey = "PointDto",
//            entityBeanKeys = {"Point"})
    @ManyToOne
    private PointDto pointDto;

    /**
     * The operations allowed at this link.
     */
    @DtoField
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_link_operations")
    private Set<String> allowedOperations = new HashSet<>();

    public LocationLinkDto() {
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

    public PointDto getPointDto() {
        return pointDto;
    }

    @JsonBackReference
    public LocationDto getLocationDto() {
        return locationDto;
    }

    @JsonBackReference
    public void setLocationDto(LocationDto locationDto) {
        this.locationDto = locationDto;
    }

    public void setPointDto(PointDto pointDto) {
        this.pointDto = pointDto;
    }

    public Set<String> getAllowedOperations() {
        return allowedOperations;
    }

    public void setAllowedOperations(Set<String> allowedOperations) {
        this.allowedOperations = allowedOperations;
    }

    public boolean addAllowedOperation(String operation) {
        if (this.allowedOperations == null) {
            this.allowedOperations = new HashSet<>();
        }

        return this.allowedOperations.add(operation);
    }

    public boolean removeAllowedOperation(String operation) {
        if (this.allowedOperations == null) {
            this.allowedOperations = new HashSet<>();
            return true;
        }

        return this.allowedOperations.remove(operation);
    }
}
