/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_order_transport_order")
//@Table(name = "tcs_order_transport_order")
public class TransportOrderDto extends BaseEntityDto {
    @JsonIgnore
    @Column(name = "scene", nullable = false)
    private Long sceneId;

    @JsonView(View.Order.class)
    @DtoField
    @Column
    private String name;

    @JsonView(View.Default.class)
    @JsonProperty(value = "destinations")
    @DtoField(value = "destination",
            dtoBeanKey = "DestinationDto",
            entityBeanKeys = "Destination")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DestinationDto> destinations = new ArrayList<>();

    /**
     * The point of time at which this TransportOrder must have been finished.
     */
    @JsonView(View.Order.class)
    @Column
    private Long deadline = Long.MAX_VALUE;

    /**
     * The point of time at which this transport order was finished.
     */
    @JsonView(View.Order.class)
    @Column
    private Long finishedTime = Long.MIN_VALUE;

    /**
     * A reference to the vehicle that is intended to process this transport
     * order. If this order is free to be processed by any vehicle, this is
     * <code>null</code>.
     */
    @JsonView(View.Order.class)
    @JsonProperty(value = "intended_vehicle_uuid")
    @Column
    private String intendedVehicleUUID;

    @JsonView(View.Order.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tcs_order_transport_order_deps")
    private List<String> deps;

    @JsonView(View.Status.class)
    @Column(name = "executing_vehicle_uuid")
    private String executingVehicle;

    @JsonView(View.Status.class)
    @Column
    private String orderState;

    /**
     * Creates a new TransportOrder.
     */
    public TransportOrderDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String inName) {
        this.name = inName;
    }

    public List<DestinationDto> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<DestinationDto> destinations) {
        this.destinations = Objects.requireNonNull(destinations, "destinations");
    }

    public void addDestionation(DestinationDto destinationDto) {
        if (this.destinations == null) {
            this.destinations = new ArrayList<>();
        }
        this.destinations.add(Objects.requireNonNull(destinationDto, "destinationDto"));
    }

    public void removeDestionation(DestinationDto destinationDto) {
        if (this.destinations == null) {
            return;
        }
        this.destinations.remove(Objects.requireNonNull(destinationDto, "destinationDto"));
    }

    /**
     * Returns this transport order's deadline. If the value of transport order's
     * deadline was not changed, the initial value <code>Long.MAX_VALUE</code>
     * is returned.
     *
     * @return This transport order's deadline or the initial deadline value.
     * <code>Long.MAX_VALUE</code>, if the deadline was not changed.
     */
    public Long getDeadline() {
        return deadline;
    }

    /**
     * Sets this transport order's deadline.
     *
     * @param newDeadline This transport order's new deadline.
     */
    public void setDeadline(Long newDeadline) {
        deadline = newDeadline;
    }

    /**
     * Returns the point of time at which this transport order was finished.
     * If the transport order has not been finished, yet,
     * <code>Long.MIN_VALUE</code> is returned.
     *
     * @return The point of time at which this transport order was finished, or
     * <code>Long.MIN_VALUE</code>, if the transport order has not been finished,
     * yet.
     */
    public Long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Long finishedTime) {
        this.finishedTime = finishedTime;
    }

    /**
     * Returns The UUID to the vehicle that is intended to process this
     * transport order.
     *
     * @return The UUID to the vehicle that is intended to process this
     * transport order. If this order is free to be processed by any vehicle,
     * <code>null</code> is returned.
     */
    public String getIntendedVehicleUUID() {
        return intendedVehicleUUID;
    }

    /**
     * Sets a UUID to the vehicle that is intended to process this transport
     * order.
     *
     * @param vehicle The UUID to the vehicle intended to process this order,
     *                  If the value == null, then use auto-selection.
     */
    public void setIntendedVehicleUUID(String vehicle) {
        intendedVehicleUUID = vehicle;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public List<String> getDeps() {
        return deps;
    }

    public void setDeps(List<String> deps) {
        this.deps = deps;
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

    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    @Dto
    @Entity(name = "tcs_order_transport_drive_order_destination")
//@Table(name = "tcs_order_transport_drive_order_destination")
    public static class DestinationDto extends BaseEntityDto {

        @JsonView(View.Default.class)
        @Column
        private boolean dummy;

        @JsonView(View.Default.class)
        @Column
        private String locationUUID; // Location if dummy == false, Point otherwise

        @JsonView(View.Default.class)
        @Column
        private String operation;

        @JsonView(View.Status.class)
        @Column
        private String state;

        public DestinationDto() {}

        public DestinationDto(String locationUUID, String operation, boolean dummy) {
            this.locationUUID = locationUUID;
            this.operation = operation;
            this.dummy = dummy;
        }

        public boolean isDummy() {
            return dummy;
        }

        public void setDummy(boolean dummy) {
            this.dummy = dummy;
        }

        public void setLocationUUID(String locationUUID) {
            this.locationUUID = locationUUID;
        }

        public String getLocationUUID() {
            return locationUUID;
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

    public static class View {
        public interface Default {}
        public interface Order extends Default {}
        public interface Status extends Default {}
    }
}
