package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.api.v1.dto.converter.DtoConverter;
import com.mj.tcs.api.v1.dto.resource.PointDtoResourceAssembler;
import com.mj.tcs.exception.ObjectAccessViolationException;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.data.model.Point;
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
//@ExposesResourceFor(PointController.class)
@RequestMapping("/api/v1")
public class PointController extends ServiceController {

    @Autowired
    @Qualifier(value = "PointDtoConverter")
    private DtoConverter dtoConverter;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/scenes/{sceneId}/points", method = RequestMethod.GET)
    public ResponseEntity<?> getAllPoints(@PathVariable("sceneId") Long sceneId) {
        Collection<Point> pointEntities = getModellingService().getAllPointsFromScene(sceneId);
        if (pointEntities == null || pointEntities.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<PointDto> pointDtos = pointEntities.stream()
                .map(item -> (PointDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());

        return new ResponseEntity<>(
                new Resources<>(
                        new PointDtoResourceAssembler().toResources(pointDtos)
                ),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/points", method = RequestMethod.POST)
    public ResponseEntity<?> createPoint(@PathVariable("sceneId") Long sceneId,
                                         @RequestBody PointDto pointDto) throws ObjectUnknownException{
        Scene scene = Objects.requireNonNull(getModellingService().getScene(sceneId),
            "scene is null by the sceneId: " + sceneId);

        Point newPoint = (Point) dtoConverter.convertToEntity(pointDto);
        newPoint.setScene(scene);

        newPoint.clearId();

        newPoint = getModellingService().createPoint(newPoint);


        return new ResponseEntity<>(
                new PointDtoResourceAssembler().toResource((PointDto) dtoConverter.convertToDto(newPoint)),
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOnePoint(@PathVariable("sceneId") Long sceneId,
                                         @PathVariable("pointId") Long pointId) {
        checkAccessViolation(sceneId, pointId);

        Point point = getModellingService().getPoint(pointId);

        if (point == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new PointDtoResourceAssembler().toResource(
                        (PointDto) dtoConverter.convertToDto(point)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePoint(@PathVariable("sceneId") Long sceneId,
                                         @PathVariable("pointId") Long pointId,
                                         PointDto pointDto) {
        checkAccessViolation(sceneId, pointId);

        PointDto dto = Objects.requireNonNull(pointDto, "pointDto is null");
        Point entity = (Point)dtoConverter.convertToEntity(dto);

        entity = getModellingService().updatePoint(entity);

        return new ResponseEntity<>(
                new PointDtoResourceAssembler().toResource(
                        (PointDto) dtoConverter.convertToDto(entity)),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updatePointPartial(@PathVariable("sceneId") Long sceneId,
                                                @PathVariable("pointId") Long pointId,
                                                BaseEntityAuditDto baseEntityAuditDto) {
        checkAccessViolation(sceneId, pointId);

        Point point = getModellingService().getPoint(pointId);
        point = (Point) dtoConverter.mergePropertiesToEntity(point, baseEntityAuditDto.getProperties());

        point = getModellingService().updatePoint(point);

        return new ResponseEntity<>(
                new PointDtoResourceAssembler().toResource(
                        (PointDto) dtoConverter.convertToDto(point)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/points/{pointId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePoint(@PathVariable("sceneId") Long sceneId,
                                         @PathVariable("pointId") Long pointId) {
        checkAccessViolation(sceneId, pointId);

        Point point = getModellingService().getPoint(pointId);
        if (point != null) {
            getModellingService().deletePoint(point);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkAccessViolation(Long sceneId, Long pointId) {
        Point point = Objects.requireNonNull(getModellingService().getPoint(pointId),
                "point is null by id: " + pointId);

        if (point.getScene().getId() != sceneId) {
            throw new ObjectAccessViolationException(sceneId, pointId);
        }
    }
}
