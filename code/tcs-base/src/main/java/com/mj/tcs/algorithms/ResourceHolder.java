package com.mj.tcs.algorithms;

import com.mj.tcs.data.base.BaseResource;

import java.util.Set;

/**
 * Defines callback methods for clients of the resource scheduler.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface ResourceHolder {
    /**
     * Returns an ID string for this <code>ResourceHolder</code>.
     * The returned string should be unique among all <code>ResourceHolder</code>s
     * in the system. This method may never return <code>null</code>.
     *
     * @return An ID string for this <code>ResourceHolder</code>.
     */
    String getId();
    /**
     * Called when resources have been reserved for this
     * <code>ResourceHolder</code>.
     *
     * @param resources The resources reserved.
     * @return <code>true</code> if, and only if, this <code>ResourceHolder</code>
     * accepts the resources allocated. A return value of <code>false</code>
     * indicates this <code>ResourceHolder</code> does not need the given resources
     * (any more), freeing them implicitly.
     */
    boolean allocationSuccessful(Set<BaseResource> resources);

    /**
     * Called if it was impossible to allocate resources for this
     * <code>ResourceHolder</code>.
     *
     * @param resources The resources which could not be reserved.
     */
    void allocationFailed(Set<BaseResource> resources);
}
