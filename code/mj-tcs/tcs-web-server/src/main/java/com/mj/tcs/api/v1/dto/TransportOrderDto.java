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
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.util.UniqueTimestampGenerator;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class TransportOrderDto extends BaseEntityAuditDto {
    /**
     * The timestamp generator for order creation times.
     */
    private static final UniqueTimestampGenerator timestampGenerator =
            new UniqueTimestampGenerator();

    @DtoField
    private String name;

    @JsonProperty(value = "destination")
    @DtoField(value = "destination",
            dtoBeanKey = "DestinationDto",
            entityBeanKeys = "Destination")
    private DestinationDto destinationDto;
    /**
     * The point of time at which this TransportOrder was created.
     */
    private long creationTime;
    /**
     * The point of time at which this TransportOrder must have been finished.
     */
    private long deadline = Long.MAX_VALUE;
    /**
     * The point of time at which this transport order was finished.
     */
    private long finishedTime = Long.MIN_VALUE;
    /**
     * A reference to the vehicle that is intended to process this transport
     * order. If this order is free to be processed by any vehicle, this is
     * <code>null</code>.
     */
    @JsonProperty(value = "intended_vehicle_id")
    private long intendedVehicleId;

    /**
     * Whether this order is dispensable (may be withdrawn automatically).
     */
    private boolean dispensable;


    /**
     * Creates a new TransportOrder.
     */
    public TransportOrderDto() {
        creationTime = timestampGenerator.getNextTimestamp();
    }

    public String getName() {
        return name;
    }

    public void setName(String inName) {
        this.name = inName;
    }

    public DestinationDto getDestinationDto() {
        return destinationDto;
    }

    public void setDestinationDto(DestinationDto destinationDto) {
        this.destinationDto = destinationDto;
    }

    /**
     * Returns this transport order's creation time.
     *
     * @return This transport order's creation time.
     */
    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
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
    public long getIntendedVehicleId() {
        return intendedVehicleId;
    }

    /**
     * Sets a reference to the vehicle that is intended to process this transport
     * order.
     *
     * @param vehicleId The ID to the vehicle intended to process this order,
     *                  If the value <= 0, then use auto-selection.
     */
    public void setIntendedVehicleId(long vehicleId) {
        intendedVehicleId = vehicleId;
    }

    /**
     * Checks if this order is dispensable.
     *
     * @return <code>true</code> if, and only if, this order is dispensable.
     */
    public boolean isDispensable() {
        return dispensable;
    }

    /**
     * Sets this order's <em>dispensable</em> flag.
     *
     * @param dispensable This order's new <em>dispensable</em> flag.
     */
    public void setDispensable(boolean dispensable) {
        this.dispensable = dispensable;
    }
}
