package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.data.model.Path;

/**
 * @author Wang Zhen
 */
public class PathDto2PathMatcher implements DtoToEntityMatcher<PathDto, Path> {
    @Override
    public boolean match(PathDto dto, Path entity) {
        return dto.getId() == entity.getId();
    }
}
