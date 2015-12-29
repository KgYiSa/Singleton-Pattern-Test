package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.communication.OrderStatusMessage;
import com.mj.tcs.api.v1.dto.communication.VehicleStatusMessage;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.base.Triple;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.eventsystem.TCSObjectEvent;
import com.mj.tcs.util.eventsystem.EventFilter;
import com.mj.tcs.util.eventsystem.EventListener;
import com.mj.tcs.util.eventsystem.TCSEvent;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * @author Wang Zhen
 */
public class KernelEventHandler implements EventListener<TCSEvent>, EventFilter<TCSEvent>, Serializable {
    /**
     /**
     * This class's Logger.
     */
    private static final Logger log = Logger.getLogger(KernelEventHandler.class);

    @Override
    public void processEvent(TCSEvent event) {
        if (!(event instanceof TCSObjectEvent)) {
            return;
        }
        TCSObjectEvent objectEvent = (TCSObjectEvent) event;

        Class<?> eventObjectClass = objectEvent.getCurrentOrPreviousObjectState().getClass();
        if (eventObjectClass.equals(TransportOrder.class)) {
            TransportOrder order = (TransportOrder) objectEvent.getCurrentOrPreviousObjectState();
            OrderStatusMessage orderMessage = new OrderStatusMessage();
            orderMessage.setOrderName(order.getName());
            orderMessage.setOrderState(order.getState());
            orderMessage.setExecutingVehicleName(order.getProcessingVehicle());
            for (DriveOrder curDriveOrder : order.getAllDriveOrders()) {
                OrderStatusMessage.Destination dest
                        = new OrderStatusMessage.Destination();
                dest.setLocationName(
                        curDriveOrder.getDestination().getLocation().getName());
                dest.setOperation(curDriveOrder.getDestination().getOperation());
                dest.setState(curDriveOrder.getState());
                orderMessage.getDestinations().add(dest);
//                message = orderMessage;
                System.out.println(orderMessage);
            }
        } else if (eventObjectClass.equals(Vehicle.class)) {
            Vehicle vehicle = (Vehicle) objectEvent.getCurrentOrPreviousObjectState();
            VehicleStatusMessage vehicleMessage = new VehicleStatusMessage();
            // Set vehicle name
            vehicleMessage.setVehicleName(vehicle.getName());
            vehicleMessage.setEnergyLevel(vehicle.getEnergyLevel());
            // Set position
            TCSObjectReference<Point> posRef = vehicle.getCurrentPosition();
            if (posRef != null) {
                vehicleMessage.setPosition(posRef.getName());
            }
            // Set vehicle state
            Vehicle.State state = vehicle.getState();
            vehicleMessage.setState(state);
            // Set vehciel processing state
            Vehicle.ProcState procState = vehicle.getProcState();
            vehicleMessage.setProcState(procState);
            // Set presice position
            Triple precisePos = vehicle.getPrecisePosition();
            if (precisePos != null) {
                VehicleStatusMessage.PrecisePosition precisePosElement;
                precisePosElement = new VehicleStatusMessage.PrecisePosition(
                        precisePos.getX(), precisePos.getY(), precisePos.getZ());
                vehicleMessage.setPrecisePosition(precisePosElement);
            }
//            message = vehicleMessage;
            System.out.println(vehicleMessage);
        }
    }

    @Override
    public boolean accept(TCSEvent event) {
        return (event != null) && (event instanceof TCSObjectEvent);
    }
}
