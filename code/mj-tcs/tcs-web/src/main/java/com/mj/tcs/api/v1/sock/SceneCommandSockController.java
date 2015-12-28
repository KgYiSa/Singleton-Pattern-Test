package com.mj.tcs.api.v1.sock;

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
                responseEntity = new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,
                        "The action code [" + actionCode + "] is not recognized.");
                break;
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
                sceneDtosProfile.add(item);
            }
        }

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDtosProfile);
    }

    private TCSResponseEntity<?> createSceneDto(Object jsonBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        SceneDto newSceneDto = null;
        try {
            String json = mapper.writeValueAsString(jsonBody);
            SceneDto sceneDto = mapper.readValue(json, SceneDto.class);
            newSceneDto = TCSDtoUtils.resolveSceneDtoRelationships(sceneDto);

            // Creating new scene
            newSceneDto = getService().createScene(newSceneDto);
        } catch (Exception e) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR, e.getMessage());
        }

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, newSceneDto, "Operation Success");
    }


    /**
     * TODO: lazy loading error!
     * @param jsonBody
     * @return
     */
    private TCSResponseEntity<?> getOneScene(Object jsonBody) {
        TCSResponseEntity.Status status = TCSResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        SceneDto sceneDto = null;
        Long sceneId = null;
        try {
            sceneId = Long.parseLong(jsonBody.toString());
        } catch (Exception e) {
            status = TCSResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

            sceneDto = getService().getSceneDto(sceneId);
        return new TCSResponseEntity<>(status, sceneDto, errorMessage);
    }

    private TCSResponseEntity<?> deleteSceneDto(Object jsonBody) {
        TCSResponseEntity.Status status = TCSResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            getService().deleteScene(sceneId);
        } catch (Exception e) {
            status = TCSResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TCSResponseEntity<>(status, errorMessage);
    }

    //////////////// ACTIONS ///////////////////////////
    private TCSResponseEntity<?> startScene(Object jsonBody) {
        TCSResponseEntity.Status status = TCSResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            SceneDto sceneDto = getService().getSceneDto(sceneId);
            getService().loadSceneDto(sceneDto);
        } catch (Exception e) {
            status = TCSResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TCSResponseEntity<>(status, errorMessage);
    }

    private TCSResponseEntity<?> stopScene(Object jsonBody) {
        TCSResponseEntity.Status status = TCSResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            SceneDto sceneDto = getService().getSceneDto(sceneId);
            getService().unloadSceneDto(sceneDto);
        } catch (Exception e) {
            status = TCSResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TCSResponseEntity<>(status, errorMessage);
    }
}
