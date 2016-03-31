/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import by.kursovaa.logicAvtushkoVM.EMailRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.EmailFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KassaFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KategoriiFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KlientyFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.PolzovateliFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.TovaryFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazInfoFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazStatusFacadeRemote;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazyFacadeRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Vladimir
 */
public class Ejb {

    public static Ejb getInterface() {
        return new Ejb();
    }

    public EMailRemote lookupEMailRemote() {
        try {
            Context c = new InitialContext();
            return (EMailRemote) c.lookup("java:global/DoktorOnix-ejb/EMail!by.kursovaa.logicAvtushkoVM.EMailRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public EmailFacadeRemote lookupEmailFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (EmailFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/EmailFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.EmailFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KassaFacadeRemote lookupKassaFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KassaFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KassaFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KassaFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KategoriiFacadeRemote lookupKategoriiFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KategoriiFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KategoriiFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KategoriiFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KlientyFacadeRemote lookupKlientyFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KlientyFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KlientyFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KlientyFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public PolzovateliFacadeRemote lookupPolzovateliFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (PolzovateliFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/PolzovateliFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.PolzovateliFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public TovaryFacadeRemote lookupTovaryFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (TovaryFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/TovaryFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.TovaryFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazInfoFacadeRemote lookupZakazInfoFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazInfoFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazInfoFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazInfoFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazStatusFacadeRemote lookupZakazStatusFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazStatusFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazStatusFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazStatusFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazyFacadeRemote lookupZakazyFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazyFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazyFacade!by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazyFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
