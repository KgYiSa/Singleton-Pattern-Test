package com.mj.tcs.api.v1.web;

/**
 * @author Wang Zhen
 */
//@RestController
//@ExposesResourceFor(PathController.class)
//@RequestMapping({"/api/v1", ""})
public class PathController extends ServiceController {
//
//    @Autowired
//    @Qualifier(value = "PathDtoConverter")
//    private DtoConverter dtoConverter;
//
//    @Autowired
//    private EntityLinks entityLinks;
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllPaths(@PathVariable("sceneId") Long sceneId) {
//        Collection<Path> pathEntities = getModellingService().getAllPathsFromScene(sceneId);
//        if (pathEntities == null || pathEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<PathDto> pathDtos = pathEntities.stream()
//                .map(item -> (PathDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new PathDtoResourceAssembler().toResources(pathDtos)
//                ),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths", method = RequestMethod.POST)
//    public ResponseEntity<?> createPath(@PathVariable("sceneId") Long sceneId,
//                                         @RequestBody PathDto pathDto) throws ObjectUnknownException{
//        Scene scene = Objects.requireNonNull(getModellingService().getSceneDto(sceneId),
//            "scene is null by the sceneId: " + sceneId);
//
//        Path newPath = (Path) dtoConverter.convertToEntity(pathDto);
//        newPath.setSceneDto(scene);
//        Point srcPoint = getModellingService().getPoint(pathDto.getSourcePointDto());
//        Point dstPoint = getModellingService().getPoint(pathDto.getDestinationPointDto());
//        Objects.requireNonNull(srcPoint, "Source Point is null by id " + pathDto.getSourcePointDto());
//        Objects.requireNonNull(dstPoint, "Destination point is null by id " + pathDto.getDestinationPointDto());
//        if (pathDto.getSourcePointDto() == pathDto.getDestinationPointDto()) {
//            throw new TcsServerRuntimeException("The source point is the same as the destination point of the path.");
//        }
//        newPath.setSourcePoint(srcPoint);
//        newPath.setDestinationPoint(dstPoint);
//
//        newPath.clearId();
//
//        newPath = getModellingService().createPath(newPath);
//
//        return new ResponseEntity<>(
//                new PathDtoResourceAssembler().toResource((PathDto) dtoConverter.convertToDto(newPath)),
//                HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths/{pathId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOnePath(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("pathId") Long pathId) {
//        checkAccessViolation(sceneId, pathId);
//
//        Path path = getModellingService().getPath(pathId);
//
//        if (path == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new PathDtoResourceAssembler().toResource(
//                        (PathDto) dtoConverter.convertToDto(path)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths/{pathId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updatePath(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("pathId") Long pathId,
//                                         PathDto pathDto) {
//        checkAccessViolation(sceneId, pathId);
//
//        PathDto dto = Objects.requireNonNull(pathDto, "pathDto is null");
//        Path entity = (Path)dtoConverter.convertToEntity(dto);
//
//        entity = getModellingService().updatePath(entity);
//
//        return new ResponseEntity<>(
//                new PathDtoResourceAssembler().toResource(
//                        (PathDto) dtoConverter.convertToDto(entity)),
//                HttpStatus.SUCCESS);
//    }
//
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths/{pathId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updatePathPartial(@PathVariable("sceneId") Long sceneId,
//                                                @PathVariable("pathId") Long pathId,
//                                                EntityAuditorDto entityAuditorDto) {
//        checkAccessViolation(sceneId, pathId);
//
//        Path path = getModellingService().getPath(pathId);
//        path = (Path) dtoConverter.mergePropertiesToEntity(path, entityAuditorDto.getProperties());
//
//        path = getModellingService().updatePath(path);
//
//        return new ResponseEntity<>(
//                new PathDtoResourceAssembler().toResource(
//                        (PathDto) dtoConverter.convertToDto(path)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths/{pathId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deletePath(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("pathId") Long pathId) {
//        checkAccessViolation(sceneId, pathId);
//
//        Path path = getModellingService().getPath(pathId);
//        if (path != null) {
//            getModellingService().deletePath(path);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private void checkAccessViolation(Long sceneId, Long pathId) {
//        Path path = Objects.requireNonNull(getModellingService().getPath(pathId),
//                "path is null by id: " + pathId);
//
//        if (path.getSceneDto().getId() != sceneId) {
//            throw new ObjectAccessViolationException(sceneId, pathId);
//        }
//    }
}
