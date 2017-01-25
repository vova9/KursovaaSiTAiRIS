/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email;

import by.kursovaa.email.service.DataBaseEmail;
import by.kursovaa.email.service.Imap;
import by.kursovaa.email.service.Smtp;
import by.kursovaa.email.service.Synchronized;
import by.kursovaa.pojo.MessageInfo;
import by.kursovaa.entity.Email;
import by.kursovaa.interfaces.EmailServiceRemote;
import by.kursovaa.pojo.CountMessage;
import by.kursovaa.pojo.FileMeta;
import by.kursovaa.service.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.annotation.security.PermitAll;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Vladimir
 */
@PermitAll
@Stateless
public class EmailService implements EmailServiceRemote {

    private Imap imap = null;
    private String textError;
    private Thread threadGetMessages = null;
    private Synchronized synchroniz = null;
    private ArrayList<byte[]> attachments = null;

    @Override
    public String getTextError() {
        System.out.println("EmailService::getTextError STOK");

        if (synchroniz != null && !synchroniz.getTextError().isEmpty()) {
            return textError + "\n" + synchroniz.getTextError();
        }
        return textError;
    }

    @Override
    public void remove() {
        System.out.println("EmailService::remove ST");

        end();

        System.out.println("EmailService::remove OK");
    }

    @Remove(retainIfException = true)
    public void end() {
        System.out.println("EmailService::end STOK");

        threadGetMessages = null;
        synchroniz = null;
        imap = null;
    }

    @PreDestroy
    public void destroy() {
        System.out.println("EmailService::destroy STOK");

        threadGetMessages = null;
        synchroniz = null;
        imap = null;
    }

    @Override
    public void startSynchronization(Email accountInfo) {
        System.out.println("EmailService::startSynchronization ST");

        textError = "";
        try {
            imap = new Imap();

            synchroniz = new Synchronized();
            synchroniz.setAccountInfo(accountInfo);
            synchroniz.setImap(imap);
            threadGetMessages = new Thread(synchroniz);
            threadGetMessages.start();

        } catch (IllegalThreadStateException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::startSynchronization OK");
    }

    @Override
    public boolean isSynchronization() {
        System.out.println("EmailService::isSynchronization STOK");

        if (threadGetMessages != null) {
            return threadGetMessages.isAlive();
        }
        return false;
    }

    @Override
    public void stopSynchronization() {
        System.out.println("EmailService::stopSynchronization ST");

        try {
            if (threadGetMessages != null && threadGetMessages.isAlive()) {
                threadGetMessages.interrupt();
            }
            DataBaseEmail.close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::stopSynchronization OK");
    }

    @Override
    public ArrayList<MessageInfo> getMessageList(Email accountInfo, String folderName, int limit, int offset) {
        System.out.println("EmailService::getMessageList ST");

        ArrayList<MessageInfo> listMail = null;
        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();

        try {
            DataBaseEmail.connect(url);
            listMail = DataBaseEmail.getMessages(folderName, limit, offset);

            System.out.println("EmailService::getMessageList OK");
            return listMail;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::getMessageList OK null");
        return listMail;
    }

    @Override
    public void markFLAGGED(Email accountInfo, MessageInfo messageInfo) {
        System.out.println("EmailService::markFLAGGED ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            DataBaseEmail.connect(url);
            if (imap == null) {
                imap = new Imap();
            }
            imap.connectionImap(accountInfo);

            if (messageInfo.isSynchronize()) {
                imap.markMessage((int) messageInfo.getMsgId(), messageInfo.getDirectory(), !messageInfo.isMark());
                DataBaseEmail.updatedMarkMessage(messageInfo.getDirectory(), messageInfo.getMessageID(),
                        !messageInfo.isMark(), false);
            } else {
                DataBaseEmail.updatedMarkMessage(messageInfo.getDirectory(), messageInfo.getMessageID(), !messageInfo.isMark(), true);
            }
            imap.closeFolderSave();
        } catch (GeneralSecurityException | MessagingException | UnknownHostException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
            try {
                DataBaseEmail.updatedUnSynchronizedMark(messageInfo.getDirectory(), !messageInfo.isMark(), messageInfo.getMessageID());
            } catch (SQLException ex1) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex1);
                textError = ex.getMessage();
            }
        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } finally {

            try {
                imap.disconnectionImap();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
                textError = ex.getMessage();
            }

        }

        System.out.println("EmailService::markFLAGGED OK");
    }

    @Override
    public void markRead(Email accountInfo, MessageInfo messageInfo) {
        System.out.println("EmailService::markRead ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            DataBaseEmail.connect(url);
            if (imap == null) {
                imap = new Imap();
            }
            imap.connectionImap(accountInfo);

            if (messageInfo.isSynchronize()) {
                imap.readMessage((int) messageInfo.getMsgId(), messageInfo.getDirectory(), !messageInfo.isRead());
                DataBaseEmail.updatedReadMessage(messageInfo.getDirectory(), messageInfo.getMessageID(),
                        !messageInfo.isRead(), false);

            } else {
                DataBaseEmail.updatedReadMessage(messageInfo.getDirectory(), messageInfo.getMessageID(),
                        !messageInfo.isRead(), true);
            }
            imap.closeFolderSave();

        } catch (GeneralSecurityException | MessagingException | UnknownHostException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
            try {
                DataBaseEmail.updatedUnSynchronizedRead(messageInfo.getDirectory(), messageInfo.getMessageID());
            } catch (SQLException ex1) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex1);
                textError = ex.getMessage();
            }
        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } finally {

            try {
                imap.disconnectionImap();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
                textError = ex.getMessage();
            }

        }

        System.out.println("EmailService::markRead OK");
    }

    @Override
    public void deleteMessage(Email accountInfo, MessageInfo messageInfo) {
        System.out.println("EmailService::deleteMessage ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            DataBaseEmail.connect(url);
            if (imap == null) {
                imap = new Imap();
            }
            imap.connectionImap(accountInfo);

            if (messageInfo.isSynchronize()) {
                imap.deleteMessage((int) messageInfo.getMsgId(), messageInfo.getDirectory());
                DataBaseEmail.deleteMessage(messageInfo.getDirectory(), messageInfo.getMessageID());
            } else {
                DataBaseEmail.deleteMessageNeed(messageInfo.getDirectory(), messageInfo.getMessageID());
            }
            imap.closeFolderSave();

        } catch (GeneralSecurityException | MessagingException | UnknownHostException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();

            try {
                DataBaseEmail.deleteMessageNeed(messageInfo.getDirectory(), messageInfo.getMessageID());
            } catch (SQLException ex1) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex1);
                textError = ex.getMessage();
            }

        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } finally {

            try {
                imap.disconnectionImap();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
                textError = ex.getMessage();
            }

        }

        System.out.println("EmailService::deleteMessage OK");
    }

    @Override
    public void deleteMessageMultiple(Email accountInfo, ArrayList<MessageInfo> listMail) {
        System.out.println("EmailService::markReadMultiple ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            DataBaseEmail.connect(url);
            if (imap == null) {
                imap = new Imap();
            }
            imap.connectionImap(accountInfo);

            for (int i = 0; i < listMail.size(); i++) {
                MessageInfo messageInfo = listMail.get(i);
                if (messageInfo.isSynchronize()) {
                    imap.deleteMessage((int) messageInfo.getMsgId(), messageInfo.getDirectory());
                    DataBaseEmail.deleteMessage(messageInfo.getDirectory(), messageInfo.getMessageID());
                } else {
                    DataBaseEmail.deleteMessageNeed(messageInfo.getDirectory(), messageInfo.getMessageID());
                }
            }

            imap.closeFolderSave();

        } catch (GeneralSecurityException | MessagingException | UnknownHostException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();

            try {
                DataBaseEmail.deleteMessageNeed(listMail);
            } catch (SQLException ex1) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex1);
                textError = ex.getMessage();
            }

        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } finally {

            try {
                imap.disconnectionImap();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
                textError = ex.getMessage();
            }

        }

        System.out.println("EmailService::markReadMultiple OK");
    }

    @Override
    public void markReadMultiple(Email accountInfo, ArrayList<MessageInfo> listMail) {
        System.out.println("EmailService::deleteMessageMultiple ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            DataBaseEmail.connect(url);
            if (imap == null) {
                imap = new Imap();
            }
            imap.connectionImap(accountInfo);
            for (int i = 0; i < listMail.size(); i++) {
                MessageInfo messageInfo = listMail.get(i);
                if (messageInfo.isSynchronize()) {
                    imap.readMessage((int) messageInfo.getMsgId(), messageInfo.getDirectory(), !messageInfo.isRead());
                    DataBaseEmail.updatedReadMessage(messageInfo.getDirectory(), messageInfo.getMessageID(),
                            !messageInfo.isRead(), false);
                } else {
                    DataBaseEmail.updatedReadMessage(messageInfo.getDirectory(), messageInfo.getMessageID(),
                            !messageInfo.isRead(), true);
                }
            }
            imap.closeFolderSave();

        } catch (GeneralSecurityException | MessagingException | UnknownHostException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();

            try {
                DataBaseEmail.updatedUnSynchronizedRead(listMail);
            } catch (SQLException ex1) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex1);
                textError = ex.getMessage();
            }

        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } finally {

            try {
                imap.disconnectionImap();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
                textError = ex.getMessage();
            }

        }

        System.out.println("EmailService::deleteMessageMultiple OK");
    }

    @Override
    public CountMessage getMessageCout(Email accountInfo, String folderName) {
        System.out.println("EmailService::getMessageCout ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        CountMessage cout = new CountMessage();
        try {
            DataBaseEmail.connect(url);
            cout.setNewMess(DataBaseEmail.countUnRead("INBOX"));
            cout.setCout(DataBaseEmail.countRecord(folderName));
            cout.setSpammail(DataBaseEmail.countRecord("Junk"));
            cout.setDmail(DataBaseEmail.countRecord("Drafts"));

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::getMessageCout OK");
        return cout;
    }

    @Override
    public MessageInfo getMessageBody(Email accountInfo, MessageInfo messageInfo) {
        System.out.println("EmailService::getMessageBody ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin()
                + File.separator + messageInfo.getDirectory() + File.separator + messageInfo.getMessageID() + ".eml";
        textError = "";
        try {
            MessageInfo message = parseMessage(url, messageInfo);

            System.out.println("EmailService::getMessageBody OK");
            return message;
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::getMessageBody OK null");
        return null;
    }

    @Override
    public byte[] downloadAttachment(String path, String fileName) {
        System.out.println("EmailService::downloadAttachment ST");
// TODO REFRACTORING
        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + path;
        textError = "";

        byte[] bArray = null;
        InputStream x = null;

        try {
            Properties props = new Properties();
            Session session = Session.getInstance(props);

            FileInputStream file = new FileInputStream(url);
            MimeMessage message = new MimeMessage(session, file);
            String contentType = message.getContentType();
            if (contentType.contains("multipart")) {
                // content may contain attachments
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        // this part is attachment
                        String fileName_ = MimeUtility.decodeText(part.getFileName());
                        if (fileName_ == null ? fileName == null : fileName_.equals(fileName)) {

                            x = part.getInputStream();
                            // Construct the required byte array
                            int i = 0;
                            bArray = new byte[x.available()];
                            while ((i = (int) ((InputStream) x).available()) > 0) {
                                int result = (int) (((InputStream) x).read(bArray));
                                if (result == -1) {
                                    break;
                                }
                            }
                            x.close();
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (MessagingException | IOException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::downloadAttachment OK");
        return bArray;
    }

    @Override
    public void sentMessage(Email accountInfo, MessageInfo messageInfo) {
        System.out.println("EmailService::sentMessage ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            Smtp smtp = new Smtp();
            smtp.connectionSmtp(accountInfo);

            if (attachments != null && attachments.size() == 0) {
                for (int i = 0; i < attachments.size(); i++) {
                    messageInfo.getAttachments().get(i).setBytes(attachments.get(i));
                }
                attachments.clear();
            }

            smtp.createMessage(accountInfo, messageInfo);

            // отправим сообщение
            smtp.send();

            // сохраним сообщение
            MimeMessage message = smtp.getMessage();

            if (imap == null) {
                imap = new Imap();
            }

            imap.connectionImap(accountInfo);
            imap.saveMessage(message, "INBOX.Sent");
            imap.closeFolderSave();
            imap.disconnectionImap();

            messageInfo.setMessageID(message.getContentID());
            messageInfo.setRead(true);
            messageInfo.setDirectory("Sent");

            saveFileMessage(url, message, messageInfo);

            DataBaseEmail.connect(url);
            DataBaseEmail.createTable("Sent");
            DataBaseEmail.insert("Sent", messageInfo);

        } catch (AddressException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (MessagingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (GeneralSecurityException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::sentMessage OK");
    }

    @Override
    public void saveDraft(Email accountInfo, MessageInfo messageInfo) {
        System.out.println("EmailService::saveDraft ST");

        String url = Path.getInterface().getPath() + "mail_temp" + File.separator + accountInfo.getLogin();
        textError = "";
        try {
            if (imap == null) {
                imap = new Imap();
            }
            imap.connectionImap(accountInfo);

            Smtp smtp = new Smtp();

            if (attachments != null && attachments.size() == 0) {
                for (int i = 0; i < attachments.size(); i++) {
                    messageInfo.getAttachments().get(i).setBytes(attachments.get(i));
                }
                attachments.clear();
            }

            smtp.createMessage(accountInfo, messageInfo);

            MimeMessage message = smtp.getMessage();
            message.setFlag(Flags.Flag.DRAFT, true);

            imap.saveMessage(message, "INBOX.Drafts");
            imap.closeFolderSave();
            imap.disconnectionImap();

            messageInfo.setMessageID(message.getContentID());
            messageInfo.setRead(true);
            messageInfo.setDirectory("Drafts");

            saveFileMessage(url, message, messageInfo);

            DataBaseEmail.connect(url);
            DataBaseEmail.createTable("Drafts");
            DataBaseEmail.insert("Drafts", messageInfo);

        } catch (GeneralSecurityException | MessagingException | IOException | NullPointerException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            textError = ex.getMessage();
        }

        System.out.println("EmailService::saveDraft OK");
    }

    @Override
    public boolean hasNewMessage(by.kursovaa.entity.Email accountInfo) {
        System.out.println("EmailService::hasNewMessage ST");

        textError = "";
        boolean newMess = false;
//        try {
//            Store store = connectionImap(accountInfo);
//            Folder emailFolder = store.getFolder("INBOX");
//            emailFolder.open(Folder.READ_WRITE);
//            newMess = emailFolder.hasNewMessages();
//            emailFolder.close(false);
//        } catch (GeneralSecurityException | MessagingException | UnknownHostException | NullPointerException ex) {
//            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
//            textError = ex.getMessage();
//        }

        System.out.println("EmailService::hasNewMessage OK");
        return newMess;
    }

    @Override
    public String convertByteCount(long bytes, boolean si) {
        System.out.println("EmailService::convertByteCount ST");

        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            System.out.println("EmailService::convertByteCount OK bytes");
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        System.out.println("EmailService::convertByteCount OK");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public void setFile(byte[] bytes) {
        System.out.println("EmailService::setFile ST");

        if (attachments == null) {
            attachments = new ArrayList<byte[]>();
        }
        attachments.add(bytes);

        System.out.println("EmailService::setFile ST");
    }

    private MessageInfo parseMessage(String url, MessageInfo messageInfo)
            throws MessagingException, IOException {

        System.out.println("EmailService::parseMessage ST");
// TODO REFRACTORING
        Properties props = new Properties();
        Session session = Session.getInstance(props);

        FileInputStream file = new FileInputStream(url);
        MimeMessage message = new MimeMessage(session, file);
        String messageContent = "";
        String contentType = message.getContentType();
        if (contentType.contains("multipart")) {
            // content may contain attachments
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();

            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);

                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    // this part is attachment
                    FileMeta fileMeta = new FileMeta();

                    String fileSize = convertByteCount(part.getSize(), false);
                    fileMeta.setFileSize(fileSize);

                    String fileName = part.getFileName();
                    fileMeta.setFileName(MimeUtility.decodeText(fileName));

                    messageInfo.addAttachments(fileMeta);
                } else {
                    // this part may be the message content
                    // TODO здесь может быть html
                    messageContent = part.getContent().toString();

                    while (true) {
                        if (messageContent.indexOf("\n") != -1) {
                            messageInfo.addContent(messageContent.substring(0, messageContent.indexOf("\n")));
                            messageContent = messageContent.substring(messageContent.indexOf("\n") + 1,
                                    messageContent.length());
                        } else {
                            messageInfo.addContent(messageContent);
                            break;
                        }
                    }
                }
            }
        } else if (contentType.contains("text/plain")) {
            Object content = message.getContent();
            if (content != null) {
                messageContent = content.toString();
                while (true) {
                    if (messageContent.indexOf("\n") != -1) {
                        messageInfo.addContent(messageContent.substring(0, messageContent.indexOf("\n")));
                        messageContent = messageContent.substring(messageContent.indexOf("\n") + 1,
                                messageContent.length());
                    } else {
                        messageInfo.addContent(messageContent);
                        break;
                    }
                }
            }
        } else if (contentType.contains("text/html")) {
            Object content = message.getContent();
            if (content != null) {
                messageContent = content.toString();
                // TODO здесь может быть <br\>
                while (true) {
                    if (messageContent.indexOf("<br>") != -1) {
                        messageInfo.addContent(messageContent.substring(0, messageContent.indexOf("<br>")));
                        messageContent = messageContent.substring(messageContent.indexOf("<br>") + 4,
                                messageContent.length());
                    } else {
                        messageInfo.addContent(messageContent);
                        break;
                    }
                }
            }
        }

        System.out.println("EmailService::parseMessage OK");
        return messageInfo;
    }

    private void saveFileMessage(String url, Message messages, MessageInfo messageInfo)
            throws MessagingException, FileNotFoundException, IOException {

        System.out.println("EmailService::saveFileMessage ST");

        String nameFile = messageInfo.getDirectory() + File.separator + messageInfo.getMessageID() + ".eml";

        File subDir = new File(url + File.separator + nameFile);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }

        System.out.println(">>>>" + url + File.separator + nameFile);

        messages.writeTo(new FileOutputStream(new File(url + File.separator + nameFile)));

        System.out.println("EmailService::saveFileMessage OK");
    }
}
