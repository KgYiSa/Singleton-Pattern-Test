/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tcs.data.base;

/**
 * A generic 3-tuple of long integer values, usable for 3D coordinates and
 * vectors.
 *
 * @author liumin
 * @author Wang Zhen
 */
public class Triple extends BaseEntity implements Cloneable {

    /**
     * The X coordinate.
     */
    private long x;
    /**
     * The Y coordinate.
     */
    private long y;
    /**
     * The Z coordinate.
     */
    private long z;

    /**
     * Creates a new Triple with all values set to 0.
     */
    public Triple() {
        this(0L, 0L, 0L);
    }

    /**
     * Creates a new Triple with the given values.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     */
    public Triple(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new Triple with values copied from the given one.
     *
     * @param original The Triple from which to copy the values.
     */
    public Triple(Triple original) {
        this(original.x, original.y, original.z);
    }

    /**
     * Returns the x coordinate.
     *
     * @return x
     */
    public long getX() {
        return x;
    }

    /**
     * Sets the new x coordinate.
     *
     * @param x The new x coordinate.
     */
    public void setX(long x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate.
     *
     * @return y
     */
    public long getY() {
        return y;
    }

    /**
     * Sets the new y coordinate.
     *
     * @param y The new y coordinate.
     */
    public void setY(long y) {
        this.y = y;
    }

    /**
     * Returns the z coordinate.
     *
     * @return z
     */
    public long getZ() {
        return z;
    }

    /**
     * Sets the new z coordinate.
     *
     * @param z The new z coordinate.
     */
    public void setZ(long z) {
        this.z = z;
    }

    @Override
    public Triple clone() {
        Triple clone = (Triple) super.clone();
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triple)) {
            return false;
        }
        Triple other = (Triple) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
