package com.mj.tcs.algorithms;

import com.mj.tcs.data.base.TCSResource;
import com.mj.tcs.data.order.Route;

import java.util.Map;
import java.util.Set;

/**
 * A <code>Scheduler</code> manages resources used by vehicles, preventing
 * both collisions and deadlocks.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface Scheduler {

    /**
     * Called by the dispatcher once it knows the new route of a vehicle.
     *
     * @param holder The resource holder for which the route is defined.
     * @param route The new/current route of the resource holder.
     */
    void setRoute(ResourceHolder holder, Route route);

    /**
     * Called by the vehicle controller whenever the vehicle has finished another
     * step on its current route.
     *
     * @param holder The resource holder for which the position is updated.
     * @param index The index of the holder's current position on its route.
     * @throws IllegalArgumentException If the index is not a valid index for the
     * vehicle's current route.
     */
    void setRouteIndex(ResourceHolder holder, int index);

    /**
     * Claims a set of resources for a vehicle.
     *
     * @param resourceUser The <code>ResourceHolder</code> claiming the resources.
     * @param resources The resources claimed.
     */
    void claim(ResourceHolder resourceUser, Set<TCSResource> resources);

    /**
     * Allocates a set of resources for a vehicle.
     *
     * @param resourceUser The <code>ResourceHolder</code> requesting the resources.
     * @param resources The resources requested.
     */
    void allocate(ResourceHolder resourceUser, Set<TCSResource> resources);

    /**
     * Informs the scheduler that a set of resources are to be allocated for the
     * given <code>ResourceHolder</code> <em>immediately</em>, i.e. without
     * blocking.
     * <p>
     * This method should only be called in urgent/emergency cases, for instance
     * if a vehicle has been moved to a different point manually, which has to be
     * reflected by resource allocation in the scheduler.
     * </p>
     * This method does not block, which means that it's safe to call it
     * synchronously.
     *
     * @param resourceUser The <code>ResourceHolder</code> requesting the resources.
     * @param resources The resources requested.
     * @throws ResourceAllocationException If it's impossible to allocate the
     * given set of resources for the given <code>ResourceHolder</code>.
     */
    void allocateNow(ResourceHolder resourceUser, Set<TCSResource> resources)
            throws ResourceAllocationException;

    /**
     * Releases a set of resources allocated by a vehicle.
     *
     * @param resourceUser The <code>ResourceHolder</code> releasing the resources.
     * @param resources The resources released. Any resources in the given set not
     * allocated by the given <code>ResourceHolder</code> are ignored.
     */
    void free(ResourceHolder resourceUser, Set<TCSResource> resources);

    /**
     * Unclaims a set of resources claimed by a vehicle.
     *
     * @param resourceUser The <code>ResourceHolder</code> unclaiming the resources.
     * @param resources The resources unclaimed.
     */
    void unclaim(ResourceHolder resourceUser, Set<TCSResource> resources);

    /**
     * Returns all resource allocations as a map of <code>ResourceHolder</code> IDs
     * to resources.
     *
     * @return All resource allocations as a map of <code>ResourceHolder</code> IDs
     * to resources.
     */
    Map<String, Set<TCSResource>> getAllocations();
}

