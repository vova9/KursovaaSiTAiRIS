/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.interfaces;

import by.kursovaa.email.service.Imap;
import by.kursovaa.entity.Email;
import by.kursovaa.pojo.MessageInfo;
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 *
 * @author Vladimir.Avtushko
 */
public interface SynchronizedLocal {

    void saveMessage(String messageID, String folderName, Message messages, MessageInfo messageInfo)
            throws IOException, MessagingException;

    void setAccountInfo(Email accountInfo);

    public void setImap(Imap imap);

    public String getTextError();
}
