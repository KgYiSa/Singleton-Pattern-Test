package com.mj.tcs.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

/**
 * @author Wang Zhen
 */
//@RestController
@Controller
@RequestMapping("/web")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletRequest req, Principal principal, Model model){
        model.addAttribute("date", new Date());
        return "login";
    }

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/resources/images/favicon.ico";
    }

    @RequestMapping(value = "/operating")
    public String operate(HttpServletRequest req, Principal principal, Model model) {
    	return "operating";
    }
    
    @RequestMapping(value = "/modelling")
    public String model(HttpServletRequest req, Principal principal, Model model) {
    	return "modelling";
    }
}