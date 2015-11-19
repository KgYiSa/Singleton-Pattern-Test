package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import com.mj.tcs.data.model.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class PointDto extends BaseEntityAuditDto {
    @DtoField
    private String name;

    @JsonProperty("position")
    @DtoField(value = "position",
                dtoBeanKey = "TripleDto",
                entityBeanKeys = {"Triple"})
    private TripleDto tripleDto;

    @DtoField()
    private double vehicleOrientationAngle = 0d/*Double.NaN*/;//Can NOT be NaN, will cause issue: java.sql.SQLException: 'NaN' is not a valid numeric or approximate numeric value

    @DtoField
    private Point.Type type = Point.Type.HALT_POSITION;

    // convert outside
    @JsonProperty("incomingPaths")
    private Set<Long> incomingPathIds = new HashSet<>();

    // convert outside
    @JsonProperty("outgoingPaths")
    private Set<Long> outgoingPathIds = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TripleDto getTripleDto() {
        return tripleDto;
    }

    public void setTripleDto(TripleDto tripleDto) {
        this.tripleDto = tripleDto;
    }

    public double getVehicleOrientationAngle() {
        return vehicleOrientationAngle;
    }

    public void setVehicleOrientationAngle(double vehicleOrientationAngle) {
        this.vehicleOrientationAngle = vehicleOrientationAngle;
    }

    public Point.Type getType() {
        return type;
    }

    public void setType(Point.Type type) {
        this.type = type;
    }

    public Set<Long> getIncomingPathIds() {
        return incomingPathIds;
    }

    public void setIncomingPathIds(Set<Long> incomingPathIds) {
        this.incomingPathIds = incomingPathIds;
    }

    public void addIncomingPathId(long incomingPathId) {
        this.incomingPathIds.add(incomingPathId);
    }

    public Set<Long> getOutgoingPathIds() {
        return outgoingPathIds;
    }

    public void setOutgoingPathIds(Set<Long> outgoingPathIds) {
        this.outgoingPathIds = outgoingPathIds;
    }

    public void addOutgoingPathId(long outgoingPathId) {
        this.outgoingPathIds.add(outgoingPathId);
    }
}
