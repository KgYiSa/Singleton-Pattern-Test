/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tcs.data.base;

import com.mj.tcs.util.UniqueTimestampGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author Wang Zhen
 */
@Embeddable
public class EntityAuditor implements Cloneable {
    /**
     * The timestamp generator for order creation times.
     */
    @Transient
    private static final UniqueTimestampGenerator timestampGenerator =
            new UniqueTimestampGenerator();

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by", length = 20)
    private String createdBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    public EntityAuditor() {}

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Sets createdAt before insert
     */
    @PrePersist
    public void setCreationDate() {
        this.createdAt = timestampGenerator.getNextDateTime();
    }

    /**
     * Sets updatedAt before update
     */
    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = timestampGenerator.getNextDateTime();
    }

    @Override
    public EntityAuditor clone() throws IllegalStateException {
        EntityAuditor clone = null;
        try {
            clone = (EntityAuditor) super.clone();
            clone.setCreatedAt(getCreatedAt());
            clone.setCreatedBy(getCreatedBy());
            clone.setUpdatedAt(getUpdatedAt());
            clone.setUpdatedBy(getUpdatedBy());
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("EntityAuditor clone: Unexpected exception", e);
        }
        return clone;
    }
}
