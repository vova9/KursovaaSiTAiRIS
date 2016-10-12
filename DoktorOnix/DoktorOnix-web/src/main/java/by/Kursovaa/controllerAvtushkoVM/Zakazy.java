/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
@RequestMapping("/zakazy")
public class Zakazy {

    @RequestMapping(value = "/index")
    public String homeZakazy(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        List<by.kursovaa.dbAvtushkoVM.Zakazy> zakazy = Ejb.getInterface().lookupZakazyFacadeRemote().findAll();
        model.addAttribute("Zakazy", zakazy);
        model.addAttribute("User", obj);
        return "zakazy/index";
    }

    @RequestMapping(value = "/add")
    public String addZakazy(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Zakazy zakaz = new by.kursovaa.dbAvtushkoVM.Zakazy();
        zakaz.setTelefon(new by.kursovaa.dbAvtushkoVM.Klienty());
        model.addAttribute("Klienty", Ejb.getInterface().lookupKlientyFacadeRemote().findAll());
        model.addAttribute("Zakazy", zakaz);
        model.addAttribute("User", obj);
        return "zakazy/info";
    }

    @RequestMapping(value = "/edit/{id}")
    public String editZakazy(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Zakazy zakazy = Ejb.getInterface().lookupZakazyFacadeRemote().find(Integer.parseInt(id));
        model.addAttribute("Zakazy", zakazy);
        model.addAttribute("ZakazInfo", zakazy.getZakazInfoList());
        model.addAttribute("ZakazHistori", zakazy.getZakazStatusList());
        model.addAttribute("User", obj);
        return "zakazy/form";
    }

    @RequestMapping(value = "/edit/info/{id}")
    public String editInfoZakazy(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Zakazy zakazy = Ejb.getInterface().lookupZakazyFacadeRemote().find(Integer.parseInt(id));
        model.addAttribute("Zakazy", zakazy);
        model.addAttribute("Klienty", Ejb.getInterface().lookupKlientyFacadeRemote().findAll());
        model.addAttribute("User", obj);
        return "zakazy/info";
    }

    @RequestMapping(value = "/info/save")
    public String infoSaveZakazy(@ModelAttribute("zakazy") by.kursovaa.dbAvtushkoVM.Zakazy zakazy, HttpSession httpSession,
            Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        if (zakazy.getId() != null) {
            Ejb.getInterface().lookupZakazyFacadeRemote().edit(zakazy);
            by.kursovaa.dbAvtushkoVM.Zakazy zakaz = Ejb.getInterface().lookupZakazyFacadeRemote().find(zakazy.getId());
            model.addAttribute("Zakazy", zakaz);
            model.addAttribute("ZakazInfo", zakaz.getZakazInfoList());
            model.addAttribute("ZakazHistori", zakaz.getZakazStatusList());
        } else {
            Calendar calendar = Calendar.getInstance();
            zakazy.setDataOtpravki(calendar.getTime());
            Ejb.getInterface().lookupZakazyFacadeRemote().create(zakazy);
            List<by.kursovaa.dbAvtushkoVM.Zakazy> zakaz = Ejb.getInterface().lookupZakazyFacadeRemote().findAll();
            model.addAttribute("Zakazy", zakaz);
            model.addAttribute("User", obj);
            return "zakazy/index";
        }
        model.addAttribute("User", obj);
        return "zakazy/form";
    }

    @RequestMapping(value = "/delete")
    public String deleteZakazy(@ModelAttribute("zakazy") by.kursovaa.dbAvtushkoVM.Zakazy zakazy, HttpSession httpSession,
            Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        Ejb.getInterface().lookupZakazyFacadeRemote().remove(zakazy);
        List<by.kursovaa.dbAvtushkoVM.Zakazy> zakaz = Ejb.getInterface().lookupZakazyFacadeRemote().findAll();
        model.addAttribute("Zakazy", zakaz);
        model.addAttribute("User", obj);
        return "zakazy/index";
    }

    @RequestMapping(value = "/tovary/add/{id}")
    public String addTovaryZakazy(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Zakazy zakazy = Ejb.getInterface().lookupZakazyFacadeRemote().find(Integer.parseInt(id));
        by.kursovaa.dbAvtushkoVM.ZakazInfo zakazInfo = new by.kursovaa.dbAvtushkoVM.ZakazInfo();
        zakazInfo.setIdZakat(zakazy);
        zakazInfo.setTovary(new by.kursovaa.dbAvtushkoVM.Tovary());
        model.addAttribute("ZakazInfo", zakazInfo);
        model.addAttribute("Tovary", Ejb.getInterface().lookupTovaryFacadeRemote().findAll());
        model.addAttribute("User", obj);
        return "zakazy/tovary";
    }

    @RequestMapping(value = "/tovary/edit/{id}")
    public String editTovaryZakazy(@PathVariable("id") String id, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.ZakazInfo zakazInfo = Ejb.getInterface().lookupZakazInfoFacadeRemote().find(Integer.parseInt(id));
        model.addAttribute("ZakazInfo", zakazInfo);
        model.addAttribute("Tovary", Ejb.getInterface().lookupTovaryFacadeRemote().findAll());
        model.addAttribute("User", obj);
        return "zakazy/tovary";
    }

    @RequestMapping(value = "/tovar/save")
    public String tovarSaveZakazy(@ModelAttribute("ZakazInfo") by.kursovaa.dbAvtushkoVM.ZakazInfo zakazInfo,
            HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Tovary tovary
                = Ejb.getInterface().lookupTovaryFacadeRemote().find(zakazInfo.getTovary().getArtikul());
        if (zakazInfo.getIdZakat().getPribyl() == null) {
            zakazInfo.getIdZakat().setPribyl(0.0);
        }
        if (zakazInfo.getIdZakat().getSkidka() == null) {
            zakazInfo.getIdZakat().setSkidka(0.0);
        }
        if (zakazInfo.getIdZakat().getSumma() == null) {
            zakazInfo.getIdZakat().setSumma(0.0);
        }
        if (zakazInfo.getSebestoimost() == null) {
            zakazInfo.setSebestoimost(0.0);
        }
        zakazInfo.setZena(tovary.getZena() - zakazInfo.getIdZakat().getSkidka());
        if (zakazInfo.getIdzakazInfo() == null) {
            zakazInfo.getIdZakat().setSumma(zakazInfo.getIdZakat().getSumma() + zakazInfo.getZena() * zakazInfo.getKolichestovo());
            zakazInfo.getIdZakat().setPribyl(zakazInfo.getIdZakat().getPribyl()
                    + ((tovary.getZena() * zakazInfo.getKolichestovo())
                    - (zakazInfo.getSebestoimost() * zakazInfo.getKolichestovo())));
        } else {
            by.kursovaa.dbAvtushkoVM.ZakazInfo zakazinfo
                    = Ejb.getInterface().lookupZakazInfoFacadeRemote().find(zakazInfo.getIdzakazInfo());
            zakazInfo.getIdZakat().setSumma(zakazInfo.getIdZakat().getSumma()
                    + ((zakazInfo.getZena() * zakazInfo.getKolichestovo()) - (zakazinfo.getZena() * zakazinfo.getKolichestovo())));
            zakazInfo.getIdZakat().setPribyl(zakazInfo.getIdZakat().getPribyl()
                    + (((tovary.getZena() * zakazInfo.getKolichestovo()) - (zakazInfo.getSebestoimost() * zakazInfo.getKolichestovo()))
                    - ((tovary.getZena() * zakazinfo.getKolichestovo()) - (zakazinfo.getSebestoimost() * zakazinfo.getKolichestovo())))
            );
        }
        Ejb.getInterface().lookupZakazInfoFacadeRemote().edit(zakazInfo);
        Ejb.getInterface().lookupZakazyFacadeRemote().edit(zakazInfo.getIdZakat());
        by.kursovaa.dbAvtushkoVM.Zakazy zakaz
                = Ejb.getInterface().lookupZakazyFacadeRemote().find(zakazInfo.getIdZakat().getId());
        model.addAttribute("Zakazy", zakaz);
        model.addAttribute("ZakazInfo", zakaz.getZakazInfoList());
        model.addAttribute("ZakazHistori", zakaz.getZakazStatusList());
        model.addAttribute("User", obj);
        return "zakazy/form";
    }

    @RequestMapping(value = "/tovar/delete")
    public String tovarDeleteZakazy(@ModelAttribute("ZakazInfo") by.kursovaa.dbAvtushkoVM.ZakazInfo zakazInfo,
            HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        zakazInfo.getIdZakat().setSumma(zakazInfo.getIdZakat().getSumma() - (zakazInfo.getZena() * zakazInfo.getKolichestovo()));
        zakazInfo.getIdZakat().setPribyl(zakazInfo.getIdZakat().getPribyl()
                - ((zakazInfo.getZena() * zakazInfo.getKolichestovo()) - (zakazInfo.getSebestoimost() * zakazInfo.getKolichestovo())));
        Ejb.getInterface().lookupZakazyFacadeRemote().edit(zakazInfo.getIdZakat());
        Ejb.getInterface().lookupZakazInfoFacadeRemote().remove(zakazInfo);
        by.kursovaa.dbAvtushkoVM.Zakazy zakaz
                = Ejb.getInterface().lookupZakazyFacadeRemote().find(zakazInfo.getIdZakat().getId());
        model.addAttribute("Zakazy", zakaz);
        model.addAttribute("ZakazInfo", zakaz.getZakazInfoList());
        model.addAttribute("ZakazHistori", zakaz.getZakazStatusList());
        model.addAttribute("User", obj);
        return "zakazy/form";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    //jasper
    @RequestMapping(value = "/nakladnia/{id}")
    public String nakladniaZakazy(@PathVariable("id") String id, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            by.kursovaa.dbAvtushkoVM.Zakazy zakazy = Ejb.getInterface().lookupZakazyFacadeRemote().find(Integer.parseInt(id));
            Jasper jasper = new Jasper();
            List<by.kursovaa.dbAvtushkoVM.ZakazInfo> Zakaz = zakazy.getZakazInfoList();
            HashMap<String, Object> hmParams = new HashMap<String, Object>();
            JasperReport jasperReport = jasper.getCompiledFile("nakladmaa");
            jasper.generateReportPDF(response, hmParams, jasperReport, zakazy.getZakazInfoList());
        } catch (JRException | NamingException | IOException ex) {
            Logger.getLogger(Zakazy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
