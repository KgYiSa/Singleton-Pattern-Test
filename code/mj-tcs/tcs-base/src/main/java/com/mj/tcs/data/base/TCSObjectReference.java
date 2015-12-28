/*
 * MJTCS copyright information:
 * Copyright (c) 2005 Fraunhofer IML
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package com.mj.tcs.data.base;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Instances of this class provide transient references to business objects.
 * They can be used to prevent serialization of whole object graphs but still
 * keep a reference to the actual object (i.e. its ID and name, which both are
 * unique in a model).
 *
 * @param <E> The actual object class.
 * @author Stefan Walter (Fraunhofer IML)
 */
public class TCSObjectReference<E extends TCSObject<E>>
        implements Serializable, Cloneable {

    /**
     * This class's Logger.
     */
    private static final Logger log =
            Logger.getLogger(TCSObjectReference.class.getName());

    /**
     * The referenced object's UUID.
     */
    private final String uuid;

    private final boolean dummy;
    /**
     * The referenced object's name.
     */
    private String name;

    /**
     * The referenced object's class.
     */
    private final Class<?> referentClass;

    /**
     * Creates a new TCSObjectReference.
     *
     * @param newReferent The object this reference references.
     */
    protected TCSObjectReference(TCSObject<E> newReferent) {
        log.finer("method entry");
        Objects.requireNonNull(newReferent);
        this.uuid = newReferent.getUUID();
        this.name = newReferent.getName();
        referentClass = newReferent.getClass();
        this.dummy = false;
    }

    /**
     * Creates a dummy reference, referencing nothing.
     *
     * @param clazz The class of the object being referenced.
     * @param uuid The new reference's uuid.
     */
    private TCSObjectReference(Class<?> clazz, String uuid) {
        log.finer("method entry");
        this.uuid = Objects.requireNonNull(uuid, "uuid is null");
        referentClass = Objects.requireNonNull(clazz, "clazz is null");
        this.dummy = true;
    }

    /**
     * Returns the referenced object's UUID.
     *
     * @return The referenced object's UUID.
     */
    public final String getUUID() {
        log.finer("method entry");
        return uuid;
    }

    /**
     * Returns the referenced object's name.
     *
     * @return The referenced object's name.
     */
    public final String getName() {
        log.finer("method entry");
        return name;
    }

    /**
     * Sets the referenced object's name.
     * Note that this method can be used to change the name stored inside this
     * reference to the referenced object's actual name - it cannot be used to
     * change the name of the referenced object.
     *
     * @param newName The referenced object's new name.
     */
    public final void setName(String newName) {
        log.finer("method entry");
        name = Objects.requireNonNull(newName, "newName is null");
    }

    public Class<?> getReferentClass() {
        return referentClass;
    }

    /**
     * Returns <code>false</code> if this reference really references an object,
     * or <code>true</code> if it's a dummy reference without a real object.
     *
     * @return <code>false</code> if this reference really references an object,
     * or <code>true</code> if it's a dummy reference without a real object.
     */
    public boolean isDummy() {
        return dummy;
    }

    /**
     * Indicates whether a TCSObjectReference is equal to another one.
     * Two TCSObjectReferences are equal if
     * <ul>
     * <li>the IDs of the TCSObjects they refer to are equal and
     * <li>the classes of the TCSObjects they refer to are equal.
     * </ul>
     *
     * @param otherObj The object to check for equality.
     * @return <code>true</code> if <code>otherObj</code> is not
     * <code>null</code>, is a TCSObjectReference, too, and both its ID and
     * the implementing class of TCSObject refers to are equal, else
     * <code>false</code>.
     */
    @Override
    public boolean equals(Object otherObj) {
        log.finer("method entry");
        if (otherObj instanceof TCSObjectReference) {
            TCSObjectReference otherRef = (TCSObjectReference) otherObj;
            return uuid.equals(otherRef.getUUID());
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this TCSObjectReference.
     * The hash code for a TCSObjectReference is computed as the exclusive
     * OR (XOR) of the hash codes of the ID and the class name of the TCSObject
     * the reference refers to.
     *
     * @return A hash code for this TCSObjectReference.
     */
    @Override
    public int hashCode() {
        log.finer("method entry");
        return getUUID().hashCode();
    }

    @Override
    public String toString() {
        return "[" + getUUID() + "]:" + name;
    }

    /**
     * Returns a distinct copy of this reference.
     * The clone's <code>referent</code> attribute is set to <code>null</code> to
     * prevent the actual referenced object to be leaked outside the kernel.
     *
     * @return A distinct copy of this reference, with its <code>referent</code>
     * attribute set to <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    @Override
    public TCSObjectReference<E> clone() {
        log.finer("method entry");
        TCSObjectReference<E> clone = null;
        try {
            clone = (TCSObjectReference<E>) super.clone();
        } catch (CloneNotSupportedException exc) {
            throw new RuntimeException("Unexpected exception", exc);
        }
        return clone;
    }

    /**
     * Returns a dummy reference, referencing nothing.
     *
     * @param <T>  The type of the dummy reference to be returned.
     * @param uuid
     * @return A dummy reference, referencing nothing.
     */
    public static <T extends TCSObject<T>> TCSObjectReference<T>
    getDummyReference(Class<?> clazz, String uuid) {
        TCSObjectReference<T> answer = new TCSObjectReference<>(clazz, uuid);
        // TODO: Optimize
        answer.setName("");
        return answer;
    }
}
