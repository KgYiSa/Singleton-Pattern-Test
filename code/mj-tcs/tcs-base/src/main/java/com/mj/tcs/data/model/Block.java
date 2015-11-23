package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseResource;

import java.util.HashSet;
import java.util.Set;

/**
 * An aggregation of resources that can never be used by more than one vehicle
 * at the same time.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public final class Block extends BaseResource implements Cloneable {

    private Scene scene;

    /**
     * The resources aggregated in this block.
     */
    private Set<BaseResource> members = new HashSet<>();

    public Block() {
    }

    /**
     * Returns a set of all members of this block.
     *
     * @return A set of all members of this block.
     */
    public Set<BaseResource> getMembers() {
        return members;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Adds a new member to this block.
     *
     * @param newMember The new member to be added to this block.
     */
    public void addMember(BaseResource newMember) {
        if (newMember == null) {
            throw new NullPointerException("newMember is null");
        }
        members.add(newMember);
    }

    /**
     * Removes a member from this block.
     *
     * @param rmMember The member to be removed from this block.
     */
    public void removeMember(BaseResource rmMember) {
        if (rmMember == null) {
            throw new NullPointerException("rmMember is null");
        }
        members.remove(rmMember);
    }

    /**
     * Checks if this block contains a given object.
     *
     * @param chkMember The object to be checked for membership.
     * @return <code>true</code> if, and only if, the given object is a member of
     * this block.
     */
    public boolean containsMember(BaseResource chkMember) {
        if (chkMember == null) {
            throw new NullPointerException("chkMember is null");
        }
        return members.contains(chkMember);
    }

    @Override
    public Block clone() {
        Block clone = null;
        clone = (Block) super.clone();
        clone.members = new HashSet<>(members);
        return clone;
    }
}
