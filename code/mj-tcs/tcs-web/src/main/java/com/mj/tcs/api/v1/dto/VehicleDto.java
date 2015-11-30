package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;

import javax.persistence.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_vehicle")
//@Table(name = "tcs_model_vehicle", uniqueConstraints =
//    @UniqueConstraint(columnNames = {"name", "scene"})
//)
public class VehicleDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @DtoField
    @Column
    private int length;

    @DtoField
    @Column
    private int energyLevel;

    @DtoField
    @Column
    private int energyLevelCritical;

    @DtoField
    @Column
    private int energyLevelGood;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getEnergyLevelCritical() {
        return energyLevelCritical;
    }

    public void setEnergyLevelCritical(int energyLevelCritical) {
        this.energyLevelCritical = energyLevelCritical;
    }

    public int getEnergyLevelGood() {
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
