/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Email;
import com.sun.mail.util.MailSSLSocketFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Date.from;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;


/**
 *
 * @author Vladimir
 */
@Stateful
public class EMail implements EMailRemote {

   
    final String ENCODING = "koi8-r";

    @Override
    public void delete(String user, String password, int n, String host) {
    }

    @Override
    public LinkedList<MessageBean> receive(String what, Email mail) {
        try {
        
        // Создание свойств
            Properties props = new Properties();
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.imaps.ssl.trust", "*");
            java.security.Security.setProperty("ssl.SocketFactory.provider",
                    "by.Kursovaa.LogicAvtushkoVM.MySSLSocketFactory");
            props.put("mail.imaps.ssl.socketFactory", sf);

            //включение debug-режима
            props.put("mail.debug", "true");

            //Указываем протокол - IMAP с SSL
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            Store store = session.getStore();

            //подключаемся к почтовому серверу
            store.connect(mail.getImap(), mail.getLogin(), mail.getPassword());

            //получаем папку с входящими сообщениями
            Folder inbox = store.getFolder(what);

            //открываем её только для чтения
            inbox.open(Folder.READ_ONLY);

            //получаем последнее сообщение (самое старое будет под номером 1)
            Message[] messages = inbox.getMessages();
            ArrayList<String> attachments = new ArrayList<String>();
            LinkedList<MessageBean> listMessages = getPart(messages, attachments);
            //          inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
            inbox.close(false);
            store.close();
            return listMessages;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private LinkedList<MessageBean> getPart(Message[] messages, ArrayList<String> attachments) {
        LinkedList<MessageBean> listMessages = new LinkedList<MessageBean>();
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        for (Message inMessage : messages) {
            try {
                attachments.clear();
                if (inMessage.isMimeType("text/plain")) {
                    MessageBean message = new MessageBean(inMessage.getMessageNumber(), MimeUtility.decodeText(inMessage.getSubject()), MimeUtility.decodeText(inMessage.getFrom()[0].toString()), null, f.format(inMessage.getSentDate()), (String) inMessage.getContent(), false, null);
                    listMessages.add(message);
                } else if (inMessage.isMimeType("multipart/*")) {
                    Multipart mp = (Multipart) inMessage.getContent();
                    MessageBean message = null;
                    for (int i = 0; i < mp.getCount(); i++) {
                        Part part = mp.getBodyPart(i);
                        if ((part.getFileName() == null || part.getFileName() == "") && part.isMimeType("text/plain")) {
                            message = new MessageBean(inMessage.getMessageNumber(), inMessage.getSubject(), inMessage.getFrom()[0].toString(), null, f.format(inMessage.getSentDate()), (String) part.getContent(), false, null);
                        } else if (part.getFileName() != null || part.getFileName() != "") {
                            if ((part.getDisposition() != null) && (part.getDisposition().equals(Part.ATTACHMENT))) {
                                attachments.add(saveFile(MimeUtility.decodeText(part.getFileName()), part.getInputStream()));
                                if (message != null) {
                                    message.setAttachments(attachments);
                                }
                            }
                        }
                    }
                    listMessages.add(message);
                }
            } catch (MessagingException ex) {
                Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listMessages;
    }

    private String saveFile(String filename, InputStream inputStream) {
        String path = "attachments\\" + filename;
        try {
            byte[] attachment = new byte[inputStream.available()];
            inputStream.read(attachment);
            File file = new File(path);
            FileOutputStream out = new FileOutputStream(file);
            out.write(attachment);
            inputStream.close();
            out.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public void send(String to, String content, String subject, ArrayList<String> attachments,Email mail) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", mail.getSmtp());
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new MyAuthenticator(mail.getLogin(), mail.getPassword());
            Session mailSession = Session.getDefaultInstance(props, auth);
            // uncomment for debugging infos to stdout
            // mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();
            MimeMessage msg = new MimeMessage(mailSession);

            msg.setFrom(new InternetAddress(mail.getLogin()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject, ENCODING);

            if (attachments.size() == 0) {
                msg.setText(content, ENCODING);
            } else {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(content, "text/plain; charset=" + ENCODING + "");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                addAttachments(attachments, multipart);
                msg.setContent(multipart);
            }

            Transport.send(msg);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void addAttachments(ArrayList<String> attachments, Multipart multipart) throws MessagingException, UnsupportedEncodingException {
        for (int i = 0; i < attachments.size(); i++) {
            String filename = attachments.get(i);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
            multipart.addBodyPart(attachmentBodyPart);
        }
    }
}
