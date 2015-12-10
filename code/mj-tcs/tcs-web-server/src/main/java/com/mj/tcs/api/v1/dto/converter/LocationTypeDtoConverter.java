package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.LocationTypeDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.Numeric2PathConverter;
import com.mj.tcs.api.v1.dto.converter.value.converter.PointDto2PointMatcher;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.model.LocationType;
import com.mj.tcs.util.TcsDtoUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "LocationTypeDtoConverter")
public class LocationTypeDtoConverter implements DtoConverter  {

    private static final Map<String, Object> pointConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
        pointConverters.put("longToPath", new Numeric2PathConverter());
        pointConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());

        beanFactory.registerEntity("LocationType", "com.mj.tcs.data.model.LocationType", "com.mj.tcs.data.model.LocationType");
        beanFactory.registerDto("LocationTypeDto", "com.mj.tcs.api.v1.dto.LocationTypeDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof LocationType) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        LocationTypeDto LocationTypeDto = new LocationTypeDto();
        LocationType locationType = (LocationType) entity;

        DTOAssembler.newAssembler(LocationTypeDto.getClass(), locationType.getClass())
                .assembleDto(LocationTypeDto, locationType, null, null);
        return LocationTypeDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof LocationTypeDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        LocationTypeDto LocationTypeDto = (LocationTypeDto) dto;
        LocationType locationType = new LocationType();

        DTOAssembler.newAssembler(LocationTypeDto.getClass(), locationType.getClass())
                .assembleEntity(LocationTypeDto, locationType, null, null);
        return locationType;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof LocationTypeDto) {
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

        if (entity != null && entity instanceof LocationType) {
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
