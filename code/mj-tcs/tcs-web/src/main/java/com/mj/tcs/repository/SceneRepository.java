package com.mj.tcs.repository;

import com.mj.tcs.data.model.Scene;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Wang Zhen
 */
public interface SceneRepository extends CrudRepository<Scene, Long> {

    @Query(value = "select s from Scene s where s.name = :sceneName")
    Scene findOneByName(@Param("sceneName") String sceneName);
}
