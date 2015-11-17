package com.mj.tcs.service.modelling;

import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class VehicleService implements IEntityService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (Vehicle.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                Vehicle entity = vehicleRepository.findOne(
                        (long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID)
                );
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) vehicleRepository.findAllBySceneId(
                        (long) params.getParameter(ServiceGetParams.NAME_SCENE_ID)
                );
            case GET_ALL:
                return (Collection) vehicleRepository.findAll();
            default:
                throw new TcsServerRuntimeException("parameters in vehicle service is incorrect: " + params);
        }
    }

    @Override
    public Vehicle create(BaseEntity entity) {
        if (entity instanceof Vehicle) {
            entity = createVehicle((Vehicle) entity);
            return (Vehicle) entity;
        } else {
            throw new TcsServerRuntimeException("create vehicle with different entity type");
        }
    }

    @Override
    public Vehicle update(BaseEntity entity) {
        if (entity instanceof Vehicle) {
            entity = updateVehicle((Vehicle) entity);
            return (Vehicle) entity;
        } else {
            throw new TcsServerRuntimeException("update vehicle with different entity type");
        }
    }

    @Override
    public void delete(Long id) {
        // TODO: Need to check the vehicle's state?
        vehicleRepository.delete(id);
//        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }

    private Vehicle createVehicle(Vehicle entity) {
        // TODO: Need to notify the current scene ?
        return vehicleRepository.save(entity);
//        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private Vehicle updateVehicle(Vehicle entity) {
        Vehicle vehicle = (Vehicle) Objects.requireNonNull(entity, "vehicle entity is null");
        Point currentPoint = Objects.requireNonNull(vehicle.getCurrentPosition(), "The vehicle object has no current point!");
        Point nextPoint = vehicle.getNextPosition();

        Scene scene = vehicle.getScene();

        // check these components belongs to the scene
        if (currentPoint != null) {
            final long currentPointId = currentPoint.getId();
            if (!scene.getPointById(currentPointId).isPresent()) {
                throw new TcsServerRuntimeException("The current point of the vehicle is not belonging to the scene");
            }

            if (nextPoint != null) {
                final long nextPointId = nextPoint.getId();
                if (!scene.getPointById(nextPointId).isPresent()) {
                    throw new TcsServerRuntimeException("The next point of the vehicle is not belonging to the scene");
                }

                if (currentPointId == nextPointId) {
                    throw new TcsServerRuntimeException("The current point and the next point of the vehicle is the same");
                }
            }
        }

        return vehicleRepository.save(vehicle);
//        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private void checkNullException(Object entry, String message) {
        if (entry == null) {
            throw new ObjectUnknownException(message);
        }
    }

}
