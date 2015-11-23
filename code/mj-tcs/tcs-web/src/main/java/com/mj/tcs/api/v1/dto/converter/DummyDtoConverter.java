package com.mj.tcs.api.v1.dto.converter;

import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.exception.TcsServerRuntimeException;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author Wang Zhen
 */
@Component
public abstract class DummyDtoConverter implements DtoConverter {

    @Override
    public boolean canConvertToDto(final BaseEntity entity) {
        return false;
    }

    @Override
    public Object convertToDto(final BaseEntity entity) {
        throw new TcsServerRuntimeException("DummyDtoConverter.convertToDto");
    }

    @Override
    public boolean canConvertToEntity(final BaseEntityDto dto) {
        return false;
    }

    @Override
    public Object convertToEntity(final BaseEntityDto dto) {
        throw new TcsServerRuntimeException("DummyDtoConverter.convertToEntity");
    }

    @Override
    public boolean canMergePropertiesToDto(final BaseEntityDto dto, Map<String, Object> properties) {
        return true;
    }

    @Override
    public Object mergePropertiesToDto(final BaseEntityDto dto, Map<String, Object> properties) {
        throw new TcsServerRuntimeException("DummyDtoConverter.mergePropertiesToDto");
    }

    @Override
    public boolean canMergePropertiesToEntity(final BaseEntity entity, Map<String, Object> properties) {
        return true;
    }

    @Override
    public Object mergePropertiesToEntity(final BaseEntity entity, Map<String, Object> properties) {
        throw new TcsServerRuntimeException("DummyDtoConverter.mergePropertiesToEntity");
    }
}
