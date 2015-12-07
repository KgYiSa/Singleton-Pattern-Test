package com.mj.tcs.api.v1.web;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Controller
//@RequestMapping({"/api/v1", ""})
public class OperatingMessageGeneratorController extends ServiceController {

//    @RequestMapping("/hello")
//    @MessageMapping("/hello")
//    public String test(Action message) throws Exception {
//        System.out.println(message);
//        return "index";
//    }


//    @RequestMapping("/model")
//    @MessageMapping("/topic/greetings")
//    @SendTo("/topic/upload_model_result")

//    @Scheduled(fixedRate = 1000)
    public void receiveModel(/*Action message*/) throws Exception {
//        System.out.println("xxx");
        getMessageSender().convertAndSend("/topic/greetings","xxx");
    }

    @Scheduled(fixedRate = 5000)
    public void pushMessage() throws Exception {
//        System.out.println("xxx");
        getMessageSender().convertAndSend("/topic/greetings","xxx");
    }

    @MessageMapping("/scene/{sceneId}/vehicles")
//    @SendTo("/topic/upload_model_result")
    public void test2(@DestinationVariable Long sceneId, Action message) throws Exception {
        System.out.println(sceneId);
        System.out.println(message);
    }

    public static class Action {
        private Map<String, String> properties = new LinkedHashMap<>();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
