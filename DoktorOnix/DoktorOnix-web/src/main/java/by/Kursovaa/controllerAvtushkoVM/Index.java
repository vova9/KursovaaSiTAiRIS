/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import by.Kursovaa.service.Avtorizazia;
import by.kursovaa.logicAvtushkoVM.MessageBean;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
public class Index {

    @RequestMapping("/")
    public String indexPage(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            Avtorizazia avtorizazia = new Avtorizazia();
            model.addAttribute("Avtozizazia", avtorizazia);
            return "login";
        } else {
            by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
            model.addAttribute("User", obj);
            int a[] = new int[2];
            a[0] = 0;
            a[1] = 10;
            List<by.kursovaa.dbAvtushkoVM.Zakazy> zakaz = Ejb.getInterface().lookupZakazyFacadeRemote().findRange(a);
            model.addAttribute("Zakazy", zakaz);
            MessageBean messge = new MessageBean();
            model.addAttribute("Email", messge);
            model.addAttribute("coutZakaz", "150");
            model.addAttribute("coutKlientov", "44");
            model.addAttribute("coutSumma", "133");
            model.addAttribute("coutPribyl", "133");
        }
        return "index";
    }

    @RequestMapping("/avtozizazia")
    public String avtozizaziaPage(@ModelAttribute("Avtozizazia") Avtorizazia avtorizazia, HttpSession httpSession,
            Model model) {
        List<by.kursovaa.dbAvtushkoVM.Polzovateli> obj = Ejb.getInterface().lookupPolzovateliFacadeRemote().findAll();
       System.out.println(avtorizazia.getLogin()+" "+avtorizazia.getPassword());
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i).getLogin().equals(avtorizazia.getLogin())) {
                if (obj.get(i).getParol().equals(avtorizazia.getPassword())) {
                    httpSession.setAttribute("user", obj.get(i));
                    model.addAttribute("User", obj.get(i));
                    int a[] = new int[2];
                    a[0] = 0;
                    a[1] = 10;
                    List<by.kursovaa.dbAvtushkoVM.Zakazy> zakaz = Ejb.getInterface().lookupZakazyFacadeRemote().findRange(a);
                    model.addAttribute("Zakazy", zakaz);
                    MessageBean messge = new MessageBean();
                    model.addAttribute("Email", messge);
                    model.addAttribute("coutZakaz", "150");
                    model.addAttribute("coutKlientov", "44");
                    model.addAttribute("coutSumma", "133");
                    model.addAttribute("coutPribyl", "133");
                    return "index";
                }
            }
        }
        Avtorizazia avtorizazia1 = new Avtorizazia();
        model.addAttribute("Avtozizazia", avtorizazia1);
        model.addAttribute("danger", "1");
        return "login";
    }

    @RequestMapping("/logout")
    public String logoutPage(Model model, HttpSession httpSession) {
        httpSession.removeAttribute("user");
        Avtorizazia avtorizazia = new Avtorizazia();
        model.addAttribute("login", avtorizazia);
        return "login";
    }
}
