package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.LocationTypeDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.api.v1.dto.converter.DtoConverter;
import com.mj.tcs.api.v1.dto.resource.LocationTypeDtoResourceAssembler;
import com.mj.tcs.exception.ObjectAccessViolationException;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.data.model.LocationType;
import com.mj.tcs.data.model.Scene;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@RestController
@ExposesResourceFor(LocationTypeController.class)
@RequestMapping("/api/v1")
public class LocationTypeController extends ServiceController {

    @Autowired
    @Qualifier(value = "LocationTypeDtoConverter")
    private DtoConverter dtoConverter;

    @Autowired
    private EntityLinks entityLinks;

//    @RequestMapping(value = "/scenes/{sceneId}/location_types", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllLocationTypes(@PathVariable("sceneId") Long sceneId) {
//        Collection<LocationType> locationTypeEntities = getModellingService().getAllLocationTypesFromScene(sceneId);
//        if (locationTypeEntities == null || locationTypeEntities.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<LocationTypeDto> locationTypeDtos = locationTypeEntities.stream()
//                .map(item -> (LocationTypeDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());
//
//        return new ResponseEntity<>(
//                new Resources<>(
//                        new LocationTypeDtoResourceAssembler().toResources(locationTypeDtos)
//                ),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/location_types", method = RequestMethod.POST)
//    public ResponseEntity<?> createLocationType(@PathVariable("sceneId") Long sceneId,
//                                         @RequestBody LocationTypeDto locationTypeDto) throws ObjectUnknownException{
//        Scene scene = Objects.requireNonNull(getModellingService().getScene(sceneId),
//            "scene is null by the sceneId: " + sceneId);
//
//        LocationType newLocationType = (LocationType) dtoConverter.convertToEntity(locationTypeDto);
//        newLocationType.setScene(scene);
//
//        newLocationType.setId(null);
//
//        newLocationType = getModellingService().createLocationType(newLocationType);
//
//
//        return new ResponseEntity<>(
//                new LocationTypeDtoResourceAssembler().toResource((LocationTypeDto) dtoConverter.convertToDto(newLocationType)),
//                HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/location_types/{locationTypeId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getOneLocationType(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("locationTypeId") Long locationTypeId) {
//        checkAccessViolation(sceneId, locationTypeId);
//
//        LocationType locationType = getModellingService().getLocationType(locationTypeId);
//
//        if (locationType == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new LocationTypeDtoResourceAssembler().toResource(
//                        (LocationTypeDto) dtoConverter.convertToDto(locationType)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/location_types/{locationTypeId}", method = RequestMethod.PUT)
//    public ResponseEntity<?> updateLocationType(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("locationTypeId") Long locationTypeId,
//                                         LocationTypeDto locationTypeDto) {
//        checkAccessViolation(sceneId, locationTypeId);
//
//        LocationTypeDto dto = Objects.requireNonNull(locationTypeDto, "locationTypeDto is null");
//        LocationType entity = (LocationType)dtoConverter.convertToEntity(dto);
//
//        entity = getModellingService().updateLocationType(entity);
//
//        return new ResponseEntity<>(
//                new LocationTypeDtoResourceAssembler().toResource(
//                        (LocationTypeDto) dtoConverter.convertToDto(entity)),
//                HttpStatus.OK);
//    }
//
//
//    @RequestMapping(value = "/scenes/{sceneId}/location_types/{locationTypeId}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> updateLocationTypePartial(@PathVariable("sceneId") Long sceneId,
//                                                @PathVariable("locationTypeId") Long locationTypeId,
//                                                BaseEntityAuditDto baseEntityAuditDto) {
//        checkAccessViolation(sceneId, locationTypeId);
//
//        LocationType locationType = getModellingService().getLocationType(locationTypeId);
//        locationType = (LocationType) dtoConverter.mergePropertiesToEntity(locationType, baseEntityAuditDto.getProperties());
//
//        locationType = getModellingService().updateLocationType(locationType);
//
//        return new ResponseEntity<>(
//                new LocationTypeDtoResourceAssembler().toResource(
//                        (LocationTypeDto) dtoConverter.convertToDto(locationType)),
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/location_types/{locationTypeId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deleteLocationType(@PathVariable("sceneId") Long sceneId,
//                                         @PathVariable("locationTypeId") Long locationTypeId) {
//        checkAccessViolation(sceneId, locationTypeId);
//
//        LocationType locationType = getModellingService().getLocationType(locationTypeId);
//        if (locationType != null) {
//            getModellingService().deleteLocationType(locationType);
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    private void checkAccessViolation(Long sceneId, Long locationTypeId) {
        LocationType locationType = Objects.requireNonNull(getModellingService().getLocationType(locationTypeId),
                "locationType is null by id: " + locationTypeId);

        if (locationType.getScene().getId() != sceneId) {
            throw new ObjectAccessViolationException(sceneId, locationTypeId);
        }
    }
}
