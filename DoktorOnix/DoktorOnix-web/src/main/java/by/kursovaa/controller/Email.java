/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.controller;

import by.kursovaa.service.Ejb;
import by.kursovaa.pojo.CountMessage;
import by.kursovaa.pojo.FileMeta;
import by.kursovaa.pojo.MessageInfo;
import by.kursovaa.service.Translit;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author Vladimir
 */
@Controller
@RequestMapping("/mailbox")
public class Email {

    private Map colMail = new HashMap<String, MessageInfo>();
    private final int COUNT_MAIL_IN_PAGE = 50;
    private int nomberPage = 0;
    private int nomberLast = 0;
    private CountMessage count;
    private ArrayList<FileMeta> attachments = null;
    private static final String EMAIL_PATTERN = "^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$";
    private static final String EMAIL_NAME_PATTERN = "([-a-z0-9~!$%^&*_=@+.\\s]*)(<[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?[>]$)";

    // ----- mailbox -----------------------------------------------
    @RequestMapping(value = "/index")
    public String homeMail(Model model, HttpSession httpSession) {
        nomberPage = 0;
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

        ArrayList<MessageInfo> mailList = Ejb.getInterface().lookupEmailServiceRemote().
                getMessageList(accountInfo, "Inbox", COUNT_MAIL_IN_PAGE, 0);

        if (!mailList.isEmpty()) {
            nomberLast = (int) mailList.get(0).getMsgId();
            initColMail(mailList);
        }

//        httpSession.setAttribute("Email", mailList);
        httpSession.setAttribute("mailbox", "INBOX");

        model.addAttribute("mailbox", "Inbox");
        model.addAttribute("Email", mailList);
        model.addAttribute("zagolovok", "Входящие");
        model.addAttribute("User", user);

        setModel(model, httpSession);
        return "mailbox/mailbox";
    }

    @RequestMapping(value = "/index/{inbox}")
    public String viewFolder(@PathVariable("inbox") String folderName, HttpSession httpSession, Model model) {
        boolean flag = false;
        nomberPage = 0;
        if ("Drafts".equals(folderName)) {
            model.addAttribute("zagolovok", "Черновики");
            httpSession.setAttribute("mailbox", "Drafts");
            flag = true;
        }
        if ("Trash".equals(folderName)) {
            model.addAttribute("zagolovok", "Коризина");
            httpSession.setAttribute("mailbox", "Trash");
            flag = true;
        }
        if ("Sent".equals(folderName)) {
            model.addAttribute("zagolovok", "Отправленные");
            httpSession.setAttribute("mailbox", "Sent");
            flag = true;
        }
        if ("Junk".equals(folderName)) {
            model.addAttribute("zagolovok", "Спам");
            httpSession.setAttribute("mailbox", "Junk");
            flag = true;
        }
        if (flag) {
            by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
            by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
            by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

            ArrayList<MessageInfo> mailList = Ejb.getInterface().lookupEmailServiceRemote().
                    getMessageList(accountInfo, folderName, COUNT_MAIL_IN_PAGE, 0);

            if (!mailList.isEmpty()) {
                nomberLast = (int) mailList.get(0).getMsgId();
                initColMail(mailList);
            }
//            httpSession.setAttribute("Email", mailList);

            model.addAttribute("Email", mailList);
            model.addAttribute("User", user);
            model.addAttribute("mailbox", (String) httpSession.getAttribute("mailbox"));

            setModel(model, httpSession);
        }
        return "/mailbox/mailbox";
    }

    @RequestMapping(value = "/mark/{messageID}/")
    public @ResponseBody
    String markMessage(@PathVariable("messageID") String messageID, HttpSession httpSession) {
        System.out.println("Email::markMessage ST " + messageID);

        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);
        MessageInfo messageInfo = (MessageInfo) colMail.get(messageID);

        Ejb.getInterface().lookupEmailServiceRemote().markFLAGGED(accountInfo, messageInfo);
        return "ok";
    }

    @RequestMapping(value = "/reflesh")
    public String reflesh(HttpSession httpSession, Model model) {
        return "redirect:/mailbox/index/";
    }

    @RequestMapping(value = "/next")
    public String nextPage(HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);
        String folderName = (String) httpSession.getAttribute("mailbox");

        nomberPage++;

        ArrayList<MessageInfo> mailList = Ejb.getInterface().lookupEmailServiceRemote().
                getMessageList(accountInfo, folderName, COUNT_MAIL_IN_PAGE, (nomberPage * COUNT_MAIL_IN_PAGE));

        if (!mailList.isEmpty()) {
            initColMail(mailList);
        }

        model.addAttribute("Email", mailList);
        model.addAttribute("User", user);
        model.addAttribute("mailbox", (String) httpSession.getAttribute("mailbox"));

        if ((nomberPage * COUNT_MAIL_IN_PAGE) > count.getCout()) {
            nomberPage--;
        }

        setModel(model, httpSession);
        return "/mailbox/mailbox";
    }

    @RequestMapping(value = "/previous")
    public String previousPage(HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);
        String folderName = (String) httpSession.getAttribute("mailbox");

        nomberPage--;
        if (nomberPage < 0) {
            nomberPage = 0;
        }

        ArrayList<MessageInfo> mailList = Ejb.getInterface().lookupEmailServiceRemote().
                getMessageList(accountInfo, folderName, COUNT_MAIL_IN_PAGE, (nomberPage * COUNT_MAIL_IN_PAGE));

        if (!mailList.isEmpty()) {
            initColMail(mailList);
        }

        model.addAttribute("Email", mailList);
        model.addAttribute("User", user);
        model.addAttribute("mailbox", (String) httpSession.getAttribute("mailbox"));

        setModel(model, httpSession);
        return "/mailbox/mailbox";
    }

// ----- read -----------------------------------------------
    @RequestMapping(value = "/read/{mes}")
    public String readMail(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

        model.addAttribute("User", user);
        Long id = Long.parseLong(mes);

        if (id < 1) {
            id = 1L;
        } else if (id > colMail.size()) {
            int colMailSize = colMail.size();
            id = Long.valueOf(colMailSize);
        }

        MessageInfo messageInfo = getMessageInfo(id);

        if (messageInfo != null) {
            if (!messageInfo.isRead()) {
                Ejb.getInterface().lookupEmailServiceRemote().markRead(accountInfo, messageInfo);
            }
            messageInfo = Ejb.getInterface().lookupEmailServiceRemote().getMessageBody(accountInfo, messageInfo);
        }

        model.addAttribute("mail", messageInfo);
        setModel(model, httpSession);
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

    @RequestMapping(value = "/downloadAttachment/{mesId}/{fileName}/")
    public void downloadAttachment(@PathVariable("mesId") String mesId, @PathVariable("fileName") String fileName,
            HttpServletResponse resp, HttpSession httpSession, Model model) {

        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

        Long id = Long.parseLong(mesId);
        MessageInfo messageInfo = getMessageInfo(id);

        if (messageInfo != null) {
            String url = accountInfo.getLogin() + File.separator + messageInfo.getDirectory()
                    + File.separator + messageInfo.getMessageID() + ".eml";

            byte[] bytes = Ejb.getInterface().lookupEmailServiceRemote().downloadAttachment(url, fileName);

            model.addAttribute("User", user);

            resp.reset();
            resp.resetBuffer();

            String mimeType = URLConnection.guessContentTypeFromName(fileName);
            if (mimeType == null) {
                System.out.println("mimetype is not detectable, will take default");
                mimeType = "application/octet-stream";
            }

            System.out.println("mimetype : " + mimeType);

            resp.setContentType(mimeType);
            resp.setHeader("Content-Disposition", String.format("inline; filename=\"" + Translit.cyr2lat(fileName) + "\""));
            resp.setContentLength(bytes.length);

            ServletOutputStream ouputStream;
            try {
                ouputStream = resp.getOutputStream();

                ouputStream.write(bytes, 0, bytes.length);
                ouputStream.flush();
            } catch (IOException ex) {
                Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @RequestMapping(value = "/delete/{mes}")
    public String readDelete(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

        Long id = Long.parseLong(mes);
        MessageInfo messageInfo = getMessageInfo(id);
        id--;

        Ejb.getInterface().lookupEmailServiceRemote().deleteMessage(accountInfo, messageInfo);
        return "redirect:/mailbox/read/" + id;
    }

// ----- compose -----------------------------------------------
    @RequestMapping(value = "/new")
    public String newMail(HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        MessageInfo messageInfo = new MessageInfo();
        attachments = new ArrayList<FileMeta>();

        model.addAttribute("Email", messageInfo);
        model.addAttribute("User", user);

        setModel(model, httpSession);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/sendmail")
    public String sendMail(@ModelAttribute("Email") MessageInfo Email, HttpSession httpSession, Model model) {
        System.out.println("sentmail");

        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

        if (checkRecipient(Email) == false) {
            model.addAttribute("Email", Email);
            model.addAttribute("User", user);
            model.addAttribute("error", "Не верный Email");
            setModel(model, httpSession);
            return "mailbox/compose";
        }

        Email.setMsgId(nomberLast++);

        for (int i = 0; i < attachments.size(); i++) {
            FileMeta fileMeta = new FileMeta(attachments.get(i));
            Email.addAttachments(fileMeta);
        }

        attachments.clear();

        System.out.println(Email.getAttachments().get(0).getFileName());

        Ejb.getInterface().lookupEmailServiceRemote().sentMessage(accountInfo, Email);

        model.addAttribute("success", "Сообщение было успешно отправлено!");
        return "redirect:/mailbox/index";
    }

    @RequestMapping(value = "/upload")
    public @ResponseBody
    ArrayList<FileMeta> uploadAttachment(MultipartHttpServletRequest request, HttpServletResponse response) {
        FileMeta fileMeta = null;
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;

        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());

            fileMeta = new FileMeta();
            fileMeta.setFileName(mpf.getOriginalFilename());
            fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
            fileMeta.setFileType(mpf.getContentType());

            try {
                fileMeta.setBytes(mpf.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (attachments == null) {
                attachments = new ArrayList<FileMeta>();
            }
            attachments.add(fileMeta);
        }
        return attachments;
    }

    @RequestMapping(value = "/reply/{mes}")
    public String readReply(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        attachments = new ArrayList<FileMeta>();
        Long id = Long.parseLong(mes);
        MessageInfo messageInfo = getMessageInfo(id);

        if (messageInfo != null) {
            messageInfo.setToString(messageInfo.getFrom());

            ArrayList<String> content = messageInfo.getContent();
            content.add(0, "---------- Original message ----------");
            content.add(1, "From: " + messageInfo.getFrom());
            content.add(2, "Date: " + messageInfo.getDateSent());
            content.add(3, "Subject: " + messageInfo.getSubject());

            String recipient = "";

            for (int i = 0; i < messageInfo.getTo().size(); i++) {
                recipient += messageInfo.getTo().get(i);
                recipient += ";";
            }
            content.add(4, "To: " + recipient);

            if (!messageInfo.getCc().isEmpty()) {
                recipient = "";
                for (int i = 0; i < messageInfo.getCc().size(); i++) {
                    recipient += messageInfo.getCc().get(i);
                    recipient += ";";
                }
                content.add(5, "Cc: " + recipient);
            }
            content.add(6, "-------------------------------------------");
            messageInfo.setSubject("Re: " + messageInfo.getSubject());
        }

        model.addAttribute("Email", messageInfo);
        model.addAttribute("User", user);
        setModel(model, httpSession);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/forward/{mes}")
    public String readForward(@PathVariable("mes") String mes, HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        attachments = new ArrayList<FileMeta>();
        Long id = Long.parseLong(mes);
        MessageInfo messageInfo = getMessageInfo(id);

        if (messageInfo != null) {

            ArrayList<String> content = messageInfo.getContent();
            content.add(0, "---------- Forward message ----------");
            content.add(1, "From: " + messageInfo.getFrom());
            content.add(2, "Date: " + messageInfo.getDateSent());
            content.add(3, "Subject: " + messageInfo.getSubject());

            String recipient = "";

            for (int i = 0; i < messageInfo.getTo().size(); i++) {
                recipient += messageInfo.getTo().get(i);
                recipient += ";";
            }
            content.add(4, "To: " + recipient);

            if (!messageInfo.getCc().isEmpty()) {
                recipient = "";
                for (int i = 0; i < messageInfo.getCc().size(); i++) {
                    recipient += messageInfo.getCc().get(i);
                    recipient += ";";
                }
                content.add(5, "Cc: " + recipient);
            }
            content.add(6, "-------------------------------------------");
            messageInfo.setSubject("Fwd: " + messageInfo.getSubject());
        }

        model.addAttribute("Email", messageInfo);
        model.addAttribute("User", user);
        setModel(model, httpSession);
        return "mailbox/compose";
    }

    @RequestMapping(value = "/draftsmail")
    public String draftsMail(@ModelAttribute("Email") MessageInfo Email, HttpSession httpSession, Model model) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);

        if (checkRecipient(Email) == false) {
            model.addAttribute("Email", Email);
            model.addAttribute("User", user);
            model.addAttribute("error", "Не верный Email");
            setModel(model, httpSession);
            return "mailbox/compose";
        }

        Ejb.getInterface().lookupEmailServiceRemote().saveDraft(accountInfo, Email);

        model.addAttribute("success", "Сообщение было успешно сохранено!");
        return "redirect:/mailbox/index";
    }

//    @RequestMapping(value = "/uploadDelete")
//    public @ResponseBody
//    ArrayList<FileMeta> uploadDelete(MultipartHttpServletRequest request, HttpServletResponse response) {
//    
//    }
    private boolean checkRecipient(MessageInfo messageInfo) {
        String[] toString = messageInfo.getToString().split(";");
        String[] ccString = messageInfo.getCcString().split(";");
        String[] bccString = messageInfo.getBccString().split(";");

        String recipient;

        for (int i = 0; i < toString.length; i++) {
            recipient = toString[i].trim();
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(recipient);
            if (matcher.matches()) {
                System.out.println("1");

                messageInfo.addRecipientTo(recipient);
            } else {
                System.out.println("2");

                Pattern pattern1 = Pattern.compile(EMAIL_NAME_PATTERN);
                Matcher matcher1 = pattern1.matcher(recipient);
                if (matcher1.matches()) {
                    System.out.println("3");

                    messageInfo.addRecipientTo(recipient);
                } else {
                    return false;
                }
            }
        }

        if (!messageInfo.getCcString().isEmpty()) {
            for (int i = 0; i < ccString.length; i++) {
                System.out.println("cc 1 " + ccString[i]);

                recipient = ccString[i].trim();
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(recipient);
                if (matcher.matches()) {
                    System.out.println("cc 2");

                    messageInfo.addRecipientCc(recipient);
                } else {
                    System.out.println("cc 3");

                    Pattern pattern1 = Pattern.compile(EMAIL_NAME_PATTERN);
                    Matcher matcher1 = pattern1.matcher(recipient);
                    if (matcher1.matches()) {
                        System.out.println("cc 4");

                        messageInfo.addRecipientCc(recipient);
                    } else {
                        return false;
                    }
                }
            }
        }

        if (!messageInfo.getBccString().isEmpty()) {
            for (int i = 0; i < bccString.length; i++) {
                System.out.println("bcc 1");

                recipient = bccString[i].trim();
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(recipient);
                if (matcher.matches()) {
                    messageInfo.addRecipientBcc(recipient);
                } else {
                    Pattern pattern1 = Pattern.compile(EMAIL_NAME_PATTERN);
                    Matcher matcher1 = pattern1.matcher(recipient);
                    if (matcher1.matches()) {
                        messageInfo.addRecipientBcc(recipient);
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void setModel(Model model, HttpSession httpSession) {
        by.kursovaa.entity.Polzovateli user = (by.kursovaa.entity.Polzovateli) httpSession.getAttribute("user");
        by.kursovaa.entity.Email email = user.getIdEmailSluzebny();
        by.kursovaa.entity.Email accountInfo = new by.kursovaa.entity.Email(email);
        String folderName = (String) httpSession.getAttribute("mailbox");
        count = Ejb.getInterface().lookupEmailServiceRemote().getMessageCout(accountInfo, folderName);

        boolean isSynchronization = Ejb.getInterface().lookupEmailServiceRemote().isSynchronization();
        String textError = Ejb.getInterface().lookupEmailServiceRemote().getTextError();

        if (count.getNewMess() > 0) {
            model.addAttribute("newmail", count.getNewMess());
        }
        if (count.getDmail() > 0) {
            model.addAttribute("dmail", count.getDmail());
        }
        if (count.getBmail() > 0) {
            model.addAttribute("bmail", count.getBmail());
        }
        if (count.getSpammail() > 0) {
            model.addAttribute("spammail", count.getSpammail());
        }
        if (count.getNewMess() > 0) {
            model.addAttribute("newmailhead", count.getNewMess() + " новых сообщений");
        }
        if (!textError.isEmpty()) {
            model.addAttribute("error", textError);
        }
        if (isSynchronization) {
            model.addAttribute("info", isSynchronization);
        }

        if (((nomberPage + 1) * COUNT_MAIL_IN_PAGE) > count.getCout()) {
            model.addAttribute("coutmail", "Показано " + count.getCout() + " из " + count.getCout());
        } else {
            model.addAttribute("coutmail", "Показано " + ((nomberPage + 1) * COUNT_MAIL_IN_PAGE) + " из " + count.getCout());
        }
    }

    private void initColMail(ArrayList<MessageInfo> mailList) {
        colMail.clear();

        for (int i = 0; i < mailList.size(); i++) {
            colMail.put(mailList.get(i).getMessageID(), mailList.get(i));
        }
    }

    private MessageInfo getMessageInfo(Long id) {
        if (!colMail.isEmpty()) {

            for (Iterator it = colMail.values().iterator(); it.hasNext();) {
                MessageInfo value = (MessageInfo) it.next();
                if (id == value.getId()) {
                    return value;
                }
            }
        }
        return null;
    }
}
