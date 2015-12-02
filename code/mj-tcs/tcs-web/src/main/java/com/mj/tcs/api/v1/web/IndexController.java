package com.mj.tcs.api.v1.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
        System.out.println(req.getRequestURI());
//        System.out.println(req.getContextPath());

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

    @RequestMapping(value = "/toUpload")
    public String toUpload(HttpServletRequest req, Principal principal, Model model) {
        System.out.print("toUpload");
        return "upload";
    }

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}