/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.service.modelling;

import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class LocationService implements IEntityService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (Location.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                Location entity = locationRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) locationRepository.findAllByScene((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) locationRepository.findAll();
            default:
                throw new IllegalArgumentException("parameters in location service is incorrect: " + params);
        }
    }

    @Override
    public Location create(BaseEntity entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    @Override
    public Location update(BaseEntity entity) {
        Location location = (Location) Objects.requireNonNull(entity, "location entity is null");

        Scene scene = location.getScene();

        final long id = location.getId();
        if (scene.getLocationById(id) == null) {
            throw new TcsServerRuntimeException("The location is not belonging to the scene " +
                    "or you can not create it by the method");
        }

        return locationRepository.save(location);
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }
}
