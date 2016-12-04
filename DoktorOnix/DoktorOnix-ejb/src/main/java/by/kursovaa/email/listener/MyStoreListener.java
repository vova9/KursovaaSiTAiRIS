/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.listener;

import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;

/**
 *
 * @author Vladimir
 */
public class MyStoreListener implements StoreListener {

    @Override
    public void notification(StoreEvent e) {
        System.err.println("MyStoreListener::notification STOK");
        System.err.println(e.getMessage());
    }

}
