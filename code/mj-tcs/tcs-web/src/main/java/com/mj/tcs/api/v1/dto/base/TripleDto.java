package com.mj.tcs.api.v1.dto.base;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_triple")
//@Table(name = "tcs_model_triple")
public class TripleDto extends BaseEntityDto {
    /**
     * The X coordinate.
     */
    @DtoField
    @Column(nullable = false)
    private long x;
    /**
     * The Y coordinate.
     */
    @DtoField
    @Column(nullable = false)
    private long y;
    /**
     * The Z coordinate.
     */
    @DtoField
    @Column(nullable = false)
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
