/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface EmailServiceRemote {

    ArrayList<MessageBean> fetch(by.kursovaa.dbAvtushkoVM.Email mail, String what);

    void sent(by.kursovaa.dbAvtushkoVM.Email mail, MessageBean mess);

    void delete(by.kursovaa.dbAvtushkoVM.Email mail, String what, Integer mess);

    boolean newMess(by.kursovaa.dbAvtushkoVM.Email mail);

    CoutMessage chek(by.kursovaa.dbAvtushkoVM.Email mail);

    void messegeRead(by.kursovaa.dbAvtushkoVM.Email mail, MessageBean mess, String what);

    public void draft(MessageBean Email, by.kursovaa.dbAvtushkoVM.Email idEmailSluzebny);
}
