package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.EntityAuditDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import com.mj.tcs.data.model.Vehicle;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class VehicleDto extends EntityAuditDto {
    @DtoField
    private String name;

    @DtoField
    private int length;

    @DtoField
    private int energyLevel;

    @DtoField
    private int energyLevelCritical;

    @DtoField
    private int energyLevelGood;

    @DtoField
    private String rechargeOperation;

    @DtoField
    private int maxVelocity;

    @DtoField
    private int maxReverseVelocity;

    @DtoField(value = "state")
    private Vehicle.State state;

    @DtoField(value = "procState")
    private Vehicle.ProcState processState;

    @DtoField
    private int routeProgressIndex;

    private long currentPositionPointId;
    private long nextPositionPointId;

    @DtoField
    private int accumulatedDistance;

    @JsonProperty("precise_position")
    @DtoField(value = "precisePosition",
            dtoBeanKey = "TripleDto",
            entityBeanKeys = {"Triple"})
    private TripleDto tripleDto;

    @DtoField
    private double orientationAngle;

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

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public int getMaxReverseVelocity() {
        return maxReverseVelocity;
    }

    public void setMaxReverseVelocity(int maxReverseVelocity) {
        this.maxReverseVelocity = maxReverseVelocity;
    }

    public Vehicle.State getState() {
        return state;
    }

    public void setState(Vehicle.State state) {
        this.state = state;
    }

    public Vehicle.ProcState getProcessState() {
        return processState;
    }

    public void setProcessState(Vehicle.ProcState processState) {
        this.processState = processState;
    }

    public int getRouteProgressIndex() {
        return routeProgressIndex;
    }

    public void setRouteProgressIndex(int routeProgressIndex) {
        this.routeProgressIndex = routeProgressIndex;
    }

    public long getCurrentPositionPointId() {
        return currentPositionPointId;
    }

    public void setCurrentPositionPointId(long currentPositionPointId) {
        this.currentPositionPointId = currentPositionPointId;
    }

    public long getNextPositionPointId() {
        return nextPositionPointId;
    }

    public void setNextPositionPointId(long nextPositionPointId) {
        this.nextPositionPointId = nextPositionPointId;
    }

    public int getAccumulatedDistance() {
        return accumulatedDistance;
    }

    public void setAccumulatedDistance(int accumulatedDistance) {
        this.accumulatedDistance = accumulatedDistance;
    }

    public TripleDto getTripleDto() {
        return tripleDto;
    }

    public void setTripleDto(TripleDto tripleDto) {
        this.tripleDto = tripleDto;
    }

    public double getOrientationAngle() {
        return orientationAngle;
    }

    public void setOrientationAngle(double orientationAngle) {
        this.orientationAngle = orientationAngle;
    }
}
