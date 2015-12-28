package com.mj.tcs.api.v1.dto.converter;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.TripleDto;
import com.mj.tcs.data.base.TCSObject;
import com.mj.tcs.data.base.TCSResource;
import com.mj.tcs.data.base.Triple;
import com.mj.tcs.data.model.*;
import com.mj.tcs.kernel.Scene;

import java.util.*;

/**
 * @author Wang Zhen
 */
public class SceneDtoConverter {

    private static final Map<String, Object> sceneConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
        // Converters
//        sceneConverters.put("Numeric2PathConverter", new Numeric2PathConverter());
//
//        // Matchers
//        sceneConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());
//        sceneConverters.put("PathDto2PathMatcher", new PathDto2PathMatcher());
//        sceneConverters.put("LocationTypeDto2LocationTypeMatcher", new LocationTypeDto2LocationTypeMatcher());
//        sceneConverters.put("LocationDto2LocationMatcher", new LocationDto2LocationMatcher());
//        sceneConverters.put("LocationLinkDto2LocationLinkMatcher", new LocationLinkDto2LocationLinkMatcher());

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

    public Object convertToDto(final Scene scene) {
        SceneDto sceneDto = new SceneDto();

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
//            Optional<Location> location = scene.getLocationByUUID(id);
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
//                            throw new TCSServerRuntimeException("The link is null by Id " + linkDto.getId() + " of location " + locationDto.getName());
//                        }
//                        Optional<Point> linkedPoint = scene.getPointByUUID(locationLink.get().getPoint().getId());
//                        if (!linkedPoint.isPresent()) {
//                            throw new TCSServerRuntimeException("The linked point for link " + linkDto.getName() + " is null");
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
//                    Optional<StaticRoute> entityOp = scene.getStaticRouteByUUID(staticRouteDto.getId());
//                    if (!entityOp.isPresent()) {
//                        throw new TCSServerRuntimeException("StaticRoute can not be found for the id "
//                                + staticRouteDto.getId());
//                    }
//
//                    List<Long> pointIds = entityOp.get().getHops().stream()
//                            .map(h -> h.getId()).collect(Collectors.toList());
//                    staticRouteDto.setHops(pointIds);
//                }
//            } else {
//                throw new TCSServerRuntimeException("The staticRoute Dto is not existed");
//            }
//        }

        return sceneDto;
    }

    public Scene convertToEntity(final SceneDto sceneDto) {
        final Scene scene = new Scene(sceneDto.getId());

        scene.setName(sceneDto.getName());

        // UUID <--> Point
        Map<String, TCSObject> tcsObjectMap = new HashMap<>();

        if (sceneDto.getPointDtos() != null) {
            sceneDto.getPointDtos().forEach(v -> {
                final Point point = new Point(v.getUUID(), v.getName());
                point.setType(Point.Type.fromString(v.getType().toString()));

                TripleDto dtoPosition = Objects.requireNonNull(v.getPosition());
                Triple position = new Triple(dtoPosition.getX(),
                                             dtoPosition.getY(),
                                             dtoPosition.getZ());
                point.setPosition(position);

                point.setVehicleOrientationAngle(v.getVehicleOrientationAngle());

                if (v.getProperties() != null) {
                    v.getProperties().forEach(vv -> {
                        point.setProperty(vv.getName(), vv.getValue());
                    });
                }

                scene.addPoint(point);
                tcsObjectMap.put(v.getUUID(), point);
            });
        }

        if (sceneDto.getPathDtos() != null) {
            sceneDto.getPathDtos().forEach(v -> {
                Point sourcePoint = Point.class.cast(Objects.requireNonNull(tcsObjectMap.get(v.getSourcePointDto().getUUID())));
                Point destinationPoint = Point.class.cast(Objects.requireNonNull(tcsObjectMap.get(v.getDestinationPointDto().getUUID())));

                final Path path = new Path(v.getUUID(), v.getName(), sourcePoint.getReference(), destinationPoint.getReference());

                if (v.getControlPoints() != null) {
                    final List<Triple> controlPoints = new ArrayList<>();
                    v.getControlPoints().forEach(vv -> {
                        controlPoints.add(new Triple(vv.getX(), vv.getY(), vv.getZ()));
                    });
                    path.setControlPoints(controlPoints);
                }

                path.setLength(v.getLength());

                path.setLocked(v.isLocked());

                path.setMaxVelocity(v.getMaxVelocity());
                path.setMaxReverseVelocity(v.getMaxReverseVelocity());

                path.setRoutingCost(v.getRoutingCost());

                if (v.getProperties() != null) {
                    v.getProperties().forEach(vv -> {
                        path.setProperty(vv.getName(), vv.getValue());
                    });
                }

                sourcePoint.addOutgoingPath(path.getReference());
                destinationPoint.addIncomingPath(path.getReference());

                scene.addPath(path);
                tcsObjectMap.put(v.getUUID(), path);
            });
        }

        if (sceneDto.getLocationTypeDtos() != null) {
            sceneDto.getLocationTypeDtos().forEach(v -> {
                final LocationType type = new LocationType(v.getUUID(), v.getName());

                if (v.getAllowedOperations() != null) {
                    v.getAllowedOperations().forEach(vv -> type.addAllowedOperation(vv));
                }

                if (v.getProperties() != null) {
                    v.getProperties().forEach(vv -> {
                        type.setProperty(vv.getName(), vv.getValue());
                    });
                }

                scene.addLocationType(type);
                tcsObjectMap.put(v.getUUID(), type);
            });
        }

        if (sceneDto.getLocationDtos() != null) {
            sceneDto.getLocationDtos().forEach(v -> {
                final LocationType type = LocationType.class.cast(Objects.requireNonNull(tcsObjectMap.get(v.getLocationTypeDto().getUUID())));

                final Location location = new Location(v.getUUID(), v.getName(), type.getReference());

                TripleDto dtoPosition = Objects.requireNonNull(v.getPosition());
                Triple position = new Triple(dtoPosition.getX(),
                        dtoPosition.getY(),
                        dtoPosition.getZ());
                location.setPosition(position);

                // links
                if (v.getAttachedLinks() != null) {
                    v.getAttachedLinks().forEach(vv -> {
                        final Point point = Point.class.cast(Objects.requireNonNull(tcsObjectMap.get(vv.getPointDto().getUUID())));

                        final Location.Link link = new Location.Link(location.getReference(), point.getReference());
                        if (vv.getAllowedOperations() != null) {
                            vv.getAllowedOperations().forEach(o -> link.addAllowedOperation(o));
                        }

                        point.attachLink(link);
                        location.attachLink(link);
                    });
                }

                if (v.getProperties() != null) {
                    v.getProperties().forEach(vv -> {
                        location.setProperty(vv.getName(), vv.getValue());
                    });
                }

                scene.addLocation(location);
                tcsObjectMap.put(v.getUUID(), location);
            });
        }

        if (sceneDto.getVehicleDtos() != null) {
            sceneDto.getVehicleDtos().forEach(v -> {
                final Vehicle vehicle = new Vehicle(v.getUUID(), v.getName());
                // TODO: not necessary ???
//                vehicle.setAdapterState(CommunicationAdapter.State.valueOf());
//                vehicle.setState();

                // TODO: ONLY IN SIMULATION MODE
                if (v.getInitialPoint() != null) {
                    final Point point = Point.class.cast(Objects.requireNonNull(tcsObjectMap.get(v.getInitialPoint().getUUID())));
                    vehicle.setCurrentPosition(point.getReference());
                }
                vehicle.setEnergyLevel(v.getEnergyLevel());
                vehicle.setEnergyLevelCritical(v.getEnergyLevelCritical());
                vehicle.setEnergyLevelGood(v.getEnergyLevelGood());
                vehicle.setLength(v.getLength());
//                vehicle.setLoadHandlingDevices(v.getl);
                vehicle.setMaxVelocity(v.getMaxVelocity());
                vehicle.setMaxReverseVelocity(v.getMaxReverseVelocity());
                vehicle.setOrientationAngle(v.getOrientationAngle());

                if (v.getPrecisePosition() != null) {
                    final TripleDto posDto = Objects.requireNonNull(v.getPrecisePosition());
                    final Triple pos = new Triple(posDto.getX(), posDto.getY(), posDto.getZ());
                    vehicle.setPrecisePosition(pos);
                }

                if (v.getRechargeOperation() != null) {
                    vehicle.setRechargeOperation(v.getRechargeOperation());
                }

                if (v.getProperties() != null) {
                    v.getProperties().forEach(vv -> {
                        vehicle.setProperty(vv.getName(), vv.getValue());
                    });
                }

                scene.addVehicle(vehicle);
                tcsObjectMap.put(v.getUUID(), vehicle);
            });
        }

        if (sceneDto.getBlockDtos() != null) {
            sceneDto.getBlockDtos().forEach(v -> {

                final Block block = new Block(v.getUUID(), v.getName());

                if (v.getMembers() != null && !v.getMembers().isEmpty()) {
                    v.getMembers().forEach(vv -> block.addMember(
                            TCSResource.class.cast(
                                    Objects.requireNonNull(tcsObjectMap.get(vv.getResource().getUUID())))
                                    .getReference()
                    ));

                    if (v.getProperties() != null) {
                        v.getProperties().forEach(vv -> {
                            block.setProperty(vv.getName(), vv.getValue());
                        });
                    }

                    scene.addBlock(block);
                }
            });
        }

        if (sceneDto.getStaticRouteDtos() != null) {
            sceneDto.getStaticRouteDtos().forEach(v -> {
                final StaticRoute route = new StaticRoute(v.getUUID(), v.getName());

                if (v.getHops() != null && !v.getHops().isEmpty()) {
                    v.getHops().forEach(vv -> route.addHop(
                            Point.class.cast(Objects.requireNonNull(tcsObjectMap.get(vv.getUUID())))
                                    .getReference()));

                    if (v.getProperties() != null) {
                        v.getProperties().forEach(vv -> {
                            route.setProperty(vv.getName(), vv.getValue());
                        });
                    }

                    scene.addStaticRoute(route);
                }
            });
        }
////         TODO
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
//                            throw new TCSServerRuntimeException("Source Point or Destination Point can not be found for the path "
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
//                        throw new TCSServerRuntimeException("LocationDto can not be found for the location "
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
//                                throw new TCSServerRuntimeException("LocationLinkDto can not be found for the location "
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
//                        throw new TCSServerRuntimeException("Location Type can not be found for the location "
//                                + location.getName());
//                    }
//                    location.setType(locationType);
//
//                }
//            } else {
//                throw new TCSServerRuntimeException("The Location Entity is not existed");
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
//                        throw new TCSServerRuntimeException("StaticRouteDto can not be found for the id "
//                                + staticRoute.getId());
//                    }
//
//                    List<Point> points = dtoOp.get().getHops().stream()
//                            .map(h -> (Point) pointMapping.get(h).getRight()).collect(Collectors.toList());
//                    staticRoute.setHops(points);
//                }
//            } else {
//                throw new TCSServerRuntimeException("The staticRoute Entity is not existed");
//            }
//        }

        return scene;
    }
}
