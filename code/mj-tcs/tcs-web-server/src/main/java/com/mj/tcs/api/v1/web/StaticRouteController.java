package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.StaticRouteDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityAuditDto;
import com.mj.tcs.api.v1.dto.converter.DtoConverter;
import com.mj.tcs.api.v1.dto.resource.StaticRouteDtoResourceAssembler;
import com.mj.tcs.exception.ObjectAccessViolationException;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.model.StaticRoute;
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
@ExposesResourceFor(StaticRouteController.class)
@RequestMapping("/api/v1")
public class StaticRouteController extends ServiceController {

    @Autowired
    @Qualifier(value = "StaticRouteDtoConverter")
    private DtoConverter dtoConverter;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/scenes/{sceneId}/static_routes", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStaticRouteDtos(@PathVariable("sceneId") Long sceneId) {
        Collection<StaticRoute> staticRouteEntities = getModellingService().getAllStaticRoutesFromScene(sceneId);
        if (staticRouteEntities == null || staticRouteEntities.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<StaticRouteDto> staticRouteDtos = staticRouteEntities.stream()
                .map(item -> (StaticRouteDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());

        return new ResponseEntity<>(
                new Resources<>(
                        new StaticRouteDtoResourceAssembler().toResources(staticRouteDtos)
                ),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/static_routes", method = RequestMethod.POST)
    public ResponseEntity<?> createStaticRouteDto(@PathVariable("sceneId") Long sceneId,
                                        @RequestBody StaticRouteDto staticRouteDto) throws ObjectUnknownException {
        Scene scene = Objects.requireNonNull(getModellingService().getScene(sceneId),
                "scene is null by the sceneId: " + sceneId);

        if (!staticRouteDto.isValid()) {
            throw new TcsServerRuntimeException("The length of the point sequence is too small ( < 2)");
        }

        StaticRoute newStaticRoute = (StaticRoute) dtoConverter.convertToEntity(staticRouteDto);
        newStaticRoute.setScene(scene);

        // check hops
        for (long pointId : staticRouteDto.getHops()) {
            Point point = getModellingService().getPoint(pointId);
            Objects.requireNonNull(point, "Point of the static route is null by id " + pointId);
        }

        newStaticRoute.clearId();

        newStaticRoute = getModellingService().createStaticRoute(newStaticRoute);

        return new ResponseEntity<>(
                new StaticRouteDtoResourceAssembler().toResource((StaticRouteDto) dtoConverter.convertToDto(newStaticRoute)),
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/scenes/{sceneId}/static_routes/{staticRouteId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOneStaticRouteDto(@PathVariable("sceneId") Long sceneId,
                                        @PathVariable("staticRouteId") Long staticRouteId) {
        checkAccessViolation(sceneId, staticRouteId);

        StaticRoute staticRoute = getModellingService().getStaticRoute(staticRouteId);

        if (staticRoute == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new StaticRouteDtoResourceAssembler().toResource(
                        (StaticRouteDto) dtoConverter.convertToDto(staticRoute)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/static_routes/{staticRouteId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStaticRoute(@PathVariable("sceneId") Long sceneId,
                                        @PathVariable("staticRouteId") Long staticRouteId,
                                        StaticRouteDto staticRouteDto) {
        checkAccessViolation(sceneId, staticRouteId);

        StaticRouteDto dto = Objects.requireNonNull(staticRouteDto, "staticRouteDto is null");
        StaticRoute entity = (StaticRoute)dtoConverter.convertToEntity(dto);

        entity = getModellingService().updateStaticRoute(entity);

        return new ResponseEntity<>(
                new StaticRouteDtoResourceAssembler().toResource(
                        (StaticRouteDto) dtoConverter.convertToDto(entity)),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/scenes/{sceneId}/static_routes/{staticRouteId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateStaticRouteDtoPartial(@PathVariable("sceneId") Long sceneId,
                                               @PathVariable("staticRouteId") Long staticRouteId,
                                               BaseEntityAuditDto baseEntityAuditDto) {
        checkAccessViolation(sceneId, staticRouteId);

        StaticRoute staticRoute = getModellingService().getStaticRoute(staticRouteId);
        staticRoute = (StaticRoute) dtoConverter.mergePropertiesToEntity(staticRoute, baseEntityAuditDto.getProperties());

        staticRoute = getModellingService().updateStaticRoute(staticRoute);

        return new ResponseEntity<>(
                new StaticRouteDtoResourceAssembler().toResource(
                        (StaticRouteDto) dtoConverter.convertToDto(staticRoute)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/static_routes/{staticRouteId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStaticRoute(@PathVariable("sceneId") Long sceneId,
                                        @PathVariable("staticRouteId") Long staticRouteId) {
        checkAccessViolation(sceneId, staticRouteId);

        StaticRoute staticRoute = getModellingService().getStaticRoute(staticRouteId);
        if (staticRoute != null) {
            getModellingService().deleteStaticRoute(staticRoute);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkAccessViolation(Long sceneId, Long staticRouteId) {
        StaticRoute staticRoute = Objects.requireNonNull(getModellingService().getStaticRoute(staticRouteId),
                "staticRoute is null by id: " + staticRouteId);

        if (staticRoute.getScene().getId() != sceneId) {
            throw new ObjectAccessViolationException(sceneId, staticRouteId);
        }
    }
}
