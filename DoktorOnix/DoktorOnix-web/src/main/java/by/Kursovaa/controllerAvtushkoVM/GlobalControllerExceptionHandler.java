/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
public class GlobalControllerExceptionHandler {

    @RequestMapping(value = "/404")
    public String handleConflict() {
        System.out.println(5);
        return "404";
    }
}
