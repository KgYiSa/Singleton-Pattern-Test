package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_scene")
//@Table(name = "tcs_model_scene")
public class SceneDto extends BaseEntityDto {
    @DtoField
    @Column(unique = true, nullable = false)
    private String name;

    @JsonProperty("pointDtos")
    @DtoCollection(value = "pointDtos",
                    entityCollectionClass = HashSet.class,
                    dtoCollectionClass = HashSet.class,
                    dtoBeanKey = "PointDto",
                    entityBeanKeys = {"PointDto"},
                    dtoToEntityMatcher = PointDto2PointMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<PointDto> pointDtos;

    @JsonProperty("pathDtos")
    @DtoCollection(value = "pathDtos",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "PathDto",
            entityBeanKeys = {"Path"},
            dtoToEntityMatcher = PathDto2PathMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<PathDto> pathDtos;

    @JsonProperty("locationDtos")
    @DtoCollection(value = "locationDtos",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "LocationDto",
            entityBeanKeys = {"Location"},
            dtoToEntityMatcher = LocationDto2LocationMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<LocationDto> locationDtos;

    @JsonProperty("location_types")
    @DtoCollection(value = "locationTypeDtos",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "LocationTypeDto",
            entityBeanKeys = {"LocationType"},
            dtoToEntityMatcher = LocationTypeDto2LocationTypeMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<LocationTypeDto> locationTypeDtos;

    @JsonProperty("static_routes")
    @DtoCollection(value = "staticRouteDtos",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "StaticRouteDto",
            entityBeanKeys = {"StaticRoute"},
            dtoToEntityMatcher = StaticRouteDto2StaticRouteMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<StaticRouteDto> staticRouteDtos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PointDto> getPointDtos() {
        return pointDtos;
    }

    public void setPointDtos(Set<PointDto> pointDtos) {
        this.pointDtos = pointDtos;
    }

    public PointDto getPointDtoById(long id) {
        if (pointDtos == null) {
            return null;
        }
        Optional<PointDto> pointDto = pointDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (pointDto.isPresent()) {
            return pointDto.get();
        }
        return null;
    }

    public boolean addPointDto(PointDto dto) {
        if (getPointDtos() == null) {
            this.pointDtos = new HashSet<>();
        }

        return this.pointDtos.add(dto);
    }

    public boolean removePoint(PointDto point) {
        if (this.pointDtos == null) {
            return false;
        }
        return this.pointDtos.remove(Objects.requireNonNull(point, "path to removed is null"));
    }

    public boolean removePointById(long id) {
        PointDto point = getPointDtoById(id);
        if (point == null) {
            throw new NullPointerException("path can not be found by id " + id);
        }

        return removePoint(point);
    }
    
    public Set<PathDto> getPathDtos() {
        return pathDtos;
    }

    public void setPathDtos(Set<PathDto> pathDtos) {
        this.pathDtos = pathDtos;
    }

    public boolean addPathDto(PathDto dto) {
        if (getPathDtos() == null) {
            this.pathDtos = new HashSet<>();
        }

        return this.pathDtos.add(dto);
    }

    public PathDto getPathDtoById(long id) {
        if (pathDtos == null) {
            return null;
        }
        Optional<PathDto> pathDto = pathDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (pathDto.isPresent()) {
            return pathDto.get();
        }
        return null;
    }

    public boolean removePathDto(PathDto path) {
        if (this.pathDtos == null) {
            return false;
        }
        return this.pathDtos.remove(Objects.requireNonNull(path, "path to removed is null"));
    }

    public boolean removePathById(long id) {
        PathDto path = getPathDtoById(id);
        if (path == null) {
            throw new NullPointerException("path can not be found by id " + id);
        }

        return removePathDto(path);
    }

    public Set<LocationTypeDto> getLocationTypeDtos() {
        return locationTypeDtos;
    }

    public void setLocationTypeDtos(Set<LocationTypeDto> locationTypes) {
        this.locationTypeDtos = locationTypes;
    }

    public boolean addLocationTypeDto(LocationTypeDto dto) {
        if (getLocationTypeDtos() == null) {
            this.locationTypeDtos = new HashSet<>();
        }

        return this.locationTypeDtos.add(dto);
    }

    public boolean removeLocationTypeDto(LocationTypeDto locationTypeDto) {
        if (this.locationTypeDtos == null) {
            return false;
        }
        return this.locationTypeDtos.remove(Objects.requireNonNull(locationTypeDtos, "locationTypeDtos to removed is null"));
    }

    public LocationTypeDto getLocationTypeDtoById(long id) {
        if (locationTypeDtos == null) {
            return null;
        }

        Optional<LocationTypeDto> locationTypeDtoOp = locationTypeDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (locationTypeDtoOp.isPresent()) {
            return locationTypeDtoOp.get();
        }
        return null;
    }

    public LocationDto getLocationDtoById(long id) {
        if (locationDtos == null) {
            return null;
        }

        Optional<LocationDto> locationDto = locationDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (locationDto.isPresent()) {
            return locationDto.get();
        }
        return null;
    }

    public Set<LocationDto> getLocationDtos() {
        return locationDtos;
    }

    public void setLocationDtos(Set<LocationDto> locations) {
        this.locationDtos = locations;
    }

    public boolean removeLocationDto(LocationDto location) {
        if (this.locationDtos == null) {
            return false;
        }
        return this.locationDtos.remove(Objects.requireNonNull(location, "location to removed is null"));
    }

    public Set<StaticRouteDto> getStaticRouteDtos() {
        return staticRouteDtos;
    }

    public boolean removeStaticRouteDto(StaticRouteDto route) {
        if (this.staticRouteDtos == null) {
            return false;
        }
        return this.staticRouteDtos.remove(Objects.requireNonNull(route, "route to removed is null"));
    }

    public StaticRouteDto getStaticRouteDtoById(long id) {
        if (staticRouteDtos == null) {
            return null;
        }

        Optional<StaticRouteDto> staticRouteDto = staticRouteDtos.stream().filter(sr -> sr.getId() == id).findFirst();
        if (staticRouteDto.isPresent()) {
            return staticRouteDto.get();
        }
        return null;
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
