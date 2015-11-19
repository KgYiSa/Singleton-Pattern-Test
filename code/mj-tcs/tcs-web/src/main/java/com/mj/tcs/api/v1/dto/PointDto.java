package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Dto
@Entity
@Table(name = "tcs_model_point", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class PointDto extends BaseEntityDto {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private SceneDto scene;

    @DtoField
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

    @DtoField()
    @Column(name = "vehicle_orientation_angle")
    private double vehicleOrientationAngle = 0d/*Double.NaN*/;//Can NOT be NaN, will cause issue: java.sql.SQLException: 'NaN' is not a valid numeric or approximate numeric value

    @DtoField
    @Enumerated(value = EnumType.STRING)
    private PointDto.Type type = Type.HALT_POSITION;

    // convert outside
    @JsonProperty("incomingPaths")
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "destinationPoint")
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_imcoming_paths")
    private Set<Long> incomingPaths = new HashSet<>();

    // convert outside
    @JsonProperty("outgoingPaths")
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "sourcePoint")
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_outgoing_paths")
    private Set<Long> outgoingPaths = new HashSet<>();

//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "point")
    @ElementCollection
    @CollectionTable(name = "tcs_model_rel_attached_links")
    private Set<Long> attachedLinks = new HashSet<>();

    // OPERATING MODEL ONLY !!!
    /**
     * The vehicle occupying this point.
     */
    private Long occupyingVehicleId = -1L;

    public SceneDto getScene() {
        return scene;
    }

    public void setScene(SceneDto scene) {
        this.scene = scene;
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

    public Set<Long> getIncomingPaths() {
        return incomingPaths;
    }

    public void setIncomingPaths(Set<Long> incomingPathIds) {
        this.incomingPaths = incomingPathIds;
    }

    public void addIncomingPath(long incomingPathId) {
        this.incomingPaths.add(incomingPathId);
    }

    public Set<Long> getOutgoingPaths() {
        return outgoingPaths;
    }

    public void setOutgoingPaths(Set<Long> outgoingPathIds) {
        this.outgoingPaths = outgoingPathIds;
    }

    public void addOutgoingPath(long outgoingPathId) {
        this.outgoingPaths.add(outgoingPathId);
    }

    /**
     * The elements of this enumeration describe the various types of positions
     * known in openTCS scene.
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
