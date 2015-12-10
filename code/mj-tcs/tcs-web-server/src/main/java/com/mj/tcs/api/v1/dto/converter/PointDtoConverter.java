package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.Numeric2PathConverter;
import com.mj.tcs.api.v1.dto.converter.value.converter.PointDto2PointMatcher;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.util.TcsDtoUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "PointDtoConverter")
public class PointDtoConverter implements DtoConverter  {

    private static final Map<String, Object> pointConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
        pointConverters.put("longToPath", new Numeric2PathConverter());
        pointConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());

        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point", "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");

    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof Point) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        PointDto pointDto = new PointDto();
        Point point = (Point) entity;

        DTOAssembler.newAssembler(pointDto.getClass(), point.getClass())
                .assembleDto(pointDto, point, pointConverters, beanFactory);
        return pointDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof PointDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        PointDto pointDto = (PointDto) dto;
        Point point = new Point();

        DTOAssembler.newAssembler(pointDto.getClass(), point.getClass())
                .assembleEntity(pointDto, point, pointConverters, beanFactory);
        return point;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof PointDto) {
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

        if (entity != null && entity instanceof Point) {
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
