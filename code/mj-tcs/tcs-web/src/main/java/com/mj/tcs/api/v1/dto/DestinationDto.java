package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_order_transport_drive_order_destination")
//@Table(name = "tcs_order_transport_drive_order_destination")
public class DestinationDto extends BaseEntityDto {

    @Column
    private boolean dummy;

    @Column
    private String locationUUID; // Location if dummy == false, Point otherwise

    @DtoField
    @Column
    private String operation;

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
}
