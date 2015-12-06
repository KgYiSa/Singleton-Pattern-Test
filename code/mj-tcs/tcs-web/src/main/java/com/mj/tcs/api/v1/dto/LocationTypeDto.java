package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;

import javax.persistence.*;
import java.util.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_location_type")
//@Table(name = "tcs_model_location_type", uniqueConstraints =
//    @UniqueConstraint(columnNames = {"name", "scene"})
//)
public class LocationTypeDto extends BaseEntityDto {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_location_type_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new LinkedHashSet<>();

    /**
     * The operations allowed at locations of this type.
     */
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
//    @DtoCollection(value = "allowedOperations",
//            entityCollectionClass = String.class,
//            dtoCollectionClass = HashSet.class,
//            dtoBeanKey = "PathDto",
//            entityBeanKeys = {"Path"},
//            dtoToEntityMatcher = PathDto2PathMatcher.class)
    @DtoField
    @ElementCollection
    @CollectionTable(name = "tcs_model_location_type_operations", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<String> allowedOperations = new LinkedHashSet<>();
    
    public LocationTypeDto(){
        //DO nothing
    }

    public SceneDto getSceneDto() {
        return sceneDto;
    }

    public void setSceneDto(SceneDto sceneDto) {
        this.sceneDto = sceneDto;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String inName) {
        this.name = inName;
    }

    /**
     * Add property. It can be used to put any unknown property during deSerialization.
     *
     * Note: If value is null, then remove the property.
     *
     * @param name
     * @param value
     */
    public void addProperty(String name, String value, String type) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            if (value == null) {
                properties.remove(propertyOptional.get());
                return;
            } else {
                propertyOptional.get().setValue(Objects.requireNonNull(value));
                propertyOptional.get().setType(Objects.requireNonNull(type));
            }
        } else {
            if (value == null) {
                return;
            } else {
                EntityProperty property = new EntityProperty();
                property.setName(Objects.requireNonNull(name));
                property.setValue(Objects.requireNonNull(value));
                property.setType(Objects.requireNonNull(type));
                properties.add(property);
            }
        }
    }

    public String getProperty(String name) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            return propertyOptional.get().getValue();
        }

        return null;
    }

    public void setProperties(Set<EntityProperty> properties) {
        this.properties = properties;
    }

    public Set<EntityProperty> getProperties() {
        return properties;
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
            ops = new LinkedHashSet<>();
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
            ops = new LinkedHashSet<>();
        }

        boolean answer = ops.remove(operation);

        setAllowedOperations(ops);

        return answer;
    }

    public void setAllowedOperations(Set<String> allowedOperations) {
        this.allowedOperations = allowedOperations;
    }
}
