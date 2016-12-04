/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.listener;

import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;

/**
 *
 * @author Vladimir
 */
public class MyMessageChangedListener implements MessageChangedListener {

    @Override
    public void messageChanged(MessageChangedEvent e) {
        System.err.println("MyConnectionListener::opened STOK");
    }

}
