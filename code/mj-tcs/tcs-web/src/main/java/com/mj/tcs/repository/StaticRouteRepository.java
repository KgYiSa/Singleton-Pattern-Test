/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.repository;

import com.mj.tcs.api.v1.dto.StaticRouteDto;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface StaticRouteRepository extends CrudRepository<StaticRouteDto, Long> {
    Iterable<StaticRouteDto> findAllBySceneDto(/*@Param("sceneId")*/ Long sceneId);
}
