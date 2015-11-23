/**
 * Instances of this class describe the attributes of location types relevant
 * to the openTCS system.
 */
package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author lkm
 * @author Wang Zhen
 */
public class LocationType extends BaseEntity implements Cloneable {

    private Scene scene;

    /**
     * The operations allowed at locations of this type.
     */
//    @ElementCollection
//    @CollectionTable(name = "tcs_model_location_type_operations")
    // splitted by ;
    private String allowedOperations = "";

    public LocationType(){
        //DO nothing
    }

    @Override
    public void clearId() {
        setId(null);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Returns a set of operations allowed with locations of this type.
     *
     * @return A set of operations allowed with locations of this type.
     */
    public Set<String> getAllowedOperations() {
        return stringToSet(allowedOperations);
    }

    /**
     * Checks if a given operation is allowed with locations of this type.
     *
     * @param operation The operation to be checked for.
     * @return <code>true</code> if, and only if, the given operation is allowed
     * with locations of this type.
     */
    public boolean isAllowedOperation(String operation) {
        Objects.requireNonNull(operation, "operation");
        Set<String> ops = getAllowedOperations();
        if (ops != null) {
            return getAllowedOperations().contains(operation);
        } else {
            return false;
        }
    }

    /**
     * Adds an allowed operation.
     *
     * @param operation The operation to be allowed.
     * @return <code>true</code> if, and only if, the given operation wasn't
     * already allowed with this location type.
     */
    public boolean addAllowedOperation(String operation) {
        Objects.requireNonNull(operation, "operation");
        Set<String> ops = getAllowedOperations();
        if (ops != null) {
            if (ops.contains(operation)) {
                return false;
            }
        } else {
            ops = new HashSet<>();
        }

        boolean answer = ops.add(operation);

        setAllowedOperations(ops);

        return answer;
    }

    /**
     * Removes an allowed operation.
     *
     * @param operation The operation to be disallowed.
     * @return <code>true</code> if, and only if, the given operation was allowed
     * with this location type before.
     */
    public boolean removeAllowedOperation(String operation) {
        Objects.requireNonNull(operation, "operation");

        Set<String> ops = getAllowedOperations();
        if (ops != null) {
            if (ops.contains(operation)) {
                return false;
            }
        } else {
            ops = new HashSet<>();
        }

        boolean answer = ops.remove(operation);

        setAllowedOperations(ops);

        return answer;
    }

    public void setAllowedOperations(Set<String> allowedOperations) {
        this.allowedOperations = setToString(allowedOperations);
    }

    @Override
    public LocationType clone() {
        LocationType clone = null;
        clone = (LocationType) super.clone();
        clone.allowedOperations = allowedOperations;

        return clone;
    }

    private String setToString(Set<String> strList) {
        if (strList == null || strList.size() == 0) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        // FORMAT: xxx;yyy;zzz
        for (String str : strList) {
            if (str == null || str.trim().length() == 0) {
                continue;
            }

            if(buffer.length() != 0) { // not the first time
                buffer.append(";");
            }
            buffer.append(str.trim());
        }

        return buffer.toString();
    }

    private Set<String> stringToSet(String inString) {
        if (inString == null || inString.trim().length() == 0) {
            return new HashSet<>();
        }

        String[] texts = inString.trim().split(";");
        if (texts == null || texts.length == 0) {
            return new HashSet<>();
        }


        Set<String> answer = new HashSet<>();
        for (String text : texts) {
            answer.add(text);
        }

        return answer;
    }
}
