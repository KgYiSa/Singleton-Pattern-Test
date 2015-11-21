package com.mj.tcs.api.v1.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * @author Wang Zhen
 */
//@RestController
@Controller
@RequestMapping({"/api/v1", ""})
public class IndexController {

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(HttpServletRequest req, Principal principal, Model model){

//    ModelAndView mv = new ModelAndView("name");
    return "login";

    }

    @RequestMapping(value = "/hello")
    public String hello(HttpServletRequest req, Principal principal, Model model) {
        System.out.println("come to hello  infrcccccc");
        System.out.println(req.getContextPath());

        return "index";
    }
}