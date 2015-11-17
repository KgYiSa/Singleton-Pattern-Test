package com.mj.tcs.repository;

import com.mj.tcs.data.order.TransportOrder;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface TransportOrderRepository extends CrudRepository<TransportOrder, Long> {
    /**
     * Returns all instances of the type in the scene.
     *
     * @param scene the ID of the scene
     * @return all entities of the scene
     */
    Iterable<TransportOrder> findAllByScene(/*@Param("sceneId")*/ Long scene);
}
