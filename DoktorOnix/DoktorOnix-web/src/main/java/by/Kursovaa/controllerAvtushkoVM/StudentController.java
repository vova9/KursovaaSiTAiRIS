/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import by.Kursovaa.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/app")
public class StudentController {

    @Autowired
    private StudentService service;

    @RequestMapping(value = "/login")
    public String homePage() {
        return "Login";
    }

    @RequestMapping(value = "/secure/studentDetail")
    public String studentDetail(Model model) {
        model.addAttribute("students", service.getStudents());
        return "student";
    }
}
