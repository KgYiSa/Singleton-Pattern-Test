//package com.mj.tcs.api.dto.connectity;
//
//import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
//import com.mj.tcs.api.dto.PointDto;
//
//import javax.persistence.*;
//
///**
// * @author Wang Zhen
// */
//public class PointConnectivityDto {
//
//    @DtoField
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
//    private Long id;
//
//    @DtoField
//    @Column(name = "version", nullable = false)
//    @Version
//    private Long version;
//
//    @ManyToOne
//    private SceneExtDto sceneExtDto;
//
//    @ManyToOne(optional = false)
//    private PointDto sourcePointDto;
//
//    @ManyToOne(optional = false)
//    private PointDto destinationPointDto;
//
//    @Column(nullable = false)
//    private boolean forwarding;
//
//    public Long getId() {
//        return id;
//    }
//
//    public PointConnectivityDto() {
//
//    }
//
//    public PointConnectivityDto(PointDto sourcePointDto, PointDto destinationPointDto, boolean forwarding) {
//        this.sourcePointDto = sourcePointDto;
//        this.destinationPointDto = destinationPointDto;
//        this.forwarding = forwarding;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getVersion() {
//        return version;
//    }
//
//    public void setVersion(Long version) {
//        this.version = version;
//    }
//
//    public SceneExtDto getSceneExtDto() {
//        return sceneExtDto;
//    }
//
//    public void setSceneExtDto(SceneExtDto sceneExtDto) {
//        this.sceneExtDto = sceneExtDto;
//    }
//
//    public PointDto getSourcePointDto() {
//        return sourcePointDto;
//    }
//
//    public void setSourcePointDto(PointDto sourcePointDto) {
//        this.sourcePointDto = sourcePointDto;
//    }
//
//    public PointDto getDestinationPointDto() {
//        return destinationPointDto;
//    }
//
//    public void setDestinationPointDto(PointDto destinationPointDto) {
//        this.destinationPointDto = destinationPointDto;
//    }
//
//    public boolean isForwarding() {
//        return forwarding;
//    }
//
//    public void setForwarding(boolean forwarding) {
//        this.forwarding = forwarding;
//    }
//}
