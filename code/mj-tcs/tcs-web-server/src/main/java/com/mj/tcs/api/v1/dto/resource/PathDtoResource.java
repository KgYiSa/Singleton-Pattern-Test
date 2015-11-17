package com.mj.tcs.api.v1.dto.resource;

import com.mj.tcs.api.v1.dto.PathDto;
import org.springframework.hateoas.Resource;

/**
 * @author Wang Zhen
 */
public class PathDtoResource extends Resource {

    public PathDtoResource(PathDto path) {
        super(path);
    }
}