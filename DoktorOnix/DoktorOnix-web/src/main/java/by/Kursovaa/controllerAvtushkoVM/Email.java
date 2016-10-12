/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

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
    public String homeMail(Model model, HttpSession httpSession) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        System.out.println(em.getLogin());
        ArrayList<MessageBean> mail = Ejb.getInterface().lookupEmailServiceRemote().fetch(em, "INBOX");
        httpSession.setAttribute("Email", mail);
        httpSession.setAttribute("mailbox", "Inbox");
        model.addAttribute("mailbox", "Inbox");
        model.addAttribute("Email", mail);
        model.addAttribute("zagolovok", "Входящие");
        setModel(model, em);
        model.addAttribute("User", obj);
        return "mailbox/mailbox";
    }

    @RequestMapping(value = "/new")
    public String newMail(HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        MessageBean messge = new MessageBean();
        model.addAttribute("Email", messge);
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        setModel(model, em);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/index/{inbox}")
    public String readSend(@PathVariable("inbox") String inbox, HttpSession httpSession, Model model) {
        boolean flag = false;
        String temp = "INBOX." + inbox;
        if ("INBOX.Drafts".equals(temp)) {
            model.addAttribute("zagolovok", "Черновики");
            httpSession.setAttribute("mailbox", "Drafts");
            flag = true;
        }
        if ("INBOX.Trash".equals(temp)) {
            model.addAttribute("zagolovok", "Коризина");
            httpSession.setAttribute("mailbox", "Trash");
            flag = true;
        }
        if ("INBOX.Sent".equals(temp)) {
            model.addAttribute("zagolovok", "Отправленные");
            httpSession.setAttribute("mailbox", "Sent");
            flag = true;
        }
        if ("INBOX.Junk".equals(temp)) {
            model.addAttribute("zagolovok", "Спам");
            httpSession.setAttribute("mailbox", "Junk");
            flag = true;
        }
        if (flag) {
            by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
            by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
            em.setLogin("ras@doktoronix.net.ua");
            em.setPassword("1629807V");
            em.setSmtp("mail.ukraine.com.ua");
            em.setImap("mail.ukraine.com.ua");
            model.addAttribute("User", obj);
            ArrayList<MessageBean> mail
                    = Ejb.getInterface().lookupEmailServiceRemote().fetch(em, temp);
            CoutMessage a = Ejb.getInterface().lookupEmailServiceRemote().chek(em);
            httpSession.setAttribute("Email", mail);
            model.addAttribute("Email", mail);
            model.addAttribute("mailbox", (String) httpSession.getAttribute("mailbox"));
            setModel(model, em);
        }
        return "/mailbox/mailbox";
    }

    @RequestMapping(value = "/read/{mes}")
    public String readMail(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        MessageBean messge = null;
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        Long id = Long.parseLong(mes);
        if (id < mail.size() || id > 0) {
            for (int i = 0; i < mail.size(); i++) {
                if (mail.get(i).getMsgId() == id) {
                    messge = mail.get(i);
                    break;
                }
            }
        } else {
            if (id >= mail.size()) {
                messge = mail.get(mail.size() - 1);
            }
            if (id < 0) {
                messge = mail.get(0);
            }
            model.addAttribute("info", "Нет сообшений!");
        }
        if (messge.getIsNew()) {
            String temp = (String) httpSession.getAttribute("mailbox");
            if ("Inbox".equals(temp)) {
                temp = "INBOX";
            } else {
                temp = "INBOX." + temp;
            }
            Ejb.getInterface().lookupEmailServiceRemote().messegeRead(em,
                    messge, temp);
        }
        model.addAttribute("mail", messge);
        setModel(model, em);
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
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        String temp = (String) httpSession.getAttribute("mailbox");
        if ("Inbox".equals(temp)) {
            temp = "INBOX";
        } else {
            temp = "INBOX." + temp;
        }
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        Ejb.getInterface().lookupEmailServiceRemote().delete(em, temp, Integer.parseInt(mes));
        Long id = Long.parseLong(mes) - 1;
        return "redirect:/mailbox/read/" + id;
    }

    @RequestMapping(value = "/reply/{mes}")
    public String readReply(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        MessageBean messge = new MessageBean();
        Long id = Long.parseLong(mes);
        if (id < mail.size() || id > 0) {
            for (int i = 0; i < mail.size(); i++) {
                if (mail.get(i).getMsgId() == id) {
                    messge.setTo(mail.get(i).getFrom());
                    messge.setSubject("Re: " + mail.get(i).getSubject());
                    break;
                }
            }
        }
        model.addAttribute("Email", messge);
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        setModel(model, em);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/forward/{mes}")
    public String readForward(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        MessageBean messeg = new MessageBean();
        Long id = Long.parseLong(mes);
        if (id < mail.size() || id > 0) {
            for (int i = 0; i < mail.size(); i++) {
                if (mail.get(i).getMsgId() == id) {
                    messeg = mail.get(i);
                    ArrayList<String> comtent = messeg.getContent();
                    comtent.add(0, "---------- Forwarded message ----------");
                    comtent.add(1, "From: " + mail.get(i).getFrom());
                    comtent.add(2, "Date: " + mail.get(i).getDateSent());
                    comtent.add(3, "Subject: " + mail.get(i).getSubject());
                    comtent.add(4, "To: " + mail.get(i).getTo());
                    comtent.add(5, "-------------------------------------------");
                    messeg.setSubject("Fwd: " + mail.get(i).getSubject());
                    messeg.setTo("");
                    messeg.setContent(comtent);
                    break;
                }
            }
        }
        model.addAttribute("Email", messeg);
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        setModel(model, em);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/draftsmail")
    public String draftsMail(@ModelAttribute("Email") MessageBean Email, HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        Email.setAttachments(new ArrayList<String>());
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        Ejb.getInterface().lookupEmailServiceRemote().draft(Email, em);
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        httpSession.setAttribute("mailbox", "Inbox");
        model.addAttribute("mailbox", "Inbox");
        model.addAttribute("Email", mail);
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("success", "Сообщение было успешно сохранено!");
        setModel(model, em);
        return "mailbox/mailbox";
    }

    @RequestMapping(value = "/sendmail")
    public String sendMail(@ModelAttribute("Email") MessageBean Email,
            HttpSession httpSession, Model model) {
        by.kursovaa.dbAvtushkoVM.Polzovateli obj = (by.kursovaa.dbAvtushkoVM.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.dbAvtushkoVM.Email em = new by.kursovaa.dbAvtushkoVM.Email();
        em.setLogin("ras@doktoronix.net.ua");
        em.setPassword("1629807V");
        em.setSmtp("mail.ukraine.com.ua");
        em.setImap("mail.ukraine.com.ua");
        model.addAttribute("User", obj);
        Ejb.getInterface().lookupEmailServiceRemote().sent(em, Email);
        ArrayList<MessageBean> mail = (ArrayList<MessageBean>) httpSession.getAttribute("Email");
        httpSession.setAttribute("mailbox", "Inbox");
        model.addAttribute("mailbox", "Inbox");
        model.addAttribute("Email", mail);
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("success", "Сообщение было успешно отправлено!");
        setModel(model, em);
        return "mailbox/mailbox";
    }

    @RequestMapping(value = "/reflesh")
    public String Reflesh(HttpSession httpSession, Model model) {
        return "redirect:/mailbox/index/";
    }

    private void setModel(Model model, by.kursovaa.dbAvtushkoVM.Email obj) {
        CoutMessage cout = Ejb.getInterface().lookupEmailServiceRemote().chek(obj);
        if (cout.getNewMess() > 0) {
            model.addAttribute("newmail", cout.getNewMess());
        }
        if (cout.getDmail() > 0) {
            model.addAttribute("dmail", cout.getDmail());
        }
        if (cout.getBmail() > 0) {
            model.addAttribute("bmail", cout.getBmail());
        }
        if (cout.getSpammail() > 0) {
            model.addAttribute("spammail", cout.getSpammail());
        }
        if (cout.getNewMess() > 0) {
            model.addAttribute("newmailhead", cout.getNewMess() + " новых сообщений");
        }
        model.addAttribute("coutmail", "Показано " + cout.getCout() + " из " + cout.getCout());
    }
}
