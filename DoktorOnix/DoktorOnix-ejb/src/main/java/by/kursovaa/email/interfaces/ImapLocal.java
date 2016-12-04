/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.interfaces;

import by.kursovaa.entity.Email;
import by.kursovaa.pojo.MessageInfo;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Vladimir
 */
public interface ImapLocal {

    public void connectionImap(Email accountInfo)
            throws GeneralSecurityException, NoSuchProviderException, MessagingException,
            UnknownHostException, NullPointerException;

    public String getMessageID(MimeMessage message) throws MessagingException;

    public MessageInfo getMessageInfo(MimeMessage message, MessageInfo messageInfo)
            throws MessagingException, UnsupportedEncodingException;

    public boolean getFlag(MessageInfo messageBean, Message message) throws MessagingException;

    public Folder[] getFolders() throws MessagingException;

    public void markMessage(int msgId, String folderName, boolean mark) throws MessagingException;

    public void readMessage(int msgId, String folderName, boolean mark) throws MessagingException;

    public void deleteMessage(int msgId, String folderName) throws MessagingException;

    public void save() throws MessagingException;

    public void close() throws MessagingException;
}
