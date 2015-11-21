package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.LocationLinkDto2LocationLinkMatcher;

import javax.persistence.*;
import java.util.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity(name = "tcs_model_location")
//@Table(name = "tcs_model_location", uniqueConstraints =
//    @UniqueConstraint(columnNames = {"name", "scene"})
//)
public class LocationDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    /**
     * This locationDto's position in mm.
     */
    @JsonProperty("position")
    @DtoField(value = "position",
            dtoBeanKey = "TripleDto",
            entityBeanKeys = {"Triple"})
    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    private TripleDto position = new TripleDto();

    /**
     * A reference to this locationDto's locationTypeDto.
     */
    @DtoField(value = "locationTypeDto",
            dtoBeanKey = "LocationTypeDto",
            entityBeanKeys = {"LocationType"})
    @Column
    private LocationTypeDto locationTypeDto;

    /**
     * A set of links attached to this locationDto.
     */
    @JsonManagedReference
    @DtoCollection(value = "attachedLinks",
            entityCollectionClass = HashSet.class,
            dtoCollectionClass = HashSet.class,
            dtoBeanKey = "LocationLinkDto",
            entityBeanKeys = {"Location$Link"},
            dtoToEntityMatcher = LocationLinkDto2LocationLinkMatcher.class)
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_attached_links")
    private Set<LocationLinkDto> attachedLinks = new HashSet<>();

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
}
