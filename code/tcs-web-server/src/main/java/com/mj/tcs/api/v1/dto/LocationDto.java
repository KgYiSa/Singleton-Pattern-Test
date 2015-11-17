package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.LocationLinkDto2LocationLinkMatcher;

import java.util.*;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
public class LocationDto extends BaseEntityAuditDto {
    @DtoField
    private String name;
    /**
     * This locationDto's tripleDto in mm.
     */
    @JsonProperty("position")
    @DtoField(value = "position",
            dtoBeanKey = "TripleDto",
            entityBeanKeys = {"Triple"})
    private TripleDto tripleDto = new TripleDto();
    /**
     * A reference to this locationDto's type.
     */
//    @DtoField(value = "type",
//            dtoBeanKey = "LocationTypeDto",
//            entityBeanKeys = {"LocationType"})
//    private LocationTypeDto type;
    private Long locationTypeId;

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
    private Set<LocationLinkDto> attachedLinks = new HashSet<>();

    public LocationDto(){
        //do nothing
    }
    /**
     * Creates a new LocationDto.
     *
     * @param name The new locationDto's name.
     * @param locationTypeId The new locationDto's type.
     */
    public LocationDto(String name,
                       Long locationTypeId) {
        this.name = name;
        this.locationTypeId = Objects.requireNonNull(locationTypeId, "locationTypeId is null");
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
    public TripleDto getTripleDto() {
      return tripleDto;
    }

    /**
     * Sets the physical coordinates of this locationDto in mm.
     *
     * @param newPosition The new physical coordinates of this locationDto. May not
     * be <code>null</code>.
     */
    public void setTripleDto(TripleDto newPosition) {
      tripleDto = Objects.requireNonNull(newPosition, "newPosition is null");
    }

    /**
     * Returns a reference to the type of this locationDto.
     *
     * @return The Id to the type of this locationDto.
     */
    public Long getLocationTypeId() {
      return locationTypeId;
    }

    /**
     * Sets this locationDto's type.
     *
     * @param newTypeId This locationDto's new type.
     */
    public void setLocationTypeId(Long newTypeId) {
//      type = Objects.requireNonNull(newType, "newType is null");
        // NOT required
        this.locationTypeId = newTypeId;
    }

    /**
     * Returns a set of links attached to this locationDto.
     *
     * @return A set of links attached to this locationDto.
     */
    public Set<LocationLinkDto> getAttachedLinks() {
      return new HashSet<>(attachedLinks);
    }

    public void setAttachedLinks(Set<LocationLinkDto> attachedLinks) {
        this.attachedLinks = attachedLinks;
    }

    public Optional<LocationLinkDto> getAttachedLinkById(long id) {
        if (attachedLinks == null) {
            return Optional.ofNullable(null);
        }

        return attachedLinks.stream().filter(l -> l.getId() == id).findFirst();
    }

    public boolean attachLink(LocationLinkDto newLink) {
        Objects.requireNonNull(newLink, "newLink is null");
        LocationDto linkLocation = Objects.requireNonNull(newLink.getLocationDto(), "newLink's locationDto is null");

        if (!linkLocation.equals(this)) {
            throw new IllegalArgumentException(
                    "locationDto end of link is not this locationDto");
        }
        return attachedLinks.add(newLink);
    }

    /**
     * Detaches a link from this locationDto.
     *
     * @param pointId The ID of the point end of the link to be detached from this
     * locationDto.
     * @return <code>true</code> if, and only if, there was a link to the given
     * pointDto attached to this locationDto.
     */
    public boolean detachLink(long pointId) {
      Iterator<LocationLinkDto> linkIter = attachedLinks.iterator();
      while (linkIter.hasNext()) {
        LocationLinkDto curLink = linkIter.next();
        if (pointId == curLink.getPointId()) {
          linkIter.remove();
          return true;
        }
      }
      return false;
    }

}
