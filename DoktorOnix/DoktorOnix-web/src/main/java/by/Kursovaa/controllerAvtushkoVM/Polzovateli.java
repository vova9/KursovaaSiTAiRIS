/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/polzovateli")
public class Polzovateli {

    @RequestMapping(value = "/index")
    public String homePolzovateli(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.dbAvtushkoVM.Polzovateli> polzovateli = Ejb.getInterface().lookupPolzovateliFacadeRemote().findAll();
        model.addAttribute("Polzovateli", polzovateli);
        model.addAttribute("User", obj);
        return "polzovateli/index";
    }

    @RequestMapping(value = "/edit/{id}")
    public String editPolzovateli(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        Integer id1 = Integer.parseInt(id);
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Polzovateli polzovateli = Ejb.getInterface().lookupPolzovateliFacadeRemote().find(id1);
        if (polzovateli.getIdEmailSluzebny() == null) {
            polzovateli.setIdEmailSluzebny(new by.kursovaa.dbAvtushkoVM.Email());
        }
        model.addAttribute("Polzovateli", polzovateli);
        model.addAttribute("User", obj);
        model.addAttribute("whot", "Изменить операцию");
        return "polzovateli/form";
    }

    @RequestMapping(value = "/add")
    public String addPolzovateli(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Polzovateli polzovateli = new by.kursovaa.dbAvtushkoVM.Polzovateli();
        polzovateli.setIdEmailSluzebny(new by.kursovaa.dbAvtushkoVM.Email());
        model.addAttribute("Polzovateli", polzovateli);
        model.addAttribute("User", obj);
        model.addAttribute("whot", "Добавить операцию");
        return "polzovateli/form";
    }

    @RequestMapping(value = "/save")
    public String savePolzovateli(@ModelAttribute("polzovateli") by.kursovaa.dbAvtushkoVM.Polzovateli polzovateli,
            HttpSession httpSession, Model model) {
        if (polzovateli.getId() == null) {
            if (polzovateli.getIdEmailSluzebny().getLogin() == null) {
                polzovateli.setIdEmailSluzebny(null);
                Ejb.getInterface().lookupPolzovateliFacadeRemote().create(polzovateli);
            } else {
                Ejb.getInterface().lookupPolzovateliFacadeRemote().edit(polzovateli);
            }
        } else if (polzovateli.getIdEmailSluzebny().getIdemail() == null) {
            if (polzovateli.getIdEmailSluzebny().getLogin() == null) {
                polzovateli.setIdEmailSluzebny(null);
                Ejb.getInterface().lookupPolzovateliFacadeRemote().edit(polzovateli);
            } else {
                Ejb.getInterface().lookupPolzovateliFacadeRemote().edit(polzovateli);
            }
        } else {
            Ejb.getInterface().lookupEmailFacadeRemote().edit(polzovateli.getIdEmailSluzebny());
            Ejb.getInterface().lookupPolzovateliFacadeRemote().edit(polzovateli);
        }
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        if (Objects.equals(obj.getId(), polzovateli.getId())) {
            httpSession.setAttribute("user", polzovateli);
            model.addAttribute("User", polzovateli);
        } else {
            model.addAttribute("User", obj);
        }
        List<by.kursovaa.dbAvtushkoVM.Polzovateli> polzovatel = Ejb.getInterface().lookupPolzovateliFacadeRemote().findAll();
        model.addAttribute("Polzovateli", polzovatel);
        model.addAttribute("success", "Пользователь был успешно сохранен!");
        return "polzovateli/index";
    }

    @RequestMapping(value = "/delete")
    public String deletePolzovateli(@ModelAttribute("Polzovateli") by.kursovaa.dbAvtushkoVM.Polzovateli polzovateli, HttpSession httpSession, Model model) {
        Ejb.getInterface().lookupPolzovateliFacadeRemote().remove(polzovateli);
        if (polzovateli.getIdEmailSluzebny() != null) {
            Ejb.getInterface().lookupEmailFacadeRemote().remove(polzovateli.getIdEmailSluzebny());
        }
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.dbAvtushkoVM.Polzovateli> polzovatel = Ejb.getInterface().lookupPolzovateliFacadeRemote().findAll();
        model.addAttribute("Polzovateli", polzovatel);
        model.addAttribute("User", obj);
        model.addAttribute("success", "Пользователь был успешно удален!");
        return "polzovateli/index";
    }

    @RequestMapping("/userpage")
    public String userPagePolzovateli(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        model.addAttribute("User", obj);
        return "polzovateli/userpage";
    }

    @RequestMapping("/userpage/save")
    public String userPageSavePolzovateli(@ModelAttribute("Polzovateli") by.kursovaa.dbAvtushkoVM.Polzovateli polzovateli,
            HttpSession httpSession, Model model) {
        Ejb.getInterface().lookupEmailFacadeRemote().edit(polzovateli.getIdEmailSluzebny());
        Ejb.getInterface().lookupPolzovateliFacadeRemote().edit(polzovateli);
        httpSession.setAttribute("user", polzovateli);
        model.addAttribute("User", polzovateli);
        model.addAttribute("success", "Профиль был успешно обновлен!");
        return "polzovateli/userpage";
    }
}
