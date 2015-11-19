package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class DestinationDto extends BaseEntityAuditDto {
    @JsonProperty(value = "location")
    @DtoField(value = "location",
        dtoBeanKey = "LocationDto",
        entityBeanKeys = {"Location"})
    private GenericLocationDto locationDto;

    @DtoField
    private String operation;

    public GenericLocationDto getLocationDto() {
        return locationDto;
    }

    public void setLocationDto(GenericLocationDto locationDto) {
        this.locationDto = locationDto;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
