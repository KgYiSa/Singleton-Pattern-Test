package com.mj.tcs.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web")
public class LoginController {

    /**
     * Open login page
     *
     * @return String
     */
    @RequestMapping(value= "/login",method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }
}
