/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tcs.data.base;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author liumin
 * @author Wang Zhen
 */
public abstract class BaseEntity extends IdentifiableEntity
        implements Serializable, Cloneable {
    /**
     * A <code>Comparator</code> for ordering <code>TCSObject</code>s ascendingly
     * by their IDs.
     */
    public static final Comparator<BaseEntity> idComparator =
            new IDComparator();
    /**
     * A <code>Comparator</code> for ordering <code>TCSObject</code>s ascendingly
     * by their names.
     */
    public static final Comparator<BaseEntity> nameComparator =
            new NameComparator();

    private EntityAuditor auditor = null;

    private Long version;

    protected String name; // Because some entity have different column format !!!

    private Map<String, String> properties = new TreeMap<>();

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityAuditor getAuditor() {
        return auditor;
    }

    public void setAuditor(EntityAuditor auditor) {
        this.auditor = auditor;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Sets a property for this object.
     *
     * @param key The new property's key.
     * @param value The new property's value. If <code>null</code>, removes the
     * property from this object.
     */
    public void setProperty(String key, String value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (value == null) {
            properties.remove(key);
        }
        else {
            properties.put(key, value);
        }
    }

    /**
     * Clears all of this entity's properties.
     */
    public void clearProperties() {
        properties.clear();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.getId(), ((BaseEntity) object).getId());
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [ID=" + id + "]";
    }

    @Override
    public BaseEntity clone() throws IllegalStateException {
        BaseEntity clone = null;
        clone = (BaseEntity) super.clone();
        clone.setName(getName());
        if (auditor != null) {
            clone.setAuditor(auditor.clone());
        }
        clone.properties = new HashMap<>(properties);
        return clone;
    }

    // Private classes start here.
    /**
     * A <code>Comparator</code> for ordering <code>TCSObject</code>s ascendingly
     * by their IDs.
     */
    private static final class IDComparator
            implements Comparator<BaseEntity> {

        /**
         * Creates a new IDComparator.
         */
        private IDComparator() {
        }

        @Override
        public int compare(BaseEntity o1, BaseEntity o2) {
            return (int)(o1.getId() - o2.getId());
        }
    }

    /**
     * A <code>Comparator</code> for ordering <code>TCSObject</code>s ascendingly
     * by their names.
     */
    private static final class NameComparator
            implements Comparator<BaseEntity> {

        /**
         * Creates a new NameComparator.
         */
        private NameComparator() {
        }

        @Override
        public int compare(BaseEntity o1, BaseEntity o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
