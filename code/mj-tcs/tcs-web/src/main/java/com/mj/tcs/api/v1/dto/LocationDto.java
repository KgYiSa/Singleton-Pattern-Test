package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;

import javax.persistence.*;
import java.util.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity
@Table(name = "tcs_model_location", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class LocationDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto scene;

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
     * A reference to this locationDto's type.
     */
//    @DtoField(value = "type",
//            dtoBeanKey = "LocationTypeDto",
//            entityBeanKeys = {"LocationType"})
//    private LocationTypeDto type;
    @Column
    private Long locationType;

    /**
     * A set of links attached to this locationDto.
     */
//    @JsonManagedReference
//    @DtoCollection(value = "attachedLinks",
//            entityCollectionClass = HashSet.class,
//            dtoCollectionClass = HashSet.class,
//            dtoBeanKey = "LocationLinkDto",
//            entityBeanKeys = {"Location$Link"},
//            dtoToEntityMatcher = LocationLinkDto2LocationLinkMatcher.class)
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_attached_links")
    private Set<Long> attachedLinks = new HashSet<>();

    public LocationDto(){
        //do nothing
    }
    /**
     * Creates a new LocationDto.
     *
     * @param name The new locationDto's name.
     * @param locationType The new locationDto's type.
     */
    public LocationDto(String name,
                       Long locationType) {
        this.name = name;
        this.locationType = Objects.requireNonNull(locationType, "locationType is null");
    }

    public SceneDto getScene() {
        return scene;
    }

    public void setScene(SceneDto scene) {
        this.scene = scene;
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
     * Returns a reference to the type of this locationDto.
     *
     * @return The Id to the type of this locationDto.
     */
    public Long getLocationType() {
      return locationType;
    }

    /**
     * Sets this locationDto's type.
     *
     * @param newTypeId This locationDto's new type.
     */
    public void setLocationType(Long newTypeId) {
//      type = Objects.requireNonNull(newType, "newType is null");
        // NOT required
        this.locationType = newTypeId;
    }

    /**
     * Returns a set of links attached to this locationDto.
     *
     * @return A set of links attached to this locationDto.
     */
    public Set<Long> getAttachedLinks() {
      return attachedLinks;
    }

    public void setAttachedLinks(Set<Long> attachedLinks) {
        this.attachedLinks = attachedLinks;
    }

    public boolean attachLink(Long newLinkId) {
        Objects.requireNonNull(newLinkId, "newLinkId is null");

        return attachedLinks.add(newLinkId);
    }

    public boolean detachLink(long linkId) {
        return attachedLinks.remove(linkId);
    }
}
