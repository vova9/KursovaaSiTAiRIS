/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.service;

import by.kursovaa.email.interfaces.ImapLocal;
import by.kursovaa.email.listener.MyConnectionListener;
import by.kursovaa.email.listener.MyFolderListener;
import by.kursovaa.email.listener.MyMessageChangedListener;
import by.kursovaa.email.listener.MyMessageCountListener;
import by.kursovaa.email.listener.MyStoreListener;
import by.kursovaa.pojo.MessageInfo;
import by.kursovaa.entity.Email;
import com.sun.mail.util.MailSSLSocketFactory;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Vladimir
 */
public class Imap implements ImapLocal {

    private Store store;
    private Folder emailFolder;

    @Override
    public void connectionImap(Email accountInfo)
            throws GeneralSecurityException, NoSuchProviderException, MessagingException,
            UnknownHostException, NullPointerException {

        System.out.println("Imap::connectionImap ST");

        MailSSLSocketFactory mailSSLSocket = new MailSSLSocketFactory();
        mailSSLSocket.setTrustAllHosts(true);

        Properties props = new Properties();
        props.put("mail.imaps.ssl.trust", "*");
        java.security.Security.setProperty("ssl.SocketFactory.provider", "by.kursovaa.emailService.MySSLSocketFactory");
        props.put("mail.imaps.ssl.socketFactory", mailSSLSocket);

        //включение debug-режима
//        props.put("mail.debug", "true");
        //Указываем протокол - IMAP с SSL
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);

        store = session.getStore();

        if (store == null) {
            return;
        }
        if (store.isConnected()) {
            return;
        }

        store.addConnectionListener(new MyConnectionListener());
        store.addFolderListener(new MyFolderListener());
        store.addStoreListener(new MyStoreListener());

        //подключаемся к почтовому серверу
        store.connect(accountInfo.getImap(), accountInfo.getLogin(), accountInfo.getPassword());

        System.out.println("Imap::connectionImap OK");
    }

    @Override
    public String getMessageID(MimeMessage message) throws MessagingException {
        System.out.println("Imap::getMessageID ST");

        String messageID = Arrays.toString(message.getHeader("Message-ID"));
        messageID = messageID.replace("<", "");
        messageID = messageID.replace(">", "");
        messageID = messageID.replace("/", "");
        messageID = messageID.replace("\\", "");
        messageID = messageID.replace("+", "");
        messageID = messageID.replace(":", "");
        messageID = messageID.replace("*", "");
        messageID = messageID.replace("«", "");
        messageID = messageID.replace("|", "");
        messageID = messageID.replace("?", "");

        System.out.println("Imap::getMessageID OK");
        return messageID;
    }

    @Override
    public MessageInfo getMessageInfo(MimeMessage message, MessageInfo messageInfo)
            throws MessagingException, UnsupportedEncodingException {

        System.out.println("Imap::getMessageInfo ST " + message.getMessageNumber());

        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        Address[] address;
        if ((address = message.getFrom()) != null) {
            messageInfo.setFrom(MimeUtility.decodeText(address[0].toString()));
        }

        if ((address = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int i = 0; i < address.length; i++) {
                messageInfo.addRecipientTo(MimeUtility.decodeText(address[i].toString()));
            }
        }

        if ((address = message.getRecipients(Message.RecipientType.CC)) != null) {
            for (int i = 0; i < address.length; i++) {
                messageInfo.addRecipientCc(MimeUtility.decodeText(address[i].toString()));
            }
        }

        if ((address = message.getRecipients(Message.RecipientType.BCC)) != null) {
            for (int i = 0; i < address.length; i++) {
                messageInfo.addRecipientBcc(MimeUtility.decodeText(address[i].toString()));
            }
        }

        if (message.getSubject() != null) {
            messageInfo.setSubject(message.getSubject());
        }

        if (message.getSentDate() != null) {
            messageInfo.setDateSent(f.format(message.getSentDate()));
        }

        messageInfo.setMsgId(message.getMessageNumber());
        messageInfo.setSize(message.getSize());

        if (message.isMimeType("multipart/*")) {
            messageInfo.setAttachment(true);
        }

        System.out.println("Imap::getMessageInfo OK");
        return messageInfo;
    }

    @Override
    public boolean getFlag(MessageInfo messageBean, Message message) throws MessagingException {
        System.out.println("Imap::getFlag ST");

        boolean hasUpdate = false;
        if (message.isSet(Flags.Flag.DELETED)) {
            System.out.println("Imap::getFlag  Deleted");
        }

        if (message.isSet(Flags.Flag.ANSWERED)) {
            System.out.println("Imap::getFlag Answered");
        }

        if (message.isSet(Flags.Flag.DRAFT)) {
            System.out.println("Imap::getFlag Draft");
        }

        if (message.isSet(Flags.Flag.FLAGGED)) {
            System.out.println("Imap::getFlag Marked");
            if (!messageBean.isMark()) {
                messageBean.setMark(true);
                hasUpdate = true;
            }
        } else if (messageBean.isMark()) {
            messageBean.setMark(false);
            hasUpdate = true;
        }

        if (message.isSet(Flags.Flag.RECENT)) {
            System.out.println("Imap::getFlag Recent");
        }

        if (message.isSet(Flags.Flag.SEEN)) {
            System.out.println("Imap::getFlag Read");
            if (!messageBean.isRead()) {
                messageBean.setRead(true);
                hasUpdate = true;
            }
        } else if (messageBean.isRead()) {
            messageBean.setRead(false);
            hasUpdate = true;
        }

        if (message.isSet(Flags.Flag.USER)) {
            // We don't know what the user flags might be in advance
            // so they're returned as an array of strings
            String[] userFlags = message.getFlags().getUserFlags();
            for (int j = 0; j < userFlags.length; j++) {
                System.out.println("User flag: " + userFlags[j]);
            }
        }

        System.out.println("Imap::getFlag OK");
        return hasUpdate;
    }

    @Override
    public Folder[] getFolders() throws MessagingException {
        System.out.println("Imap::getFolders ST");

        MyMessageCountListener messageCountListener = new MyMessageCountListener();
        MyMessageChangedListener messageChangedListener = new MyMessageChangedListener();

        Folder[] folders = store.getDefaultFolder().list("*");

        for (int i = 0; i < folders.length; i++) {
            folders[i].addMessageCountListener(messageCountListener);
            folders[i].addMessageChangedListener(messageChangedListener);
        }

        System.out.println("Imap::getFolders OK");
        return folders;
    }

    @Override
    public void markMessage(int msgId, String folderName, boolean mark) throws MessagingException {
        System.out.println("Imap::markMessage ST");

        if ("Inbox".equals(folderName)) {
            folderName = "INBOX";
        } else {
            folderName = "INBOX." + folderName;
        }

        emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_WRITE);
        Message message = emailFolder.getMessage(msgId);
        message.setFlag(Flags.Flag.FLAGGED, mark);
        System.out.println("Imap::markMessage OK");
    }

    @Override
    public void readMessage(int msgId, String folderName, boolean mark) throws MessagingException {
        System.out.println("Imap::markMessage ST");

        if ("Inbox".equals(folderName)) {
            folderName = "INBOX";
        } else {
            folderName = "INBOX." + folderName;
        }

        emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_WRITE);
        Message message = emailFolder.getMessage(msgId);
        message.setFlag(Flags.Flag.SEEN, mark);

        System.out.println("Imap::markMessage OK");
    }

    @Override
    public void deleteMessage(int msgId, String folderName) throws MessagingException {
        System.out.println("Imap::deleteMessage ST");

        if ("Inbox".equals(folderName)) {
            folderName = "INBOX";
        } else {
            folderName = "INBOX." + folderName;
        }

        emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_WRITE);
        Message message = emailFolder.getMessage(msgId);
        message.setFlag(Flags.Flag.DELETED, true);

        System.out.println("Imap::deleteMessage OK");
    }

    @Override
    public void closeFolderSave() throws MessagingException {
        System.out.println("Imap::save STOK");
        emailFolder.close(true);
    }

    @Override
    public void disconnectionImap() throws MessagingException {
        System.out.println("Imap::close STOK");
        store.close();
    }

    @Override
    public void saveMessage(MimeMessage message, String folderName) throws MessagingException {
        System.out.println("Imap::saveMessage ST");

        emailFolder = store.getFolder(folderName);
        emailFolder.open(Folder.READ_WRITE);
        emailFolder.appendMessages(new Message[]{message});

        System.out.println("Imap::saveMessage OK");
    }
}
