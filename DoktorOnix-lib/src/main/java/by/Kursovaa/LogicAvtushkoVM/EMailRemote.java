/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM;

import java.io.IOException;
import java.util.LinkedList;
import javax.ejb.Remote;
import javax.mail.MessagingException;

/**
 *
 * @author Vladimir
 */
@Remote
public interface EMailRemote {

    void delete(String user, String password, int n, String host) throws MessagingException, IOException;

    LinkedList<MessageBean> receive(String user, String password, String host);

}
