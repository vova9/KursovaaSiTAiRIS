/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Polzovateli;
import by.kursovaa.logicAvtushkoVM.CoutMessage;
import by.kursovaa.logicAvtushkoVM.MessageBean;
import java.util.ArrayList;
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
@RequestMapping("/mailbox")
public class Email {

    @RequestMapping(value = "/index")
    public String homeMail(HttpSession httpSession, Model model) {
        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
        CoutMessage cout= Ejb.getInterface().lookupEMailRemote().chek(obj.getIdEmailSluzebny());
        ArrayList<MessageBean> mail
                = Ejb.getInterface().lookupEMailRemote().fetch(obj.getIdEmailSluzebny(), "INBOX");
        httpSession.setAttribute("Email", mail);
        httpSession.setAttribute("mailbox", "Inbox");
        model.addAttribute("mailbox", "Inbox");
        model.addAttribute("Email", mail);
        if (cout.getNewMess()>0){model.addAttribute("newmail", cout.getNewMess());}
        if (cout.getDmail()>0){model.addAttribute("dmail", cout.getDmail());}
        if (cout.getBmail()>0){model.addAttribute("bmail", cout.getBmail());}
        if (cout.getSpammail()>0){model.addAttribute("spammail", cout.getSpammail());}
        if (cout.getNewMess()>0){model.addAttribute("newmailhead", cout.getNewMess()+" новых сообщений");}
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("coutmail", "Показано "+cout.getCout()+ " из " + cout.getCout());
        model.addAttribute("success");
        return "mailbox/mailbox";
    }

    @RequestMapping(value = "/new")
    public String newMail(Model model) {
        MessageBean messge = new MessageBean();
        model.addAttribute("Email", messge);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/index/{inbox}")
    public String readSend(@PathVariable("inbox") String inbox, HttpSession httpSession, Model model) {
        boolean flag = false;
        String temp = "INBOX." + inbox;
        if ("INBOX.Drafts".equals(temp)) {
            model.addAttribute("zagolovok", "Черновики");
            model.addAttribute("mailbox", "Drafts");
            flag = true;
        }
        if ("INBOX.Trash".equals(temp)) {
            model.addAttribute("zagolovok", "Коризина");
            model.addAttribute("mailbox", "Trash");
            flag = true;
        }
        if ("INBOX.Sent".equals(temp)) {
            model.addAttribute("zagolovok", "Отправленные");
            model.addAttribute("mailbox", "Sent");
            flag = true;
        }
        if ("INBOX.Junk".equals(temp)) {
            model.addAttribute("zagolovok", "Спам");
            model.addAttribute("mailbox", "Junk");
            flag = true;
        }
        if (flag) {
            Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
            CoutMessage cout= Ejb.getInterface().lookupEMailRemote().chek(obj.getIdEmailSluzebny());
            ArrayList<MessageBean> mail
                    = Ejb.getInterface().lookupEMailRemote().fetch(obj.getIdEmailSluzebny(), temp);
            CoutMessage a= Ejb.getInterface().lookupEMailRemote().chek(obj.getIdEmailSluzebny());
            httpSession.setAttribute("Email", mail);
            model.addAttribute("Email", mail);
           if (cout.getNewMess()>0){model.addAttribute("newmail", cout.getNewMess());}
        if (cout.getDmail()>0){model.addAttribute("dmail", cout.getDmail());}
        if (cout.getBmail()>0){model.addAttribute("bmail", cout.getBmail());}
        if (cout.getSpammail()>0){model.addAttribute("spammail", cout.getSpammail());}
        if (cout.getNewMess()>0){model.addAttribute("newmailhead", cout.getNewMess()+" новых сообщений");}
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("coutmail", "Показано "+cout.getCout()+ " из " + cout.getCout());
        }
        return "/mailbox/mailbox";
    }

    @RequestMapping(value = "/read/{mes}")
    public String readMail(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
            CoutMessage cout= Ejb.getInterface().lookupEMailRemote().chek(obj.getIdEmailSluzebny());
        MessageBean messge = null;
        Long id = Long.parseLong(mes);
        if (id < mail.size() || id > 0) {
            for (int i = 0; i < mail.size(); i++) {
                if (mail.get(i).getMsgId() == id) {
                    messge = mail.get(i);
                    break;
                }
            }
        } else {
            model.addAttribute("error", "Нет сообшений!");
        }
        if(messge.getIsNew()){  Ejb.getInterface().lookupEMailRemote().messegeRead(obj.getIdEmailSluzebny(), 
                messge, "INBOX");}
        model.addAttribute("mail", messge);
        if (cout.getNewMess()>0){model.addAttribute("newmail", cout.getNewMess());}
        if (cout.getDmail()>0){model.addAttribute("dmail", cout.getDmail());}
        if (cout.getBmail()>0){model.addAttribute("bmail", cout.getBmail());}
        if (cout.getSpammail()>0){model.addAttribute("spammail", cout.getSpammail());}
        if (cout.getNewMess()>0){model.addAttribute("newmailhead", cout.getNewMess()+" новых сообщений");}
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("coutmail", "Показано "+cout.getCout()+ " из " + cout.getCout());
        return "/mailbox/read";
    }

    @RequestMapping(value = "/nextmail/{mes}")
    public String readNexMail(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        Long id = Long.parseLong(mes) + 1;
        return "redirect:/mailbox/read/" + id;
    }

    @RequestMapping(value = "/prevmail/{mes}")
    public String readPrevMail(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        Long id = Long.parseLong(mes) - 1;
        return "redirect:/mailbox/read/" + id;
    }

    @RequestMapping(value = "/delete/{mes}")
    public String readDelete(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
        Ejb.getInterface().lookupEMailRemote().delete(obj.getIdEmailSluzebny(), "INBOX", Integer.parseInt(mes));
        Long id = Long.parseLong(mes) - 1;
        return "redirect:/mailbox/read/" + id;
    }

    @RequestMapping(value = "/reply/{mes}")
    public String readForward(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
            CoutMessage cout= Ejb.getInterface().lookupEMailRemote().chek(obj.getIdEmailSluzebny());
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        MessageBean messge = null;
        Long id = Long.parseLong(mes);
        if (id < mail.size() || id > 0) {
            for (int i = 0; i < mail.size(); i++) {
                if (mail.get(i).getMsgId() == id) {
                    messge.setTo(mail.get(i).getFrom());
                    messge.setSubject("Re"+mail.get(i).getSubject());
                    break;
                }
            }
        } else {
            model.addAttribute("error", "Нет сообшений!");
        }
        model.addAttribute("mail", messge);
        if (cout.getNewMess()>0){model.addAttribute("newmail", cout.getNewMess());}
        if (cout.getDmail()>0){model.addAttribute("dmail", cout.getDmail());}
        if (cout.getBmail()>0){model.addAttribute("bmail", cout.getBmail());}
        if (cout.getSpammail()>0){model.addAttribute("spammail", cout.getSpammail());}
        if (cout.getNewMess()>0){model.addAttribute("newmailhead", cout.getNewMess()+" новых сообщений");}
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("coutmail", "Показано "+cout.getCout()+ " из " + cout.getCout());
        return "mailbox/compose";
    }

    @RequestMapping(value = "/draftsmail")
    public String draftsMail(@ModelAttribute("Email") MessageBean Email,HttpSession httpSession, Model model) {
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        System.out.println(Email.getTo());
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
    @RequestMapping(value = "/sendmail")
    public String sendMail(@ModelAttribute("Email")MessageBean Email,
            HttpSession httpSession, Model model) {
        Polzovateli obj = (Polzovateli) httpSession.getAttribute("user");
        Ejb.getInterface().lookupEMailRemote().sent(obj.getIdEmailSluzebny(),Email);
       ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
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
    @RequestMapping(value = "/reflesh")
        public String Reflesh(HttpSession httpSession, Model model) {
        return "redirect:/mailbox/index/";
    }
}
