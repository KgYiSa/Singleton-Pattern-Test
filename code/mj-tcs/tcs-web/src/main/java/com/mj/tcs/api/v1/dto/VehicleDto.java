package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;
import com.mj.tcs.api.v1.dto.base.TripleDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@UUID")
@Dto
@Entity(name = "tcs_model_vehicle")
//@Table(name = "tcs_model_vehicle", uniqueConstraints =
//    @UniqueConstraint(columnNames = {"name", "scene"})
//)
public class VehicleDto extends BaseEntityDto {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_vehicle_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new HashSet<>();

    @DtoField
    @Column
    private double length;

    @DtoField
    @Column
    private double energyLevel;

    @DtoField
    @Column
    private double energyLevelCritical;

    @DtoField
    @Column
    private double energyLevelGood;

    @DtoField
    @Column
    private String rechargeOperation;

    @DtoField
    @Column
    private double maxVelocity;

    @DtoField
    @Column
    private double maxReverseVelocity;

    @Column
    private PointDto currentPosition;

    @JsonProperty("precise_position")
    @DtoField(value = "precisePosition",
            dtoBeanKey = "TripleDto",
            entityBeanKeys = {"Triple"})
    @OneToOne
    private TripleDto precisePosition;

    @DtoField
    @Column
    private double orientationAngle;

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

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(double energyLevel) {
        this.energyLevel = energyLevel;
    }

    public double getEnergyLevelCritical() {
        return energyLevelCritical;
    }

    public void setEnergyLevelCritical(int energyLevelCritical) {
        this.energyLevelCritical = energyLevelCritical;
    }

    public double getEnergyLevelGood() {
        return energyLevelGood;
    }

    public void setEnergyLevelGood(int energyLevelGood) {
        this.energyLevelGood = energyLevelGood;
    }

    public String getRechargeOperation() {
        return rechargeOperation;
    }

    public void setRechargeOperation(String rechargeOperation) {
        this.rechargeOperation = rechargeOperation;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getMaxReverseVelocity() {
        return maxReverseVelocity;
    }

    public void setMaxReverseVelocity(int maxReverseVelocity) {
        this.maxReverseVelocity = maxReverseVelocity;
    }

    public PointDto getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(PointDto currentPosition) {
        this.currentPosition = currentPosition;
    }

    public TripleDto getPrecisePosition() {
        return precisePosition;
    }

    public void setPrecisePosition(TripleDto precisePosition) {
        this.precisePosition = precisePosition;
    }

    public double getOrientationAngle() {
        return orientationAngle;
    }

    public void setOrientationAngle(double orientationAngle) {
        this.orientationAngle = orientationAngle;
    }
}
