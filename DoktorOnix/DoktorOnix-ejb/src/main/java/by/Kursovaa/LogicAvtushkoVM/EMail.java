/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import com.sun.mail.util.MailSSLSocketFactory;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
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
@Stateless
public class Email implements EmailRemote {

    final String ENCODING = "koi8-r";

    @Override
    public ArrayList<MessageBean> fetch(by.kursovaa.dbAvtushkoVM.Email mail, String what) {
        ArrayList<MessageBean> listMail = new ArrayList();
        try {
            // create the folder object and open it
            Store store = connection(mail);
            Folder emailFolder = store.getFolder(what);
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            int newMes = emailFolder.getNewMessageCount();
            for (int i = 0; i < messages.length; i++) {
                ArrayList<String> arr = new ArrayList();
                ArrayList<String> content = new ArrayList();
                Message message = messages[i];
                listMail.add(writePart(message, arr, content));
                if (i > messages.length - newMes) {
                    listMail.get(i).setNew(true);
                }
            }
            emailFolder.close(false);
            store.close();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.reverse(listMail);
        return listMail;
    }

    @Override
    public void sent(by.kursovaa.dbAvtushkoVM.Email mail, MessageBean mess) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", mail.getSmtp());
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new MyAuthenticator(mail.getLogin(), mail.getPassword());
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mail.getLogin()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mess.getTo()));
            msg.setSubject(mess.getSubject(), ENCODING);
            if (mess.getAttachments().size() == 0) {
                if (mess.getContent().size() != 0) {
                    String temp="";
                    for (int i=0;i<mess.getContent().size();i++){
                        if (mess.getContent().get(i).indexOf("<p>")==-1){
                        temp+="<p>"+mess.getContent().get(i)+"</p>";
                        } 
                        else{temp+=mess.getContent().get(i);}
                    }
                    msg.setContent(temp, "text/html; charset=" + ENCODING + "");}
            } else {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(mess.getContent().get(0), "text/plain; charset=" + ENCODING + "");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                addAttachments(mess.getAttachments(), multipart);
                msg.setContent(multipart);
            }
            Transport.send(msg);
            Properties prop = new Properties();
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.imaps.ssl.trust", "*");
            java.security.Security.setProperty("ssl.SocketFactory.provider",
                    "by.Kursovaa.LogicAvtushkoVM.MySSLSocketFactory");
            prop.put("mail.imaps.ssl.socketFactory", sf);

            //включение debug-режима
            prop.put("mail.debug", "true");

            //Указываем протокол - IMAP с SSL
            prop.put("mail.store.protocol", "imaps");
            session = Session.getInstance(prop);
            Store store = session.getStore();
            //подключаемся к почтовому серверу
            store.connect(mail.getImap(), mail.getLogin(), mail.getPassword());
            Folder emailFolder = store.getFolder("INBOX.Sent");
            emailFolder.open(Folder.READ_WRITE);
            emailFolder.appendMessages(new Message[]{msg});
            emailFolder.close(true);
        } catch (AddressException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void draft(MessageBean Email, by.kursovaa.dbAvtushkoVM.Email mail) {
        try {
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
            Folder emailFolder = store.getFolder("INBOX.Drafts");
            emailFolder.open(Folder.READ_WRITE);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getLogin()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(Email.getTo()));
            message.setSubject(Email.getSubject(), ENCODING);
            if (Email.getAttachments().size() == 0) {
                if (Email.getContent().size() != 0) {
                    String temp="";
                    for (int i=0;i<Email.getContent().size();i++){
                        if (Email.getContent().get(i).indexOf("<p>")==-1){
                        temp+="<p>"+Email.getContent().get(i)+"</p>";
                        } 
                        else{temp+=Email.getContent().get(i);}
                    }
                    message.setContent(temp, "text/html; charset=" + ENCODING + "");}
            }  else {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(Email.getContent().get(0), "text/plain; charset=" + ENCODING + "");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                addAttachments(Email.getAttachments(), multipart);
                message.setContent(multipart);
            }
            emailFolder.appendMessages(new Message[]{message});
            emailFolder.close(true);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean newMess(by.kursovaa.dbAvtushkoVM.Email mail) {
        boolean newMess = false;
        try {
            Store store = connection(mail);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            newMess = emailFolder.hasNewMessages();
            emailFolder.close(false);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newMess;
    }

    @Override
    public CoutMessage chek(by.kursovaa.dbAvtushkoVM.Email mail) {
        CoutMessage cout = new CoutMessage();
        try {
            Store store = connection(mail);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            cout.setCout(emailFolder.getMessageCount());
            cout.setNewMess(emailFolder.getNewMessageCount());
            emailFolder = store.getFolder("INBOX.Drafts");
            emailFolder.open(Folder.READ_ONLY);
            cout.setDmail(emailFolder.getMessageCount());
            emailFolder = store.getFolder("INBOX.Junk");
            emailFolder.open(Folder.READ_ONLY);
            cout.setSpammail(emailFolder.getNewMessageCount());
            emailFolder.close(false);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cout;
    }

    @Override
    public void messegeRead(by.kursovaa.dbAvtushkoVM.Email mail, MessageBean mess, String what) {
        try {
            Store store = connection(mail);
            Folder emailFolder = store.getFolder(what);
            emailFolder.open(Folder.READ_WRITE);
            Message messages = emailFolder.getMessage(mess.getMsgId());
            messages.setFlag(Flags.Flag.USER, true);
            emailFolder.close(true);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(by.kursovaa.dbAvtushkoVM.Email mail, String what, Integer mess) {
        try {
            Store store = connection(mail);
            Folder emailFolder = store.getFolder(what);
            emailFolder.open(Folder.READ_WRITE);
            Message messages = emailFolder.getMessage(mess);
            messages.setFlag(Flags.Flag.DELETED, true);
            emailFolder.close(true);
            store.close();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MessageBean writePart(Part p, ArrayList<String> arr, ArrayList<String> content) {
        MessageBean messageBean = new MessageBean();
        try {
            if (p instanceof Message) {
                messageBean = writeEnvelope((Message) p, messageBean);
            }
            //check if the content is plain text
            if (p.isMimeType("text/plain")) {
                String a = (String) p.getContent();
                while (true) {
                    if (a.indexOf("\n") != -1) {
                        content.add(a.substring(0, a.indexOf("\n")));
                        a = a.substring(a.indexOf("\n") + 1, a.length());
                    } else {
                        content.add(a);
                        break;
                    }
                }
            } else if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    writePart(mp.getBodyPart(i), arr, content);
                }
            } //check if the content is a nested message
            else if (p.isMimeType("message/rfc822")) {
                writePart((Part) p.getContent(), arr, content);
            } //check if the content is an inline image
            else if (p.isMimeType("image/jpeg")) {
                arr.add(MimeUtility.decodeText(p.getFileName()));
                Object o = p.getContent();
                InputStream x = (InputStream) o;
                // Construct the required byte array
                int i = 0;
                byte[] bArray = new byte[x.available()];
                while ((i = (int) ((InputStream) x).available()) > 0) {
                    int result = (int) (((InputStream) x).read(bArray));
                    if (result == -1) {
                        break;
                    }
                }
                FileOutputStream f2 = new FileOutputStream("D://email//" + "image.jpg");
                f2.write(bArray);
            } else if (p.getContentType().contains("image/")) {
                arr.add(MimeUtility.decodeText(p.getFileName()));
                File f = new File("D://email//" + MimeUtility.decodeText(p.getFileName()) + ".jpg");
                DataOutputStream output = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(f)));
                com.sun.mail.util.BASE64DecoderStream test
                        = (com.sun.mail.util.BASE64DecoderStream) p
                        .getContent();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = test.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } else {
                Object o = p.getContent();
                if (o instanceof String) {
                    if ((p.getDisposition() != null) && (p.getDisposition().equals(Part.ATTACHMENT))) {
                        arr.add(MimeUtility.decodeText(p.getFileName()));
                        File file = new File("D://email//" + MimeUtility.decodeText(p.getFileName()));
                        FileWriter writer = new FileWriter(file);
                        writer.write((String) o);
                        writer.close();
                    }
                    if ((p.getFileName() == null || p.getFileName() == "") && p.isMimeType("text/html")) {
                        String a = (String) o;
                        content.add(a);
                        while (true) {
                            if (a.indexOf("<br>") != -1) {
                                content.add(a.substring(0, a.indexOf("<br>")));
                                a = a.substring(a.indexOf("<br>") + 4, a.length());
                            } else {
                                content.add(a);
                                break;
                            }
                        }
                    }
                } else if (o instanceof InputStream) {
                    arr.add(MimeUtility.decodeText(p.getFileName()));
                    File file = new File("D://email//" + MimeUtility.decodeText(p.getFileName()));
                    FileWriter writer = new FileWriter(file);
                    InputStream is = (InputStream) o;
                    is = (InputStream) o;
                    int c;
                    while ((c = is.read()) != -1) {
                        writer.write(c);
                    }
                    writer.close();
                }
            }
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        messageBean.setAttachments(arr);
        messageBean.setContent(content);
        return messageBean;
    }

    private MessageBean writeEnvelope(Message m, MessageBean messageBean) {
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        messageBean.setMsgId(m.getMessageNumber());
        try {
            Address[] a;
            if ((a = m.getFrom()) != null) {
                messageBean.setFrom(MimeUtility.decodeText(a[0].toString()));
            }
            if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
                messageBean.setTo(MimeUtility.decodeText(a[0].toString()));
            }
            if (m.getSubject() != null) {
                messageBean.setSubject(m.getSubject());
            }
            if (m.getSentDate() != null) {
                messageBean.setDateSent(f.format(m.getSentDate()));
            }
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messageBean;
    }

    private void addAttachments(ArrayList<String> attachments, Multipart multipart) throws MessagingException, UnsupportedEncodingException {
        for (int i = 0; i < attachments.size(); i++) {
            String filename = "D://email//" + attachments.get(i);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
            multipart.addBodyPart(attachmentBodyPart);
        }
    }

    private Store connection(by.kursovaa.dbAvtushkoVM.Email mail) throws GeneralSecurityException, NoSuchProviderException, MessagingException {
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
        return store;
    }

}
