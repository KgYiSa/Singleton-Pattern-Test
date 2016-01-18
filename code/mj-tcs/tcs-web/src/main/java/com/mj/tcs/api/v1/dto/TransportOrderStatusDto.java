//package com.mj.tcs.api.v1.dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
//
//import javax.persistence.*;
//import java.util.List;
//
///**
// * @author Wang Zhen
// */
//@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
//@Entity(name = "tcs_order_transport_order_status")
//public class TransportOrderStatusDto extends BaseEntityDto {
//    @Transient
//    private String type = "TOrderStatusMessage";
//
//    @JsonIgnore
//    @Column(name = "scene", nullable = false)
//    private Long sceneId;
//
//    @Column(name = "executing_vehicle_uuid", nullable = false)
//    private String executingVehicle;
//
//    @Column(nullable = false)
//    private String orderState;
//
//    @JsonProperty("destinations")
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<DestinationStatusDto> destinationDtos;
//
//    @Override
//    public void setUUID(String uuid) {
//        super.setUUID(uuid);
//    }
//
//    @Override
//    public String getUUID() {
//        return super.getUUID();
//    }
//
//    public Long getSceneId() {
//        return sceneId;
//    }
//
//    public void setSceneId(Long sceneId) {
//        this.sceneId = sceneId;
//    }
//
//    public String getExecutingVehicle() {
//        return executingVehicle;
//    }
//
//    public void setExecutingVehicle(String executingVehicle) {
//        this.executingVehicle = executingVehicle;
//    }
//
//    public String getOrderState() {
//        return orderState;
//    }
//
//    public void setOrderState(String orderState) {
//        this.orderState = orderState;
//    }
//
//    public List<DestinationStatusDto> getDestinationDtos() {
//        return destinationDtos;
//    }
//
//    public void setDestinationDtos(List<DestinationStatusDto> destinationDtos) {
//        this.destinationDtos = destinationDtos;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
//    @Entity(name = "tcs_order_transport_order_status_dest")
//    public static class DestinationStatusDto {
//        @Column
//        private String locationUUID;
//        @Column
//        private String operation;
//        @Column
//        private String state;
//
//        public String getLocationUUID() {
//            return locationUUID;
//        }
//
//        public void setLocationUUID(String locationUUID) {
//            this.locationUUID = locationUUID;
//        }
//
//        public String getOperation() {
//            return operation;
//        }
//
//        public void setOperation(String operation) {
//            this.operation = operation;
//        }
//
//        public String getState() {
//            return state;
//        }
//
//        public void setState(String state) {
//            this.state = state;
//        }
//    }
//}
