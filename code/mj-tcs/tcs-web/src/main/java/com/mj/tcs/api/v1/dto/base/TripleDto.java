package com.mj.tcs.api.v1.dto.base;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class TripleDto extends BaseEntityAuditDto {
    /**
     * The X coordinate.
     */
    @DtoField
    private long x;
    /**
     * The Y coordinate.
     */
    @DtoField
    private long y;
    /**
     * The Z coordinate.
     */
    @DtoField
    private long z;

    /**
     * Creates a new TripleDto with all values set to 0.
     */
    public TripleDto() {
        this(0L, 0L, 0L);
    }

    /**
     * Creates a new TripleDto with the given values.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     */
    public TripleDto(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }
}
