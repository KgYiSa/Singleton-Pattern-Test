package com.mj.tcs.repository;

import com.mj.tcs.api.v1.dto.DestinationDto;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wang Zhen
 */
public interface DestinationRepository extends CrudRepository<DestinationDto, Long> {
}
