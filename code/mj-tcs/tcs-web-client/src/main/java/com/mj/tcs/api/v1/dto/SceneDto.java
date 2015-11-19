package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class SceneDto extends BaseEntityAuditDto {
    @DtoField
    private String name;

    /**
     * This scene's state (UNKNOWN, MODELLING, OPERATING)
     */
    @JsonIgnore
    private static final String[] states;
    @DtoField
    private String state = "UNKNOWN";

    @JsonProperty("points")
    @DtoCollection(value = "points",
                    entityCollectionClass = HashSet.class,
                    dtoCollectionClass = HashSet.class,
                    dtoBeanKey = "PointDto",
                    entityBeanKeys = {"Point"},
                    dtoToEntityMatcher = PointDto2PointMatcher.class)
    private Set<PointDto> pointDtos;

    @JsonProperty("paths")
    @DtoCollection(value = "paths",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "PathDto",
            entityBeanKeys = {"Path"},
            dtoToEntityMatcher = PathDto2PathMatcher.class)
    private Set<PathDto> pathDtos;

    @JsonProperty("locations")
    @DtoCollection(value = "locations",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "LocationDto",
            entityBeanKeys = {"Location"},
            dtoToEntityMatcher = LocationDto2LocationMatcher.class)
    private Set<LocationDto> locationDtos;

    @JsonProperty("location_types")
    @DtoCollection(value = "locationTypes",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "LocationTypeDto",
            entityBeanKeys = {"LocationType"},
            dtoToEntityMatcher = LocationTypeDto2LocationTypeMatcher.class)
    private Set<LocationTypeDto> locationTypeDtos;

    @JsonProperty("static_routes")
    @DtoCollection(value = "staticRoutes",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "StaticRouteDto",
            entityBeanKeys = {"StaticRoute"},
            dtoToEntityMatcher = StaticRouteDto2StaticRouteMatcher.class)
    private Set<StaticRouteDto> staticRouteDtos;

    static {
        states = new String[] {
                "UNKNOWN", "MODELLING", "OPERATING"
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<PointDto> getPointDtos() {
        return pointDtos;
    }

    public void setPointDtos(Set<PointDto> pointDtos) {
        this.pointDtos = pointDtos;
    }

    public Set<PathDto> getPathDtos() {
        return pathDtos;
    }

    public void setPathDtos(Set<PathDto> pathDtos) {
        this.pathDtos = pathDtos;
    }

    public static String[] getStates() {
        return states;
    }

    public Set<LocationTypeDto> getLocationTypeDtos() {
        return locationTypeDtos;
    }

    public void setLocationTypeDtos(Set<LocationTypeDto> locationTypeDtos) {
        this.locationTypeDtos = locationTypeDtos;
    }

    public boolean addLocationTypeDto(LocationTypeDto dto) {
        if (getLocationTypeDtos() == null) {
            this.locationTypeDtos = new HashSet<>();
        }

        return this.locationTypeDtos.add(dto);
    }

    public Optional<LocationDto> getLocationDtoById(long id) {
        if (locationDtos == null) {
            return Optional.ofNullable(null);
        }

        return locationDtos.stream().filter(l -> l.getId() == id).findFirst();
    }

    public Set<LocationDto> getLocationDtos() {
        return locationDtos;
    }

    public void setLocationDtos(Set<LocationDto> locationDtos) {
        this.locationDtos = locationDtos;
    }

    public Set<StaticRouteDto> getStaticRouteDtos() {
        return staticRouteDtos;
    }

    public Optional<StaticRouteDto> getStaticRouteDtoById(long id) {
        if (staticRouteDtos == null) {
            return Optional.ofNullable(null);
        }

        return staticRouteDtos.stream().filter(sr -> sr.getId() == id).findFirst();
    }

    public void setStaticRouteDtos(Set<StaticRouteDto> staticRouteDtos) {
        this.staticRouteDtos = staticRouteDtos;
    }

    public boolean addStaticRouteDto(StaticRouteDto dto) {
        if (getStaticRouteDtos() == null) {
            this.staticRouteDtos = new HashSet<>();
        }

        return this.staticRouteDtos.add(dto);
    }

    public boolean addLocationDto(LocationDto dto) {
        if (getLocationDtos() == null) {
            this.locationDtos = new HashSet<>();
        }

        return this.locationDtos.add(dto);
    }
}
