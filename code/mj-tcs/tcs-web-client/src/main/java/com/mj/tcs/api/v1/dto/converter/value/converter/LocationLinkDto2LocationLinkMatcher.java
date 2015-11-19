package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.LocationLinkDto;
import com.mj.tcs.data.model.Location;

/**
 * @author Wang Zhen
 */
public class LocationLinkDto2LocationLinkMatcher implements DtoToEntityMatcher<LocationLinkDto, Location.Link> {
    @Override
    public boolean match(LocationLinkDto dto, Location.Link entity) {
        return dto.getId() == entity.getId();
    }
}
