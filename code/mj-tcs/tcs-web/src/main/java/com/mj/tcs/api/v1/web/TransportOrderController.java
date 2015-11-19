/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.api.v1.dto.base.EntityAuditDto;
import com.mj.tcs.api.v1.dto.converter.DtoConverter;
import com.mj.tcs.api.v1.dto.resource.TransportOrderDtoResourceAssembler;
import com.mj.tcs.exception.ObjectAccessViolationException;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.order.TransportOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@RestController
@ExposesResourceFor(TransportOrderController.class)
@RequestMapping("/api/v1")
public class TransportOrderController extends ServiceController {

    @Autowired
    @Qualifier(value = "TransportOrderDtoConverter")
    private DtoConverter dtoConverter;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/scenes/{sceneId}/transport_orders", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTransportOrdersFromOneScene(@PathVariable("sceneId") Long sceneId) {
        Collection<TransportOrder> transportOrderEntities = getOperatingService().getAllTransportOrdersFromScene(sceneId);
        if (transportOrderEntities == null || transportOrderEntities.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TransportOrderDto> transportOrderDtos = transportOrderEntities.stream()
                .map(item -> (TransportOrderDto) dtoConverter.convertToDto(item)).collect(Collectors.toList());

        return new ResponseEntity<>(
                new Resources<>(
                        new TransportOrderDtoResourceAssembler().toResources(transportOrderDtos)
                ),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/transport_orders", method = RequestMethod.POST)
    public ResponseEntity<?> createTransportOrder(@PathVariable("sceneId") Long sceneId,
                                         @RequestBody TransportOrderDto transportOrderDto) throws ObjectUnknownException{
        Scene scene = null;
        if (sceneId >= 0) {
            scene = Objects.requireNonNull(getModellingService().getScene(sceneId),
                    "scene is null by the sceneId: " + sceneId);
        }

        TransportOrder newTransportOrder = (TransportOrder) dtoConverter.convertToEntity(transportOrderDto);
        newTransportOrder.setScene(scene);

        if (transportOrderDto.getIntendedVehicleId() > 0) {
            newTransportOrder.setIntendedVehicle(Objects.requireNonNull(getModellingService().getVehicle(transportOrderDto.getIntendedVehicleId()),
                    "the vehicle can not be found by id " + transportOrderDto.getIntendedVehicleId()));
        } else {
            newTransportOrder.setIntendedVehicle(null);
        }

        newTransportOrder.clearId();

        newTransportOrder = getOperatingService().createTransportOrder(newTransportOrder);

        return new ResponseEntity<>(
                new TransportOrderDtoResourceAssembler().toResource((TransportOrderDto) dtoConverter.convertToDto(newTransportOrder)),
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/scenes/{sceneId}/transport_orders/{transportOrderId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOneTransportOrder(@PathVariable("sceneId") Long sceneId,
                                         @PathVariable("transportOrderId") Long transportOrderId) {
        checkAccessViolation(sceneId, transportOrderId);

        TransportOrder transportOrder = getOperatingService().getTransportOrder(transportOrderId);

        if (transportOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new TransportOrderDtoResourceAssembler().toResource(
                        (TransportOrderDto) dtoConverter.convertToDto(transportOrder)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/scenes/{sceneId}/transport_orders/{transportOrderId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTransportOrder(@PathVariable("sceneId") Long sceneId,
                                         @PathVariable("transportOrderId") Long transportOrderId,
                                         TransportOrderDto transportOrderDto) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/scenes/{sceneId}/transport_orders/{transportOrderId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateTransportOrderPartial(@PathVariable("sceneId") Long sceneId,
                                                @PathVariable("transportOrderId") Long transportOrderId,
                                                EntityAuditDto entityAuditDto) {

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/scenes/{sceneId}/transport_orders/{transportOrderId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTransportOrder(@PathVariable("sceneId") Long sceneId,
                                         @PathVariable("transportOrderId") Long transportOrderId) {
        checkAccessViolation(sceneId, transportOrderId);

        TransportOrder transportOrder = Objects.requireNonNull(getOperatingService().getTransportOrder(transportOrderId),
                "transportOrder is null by transportOrderId: " + transportOrderId);

        getOperatingService().deleteTransportOrder(transportOrder);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkAccessViolation(Long sceneId, Long transportOrderId) {
        TransportOrder transportOrder = Objects.requireNonNull(getOperatingService().getTransportOrder(transportOrderId),
                "transportOrder is null by id: " + transportOrderId);

        if (transportOrder != null && transportOrder.getScene().getId() != sceneId) {
            throw new ObjectAccessViolationException(sceneId, transportOrderId);
        }
    }
}
