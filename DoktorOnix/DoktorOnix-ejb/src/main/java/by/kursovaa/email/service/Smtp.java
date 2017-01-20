/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.service;

import by.kursovaa.email.interfaces.SmtpLocal;
import by.kursovaa.entity.Email;
import by.kursovaa.pojo.FileMeta;
import by.kursovaa.pojo.MessageInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Part;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Vladimir
 */
public class Smtp implements SmtpLocal {

    private final String ENCODING = "koi8-r";
    private Session session;
    private MimeMessage message;
    private String contentType;

    @Override
    public void createMessage(Email accountInfo, MessageInfo messageInfo)
            throws MessagingException, IOException {

        System.out.println("Smtp::createMessage ST");

        contentType = "";
        message = new MimeMessage(session);
        message.setFrom(new InternetAddress(accountInfo.getLogin()));

        addRecipient(messageInfo);

        if (messageInfo.getAttachments().isEmpty()) {
            System.out.println("Smtp::createMessage isEmpty");
            addContent(messageInfo, message);
        } else {
            BodyPart messageBodyPart = new MimeBodyPart();

            addContent(messageInfo, messageBodyPart);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            addAttachments(messageInfo.getAttachments(), multipart);

            message.setContent(multipart);
        }

        System.out.println("Smtp::createMessage OK");
    }

    @Override
    public void connectionSmtp(Email accountInfo) {
        System.out.println("Smtp::connectionSmtp ST");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", accountInfo.getSmtp());
        props.put("mail.smtp.auth", "true");

        MyAuthenticator auth = new MyAuthenticator(accountInfo.getLogin(), accountInfo.getPassword());

        session = Session.getDefaultInstance(props, auth);
        session.setDebug(true);

        System.out.println("Smtp::connectionSmtp OK");
    }

    @Override
    public void send() throws MessagingException {
        Transport.send(message);
    }

    @Override
    public MimeMessage getMessage() {
        return message;
    }

    private void addContent(MessageInfo messageInfo, Part messageBodyPart) throws MessagingException {
        System.out.println("Smtp::addContent ST");

        if (!messageInfo.getContent().isEmpty()) {
            String text = createBoby(messageInfo);

            System.out.println("Smtp::addContent text" + text);
            System.out.println("Smtp::addContent contentType" + contentType);

            if (!text.isEmpty()) {
                messageBodyPart.setContent(text, "text/html ; charset=" + ENCODING + "");
            }
        } else {
            System.out.println("Smtp::addContent uzas!!!");
        }

        System.out.println("Smtp::addContent OK");
    }

    private String createBoby(MessageInfo messageInfo) {
        System.out.println("Smtp::createBoby ST");

        String text = "";
        for (int i = 0; i < messageInfo.getContent().size(); i++) {

            if (messageInfo.getContent().get(i).indexOf("<p>") == -1) {
                text += "<p>" + messageInfo.getContent().get(i) + "</p>";

                System.out.println("Smtp::createBoby text" + text);

                if (contentType.isEmpty()) {
                    contentType = "text/html";
                }
            } else {
                text += messageInfo.getContent().get(i);
                if (contentType.isEmpty()) {
                    contentType = "text/plain";
                }
            }
        }

        System.out.println("Smtp::addContent OK");
        return text;
    }

    private void addRecipient(MessageInfo messageInfo) throws AddressException, MessagingException {
        for (int i = 0; i < messageInfo.getTo().size(); i++) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(messageInfo.getTo().get(i)));
        }

        for (int i = 0; i < messageInfo.getCc().size(); i++) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(messageInfo.getCc().get(i)));
        }

        for (int i = 0; i < messageInfo.getBcc().size(); i++) {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(messageInfo.getBcc().get(i)));
        }

        message.setSubject(messageInfo.getSubject(), ENCODING);
    }

    private void addAttachments(ArrayList<FileMeta> attachments, Multipart multipart)
            throws MessagingException, UnsupportedEncodingException, IOException {

        System.out.println("Smtp::addAttachments ST");

        MimeBodyPart attachmentBodyPart;

        for (int i = 0; i < attachments.size(); i++) {
            FileDataSource fileDataSource = new FileDataSource(attachments.get(i).getFileName());

            OutputStream outputStream = fileDataSource.getOutputStream();
            outputStream.write(attachments.get(i).getBytes());

            attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentBodyPart.setFileName(MimeUtility.encodeText(attachments.get(i).getFileName()));

            multipart.addBodyPart(attachmentBodyPart);
        }

        System.out.println("Smtp::addAttachments OK");
    }
}
