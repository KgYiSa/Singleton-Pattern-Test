package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;
import com.mj.tcs.exception.ResourceUnknownException;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_block")
public class BlockDto  extends BaseEntityDto {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_block_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new HashSet<>();

    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_block_members", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
//    private Set<BaseEntityDto> members = new HashSet<>();
    private Set<BlockElementDto> members = new HashSet<>();

    @JsonIgnore
    @Transient
    private final static Map<Class, String> RESOURCE_CLASSES;

    static {
        // CLASS, METHOD
        RESOURCE_CLASSES = new HashMap<>();
        RESOURCE_CLASSES.put(PointDto.class, "getPointDtoByUUID");
        RESOURCE_CLASSES.put(PathDto.class, "getPathDtoByUUID");
        RESOURCE_CLASSES.put(BlockDto.class, "getBlockDtoByUUID");
    }

    @JsonIgnore
    public SceneDto getSceneDto() {
        return sceneDto;
    }

    public void setSceneDto(SceneDto sceneDto) {
        this.sceneDto = sceneDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add property. It can be used to put any unknown propery during deSerialization.
     *
     * @param name
     * @param value
     */
    public void addProperty(String name, String value, String type) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            propertyOptional.get().setValue(Objects.requireNonNull(value));
            propertyOptional.get().setType(Objects.requireNonNull(type));
        } else {
            EntityProperty property = new EntityProperty();
            property.setName(Objects.requireNonNull(name));
            property.setValue(Objects.requireNonNull(value));
            property.setType(Objects.requireNonNull(type));
            properties.add(property);
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

    public Set<BlockElementDto> getMembers() {
        return members;
    }

    public void setMembers(Set<BlockElementDto> members) {
        Objects.requireNonNull(members);
        for (BlockElementDto dto : members) {
            if (!checkResourceEntity(Objects.requireNonNull(dto))) {
                throw new ResourceUnknownException(dto);
            }

            this.members.add(dto);
        }
    }
//    public void setMembers(Set<BlockElementDto> members) {
//        Objects.requireNonNull(members);
//        for (BlockElementDto dto : members) {
//            if (!checkResourceEntity(Objects.requireNonNull(dto))) {
//                throw new ResourceUnknownException(dto);
//            }
//
//            this.members.add(dto);
//        }
//    }

    public void setResources(Set<BaseEntityDto> members) {
        Objects.requireNonNull(members);
        for (BaseEntityDto dto : members) {
            if (!checkResourceEntity(Objects.requireNonNull(dto))) {
                throw new ResourceUnknownException(dto);
            }

            this.members.add(new BlockElementDto(dto));
        }
    }

    public void addMember(BaseEntityDto member) {
        Objects.requireNonNull(member);
        if (!checkResourceEntity(member)) {
            throw new ResourceUnknownException(member);
        }

        this.members.add(new BlockElementDto(member));
    }

    @JsonIgnore
    public Set<BaseEntityDto> getResources() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<BaseEntityDto> resources = new HashSet<>();

        Class resourceClass;
        String resourceMethodName;
        Method resourceMethod;
        for (BlockElementDto elementDto : members) {
            resourceClass = elementDto.getClass();
            resourceMethodName = RESOURCE_CLASSES.get(resourceClass);
            resourceMethod = SceneDto.class.getMethod(resourceMethodName);
            resources.add((BaseEntityDto) resourceClass.cast(resourceMethod.invoke(sceneDto, elementDto.getElementUUID())));
        }

        return resources;
    }

    private boolean checkResourceEntity(BaseEntityDto dto) {
        Objects.requireNonNull(dto);
        return RESOURCE_CLASSES.keySet().contains(dto.getClass());
    }

    private boolean checkResourceEntity(BlockElementDto dto) {
        Objects.requireNonNull(dto);

        for (Class cls : RESOURCE_CLASSES.keySet()) {
            if (cls.getName().equals(dto.getClassName())) {
                return true;
            }
        }

        return false;
    }

    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    @Embeddable
    public static class BlockElementDto implements Serializable, Cloneable {

        @Column
        private String className;

        @Column
        private String elementUUID;

        public BlockElementDto() {
        }

        public BlockElementDto(BaseEntityDto entityDto) {
            Objects.requireNonNull(entityDto);
            setClassName(entityDto.getClass().getName());
            setElementUUID(Objects.requireNonNull(entityDto.getUUID()));
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = Objects.requireNonNull(className);
        }

        public String getElementUUID() {
            return elementUUID;
        }

        public void setElementUUID(String elementUUID) {
            this.elementUUID = Objects.requireNonNull(elementUUID);
        }

        @Override
        public boolean equals(Object elem) {
            if (!(elem instanceof BlockElementDto)) {
                return false;
            }
            Objects.requireNonNull(elem);
            BlockElementDto blockElement = (BlockElementDto) elem;

            return className.equals(blockElement.getClassName()) && elementUUID.equals(blockElement.getElementUUID());
        }
    }
}

