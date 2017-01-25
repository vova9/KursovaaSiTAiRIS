/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
public class GlobalControllerExceptionHandler {

    @RequestMapping(value = "/404")
    public String Eror404() {
        return "404";
    }

    @RequestMapping(value = "/403")
    public String Eror403() {
        return "403";
    }

    @RequestMapping(value = "/500")
    public String Eror500() {
        return "500";
    }
}
