package com.mj.tcs.api.v1.web;

/**
 * @author Wang Zhen
 */
//@RestController
//@ExposesResourceFor(VehicleController.class)
//@RequestMapping({"/api/v1", ""})
public class VehicleController extends ServiceController {
//
//    @Autowired
//    @Qualifier(value = "VehicleDtoConverter")
//    private DtoConverter dtoConverter;
//
//    @Autowired
//    private EntityLinks entityLinks;
//
//    @RequestMapping(value = "/vehicles", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllVehicles() {
//        Collection<Vehicle> vehicleEntities = getModellingService().getAllVehicles();
//        if (vehicleEntities == null || vehicleEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<VehicleDto> vehicleDtos = vehicleEntities.stream()
//                .map(item -> (VehicleDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new VehicleDtoResourceAssembler().toResources(vehicleDtos)
//                ),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/vehicles", method = RequestMethod.POST)
//    public ResponseEntity<?> createVehicle(@RequestBody VehicleDto vehicleDto) throws ObjectUnknownException{
//        return createVehicle(-1L, vehicleDto);
//    }
//
//    @RequestMapping(value = "/vehicles/{vehicleId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOneVehicle(@PathVariable("vehicleId") Long vehicleId) {
//        Objects.requireNonNull(getModellingService().getVehicle(vehicleId),
//                "vehicle is null by id: " + vehicleId);
//
//        Vehicle vehicle = getModellingService().getVehicle(vehicleId);
//
//        if (vehicle == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource(
//                        (VehicleDto) dtoConverter.convertToDto(vehicle)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/vehicles/{vehicleId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updateVehicle(@PathVariable("vehicleId") Long vehicleId,
//                                           VehicleDto vehicleDto) {
//        Objects.requireNonNull(getModellingService().getVehicle(vehicleId),
//                "vehicle is null by id: " + vehicleId);
//
//        VehicleDto dto = Objects.requireNonNull(vehicleDto, "vehicleDto is null");
//        Vehicle entity = (Vehicle)dtoConverter.convertToEntity(dto);
//
//        entity = getModellingService().updateVehicle(entity);
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource(
//                        (VehicleDto) dtoConverter.convertToDto(entity)),
//                HttpStatus.SUCCESS);
//    }
//
//
//    @RequestMapping(value = "/vehicles/{vehicleId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateVehiclePartial(@PathVariable("vehicleId") Long vehicleId,
//                                                  EntityAuditorDto entityAuditorDto) {
//        Objects.requireNonNull(getModellingService().getVehicle(vehicleId),
//                "vehicle is null by id: " + vehicleId);
//
//        Vehicle vehicle = getModellingService().getVehicle(vehicleId);
//        vehicle = (Vehicle) dtoConverter.mergePropertiesToEntity(vehicle, entityAuditorDto.getProperties());
//
//        vehicle = getModellingService().updateVehicle(vehicle);
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource(
//                        (VehicleDto) dtoConverter.convertToDto(vehicle)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/vehicles/{vehicleId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deleteVehicle(@PathVariable("vehicleId") Long vehicleId) {
//        Vehicle vehicle = getModellingService().getVehicle(vehicleId);
//        if (vehicle != null) {
//            getModellingService().deleteVehicle(vehicle);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    /////////////////// SCENE PATHS //////////////////////
//
//    @RequestMapping(value = "/scenes/{sceneId}/vehicles", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllVehiclesFromOneScene(@PathVariable("sceneId") Long sceneId) {
//        Collection<Vehicle> vehicleEntities = getModellingService().getAllVehiclesFromScene(sceneId);
//        if (vehicleEntities == null || vehicleEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<VehicleDto> vehicleDtos = vehicleEntities.stream()
//                .map(item -> (VehicleDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new VehicleDtoResourceAssembler().toResources(vehicleDtos)
//                ),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/vehicles", method = RequestMethod.POST)
//    public ResponseEntity<?> createVehicle(@PathVariable("sceneId") Long sceneId,
//                                         @RequestBody VehicleDto vehicleDto) throws ObjectUnknownException{
//        Scene scene = null;
//        if (sceneId >= 0) {
//            scene = Objects.requireNonNull(getModellingService().getSceneDto(sceneId),
//                    "scene is null by the sceneId: " + sceneId);
//        }
//
//        Vehicle newVehicle = (Vehicle) dtoConverter.convertToEntity(vehicleDto);
//        newVehicle.setSceneDto(scene);
//        Point currentPoint = vehicleDto.getCurrentPositionPointId() < 0 ?
//                null : getModellingService().getPoint(vehicleDto.getCurrentPositionPointId());
//        Point nextPoint = vehicleDto.getNextPositionPointId() < 0 ?
//                null : getModellingService().getPoint(vehicleDto.getNextPositionPointId());
//
//        if (currentPoint != null && nextPoint != null &&
//                currentPoint.getId() == nextPoint.getId()) {
//            throw new TcsServerRuntimeException("The current point is the same as the next point of the vehicle.");
//        }
//        newVehicle.setInitialPoint(currentPoint);
//        newVehicle.setNextPosition(nextPoint);
//
//        newVehicle.clearId();
//
//        newVehicle = getModellingService().createVehicle(newVehicle);
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource((VehicleDto) dtoConverter.convertToDto(newVehicle)),
//                HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/vehicles/{vehicleId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOneVehicle(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("vehicleId") Long vehicleId) {
//        checkAccessViolation(sceneId, vehicleId);
//
//        Vehicle vehicle = getModellingService().getVehicle(vehicleId);
//
//        if (vehicle == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource(
//                        (VehicleDto) dtoConverter.convertToDto(vehicle)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/vehicles/{vehicleId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updateVehicle(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("vehicleId") Long vehicleId,
//                                         VehicleDto vehicleDto) {
//        checkAccessViolation(sceneId, vehicleId);
//
//        VehicleDto dto = Objects.requireNonNull(vehicleDto, "vehicleDto is null");
//        Vehicle entity = (Vehicle)dtoConverter.convertToEntity(dto);
//
//        entity = getModellingService().updateVehicle(entity);
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource(
//                        (VehicleDto) dtoConverter.convertToDto(entity)),
//                HttpStatus.SUCCESS);
//    }
//
//
//    @RequestMapping(value = "/scenes/{sceneId}/vehicles/{vehicleId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateVehiclePartial(@PathVariable("sceneId") Long sceneId,
//                                                @PathVariable("vehicleId") Long vehicleId,
//                                                EntityAuditorDto entityAuditorDto) {
//        checkAccessViolation(sceneId, vehicleId);
//
//        Vehicle vehicle = getModellingService().getVehicle(vehicleId);
//        vehicle = (Vehicle) dtoConverter.mergePropertiesToEntity(vehicle, entityAuditorDto.getProperties());
//
//        vehicle = getModellingService().updateVehicle(vehicle);
//
//        return new ResponseEntity<>(
//                new VehicleDtoResourceAssembler().toResource(
//                        (VehicleDto) dtoConverter.convertToDto(vehicle)),
//                HttpStatus.SUCCESS);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/vehicles/{vehicleId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deleteVehicle(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("vehicleId") Long vehicleId) {
//        checkAccessViolation(sceneId, vehicleId);
//
//        Vehicle vehicle = Objects.requireNonNull(getModellingService().getVehicle(vehicleId),
//                "vehicle is null by vehicleId: " + vehicleId);
//
//        getModellingService().deleteVehicle(vehicle);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private void checkAccessViolation(Long sceneId, Long vehicleId) {
//        Vehicle vehicle = Objects.requireNonNull(getModellingService().getVehicle(vehicleId),
//                "vehicle is null by id: " + vehicleId);
//
//        if (vehicle != null && vehicle.getSceneDto().getId() != sceneId) {
//            throw new ObjectAccessViolationException(sceneId, vehicleId);
//        }
//    }
}
