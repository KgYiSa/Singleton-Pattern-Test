package com.mj.tcs.data.base;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@MappedSuperclass
public abstract class BaseResource extends BaseEntity implements Serializable, Cloneable {
    /**
     * A set of resources that <em>must</em> be acquired, too, when acquiring this
     * one.
     */
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_attached_resources")
    private Set<BaseResource> attachedResources = new HashSet<>();

    public Set<BaseResource> getAttachedResources() {
        return attachedResources;
    }

    public void setAttachedResources(Set<BaseResource> attachedResources) {
        this.attachedResources = attachedResources;
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
    public boolean attachResource(BaseResource newResource) {
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
    public boolean detachResource(BaseResource rmResource) {
        if (rmResource == null) {
            throw new NullPointerException("rmResource is null");
        }
        return attachedResources.remove(rmResource);
    }

    @Override
    public BaseResource clone() {
        BaseResource clone = (BaseResource) super.clone();
        clone.attachedResources = new HashSet<>(attachedResources);
        return clone;
    }
}
