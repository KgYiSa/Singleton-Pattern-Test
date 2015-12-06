package com.mj.tcs.api.v1.rest;

import com.mj.tcs.api.v1.dto.BlockDto;
import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.ResourceUnknownException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/scenes", method = RequestMethod.GET)
    public ResponseEntity<?> getAllScenes() {
        Collection<SceneDto> sceneDtos = getModellingService().getAllScenes();
        if (sceneDtos == null || sceneDtos.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity<>(
                sceneDtos,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getAllScenesProfile() {
        Collection<SceneDto> sceneDtos = getModellingService().getAllScenes();
        if (sceneDtos == null || sceneDtos.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Map<String, String>> sceneDtosProfile = new ArrayList<>();
        for (SceneDto sceneDto : sceneDtos) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("id", sceneDto.getId().toString());
            item.put("name", sceneDto.getName());
            sceneDtosProfile.add(item);
        }

        return new ResponseEntity<>(
                sceneDtosProfile,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes", method = RequestMethod.POST)
    public ResponseEntity<?> createScene(@RequestBody SceneDto sceneDto) {
        SceneDto newSceneDto = resolveRelationships(sceneDto);


        // Creating new scene
        newSceneDto = getModellingService().createScene(newSceneDto);

        return new ResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(newSceneDto)),
                newSceneDto,
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOneScene(@PathVariable("sceneId") Long sceneId) {
        SceneDto sceneDto = getModellingService().getSceneDto(sceneId);

        if (sceneDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(sceneDto)),
                sceneDto,
                HttpStatus.OK);
    }

//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updateScene(@PathVariable("sceneId") Long sceneId, SceneDto sceneDto) {
////        sceneDto.setId(sceneId);
//
//        if (sceneDto.getState() == null ||
//            (!"OPERATING".equals(sceneDto.getState())) &&
//            (!"MODELLING".equals(sceneDto.getState())) &&
//            (!"UNKNOWN".equals(sceneDto.getState()))) {
//            throw new TcsServerRuntimeException("upsupported scene state: " + sceneDto.getState() +
//                "when put new scene.");
//        }
//
//        // OPTION 1
//        // TODO: Check why PUT arguments are null ?
////        Map<String, Object> resourceMap = new BeanMap(sceneDto);
//        SceneDto scene = getModellingService().getSceneDto(sceneId);
////        dtoConverter.mergePropertiesToEntity(scene, resMap);
//
//        // OPTION 2 (RECOMMEND)
//        SceneDto scene = (SceneDto)dtoConverter.convertToEntity(sceneDto);
//
//        getModellingService().updateScene(scene);
//
//        return new ResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateScenePartial(@PathVariable("sceneId") Long sceneId, EntityAuditorDto entityAuditorDto) {
//        entityAuditorDto.setId(sceneId);
//
//        SceneDto scene = getModellingService().getSceneDto(sceneId);
//        // TODO: find a way to found which attribute should be updated!!! (merge new attributes (Map) to the current valueconverter)
//        scene = (SceneDto) dtoConverter.mergePropertiesToEntity(scene, entityAuditorDto.getProperties());
//
//        // TODO: Should update the updated time properties?
//
//        // save
//        getModellingService().updateScene(scene);
//
//        return new ResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
//                HttpStatus.OK);
//    }

    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteScene(@PathVariable("sceneId") Long sceneId) {
        try {
            getModellingService().deleteScene(sceneId);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
            });
        }
        if (sceneDto.getPathDtos() != null) {
            sceneDto.getPathDtos().forEach(p -> {
                p.setSceneDto(sceneDto);

                // CHECK RELATIONSHIP (Already linked in points' settings) TODO?
                if (p.getSourcePointDto() == null || p.getDestinationPointDto()  == null) {
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

                if (v.getCurrentPosition() != null) {
                    v.setCurrentPosition(sceneDto.getPointDtoByUUID(v.getCurrentPosition().getUUID()));
                }
            });
        }

        return sceneDto;
    }
}
