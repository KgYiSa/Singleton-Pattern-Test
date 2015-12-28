package com.mj.tcs.data.model;


import com.mj.tcs.data.base.TCSObject;
import com.mj.tcs.data.base.TCSObjectReference;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An aggregation of model elements.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public final class Group
        extends TCSObject<Group>
        implements Serializable, Cloneable {

    /**
     * The model elements aggregated in this group.
     */
    private Set<TCSObjectReference<?>> members = new HashSet<>();

    /**
     * Creates a new, empty group.
     *
     * @param objectUUID This group's UUID.
     * @param name This group's name.
     */
    public Group(String objectUUID, String name) {
        super(objectUUID, name);
    }

    /**
     * Returns a set of all members of this group.
     *
     * @return A set of all members of this group.
     */
    public Set<TCSObjectReference<?>> getMembers() {
        return new HashSet<>(members);
    }

    /**
     * Adds a new member to this group.
     *
     * @param newMember The new member to be added to this group.
     */
    public void addMember(TCSObjectReference<?> newMember) {
        Objects.requireNonNull(newMember, "newMember is null");
        members.add(newMember);
    }

    /**
     * Removes a member from this group.
     *
     * @param rmMember The member to be removed from this group.
     */
    public void removeMember(TCSObjectReference<?> rmMember) {
        Objects.requireNonNull(rmMember, "rmMember is null");
        members.remove(rmMember);
    }

    /**
     * Checks if this group contains a given object.
     *
     * @param chkMember The object to be checked for membership.
     * @return <code>true</code> if, and only if, the given object is a member of
     * this group.
     */
    public boolean containsMember(TCSObjectReference<?> chkMember) {
        Objects.requireNonNull(chkMember, "chkMember is null");
        return members.contains(chkMember);
    }

    @Override
    public Group clone() {
        Group clone = (Group) super.clone();
        clone.members = new HashSet<>();
        for (TCSObjectReference curRef : members) {
            clone.members.add(curRef);
        }
        return clone;
    }
}
