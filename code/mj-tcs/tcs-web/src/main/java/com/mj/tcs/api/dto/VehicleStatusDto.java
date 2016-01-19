package com.mj.tcs.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class VehicleStatusDto {
    private String type = "vehicleStatusMessage";
    private String uuid;
    
    private String positionUUID;
    private String processingState;
    private String state;

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        Objects.requireNonNull(uuid);

        this.uuid = uuid;
    }

    public String getPositionUUID() {
        return positionUUID;
    }

    public void setPosition(String positionUUID) {
        this.positionUUID = positionUUID;
    }

    public String getProcessingState() {
        return processingState;
    }

    public void setProcessingState(String processingState) {
        this.processingState = processingState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }
}
