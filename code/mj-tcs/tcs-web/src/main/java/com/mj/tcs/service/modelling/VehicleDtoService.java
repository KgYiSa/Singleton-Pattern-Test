package com.mj.tcs.service.modelling;

import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.VehicleDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
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
public class VehicleDtoService implements IEntityDtoService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (VehicleDto.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                VehicleDto entity = vehicleRepository.findOne(
                        (long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID)
                );
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) vehicleRepository.findAllBySceneDto(
                        (long) params.getParameter(ServiceGetParams.NAME_SCENE_ID)
                );
            case GET_ALL:
                return (Collection) vehicleRepository.findAll();
            default:
                throw new TcsServerRuntimeException("parameters in vehicle service is incorrect: " + params);
        }
    }

    @Override
    public VehicleDto create(BaseEntityDto entity) {
        if (entity instanceof VehicleDto) {
            entity = createVehicle((VehicleDto) entity);
            return (VehicleDto) entity;
        } else {
            throw new TcsServerRuntimeException("create vehicle with different entity type");
        }
    }

    @Override
    public VehicleDto update(BaseEntityDto entity) {
        if (entity instanceof VehicleDto) {
            entity = updateVehicle((VehicleDto) entity);
            return (VehicleDto) entity;
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

    private VehicleDto createVehicle(VehicleDto entity) {
        // TODO: Need to notify the current scene ?
        return vehicleRepository.save(entity);
//        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private VehicleDto updateVehicle(VehicleDto entity) {
        VehicleDto vehicle = (VehicleDto) Objects.requireNonNull(entity, "vehicle entity is null");
        PointDto currentPoint = Objects.requireNonNull(vehicle.getCurrentPosition(), "The vehicle object has no current point!");

        SceneDto scene = vehicle.getSceneDto();

        // check these components belongs to the scene
        if (currentPoint != null) {
            final long currentPointId = currentPoint.getId();
            if (scene.getPointDtoById(currentPointId) == null) {
                throw new TcsServerRuntimeException("The current point of the vehicle is not belonging to the scene");
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
