/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Email;
import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface EmailRemote {

    ArrayList<MessageBean> fetch(Email mail, String what);

    void sent(Email mail, MessageBean mess);

    void delete(Email mail, String what, Integer mess);

    boolean newMess(Email mail);

    CoutMessage chek(Email mail);

    void messegeRead(Email mail, MessageBean mess, String what);

    public void draft(MessageBean Email, Email idEmailSluzebny);
}
