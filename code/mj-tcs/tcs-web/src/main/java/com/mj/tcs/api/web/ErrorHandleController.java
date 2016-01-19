package com.mj.tcs.api.web;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hushilei on 2015/11/20.
 */
@Controller
public class ErrorHandleController implements ErrorController {

    @Override
    @RequestMapping("/error")
    public String getErrorPath() {
        return "../404";
    }
}