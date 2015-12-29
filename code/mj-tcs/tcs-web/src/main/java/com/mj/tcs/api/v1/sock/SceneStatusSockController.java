package com.mj.tcs.api.v1.sock;

import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.VehicleDto;
import com.mj.tcs.api.v1.dto.VehicleStatusDto;
import com.mj.tcs.api.v1.web.ServiceController;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author Wang Zhen
 */
@EnableScheduling
@Controller
public class SceneStatusSockController extends ServiceController {

    @Scheduled(fixedRate = 1000)
    public void pushMessage() throws Exception {
//        System.out.println("xxx");
        for (final SceneDto sceneDto : getService().getRunningSceneDtos()) {
            final long sceneId = sceneDto.getId();
            if (sceneDto.getVehicleDtos() != null) {
                VehicleDto vehicleDto = getRandomMember(sceneDto.getVehicleDtos());

                VehicleStatusDto statusDto = new VehicleStatusDto();
                statusDto.setUUID(vehicleDto.getUUID());
                statusDto.setPosition(((PointDto) getRandomMember((Collection) sceneDto.getPointDtos())).getUUID());

                Set<String> processingStates = new HashSet<>();
                processingStates.add("UNAVAILABLE");
                processingStates.add("IDLE");
                processingStates.add("AWAITING_ORDER");
                processingStates.add("PROCESSING_ORDER");
                statusDto.setProcessingState((String) getRandomMember((Collection) processingStates));

                Set<String> states = new HashSet<>();
                states.add("UNKNOWN");
                states.add("UNAVAILABLE");
                states.add("ERROR");
                states.add("IDLE");
                states.add("EXECUTING");
                states.add("CHARGING");
                statusDto.setState((String) getRandomMember((Collection) states));

                getMessageSender().convertAndSend("/topic/scene/" + sceneId, statusDto);
            }
        }
    }

    private <T> T getRandomMember(Collection<T> members) {
        if (members == null || members.isEmpty()) {
            return null;
        }

        final int count = members.size();
        final int val = new Random().nextInt(count);

        int i = 0;
        Iterator<T> iterator = members.iterator();
        while (iterator.hasNext()) {
            T n = iterator.next();
            if (val == i) {
                return n;
            }
            i++;
        }

        return null;
    }
}
