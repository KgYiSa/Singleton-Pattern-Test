package com.mj.tcs.api.v1.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author Wang Zhen
 */
@RestController
@RequestMapping({"/api/v1", ""})
public class IndexController {

    @RequestMapping(value = {"", "/"})
    public String index(HttpServletRequest req, Principal principal, Model model) {
        return "login";
    }
}