/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.LocationDto;
import com.mj.tcs.data.model.Location;

/**
 * @author Wang Zhen
 */
public class LocationDto2LocationMatcher implements DtoToEntityMatcher<LocationDto, Location> {
    @Override
    public boolean match(LocationDto dto, Location entity) {
        return dto.getId() == entity.getId();
    }
}
