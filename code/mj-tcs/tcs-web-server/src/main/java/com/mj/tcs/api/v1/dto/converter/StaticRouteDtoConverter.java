/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.StaticRouteDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.model.StaticRoute;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.util.TcsDtoUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "StaticRouteDtoConverter")
public class StaticRouteDtoConverter extends DummyDtoConverter {

    private static final Map<String, Object> staticRouteConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
//        staticRouteConverters.put("longToPath", new Numeric2PathConverter());

//        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point", "com.mj.tcs.data.model.Point");
//        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");
//
//        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
//                "com.mj.tcs.data.base.Triple");
//        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof StaticRoute) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        StaticRouteDto staticRouteDto = new StaticRouteDto();
        StaticRoute staticRoute = (StaticRoute) entity;

        DTOAssembler.newAssembler(staticRouteDto.getClass(), staticRoute.getClass())
                .assembleDto(staticRouteDto, staticRoute, staticRouteConverters, beanFactory);
        return staticRouteDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof StaticRouteDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        StaticRouteDto staticRouteDto = (StaticRouteDto) dto;
        StaticRoute staticRoute = new StaticRoute();

        DTOAssembler.newAssembler(staticRouteDto.getClass(), staticRoute.getClass())
                .assembleEntity(staticRouteDto, staticRoute, staticRouteConverters, beanFactory);
        return staticRoute;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof StaticRouteDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {
        TcsDtoUtils.copyProperties(dto, properties);

        return dto;
    }

    @Override
    public boolean canMergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {

        if (entity != null && entity instanceof StaticRoute) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {
        TcsDtoUtils.copyProperties(entity, properties);

        return entity;
    }
}
