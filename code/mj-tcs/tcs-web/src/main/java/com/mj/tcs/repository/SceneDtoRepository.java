package com.mj.tcs.repository;

import com.mj.tcs.api.dto.SceneDto;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface SceneDtoRepository extends CrudRepository<SceneDto, Long> {

//    @Query(value = "select s from SceneDto s where s.name = :sceneName")
//    SceneDto findOneByName(@Param("sceneName") String sceneName);
}
