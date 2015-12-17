package com.mj.tcs.api.v1.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

/**
 * @author Wang Zhen
 */
//@RestController
@Controller
@RequestMapping(value = "/")
public class IndexController {

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(HttpServletRequest req, Principal principal, Model model){
        System.out.println(req.getRequestURI());
        model.addAttribute("date", new Date());
    return "login";

    }

    @RequestMapping(value = "/hello")
    public String hello(HttpServletRequest req, Principal principal, Model model) {
        System.out.println("come to hello ");
        System.out.println(req.getContextPath());

        return "index";
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