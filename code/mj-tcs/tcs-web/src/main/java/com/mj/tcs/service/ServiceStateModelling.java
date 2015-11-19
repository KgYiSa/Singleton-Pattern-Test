package com.mj.tcs.service;

import com.mj.tcs.config.AppContext;
import com.mj.tcs.data.model.*;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.service.modelling.IEntityService;
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
    private static Map<String, IEntityService> modellingServiceMapping = new CaseInsensitiveMap();

    public void registerService(String key, IEntityService service) {
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
    public Collection<Point> getAllPointsFromScene(long sceneId) {
        return getAllInOneSceneInternal(Point.class, sceneId).stream()
                .map(item -> (Point) item).collect(Collectors.toList());
    }

    public Point getPoint(long pointId) {
        return (Point) getOneInternal(Point.class, pointId);
    }

    public Collection<Point> getAllPoints() {
        return getAllInternal(Point.class).stream()
                .map(item -> (Point) item).collect(Collectors.toList());
    }

    public Point createPoint(Point entity) {
        // should create by scene service to have the correct connection relationship
        entity = Objects.requireNonNull(entity, "pointEntity is null");
        Scene scene = Objects.requireNonNull(entity.getScene(),
                "the belonging scene is null for point: " + entity.toString());

        // the scene object will NOT know the changes, so let scene handle it.
//        Point point = (Point) getService(Point.class).create(entity);

        // we should know the new ID of the point.
        List<Long> currentPointIds = new ArrayList<>();
        if (scene.getPoints() != null) {
            currentPointIds = scene.getPoints().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addPoint(entity);

        getService(Scene.class).update(scene);

        for (Point point : scene.getPoints()) {
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

    public Point updatePoint(Point entity) {
        entity = Objects.requireNonNull(entity, "pointEntity is null");

        Scene scene = Objects.requireNonNull(entity.getScene(),
                "the belonging scene is null for point: " + entity.toString());

        final long id = entity.getId();
        Optional<Point> pointOp = scene.getPointById(id);
        if (!pointOp.isPresent()) {
            throw new TcsServerRuntimeException("The point is null by Id " + id + " for scene " + scene.getName());
        }

        Point point = pointOp.get();
        point = entity;

        getService(Point.class).update(scene);

        return (Point) getService(Point.class)
                .update(Objects.requireNonNull(point, "point to be updated is null"));
    }

    public void deletePoint(Point entity) {
        Point point = Objects.requireNonNull(entity, "point to be deleted is null");

        Scene scene = Objects.requireNonNull(entity.getScene(),
                "the belonging scene is null for point: " + entity.toString());

        // spring will remove all path objects according to the cascadeType
        scene.removePoint(point);

        getService(Scene.class).update(scene);
    }

    // PATHS
    public Collection<Path> getAllPathsFromScene(long sceneId) {
        return getAllInOneSceneInternal(Path.class, sceneId).stream()
                .map(item -> (Path) item).collect(Collectors.toList());
    }

    public Collection<Path> getAllPaths() {
        return getAllInternal(Path.class).stream()
                .map(item -> (Path) item).collect(Collectors.toList());
    }

    public Path getPath(long pathId) {
        return (Path) getOneInternal(Path.class, pathId);
    }

    public Path createPath(Path entity) {
        // should create by scene service to have the correct connection relationship

        // the scene object will NOT know the changes, so let scene handle it.
        Path path = Objects.requireNonNull(entity, "new path object is null");
        Scene scene = Objects.requireNonNull(entity.getScene(), "The new path object belongs no scene!");
        Point srcPoint = Objects.requireNonNull(entity.getSourcePoint(), "The new path object belongs no source point!");
        Point dstPoint = Objects.requireNonNull(entity.getDestinationPoint(), "The new path object belongs no destination point!");

        // we should know the new ID of the path.
        List<Long> currentPathIds = new ArrayList<>();
        if (scene.getPaths() != null) {
            currentPathIds = scene.getPaths().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addPath(path);

        // system helps do the following connections
//        srcPoint.addOutgoingPath(path);
//        dstPoint.addIncomingPath(path);

        getService(Scene.class).update(scene);

        for (Path eachPath : scene.getPaths()) {
            boolean found = false;
            for (Long existedId : currentPathIds) {
                if (existedId == eachPath.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return eachPath;
            }
        }

        throw new TcsServerRuntimeException("New path creation error");
    }

    public Path updatePath(Path entity) {
        return (Path) getService(Path.class).update(Objects.requireNonNull(entity, "path to be updated is null"));
    }

    public void deletePath(Path entity) {
        entity = Objects.requireNonNull(entity, "path is null");
        Point srcPoint = Objects.requireNonNull(entity.getSourcePoint(), "The path object belongs no source point!");
        Point dstPoint = Objects.requireNonNull(entity.getDestinationPoint(), "The path object belongs no destination point!");

        Scene scene = entity.getScene();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (!scene.getPathById(id).isPresent()) {
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

        scene.removePath(entity);

        getService(Scene.class).update(scene);
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
        Scene scene = Objects.requireNonNull(entity.getScene(), "The new location object belongs no scene!");
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

        getService(Scene.class).update(scene);

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

        Scene scene = entity.getScene();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (scene.getLocationById(id) == null) {
            throw new TcsServerRuntimeException("The location is not belonging to the scene");
        }

        scene.removeLocation(entity);

        getService(Scene.class).update(scene);
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
        Scene scene = Objects.requireNonNull(entity.getScene(), "The new locationType object belongs no scene!");

        // we should know the new ID of the path.
        List<Long> currentLocationTypeIds = new ArrayList<>();
        if (scene.getLocationTypes() != null) {
            currentLocationTypeIds = scene.getLocationTypes().stream()
                    .map(item -> item.getId()).collect(Collectors.toList());
        }
        scene.addLocationType(entity);

        entity.setId(null);

        getService(Scene.class).update(scene);

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

        Scene scene = Objects.requireNonNull(entity.getScene(),
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

        Scene scene = Objects.requireNonNull(entity.getScene(),
                "the belonging scene is null for locationType: " + entity.toString());

        scene.removeLocationType(locationType);

        getService(Scene.class).update(scene);
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
        Scene scene = Objects.requireNonNull(entity.getScene(), "The new staticRoute object belongs no scene!");
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

        getService(Scene.class).update(scene);

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

        Scene scene = entity.getScene();

        // check these components belongs to the scene
        final long id = entity.getId();
        if (!scene.getStaticRouteById(id).isPresent()) {
            throw new TcsServerRuntimeException("The staticRoute is not belonging to the scene");
        }

        scene.removeStaticRoute(entity);

        getService(Scene.class).update(scene);
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
    public Scene getScene(long sceneId) {
        return (Scene) getOneInternal(Scene.class, sceneId);
    }

    public Collection<Scene> getAllScenes() {
        return getAllInternal(Scene.class).stream()
                .map(item -> (Scene)item).collect(Collectors.toList());
    }

    public Scene createScene(Scene entity) {
        return (Scene) getService(Scene.class).create(entity);
    }

    public Scene updateScene(Scene entity) {
        return (Scene) getService(Scene.class).update(entity);
    }

    public void deleteScene(long sceneId) {
        getService(Scene.class).delete(sceneId);
    }


    // PRIVATE

    private IEntityService getService(Class entityClass) {
        if (entityClass == null) {
            throw new TcsServerRuntimeException("getService with a null class !");
        }

        String name = entityClass.getSimpleName();
        if (modellingServiceMapping.get(name) == null) {
            Map<String, IEntityService> services = AppContext.getContext().getBeansOfType(IEntityService.class);

            if (services != null) {
                for (IEntityService service : services.values()) {
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
    private Point addPointIncomingPath(Point point, Path path) {

        if (point == null) {
            throw new ObjectUnknownException("Point is null, Path: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("Path is null, Point: " + point);
        }

        // Check if the point really is the path's destination.
        if (!path.getDestinationPoint().equals(point)) {
            throw new TcsServerRuntimeException("Point is not the path's destination.");
        }

        point.addIncomingPath(path);

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
    public Point removePointIncomingPath(Point point,
                                               Path path) throws ObjectUnknownException {
        if (point == null) {
            throw new ObjectUnknownException("Point is null, Path: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("Path is null, Point: " + point);
        }

        point.removeIncomingPath(path);

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
    private Point addPointOutgoingPath(Point point, Path path) {

        if (point == null) {
            throw new ObjectUnknownException("Point is null, Path: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("Path is null, Point: " + point);
        }

        // Check if the point really is the path's source.
        if (!path.getSourcePoint().equals(point)) {
            throw new TcsServerRuntimeException("Point is not the path's source.");
        }

        point.addOutgoingPath(path);

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
    public Point removePointOutgoingPath(Point point,
                                               Path path)
            throws ObjectUnknownException {

        if (point == null) {
            throw new ObjectUnknownException("Point is null, Path: " + path);
        }

        if (path == null) {
            throw new ObjectUnknownException("Path is null, Point: " + point);
        }

        point.removeOutgoingPath(path);

        return point;
    }
}
