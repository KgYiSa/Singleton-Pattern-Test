/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.StaticRouteDto;
import com.mj.tcs.data.model.StaticRoute;

/**
 * @author Wang Zhen
 */
public class StaticRouteDto2StaticRouteMatcher implements DtoToEntityMatcher<StaticRouteDto, StaticRoute> {
    @Override
    public boolean match(StaticRouteDto dto, StaticRoute entity) {
        return dto.getId() == entity.getId();
    }
}
