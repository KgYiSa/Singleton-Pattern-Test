package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.communication.TcsRequestEntity;
import com.mj.tcs.api.v1.dto.communication.TcsResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.util.TcsDtoUtils;
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
    public TcsResponseEntity<?> executeAction(/*Principal principal, */SimpMessageHeaderAccessor ha, TcsRequestEntity request) {
        Objects.requireNonNull(request);
        TcsResponseEntity<?> responseEntity = null;

        TcsRequestEntity.Action actionCode = request.getActionCode();
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
                responseEntity = new TcsResponseEntity<>(TcsResponseEntity.Status.ERROR,
                        "The action code [" + actionCode + "] is not recognized.");
                break;
        }

        responseEntity.setResponseUUID(request.getRequestUUID());
        return responseEntity;
    }

//
//    @MessageMapping("/topic/scene/{sceneId}/actions/request")
//    @SendTo("/topic/scene/{sceneId}/actions/response")
//    public TcsResponseEntity<?> executeSceneAction(@DestinationVariable Long sceneId, TcsRequestEntity request) {
//        Objects.requireNonNull(request);
//        TcsResponseEntity<?> responseEntity = null;
//
//        TcsRequestEntity.Action actionCode = request.getActionCode();
//        switch (actionCode) {
//            case SCENE_PROFILE:
//                responseEntity = getAllScenesProfile();
//                break;
//            case SCENE_CREATE:
//                // ERROR !!! FORBIDDEN
//                responseEntity = createSceneDto(request.getBody());
//                break;
//            case SCENE_DELETE:
//                responseEntity = deleteSceneDto(request.getBody());
//                break;
//            case SCENE_FIND:
//                responseEntity = getOneScene(request.getBody());
//                break;
//            case SCENE_START:
//                responseEntity = startScene(request.getBody());
//                break;
//            case SCENE_STOP:
//                responseEntity = stopScene(request.getBody());
//                break;
//            default:
//                responseEntity = new TcsResponseEntity<>(TcsResponseEntity.Status.ERROR,
//                        "The action code [" + actionCode + "] is not recognized.");
//                break;
//        }
//
//        return responseEntity;
//    }

    private TcsResponseEntity<?> getAllScenesProfile() {
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

        return new TcsResponseEntity<>(TcsResponseEntity.Status.SUCCESS, sceneDtosProfile);
    }

    private TcsResponseEntity<?> createSceneDto(Object jsonBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        SceneDto newSceneDto = null;
        try {
            String json = mapper.writeValueAsString(jsonBody);
            SceneDto sceneDto = mapper.readValue(json, SceneDto.class);
            newSceneDto = TcsDtoUtils.resolveSceneDtoRelationships(sceneDto);

            // Creating new scene
            newSceneDto = getService().createScene(newSceneDto);
        } catch (Exception e) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ERROR, e.getMessage());
        }

        return new TcsResponseEntity<>(TcsResponseEntity.Status.SUCCESS, newSceneDto, "Operation Success");
    }


    /**
     * TODO: lazy loading error!
     * @param jsonBody
     * @return
     */
    private TcsResponseEntity<?> getOneScene(Object jsonBody) {
        TcsResponseEntity.Status status = TcsResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        SceneDto sceneDto = null;
        Long sceneId = null;
        try {
            sceneId = Long.parseLong(jsonBody.toString());
        } catch (Exception e) {
            status = TcsResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

            sceneDto = getService().getSceneDto(sceneId);
        return new TcsResponseEntity<>(status, sceneDto, errorMessage);
    }

    private TcsResponseEntity<?> deleteSceneDto(Object jsonBody) {
        TcsResponseEntity.Status status = TcsResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            getService().deleteScene(sceneId);
        } catch (Exception e) {
            status = TcsResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TcsResponseEntity<>(status, errorMessage);
    }

    //////////////// ACTIONS ///////////////////////////
    private TcsResponseEntity<?> startScene(Object jsonBody) {
        TcsResponseEntity.Status status = TcsResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            SceneDto sceneDto = getService().getSceneDto(sceneId);
            getService().loadSceneDto(sceneDto);
        } catch (Exception e) {
            status = TcsResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TcsResponseEntity<>(status, errorMessage);
    }

    private TcsResponseEntity<?> stopScene(Object jsonBody) {
        TcsResponseEntity.Status status = TcsResponseEntity.Status.SUCCESS;
        String errorMessage = "Operation Success";

        try {
            Long sceneId = Long.parseLong(jsonBody.toString());
            SceneDto sceneDto = getService().getSceneDto(sceneId);
            getService().unloadSceneDto(sceneDto);
        } catch (Exception e) {
            status = TcsResponseEntity.Status.ERROR;
            errorMessage = e.getMessage();
        }

        return new TcsResponseEntity<>(status, errorMessage);
    }
}
