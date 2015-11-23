package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.repository.DestinationRepository;
import com.mj.tcs.repository.TransportOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
@Component
public class ServiceStateOperating {

    @Autowired
    TransportOrderRepository transportOrderRepository;
    @Autowired
    DestinationRepository destinationRepository;

    public Collection<TransportOrderDto> getAllTransportOrders() {
        return (Collection) transportOrderRepository.findAll();
    }

    public Collection<TransportOrderDto> getAllTransportOrdersFromScene(long sceneId) {
//        return (Collection) transportOrderRepository.findAllByScene(sceneId);
        // TODO
        throw null;
    }

    public TransportOrderDto createTransportOrder(TransportOrderDto order) {
        return transportOrderRepository.save(Objects.requireNonNull(order,
                "Transport Order should Not be null"));
    }

    public TransportOrderDto getTransportOrder(long orderId) {
        return transportOrderRepository.findOne(orderId);
    }

    public void deleteTransportOrder(TransportOrderDto order) {
        transportOrderRepository.delete(order);
    }
}
