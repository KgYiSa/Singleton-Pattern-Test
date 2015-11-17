package com.mj.tcs.repository;

import com.mj.tcs.data.order.DriveOrder;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface DestinationRepository extends CrudRepository<DriveOrder.Destination, Long> {
}
