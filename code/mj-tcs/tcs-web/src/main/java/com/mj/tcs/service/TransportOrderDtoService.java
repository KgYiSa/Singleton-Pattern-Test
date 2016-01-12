package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.exception.TCSServerRuntimeException;
import com.mj.tcs.repository.TransportOrderDtoRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class TransportOrderDtoService {
    @Autowired
    private TransportOrderDtoRepository orderDtoRepository;

//    @Autowired
//    private SceneExtDtoRepository sceneExtDtoRepository;

    public Iterable<TransportOrderDto> findAll() {
        return orderDtoRepository.findAll();
    }

//    TODO:
//    public Iterable<TransportOrderDto> findAllFromAScene(long sceneId) {
//    }

    public TransportOrderDto create(TransportOrderDto dto) {
        Objects.requireNonNull(dto, "new transport order dto object is null");
//        TODO:

        return orderDtoRepository.save(dto);
    }

    public TransportOrderDto update(TransportOrderDto dto) {
        Objects.requireNonNull(dto, "updated transport order dto object is null");

        // TODO: merge && update creation & update time
//        dto.setCreatedAt(oldEntity.getCreatedAt());
//        dto.setCreatedBy(oldEntity.getCreatedBy());

        return orderDtoRepository.save(dto);
    }

    public void delete(long id) {
        orderDtoRepository.delete(id);
    }
}
