package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.EntityAuditDto;
import com.mj.tcs.api.v1.dto.converter.DtoConverter;
import com.mj.tcs.api.v1.dto.resource.SceneDtoResourceAssembler;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.exception.TcsServerRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class maintains the complete static topology of a scene model
 * i.e. Points, Paths, Blocks, Groups, Locations, LocationDto Types,  Static Routes, Vehicles etc.
 *
 * @author Wang Zhen
 */
@RestController
@ExposesResourceFor(Scene.class)
@RequestMapping("/api/v1")
public class SceneController extends ServiceController {
    @Autowired
    @Qualifier(value = "SceneDtoConverter")
    private DtoConverter dtoConverter;// = new DtoConverterImp();

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/scenes", method = RequestMethod.GET)
    public ResponseEntity<?> getAllScenes() {
        Collection<Scene> sceneEntities = getModellingService().getAllScenes();
        if (sceneEntities == null || sceneEntities.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<SceneDto> sceneDtos = sceneEntities.stream()
                .map(item -> (SceneDto) dtoConverter.convertToDto(item))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new Resources<>(
                        new SceneDtoResourceAssembler().toResources(sceneDtos)
                ),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes", method = RequestMethod.POST)
    public ResponseEntity<?> createScene(@RequestBody SceneDto sceneDto) {
        Scene scene = null;

        synchronized (sceneDto) {
            scene = (Scene) dtoConverter.convertToEntity(sceneDto);
        }

        if (scene == null) {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }

        scene.clearId();

        // Creating new scene
        scene = getModellingService().createScene(scene);

        return new ResponseEntity<>(
                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOneScene(@PathVariable("sceneId") Long sceneId) {
        Scene scene = getModellingService().getScene(sceneId);

        if (scene == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateScene(@PathVariable("sceneId") Long sceneId, SceneDto sceneDto) {
        sceneDto.setId(sceneId);

        if (sceneDto.getState() == null ||
            (!"OPERATING".equals(sceneDto.getState())) &&
            (!"MODELLING".equals(sceneDto.getState())) &&
            (!"UNKNOWN".equals(sceneDto.getState()))) {
            throw new TcsServerRuntimeException("upsupported scene state: " + sceneDto.getState() +
                "when put new scene.");
        }

        // OPTION 1
        // TODO: Check why PUT arguments are null ?
//        Map<String, Object> resourceMap = new BeanMap(sceneDto);
//        Scene scene = getModellingService().getScene(sceneId);
//        dtoConverter.mergePropertiesToEntity(scene, resMap);

        // OPTION 2 (RECOMMEND)
        Scene scene = (Scene)dtoConverter.convertToEntity(sceneDto);

        getModellingService().updateScene(scene);

        return new ResponseEntity<>(
                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateScenePartial(@PathVariable("sceneId") Long sceneId, EntityAuditDto entityAuditDto) {
        entityAuditDto.setId(sceneId);

        Scene scene = getModellingService().getScene(sceneId);
        // TODO: find a way to found which attribute should be updated!!! (merge new attributes (Map) to the current valueconverter)
        scene = (Scene) dtoConverter.mergePropertiesToEntity(scene, entityAuditDto.getProperties());

        // TODO: Should update the updated time properties?

        // save
        getModellingService().updateScene(scene);

        return new ResponseEntity<>(
                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteScene(@PathVariable("sceneId") Long sceneId) {
        getModellingService().deleteScene(sceneId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
