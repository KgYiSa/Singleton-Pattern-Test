package com.mj.tcs.api.v1.web;

/**
 * The class maintains the complete static topology of a scene model
 * i.e. Points, Paths, Blocks, Groups, Locations, LocationDto Types,  Static Routes, Vehicles etc.
 *
 * @author Wang Zhen
 */
//@RestController
//@ExposesResourceFor(SceneDto.class)
//@RequestMapping({"/api/v1", ""})
public class SceneController extends ServiceController {
//    @Autowired
//    @Qualifier(value = "SceneDtoConverter")
//    private DtoConverter dtoConverter;// = new DtoConverterImp();
//
//    @Autowired
//    private EntityLinks entityLinks;
//
//    @RequestMapping(value = "/scenes", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllScenes() {
//        Collection<Scene> sceneEntities = getModellingService().getAllScenes();
//        if (sceneEntities == null || sceneEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<SceneDto> sceneDtos = sceneEntities.stream()
//                .map(item -> (SceneDto) dtoConverter.convertToDto(item))
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new SceneDtoResourceAssembler().toResources(sceneDtos)
//                ),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes", method = RequestMethod.POST)
//    public ResponseEntity<?> createScene(@RequestBody SceneDto sceneDto) {
//        Scene scene = null;
//
//        synchronized (sceneDto) {
//            scene = (Scene) dtoConverter.convertToEntity(sceneDto);
//        }
//
//        if (scene == null) {
//            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
//        }
//
//        scene.clearId();
//
//        // Creating new scene
//        scene = getModellingService().createScene(scene);
//
//        return new ResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
//                HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOneScene(@PathVariable("sceneId") Long sceneId) {
//        Scene scene = getModellingService().getSceneDto(sceneId);
//
//        if (scene == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new SceneDtoResourceAssembler().toResource((SceneDto) dtoConverter.convertToDto(scene)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updateScene(@PathVariable("sceneId") Long sceneId, SceneDto sceneDto) {
//        sceneDto.setId(sceneId);
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
////        Scene scene = getModellingService().getSceneDto(sceneId);
////        dtoConverter.mergePropertiesToEntity(scene, resMap);
//
//        // OPTION 2 (RECOMMEND)
//        Scene scene = (Scene)dtoConverter.convertToEntity(sceneDto);
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
//        Scene scene = getModellingService().getSceneDto(sceneId);
//        // TODO: find a way to found which attribute should be updated!!! (merge new attributes (Map) to the current valueconverter)
//        scene = (Scene) dtoConverter.mergePropertiesToEntity(scene, entityAuditorDto.getProperties());
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
//
//    @RequestMapping(value = "/scenes/{sceneId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deleteScene(@PathVariable("sceneId") Long sceneId) {
//        getModellingService().deleteScene(sceneId);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
