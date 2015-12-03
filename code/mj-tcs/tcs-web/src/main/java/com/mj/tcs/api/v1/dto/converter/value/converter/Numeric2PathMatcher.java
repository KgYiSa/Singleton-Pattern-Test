package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.data.model.Path;

/**
 * @author Wang Zhen
 */
public class Numeric2PathMatcher implements DtoToEntityMatcher<Long, Path> {
    @Override
    public boolean match(Long id, Path entity) {
        return id == entity.getId();
    }
}
