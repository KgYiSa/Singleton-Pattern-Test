package com.mj.tcs.api.v1.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author Wang Zhen
 */
@Controller
//@RequestMapping({"/api/v1", ""})
public class OperatingMessageGeneratorController extends ServiceController {

//    @RequestMapping("/hello")
    @MessageMapping("/hello")
    public String test(Map<String, String> message) throws Exception {
        System.out.println(message);
        return "index";
    }


//    @RequestMapping("/model")
    @MessageMapping("/tcs_model")
    @SendTo("/topic/upload_model_result")
    public void receiveModel(Map<String, String> message) throws Exception {
        System.out.println(message);
    }
}
