package com.mj.tcs.api.v1.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author Wang Zhen
 */
@Controller
@RequestMapping({"/api/v1", ""})
public class IndexController {

    @RequestMapping(value = {"/"})
    public ModelAndView index(HttpServletRequest req, Principal principal, Model model) {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/hello")
    public String hello(HttpServletRequest req, Principal principal, Model model) {
        System.out.println("come to hello P");
        return "login";
    }
}