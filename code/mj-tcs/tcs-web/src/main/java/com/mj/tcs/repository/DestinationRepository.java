package com.mj.tcs.repository;

import com.mj.tcs.api.dto.TransportOrderDto;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface DestinationRepository extends CrudRepository<TransportOrderDto.DestinationDto, Long> {
}
