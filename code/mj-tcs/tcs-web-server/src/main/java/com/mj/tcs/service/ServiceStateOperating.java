package com.mj.tcs.service;

import com.mj.tcs.data.order.TransportOrder;
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

    public Collection<TransportOrder> getAllTransportOrders() {
        return (Collection) transportOrderRepository.findAll();
    }

    public Collection<TransportOrder> getAllTransportOrdersFromScene(long sceneId) {
        return (Collection) transportOrderRepository.findAllByScene(sceneId);
    }

    public TransportOrder createTransportOrder(TransportOrder order) {
        return transportOrderRepository.save(Objects.requireNonNull(order,
                "Transport Order should Not be null"));
    }

    public TransportOrder getTransportOrder(long orderId) {
        return transportOrderRepository.findOne(orderId);
    }

    public void deleteTransportOrder(TransportOrder order) {
        transportOrderRepository.delete(order);
    }
}
