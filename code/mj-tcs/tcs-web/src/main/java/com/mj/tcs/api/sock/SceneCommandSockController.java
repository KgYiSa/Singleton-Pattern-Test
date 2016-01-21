package com.mj.tcs.api.sock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.api.dto.SceneDto;
import com.mj.tcs.api.dto.communication.TCSRequestEntity;
import com.mj.tcs.api.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.web.ServiceController;
import com.mj.tcs.util.TCSDtoUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

/**
 * @author Wang Zhen
 */
@Controller
public class SceneCommandSockController extends ServiceController {

    @MessageMapping("/topic/actions/request")
    @SendToUser("/topic/actions/response")
    public TCSResponseEntity<?> executeGeneralAction(/*Principal principal, */SimpMessageHeaderAccessor ha, TCSRequestEntity request) {
        Objects.requireNonNull(request);
        TCSResponseEntity<?> responseEntity = null;

        TCSRequestEntity.Action actionCode = request.getActionCode();
        if (actionCode == null) {// Check actionCode
            return TCSResponseEntity.getBuilder()
                    .setResponseUUID(request.getRequestUUID())
                    .setStatus(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage("The action code is null.")
                    .get();
        }

        try {
            switch (actionCode) {
                case SCENE_PROFILE:
                    responseEntity = getAllScenesProfile();
                    break;
                case SCENE_CREATE:
                    // ERROR !!! FORBIDDEN
                    responseEntity = createSceneDto(request.getBody());
                    break;
                case SCENE_DELETE:
                    responseEntity = deleteSceneDto(request.getBody());
                    break;
                case SCENE_FIND:
                    responseEntity = getOneScene(request.getBody());
                    break;
                case SCENE_START:
                    responseEntity = startScene(request.getBody());
                    break;
                case SCENE_STOP:
                    responseEntity = stopScene(request.getBody());
                    break;
                default:
                    throw new IllegalArgumentException("The action code [" + actionCode + "] is not recognized.");
            }
        } catch (Exception e) {
            responseEntity = TCSResponseEntity.getBuilder()
                    .setResponseUUID(request.getRequestUUID())
                    .setStatus(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage(e.getMessage())
                    .get();
        }

        responseEntity.setResponseUUID(request.getRequestUUID());
        return responseEntity;
    }

    @MessageMapping("/topic/actions/scenes/{sceneId}/request")
    @SendToUser("/topic/actions/response")
    public TCSResponseEntity<?> executeSpecificAction(@DestinationVariable Long sceneId,
                                                      SimpMessageHeaderAccessor ha,
                                                      TCSRequestEntity request) {
        Objects.requireNonNull(request);
        TCSResponseEntity<?> responseEntity = null;

        TCSRequestEntity.Action actionCode = request.getActionCode();
        if (actionCode == null) {// Check actionCode
            return TCSResponseEntity.getBuilder()
                    .setStatus(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage("The action code is null.")
                    .get();
        }

        try {
            switch (actionCode) {
                case SCENE_SPECIFIC_PROFILE:
                    responseEntity = getSceneProfile(sceneId);
                    break;
                default:
                    throw new IllegalArgumentException("The action code [" + actionCode + "] is not recognized.");
            }
        } catch (Exception e) {
            responseEntity = TCSResponseEntity.getBuilder()
                    .setStatus(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage(e.getMessage())
                    .get();
        }

        responseEntity.setResponseUUID(request.getRequestUUID());
        return responseEntity;
    }

    private TCSResponseEntity<?> getAllScenesProfile() {
        Collection<SceneDto> sceneDtos = getService().findAllScene();
        List<Map<String, String>> sceneDtosProfile = new ArrayList<>();

        if (sceneDtos != null) {
            for (SceneDto sceneDto : sceneDtos) {
                Map<String, String> item = new LinkedHashMap<>();
                item.put("id", sceneDto.getId().toString());
                item.put("name", sceneDto.getName());
                item.put("status", getService().isSceneDtoRunning(sceneDto) ? "running" : "stopped");
                item.put("updated_at", sceneDto.getUpdatedAt());
                sceneDtosProfile.add(item);
            }
        }

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .setBody(sceneDtosProfile)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDtosProfile);
    }

    private TCSResponseEntity<?> getSceneProfile(long sceneId) {
        SceneDto sceneDto = Objects.requireNonNull(getService().getSceneDto(sceneId));
        Map<String, String> sceneDtoProfile = new TreeMap<>();

        if (sceneDto != null) {
            sceneDtoProfile.put("id", sceneDto.getId().toString());
            sceneDtoProfile.put("name", sceneDto.getName());
            sceneDtoProfile.put("status", getService().isSceneDtoRunning(sceneDto) ? "running" : "stopped");
            sceneDtoProfile.put("updated_at", sceneDto.getUpdatedAt());
        }

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .setBody(sceneDtoProfile)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDtoProfile);
    }

    private TCSResponseEntity<?> createSceneDto(Object jsonBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        String json = mapper.writeValueAsString(jsonBody);
        SceneDto sceneDto = mapper.readValue(json, SceneDto.class);
        SceneDto newSceneDto = TCSDtoUtils.resolveSceneDtoRelationships(sceneDto);

        // Creating new scene
        newSceneDto = getService().createScene(newSceneDto);

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .setBody(newSceneDto)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, newSceneDto);
    }

    /**
     * TODO: lazy loading error!
     * @param jsonBody
     * @return
     */
    private TCSResponseEntity<?> getOneScene(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());

        SceneDto sceneDto = getService().getSceneDto(sceneId);

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .setBody(sceneDto)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDto);
    }

    private TCSResponseEntity<?> deleteSceneDto(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());
        getService().deleteScene(sceneId);

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    //////////////// ACTIONS ///////////////////////////
    private TCSResponseEntity<?> startScene(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());
        SceneDto sceneDto = Objects.requireNonNull(getService().getSceneDto(sceneId));
        getService().loadSceneDto(sceneDto);

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    private TCSResponseEntity<?> stopScene(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());
        SceneDto sceneDto = Objects.requireNonNull(getService().getSceneDto(sceneId));
        getService().unloadSceneDto(sceneDto);

        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }
}
