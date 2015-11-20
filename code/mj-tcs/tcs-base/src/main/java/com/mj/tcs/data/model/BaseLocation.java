package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseResource;

/**
 * @author Wang Zhen
 */
public abstract class BaseLocation extends BaseResource implements Cloneable {
    /**
     * A reference to this location's type.
     */
    protected LocationType type;

    /**
     * Returns a reference to the type of this location.
     *
     * @return A reference to the type of this location.
     */
    public LocationType getType() {
        return type;
    }

    /**
     * Sets this location's type.
     *
     * @param newType This location's new type.
     */
    public void setType(LocationType newType) {
//      type = Objects.requireNonNull(newType, "newType is null");
        type = newType;
    }

    @Override
    public BaseLocation clone() {
        BaseLocation clone = null;
        clone = (BaseLocation) super.clone();
        clone.type = type.clone();
        return clone;
    }
}
