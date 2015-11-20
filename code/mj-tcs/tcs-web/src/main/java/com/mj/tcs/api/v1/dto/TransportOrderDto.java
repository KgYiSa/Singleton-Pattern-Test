/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity
@Table(name = "tcs_order_transport_order")
public class TransportOrderDto extends BaseEntityDto {

    @Column(name = "scene", nullable = false)
    private SceneDto scene;

    @DtoField
    @Column
    private String name;

    @JsonProperty(value = "destinations")
    @DtoField(value = "destination",
            dtoBeanKey = "DestinationDto",
            entityBeanKeys = "Destination")
    @OneToMany
    private List<DestinationDto> destinations;

    /**
     * The point of time at which this TransportOrder must have been finished.
     */
    @Column
    private long deadline = Long.MAX_VALUE;

    /**
     * The point of time at which this transport order was finished.
     */
    @Column
    private long finishedTime = Long.MIN_VALUE;

    /**
     * A reference to the vehicle that is intended to process this transport
     * order. If this order is free to be processed by any vehicle, this is
     * <code>null</code>.
     */
    @JsonProperty(value = "intended_vehicle")
    @Column
    private long intendedVehicle;

    /**
     * Creates a new TransportOrder.
     */
    public TransportOrderDto() {
    }

    public SceneDto getScene() {
        return scene;
    }

    public void setScene(SceneDto scene) {
        this.scene = scene;
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

    /**
     * Returns this transport order's deadline. If the value of transport order's
     * deadline was not changed, the initial value <code>Long.MAX_VALUE</code>
     * is returned.
     *
     * @return This transport order's deadline or the initial deadline value.
     * <code>Long.MAX_VALUE</code>, if the deadline was not changed.
     */
    public long getDeadline() {
        return deadline;
    }

    /**
     * Sets this transport order's deadline.
     *
     * @param newDeadline This transport order's new deadline.
     */
    public void setDeadline(long newDeadline) {
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
    public long getFinishedTime() {
        return finishedTime;
    }

    /**
     * Returns a reference to the vehicle that is intended to process this
     * transport order.
     *
     * @return A reference to the vehicle that is intended to process this
     * transport order. If this order is free to be processed by any vehicle,
     * <code>null</code> is returned.
     */
    public long getIntendedVehicle() {
        return intendedVehicle;
    }

    /**
     * Sets a reference to the vehicle that is intended to process this transport
     * order.
     *
     * @param vehicle The ID to the vehicle intended to process this order,
     *                  If the value <= 0, then use auto-selection.
     */
    public void setIntendedVehicle(long vehicle) {
        intendedVehicle = vehicle;
    }

}
