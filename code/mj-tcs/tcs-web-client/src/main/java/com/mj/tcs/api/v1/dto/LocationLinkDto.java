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
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class LocationLinkDto extends BaseEntityAuditDto {
    @DtoField
    private String name;
    /**
     * A reference to the locationDto end of this link.
     */
    @JsonProperty("location")
    @JsonBackReference
    // Convert outside in case of stack overflow
    private LocationDto locationDto;
    /**
     * A reference to the pointDto end of this link.
     */
    @JsonProperty("point")
//    @DtoField(value = "point",
//            dtoBeanKey = "PointDto",
//            entityBeanKeys = {"Point"})
    private Long pointId;
    /**
     * The operations allowed at this link.
     */
    @DtoField
    private Set<String> allowedOperations = new HashSet<>();

    public LocationLinkDto() {
    }

    public LocationLinkDto(LocationDto linkLocationDto,
                           Long linkedPointId) {
        locationDto = Objects.requireNonNull(linkLocationDto, "linkLocationDto is null");
        this.pointId = Objects.requireNonNull(linkedPointId, "linkedPointId is null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPointId() {
        return pointId;
    }

    @JsonBackReference
    public LocationDto getLocationDto() {
        return locationDto;
    }

    @JsonBackReference
    public void setLocationDto(LocationDto locationDto) {
        this.locationDto = locationDto;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
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
