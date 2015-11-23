/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.service.modelling;

import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.StaticRouteDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
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
public class StaticRouteDtoService implements IEntityDtoService {

    @Autowired
    StaticRouteRepository staticRouteRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (StaticRouteDto.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                StaticRouteDto entity = staticRouteRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) staticRouteRepository.findAllBySceneDto((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) staticRouteRepository.findAll();
            default:
                throw new TcsServerRuntimeException("parameters in staticRoute service is incorrect: " + params);
        }
    }

    @Override
    public StaticRouteDto create(BaseEntityDto entity) {
        if (entity instanceof StaticRouteDto) {
            entity = createStaticRoute((StaticRouteDto) entity);
            return (StaticRouteDto) entity;
        } else {
            throw new TcsServerRuntimeException("create staticRoute with different valueconverter type");
        }
    }

    @Override
    public StaticRouteDto update(BaseEntityDto entity) {
        if (entity instanceof StaticRouteDto) {
            entity = updateStaticRoute((StaticRouteDto) entity);
            return (StaticRouteDto) entity;
        } else {
            throw new TcsServerRuntimeException("update staticRoute with different valueconverter type");
        }
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }

    private StaticRouteDto createStaticRoute(StaticRouteDto entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private StaticRouteDto updateStaticRoute(StaticRouteDto entity) {
        StaticRouteDto staticRoute = (StaticRouteDto) Objects.requireNonNull(entity, "staticRoute entity is null");
        PointDto srcPoint = Objects.requireNonNull(staticRoute.getSourcePoint(), "The staticRoute object belongs no source point!");
        PointDto dstPoint = Objects.requireNonNull(staticRoute.getDestinationPoint(), "The staticRoute object belongs no destination point!");

        SceneDto scene = staticRoute.getSceneDto();

        // check these components belongs to the scene
        final long id = staticRoute.getId();
        if (scene.getStaticRouteDtoById(id) == null) {
            throw new TcsServerRuntimeException("The staticRoute is not belonging to the scene " +
                    "or you can not create it by the method");
        }
        final long srcPointId = srcPoint.getId();
        if (scene.getPointDtoById(srcPointId) == null) {
            throw new TcsServerRuntimeException("The source point of the staticRoute is not belonging to the scene");
        }
        final long dstPointId = dstPoint.getId();
        if (scene.getPointDtoById(dstPointId) == null) {
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
