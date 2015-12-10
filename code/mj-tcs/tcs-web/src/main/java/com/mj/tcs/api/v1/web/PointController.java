package com.mj.tcs.api.v1.web;

/**
 * @author Wang Zhen
 */
//@RestController
//@ExposesResourceFor(PointController.class)
//@RequestMapping({"/api/v1", ""})
public class PointController extends ServiceController {

//    @Autowired
//    @Qualifier(value = "PointDtoConverter")
//    private DtoConverter dtoConverter;
//
//    @Autowired
//    private EntityLinks entityLinks;
//
//    @RequestMapping(value = "/scenes/{sceneId}/points", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllPoints(@PathVariable("sceneId") Long sceneId) {
//        Collection<Point> pointEntities = getModellingService().getAllPointsFromScene(sceneId);
//        if (pointEntities == null || pointEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<PointDto> pointDtos = pointEntities.stream()
//                .map(item -> (PointDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new PointDtoResourceAssembler().toResources(pointDtos)
//                ),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/points", method = RequestMethod.POST)
//    public ResponseEntity<?> createPoint(@PathVariable("sceneId") Long sceneId,
//                                         @RequestBody PointDto pointDto) throws ObjectUnknownException{
//        Scene scene = Objects.requireNonNull(getModellingService().getSceneDto(sceneId),
//            "scene is null by the sceneId: " + sceneId);
//
//        Point newPoint = (Point) dtoConverter.convertToEntity(pointDto);
//        newPoint.setSceneDto(scene);
//
//        newPoint.clearId();
//
//        newPoint = getModellingService().createPoint(newPoint);
//
//
//        return new ResponseEntity<>(
//                new PointDtoResourceAssembler().toResource((PointDto) dtoConverter.convertToDto(newPoint)),
//                HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOnePoint(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("pointId") Long pointId) {
//        checkAccessViolation(sceneId, pointId);
//
//        Point point = getModellingService().getPoint(pointId);
//
//        if (point == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new PointDtoResourceAssembler().toResource(
//                        (PointDto) dtoConverter.convertToDto(point)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updatePoint(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("pointId") Long pointId,
//                                         PointDto pointDto) {
//        checkAccessViolation(sceneId, pointId);
//
//        PointDto dto = Objects.requireNonNull(pointDto, "pointDto is null");
//        Point entity = (Point)dtoConverter.convertToEntity(dto);
//
//        entity = getModellingService().updatePoint(entity);
//
//        return new ResponseEntity<>(
//                new PointDtoResourceAssembler().toResource(
//                        (PointDto) dtoConverter.convertToDto(entity)),
//                HttpStatus.SUCCESS);
//    }
//
//
//    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updatePointPartial(@PathVariable("sceneId") Long sceneId,
//                                                @PathVariable("pointId") Long pointId,
//                                                EntityAuditorDto entityAuditorDto) {
//        checkAccessViolation(sceneId, pointId);
//
//        Point point = getModellingService().getPoint(pointId);
//        point = (Point) dtoConverter.mergePropertiesToEntity(point, entityAuditorDto.getProperties());
//
//        point = getModellingService().updatePoint(point);
//
//        return new ResponseEntity<>(
//                new PointDtoResourceAssembler().toResource(
//                        (PointDto) dtoConverter.convertToDto(point)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deletePoint(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("pointId") Long pointId) {
//        checkAccessViolation(sceneId, pointId);
//
//        Point point = getModellingService().getPoint(pointId);
//        if (point != null) {
//            getModellingService().deletePoint(point);
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private void checkAccessViolation(Long sceneId, Long pointId) {
//        Point point = Objects.requireNonNull(getModellingService().getPoint(pointId),
//                "point is null by id: " + pointId);
//
//        if (point.getSceneDto().getId() != sceneId) {
//            throw new ObjectAccessViolationException(sceneId, pointId);
//        }
//    }
}
