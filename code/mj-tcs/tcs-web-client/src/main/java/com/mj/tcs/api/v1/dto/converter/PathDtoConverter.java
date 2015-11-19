package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.value.converter.Numeric2PathConverter;
import com.mj.tcs.api.v1.dto.converter.value.converter.PointDto2PointMatcher;
import com.mj.tcs.data.model.Path;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.util.TcsBeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "PathDtoConverter")
public class PathDtoConverter extends DummyDtoConverter {

    private static final Map<String, Object> pathConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
        pathConverters.put("longToPath", new Numeric2PathConverter());
        pathConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());

        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point", "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof Path) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        PathDto pathDto = new PathDto();
        Path path = (Path) entity;

        DTOAssembler.newAssembler(pathDto.getClass(), path.getClass())
                .assembleDto(pathDto, path, pathConverters, beanFactory);
        return pathDto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof PathDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        PathDto pathDto = (PathDto) dto;
        Path path = new Path();

        DTOAssembler.newAssembler(pathDto.getClass(), path.getClass())
                .assembleEntity(pathDto, path, pathConverters, beanFactory);
        return path;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof PathDto) {
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

        if (entity != null && entity instanceof Path) {
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
