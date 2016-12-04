/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.listener;

import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

/**
 *
 * @author Vladimir
 */
public class MyConnectionListener implements ConnectionListener, TransportListener {

    @Override
    public void opened(ConnectionEvent e) {
        System.err.println("MyConnectionListener::opened STOK");
    }

    @Override
    public void disconnected(ConnectionEvent e) {
        System.err.println("MyConnectionListener::disconnected STOK");
    }

    @Override
    public void closed(ConnectionEvent e) {
        System.err.println("MyConnectionListener::closed STOK");
    }

    @Override
    public void messageDelivered(TransportEvent e) {
        System.err.println("MyConnectionListener::messageDelivered STOK");
    }

    @Override
    public void messageNotDelivered(TransportEvent e) {
        System.err.println("MyConnectionListener::messageNotDelivered STOK");
    }

    @Override
    public void messagePartiallyDelivered(TransportEvent e) {
        System.err.println("MyConnectionListener::messagePartiallyDelivered STOK");
    }
}
