/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.util.TcsBeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "TransportOrderDtoConverter")
public class TransportOrderDtoConverter extends DummyDtoConverter {

    private static final Map<String, Object> transportOrderConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
//        transportOrderConverters.put("longToPath", new Numeric2PathConverter());
//        transportOrderConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());

        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point", "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");


        beanFactory.registerEntity("Destination", "com.mj.tcs.data.order.DriveOrder$Destination",
                "com.mj.tcs.data.order.DriveOrder$Destination");
        beanFactory.registerDto("DestinationDto", "com.mj.tcs.api.v1.dto.DestinationDto");

        beanFactory.registerEntity("Location", "com.mj.tcs.data.model.Location",
                "com.mj.tcs.data.model.Location");
        beanFactory.registerDto("LocationDto", "com.mj.tcs.api.v1.dto.LocationDto");

        beanFactory.registerEntity("TransportOrder", "com.mj.tcs.data.order.TransportOrder",
                "com.mj.tcs.data.order.TransportOrder");
        beanFactory.registerDto("TransportOrderDto", "com.mj.tcs.api.v1.dto.TransportOrderDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof TransportOrder) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        TransportOrderDto dto = new TransportOrderDto();
        TransportOrder specificEntity = (TransportOrder) entity;

        DTOAssembler.newAssembler(dto.getClass(), specificEntity.getClass())
                .assembleDto(dto, specificEntity, transportOrderConverters, beanFactory);

        if (specificEntity.getIntendedVehicle() != null) {
            dto.setIntendedVehicle(specificEntity.getIntendedVehicle().getId());
        }
        return dto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof TransportOrderDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        TransportOrderDto entityDto  = (TransportOrderDto) dto;
        TransportOrder entity = new TransportOrder();

        DTOAssembler.newAssembler(entityDto.getClass(), entity.getClass())
                .assembleEntity(entityDto, entity, transportOrderConverters, beanFactory);

        return entity;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof TransportOrderDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {
        TcsBeanUtils.copyProperties(dto, properties);

        return dto;
    }

    @Override
    public boolean canMergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {

        if (entity != null && entity instanceof TransportOrder) {
            return true;
        }

        return false;
    }

    @Override
    public Object mergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties) {
        TcsBeanUtils.copyProperties(entity, properties);

        return entity;
    }
}
