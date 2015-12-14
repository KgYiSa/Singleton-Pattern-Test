package com.mj.tcs.api.v1.rest;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.communication.TcsResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.util.TcsDtoUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * The class maintains the complete static topology of a scene model
 * i.e. Points, Paths, Blocks, Groups, Locations, LocationDto Types,  Static Routes, Vehicles etc.
 *
 * @author Wang Zhen
 */
@RestController
//@ExposesResourceFor(SceneDto.class)
@RequestMapping({"/api/v1/rest", "/rest"})
public class SceneRestfulController extends ServiceController {
//    @Autowired
//    @Qualifier(value = "SceneDtoConverter")
//    private DtoConverter dtoConverter;// = new DtoConverterImp();
//
//    @Autowired
//    private EntityLinks entityLinks;

    @RequestMapping(value = "/scenes/profile", method = RequestMethod.GET)
    public TcsResponseEntity<?> getAllScenesProfile() {
        Collection<SceneDto> sceneDtos = getService().findAllScene();
        if (sceneDtos == null || sceneDtos.size() == 0) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.INFORMATION, "No Content");
        }

        List<Map<String, String>> sceneDtosProfile = new ArrayList<>();
        for (SceneDto sceneDto : sceneDtos) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("id", sceneDto.getId().toString());
            item.put("name", sceneDto.getName());
            item.put("status", getService().isSceneDtoRunning(sceneDto) ? "running" : "stopped");
            sceneDtosProfile.add(item);
        }

        return new TcsResponseEntity<>(
                TcsResponseEntity.Status.SUCCESS,
                sceneDtosProfile);
    }

    ///////////////// MODELLING /////////////////////
    @RequestMapping(value = "/scenes", method = RequestMethod.GET)
    public TcsResponseEntity<?> getAllScenes() {
        Collection<SceneDto> sceneDtos = getService().findAllScene();
        if (sceneDtos == null || sceneDtos.size() == 0) {
            return new TcsResponseEntity<>(
                    TcsResponseEntity.Status.INFORMATION).setStatusMessage("No Content");
        }

        return new TcsResponseEntity<>(
                TcsResponseEntity.Status.SUCCESS,
                sceneDtos);
    }

    @RequestMapping(value = "/scenes", method = RequestMethod.POST)
    public TcsResponseEntity<?> createScene(@RequestBody SceneDto sceneDto) {
        SceneDto newSceneDto = TcsDtoUtils.resolveSceneDtoRelationships(sceneDto);

        // Creating new scene
        newSceneDto = getService().createScene(newSceneDto);

        return new TcsResponseEntity<>(
                TcsResponseEntity.Status.SUCCESS,
                newSceneDto);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.GET)
    public TcsResponseEntity<?> getOneScene(@PathVariable("sceneId") Long sceneId) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ALERT).setStatusMessage("Not Found!");
        }

        return new TcsResponseEntity<>(
                TcsResponseEntity.Status.SUCCESS,
                sceneDto);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PUT)
    public TcsResponseEntity<?> updateScene(@PathVariable("sceneId") Long sceneId, SceneDto newSceneDto) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ERROR)
                    .setStatusMessage("The scene can not be found by ID [" + sceneId + "].");
        }

        newSceneDto.setId(sceneId);
        if (getService().isSceneDtoRunning(sceneDto)) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ERROR)
                    .setStatusMessage("The scene is running, please stop it first!");
        }
        TcsDtoUtils.copyProperties(sceneDto, newSceneDto);

        sceneDto = getService().updateScene(sceneDto);

        return new TcsResponseEntity<>(TcsResponseEntity.Status.SUCCESS, sceneDto);
    }

//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PATCH)
//    public TcsResponseEntity<?> updateScenePartial(@PathVariable("sceneId") Long sceneId, EntityAuditorDto entityAuditorDto) {
//        entityAuditorDto.setId(sceneId);
//
//        SceneDto scene = getService().getSceneDto(sceneId);
//        // TODO: find a way to found which attribute should be updated!!! (merge new attributes (Map) to the current valueconverter)
//        scene = (SceneDto) dtoConverter.mergePropertiesToEntity(scene, entityAuditorDto.getProperties());
//
//        // TODO: Should update the updated time properties?
//
//        // save
//        getService().updateScene(scene);
//
//        return new TcsResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
//                HttpStatus.OK);
//    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.DELETE)
    public TcsResponseEntity<?> deleteScene(@PathVariable("sceneId") Long sceneId) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ALERT)
                    .setStatusMessage("The scene can not be found by ID [" + sceneId + "].");
        }

        if (getService().isSceneDtoRunning(sceneDto)) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ERROR)
                    .setStatusMessage("The scene is running, please stop it first!");
        }

        try {
            getService().deleteScene(sceneId);
        } catch (EmptyResultDataAccessException e) {
            return new TcsResponseEntity<>(TcsResponseEntity.Status.ALERT).setStatusMessage("Not found!");
        }

        return new TcsResponseEntity<>(TcsResponseEntity.Status.SUCCESS);
    }

    //////////////// ACTIONS ///////////////////////////
    @RequestMapping(value = "/scenes/{sceneId}/actions/start"/*, method = RequestMethod.POST*/)
    public TcsResponseEntity<?> startScene(@PathVariable("sceneId") Long sceneId/*,
                                        @RequestBody Map<String,String> params*/) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);
        getService().loadSceneDto(sceneDto);

        return new TcsResponseEntity<Object>(TcsResponseEntity.Status.SUCCESS);
    }

    @RequestMapping(value = "/scenes/{sceneId}/actions/stop"/*, method = RequestMethod.POST*/)
    public TcsResponseEntity<?> stopScene(@PathVariable("sceneId") Long sceneId/*,
                                       @RequestBody Map<String,String> params*/) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);
        getService().unloadSceneDto(sceneDto);

        return new TcsResponseEntity<Object>(TcsResponseEntity.Status.SUCCESS);
    }
}
