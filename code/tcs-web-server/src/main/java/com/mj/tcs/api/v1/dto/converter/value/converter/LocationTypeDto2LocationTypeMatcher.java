package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.LocationTypeDto;
import com.mj.tcs.data.model.LocationType;

/**
 * @author Wang Zhen
 */
public class LocationTypeDto2LocationTypeMatcher implements DtoToEntityMatcher<LocationTypeDto, LocationType> {
    @Override
    public boolean match(LocationTypeDto dto, LocationType entity) {
        return dto.getId() == entity.getId();
    }
}
