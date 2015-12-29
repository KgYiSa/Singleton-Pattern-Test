package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;

import javax.persistence.*;
import java.util.LinkedHashSet;
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

    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    // TODO: DTO necessary ??
    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_scene_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new LinkedHashSet<>();

    @JsonProperty("points")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<PointDto> pointDtos;

    @JsonProperty("paths")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<PathDto> pathDtos;

    @JsonProperty("locations")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<LocationDto> locationDtos;

    @JsonProperty("location_types")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<LocationTypeDto> locationTypeDtos;

    @JsonProperty("blocks")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<BlockDto> blockDtos;

    @JsonProperty("static_routes")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<StaticRouteDto> staticRouteDtos;

    @JsonProperty("vehicles")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
//    @DtoCollection(value = "vehicleDtos",
//            entityCollectionClass = HashSet.class,
//            dtoCollectionClass = HashSet.class,
//            dtoBeanKey = "VehicleDto",
//            entityBeanKeys = {"Vehicle"},
//            dtoToEntityMatcher = StaticRouteDto2StaticRouteMatcher.class)
    @OneToMany(mappedBy = "sceneDto", cascade = {CascadeType.ALL})
    @OrderBy(value = "name ASC")
    private Set<VehicleDto> vehicleDtos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add property. It can be used to put any unknown property during deSerialization.
     *
     * Note: If value is null, then remove the property.
     *
     * @param name
     * @param value
     */
    public void addProperty(String name, String value) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            if (value == null) {
                properties.remove(propertyOptional.get());
                return;
            } else {
                propertyOptional.get().setValue(Objects.requireNonNull(value));
            }
        } else {
            if (value == null) {
                return;
            } else {
                EntityProperty property = new EntityProperty();
                property.setName(Objects.requireNonNull(name));
                property.setValue(Objects.requireNonNull(value));
                properties.add(property);
            }
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
        Optional<PointDto> pointDtoOp = pointDtos.stream().filter(p -> uuid.equals(p.getUUID())).findFirst();
        if (pointDtoOp.isPresent()) {
            return pointDtoOp.get();
        }
        return null;
    }

    public PointDto getPointDtoById(long id) {
        if (pointDtos == null) {
            return null;
        }
        Optional<PointDto> pointDtoOp = pointDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (pointDtoOp.isPresent()) {
            return pointDtoOp.get();
        }
        return null;
    }

    public boolean addPointDto(PointDto dto) {
        if (getPointDtos() == null) {
            this.pointDtos = new LinkedHashSet<>();
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
            this.pathDtos = new LinkedHashSet<>();
        }

        return this.pathDtos.add(dto);
    }

    public PathDto getPathDtoByUUID(String uuid) {
        if (pathDtos == null || uuid == null) {
            return null;
        }
        Optional<PathDto> pathDtoOp = pathDtos.stream().filter(l -> uuid.equals(l.getUUID())).findFirst();
        if (pathDtoOp.isPresent()) {
            return pathDtoOp.get();
        }
        return null;
    }

    public PathDto getPathDtoById(long id) {
        if (pathDtos == null) {
            return null;
        }
        Optional<PathDto> pathDtoOp = pathDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (pathDtoOp.isPresent()) {
            return pathDtoOp.get();
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
            this.locationTypeDtos = new LinkedHashSet<>();
        }

        return this.locationTypeDtos.add(dto);
    }

    public boolean removeLocationTypeDto(LocationTypeDto locationTypeDto) {
        if (this.locationTypeDtos == null) {
            return false;
        }
        return this.locationTypeDtos.remove(Objects.requireNonNull(locationTypeDto, "locationTypeDto to removed is null"));
    }

    public LocationTypeDto getLocationTypeDtoByUUID(String uuid) {
        if (locationTypeDtos == null || uuid == null) {
            return null;
        }
        Optional<LocationTypeDto> locationTypeDtoOp = locationTypeDtos.stream().filter(l -> uuid.equals(l.getUUID())).findFirst();
        if (locationTypeDtoOp.isPresent()) {
            return locationTypeDtoOp.get();
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
        Optional<LocationDto> locationDtoOp = locationDtos.stream().filter(l -> uuid.equals(l.getUUID())).findFirst();
        if (locationDtoOp.isPresent()) {
            return locationDtoOp.get();
        }
        return null;
    }

    public LocationDto getLocationDtoById(long id) {
        if (locationDtos == null) {
            return null;
        }

        Optional<LocationDto> locationDtoOp = locationDtos.stream().filter(l -> l.getId() == id).findFirst();
        if (locationDtoOp.isPresent()) {
            return locationDtoOp.get();
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

    public boolean addLocationDto(LocationDto dto) {
        if (getLocationDtos() == null) {
            this.locationDtos = new LinkedHashSet<>();
        }

        return this.locationDtos.add(dto);
    }

    public Set<BlockDto> getBlockDtos() {
        return blockDtos;
    }

    public void setBlockDtos(Set<BlockDto> blockDtos) {
        this.blockDtos = blockDtos;
    }

    public boolean addBlockDto(BlockDto dto) {
        if (getBlockDtos() == null) {
            this.blockDtos = new LinkedHashSet<>();
        }

        return this.blockDtos.add(dto);
    }

    public boolean removeBlockDto(BlockDto blockDto) {
        if (this.blockDtos == null) {
            return false;
        }
        return this.blockDtos.remove(Objects.requireNonNull(blockDto, "blockDto to removed is null"));
    }

    public BlockDto getBlockDtoByUUID(String uuid) {
        if (blockDtos == null || uuid == null) {
            return null;
        }
        Optional<BlockDto> blockDtoOp = blockDtos.stream().filter(b -> uuid.equals(b.getUUID())).findFirst();
        if (blockDtoOp.isPresent()) {
            return blockDtoOp.get();
        }
        return null;
    }

    public BlockDto getBlockDtoById(long id) {
        if (blockDtos == null) {
            return null;
        }

        Optional<BlockDto> blockDtoOp = blockDtos.stream().filter(b -> b.getId() == id).findFirst();
        if (blockDtoOp.isPresent()) {
            return blockDtoOp.get();
        }
        return null;
    }

    public Set<StaticRouteDto> getStaticRouteDtos() {
        return staticRouteDtos;
    }

    public boolean removeStaticRouteDto(StaticRouteDto route) {
        if (this.staticRouteDtos == null) {
            return false;
        }
        return this.staticRouteDtos.remove(Objects.requireNonNull(route, "vehicleDto to removed is null"));
    }

    public StaticRouteDto getStaticRouteDtoByUUID(String uuid) {
        if (staticRouteDtos == null || uuid == null) {
            return null;
        }
        Optional<StaticRouteDto> routeDtoOp = staticRouteDtos.stream().filter(r -> uuid.equals(r.getUUID())).findFirst();
        if (routeDtoOp.isPresent()) {
            return routeDtoOp.get();
        }
        return null;
    }

    public StaticRouteDto getStaticRouteDtoById(long id) {
        if (staticRouteDtos == null) {
            return null;
        }

        Optional<StaticRouteDto> staticRouteDtoOp = staticRouteDtos.stream().filter(sr -> sr.getId() == id).findFirst();
        if (staticRouteDtoOp.isPresent()) {
            return staticRouteDtoOp.get();
        }
        return null;
    }

    public void setStaticRouteDtos(Set<StaticRouteDto> staticRouteDtos) {
        this.staticRouteDtos = staticRouteDtos;
    }

    public boolean addStaticRouteDto(StaticRouteDto dto) {
        if (getStaticRouteDtos() == null) {
            this.staticRouteDtos = new LinkedHashSet<>();
        }

        return this.staticRouteDtos.add(dto);
    }

    public Set<VehicleDto> getVehicleDtos() {
        return vehicleDtos;
    }

    public boolean removeVehicleDto(VehicleDto vehicleDto) {
        if (this.vehicleDtos == null) {
            return false;
        }
        return this.vehicleDtos.remove(Objects.requireNonNull(vehicleDto, "vehicleDto to removed is null"));
    }

    public VehicleDto getVehicleDtoByUUID(String uuid) {
        if (vehicleDtos == null || uuid == null) {
            return null;
        }
        Optional<VehicleDto> vehicleDtoOp = vehicleDtos.stream().filter(v -> uuid.equals(v.getUUID())).findFirst();
        if (vehicleDtoOp.isPresent()) {
            return vehicleDtoOp.get();
        }
        return null;
    }

    public VehicleDto getVehicleDtoById(long id) {
        if (vehicleDtos == null) {
            return null;
        }

        Optional<VehicleDto> vehicleDtoOp = vehicleDtos.stream().filter(v -> v.getId() == id).findFirst();
        if (vehicleDtoOp.isPresent()) {
            return vehicleDtoOp.get();
        }
        return null;
    }

    public void setVehicleDtos(Set<VehicleDto> vehicleDtos) {
        this.vehicleDtos = vehicleDtos;
    }

    public boolean addVehicleDto(VehicleDto dto) {
        if (getVehicleDtos() == null) {
            this.vehicleDtos = new LinkedHashSet<>();
        }

        return this.vehicleDtos.add(dto);
    }
}
