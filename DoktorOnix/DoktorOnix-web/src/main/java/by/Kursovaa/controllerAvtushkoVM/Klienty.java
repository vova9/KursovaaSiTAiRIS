/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import java.io.IOException;
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
@RequestMapping("/klienty")
public class Klienty {

    @RequestMapping(value = "/index")
    public String homeKlienty(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.dbAvtushkoVM.Klienty> klienty = Ejb.getInterface().lookupKlientyFacadeRemote().findAll();
        model.addAttribute("Klient", klienty);
        model.addAttribute("User", obj);             
        return "klienty/index";
    }

    @RequestMapping(value = "/edit/{id}")
    public String editKlienty(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Klienty klienty = Ejb.getInterface().lookupKlientyFacadeRemote().find(id);
        model.addAttribute("Klient", klienty);
        model.addAttribute("User", obj);
        model.addAttribute("whot", "Изменить данные о клиенте");
        return "klienty/form";
    }

    @RequestMapping(value = "/add")
    public String addKlienty(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Klienty klienty = new by.kursovaa.dbAvtushkoVM.Klienty();
        model.addAttribute("Klient", klienty);
        model.addAttribute("User", obj);
        model.addAttribute("whot", "Добавить нового клиента");
        return "klienty/form";
    }

    @RequestMapping(value = "/save")
    public String saveKlienty(@ModelAttribute("Klienty") by.kursovaa.dbAvtushkoVM.Klienty klienty,
            HttpSession httpSession, Model model) {
        if (klienty.getTelefon().isEmpty()) {
            Ejb.getInterface().lookupKlientyFacadeRemote().create(klienty);
        } else {
            Ejb.getInterface().lookupKlientyFacadeRemote().edit(klienty);
        }
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        model.addAttribute("User", obj);
        List<by.kursovaa.dbAvtushkoVM.Klienty> klient = Ejb.getInterface().lookupKlientyFacadeRemote().findAll();
        model.addAttribute("Klient", klient);
        model.addAttribute("success", "Данные о клиенте были успешно сохранены!");
        return "klienty/index";
    }

    @RequestMapping(value = "/delete")
    public String deleteKlienty(@ModelAttribute("Klient") by.kursovaa.dbAvtushkoVM.Klienty klienty, HttpSession httpSession, Model model) {
        Ejb.getInterface().lookupKlientyFacadeRemote().remove(klienty);
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.dbAvtushkoVM.Klienty> klient = Ejb.getInterface().lookupKlientyFacadeRemote().findAll();
        model.addAttribute("Klient", klient);
        model.addAttribute("User", obj);
        model.addAttribute("success", "Данные о клиенте были успешно удалены!");
        return "klienty/index";
    }

    @RequestMapping(value = "/report")
    public String reportKassa(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Jasper jasper = new Jasper();
            List<by.kursovaa.dbAvtushkoVM.Klienty> klienty = Ejb.getInterface().lookupKlientyFacadeRemote().findAll();
            HashMap<String, Object> hmParams = new HashMap<String, Object>();
            JasperReport jasperReport = jasper.getCompiledFile("klienty");
            jasper.generateReportPDF(response, hmParams, jasperReport, klienty);
        } catch (JRException | IOException | NamingException ex) {
            Logger.getLogger(Kassa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
