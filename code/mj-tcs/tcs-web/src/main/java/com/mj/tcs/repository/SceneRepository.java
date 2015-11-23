package com.mj.tcs.repository;

import com.mj.tcs.api.v1.dto.SceneDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Wang Zhen
 */
public interface SceneRepository extends CrudRepository<SceneDto, Long> {

//    @Query(value = "select s from SceneDto s where s.name = :sceneName")
//    SceneDto findOneByName(@Param("sceneName") String sceneName);
}
