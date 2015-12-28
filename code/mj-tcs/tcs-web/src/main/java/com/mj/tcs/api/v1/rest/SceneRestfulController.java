package com.mj.tcs.api.v1.rest;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.util.TCSDtoUtils;
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
    public TCSResponseEntity<?> getAllScenesProfile() {
        Collection<SceneDto> sceneDtos = getService().findAllScene();
        if (sceneDtos == null || sceneDtos.size() == 0) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.INFORMATION, "No Content");
        }

        List<Map<String, String>> sceneDtosProfile = new ArrayList<>();
        for (SceneDto sceneDto : sceneDtos) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("id", sceneDto.getId().toString());
            item.put("name", sceneDto.getName());
            item.put("status", getService().isSceneDtoRunning(sceneDto) ? "running" : "stopped");
            sceneDtosProfile.add(item);
        }

        return new TCSResponseEntity<>(
                TCSResponseEntity.Status.SUCCESS,
                sceneDtosProfile);
    }

    ///////////////// MODELLING /////////////////////
    @RequestMapping(value = "/scenes", method = RequestMethod.GET)
    public TCSResponseEntity<?> getAllScenes() {
        Collection<SceneDto> sceneDtos = getService().findAllScene();
        if (sceneDtos == null || sceneDtos.size() == 0) {
            return new TCSResponseEntity<>(
                    TCSResponseEntity.Status.INFORMATION).setStatusMessage("No Content");
        }

        return new TCSResponseEntity<>(
                TCSResponseEntity.Status.SUCCESS,
                sceneDtos);
    }

    @RequestMapping(value = "/scenes", method = RequestMethod.POST)
    public TCSResponseEntity<?> createScene(@RequestBody SceneDto sceneDto) {
        SceneDto newSceneDto = TCSDtoUtils.resolveSceneDtoRelationships(sceneDto);

        // Creating new scene
        newSceneDto = getService().createScene(newSceneDto);

        return new TCSResponseEntity<>(
                TCSResponseEntity.Status.SUCCESS,
                newSceneDto);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.GET)
    public TCSResponseEntity<?> getOneScene(@PathVariable("sceneId") Long sceneId) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ALERT).setStatusMessage("Not Found!");
        }

        return new TCSResponseEntity<>(
                TCSResponseEntity.Status.SUCCESS,
                sceneDto);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PUT)
    public TCSResponseEntity<?> updateScene(@PathVariable("sceneId") Long sceneId, SceneDto newSceneDto) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage("The scene can not be found by ID [" + sceneId + "].");
        }

        newSceneDto.setId(sceneId);
        if (getService().isSceneDtoRunning(sceneDto)) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage("The scene is running, please stop it first!");
        }
        TCSDtoUtils.copyProperties(sceneDto, newSceneDto);

        sceneDto = getService().updateScene(sceneDto);

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, sceneDto);
    }

//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PATCH)
//    public TCSResponseEntity<?> updateScenePartial(@PathVariable("sceneId") Long sceneId, EntityAuditorDto entityAuditorDto) {
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
//        return new TCSResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
//                HttpStatus.OK);
//    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.DELETE)
    public TCSResponseEntity<?> deleteScene(@PathVariable("sceneId") Long sceneId) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ALERT)
                    .setStatusMessage("The scene can not be found by ID [" + sceneId + "].");
        }

        if (getService().isSceneDtoRunning(sceneDto)) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage("The scene is running, please stop it first!");
        }

        try {
            getService().deleteScene(sceneId);
        } catch (EmptyResultDataAccessException e) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ALERT).setStatusMessage("Not found!");
        }

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    //////////////// ACTIONS ///////////////////////////
    @RequestMapping(value = "/scenes/{sceneId}/actions/start"/*, method = RequestMethod.POST*/)
    public TCSResponseEntity<?> startScene(@PathVariable("sceneId") Long sceneId/*,
                                        @RequestBody Map<String,String> params*/) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);
        getService().loadSceneDto(sceneDto);

        return new TCSResponseEntity<Object>(TCSResponseEntity.Status.SUCCESS);
    }

    @RequestMapping(value = "/scenes/{sceneId}/actions/stop"/*, method = RequestMethod.POST*/)
    public TCSResponseEntity<?> stopScene(@PathVariable("sceneId") Long sceneId/*,
                                       @RequestBody Map<String,String> params*/) {
        SceneDto sceneDto = getService().getSceneDto(sceneId);
        getService().unloadSceneDto(sceneDto);

        return new TCSResponseEntity<Object>(TCSResponseEntity.Status.SUCCESS);
    }
}
