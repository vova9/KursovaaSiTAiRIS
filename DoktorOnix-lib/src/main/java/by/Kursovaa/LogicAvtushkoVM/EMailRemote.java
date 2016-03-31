/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Email;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface EMailRemote {

    void delete(String user, String password, int n, String host);

    LinkedList<MessageBean> receive(String what, Email mail);

    void send(String to, String content, String subject, ArrayList<String> attachments, Email mail);
}
