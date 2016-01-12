package com.mj.tcs.service;

import com.google.common.eventbus.EventBus;
import com.mj.tcs.access.status.OrderStatusMessage;
import com.mj.tcs.access.status.StatusMessage;
import com.mj.tcs.access.status.VehicleStatusMessage;
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
import java.util.Objects;

/**
 * @author Wang Zhen
 */
public class KernelEventHandler implements EventListener<TCSEvent>, EventFilter<TCSEvent>, Serializable {
    /**
     /**
     * This class's Logger.
     */
    private static final Logger log = Logger.getLogger(KernelEventHandler.class);

    private static final EventBus eventBus = new EventBus(KernelEventHandler.class.getName());

    private final long sceneId;

    public KernelEventHandler(long sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public void processEvent(TCSEvent event) {
        if (!(event instanceof TCSObjectEvent)) {
            return;
        }
        TCSObjectEvent objectEvent = (TCSObjectEvent) event;
        StatusMessage message = null;

        Class<?> eventObjectClass = objectEvent.getCurrentOrPreviousObjectState().getClass();
        if (eventObjectClass.equals(TransportOrder.class)) {
            TransportOrder order = (TransportOrder) objectEvent.getCurrentOrPreviousObjectState();
            OrderStatusMessage orderMessage = new OrderStatusMessage(this.sceneId);
            // TODO: Replace with DTO by GEDA
            orderMessage.setOrderUUID(order.getUUID());
            orderMessage.setOrderState(order.getState());
            orderMessage.setExecutingVehicle(order.getProcessingVehicle());
            System.out.println("    " + order.getName() +" - " + order.getState() + " " +
                    (order.getProcessingVehicle() == null ? "null" :order.getProcessingVehicle().getName()));
            for (DriveOrder curDriveOrder : order.getAllDriveOrders()) {
                OrderStatusMessage.Destination dest
                        = new OrderStatusMessage.Destination();
                dest.setLocationUUID(
                        curDriveOrder.getDestination().getLocation().getUUID());
                dest.setOperation(curDriveOrder.getDestination().getOperation());
                dest.setState(curDriveOrder.getState());
                orderMessage.getDestinations().add(dest);
                message = orderMessage;
//                System.out.println(orderMessage);
                System.out.println(curDriveOrder.getDestination().getLocation().getName() + " " +
                    curDriveOrder.getDestination().getOperation() + " " + curDriveOrder.getState().name());
            }
        } else if (eventObjectClass.equals(Vehicle.class)) {
            Vehicle vehicle = (Vehicle) objectEvent.getCurrentOrPreviousObjectState();
            VehicleStatusMessage vehicleMessage = new VehicleStatusMessage(this.sceneId);
            // Set vehicle UUID
            vehicleMessage.setVehicleUUID(vehicle.getUUID());
            vehicleMessage.setEnergyLevel(vehicle.getEnergyLevel());
            // Set position
            TCSObjectReference<Point> posRef = vehicle.getCurrentPosition();
            if (posRef != null) {
                vehicleMessage.setPositionUUID(posRef.getUUID());
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
            message = vehicleMessage;
            System.out.println("Vehicle: " + vehicleMessage.getVehicleUUID() + " "
                    + vehicleMessage.getPositionUUID() + " "
                    + vehicleMessage.getState().name() + " "
                    + vehicleMessage.getProcState() + " "
                    + vehicleMessage.getEngergyLevel());
        }

        if (message != null) {
            eventBus.post(message);
        }
    }

    @Override
    public boolean accept(TCSEvent event) {
        return (event != null) && (event instanceof TCSObjectEvent);
    }

    public void register(Object listener) {
        Objects.requireNonNull(listener);

        eventBus.register(listener);
    }

    public void unregister(Object listener) {
        Objects.requireNonNull(listener);

        eventBus.unregister(listener);
    }
}
