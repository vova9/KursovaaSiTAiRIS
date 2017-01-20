/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.interfaces;

import by.kursovaa.entity.Email;
import by.kursovaa.pojo.FileMeta;
import by.kursovaa.pojo.MessageInfo;
import java.io.IOException;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Vladimir.Avtushko
 */
public interface SmtpLocal {

    void createMessage(Email accountInfo, MessageInfo messageInfo)
            throws MessagingException, IOException;

    void connectionSmtp(Email accountInfo);

    void send() throws MessagingException;

    MimeMessage getMessage();
}
