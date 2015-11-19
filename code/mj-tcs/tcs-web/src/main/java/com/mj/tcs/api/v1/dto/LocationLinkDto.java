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
import com.mj.tcs.data.model.Scene;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity
@Table(name = "tcs_model_location_link", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "scene"})
)
public class LocationLinkDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private Scene scene;

    @DtoField
    @Column
    private String name;
    /**
     * A reference to the location end of this link.
     */
    @JsonProperty("location")
    @JsonBackReference
    // Convert outside in case of stack overflow
    @Column
    private Long location;

    /**
     * A reference to the pointDto end of this link.
     */
    @JsonProperty("point")
//    @DtoField(value = "point",
//            dtoBeanKey = "PointDto",
//            entityBeanKeys = {"Point"})
    @Column
    private Long point;

    /**
     * The operations allowed at this link.
     */
    @DtoField
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_link_operations")
    private Set<String> allowedOperations = new HashSet<>();

    public LocationLinkDto() {
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPoint() {
        return point;
    }

    @JsonBackReference
    public Long getLocation() {
        return location;
    }

    @JsonBackReference
    public void setLocation(Long location) {
        this.location = location;
    }

    public void setPoint(Long point) {
        this.point = point;
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
