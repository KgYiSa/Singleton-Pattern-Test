package com.mj.tcs.api.v1.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang Zhen
 */
@RestController
@RequestMapping("/api/v1")
public class IndexController {

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/xml", "application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String index() {
        String welcomeMessage = "Welcome to MJ TCS Server!\n\n" +
                "------------------------\n" +
                "The API endpoint is as follows:\n" +
                "/:                                 welcome message & help\n" +
                "/getAllScenes:                           list all scene objects\n" +
                "/getAllScenes/count:                     count the number of all scene objects\n" +
                "/getAllScenes/names:                     list all names of the scene objects\n" +
                "/scene/{name}:                     get the scene object by name\n" +
                "/scene/{newname}/actions/creation: create a new scene with the new scene name\n" +
                "------------------------\n" +
                "\n";

        return welcomeMessage;
    }
}