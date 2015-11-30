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
@Entity(name = "tcs_model_point")
//@Table(name = "tcs_model_point", uniqueConstraints =
//    @UniqueConstraint(columnNames = {"name", "scene"})
//)
public class PointDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto sceneDto;

    @DtoField
    @Column
    private String name;

    /**
     * This point's coordinates in mm.
     */
    @JsonProperty("position")
    @DtoField(value = "position",
                dtoBeanKey = "TripleDto",
                entityBeanKeys = {"Triple"})
    @OneToOne(optional = false, cascade = {CascadeType.ALL})
//    @JoinColumn(name = "position_id")
    private TripleDto position;

    @JsonProperty("display_position_x")
    @Column
    private long display_position_x;

    @JsonProperty("display_position_y")
    @Column
    private long display_position_y;

    @JsonProperty("label_offset_x")
    @Column
    private long label_offset_x;

    @JsonProperty("label_offset_y")
    @Column
    private long label_offset_y;

    @DtoField()
    @Column(name = "vehicle_orientation_angle")
    private double vehicleOrientationAngle = 0d/*Double.NaN*/;//Can NOT be NaN, will cause issue: java.sql.SQLException: 'NaN' is not a valid numeric or approximate numeric value

    @DtoField
    @Enumerated(value = EnumType.STRING)
    private Type type = Type.HALT_POSITION;

    // convert outside
    @JsonProperty("incomingPaths")
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "destinationPoint")
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_imcoming_paths")
    private Set<PathDto> incomingPaths = new HashSet<>();

    // convert outside
    @JsonProperty("outgoingPaths")
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "sourcePoint")
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_outgoing_paths")
    private Set<PathDto> outgoingPaths = new HashSet<>();

//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "point")
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_attached_links")
    private Set<LocationLinkDto> attachedLinks = new HashSet<>();

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

    public TripleDto getPosition() {
        return position;
    }

    public void setPosition(TripleDto position) {
        this.position = position;
    }

    public long getDisplay_position_x() {
        return display_position_x;
    }

    public void setDisplay_position_x(long display_position_x) {
        this.display_position_x = display_position_x;
    }

    public long getDisplay_position_y() {
        return display_position_y;
    }

    public void setDisplay_position_y(long display_position_y) {
        this.display_position_y = display_position_y;
    }

    public long getLabel_offset_x() {
        return label_offset_x;
    }

    public void setLabel_offset_x(long label_offset_x) {
        this.label_offset_x = label_offset_x;
    }

    public long getLabel_offset_y() {
        return label_offset_y;
    }

    public void setLabel_offset_y(long label_offset_y) {
        this.label_offset_y = label_offset_y;
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

    public void addIncomingPathDto(PathDto newPath) {
        this.incomingPaths.add(Objects.requireNonNull(newPath, "newPath"));
    }

    public void removeIncomingPathDto(PathDto path) {
        this.incomingPaths.remove(Objects.requireNonNull(path, "path"));
    }

    public void setIncomingPaths(Set<PathDto> incomingPaths) {
        this.incomingPaths = incomingPaths;
    }

    public void addIncomingPath(PathDto incomingPath) {
        this.incomingPaths.add(incomingPath);
    }

    public Set<PathDto> getOutgoingPaths() {
        return outgoingPaths;
    }

    public void addOutgoingPathDto(PathDto newPath) {
        this.outgoingPaths.add(Objects.requireNonNull(newPath, "newPath"));
    }

    public void removeOutgoingPathDto(PathDto pathDto) {
        this.outgoingPaths.remove(Objects.requireNonNull(pathDto, "pathDto"));
    }

    public void setOutgoingPaths(Set<PathDto> outgoingPathIds) {
        this.outgoingPaths = outgoingPathIds;
    }

    public void addOutgoingPath(PathDto outgoingPath) {
        this.outgoingPaths.add(outgoingPath);
    }

    public Set<LocationLinkDto> getAttachedLinks() {
        return attachedLinks;
    }

    public void setAttachedLinks(Set<LocationLinkDto> attachedLinks) {
        this.attachedLinks = attachedLinks;
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
        REPORT_POSITION("REPORT_POSITION"),
        /**
         * Indicates a position at which a vehicle may halt temporarily, e.g.
         * for executing an operating. The vehicle is also expected to report in
         * when it arrives at such a position. It may not park here for longer
         * than necessary, though.
         */
        HALT_POSITION("HALT_POSITION"),
        /**
         * Indicates a position at which a vehicle may halt for longer periods
         * of time when it is not processing orders. The vehicle is also
         * expected to report in when it arrives at such a position.
         */
        PARK_POSITION("PARK_POSITION");

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
