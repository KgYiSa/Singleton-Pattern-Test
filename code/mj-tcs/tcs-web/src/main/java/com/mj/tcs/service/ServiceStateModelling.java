package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.config.AppContext;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.service.modelling.IEntityDtoService;
import com.mj.tcs.service.modelling.ServiceGetParams;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
@Component
public class ServiceStateModelling {
    // valueconverter --> service mapping
    private static Map<String, IEntityDtoService> modellingServiceMapping = new CaseInsensitiveMap();

    public void registerService(String key, IEntityDtoService service) {
        if (service == null) {
            throw new TcsServerRuntimeException("registerService with a null object.");
        }

        modellingServiceMapping.put(key, service);
    }

    // GET Parameters
    public ServiceGetParams createGetParamsForAll() {
        return new ServiceGetParams().setType(ServiceGetParams.GetType.GET_ALL);
    }

    public ServiceGetParams createGetParamsForAllInScene(long sceneId) {
        return new ServiceGetParams().setType(ServiceGetParams.GetType.GET_ALL_BY_SCENE_ID)
                .addParameter(ServiceGetParams.NAME_SCENE_ID, sceneId);
    }

    public ServiceGetParams createGetParamsForOne(long id) {
        return new ServiceGetParams().setType(ServiceGetParams.GetType.GET_ONE_BY_ELEMENT_ID)
//                .addParameter(ServiceGetParams.NAME_POINT_ID, id)
//                .addParameter(ServiceGetParams.NAME_PATH_ID, id)
//                .addParameter(ServiceGetParams.NAME_VEHICLE_ID, id)
                .addParameter(ServiceGetParams.NAME_ELEMENT_ID, id)
                .addParameter(ServiceGetParams.NAME_SCENE_ID, id);
    }

    // POINTS
    public Collection<PointDto> getAllPointsFromScene(long sceneId) {
        return getAllInOneSceneInternal(PointDto.class, sceneId).stream()
                .map(item -> (PointDto) item).collect(Collectors.toList());
    }

    public PointDto getPoint(long pointId) {
        return (PointDto) getOneInternal(PointDto.class, pointId);
    }

    public Collection<PointDto> getAllPoints() {
        return getAllInternal(PointDto.class).stream()
                .map(item -> (PointDto) item).collect(Collectors.toList());
    }

    public PointDto createPoint(PointDto entity) {
        // should create by scene service to have the correct connection relationship
        entity = Objects.requireNonNull(entity, "pointEntity is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for point: " + entity.toString());

        // the scene object will NOT know the changes, so let scene handle it.
//        PointDto point = (PointDto) getService(PointDto.class).create(entity);

        // we should know the new ID of the point.
        List<Long> currentPointIds = new ArrayList<>();
        if (scene.getPointDtos() != null) {
            currentPointIds = scene.getPointDtos().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addPointDto(entity);

        getService(SceneDto.class).update(scene);

        for (PointDto point : scene.getPointDtos()) {
            boolean found = false;
            for (Long existedId : currentPointIds) {
                if (existedId == point.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return point;
            }
        }

        throw new TcsServerRuntimeException("New point creation error");
    }

    public PointDto updatePoint(PointDto entity) {
        entity = Objects.requireNonNull(entity, "pointEntity is null");

        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for point: " + entity.toString());

        final long id = entity.getId();
        PointDto pointDto = scene.getPointDtoById(id);
        if (pointDto == null) {
            throw new TcsServerRuntimeException("The point is null by Id " + id + " for scene " + scene.getName());
        }

        // TODO:
//        PointDto point = pointDto.get();
//        point = entity;

        getService(PointDto.class).update(scene);

        return (PointDto) getService(PointDto.class)
                .update(Objects.requireNonNull(pointDto, "point to be updated is null"));
    }

    public void deletePoint(PointDto entity) {
        PointDto point = Objects.requireNonNull(entity, "point to be deleted is null");

        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for point: " + entity.toString());

        // spring will remove all path objects according to the cascadeType
        scene.removePoint(point);

        getService(SceneDto.class).update(scene);
    }

    // PATHS
    public Collection<PathDto> getAllPathDtosFromScene(long sceneId) {
        return getAllInOneSceneInternal(PathDto.class, sceneId).stream()
                .map(item -> (PathDto) item).collect(Collectors.toList());
    }

    public Collection<PathDto> getAllPathDtos() {
        return getAllInternal(PathDto.class).stream()
                .map(item -> (PathDto) item).collect(Collectors.toList());
    }

    public PathDto getPathDto(long pathId) {
        return (PathDto) getOneInternal(PathDto.class, pathId);
    }

    public PathDto createPathDto(PathDto entity) {
        // should create by scene service to have the correct connection relationship

        // the scene object will NOT know the changes, so let scene handle it.
        PathDto path = Objects.requireNonNull(entity, "new path object is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new path object belongs no scene!");
        PointDto srcPoint = Objects.requireNonNull(entity.getSourcePointDto(), "The new path object belongs no source point!");
        PointDto dstPoint = Objects.requireNonNull(entity.getDestinationPointDto(), "The new path object belongs no destination point!");

        // we should know the new ID of the path.
        List<Long> currentPathDtoIds = new ArrayList<>();
        if (scene.getPathDtos() != null) {
            currentPathDtoIds = scene.getPathDtos().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addPathDto(path);

        // system helps do the following connections
//        srcPoint.addOutgoingPathDto(path);
//        dstPoint.addIncomingPathDto(path);

        getService(SceneDto.class).update(scene);

        for (PathDto eachPathDto : scene.getPathDtos()) {
            boolean found = false;
            for (Long existedId : currentPathDtoIds) {
                if (existedId == eachPathDto.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return eachPathDto;
            }
        }

        throw new TcsServerRuntimeException("New path creation error");
    }

    public PathDto updatePathDto(PathDto entity) {
        return (PathDto) getService(PathDto.class).update(Objects.requireNonNull(entity, "path to be updated is null"));
    }

    public void deletePathDto(PathDto entity) {
        entity = Objects.requireNonNull(entity, "path is null");
        PointDto srcPoint = Objects.requireNonNull(entity.getSourcePointDto(), "The path object belongs no source point!");
        PointDto dstPoint = Objects.requireNonNull(entity.getDestinationPointDto(), "The path object belongs no destination point!");

        SceneDto scene = entity.getSceneDto();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (scene.getPathDtoById(id) == null) {
            throw new TcsServerRuntimeException("The path is not belonging to the scene");
        }
        final long srcPointId = srcPoint.getId();
        if (scene.getPointDtoById(srcPointId) == null) {
            throw new TcsServerRuntimeException("The source point of the path is not belonging to the scene");
        }
        final long dstPointId = dstPoint.getId();
        if (scene.getPointDtoById(dstPointId) == null) {
            throw new TcsServerRuntimeException("The destination point of the path is not belonging to the scene");
        }

        scene.removePathDto(entity);

        getService(SceneDto.class).update(scene);
    }

    // LOCATIONS
    public Collection<LocationDto> getAllLocationsFromScene(long sceneId) {
        return getAllInOneSceneInternal(LocationDto.class, sceneId).stream()
                .map(item -> (LocationDto) item).collect(Collectors.toList());
    }

    public Collection<LocationDto> getAllLocations() {
        return getAllInternal(LocationDto.class).stream()
                .map(item -> (LocationDto) item).collect(Collectors.toList());
    }

    public LocationDto getLocation(long locationId) {
        return (LocationDto) getOneInternal(LocationDto.class, locationId);
    }

    public LocationDto createLocation(LocationDto entity) {
        // should create by scene service to have the correct connection relationship

        // the scene object will NOT know the changes, so let scene handle it.
        LocationDto location = Objects.requireNonNull(entity, "new location object is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new location object belongs no scene!");
        Set<LocationLinkDto> links = location.getAttachedLinks();
        if (links != null) {
            for (LocationLinkDto link : links) {
                Objects.requireNonNull(link, "The link object of the location is null");
            }
        }

        // we should know the new ID of the location.
        List<Long> currentLocationIds = new ArrayList<>();
        if (scene.getLocationDtos() != null) {
            currentLocationIds = scene.getLocationDtos().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addLocationDto(location);

        getService(SceneDto.class).update(scene);

        for (LocationDto eachLocation : scene.getLocationDtos()) {
            boolean found = false;
            for (Long existedId : currentLocationIds) {
                if (existedId == eachLocation.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return eachLocation;
            }
        }

        throw new TcsServerRuntimeException("New location creation error");
    }

    public LocationDto updateLocation(LocationDto entity) {
        return (LocationDto) getService(LocationDto.class).update(Objects.requireNonNull(entity, "location to be updated is null"));
    }

    public void deleteLocation(LocationDto entity) {
        entity = Objects.requireNonNull(entity, "location is null");

        SceneDto scene = entity.getSceneDto();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (scene.getLocationDtoById(id) == null) {
            throw new TcsServerRuntimeException("The location is not belonging to the scene");
        }

        scene.removeLocationDto(entity);

        getService(SceneDto.class).update(scene);
    }

    // LOCATION TYPES
    public Collection<LocationTypeDto> getAllLocationTypesFromScene(long sceneId) {
        return getAllInOneSceneInternal(LocationTypeDto.class, sceneId).stream()
                .map(item -> (LocationTypeDto) item).collect(Collectors.toList());
    }

    public LocationTypeDto getLocationType(long locationTypeId) {
        return (LocationTypeDto) getOneInternal(LocationTypeDto.class, locationTypeId);
    }

    public Collection<LocationTypeDto> getAllLocationTypes() {
        return getAllInternal(LocationTypeDto.class).stream()
                .map(item -> (LocationTypeDto) item).collect(Collectors.toList());
    }

    public LocationTypeDto createLocationType(LocationTypeDto entity) {
        // the scene object will NOT know the changes, so let scene handle it.
        Objects.requireNonNull(entity, "locationTypeEntity is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new locationType object belongs no scene!");

        // we should know the new ID of the path.
        List<Long> currentLocationTypeIds = new ArrayList<>();
        if (scene.getLocationTypeDtos() != null) {
            currentLocationTypeIds = scene.getLocationTypeDtos().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addLocationTypeDto(entity);

//        entity.setId(null);

        getService(SceneDto.class).update(scene);

        for (LocationTypeDto eachLocationType : scene.getLocationTypeDtos()) {
            boolean found = false;
            for (Long existedId : currentLocationTypeIds) {
                if (existedId == eachLocationType.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return eachLocationType;
            }
        }

        throw new TcsServerRuntimeException("New locationType creation error");
    }

    public LocationTypeDto updateLocationType(LocationTypeDto entity) {
        entity = Objects.requireNonNull(entity, "locationTypeEntity is null");

        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for locationType: " + entity.toString());

        final long id = entity.getId();
        LocationTypeDto locationType = scene.getLocationTypeDtoById(id);
        if (locationType == null) {
            throw new TcsServerRuntimeException("LocationDto Type [" + entity.getName() + "] to be updated is null from the scene");
        }
        
        //TODO
        LocationTypeDto type = locationType;
        type = entity;

        getService(LocationTypeDto.class).update(scene);

        return (LocationTypeDto) getService(LocationTypeDto.class)
                .update(Objects.requireNonNull(type, "locationType to be updated is null"));
    }

    public void deleteLocationType(LocationTypeDto entity) {
        LocationTypeDto locationType = Objects.requireNonNull(entity, "locationType to be deleted is null");

        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for locationType: " + entity.toString());

        scene.removeLocationTypeDto(locationType);

        getService(SceneDto.class).update(scene);
    }

    // STATIC_ROUTES
    public Collection<StaticRouteDto> getAllStaticRoutesFromScene(long sceneId) {
        return getAllInOneSceneInternal(StaticRouteDto.class, sceneId).stream()
                .map(item -> (StaticRouteDto) item).collect(Collectors.toList());
    }

    public Collection<StaticRouteDto> getAllStaticRoutes() {
        return getAllInternal(StaticRouteDto.class).stream()
                .map(item -> (StaticRouteDto) item).collect(Collectors.toList());
    }

    public StaticRouteDto getStaticRoute(long staticRouteId) {
        return (StaticRouteDto) getOneInternal(StaticRouteDto.class, staticRouteId);
    }

    public StaticRouteDto createStaticRoute(StaticRouteDto entity) {
        // should create by scene service to have the correct connection relationship

        // the scene object will NOT know the changes, so let scene handle it.
        StaticRouteDto staticRoute = Objects.requireNonNull(entity, "new staticRoute object is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new staticRoute object belongs no scene!");
        if (!staticRoute.isValid()) {
            throw new TcsServerRuntimeException("The static route is invalid!");
        }

        // we should know the new ID of the staticRoute.
        List<Long> currentStaticRouteIds = new ArrayList<>();
        if (scene.getStaticRouteDtos() != null) {
            currentStaticRouteIds = scene.getStaticRouteDtos().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addStaticRouteDto(staticRoute);

        getService(SceneDto.class).update(scene);

        for (StaticRouteDto eachStaticRoute : scene.getStaticRouteDtos()) {
            boolean found = false;
            for (Long existedId : currentStaticRouteIds) {
                if (existedId == eachStaticRoute.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return eachStaticRoute;
            }
        }

        throw new TcsServerRuntimeException("New staticRoute creation error");
    }

    public StaticRouteDto updateStaticRoute(StaticRouteDto entity) {
        return (StaticRouteDto) getService(StaticRouteDto.class).update(Objects.requireNonNull(entity, "staticRoute to be updated is null"));
    }

    public void deleteStaticRoute(StaticRouteDto entity) {
        entity = Objects.requireNonNull(entity, "staticRoute is null");

        SceneDto scene = entity.getSceneDto();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (scene.getStaticRouteDtoById(id) == null) {
            throw new TcsServerRuntimeException("The staticRoute is not belonging to the scene");
        }

        scene.removeStaticRouteDto(entity);

        getService(SceneDto.class).update(scene);
    }

    // VEHICLES
    public Collection<VehicleDto> getAllVehiclesFromScene(long sceneId) {
        return getAllInOneSceneInternal(VehicleDto.class, sceneId).stream()
                .map(item -> (VehicleDto) item).collect(Collectors.toList());
    }

    public Collection<VehicleDto> getAllVehicles() {
        return getAllInternal(VehicleDto.class).stream()
                .map(item -> (VehicleDto) item).collect(Collectors.toList());
    }

    public VehicleDto getVehicle(long vehicleId) {
        return (VehicleDto) getOneInternal(VehicleDto.class, vehicleId);
    }

    public VehicleDto createVehicle(VehicleDto entity) {

        VehicleDto vehicle = Objects.requireNonNull(entity, "new vehicle object is null");

        return (VehicleDto) getService(VehicleDto.class).create(entity);
    }

    public VehicleDto updateVehicle(VehicleDto entity) {
        return (VehicleDto) getService(VehicleDto.class).update(Objects.requireNonNull(entity, "vehicle to be updated is null"));
    }

    public void deleteVehicle(VehicleDto entity) {
        entity = Objects.requireNonNull(entity, "vehicle is null");
        getService(VehicleDto.class).delete(entity.getId());
    }

    //SCENES
    public SceneDto getSceneDto(long sceneId) {
        return (SceneDto) getOneInternal(SceneDto.class, sceneId);
    }

    public Collection<SceneDto> getAllScenes() {
        return getAllInternal(SceneDto.class).stream()
                .map(item -> (SceneDto)item).collect(Collectors.toList());
    }

    public SceneDto createScene(SceneDto entity) {
        return (SceneDto) getService(SceneDto.class).create(entity);
    }

    public SceneDto updateScene(SceneDto entity) {
        return (SceneDto) getService(SceneDto.class).update(entity);
    }

    public void deleteScene(long sceneId) {
        getService(SceneDto.class).delete(sceneId);
    }


    // PRIVATE

    private IEntityDtoService getService(Class entityClass) {
        if (entityClass == null) {
            throw new TcsServerRuntimeException("getService with a null class !");
        }

        String name = entityClass.getSimpleName();
        if (modellingServiceMapping.get(name) == null) {
            Map<String, IEntityDtoService> services = AppContext.getContext().getBeansOfType(IEntityDtoService.class);

            if (services != null) {
                for (IEntityDtoService service : services.values()) {
                    if (service.canSupportEntityClass(entityClass)) { // because the name is not actually the same, e.g. e.g. nameA == NameA
                        registerService(name, service);
                        return service;
                    }
                }
            }
        } else {
            return modellingServiceMapping.get(name);
        }

        throw new TcsServerRuntimeException("the service is unregistered.");
    }

    private Collection<Object> getAllInternal(Class entityClass) {
        return getService(entityClass).get(createGetParamsForAll());
    }

    private Collection<Object> getAllInOneSceneInternal(Class entityClass, long sceneId) {
        return getService(entityClass).get(createGetParamsForAllInScene(sceneId));
    }

    private Object getOneInternal(Class entityClass, long id) {
        Collection<Object> answer = getService(entityClass).get(createGetParamsForOne(id));
        if (answer == null || answer.size() == 0) {
            throw new TcsServerRuntimeException("no item found!");
        }

        return answer.toArray()[0];
    }

    // TODO::
    /**
     * Adds an incoming path to a point.
     *
     * @param point The point to be modified.
     * @param path The path
     * @return The modified point.
     * @throws ObjectUnknownException If the point or path does not exist.
     *         TcsServerRuntimeException If the path is not the outgoing one of the point.
     *
     */
    private PointDto addPointIncomingPathDto(PointDto point, PathDto path) {

        if (point == null) {
            throw new ObjectUnknownException("PointDto is null, PathDto: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("PathDto is null, PointDto: " + point);
        }

        // Check if the point really is the path's destination.
        if (!path.getDestinationPointDto().equals(point)) {
            throw new TcsServerRuntimeException("PointDto is not the path's destination.");
        }

        point.addIncomingPathDto(path);

        return point;
    }

    /**
     * Removes an incoming path from a point.
     *
     * @param point The point to be modified.
     * @param path The path.
     * @return The modified point.
     * @throws ObjectUnknownException If the referenced point or path do not
     * exist.
     */
    public PointDto removePointIncomingPathDto(PointDto point,
                                               PathDto path) throws ObjectUnknownException {
        if (point == null) {
            throw new ObjectUnknownException("PointDto is null, PathDto: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("PathDto is null, PointDto: " + point);
        }

        point.removeIncomingPathDto(path);

        return point;
    }

    /**
     * Add an outgoing path to a point.
     *
     * @param point The point to be modified.
     * @param path The path.
     * @return The modified point.
     * @throws ObjectUnknownException If the point or path does not exist.
     *         TcsServerRuntimeException If the path is not the outgoing one of the point.
     */
    private PointDto addPointOutgoingPathDto(PointDto point, PathDto path) {

        if (point == null) {
            throw new ObjectUnknownException("PointDto is null, PathDto: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("PathDto is null, PointDto: " + point);
        }

        // Check if the point really is the path's source.
        if (!path.getSourcePointDto().equals(point)) {
            throw new TcsServerRuntimeException("PointDto is not the path's source.");
        }

        point.addOutgoingPathDto(path);

        return point;
    }

    /**
     * Removes an outgoing path from a point.
     *
     * @param point The point to be modified.
     * @param path The path.
     * @return The modified point.
     * @throws ObjectUnknownException If the referenced point or path do not
     * exist.
     */
    public PointDto removePointOutgoingPathDto(PointDto point,
                                               PathDto path)
            throws ObjectUnknownException {

        if (point == null) {
            throw new ObjectUnknownException("PointDto is null, PathDto: " + point);
        }

        if (path == null) {
            throw new ObjectUnknownException("PathDto is null, PointDto: " + path);
        }

        point.removeOutgoingPathDto(path);

        return point;
    }
}
