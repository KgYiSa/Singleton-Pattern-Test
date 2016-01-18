package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.exception.TCSServerRuntimeException;
import com.mj.tcs.repository.TransportOrderDtoRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Wang Zhen
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class TransportOrderDtoService {
    @Autowired
    private TransportOrderDtoRepository orderDtoRepository;

//    @Autowired
//    private SceneExtDtoRepository sceneExtDtoRepository;

    public Iterable<TransportOrderDto> findAll() {
        return orderDtoRepository.findAll();
    }

    public TransportOrderDto findByUUID(String uuid) {
        if (uuid == null) {
            return null;
        }
        return orderDtoRepository.findByUuid(uuid);
    }
//    TODO:
//    public Iterable<TransportOrderDto> findAllFromAScene(long sceneId) {
//    }

    public TransportOrderDto create(TransportOrderDto dto) {
        Objects.requireNonNull(dto, "new transport order dto object is null");
//        TODO:

        return orderDtoRepository.save(dto);
    }

    public TransportOrderDto update(final TransportOrderDto dto) {
        Objects.requireNonNull(dto, "updated transport order dto object is null");

        // merge && update creation & update time
        final TransportOrderDto orderDto = Objects.requireNonNull(orderDtoRepository.findByUuid(dto.getUUID()));

        orderDto.setExecutingVehicle(dto.getExecutingVehicle());

        orderDto.setOrderState(Objects.requireNonNull(dto.getOrderState()));
        final List<TransportOrderDto.DestinationDto> newDestList = dto.getDestinations();
        if (orderDto.getDestinations() != null && newDestList != null) {
            orderDto.getDestinations().forEach(v -> {
                final Optional<TransportOrderDto.DestinationDto> destinationDtoOp = newDestList.stream().filter(v2 -> Objects.equals(v2.getLocationUUID(), v.getLocationUUID())).findFirst();
            });
        }
//        dto.setCreatedAt(oldEntity.getCreatedAt());
//        dto.setCreatedBy(oldEntity.getCreatedBy());

        return orderDtoRepository.save(orderDto);
    }

    public void delete(long id) {
        orderDtoRepository.delete(id);
    }
}
