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

    public String getTextError();

    public void remove();

    public void startSynchronization(Email accountInfo);

    public void stopSynchronization();

    public boolean isSynchronization();

    public ArrayList<MessageInfo> getMessageList(Email accountInfo, String folderName, int limit, int offset);

    public void markRead(Email accountInfo, MessageInfo messageInfo);

    public void markReadMultiple(Email accountInfo, ArrayList<MessageInfo> listMail);

    public void deleteMessageMultiple(Email accountInfo, ArrayList<MessageInfo> listMail);

    public void deleteMessage(Email accountInfo, MessageInfo messageInfo);

    public void markFLAGGED(Email accountInfo, MessageInfo messageInfo);

    public CountMessage getMessageCout(Email accountInfo, String folderName);

    public MessageInfo getMessageBody(Email accountInfo, MessageInfo messageInfo);

    public byte[] downloadAttachment(String url, String fileName);

    public void saveDraft(Email accountInfo, MessageInfo Email);

    public void sentMessage(Email accountInfo, MessageInfo messageInfo);

    public void setFile(byte[] bytes);

    public String convertByteCount(long bytes, boolean si);

    //////////////////////////////////////////////////////
    public boolean hasNewMessage(Email accountInfo);
}
