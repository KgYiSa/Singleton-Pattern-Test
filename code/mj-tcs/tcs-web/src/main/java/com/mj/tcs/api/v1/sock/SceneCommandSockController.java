package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.communication.TCSRequestEntity;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.util.TCSDtoUtils;
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
    public TCSResponseEntity<?> executeAction(/*Principal principal, */SimpMessageHeaderAccessor ha, TCSRequestEntity request) {
        Objects.requireNonNull(request);
        TCSResponseEntity<?> responseEntity = null;

        TCSRequestEntity.Action actionCode = request.getActionCode();
        if (actionCode == null) {// Check actionCode
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,
                    "The action code is null.");
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
            responseEntity = new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,null, e.getMessage());
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
                item.put("updated_at", String.valueOf(sceneDto.getAuditorDto().getUpdatedAt().getTime()));
                sceneDtosProfile.add(item);
            }
        }

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDtosProfile);
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

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, newSceneDto);
    }

    /**
     * TODO: lazy loading error!
     * @param jsonBody
     * @return
     */
    private TCSResponseEntity<?> getOneScene(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());

        SceneDto sceneDto = getService().getSceneDto(sceneId);
        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDto);
    }

    private TCSResponseEntity<?> deleteSceneDto(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());
        getService().deleteScene(sceneId);

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    //////////////// ACTIONS ///////////////////////////
    private TCSResponseEntity<?> startScene(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());
        SceneDto sceneDto = Objects.requireNonNull(getService().getSceneDto(sceneId));
        getService().loadSceneDto(sceneDto);

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    private TCSResponseEntity<?> stopScene(Object jsonBody) {
        Long sceneId = Long.parseLong(jsonBody.toString());
        SceneDto sceneDto = Objects.requireNonNull(getService().getSceneDto(sceneId));
        getService().unloadSceneDto(sceneDto);

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }
}
