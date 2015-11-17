/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.repository;

import com.mj.tcs.data.model.StaticRoute;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface StaticRouteRepository extends CrudRepository<StaticRoute, Long> {
    Iterable<StaticRoute> findAllBySceneId(/*@Param("sceneId")*/ Long sceneId);
}
