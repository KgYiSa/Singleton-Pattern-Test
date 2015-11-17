package com.mj.tcs.api.v1.dto.converter;

import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.data.base.BaseEntity;

import java.util.Map;

/**
 * @author Wang Zhen
 */
public interface DtoConverter {
    public boolean canConvertToDto(final BaseEntity entity);
    public Object convertToDto(final BaseEntity entity);

    public boolean canConvertToEntity(final BaseEntityDto dto);
    public Object convertToEntity(final BaseEntityDto dto);

    public boolean canMergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties);
    public Object mergePropertiesToDto(final BaseEntityDto dto, final Map<String, Object> properties);

    public boolean canMergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties);
    public Object mergePropertiesToEntity(final BaseEntity entity, final Map<String, Object> properties);
}
