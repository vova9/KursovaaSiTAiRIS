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

    void connectionImap(Email accountInfo)
            throws GeneralSecurityException, NoSuchProviderException, MessagingException,
            UnknownHostException, NullPointerException;

    void disconnectionImap() throws MessagingException;
    
    String getMessageID(MimeMessage message) throws MessagingException;

    MessageInfo getMessageInfo(MimeMessage message, MessageInfo messageInfo)
            throws MessagingException, UnsupportedEncodingException;

    boolean getFlag(MessageInfo messageBean, Message message) throws MessagingException;

    Folder[] getFolders() throws MessagingException;

    void markMessage(int msgId, String folderName, boolean mark) throws MessagingException;

    void readMessage(int msgId, String folderName, boolean mark) throws MessagingException;

    void deleteMessage(int msgId, String folderName) throws MessagingException;
  
    void closeFolderSave() throws MessagingException;

    void saveMessage(MimeMessage message, String folderName) throws MessagingException;
}
