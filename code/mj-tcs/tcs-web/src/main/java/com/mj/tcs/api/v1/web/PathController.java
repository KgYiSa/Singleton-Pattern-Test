package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.base.EntityAuditorDto;
import com.mj.tcs.api.v1.dto.converter.DtoConverter;
import com.mj.tcs.api.v1.dto.resource.PathDtoResourceAssembler;
import com.mj.tcs.exception.ObjectAccessViolationException;
import com.mj.tcs.exception.ObjectUnknownException;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@RestController
@ExposesResourceFor(PathController.class)
@RequestMapping("/api/v1")
public class PathController extends ServiceController {

    @Autowired
    @Qualifier(value = "PathDtoConverter")
    private DtoConverter dtoConverter;

    @Autowired
    private EntityLinks entityLinks;

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
//                HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/scenes/{sceneId}/paths", method = RequestMethod.POST)
//    public ResponseEntity<?> createPath(@PathVariable("sceneId") Long sceneId,
//                                         @RequestBody PathDto pathDto) throws ObjectUnknownException{
//        Scene scene = Objects.requireNonNull(getModellingService().getScene(sceneId),
//            "scene is null by the sceneId: " + sceneId);
//
//        Path newPath = (Path) dtoConverter.convertToEntity(pathDto);
//        newPath.setScene(scene);
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
//                HttpStatus.OK);
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
//                HttpStatus.OK);
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
//                HttpStatus.OK);
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
//        if (path.getScene().getId() != sceneId) {
//            throw new ObjectAccessViolationException(sceneId, pathId);
//        }
//    }
}
