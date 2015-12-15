package com.mj.tcs.data.base;

import java.io.Serializable;

/**
 * A transient reference to a <code>TCSResource</code>.
 *
 * @author Stefan Walter (Fraunhofer IML)
 * @param <E> The actual resource class.
 */
public class TCSResourceReference<E extends TCSResource<E>>
        extends TCSObjectReference<E>
        implements Serializable, Cloneable {
    /**
     * Creates a new TCSResourceReference.
     *
     * @param newReferent The resource this reference references.
     */
    protected TCSResourceReference(TCSResource<E> newReferent) {
        super(newReferent);
    }

    @Override
    public TCSResourceReference<E> clone() {
        TCSResourceReference<E> clone = (TCSResourceReference<E>) super.clone();
        return clone;
    }
}
