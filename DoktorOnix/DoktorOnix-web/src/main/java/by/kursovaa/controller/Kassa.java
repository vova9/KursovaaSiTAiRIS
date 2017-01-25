/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.controller;

import by.kursovaa.entity.Polzovateli;
import by.kursovaa.service.Ejb;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/kassa")
public class Kassa {

    @RequestMapping(value = "/index")
    public String homeKassa(HttpSession httpSession, Model model) {
        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kassa> kassa = Ejb.getInterface().lookupKassaFacadeRemote().findAll();
       
        model.addAttribute("Kassa", kassa);
        model.addAttribute("User", user);
        return "kassa/index";
    }

    @RequestMapping(value = "/edit/{id}")
    public String editKassa(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        Integer idOperations = Integer.parseInt(id);
        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Kassa kassa = Ejb.getInterface().lookupKassaFacadeRemote().find(idOperations);
       
        model.addAttribute("kasa", kassa);
        model.addAttribute("User", user);
        model.addAttribute("whot", "Изменить операцию");
        return "kassa/form";
    }

    @RequestMapping(value = "/add")
    public String addKassa(HttpSession httpSession, Model model) {
        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Kassa kassa = new by.kursovaa.entity.Kassa();
       
        model.addAttribute("kasa", kassa);
        model.addAttribute("User", user);
        model.addAttribute("whot", "Добавить операцию");
        return "kassa/form";
    }

    @RequestMapping(value = "/save")
    public String saveKassa(@ModelAttribute("kasa") by.kursovaa.entity.Kassa kasa, HttpSession httpSession,
            Model model) {

        if (kasa.getId() == null) {
            Ejb.getInterface().lookupKassaFacadeRemote().create(kasa);
        } else {
            Ejb.getInterface().lookupKassaFacadeRemote().edit(kasa);
        }

        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kassa> kassa = Ejb.getInterface().lookupKassaFacadeRemote().findAll();
       
        model.addAttribute("Kassa", kassa);
        model.addAttribute("User", user);
        model.addAttribute("success", "Операция была успешно сохранена!");
        return "kassa/index";
    }

    @RequestMapping(value = "/delete")
    public String deleteKassa(@ModelAttribute("kasa") by.kursovaa.entity.Kassa kasa, HttpSession httpSession,
            Model model) {

        Ejb.getInterface().lookupKassaFacadeRemote().remove(kasa);
        Polzovateli user = (Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.entity.Kassa> kassa = Ejb.getInterface().lookupKassaFacadeRemote().findAll();
       
        model.addAttribute("Kassa", kassa);
        model.addAttribute("User", user);
        model.addAttribute("success", "Операция была успешно удалена!");
        return "kassa/index";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/printchek")
    public String printСhekKassa(@ModelAttribute("kasa") by.kursovaa.entity.Kassa kasa, Model model,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            Jasper jasper = new Jasper();
            ArrayList<by.kursovaa.entity.Kassa> kassa = new ArrayList();
            kassa.add(kasa);
            HashMap<String, Object> hmParams = new HashMap<String, Object>();
            JasperReport jasperReport = jasper.getCompiledFile("chek");
            jasper.generateReportPDF(response, hmParams, jasperReport, kassa);
        } catch (JRException | NamingException | IOException ex) {
            Logger.getLogger(Kassa.class.getName()).log(Level.SEVERE, null, ex);
            return "500";
        }

        return null;
    }

    @RequestMapping(value = "/report")
    public String reportKassa(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Jasper jasper = new Jasper();
            List<by.kursovaa.entity.Kassa> kassa = Ejb.getInterface().lookupKassaFacadeRemote().findAll();
            HashMap<String, Object> hmParams = new HashMap<String, Object>();
            JasperReport jasperReport = jasper.getCompiledFile("kassaOtchet");
            jasper.generateReportPDF(response, hmParams, jasperReport, kassa);
        } catch (JRException | IOException | NamingException ex) {
            Logger.getLogger(Kassa.class.getName()).log(Level.SEVERE, null, ex);
            return "500";
        }

        return null;
    }
}
