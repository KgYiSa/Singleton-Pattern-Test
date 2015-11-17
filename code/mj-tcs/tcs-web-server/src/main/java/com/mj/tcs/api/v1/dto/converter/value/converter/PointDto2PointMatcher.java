package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.data.model.Point;

/**
 * @author Wang Zhen
 */
public class PointDto2PointMatcher implements DtoToEntityMatcher<PointDto, Point> {
    @Override
    public boolean match(PointDto pointDto, Point entity) {
        return pointDto.getId() == entity.getId();
    }
}
