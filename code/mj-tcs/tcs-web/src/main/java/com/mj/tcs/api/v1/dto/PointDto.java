package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
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
@Entity(name = "tcs_model_point")
@Table(name = "tcs_model_point", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class PointDto extends BaseEntityDto {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tcs_model_point_properties", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    private Set<EntityProperty> properties = new LinkedHashSet<>();

    /**
     * This point's coordinates in mm.
     */
    @JsonProperty("position")
    @DtoField(value = "position",
                dtoBeanKey = "TripleDto",
                entityBeanKeys = {"Triple"})
//    @OneToOne(optional = false, cascade = {CascadeType.ALL})
//    @JoinColumn(name = "position_id")
    @Column
    private TripleDto position;

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

    @DtoField()
    @Column(name = "vehicle_orientation_angle")
    private double vehicleOrientationAngle = 0d/*Double.NaN*/;//Can NOT be NaN, will cause issue: java.sql.SQLException: 'NaN' is not a valid numeric or approximate numeric value

    @DtoField
    @Enumerated(value = EnumType.STRING)
    private Type type = Type.HALT_POSITION;

    // convert outside
    @JsonProperty("incoming_paths")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
//    @JsonManagedReference(value = "incoming_paths")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
    @JsonIgnoreProperties({"version", "auditor", "properties", "source_point", "destination_point", "control_points", "length", "routing_cost", "max_velocity", "max_reverse_velocity", "locked"})
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "destinationPoint")
    @ElementCollection
    @CollectionTable(name = "tcs_model_point_imcoming_paths", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    @OrderBy(value = "name ASC")
    private Set<PathDto> incomingPaths = new LinkedHashSet<>();

    // convert outside
    @JsonProperty("outgoing_paths")
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
//    @JsonManagedReference(value = "outgoing_paths")
    @JsonIgnoreProperties({"version", "auditor", "properties", "source_point", "destination_point", "control_points", "length", "routing_cost", "max_velocity", "max_reverse_velocity", "locked"})
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "sourcePoint")
    @ElementCollection
    @CollectionTable(name = "tcs_model_point_outgoing_paths", joinColumns = @JoinColumn(
            nullable = false, name = "model_id", referencedColumnName = "id"))
    @OrderBy(value = "name ASC")
    private Set<PathDto> outgoingPaths = new LinkedHashSet<>();

//    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({"version", "auditor", "properties", "location", "point", "allowed_operations"})
    @JsonSerialize(as = LinkedHashSet.class)
    @JsonDeserialize(as = LinkedHashSet.class)
    @OneToMany(cascade = {CascadeType.ALL}/*, mappedBy = "point"*/)
//    @ElementCollection
//    @CollectionTable(name = "tcs_model_rel_point_attached_links")
    @OrderBy(value = "name ASC")
    private Set<LocationLinkDto> attachedLinks = new LinkedHashSet<>();

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

    public TripleDto getPosition() {
        return position;
    }

    public void setPosition(TripleDto position) {
        this.position = position;
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

    public double getVehicleOrientationAngle() {
        return vehicleOrientationAngle;
    }

    public void setVehicleOrientationAngle(double vehicleOrientationAngle) {
        this.vehicleOrientationAngle = vehicleOrientationAngle;
    }

    public PointDto.Type getType() {
        return type;
    }

    public void setType(PointDto.Type type) {
        this.type = type;
    }

    public Set<PathDto> getIncomingPaths() {
        return incomingPaths;
    }

    public void addIncomingPath(PathDto newPath) {
        this.incomingPaths.add(Objects.requireNonNull(newPath, "newPath"));
    }

    public void removeIncomingPath(PathDto path) {
        this.incomingPaths.remove(Objects.requireNonNull(path, "path"));
    }

    public void setIncomingPaths(Set<PathDto> incomingPaths) {
        this.incomingPaths = incomingPaths;
    }

    public Set<PathDto> getOutgoingPaths() {
        return outgoingPaths;
    }

    public void addOutgoingPath(PathDto newPath) {
        this.outgoingPaths.add(Objects.requireNonNull(newPath, "newPath"));
    }

    public void removeOutgoingPath(PathDto pathDto) {
        this.outgoingPaths.remove(Objects.requireNonNull(pathDto, "pathDto"));
    }

    public void setOutgoingPaths(Set<PathDto> outgoingPathIds) {
        this.outgoingPaths = outgoingPathIds;
    }

    public Set<LocationLinkDto> getAttachedLinks() {
        return attachedLinks;
    }

    public void setAttachedLinks(Set<LocationLinkDto> attachedLinks) {
        this.attachedLinks = attachedLinks;
    }

    public void addAttachedLinks(LocationLinkDto linkDto) {
        this.attachedLinks.add(Objects.requireNonNull(linkDto));
    }

    public void removeAttachedLinks(LocationLinkDto linkDto) {
        this.attachedLinks.remove(Objects.requireNonNull(linkDto));
    }

    /**
     * The elements of this enumeration describe the various types of positions
     * known in openTCS sceneDto.
     */
    public enum Type {

        /**
         * Indicates a position at which a vehicle is expected to report in.
         * Halting or even parking at such a position is not allowed.
         */
        REPORT_POSITION("REPORT"),
        /**
         * Indicates a position at which a vehicle may halt temporarily, e.g.
         * for executing an operating. The vehicle is also expected to report in
         * when it arrives at such a position. It may not park here for longer
         * than necessary, though.
         */
        HALT_POSITION("HALT"),
        /**
         * Indicates a position at which a vehicle may halt for longer periods
         * of time when it is not processing orders. The vehicle is also
         * expected to report in when it arrives at such a position.
         */
        PARK_POSITION("PARK");

        private String text;

        Type(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static Type fromString(String text) {
            Optional<Type> type = Arrays.stream(Type.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (type.isPresent()) {
                return type.get();
            }

            throw new IllegalArgumentException("The PointDto.Type enum type is no recognizable [text=" + text + "]");
        }
    }
}
