package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;
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

    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_scene_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new HashSet<>();

    @JsonProperty("points")
    @DtoCollection(value = "pointDtos",
                    entityCollectionClass = HashSet.class,
                    dtoCollectionClass = HashSet.class,
                    dtoBeanKey = "PointDto",
                    entityBeanKeys = {"PointDto"},
                    dtoToEntityMatcher = PointDto2PointMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<PointDto> pointDtos;

    @JsonProperty("paths")
    @DtoCollection(value = "pathDtos",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "PathDto",
            entityBeanKeys = {"Path"},
            dtoToEntityMatcher = PathDto2PathMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    private Set<PathDto> pathDtos;

    @JsonProperty("locations")
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

    /**
     * Add property. It can be used to put any unknown propery during deSerialization.
     *
     * @param name
     * @param value
     */
    public void addProperty(String name, String value, String type) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            propertyOptional.get().setValue(Objects.requireNonNull(value));
            propertyOptional.get().setType(Objects.requireNonNull(type));
        } else {
            EntityProperty property = new EntityProperty();
            property.setName(Objects.requireNonNull(name));
            property.setValue(Objects.requireNonNull(value));
            property.setType(Objects.requireNonNull(type));
            properties.add(property);
        }
    }

    public String getProperty(String name) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            return propertyOptional.get().getValue();
        }

        return null;
    }

    public void setProperties(Set<EntityProperty> properties) {
        this.properties = properties;
    }

    public Set<EntityProperty> getProperties() {
        return properties;
    }

    public Set<PointDto> getPointDtos() {
        return pointDtos;
    }

    public void setPointDtos(Set<PointDto> pointDtos) {
        this.pointDtos = pointDtos;
    }

    public PointDto getPointDtoByUUID(String uuid) {
        if (pointDtos == null || uuid == null) {
            return null;
        }
        Optional<PointDto> pointDto = pointDtos.stream().filter(p -> uuid.equals(p.getUUID())).findFirst();
        if (pointDto.isPresent()) {
            return pointDto.get();
        }
        return null;
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
            throw new NullPointerException("path can not be found by uuid " + id);
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

    public PathDto getPathDtoByUUID(String uuid) {
        if (pathDtos == null || uuid == null) {
            return null;
        }
        Optional<PathDto> pathDto = pathDtos.stream().filter(l -> uuid.equals(l.getUUID())).findFirst();
        if (pathDto.isPresent()) {
            return pathDto.get();
        }
        return null;
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
            throw new NullPointerException("path can not be found by uuid " + id);
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

    public LocationTypeDto getLocationTypeDtoByUUID(String uuid) {
        if (locationTypeDtos == null || uuid == null) {
            return null;
        }
        Optional<LocationTypeDto> locationTypeDto = locationTypeDtos.stream().filter(l -> uuid.equals(l.getUUID())).findFirst();
        if (locationTypeDto.isPresent()) {
            return locationTypeDto.get();
        }
        return null;
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

    public LocationDto getLocationDtoByUUID(String uuid) {
        if (locationDtos == null || uuid == null) {
            return null;
        }
        Optional<LocationDto> locationDto = locationDtos.stream().filter(l -> uuid.equals(l.getUUID())).findFirst();
        if (locationDto.isPresent()) {
            return locationDto.get();
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

    public StaticRouteDto getStaticRouteDtoByUUID(String uuid) {
        if (staticRouteDtos == null || uuid == null) {
            return null;
        }
        Optional<StaticRouteDto> routeDto = staticRouteDtos.stream().filter(r -> uuid.equals(r.getUUID())).findFirst();
        if (routeDto.isPresent()) {
            return routeDto.get();
        }
        return null;
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
