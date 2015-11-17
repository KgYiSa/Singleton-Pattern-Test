package com.mj.tcs.repository;

import com.mj.tcs.data.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    /**
     * Returns all instances of the type in the scene.
     *
     * @param sceneId the ID of the scene
     * @return all entities of the scene
     */
    Iterable<Vehicle> findAllBySceneId(/*@Param("sceneId")*/ Long sceneId);
}
