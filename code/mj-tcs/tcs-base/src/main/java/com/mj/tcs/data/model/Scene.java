package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wang Zhen
 */
public class Scene extends BaseEntity implements Cloneable {

    private Set<Point> points = new HashSet<>();

    private Set<Path> paths = new HashSet<>();

    private Set<Location> locations = new HashSet<>();

    private Set<LocationType> locationTypes = new HashSet<>();

    private Set<StaticRoute> staticRoutes = new HashSet<>();

    private Set<Block> blocks = new HashSet<>();

    private Set<Vehicle> vehicles = new HashSet<>();

    public Scene() {
    }

    @Override
    public void clearId() {
        setId(null);
        if (getPoints() != null) {
            for (Point point : getPoints()) {
                point.clearId();
                if (point.getPosition() != null) {
                    point.getPosition().setId(null);
                }
            }
        }
        if (getPaths() != null) {
            getPaths().forEach(p -> p.clearId());
        }
        if (getLocations() != null) {
            for (Location location : getLocations()) {
                location.clearId();
                if (location.getAttachedLinks() != null) {
                    location.getAttachedLinks().forEach(l -> l.clearId());
                }
                if (location.getPosition() == null) {
                    throw new NullPointerException("location position is null");
                }
                location.getPosition().clearId();
                if (location.getType() != null) {
                    location.getType().clearId();
                }
            }
        }
        if (getLocationTypes() != null) {
            getLocationTypes().forEach(lt -> lt.clearId());
        }
        if (getStaticRoutes() != null) {
            getStaticRoutes().forEach(sr -> sr.clearId());
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String inName) {
        this.name = Objects.requireNonNull(inName, "name is null");
    }

    // point
    public Set<Point> getPoints() {
        return points;
    }

    public Optional<Point> getPointById(long id) {
        if (points == null) {
            return Optional.ofNullable(null);
        }

        return points.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Optional<Point> getPointByName(String name) {
        if (points == null) {
            return Optional.ofNullable(null);
        }

        return points.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param newPoint
     */
    public boolean addPoint(Point newPoint) {
        if (this.points == null) {
            this.points = new HashSet<>();
        }

        return this.points.add(Objects.requireNonNull(newPoint, "point to be added is null"));
    }

    public boolean removePoint(Point point) {
        if (this.points == null) {
            return false;
        }
        return this.points.remove(Objects.requireNonNull(point, "point to removed is null"));
    }

    public boolean removePointById(long id) {
        Optional<Point> point = getPointById(id);
        if (!point.isPresent()) {
            throw new NullPointerException("point can not be found by id " + id);
        }

        return removePoint(point.get());
    }

    // path
    public Set<Path> getPaths() {
        return paths;
    }

    public Optional<Path> getPathById(long id) {
        if (paths == null) {
            return Optional.ofNullable(null);
        }

        return paths.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Optional<Path> getPathByName(String name) {
        if (paths == null) {
            return Optional.ofNullable(null);
        }

        return paths.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param newPath
     */
    public boolean addPath(Path newPath) {
        if (this.paths == null) {
            this.paths = new HashSet<>();
        }

        return this.paths.add(Objects.requireNonNull(newPath, "path to be added is null"));
    }

    public boolean removePath(Path path) {
        if (this.paths == null) {
            return false;
        }

        return this.paths.remove(Objects.requireNonNull(path, "path to be removed is null"));
    }

    public boolean removePathById(long id) {
        Optional<Path> path = getPathById(id);
        if (!path.isPresent()) {
            throw new NullPointerException("path can not be found by id " + id);
        }

        return removePath(path.get());
    }

    // location type
    public Set<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public Optional<LocationType> getLocationTypeById(long id) {
        if (locationTypes == null) {
            return Optional.ofNullable(null);
        }

        return locationTypes.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Optional<LocationType> getLocationTypeByName(String name) {
        if (locationTypes == null) {
            return Optional.ofNullable(null);
        }

        return locationTypes.stream().filter(l -> l.getName().equals(name)).findFirst();
    }

    public void setLocationTypes(Set<LocationType> locationTypes) {
        this.locationTypes = locationTypes;
    }

    public boolean addLocationType(LocationType newLocationType) {
        if (this.locationTypes == null) {
            this.locationTypes = new HashSet<>();
        }

        return this.locationTypes.add(Objects.requireNonNull(newLocationType, "newLocationType to be added is null"));
    }

    public boolean removeLocationType(LocationType locationType) {
        if (this.locationTypes == null) {
            return false;
        }
        return this.locationTypes.remove(Objects.requireNonNull(locationType, "locationType to be removed is null"));
    }

    public boolean removeLocationTypeById(long id) {
        Optional<LocationType> locationType = getLocationTypeById(id);
        if (!locationType.isPresent()) {
            throw new NullPointerException("locationType can not be found by id " + id);
        }

        return removeLocationType(locationType.get());
    }

    // location
    public Set<Location> getLocations() {
        return locations;
    }

    public Optional<Location> getLocationById(long id) {
        if (locations == null) {
            return Optional.ofNullable(null);
        }

        return locations.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Optional<Location> getLocationByName(String name) {
        if (locations == null) {
            return Optional.ofNullable(null);
        }

        return locations.stream().filter(l -> l.getName().equals(name)).findFirst();
    }

    public boolean addLocation(Location location) {
        if (this.locations == null) {
            this.locations = new HashSet<>();
        }

        return this.locations.add(Objects.requireNonNull(location, "location to be added is null"));
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public boolean removeLocation(Location location) {
        if (this.locations == null) {
            return false;
        }
        return this.locations.remove(Objects.requireNonNull(location, "location to be removed is null"));
    }

    public boolean removeLocationById(long id) {
        Optional<Location> location = getLocationById(id);
        if (!location.isPresent()) {
            throw new NullPointerException("location can not be found by id " + id);
        }

        return removeLocation(location.get());
    }

    // static routes
    public Set<StaticRoute> getStaticRoutes() {
        return staticRoutes;
    }

    public Optional<StaticRoute> getStaticRouteById(long id) {
        if (staticRoutes == null) {
            return Optional.ofNullable(null);
        }

        return staticRoutes.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Optional<StaticRoute> getStaticRouteByName(String name) {
        if (staticRoutes == null) {
            return Optional.ofNullable(null);
        }

        return staticRoutes.stream().filter(s -> s.getName().equals(name)).findFirst();
    }

    public boolean addStaticRoute(StaticRoute staticRoute) {
        if (this.staticRoutes == null) {
            this.staticRoutes = new HashSet<>();
        }

        return this.staticRoutes.add(Objects.requireNonNull(staticRoute, "staticRoute to be added is null"));
    }

    public void setStaticRoutes(Set<StaticRoute> staticRoutes) {
        this.staticRoutes = staticRoutes;
    }

    public boolean removeStaticRoute(StaticRoute staticRoute) {
        if (this.staticRoutes == null) {
            return false;
        }
        return this.staticRoutes.remove(Objects.requireNonNull(staticRoute, "staticRoute to be removed is null"));
    }

    public boolean removeStaticRouteById(long id) {
        Optional<StaticRoute> staticRoute = getStaticRouteById(id);
        if (!staticRoute.isPresent()) {
            throw new NullPointerException("static route can not be found by id " + id);
        }

        return removeStaticRoute(staticRoute.get());
    }

    // vehicles
    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public Optional<Vehicle> getVehicleById(long id) {
        if (vehicles == null) {
            return Optional.ofNullable(null);
        }

        return vehicles.stream().filter(v -> v.getId() == id).findFirst();
    }

    public Optional<Vehicle> getVehicleByName(String name) {
        if (vehicles == null) {
            return Optional.ofNullable(null);
        }

        return vehicles.stream().filter(v -> v.getName().equals(name)).findFirst();
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (this.vehicles == null) {
            this.vehicles = new HashSet<>();
        }

        return this.vehicles.add(Objects.requireNonNull(vehicle, "vehicle to be added is null"));
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public boolean removeVehicle(Vehicle vehicle) {
        if (this.vehicles == null) {
            return false;
        }
        return this.vehicles.remove(Objects.requireNonNull(vehicle, "vehicle to be removed is null"));
    }

    public boolean removeVehicleById(long id) {
        Optional<Vehicle> vehicle = getVehicleById(id);
        if (!vehicle.isPresent()) {
            throw new NullPointerException("vehicle can not be found by id " + id);
        }

        return removeVehicle(vehicle.get());
    }

    // blocks
    public Set<Block> getBlocks() {
        return blocks;
    }

    public Optional<Block> getBlockById(long id) {
        if (blocks == null) {
            return Optional.ofNullable(null);
        }

        return blocks.stream().filter(b -> b.getId() == id).findFirst();
    }

    public Optional<Block> getBlockByName(String name) {
        if (blocks == null) {
            return Optional.ofNullable(null);
        }

        return blocks.stream().filter(b -> b.getName().equals(name)).findFirst();
    }

    public boolean addBlock(Block block) {
        if (this.blocks == null) {
            this.blocks = new HashSet<>();
        }

        return this.blocks.add(Objects.requireNonNull(block, "block to be added is null"));
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    public boolean removeBlock(Block block) {
        if (this.blocks == null) {
            return false;
        }
        return this.blocks.remove(Objects.requireNonNull(block, "block to be removed is null"));
    }

    public boolean removeBlockById(long id) {
        Optional<Block> block = getBlockById(id);
        if (!block.isPresent()) {
            throw new NullPointerException("block can not be found by id " + id);
        }

        return removeBlock(block.get());
    }

    @Override
    public Scene clone() {
        Scene clone = null;
        clone = (Scene) super.clone();
        if (points != null) {
            for (Point point : points) {
                clone.addPoint(point.clone());
            }
        }
        if (paths != null) {
            for (Path path : paths) {
                clone.addPath(path.clone());
            }
        }
        if (locations != null) {
            for (Location location : locations) {
                clone.addLocation(location.clone());
            }
        }
        if (locationTypes != null) {
            for (LocationType type : locationTypes) {
                clone.addLocationType(type.clone());
            }
        }
        if (staticRoutes != null) {
            for (StaticRoute route : staticRoutes) {
                clone.addStaticRoute(route.clone());
            }
        }
        if (vehicles != null) {
            for (Vehicle vehicle : vehicles) {
                clone.addVehicle(vehicle.clone());
            }
        }
        if (blocks != null) {
            for (Block block : blocks) {
                clone.addBlock(block.clone());
            }
        }

        return clone;
    }
}
