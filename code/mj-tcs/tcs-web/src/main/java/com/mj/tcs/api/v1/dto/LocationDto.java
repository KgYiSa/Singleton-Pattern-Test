package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.EntityProperty;
import com.mj.tcs.api.v1.dto.base.TripleDto;

import javax.persistence.*;
import java.util.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@UUID")
@Dto
@Entity(name = "tcs_model_location")
@Table(name = "tcs_model_location", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class LocationDto extends BaseEntityDto {

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
    @CollectionTable(name = "tcs_model_location_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new LinkedHashSet<>();

    /**
     * This locationDto's position in mm.
     */
    @JsonProperty("position")
    @DtoField(value = "position",
            dtoBeanKey = "TripleDto",
            entityBeanKeys = {"Triple"})
//    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    @Column
    private TripleDto position = new TripleDto();

    /**
     * A reference to this locationDto's locationTypeDto.
     */
    @JsonProperty("location_type")
    @JsonIgnoreProperties({"version", "auditor", "properties", "allowed_operations"})
    @DtoField(value = "locationTypeDto",
            dtoBeanKey = "LocationTypeDto",
            entityBeanKeys = {"LocationType"})
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "location_type")
//    @Column(name = "locationType")
    private LocationTypeDto locationTypeDto;

    /**
     * A set of links attached to this locationDto.
     */
    @JsonManagedReference
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
//    @ElementCollection
    @OneToMany(cascade = {CascadeType.ALL})
    @CollectionTable(name = "tcs_model_location_attached_links", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    @OrderBy(value = "name ASC")
    private Set<LocationLinkDto> attachedLinks = new LinkedHashSet<>();

    @JsonProperty("display_position_x")
    @Column(name = "display_position_x")
    private long displayPositionX;

    @JsonProperty("display_position_y")
    @Column(name = "display_position_y")
    private long displayPositionY;

    @JsonProperty("label_offset_x")
    @Column(name = "label_offset_x")
    private long labelOffsetX;

    @JsonProperty("label_offset_y")
    @Column(name = "label_offset_y")
    private long labelOffsetY;


    public LocationDto(){
        //do nothing
    }
    /**
     * Creates a new LocationDto.
     *
     * @param name The new locationDto's name.
     * @param locationType The new locationDto's locationTypeDto.
     */
    public LocationDto(String name,
                       LocationTypeDto locationType) {
        this.name = name;
        this.locationTypeDto = Objects.requireNonNull(locationType, "locationType is null");
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

    /**
     * Returns the physical coordinates of this locationDto in mm.
     *
     * @return The physical coordinates of this locationDto in mm.
     */
    public TripleDto getPosition() {
      return position;
    }

    /**
     * Sets the physical coordinates of this locationDto in mm.
     *
     * @param newPosition The new physical coordinates of this locationDto. May not
     * be <code>null</code>.
     */
    public void setPosition(TripleDto newPosition) {
      position = Objects.requireNonNull(newPosition, "newPosition is null");
    }

    /**
     * Returns a reference to the locationTypeDto of this locationDto.
     *
     * @return The Id to the locationTypeDto of this locationDto.
     */
    public LocationTypeDto getLocationTypeDto() {
      return locationTypeDto;
    }

    /**
     * Sets this locationDto's locationTypeDto.
     *
     * @param newType This locationDto's new locationTypeDto.
     */
    public void setLocationTypeDto(LocationTypeDto newType) {
//      locationTypeDto = Objects.requireNonNull(newType, "newType is null");
        // NOT required
        this.locationTypeDto = newType;
    }

    /**
     * Returns a set of links attached to this locationDto.
     *
     * @return A set of links attached to this locationDto.
     */
    public Set<LocationLinkDto> getAttachedLinks() {
      return attachedLinks;
    }

    public void setAttachedLinks(Set<LocationLinkDto> attachedLinks) {
        this.attachedLinks = attachedLinks;
    }

    public boolean attachLink(LocationLinkDto newLink) {
        Objects.requireNonNull(newLink, "newLink is null");

        return attachedLinks.add(newLink);
    }

    public boolean detachLink(LocationLinkDto link) {
        return attachedLinks.remove(link);
    }

    public long getDisplayPositionX() {
        return displayPositionX;
    }

    public void setDisplayPositionX(long displayPositionX) {
        this.displayPositionX = displayPositionX;
    }

    public long getDisplayPositionY() {
        return displayPositionY;
    }

    public void setDisplayPositionY(long displayPositionY) {
        this.displayPositionY = displayPositionY;
    }

    public long getLabelOffsetX() {
        return labelOffsetX;
    }

    public void setLabelOffsetX(long labelOffsetX) {
        this.labelOffsetX = labelOffsetX;
    }

    public long getLabelOffsetY() {
        return labelOffsetY;
    }

    public void setLabelOffsetY(long labelOffsetY) {
        this.labelOffsetY = labelOffsetY;
    }
}
