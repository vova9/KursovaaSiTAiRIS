/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Polzovateli;
import by.kursovaa.logicAvtushkoVM.MessageBean;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/mailbox")
public class Email {

    @RequestMapping(value = "/index")
    public String homeMail(HttpSession httpSession, Model model) {
        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
        LinkedList<MessageBean> mail
                = Ejb.getInterface().lookupEMailRemote().receive("INBOX", obj.getIdEmailSluzebny());
        model.addAttribute("Email", mail);
        model.addAttribute("newmail", "1");
        model.addAttribute("dmail", "2");
        model.addAttribute("bmail", "3");
        model.addAttribute("spammail", "4");
        model.addAttribute("newmailhead", "13 новых сообщений");
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("coutmail", "5");
        return "mailbox/mailbox";
    }

    @RequestMapping(value = "/new")
    public String newMail() {
        return "mailbox/compose";
    }

    @RequestMapping(value = "/index/{inbox}")
    public String readSend(@PathVariable("inbox") String inbox, HttpSession httpSession, Model model) {
        boolean flag = false;
        String temp="INBOX."+inbox;
        if ("INBOX.Drafts".equals(temp)) {
            model.addAttribute("zagolovok", "Черновики");
            flag = true;
        }
        if ("INBOX.Trash".equals(temp)) {
            model.addAttribute("zagolovok", "Коризина");
            flag = true;
        }
        if ("INBOX.Sent".equals(temp)) {
            model.addAttribute("zagolovok", "Отправленные");
            flag = true;
        }
        if ("INBOX.Junk".equals(temp)) {
            model.addAttribute("zagolovok", "Спам");
            flag = true;
        }
        if (flag) {
            Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
            LinkedList<MessageBean> mail
                    = Ejb.getInterface().lookupEMailRemote().receive(temp, obj.getIdEmailSluzebny());
            model.addAttribute("Email", mail);
            model.addAttribute("newmail", "1");
            model.addAttribute("dmail", "2");
            model.addAttribute("bmail", "3");
            model.addAttribute("spammail", "4");
            model.addAttribute("newmailhead", "13 новых сообщений");
            model.addAttribute("coutmail", "5");            
        }
        return "mailbox/mailbox";
    }

    //    @RequestMapping(value = "/read-mail")
//    public String readMail(HttpSession httpSession, Model model) {
//        LinkedList<MessageBean> mail = Ejb.getInterface().lookupEMailRemote().receive("ras@doktoronix.net.ua",
//                "1629807V", "mail.ukraine.com.ua");
//        model.addAttribute("Email", mail);
//        return "mailbox/read-mail";
//    }
//    @RequestMapping(value = "/nexmail")
//    public String readNexMail(HttpSession httpSession, Model model) {
//        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
//        LinkedList<MessageBean> mail = 
//                Ejb.getInterface().lookupEMailRemote().receive("INBOX", obj.getIdEmailSluzebny());
//        model.addAttribute("Email", mail);
//        model.addAttribute("newmail", "1");
//        model.addAttribute("dmail", "2");
//        model.addAttribute("bmail", "3");
//        model.addAttribute("spammail", "4");
//        model.addAttribute("newmailhead", "13 новых сообщений");
//        model.addAttribute("zagolovok", "Входящие");
//        model.addAttribute("coutmail", "5");
//        return "mailbox/mailbox";
//    }
//    @RequestMapping(value = "/prevmail")
//    public String readPrevMail(HttpSession httpSession, Model model) {
//        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
//        LinkedList<MessageBean> mail = 
//                Ejb.getInterface().lookupEMailRemote().receive("INBOX", obj.getIdEmailSluzebny());
//        model.addAttribute("Email", mail);
//        model.addAttribute("newmail", "1");
//        model.addAttribute("dmail", "2");
//        model.addAttribute("bmail", "3");
//        model.addAttribute("spammail", "4");
//        model.addAttribute("newmailhead", "13 новых сообщений");
//        model.addAttribute("zagolovok", "Входящие");
//        model.addAttribute("coutmail", "5");
//        return "mailbox/mailbox";
//    }
//    @RequestMapping(value = "/draftsmail")
//    public String readDraftsMail(Model model) {
//        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
//        LinkedList<MessageBean> mail = 
//                Ejb.getInterface().lookupEMailRemote().receive("INBOX", obj.getIdEmailSluzebny());
//        model.addAttribute("Email", mail);
//        model.addAttribute("newmail", "1");
//        model.addAttribute("dmail", "2");
//        model.addAttribute("bmail", "3");
//        model.addAttribute("spammail", "4");
//        model.addAttribute("newmailhead", "13 новых сообщений");
//        model.addAttribute("zagolovok", "Входящие");
//        model.addAttribute("coutmail", "5");
//        return "mailbox/mailbox";
//    }
//    @RequestMapping(value = "/sendmail")
//    public String readSendMail(HttpSession httpSession, Model model) {
//        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
//        LinkedList<MessageBean> mail = 
//                Ejb.getInterface().lookupEMailRemote().receive("INBOX", obj.getIdEmailSluzebny());
//        model.addAttribute("Email", mail);
//        model.addAttribute("newmail", "1");
//        model.addAttribute("dmail", "2");
//        model.addAttribute("bmail", "3");
//        model.addAttribute("spammail", "4");
//        model.addAttribute("newmailhead", "13 новых сообщений");
//        model.addAttribute("zagolovok", "Входящие");
//        model.addAttribute("coutmail", "5");
//        return "mailbox/mailbox";
//    }                                             
}
