/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.controller;

import by.kursovaa.service.Ejb;
import by.kursovaa.service.SortKategorii;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/tovary")
public class Tovary {

    private by.kursovaa.entity.Polzovateli test() {
        by.kursovaa.entity.Polzovateli obj = new by.kursovaa.entity.Polzovateli();
        obj.setAdmin(0);
        obj.setIma("dsfdkf");
        obj.setFamilia("kjdfdfd");
        return obj;
    }

    @RequestMapping(value = "/index")
    public String homeTovary(HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
        model.addAttribute("Tovary", Ejb.getInterface().lookupTovaryFacadeRemote().findAll());
        model.addAttribute("Kategorii", kategorii);
        model.addAttribute("User", obj);
        return "tovary/index";
    }

    @RequestMapping("/ajax")
    public @ResponseBody
    String ajaxTovary(HttpServletRequest request, HttpServletResponse response) {
        String remove = request.getParameter("remove");
        String add = request.getParameter("add");
        String text = request.getParameter("text");
        String edit = request.getParameter("edit");
        if (!add.isEmpty()) {
            by.kursovaa.entity.Kategorii kategoria = new by.kursovaa.entity.Kategorii();
            List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
            Collections.sort(kategorii, new SortKategorii());
            kategoria.setId(kategorii.get(kategorii.size() - 1).getId() + 1);
            kategoria.setName(text);
            kategoria.setRoditel(Integer.parseInt(add));
            Ejb.getInterface().lookupKategoriiFacadeRemote().create(kategoria);
            return kategoria.getId().toString();
        }
        if (!edit.isEmpty()) {
            by.kursovaa.entity.Kategorii kategorii
                    = Ejb.getInterface().lookupKategoriiFacadeRemote().find(Integer.parseInt(edit));
            kategorii.setName(text);
            Ejb.getInterface().lookupKategoriiFacadeRemote().edit(kategorii);
        }
        if (!remove.isEmpty()) {
            by.kursovaa.entity.Kategorii kategoria
                    = Ejb.getInterface().lookupKategoriiFacadeRemote().find(Integer.parseInt(remove));
            if (kategoria.getRoditel() == 0) {
                List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
                for (int i = 0; i < kategorii.size(); i++) {
                    if (kategorii.get(i).getRoditel() == kategoria.getId()) {
                        Ejb.getInterface().lookupKategoriiFacadeRemote().remove(kategorii.get(i));
                    }
                }
            }
            Ejb.getInterface().lookupKategoriiFacadeRemote().remove(kategoria);
        }
        return "";
    }

    @RequestMapping(value = "/view/{id}")
    public String veiwTovary(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        Integer id1 = Integer.parseInt(id);
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
        if (id1 == 0) {
            model.addAttribute("Tovary", Ejb.getInterface().lookupTovaryFacadeRemote().findAll());
        } else {
            for (int i = 0; i < kategorii.size(); i++) {
                if (Objects.equals(kategorii.get(i).getId(), id1)) {
                    model.addAttribute("Tovary", kategorii.get(i).getTovaryList());
                }
            }
        }
        model.addAttribute("id", id1);
        model.addAttribute("Kategorii", kategorii);
        model.addAttribute("User", obj);
        return "tovary/index";
    }

    @RequestMapping(value = "/edit/{id}")
    public String editTovary(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Tovary tovary = Ejb.getInterface().lookupTovaryFacadeRemote().find(Integer.parseInt(id));
        List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
        model.addAttribute("Kategorii", kategorii);
        model.addAttribute("Tovar", tovary);
        model.addAttribute("User", obj);
        model.addAttribute("whot", "Изменить данные о товаре");
        return "tovary/form";
    }

    @RequestMapping(value = "/add")
    public String addTovary(HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Tovary tovary = new by.kursovaa.entity.Tovary();
        List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
        tovary.setKategoria(new by.kursovaa.entity.Kategorii());
        model.addAttribute("Kategorii", kategorii);
        model.addAttribute("Tovar", tovary);
        model.addAttribute("User", obj);
        model.addAttribute("whot", "Добавить новый товар");
        return "tovary/form";
    }

    @RequestMapping(value = "/save")
    public String saveTovary(@ModelAttribute("Tovar") by.kursovaa.entity.Tovary tovar,
            HttpSession httpSession, Model model) {
        Ejb.getInterface().lookupTovaryFacadeRemote().edit(tovar);
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
        model.addAttribute("Tovary", Ejb.getInterface().lookupTovaryFacadeRemote().findAll());
        model.addAttribute("Kategorii", kategorii);
        model.addAttribute("User", obj);
        model.addAttribute("success", "Товар был успешно сохранен!");
        return "tovary/index";
    }

    @RequestMapping(value = "/delete")
    public String deleteTovary(@ModelAttribute("Tovar") by.kursovaa.entity.Tovary tovar, HttpSession httpSession, Model model) {
        Ejb.getInterface().lookupTovaryFacadeRemote().remove(tovar);
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kategorii> kategorii = Ejb.getInterface().lookupKategoriiFacadeRemote().findAll();
        model.addAttribute("Tovary", Ejb.getInterface().lookupTovaryFacadeRemote().findAll());
        model.addAttribute("Kategorii", kategorii);
        model.addAttribute("User", obj);
        model.addAttribute("success", "Товар был успешно удален!");
        return "tovary/index";
    }

    @RequestMapping(value = "/import")
    public String importTovary(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
            Model model) {
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        model.addAttribute("User", obj);
        String url = request.getParameter("url");
        if (!url.isEmpty()) {
            Ejb.getInterface().lookupSeviceRemote().importTovary(url);
            Ejb.getInterface().lookupSeviceRemote().importTovary();
            model.addAttribute("success", 1);
        }
        return "tovary/import";
    }

    @RequestMapping(value = "/import/index")
    public String importIndexTovary(HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli obj = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        model.addAttribute("User", obj);
        return "tovary/import";
    }

    @RequestMapping(value = "/report")
    public String reportTovary(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Jasper jasper = new Jasper();
            List<by.kursovaa.entity.Tovary> tovary = Ejb.getInterface().lookupTovaryFacadeRemote().findAll();
            HashMap<String, Object> hmParams = new HashMap<String, Object>();
            JasperReport jasperReport = jasper.getCompiledFile("prays");
            jasper.generateReportPDF(response, hmParams, jasperReport, tovary);
        } catch (JRException | IOException | NamingException ex) {
            Logger.getLogger(Kassa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
