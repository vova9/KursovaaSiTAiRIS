/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.service;

import by.kursovaa.pojo.MessageInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import by.kursovaa.entity.Email;
import by.kursovaa.service.Path;
import java.sql.SQLException;

/**
 *
 * @author Vladimir
 */
public class Synchronized implements Runnable {

    private Imap imap;

    private String textError;
    private Email accountInfo;
    private String url;

    @Override
    public void run() {
        System.out.println("Synchronized::run ST");

        url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            File dir = new File(url);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            DataBaseEmail.connect(url);
            imap.connectionImap(accountInfo);

            Folder[] emailFolder = imap.getFolders();

            for (int i = 0; i < emailFolder.length; i++) {
                String folderName = emailFolder[i].getName();
                emailFolder[i].open(Folder.READ_ONLY);

                DataBaseEmail.createTable(folderName);
                DataBaseEmail.updateSynchronized(folderName);

                System.out.println("Synchronized::run folder name:" + folderName);

                int countMessage = emailFolder[i].getMessageCount();
                Message messages = null;

                System.out.println("Synchronized::run count message:" + countMessage);

                for (int j = countMessage; j > 0; j--) {
                    messages = emailFolder[i].getMessage(j);
                    String messageID = imap.getMessageID((MimeMessage) messages);

                    if (messageID.isEmpty() || "null".equals(messageID)) {
                        System.out.println("Synchronized::run id: " + messages.getMessageNumber());
                        System.out.println("Synchronized::run Sent Date: " + messages.getSentDate());

                        messageID = messages.getSentDate().toString();
                      
                        if (!messageID.isEmpty() || !"null".equals(messageID)) {
                            messageID = messageID.replace(" ", "");
                            messageID = messageID.replace(":", "");
                        } else {
                            messageID = messages.getReceivedDate().toString();
                            messageID = messageID.replace(" ", "");
                            messageID = messageID.replace(":", "");
                        }
                    }

                    MessageInfo messageInfo = new MessageInfo();

                    System.out.println("Synchronized::run messageID:" + messageID);

                    if (DataBaseEmail.getMessage(folderName, messageID, messageInfo)) {

                        if (messageInfo.getSize() == messages.getSize()) {
                            DataBaseEmail.updatedSynchronized(folderName, messageID);
                        } else {
                            saveMessage(messageID, folderName, messages, messageInfo);
                            DataBaseEmail.updateAll(folderName, messageInfo);
                            continue;
                        }

                        if (messageInfo.getMsgId() != messages.getMessageNumber()) {
                            DataBaseEmail.updatedMessageNumber(folderName, messageID, messages.getMessageNumber());
                        }

                        if (imap.getFlag(messageInfo, messages)) {
                            DataBaseEmail.updatedFlags(folderName, messageInfo);
                        }

                        if (messages.isMimeType("multipart/*") && !messageInfo.isAttachment()) {
                            DataBaseEmail.updatedAttachment(folderName, true, messageID);
                        } else if (!messages.isMimeType("multipart/*") && messageInfo.isAttachment()) {
                            DataBaseEmail.updatedAttachment(folderName, false, messageID);
                        }

                        DataBaseEmail.updatedSynchronized(folderName, messageID);
                    } else {
                        saveMessage(messageID, folderName, messages, messageInfo);
                        DataBaseEmail.insert(folderName, messageInfo);
                    }
                }
                emailFolder[i].close(false);
            }

        } catch (MessagingException | GeneralSecurityException ex) {
            Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
            try {
                DataBaseEmail.updateSynchronizedAll();
            } catch (SQLException ex1) {
                Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } finally {

            try {
                imap.close();
            } catch (MessagingException ex) {
                Logger.getLogger(Synchronized.class.getName()).log(Level.SEVERE, null, ex);
                textError = ex.getMessage();
            }

        }

        System.out.println("Synchronized::run OK");
    }

    public void saveMessage(String messageID, String folderName, Message messages, MessageInfo messageInfo)
            throws IOException, MessagingException {

        System.out.println("Synchronized::saveMessage ST");

        String nameFile = messageID + ".eml";

        File subDir = new File(url + File.separator + folderName);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }

        System.out.println(">>>>" + url + File.separator + folderName + File.separator + nameFile);

        messages.writeTo(new FileOutputStream(new File(url + File.separator + folderName
                + File.separator + nameFile)));

        imap.getMessageInfo((MimeMessage) messages, messageInfo);
        imap.getFlag(messageInfo, messages);

        messageInfo.setMessageID(messageID);
        messageInfo.setDirectory(folderName);
        messageInfo.setSynchronize(true);

        System.out.println("Synchronized::saveMessage OK");
    }

    public void setAccountInfo(Email accountInfo) {
        System.out.println("Synchronized::setAccountInfo STOK");
        this.accountInfo = accountInfo;
    }

    public void setImap(Imap imap) {
        this.imap = imap;
    }

    public String getTextError() {
        System.out.println("Synchronized::getTextError STOK");
        return textError;
    }
}
