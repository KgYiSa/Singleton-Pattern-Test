package com.mj.tcs.repository;

import com.mj.tcs.api.v1.dto.VehicleDto;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface VehicleRepository extends CrudRepository<VehicleDto, Long> {
    /**
     * Returns all instances of the type in the scene.
     *
     * @param sceneId the ID of the scene
     * @return all entities of the scene
     */
    Iterable<VehicleDto> findAllBySceneDto(/*@Param("sceneId")*/ Long sceneId);
}
