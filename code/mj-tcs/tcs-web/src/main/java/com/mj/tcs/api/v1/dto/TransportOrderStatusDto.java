package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TransportOrderStatusDto {
    private String type = "TOrderStatusMessage";
    private String uuid;
    
    private String executingVehicle;
    private String orderState;
    @JsonProperty("destinations")
    private List<DestinationStatusDto> destinationDtos;

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getExecutingVehicle() {
        return executingVehicle;
    }

    public void setExecutingVehicle(String executingVehicle) {
        this.executingVehicle = executingVehicle;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public List<DestinationStatusDto> getDestinationDtos() {
        return destinationDtos;
    }

    public void setDestinationDtos(List<DestinationStatusDto> destinationDtos) {
        this.destinationDtos = destinationDtos;
    }

    public String getType() {
        return type;
    }

    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    public static class DestinationStatusDto {
        private String locationUUID;
        private String operation;
        private String state;

        public String getLocationUUID() {
            return locationUUID;
        }

        public void setLocationUUID(String locationUUID) {
            this.locationUUID = locationUUID;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
