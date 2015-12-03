package com.mj.tcs.api.v1.dto.converter;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.mj.tcs.api.v1.dto.VehicleDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.util.TcsBeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Component(value = "VehicleDtoConverter")
public class VehicleDtoConverter extends DummyDtoConverter {

    private static final Map<String, Object> vehicleConverters = new HashMap<>();
    private static final SimpleMapExtensibleBeanFactory beanFactory = new SimpleMapExtensibleBeanFactory();

    static {
//        vehicleConverters.put("longToPath", new Numeric2PathConverter());
//        vehicleConverters.put("PointDto2PointMatcher", new PointDto2PointMatcher());

        beanFactory.registerEntity("Point", "com.mj.tcs.data.model.Point", "com.mj.tcs.data.model.Point");
        beanFactory.registerDto("PointDto", "com.mj.tcs.api.v1.dto.PointDto");

        beanFactory.registerEntity("Triple", "com.mj.tcs.data.base.Triple",
                "com.mj.tcs.data.base.Triple");
        beanFactory.registerDto("TripleDto", "com.mj.tcs.api.v1.dto.base.TripleDto");
    }

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        if (entity != null && entity instanceof Vehicle) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        VehicleDto dto = new VehicleDto();
        Vehicle specificEntity = (Vehicle) entity;

        DTOAssembler.newAssembler(dto.getClass(), specificEntity.getClass())
                .assembleDto(dto, specificEntity, vehicleConverters, beanFactory);
        return dto;
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {

        if (dto != null && dto instanceof VehicleDto) {
            return true;
        }

        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        VehicleDto entityDto  = (VehicleDto) dto;
        Vehicle entity = new Vehicle();

        DTOAssembler.newAssembler(entityDto.getClass(), entity.getClass())
                .assembleEntity(entityDto, entity, vehicleConverters, beanFactory);
        return entity;
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties) {

        if (dto != null && dto instanceof VehicleDto) {
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

        if (entity != null && entity instanceof Vehicle) {
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
