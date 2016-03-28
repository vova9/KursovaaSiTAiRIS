/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/mailbox")
public class Email {

    @RequestMapping(value = "/mailbox")
    public String homeMail(Model model) {
        model.addAttribute("Email", Ejb.getInterface().lookupEMailRemote().receive(null, null, null));
        return "mailbox/mailbox";
    }
}
