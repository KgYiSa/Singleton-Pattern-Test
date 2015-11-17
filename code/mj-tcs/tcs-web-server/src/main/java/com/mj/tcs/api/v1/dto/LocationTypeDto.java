package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class LocationTypeDto extends BaseEntityAuditDto {
    @DtoField
    private String name;
    /**
     * The operations allowed at locations of this type.
     */
//    @DtoCollection(value = "allowedOperations",
//            entityCollectionClass = String.class,
//            dtoCollectionClass = HashSet.class,
//            dtoBeanKey = "PathDto",
//            entityBeanKeys = {"Path"},
//            dtoToEntityMatcher = PathDto2PathMatcher.class)
    @DtoField
    private Set<String> allowedOperations = new HashSet<>();
    
    public LocationTypeDto(){
        //DO nothing
    }

    public String getName() {
        return this.name;
    }

    public void setName(String inName) {
        this.name = inName;
    }
    
    /**
     * Returns a set of operations allowed with locations of this type.
     *
     * @return A set of operations allowed with locations of this type.
     */
    public Set<String> getAllowedOperations() {
        return allowedOperations;
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
        this.allowedOperations = allowedOperations;
    }
}
