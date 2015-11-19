/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.resource;

import com.mj.tcs.api.v1.dto.StaticRouteDto;
import org.springframework.hateoas.Resource;

/**
 * @author Wang Zhen
 */
public class StaticRouteDtoResource extends Resource {

    public StaticRouteDtoResource(StaticRouteDto dto) {
        super(dto);
    }
}