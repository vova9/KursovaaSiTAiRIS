/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.ejb.Stateful;
import javax.mail.MessagingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

/**
 *
 * @author Vladimir
 */
@Stateful
public class EMail implements EMailRemote {

    @Override
    public void delete(String user, String password, int n, String host) throws MessagingException, IOException {
    }

    @Override
    public LinkedList<MessageBean> receive(String user, String password1, String host1) {
        Properties props = new Properties();
        String host = "mail.ukraine.com.ua";
        String username = "ras@doktoronix.net.ua";
        String password = "1629807V";
        String provider = "pop3";
        // String provider = "imap";
        try {
            //Connect to the server
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore(provider);
            store.connect(host, username, password);

            //open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            ArrayList<String> attachments = new ArrayList<String>();

            LinkedList<MessageBean> listMessages = getPart(messages, attachments);

            inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
            inbox.close(false);
            store.close();
            return listMessages;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
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
                    MessageBean message = new MessageBean(inMessage.getMessageNumber(), MimeUtility.decodeText(inMessage.getSubject()), inMessage.getFrom()[0].toString(), null, f.format(inMessage.getSentDate()), (String) inMessage.getContent(), false, null);
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
}
