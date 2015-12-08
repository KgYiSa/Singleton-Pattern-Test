package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.api.v1.dto.BlockDto;
import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.ResourceUnknownException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@Controller
public class SceneCommandSockController extends ServiceController {

    @MessageMapping("/topic/actions/request")
//    @SendToUser(value = "/topic/actions/response",broadcast = false)
    @SendToUser("/topic/actions/response")
    public TcsSockResponseEntity<?> request(/*Principal principal, */SimpMessageHeaderAccessor ha, TcsSockRequestEntity request) {
        Objects.requireNonNull(request);
        TcsSockResponseEntity<?> responseEntity = null;

        TcsSockRequestEntity.Action actionCode = request.getActionCode();
        switch (actionCode) {
            case SCENE_PROFILE:
                responseEntity = getAllScenesProfile();
                break;
            case SCENE_CREATE:
                responseEntity = createSceneDto(request.getBody());
                break;
            case SCENE_DELETE:
                responseEntity = deleteSceneDto(request.getBody());
                break;
            case SCENE_FIND:
                responseEntity = getOneScene(request.getBody());
                break;
            case SCENE_START:
                // TODO
                break;
            case SCENE_STOP:
                // TODO
                break;
            default:
                responseEntity = new TcsSockResponseEntity<>(TcsSockResponseEntity.Status.ERROR,
                        "The action code [" + actionCode + "] is not recognized.");
                break;
        }

        responseEntity.setUUID(request.getUUID());
        responseEntity.setActionCode(request.getActionCode());
        return responseEntity;
    }

    //    @MessageMapping("/topic/scenes/profile")
//    @SendTo("/topic/scenes/profile")
    private TcsSockResponseEntity<?> getAllScenesProfile() {
        Collection<SceneDto> sceneDtos = getModellingService().getAllScenes();
        List<Map<String, String>> sceneDtosProfile = new ArrayList<>();

        if (sceneDtos != null) {
            for (SceneDto sceneDto : sceneDtos) {
                Map<String, String> item = new LinkedHashMap<>();
                item.put("id", sceneDto.getId().toString());
                item.put("name", sceneDto.getName());
                item.put("status", getOperatingService().isSceneDtoRunning(sceneDto) ? "running" : "stopped");
                sceneDtosProfile.add(item);
            }
        }

        return new TcsSockResponseEntity<>(TcsSockResponseEntity.Status.OK, sceneDtosProfile);
    }

    //    @MessageMapping("/topic/scenes/create")
//    @SendTo("/topic/scenes/create")
    private TcsSockResponseEntity<?> createSceneDto(Object jsonBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        SceneDto newSceneDto = null;
        try {
            String json = mapper.writeValueAsString(jsonBody);
            SceneDto sceneDto = mapper.readValue(json, SceneDto.class);
            newSceneDto = resolveRelationships(sceneDto);

            // Creating new scene
            newSceneDto = getModellingService().createScene(newSceneDto);
        } catch (Exception e) {
            return new TcsSockResponseEntity<>(TcsSockResponseEntity.Status.ERROR, e.getMessage());
        }

        return new TcsSockResponseEntity<>(TcsSockResponseEntity.Status.OK, newSceneDto);
    }

    @Transactional
    //    @MessageMapping("/topic/scenes/find")
//    @SendTo("/topic/scenes/find")
    private TcsSockResponseEntity<?> getOneScene(/*@DestinationVariable */Object jsonBody) {
        TcsSockResponseEntity.Status status = TcsSockResponseEntity.Status.OK;
        String errorMessage = null;

        SceneDto sceneDto = null;
        Long sceneId = null;
        try {
            sceneId = Long.parseLong(jsonBody.toString());
        } catch (Exception e) {
            status = TcsSockResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

            sceneDto = getModellingService().getSceneDto(sceneId);
        return new TcsSockResponseEntity<>(status, sceneDto, errorMessage);
    }

    //    @MessageMapping("/topic/scenes/{sceneId}/delete")
    private TcsSockResponseEntity<?> deleteSceneDto(Object jsonBody) {
        TcsSockResponseEntity.Status status = TcsSockResponseEntity.Status.OK;
        String errorMessage = null;

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            getModellingService().deleteScene(sceneId);
        } catch (Exception e) {
            status = TcsSockResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TcsSockResponseEntity<>(status, errorMessage);
    }

    //////////////// ACTIONS ///////////////////////////
//    @MessageMapping("/api/v1/sock/scenes/{sceneId}/actions/start")
    private TcsSockResponseEntity<?> startScene(/*@DestinationVariable */Long sceneId) {
        SceneDto sceneDto = getModellingService().getSceneDto(sceneId);
        getOperatingService().loadSceneDto(sceneDto);

        return new TcsSockResponseEntity<>(TcsSockResponseEntity.Status.OK);
    }

    //    @MessageMapping("/api/v1/sock/scenes/{sceneId}/actions/stop")
    private TcsSockResponseEntity<?> stopScene(/*@DestinationVariable */Long sceneId) {
        SceneDto sceneDto = getModellingService().getSceneDto(sceneId);
        getOperatingService().unloadSceneDto(sceneDto);

        return new TcsSockResponseEntity<>(TcsSockResponseEntity.Status.OK);
    }

    private SceneDto resolveRelationships(SceneDto sceneDto) {
        if (sceneDto.getPointDtos() != null) {
            sceneDto.getPointDtos().forEach(p -> {
                p.setSceneDto(sceneDto);

                if (p.getIncomingPaths() != null) {
                    p.setIncomingPaths(p.getIncomingPaths().stream()
                            .map(pa -> {
                                final PathDto tempPa = Objects.requireNonNull(sceneDto.getPathDtoByUUID(pa.getUUID()));
                                tempPa.setDestinationPointDto(p);
                                return tempPa;
                            })
                            .collect(Collectors.toSet()));
                }
                if (p.getOutgoingPaths() != null) {
                    p.setOutgoingPaths(p.getOutgoingPaths().stream()
                            .map(pa -> {
                                final PathDto tempPa = Objects.requireNonNull(sceneDto.getPathDtoByUUID(pa.getUUID()));
                                tempPa.setSourcePointDto(p);
                                return tempPa;
                            })
                            .collect(Collectors.toSet()));
                }
                // TODO: We do NOT need to contains the raw links, just use the LocationDto->LocationLinkDto to do the associations
                if (p.getAttachedLinks() != null) {
                    p.setAttachedLinks(new LinkedHashSet<>());
                }
            });
        }
        if (sceneDto.getPathDtos() != null) {
            sceneDto.getPathDtos().forEach(p -> {
                p.setSceneDto(sceneDto);

                // CHECK RELATIONSHIP (Already linked in points' settings) TODO?
                if (p.getSourcePointDto() == null || p.getDestinationPointDto() == null) {
                    throw new ObjectUnknownException("Path : " + p.getName() + " point(s) is null!");
                }
            });
        }
        if (sceneDto.getLocationTypeDtos() != null) {
            sceneDto.getLocationTypeDtos().forEach(t -> t.setSceneDto(sceneDto));
        }
        if (sceneDto.getLocationDtos() != null) {
            sceneDto.getLocationDtos().forEach(l -> {
                l.setSceneDto(sceneDto);
                if (l.getAttachedLinks() != null) {
                    l.getAttachedLinks().forEach(li -> {
                        li.setSceneDto(sceneDto);

                        PointDto linkedPointDto = Objects.requireNonNull(sceneDto.getPointDtoByUUID(li.getPointDto().getUUID()));
                        li.setPointDto(linkedPointDto);
                        linkedPointDto.addAttachedLinks(li);
                        li.setLocationDto(sceneDto.getLocationDtoByUUID(li.getLocationDto().getUUID()));
                    });
                }

                l.setLocationTypeDto(sceneDto.getLocationTypeDtoByUUID(l.getLocationTypeDto().getUUID()));
            });
        }
        if (sceneDto.getBlockDtos() != null) {
            sceneDto.getBlockDtos().forEach(b -> {
                b.setSceneDto(sceneDto);

                // UUID -> Point/Path/Block
                if (b.getMembers() != null) {
                    Set<BlockDto.BlockElementDto> elementDtos = new LinkedHashSet<>();
                    for (BlockDto.BlockElementDto mem : b.getMembers()) {
                        String uuid = Objects.requireNonNull(mem.getUUID());

                        // Point
                        BaseEntityDto memDto = sceneDto.getPointDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        // Path
                        memDto = sceneDto.getPathDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        // Location
                        memDto = sceneDto.getLocationDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        throw new ResourceUnknownException("Resource: " + uuid + " not found!");
                    }
                    b.setMembers(elementDtos);
                }
            });
        }
        if (sceneDto.getStaticRouteDtos() != null) {
            sceneDto.getStaticRouteDtos().forEach(r -> {
                r.setSceneDto(sceneDto);

                r.setHops(r.getHops().stream().map(p -> sceneDto.getPointDtoByUUID(p.getUUID())).collect(Collectors.toList()));
            });
        }
        if (sceneDto.getVehicleDtos() != null) {
            sceneDto.getVehicleDtos().forEach(v -> {
                v.setSceneDto(sceneDto);

                if (v.getInitialPoint() != null) {
                    v.setInitialPoint(sceneDto.getPointDtoByUUID(v.getInitialPoint().getUUID()));
                }
            });
        }

        return sceneDto;
    }
}
