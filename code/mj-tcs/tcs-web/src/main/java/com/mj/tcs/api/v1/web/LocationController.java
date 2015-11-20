/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.web;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang Zhen
 */
@RestController
@ExposesResourceFor(LocationController.class)
@RequestMapping("/api/v1")
public class LocationController extends ServiceController {

//    @Autowired
//    @Qualifier(value = "LocationDtoConverter")
//    private DtoConverter dtoConverter;
//
//    @Autowired
//    private EntityLinks entityLinks;
//
//    @RequestMapping(value = "/scenes/{sceneId}/locations", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllLocations(@PathVariable("sceneId") Long sceneId) {
//        Collection<Location> locationEntities = getModellingService().getAllLocationsFromScene(sceneId);
//        if (locationEntities == null || locationEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<LocationDto> locationDtos = locationEntities.stream()
//                .map(item -> (LocationDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new LocationDtoResourceAssembler().toResources(locationDtos)
//                ),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/locations", method = RequestMethod.POST)
//    public ResponseEntity<?> createLocation(@PathVariable("sceneId") Long sceneId,
//                                         @RequestBody LocationDto locationDto) throws ObjectUnknownException{
//        Scene scene = Objects.requireNonNull(getModellingService().getSceneDto(sceneId),
//            "scene is null by the sceneId: " + sceneId);
//
//        Location newLocation = (Location) dtoConverter.convertToEntity(locationDto);
//        newLocation.setSceneDto(scene);
//        // location type
//        if (locationDto.getLocationType() != null) {
//            Long existedLocationTypeId = locationDto.getLocationType();
//            LocationType locationType;
//            if (existedLocationTypeId > 0) {
//                locationType = getModellingService().getLocationType(existedLocationTypeId);
//                if (locationType != null) {
//                    newLocation.setType(locationType);
//                    locationType.setSceneDto(scene);
//                } else {
//                    throw new TcsServerRuntimeException(String.format("Location Type Id [%d] of the location [%s] is not exist.",
//                            existedLocationTypeId, newLocation.getName()));
//                }
//            }
//        }
//        // location link
//        if (locationDto.getAttachedLinks() != null) {
//            // links
//            if (newLocation.getAttachedLinks() != null) {
//                for (Location.Link link : newLocation.getAttachedLinks()) {
//                    // location
//                    link.setLocationId(newLocation);
//
//                    // point
//                    Optional<LocationLinkDto> linkDto = locationDto.getAttachedLinkById(link.getId());
//                    if (!linkDto.isPresent()) {
//                        throw new TcsServerRuntimeException("LinkDto is null by Link Id " + link.getId() +
//                                " of location " + locationDto.getName());
//                    }
//                    Point linkedPoint = getModellingService().getPointDto(linkDto.get().getPointDto());
//                    Objects.requireNonNull(linkedPoint, "The linked point for the link[" + link.getName() + "] is null!");
//                    link.setPointDto(linkedPoint);
//                }
//            } else {
//                throw new TcsServerRuntimeException("Location Conversion error, the expected location_link is "
//                        + locationDto.getAttachedLinks().size()
//                        + ", but actually the size is "
//                        + newLocation.getAttachedLinks().size());
//            }
//        }
//
//        newLocation.clearId();
//
//        newLocation = getModellingService().createLocation(newLocation);
//
//        return new ResponseEntity<>(
//                new LocationDtoResourceAssembler().toResource((LocationDto) dtoConverter.convertToDto(newLocation)),
//                HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/locations/{locationId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOneLocation(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("locationId") Long locationId) {
//        checkAccessViolation(sceneId, locationId);
//
//        Location location = getModellingService().getLocationId(locationId);
//
//        if (location == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new LocationDtoResourceAssembler().toResource(
//                        (LocationDto) dtoConverter.convertToDto(location)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/locations/{locationId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updateLocation(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("locationId") Long locationId,
//                                         LocationDto locationDto) {
//        checkAccessViolation(sceneId, locationId);
//
//        LocationDto dto = Objects.requireNonNull(locationDto, "locationDto is null");
//        Location entity = (Location)dtoConverter.convertToEntity(dto);
//
//        entity = getModellingService().updateLocation(entity);
//
//        return new ResponseEntity<>(
//                new LocationDtoResourceAssembler().toResource(
//                        (LocationDto) dtoConverter.convertToDto(entity)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/locations/{locationId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateLocationPartial(@PathVariable("sceneId") Long sceneId,
//                                                @PathVariable("locationId") Long locationId,
//                                                EntityAuditorDto baseEntityAuditDto) {
//        checkAccessViolation(sceneId, locationId);
//
//        Location location = getModellingService().getLocationId(locationId);
//        location = (Location) dtoConverter.mergePropertiesToEntity(location, baseEntityAuditDto.getProperties());
//
//        location = getModellingService().updateLocation(location);
//
//        return new ResponseEntity<>(
//                new LocationDtoResourceAssembler().toResource(
//                        (LocationDto) dtoConverter.convertToDto(location)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/locations/{locationId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deleteLocation(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("locationId") Long locationId) {
//        checkAccessViolation(sceneId, locationId);
//
//        Location location = getModellingService().getLocationId(locationId);
//        if (location != null) {
//            getModellingService().deleteLocation(location);
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private void checkAccessViolation(Long sceneId, Long locationId) {
//        Location location = Objects.requireNonNull(getModellingService().getLocationId(locationId),
//                "location is null by id: " + locationId);
//
//        if (location.getSceneDto().getId() != sceneId) {
//            throw new ObjectAccessViolationException(sceneId, locationId);
//        }
//    }
}
