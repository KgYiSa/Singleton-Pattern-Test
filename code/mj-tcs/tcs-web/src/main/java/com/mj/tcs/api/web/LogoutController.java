package com.mj.tcs.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web")
public class LogoutController {
    /**
     * Open login page
     *
     * @return String
     */
    @RequestMapping(value= "/logout")
    public String getLogoutPage() {
        return "redirect:/login";
    }
}
