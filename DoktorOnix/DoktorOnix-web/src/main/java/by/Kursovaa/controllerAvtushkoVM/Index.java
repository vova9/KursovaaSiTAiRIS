/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
public class Index {

    @RequestMapping("/")
    public String indexPage(Model model, HttpSession httpSession) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = Ejb.getInterface().lookupPolzovateliFacadeRemote().find(14);
        httpSession.setAttribute("user", obj);
        model.addAttribute("User", obj);
        model.addAttribute("coutZakaz", "150");
        model.addAttribute("coutKlientov", "44");
        model.addAttribute("coutSumma", "133");
        model.addAttribute("coutPribyl", "133");
        return "index";
    }
}
