package com.mj.tcs.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    /**
     * Open login page
     *
     * @return String
     */
    @RequestMapping(value= {"", "/web/login"},method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }
}
