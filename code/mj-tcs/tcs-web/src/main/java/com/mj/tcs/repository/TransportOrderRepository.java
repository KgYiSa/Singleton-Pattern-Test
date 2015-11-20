package com.mj.tcs.repository;

import com.mj.tcs.api.v1.dto.TransportOrderDto;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface TransportOrderRepository extends CrudRepository<TransportOrderDto, Long> {
    /**
     * Returns all instances of the type in the scene.
     *
     * @param scene the ID of the scene
     * @return all entities of the scene
     */
    // TODO
//    Iterable<TransportOrderDto> findAllByScene(/*@Param("sceneId")*/ Long scene);
}
