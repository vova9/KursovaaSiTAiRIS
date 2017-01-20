/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.interfaces;

import by.kursovaa.pojo.CountMessage;
import by.kursovaa.pojo.MessageInfo;
import by.kursovaa.entity.Email;
import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface EmailServiceRemote {

    String getTextError();

    void remove();

    void startSynchronization(Email accountInfo);

    void stopSynchronization();

    boolean isSynchronization();

    ArrayList<MessageInfo> getMessageList(Email accountInfo, String folderName, int limit, int offset);

    void markRead(Email accountInfo, MessageInfo messageInfo);

    void markReadMultiple(Email accountInfo, ArrayList<MessageInfo> listMail);

    void deleteMessageMultiple(Email accountInfo, ArrayList<MessageInfo> listMail);

    void deleteMessage(Email accountInfo, MessageInfo messageInfo);

    void markFLAGGED(Email accountInfo, MessageInfo messageInfo);

    CountMessage getMessageCout(Email accountInfo, String folderName);

    MessageInfo getMessageBody(Email accountInfo, MessageInfo messageInfo);

    byte[] downloadAttachment(String url, String fileName);

    void saveDraft(Email accountInfo, MessageInfo Email);

    void sentMessage(Email accountInfo, MessageInfo messageInfo);

    void setFile(byte[] bytes);

    String convertByteCount(long bytes, boolean si);

    //////////////////////////////////////////////////////
    boolean hasNewMessage(Email accountInfo);
}
