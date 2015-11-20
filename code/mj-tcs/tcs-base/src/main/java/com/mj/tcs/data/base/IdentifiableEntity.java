/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.data.base;

import java.io.Serializable;

/**
 * @author Wang Zhen
 */
public abstract class IdentifiableEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    protected Long id = null;

    public Long getId() {
        return id;
    }

    /**
     * internally use only (as a Java Bean)
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Clear all IDs associated with the object.
     */
    public void clearId() {
        setId(null);
    }

    @Override
    public IdentifiableEntity clone() throws IllegalStateException {
        IdentifiableEntity clone = null;
        try {
            clone = (IdentifiableEntity) super.clone();
            clone.setId(getId());
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("IdentifiableEntity clone: Unexpected exception", e);
        }
        return clone;
    }
}
