/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.controller;

import by.kursovaa.pojo.MessageInfo;
import by.kursovaa.service.Ejb;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import by.kursovaa.entity.Polzovateli;
import by.kursovaa.entity.Zakazy;

/**
 *
 * @author Vladimir
 */
@Controller
public class Index {

    @RequestMapping("/")
    public String homePage(Model model, HttpSession httpSession) {
        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        if (user != null) {
            return "redirect:/index";
        }
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String loginPage(Model model, HttpSession httpSession) {
        return "login";
    }

    @RequestMapping("/index")
    public String indexPage(Model model, HttpSession httpSession) {
        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        model.addAttribute("User", user);

        List<Zakazy> zakaz = Ejb.getInterface().lookupZakazyFacadeRemote().findRange(new int[]{0, 10});
        model.addAttribute("Zakazy", zakaz);

        MessageInfo message = new MessageInfo();
        int countZakaz = Ejb.getInterface().lookupZakazyFacadeRemote().count();
        int countClients = Ejb.getInterface().lookupKlientyFacadeRemote().count();
        Double summaAll = Ejb.getInterface().lookupKassaFacadeRemote().summaAll();
        Double profitAll = Ejb.getInterface().lookupKassaFacadeRemote().profitAll();

        model.addAttribute("Email", message);
        model.addAttribute("coutZakaz", countZakaz);
        model.addAttribute("coutKlientov", countClients);
        model.addAttribute("coutSumma", summaAll);
        model.addAttribute("coutPribyl", profitAll);

        return "index";
    }

    @RequestMapping("/logoutSuccess")
    public String logoutSuccess(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Index::logoutPage STOK");

        Ejb.getInterface().lookupEmailServiceRemote().stopSynchronization();
        Ejb.getInterface().lookupEmailServiceRemote().remove();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
