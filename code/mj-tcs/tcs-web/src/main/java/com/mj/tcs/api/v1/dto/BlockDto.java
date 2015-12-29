package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_block")
@Table(name = "tcs_model_block", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class BlockDto  extends BaseEntityDto {

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
    @CollectionTable(name = "tcs_model_block_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new LinkedHashSet<>();

    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @ElementCollection/*(targetClass = EntityProperty.class, fetch = FetchType.LAZY)*/
    @CollectionTable(name = "tcs_model_block_members", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<BlockElementDto> members = new LinkedHashSet<>();

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
     * Add property. It can be used to put any unknown property during deSerialization.
     *
     * Note: If value is null, then remove the property.
     *
     * @param name
     * @param value
     */
    public void addProperty(String name, String value) {
        Optional<EntityProperty> propertyOptional = properties.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (propertyOptional.isPresent()) {
            if (value == null) {
                properties.remove(propertyOptional.get());
                return;
            } else {
                propertyOptional.get().setValue(Objects.requireNonNull(value));
            }
        } else {
            if (value == null) {
                return;
            } else {
                EntityProperty property = new EntityProperty();
                property.setName(Objects.requireNonNull(name));
                property.setValue(Objects.requireNonNull(value));
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

    public Set<BlockElementDto> getMembers() {
        return members;
    }

    public void setMembers(Set<BlockElementDto> members) {
        Objects.requireNonNull(members);
        this.members.clear();
        // Check to avoid duplicated element !!!
        for (BlockElementDto elem : members) {
            if (!this.members.stream().anyMatch(b -> b.equals(elem))) {
                this.members.add(elem);
            }
        }
    }

    public void setResources(Set<BaseEntityDto> members) {
        Objects.requireNonNull(members);
        this.members.clear();
        for (BaseEntityDto dto : members) {
            BlockElementDto elem = new BlockElementDto(dto);

            // Check to avoid duplicated element !!!
            if (!this.members.stream().anyMatch(b -> b.equals(elem))) {
                this.members.add(elem);
            }
        }
    }

    public void addMember(BaseEntityDto member) {
        Objects.requireNonNull(member);
//        if (!checkResourceEntity(member)) {
//            throw new ResourceUnknownException(member);
//        }

        BlockElementDto elem = new BlockElementDto(member);
        // Check to avoid duplicated element !!!
        if (!this.members.stream().anyMatch(b -> b.equals(elem))) {
            this.members.add(elem);
        }
    }

    @JsonIgnore
    public Set<BaseEntityDto> getResources() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<BaseEntityDto> resources = new LinkedHashSet<>();

        resources.addAll(members.stream().map(BlockElementDto::getResource).collect(Collectors.toList()));

        return resources;
    }

    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    @Embeddable
    @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"point_dto_member", "path_dto_member", "location_dto_member"}))
    public static class BlockElementDto implements Serializable, Cloneable {

//        /**
//         * For json serialization only!!!
//         */
//        @JsonProperty("UUID")
//        @Transient
//        private String uuid;

        /**
         * For serialization from client only!!!
         */
        @JsonIgnore
        @Transient
        private String dummyUUID = null;

        @JsonIgnore
        @OneToOne
        private PointDto pointDtoMember;

        @JsonIgnore
        @OneToOne
        private PathDto pathDtoMember;

        @JsonIgnore
        @OneToOne
        private LocationDto locationDtoMember;

//        @JsonIgnore
//        @Column(name = "block", nullable = true)
//        private BlockDto blockDtoMember;

        public BlockElementDto() {
        }

        public BlockElementDto(BaseEntityDto entityDto) {
            Objects.requireNonNull(entityDto);

            if (entityDto instanceof PointDto) {
                pointDtoMember = (PointDto) entityDto;
                pathDtoMember = null;
                locationDtoMember = null;
//                blockDtoMember = null;
            } else if (entityDto instanceof PathDto) {
                pointDtoMember = null;
                pathDtoMember = (PathDto) entityDto;
                locationDtoMember = null;
//                blockDtoMember = null;
            } else if (entityDto instanceof LocationDto) {
                pointDtoMember = null;
                pathDtoMember = null;
                locationDtoMember = (LocationDto) entityDto;
//                blockDtoMember = null;
            } /*else if (entityDto instanceof BlockDto) {
                pointDtoMember = null;
                pathDtoMember = null;
                blockDtoMember = (BlockDto) entityDto;
            }*/ else {
                dummyUUID = entityDto.getUUID();
            }
//            {
//                throw new ResourceUnknownException(entityDto);
//            }
        }

        @JsonProperty("UUID")
        public String getUUID() {
            BaseEntityDto elem = getResource();
            if (elem != null) {
                return elem.getUUID();
            }

            return dummyUUID;
        }

        @JsonProperty("UUID")
        public void setUUID(String dummyUUID) {
            this.dummyUUID = Objects.requireNonNull(dummyUUID);
        }

        public PointDto getPointDtoMember() {
            return pointDtoMember;
        }

        public void setPointDtoMember(PointDto pointDtoMember) {
            this.pointDtoMember = pointDtoMember;
        }

        public PathDto getPathDtoMember() {
            return pathDtoMember;
        }

        public void setPathDtoMember(PathDto pathDtoMember) {
            this.pathDtoMember = pathDtoMember;
        }

        public LocationDto getLocationDtoMember() {
            return locationDtoMember;
        }

        public void setLocationDtoMember(LocationDto locationDtoMember) {
            this.locationDtoMember = locationDtoMember;
        }
//        public BlockDto getBlockDtoMember() {
//            return blockDtoMember;
//        }
//
//        public void setBlockDtoMember(BlockDto blockDtoMember) {
//            this.blockDtoMember = blockDtoMember;
//        }

        @JsonIgnore
        @Transient
        public BaseEntityDto getResource() {
            if (pointDtoMember != null) {
                return pointDtoMember;
            } else if (pathDtoMember != null) {
                return pathDtoMember;
            } else if (locationDtoMember != null) {
                return locationDtoMember;
            }/* else if (blockDtoMember != null) {
                return blockDtoMember;
            }*/

            return null;
        }

        @JsonIgnore
        public boolean isEmpty() {
            return (pointDtoMember == null) && (pathDtoMember == null) && (locationDtoMember == null) /*&& (blockDtoMember == null)*/;
        }

        @Override
        public boolean equals(Object elem) {
            if (!(elem instanceof BlockElementDto)) {
                return false;
            }
            Objects.requireNonNull(elem);
            BlockElementDto blockElement = (BlockElementDto) elem;

            if (getResource() == null) {
                return false;
            }

            return getResource().equals(blockElement.getResource());
        }
    }
}

