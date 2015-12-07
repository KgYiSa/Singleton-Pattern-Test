package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.SceneDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
@Component
public class ServiceStateOperating {
    // ID <--> SceneDto
    private Map<Long, SceneDto> sceneDtoRuntimeMapping = new LinkedHashMap<>();

    public boolean loadSceneDto(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        if (isSceneDtoRunning(sceneDto)) {
            return false; // Already running
        }

        // load kernel for the sceneDto
        // TODO:

        sceneDtoRuntimeMapping.put(idKey, sceneDto);
        return true;
    }

    public void unloadSceneDto(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        if (isSceneDtoRunning(sceneDto)) {
            // Stop kernel for the sceneDto
            // TODO:

            // remove it
            sceneDtoRuntimeMapping.remove(idKey);
        }
    }

    public boolean isSceneDtoRunning(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        return sceneDtoRuntimeMapping.containsKey(idKey);
    }

//    @Autowired
//    TransportOrderRepository transportOrderRepository;
//    @Autowired
//    DestinationRepository destinationRepository;
//
//    public Collection<TransportOrderDto> getAllTransportOrders() {
//        return (Collection) transportOrderRepository.findAll();
//    }
//
//    public Collection<TransportOrderDto> getAllTransportOrdersFromScene(long sceneId) {
////        return (Collection) transportOrderRepository.findAllByScene(sceneId);
//        // TODO
//        throw null;
//    }
//
//    public TransportOrderDto createTransportOrder(TransportOrderDto order) {
//        return transportOrderRepository.save(Objects.requireNonNull(order,
//                "Transport Order should Not be null"));
//    }
//
//    public TransportOrderDto getTransportOrder(long orderId) {
//        return transportOrderRepository.findOne(orderId);
//    }
//
//    public void deleteTransportOrder(TransportOrderDto order) {
//        transportOrderRepository.delete(order);
//    }
}
