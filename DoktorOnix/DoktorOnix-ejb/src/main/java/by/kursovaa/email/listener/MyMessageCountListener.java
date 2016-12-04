/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.listener;

import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

/**
 *
 * @author Vladimir
 */
public class MyMessageCountListener implements MessageCountListener {

    @Override
    public void messagesAdded(MessageCountEvent e) {
        System.err.println("MyMessageCountListener::messagesAdded STOK");
    }

    @Override
    public void messagesRemoved(MessageCountEvent e) {
        System.err.println("MyMessageCountListener::messagesRemoved STOK");
    }

}
