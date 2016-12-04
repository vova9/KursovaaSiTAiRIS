/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.listener;

import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;

/**
 *
 * @author Vladimir
 */
public class MyFolderListener implements FolderListener {

    @Override
    public void folderCreated(FolderEvent e) {
        System.err.println("MyFolderListener::folderCreated STOK");
    }

    @Override
    public void folderDeleted(FolderEvent e) {
        System.err.println("MyFolderListener::folderDeleted STOK");

    }

    @Override
    public void folderRenamed(FolderEvent e) {
        System.err.println("MyFolderListener::folderRenamed STOK");
    }

}
