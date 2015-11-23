package com.mj.tcs.api.v1.dto.converter;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.*;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.util.TcsBeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "SceneDtoConverter")
public class SceneDtoConverter extends DummyDtoConverter {

    private static final Map<String, Object> sceneConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
        // Converters
        sceneConverters.put("Numeric2PathConverter", new Numeric2PathConverter());

        // Matchers
        sceneConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());
        sceneConverters.put("PathDto2PathMatcher", new PathDto2PathMatcher());
        sceneConverters.put("LocationTypeDto2LocationTypeMatcher", new LocationTypeDto2LocationTypeMatcher());
        sceneConverters.put("LocationDto2LocationMatcher", new LocationDto2LocationMatcher());
        sceneConverters.put("LocationLinkDto2LocationLinkMatcher", new LocationLinkDto2LocationLinkMatcher());

        // Bean factory
        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point",
                "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");

        beanFactory.registerEntity("Path", "com.mj.tcs.data.model.Path",
                "com.mj.tcs.data.model.Path");
        beanFactory.registerDto("PathDto", "com.mj.tcs.api.v1.dto.PathDto");

        beanFactory.registerEntity("LocationType", "com.mj.tcs.data.model.LocationType",
                "com.mj.tcs.data.model.LocationType");
        beanFactory.registerDto("LocationTypeDto", "com.mj.tcs.api.v1.dto.LocationTypeDto");

        beanFactory.registerEntity("Location", "com.mj.tcs.data.model.Location",
                "com.mj.tcs.data.model.Location");
        beanFactory.registerDto("LocationDto", "com.mj.tcs.api.v1.dto.LocationDto");

        beanFactory.registerEntity("LocationLink", "com.mj.tcs.data.model.Location$Link",
                "com.mj.tcs.data.model.Location$Link");
        beanFactory.registerDto("LocationLinkDto", "com.mj.tcs.api.v1.dto.LocationLinkDto");

        beanFactory.registerEntity("StaticRoute", "com.mj.tcs.data.model.StaticRoute",
                "com.mj.tcs.data.model.StaticRoute");
        beanFactory.registerDto("StaticRouteDto", "com.mj.tcs.api.v1.dto.StaticRouteDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof Scene) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        SceneDto sceneDto = new SceneDto();
        Scene scene = (Scene) entity;

        // TODO
//        DTOAssembler.newAssembler(sceneDto.getClass(), scene.getClass())
//                .assembleDto(sceneDto, scene, sceneConverters, beanFactory);
//
//        // Mapping - <Point-ID, PointDto, Point>
//        Map<Long, Pair<PointDto, Point>> pointMapping = new HashMap<>();
//        // <Path-ID, PathDto, Path>
//        Map<Long, Pair<PathDto, Path>> pathMapping = new HashMap<>();
//
//        // fill the two maps
//        if (scene.getPoints() != null && sceneDto.getPointDtos() != null) {
//            for (Point point : scene.getPoints()) {
//                for (PointDto pointDto : sceneDto.getPointDtos()) {
//                    if (point.getId() == pointDto.getId()) {
//                        pointMapping.put(point.getId(), Pair.of(pointDto, point));
//                        break;
//                    }
//                }
//            }
//        }
//        if (scene.getPaths() != null && sceneDto.getPathDtos() != null) {
//            for (Path path : scene.getPaths()) {
//                for (PathDto pathDto : sceneDto.getPathDtos()) {
//                    if (path.getId() == pathDto.getId()) {
//                        pathMapping.put(path.getId(), Pair.of(pathDto, path));
//                        break;
//                    }
//                }
//            }
//        }
//
//        // path's relationship
//        for (long pathId : pathMapping.keySet()) {
//            long srcPointId = pathMapping.get(pathId).getRight().getSourcePoint().getId();
//            long dstPointId = pathMapping.get(pathId).getRight().getDestinationPoint().getId();
//
//            pathMapping.get(pathId).getLeft().setSourcePointDto(srcPointId);
//            pathMapping.get(pathId).getLeft().setDestinationPointDto(dstPointId);
//
//            // points ! see the following inverse conversion for the reason
//            pointMapping.get(srcPointId).getLeft().addOutgoingPathId(pathId);
//            pointMapping.get(dstPointId).getLeft().addIncomingPathId(pathId);
//        }
//
//        // point's relation
//        for (Pair<PointDto, Point> pair : pointMapping.values()) {
//            for (Path path : pair.getRight().getIncomingPaths()) {
//                pair.getLeft().addIncomingPathId(path.getId());
//            }
//            for (Path path : pair.getRight().getOutgoingPaths()) {
//                pair.getLeft().addOutgoingPathId(path.getId());
//            }
//        }
//
//        // location - location type
//        for (LocationDto locationDto : sceneDto.getLocations()) {
//            long id = locationDto.getId();
//            Optional<Location> location = scene.getLocationById(id);
//            if (location.isPresent()) {
//                LocationType type = location.get().getType();
//                if (type != null) {
//                    locationDto.setLocationType(type.getId());
//                }
//
//                // links
//                if (locationDto.getAttachedLinks() != null) {
//                    for (LocationLinkDto linkDto : locationDto.getAttachedLinks()) {
//                        Optional<Location.Link> locationLink = location.get().getAttachedLinkById(linkDto.getId());
//                        if (!locationLink.isPresent()) {
//                            throw new TcsServerRuntimeException("The link is null by Id " + linkDto.getId() + " of location " + locationDto.getName());
//                        }
//                        Optional<Point> linkedPoint = scene.getPointById(locationLink.get().getPoint().getId());
//                        if (!linkedPoint.isPresent()) {
//                            throw new TcsServerRuntimeException("The linked point for link " + linkDto.getName() + " is null");
//                        }
//                        linkDto.setPointDto(linkedPoint.get().getId());
//                    }
//                }
//            }
//        }
//
//        //static route
//        if (scene.getStaticRoutes() != null) {
//            if (sceneDto.getStaticRouteDtos() != null) {
//                for (StaticRouteDto staticRouteDto : sceneDto.getStaticRouteDtos()) {
//                    Optional<StaticRoute> entityOp = scene.getStaticRouteById(staticRouteDto.getId());
//                    if (!entityOp.isPresent()) {
//                        throw new TcsServerRuntimeException("StaticRoute can not be found for the id "
//                                + staticRouteDto.getId());
//                    }
//
//                    List<Long> pointIds = entityOp.get().getHops().stream()
//                            .map(h -> h.getId()).collect(Collectors.toList());
//                    staticRouteDto.setHops(pointIds);
//                }
//            } else {
//                throw new TcsServerRuntimeException("The staticRoute Dto is not existed");
//            }
//        }

        return sceneDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof SceneDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        SceneDto sceneDto = (SceneDto) dto;
        Scene scene = new Scene();

        // TODO
//        DTOAssembler.newAssembler(sceneDto.getClass(), scene.getClass())
//                .assembleEntity(sceneDto, scene, sceneConverters, beanFactory);
//
//        // connections for each component
//        // Mapping - <Point-ID, PointDto, Point>
//        Map<Long, Pair<PointDto, Point>> pointMapping = new HashMap<>();
//        // <Path-ID, PathDto, Path>
//        Map<Long, Pair<PathDto, Path>> pathMapping = new HashMap<>();
//
//        // fill the two maps
//        if (sceneDto.getPointDtos() != null && scene.getPoints() != null) {
//            for (PointDto pointDto : sceneDto.getPointDtos()) {
//                for (Point point : scene.getPoints()) {
//                    if (pointDto.getId() == point.getId()) {
//                        pointMapping.put(pointDto.getId(), Pair.of(pointDto, point));
//                        break;
//                    }
//                }
//            }
//        }
//        if (sceneDto.getPathDtos() != null && scene.getPaths() != null) {
//            for (PathDto pathDto : sceneDto.getPathDtos()) {
//                for (Path path : scene.getPaths()) {
//                    if (pathDto.getId() == path.getId()) {
//                        // check all points of these paths should be existed.
//                        final long sourcePointId = pathDto.getSourcePointDto();
//                        final long destinationPointId = pathDto.getDestinationPointDto();
//                        if (sceneDto.getPointDtos() == null ||
//                            !sceneDto.getPointDtos().stream().anyMatch(p -> p.getId() == sourcePointId) ||
//                                !sceneDto.getPointDtos().stream().anyMatch(p -> p.getId() == destinationPointId)) {
//                            throw new TcsServerRuntimeException("Source Point or Destination Point can not be found for the path "
//                                    + path.getName());
//                        }
//
//                        pathMapping.put(pathDto.getId(), Pair.of(pathDto, path));
//                        break;
//                    }
//                }
//            }
//        }
//
//        // Path's relationship (Path is ahead of Point !!!, in case the paths & points relationship integrity,
//        // e.g. Point1 contains no incoming path, while Path1 contains the destination point Point1)
//        for (Pair<PathDto, Path> pair : pathMapping.values()) {
//            // scene
//            pair.getRight().setSceneDto(scene);
//
//            // source point
//            long srcPointId = pair.getLeft().getSourcePointDto();
//            // destination point
//            long dstPointId = pair.getLeft().getDestinationPointDto();
//
//            pair.getRight().setSourcePoint(
//                    pointMapping.get(srcPointId).getRight()
//            );
//            pair.getRight().setDestinationPoint(
//                    pointMapping.get(dstPointId).getRight()
//            );
//
//            // set the corresponding points to assure the paths & points relationship integrity !!! IMPORTANT
//            pointMapping.get(srcPointId).getRight().addOutgoingPath(
//                    pair.getRight()
//            );
//            pointMapping.get(dstPointId).getRight().addIncomingPath(
//                    pair.getRight()
//            );
//        }
//
//        // Point's relationship
//        for (Pair<PointDto, Point> pair : pointMapping.values()) {
//            // scene
//            pair.getRight().setSceneDto(scene);
//
//            // incoming paths
//            for (long incomingPathId : pair.getLeft().getIncomingPathIds()) {
//                pair.getRight().addIncomingPath(
//                        pathMapping.get(incomingPathId).getRight()
//                );
//            }
//
//            // outgoing paths
//            for (long outgoingPathId : pair.getLeft().getOutgoingPathIds()) {
//                pair.getRight().addOutgoingPath(
//                        pathMapping.get(outgoingPathId).getRight()
//                );
//            }
//        }
//
//
//        // location type
//        Map<Long, LocationType> locationTypeMapping = new HashMap<>();
//        if (scene.getLocationTypes() != null) {
//            for (LocationType locationType : scene.getLocationTypes()) {
//                locationType.setSceneDto(scene);
//
//                locationTypeMapping.put(locationType.getId(), locationType);
//            }
//        }
//
//        // location
//        if (sceneDto.getLocations() != null) {
//            if (scene.getLocations() != null) {
//                for (Location location : scene.getLocations()) {
//
//                    Optional<LocationDto> locationDto = sceneDto.getLocationDtoById(location.getId());
//                    if (!locationDto.isPresent()) {
//                        throw new TcsServerRuntimeException("LocationDto can not be found for the location "
//                                + location.getName());
//                    }
//
//                    // scene
//                    location.setSceneDto(scene);
//
//                    // links
//                    if (location.getAttachedLinks() != null) {
//                        for (Location.Link link : location.getAttachedLinks()) {
//                            // location
//                            link.setLocation(location);
//
//                            // point
//                            Optional<LocationLinkDto> linkDto = locationDto.get().getAttachedLinkById(link.getId());
//                            if (!linkDto.isPresent()) {
//                                throw new TcsServerRuntimeException("LocationLinkDto can not be found for the location "
//                                        + location.getName() + ", by link Id " + link.getId());
//                            }
//                            Point linkedPoint = pointMapping.get(linkDto.get().getPointDto()).getRight();
//                            Objects.requireNonNull(linkedPoint, "The linked point for the link[" + link.getName() + "] is null!");
//                            link.setPoint(linkedPoint);
//                        }
//                    }
//
//                    // location type
//                    final Long locationTypeId = locationDto.get().getLocationType();
//                    if (locationTypeId == null) {
//                        continue;
//                    }
//                    LocationType locationType = locationTypeMapping.get(locationTypeId);
//                    if (locationType == null) {
//                        throw new TcsServerRuntimeException("Location Type can not be found for the location "
//                                + location.getName());
//                    }
//                    location.setType(locationType);
//
//                }
//            } else {
//                throw new TcsServerRuntimeException("The Location Entity is not existed");
//            }
//        }
//
//        //static route
//        if (sceneDto.getStaticRouteDtos() != null) {
//            if (scene.getStaticRoutes() != null) {
//                for (StaticRoute staticRoute : scene.getStaticRoutes()) {
//                    staticRoute.setSceneDto(scene);
//
//                    Optional<StaticRouteDto> dtoOp = sceneDto.getStaticRouteDtoById(staticRoute.getId());
//                    if (!dtoOp.isPresent()) {
//                        throw new TcsServerRuntimeException("StaticRouteDto can not be found for the id "
//                                + staticRoute.getId());
//                    }
//
//                    List<Point> points = dtoOp.get().getHops().stream()
//                            .map(h -> (Point) pointMapping.get(h).getRight()).collect(Collectors.toList());
//                    staticRoute.setHops(points);
//                }
//            } else {
//                throw new TcsServerRuntimeException("The staticRoute Entity is not existed");
//            }
//        }

        return scene;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof SceneDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {
        TcsBeanUtils.copyProperties(dto, properties);

        return dto;
    }

    @Override
    public boolean canMergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {

        if (entity != null && entity instanceof Scene) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {
        TcsBeanUtils.copyProperties(entity, properties);

        return entity;
    }
}
