/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.LocationLinkDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.util.TcsDtoUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "LocationLinkDtoConverter")
public class LocationLinkDtoConverter extends DummyDtoConverter {

    private static final Map<String, Object> locationLinkConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
//        locationLinkConverters.put("longToPath", new Numeric2PathConverter());
//        locationLinkConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());

        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point",
                "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof Location.Link) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        LocationLinkDto locationLinkDto = new LocationLinkDto();
        Location.Link locationLink = (Location.Link) entity;

        DTOAssembler.newAssembler(locationLinkDto.getClass(), locationLink.getClass())
                .assembleDto(locationLinkDto, locationLink, locationLinkConverters, beanFactory);
        return locationLinkDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof LocationLinkDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        LocationLinkDto locationLinkDto = (LocationLinkDto) dto;
        Location.Link locationLink = new Location.Link();

        DTOAssembler.newAssembler(locationLinkDto.getClass(), locationLink.getClass())
                .assembleEntity(locationLinkDto, locationLink, locationLinkConverters, beanFactory);
        return locationLink;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof LocationLinkDto) {
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

        if (entity != null && entity instanceof Location.Link) {
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
