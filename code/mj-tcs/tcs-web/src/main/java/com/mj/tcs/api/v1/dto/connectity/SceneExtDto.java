//package com.mj.tcs.api.v1.dto.connectity;
//
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import com.inspiresoftware.lib.dto.geda.annotations.Dto;
//import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
//import com.mj.tcs.api.v1.dto.PointDto;
//import com.mj.tcs.api.v1.dto.SceneDto;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import javax.persistence.*;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * @author Wang Zhen
// */
//@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
//@Dto
//@Entity(name = "tcs_model_scene_connectity")
//public class SceneExtDto {
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
//    @OneToOne(optional = false)
//    private SceneDto sceneDto;
//
//    @OneToMany(mappedBy = "sceneExtDto", cascade = {CascadeType.ALL})
//    private Set<PointConnectivityDto> pointConnectivityDtos;
//
//    @Transient
//    private Object syncObject = new Object();
//
//    @Transient
//    private TaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//
//    public Long getId() {
//        return id;
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
//    public Set<PointConnectivityDto> getPointConnectivityDtos() {
//        return pointConnectivityDtos;
//    }
//
//    public SceneDto getSceneDto() {
//        return sceneDto;
//    }
//
//    public void setSceneDto(SceneDto sceneDto) {
//        synchronized (syncObject) {
//            this.sceneDto = Objects.requireNonNull(sceneDto);
//        }
//
//        taskExecutor.execute(new Thread(new ConnectivityCalcTask(this, sceneDto)));
//    }
//
//    public void addPointConnectivityDto(PointConnectivityDto pointConnectivityDto) {
//        this.pointConnectivityDtos.add(Objects.requireNonNull(pointConnectivityDto));
//    }
//
//    public void setPointConnectivityDtos(Set<PointConnectivityDto> pointConnectivityDtos) {
//        this.pointConnectivityDtos = pointConnectivityDtos;
//    }
//
//    private class ConnectivityCalcTask implements Runnable {
//        @Transient
//        private final SceneExtDto sceneExtDto;
//
//        @Transient
//        private final SceneDto sceneDto;
//
//        public ConnectivityCalcTask(SceneExtDto sceneExtDto, SceneDto sceneDto) {
//            this.sceneExtDto = Objects.requireNonNull(sceneExtDto);
//            this.sceneDto = Objects.requireNonNull(sceneDto);
//        }
//
//        @Override
//        public void run() {
//            synchronized (syncObject) {
//                if (sceneDto.getPointDtos() != null) {
//                    for (final PointDto pointDto : sceneDto.getPointDtos()) {
//                        if (pointDto.getIncomingPaths() != null) {
//                            pointDto.getIncomingPaths().stream()
//                                    .filter(v -> v.getMaxReverseVelocity() != 0.0)
//                                    .map(v -> v.getSourcePointDto())
//                                    .forEach(v -> {
//                                        PointConnectivityDto pointConnectivityDto = new PointConnectivityDto(pointDto, v, false);
//                                        pointConnectivityDto.setSceneExtDto(sceneExtDto);
//                                        sceneExtDto.addPointConnectivityDto(pointConnectivityDto);
//                                    });
//                        }
//
//                        if (pointDto.getOutgoingPaths() != null) {
//                            pointDto.getOutgoingPaths().stream()
//                                    .filter(v -> v.getMaxVelocity() != 0.0)
//                                    .map(v -> v.getDestinationPointDto())
//                                    .forEach(v -> {
//                                        PointConnectivityDto pointConnectivityDto = new PointConnectivityDto(pointDto, v, true);
//                                        pointConnectivityDto.setSceneExtDto(sceneExtDto);
//                                        sceneExtDto.addPointConnectivityDto(pointConnectivityDto);
//                                    });
//                        }
//                    }
//                }
//
//                syncObject.notify();
//            }
//        }
//    }
//}
