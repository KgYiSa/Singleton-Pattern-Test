package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.config.AppContext;
import com.mj.tcs.data.model.*;
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
        PointDto srcPoint = Objects.requireNonNull(entity.getSourcePoint(), "The path object belongs no source point!");
        PointDto dstPoint = Objects.requireNonNull(entity.getDestinationPoint(), "The path object belongs no destination point!");

        SceneDto scene = entity.getSceneDto();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (!scene.getPathDtoById(id).isPresent()) {
            throw new TcsServerRuntimeException("The path is not belonging to the scene");
        }
        final long srcPointId = srcPoint.getId();
        if (!scene.getPointById(srcPointId).isPresent()) {
            throw new TcsServerRuntimeException("The source point of the path is not belonging to the scene");
        }
        final long dstPointId = dstPoint.getId();
        if (!scene.getPointById(dstPointId).isPresent()) {
            throw new TcsServerRuntimeException("The destination point of the path is not belonging to the scene");
        }

        scene.removePathDto(entity);

        getService(SceneDto.class).update(scene);
    }

    // LOCATIONS
    public Collection<Location> getAllLocationsFromScene(long sceneId) {
        return getAllInOneSceneInternal(Location.class, sceneId).stream()
                .map(item -> (Location) item).collect(Collectors.toList());
    }

    public Collection<Location> getAllLocations() {
        return getAllInternal(Location.class).stream()
                .map(item -> (Location) item).collect(Collectors.toList());
    }

    public Location getLocation(long locationId) {
        return (Location) getOneInternal(Location.class, locationId);
    }

    public Location createLocation(Location entity) {
        // should create by scene service to have the correct connection relationship

        // the scene object will NOT know the changes, so let scene handle it.
        Location location = Objects.requireNonNull(entity, "new location object is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new location object belongs no scene!");
        Set<Location.Link> links = location.getAttachedLinks();
        if (links != null) {
            for (Location.Link link : links) {
                Objects.requireNonNull(link, "The link object of the location is null");
            }
        }

        // we should know the new ID of the location.
        List<Long> currentLocationIds = new ArrayList<>();
        if (scene.getLocations() != null) {
            currentLocationIds = scene.getLocations().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addLocation(location);

        getService(SceneDto.class).update(scene);

        for (Location eachLocation : scene.getLocations()) {
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

    public Location updateLocation(Location entity) {
        return (Location) getService(Location.class).update(Objects.requireNonNull(entity, "location to be updated is null"));
    }

    public void deleteLocation(Location entity) {
        entity = Objects.requireNonNull(entity, "location is null");

        SceneDto scene = entity.getSceneDto();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (scene.getLocationById(id) == null) {
            throw new TcsServerRuntimeException("The location is not belonging to the scene");
        }

        scene.removeLocation(entity);

        getService(SceneDto.class).update(scene);
    }

    // LOCATION TYPES
    public Collection<LocationType> getAllLocationTypesFromScene(long sceneId) {
        return getAllInOneSceneInternal(LocationType.class, sceneId).stream()
                .map(item -> (LocationType) item).collect(Collectors.toList());
    }

    public LocationType getLocationType(long locationTypeId) {
        return (LocationType) getOneInternal(LocationType.class, locationTypeId);
    }

    public Collection<LocationType> getAllLocationTypes() {
        return getAllInternal(LocationType.class).stream()
                .map(item -> (LocationType) item).collect(Collectors.toList());
    }

    public LocationType createLocationType(LocationType entity) {
        // the scene object will NOT know the changes, so let scene handle it.
        Objects.requireNonNull(entity, "locationTypeEntity is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new locationType object belongs no scene!");

        // we should know the new ID of the path.
        List<Long> currentLocationTypeIds = new ArrayList<>();
        if (scene.getLocationTypes() != null) {
            currentLocationTypeIds = scene.getLocationTypes().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addLocationType(entity);

        entity.setId(null);

        getService(SceneDto.class).update(scene);

        for (LocationType eachLocationType : scene.getLocationTypes()) {
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

    public LocationType updateLocationType(LocationType entity) {
        entity = Objects.requireNonNull(entity, "locationTypeEntity is null");

        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for locationType: " + entity.toString());

        final long id = entity.getId();
        Optional<LocationType> locationTypeOp = scene.getLocationTypeById(id);
        if (!locationTypeOp.isPresent()) {
            throw new TcsServerRuntimeException("Location Type [" + entity.getName() + "] to be updated is null from the scene");
        }
        LocationType type = locationTypeOp.get();
        type = entity;

        getService(LocationType.class).update(scene);

        return (LocationType) getService(LocationType.class)
                .update(Objects.requireNonNull(type, "locationType to be updated is null"));
    }

    public void deleteLocationType(LocationType entity) {
        LocationType locationType = Objects.requireNonNull(entity, "locationType to be deleted is null");

        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(),
                "the belonging scene is null for locationType: " + entity.toString());

        scene.removeLocationType(locationType);

        getService(SceneDto.class).update(scene);
    }

    // STATIC_ROUTES
    public Collection<StaticRoute> getAllStaticRoutesFromScene(long sceneId) {
        return getAllInOneSceneInternal(StaticRoute.class, sceneId).stream()
                .map(item -> (StaticRoute) item).collect(Collectors.toList());
    }

    public Collection<StaticRoute> getAllStaticRoutes() {
        return getAllInternal(StaticRoute.class).stream()
                .map(item -> (StaticRoute) item).collect(Collectors.toList());
    }

    public StaticRoute getStaticRoute(long staticRouteId) {
        return (StaticRoute) getOneInternal(StaticRoute.class, staticRouteId);
    }

    public StaticRoute createStaticRoute(StaticRoute entity) {
        // should create by scene service to have the correct connection relationship

        // the scene object will NOT know the changes, so let scene handle it.
        StaticRoute staticRoute = Objects.requireNonNull(entity, "new staticRoute object is null");
        SceneDto scene = Objects.requireNonNull(entity.getSceneDto(), "The new staticRoute object belongs no scene!");
        if (!staticRoute.isValid()) {
            throw new TcsServerRuntimeException("The static route is invalid!");
        }

        // we should know the new ID of the staticRoute.
        List<Long> currentStaticRouteIds = new ArrayList<>();
        if (scene.getStaticRoutes() != null) {
            currentStaticRouteIds = scene.getStaticRoutes().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addStaticRoute(staticRoute);

        getService(SceneDto.class).update(scene);

        for (StaticRoute eachStaticRoute : scene.getStaticRoutes()) {
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

    public StaticRoute updateStaticRoute(StaticRoute entity) {
        return (StaticRoute) getService(StaticRoute.class).update(Objects.requireNonNull(entity, "staticRoute to be updated is null"));
    }

    public void deleteStaticRoute(StaticRoute entity) {
        entity = Objects.requireNonNull(entity, "staticRoute is null");

        SceneDto scene = entity.getSceneDto();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (!scene.getStaticRouteById(id).isPresent()) {
            throw new TcsServerRuntimeException("The staticRoute is not belonging to the scene");
        }

        scene.removeStaticRoute(entity);

        getService(SceneDto.class).update(scene);
    }

    // VEHICLES
    public Collection<Vehicle> getAllVehiclesFromScene(long sceneId) {
        return getAllInOneSceneInternal(Vehicle.class, sceneId).stream()
                .map(item -> (Vehicle) item).collect(Collectors.toList());
    }

    public Collection<Vehicle> getAllVehicles() {
        return getAllInternal(Vehicle.class).stream()
                .map(item -> (Vehicle) item).collect(Collectors.toList());
    }

    public Vehicle getVehicle(long vehicleId) {
        return (Vehicle) getOneInternal(Vehicle.class, vehicleId);
    }

    public Vehicle createVehicle(Vehicle entity) {

        Vehicle vehicle = Objects.requireNonNull(entity, "new vehicle object is null");

        return (Vehicle) getService(Vehicle.class).create(entity);
    }

    public Vehicle updateVehicle(Vehicle entity) {
        return (Vehicle) getService(Vehicle.class).update(Objects.requireNonNull(entity, "vehicle to be updated is null"));
    }

    public void deleteVehicle(Vehicle entity) {
        entity = Objects.requireNonNull(entity, "vehicle is null");
        getService(Vehicle.class).delete(entity.getId());
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
        if (!path.getDestinationPoint().equals(point)) {
            throw new TcsServerRuntimeException("PointDto is not the path's destination.");
        }

        point.addIncomingPathDto(path);

        return point;
    }

    /**
     * Removes an incoming path from a point.
     *
     * @param point A reference to the point to be modified.
     * @param path A reference to the path.
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
        if (!path.getSourcePoint().equals(point)) {
            throw new TcsServerRuntimeException("PointDto is not the path's source.");
        }

        point.addOutgoingPathDto(path);

        return point;
    }

    /**
     * Removes an outgoing path from a point.
     *
     * @param point A reference to the point to be modified.
     * @param path A reference to the path.
     * @return The modified point.
     * @throws ObjectUnknownException If the referenced point or path do not
     * exist.
     */
    public PointDto removePointOutgoingPathDto(PointDto point,
                                               PathDto path)
            throws ObjectUnknownException {

        if (point == null) {
            throw new ObjectUnknownException("PointDto is null, PathDto: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("PathDto is null, PointDto: " + point);
        }

        point.removeOutgoingPathDto(path);

        return point;
    }
}
