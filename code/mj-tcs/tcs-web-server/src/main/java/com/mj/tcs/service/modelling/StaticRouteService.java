/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.service.modelling;

import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.model.StaticRoute;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.repository.StaticRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class StaticRouteService implements IEntityService {

    @Autowired
    StaticRouteRepository staticRouteRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (StaticRoute.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                StaticRoute entity = staticRouteRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) staticRouteRepository.findAllBySceneId((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) staticRouteRepository.findAll();
            default:
                throw new TcsServerRuntimeException("parameters in staticRoute service is incorrect: " + params);
        }
    }

    @Override
    public StaticRoute create(BaseEntity entity) {
        if (entity instanceof StaticRoute) {
            entity = createStaticRoute((StaticRoute) entity);
            return (StaticRoute) entity;
        } else {
            throw new TcsServerRuntimeException("create staticRoute with different valueconverter type");
        }
    }

    @Override
    public StaticRoute update(BaseEntity entity) {
        if (entity instanceof StaticRoute) {
            entity = updateStaticRoute((StaticRoute) entity);
            return (StaticRoute) entity;
        } else {
            throw new TcsServerRuntimeException("update staticRoute with different valueconverter type");
        }
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }

    private StaticRoute createStaticRoute(StaticRoute entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private StaticRoute updateStaticRoute(StaticRoute entity) {
        StaticRoute staticRoute = (StaticRoute) Objects.requireNonNull(entity, "staticRoute entity is null");
        Point srcPoint = Objects.requireNonNull(staticRoute.getSourcePoint(), "The staticRoute object belongs no source point!");
        Point dstPoint = Objects.requireNonNull(staticRoute.getDestinationPoint(), "The staticRoute object belongs no destination point!");

        Scene scene = staticRoute.getScene();

        // check these components belongs to the scene
        final long id = staticRoute.getId();
        if (!scene.getStaticRouteById(id).isPresent()) {
            throw new TcsServerRuntimeException("The staticRoute is not belonging to the scene " +
                    "or you can not create it by the method");
        }
        final long srcPointId = srcPoint.getId();
        if (!scene.getPointById(srcPointId).isPresent()) {
            throw new TcsServerRuntimeException("The source point of the staticRoute is not belonging to the scene");
        }
        final long dstPointId = dstPoint.getId();
        if (!scene.getPointById(dstPointId).isPresent()) {
            throw new TcsServerRuntimeException("The destination point of the staticRoute is not belonging to the scene");
        }

        return staticRouteRepository.save(staticRoute);
//        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private void checkNullException(Object entry, String message) {
        if (entry == null) {
            throw new ObjectUnknownException(message);
        }
    }

}
