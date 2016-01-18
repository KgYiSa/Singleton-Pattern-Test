package com.mj.tcs.data.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Instances of this class represent resources that vehicles may claim for
 * exclusive usage.
 *
 * @author Stefan Walter (Fraunhofer IML)
 * @param <E> The actual resource class.
 */
public class TCSResource<E extends TCSResource<E>>
        extends TCSObject<E>
        implements Serializable, Cloneable {
    /**
     * A set of resources that <em>must</em> be acquired, too, when acquiring this
     * one.
     */
    private Set<TCSResourceReference<?>> attachedResources = new HashSet<>();

    /**
     * Creates a new TCSResource.
     *
     * @param objectUUID The new resource's object UUID.
     * @param name The new resource's name.
     */
    protected TCSResource(String objectUUID, String name) {
        super(objectUUID, name);
        reference = new TCSResourceReference<>(this);
    }

    // Methods inherited from TCSObject<E> start here.

    @Override
    public TCSResourceReference<E> getReference() {
        return (TCSResourceReference<E>) reference;
    }

    // Methods not declared in any interface start here.

    /**
     * Returns the set of resources that <em>must</em> be acquired, too, when
     * acquiring this one.
     *
     * @return The set of resources that must be acquired along with this one.
     */
    public Set<TCSResourceReference<?>> getAttachedResources() {
        return new HashSet<>(attachedResources);
    }

    /**
     * Attaches a resource to this one, stating that the referenced resource must
     * always be acquired, too, when this one is acquired.
     *
     * @param newResource The reference to a resource that must be acquired along
     * with this one.
     * @return <code>true</code> if the given resource was not already attached to
     * this one.
     */
    public boolean attachResource(TCSResourceReference<?> newResource) {
        if (newResource == null) {
            throw new NullPointerException("newResource is null");
        }
        return attachedResources.add(newResource);
    }

    /**
     * Detaches a resource from this one, stating that the referenced resource
     * does no longer have to be acquired, too, when this one is acquired.
     *
     * @param rmResource The reference to the resource that no longer has to be
     * acquired along with this one.
     * @return <code>true</code> if the given resource was attached to this one.
     */
    public boolean detachResource(TCSResourceReference<?> rmResource) {
        if (rmResource == null) {
            throw new NullPointerException("rmResource is null");
        }
        return attachedResources.remove(rmResource);
    }

    @Override
    public TCSResource<E> clone() {
        TCSResource<E> clone = (TCSResource<E>) super.clone();
        clone.attachedResources = new HashSet<>(attachedResources);
        return clone;
    }
}
