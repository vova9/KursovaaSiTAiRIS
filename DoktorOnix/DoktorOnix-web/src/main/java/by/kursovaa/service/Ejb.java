/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.service;

import by.kursovaa.interfaces.EmailServiceRemote;
import by.kursovaa.interfaces.SeviceRemote;
import by.kursovaa.interfaces.EmailFacadeRemote;
import by.kursovaa.interfaces.KassaFacadeRemote;
import by.kursovaa.interfaces.KategoriiFacadeRemote;
import by.kursovaa.interfaces.KlientyFacadeRemote;
import by.kursovaa.interfaces.PolzovateliFacadeRemote;
import by.kursovaa.interfaces.TovaryFacadeRemote;
import by.kursovaa.interfaces.ZakazInfoFacadeRemote;
import by.kursovaa.interfaces.ZakazStatusFacadeRemote;
import by.kursovaa.interfaces.ZakazyFacadeRemote;
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

    public EmailFacadeRemote lookupEmailFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (EmailFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/EmailFacade!by.kursovaa.interfaces.EmailFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KassaFacadeRemote lookupKassaFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KassaFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KassaFacade!by.kursovaa.interfaces.KassaFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KategoriiFacadeRemote lookupKategoriiFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KategoriiFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KategoriiFacade!by.kursovaa.interfaces.KategoriiFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KlientyFacadeRemote lookupKlientyFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KlientyFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KlientyFacade!by.kursovaa.interfaces.KlientyFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public PolzovateliFacadeRemote lookupPolzovateliFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (PolzovateliFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/PolzovateliFacade!by.kursovaa.interfaces.PolzovateliFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public TovaryFacadeRemote lookupTovaryFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (TovaryFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/TovaryFacade!by.kursovaa.interfaces.TovaryFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazInfoFacadeRemote lookupZakazInfoFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazInfoFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazInfoFacade!by.kursovaa.interfaces.ZakazInfoFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazStatusFacadeRemote lookupZakazStatusFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazStatusFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazStatusFacade!by.kursovaa.interfaces.ZakazStatusFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazyFacadeRemote lookupZakazyFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazyFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazyFacade!by.kursovaa.interfaces.ZakazyFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public SeviceRemote lookupSeviceRemote() {
        try {
            Context c = new InitialContext();
            return (SeviceRemote) c.lookup("java:global/DoktorOnix-ejb/Sevice!by.kursovaa.interfaces.SeviceRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public EmailServiceRemote lookupEmailServiceRemote() {
        try {
            Context c = new InitialContext();
            return (EmailServiceRemote) c.lookup("java:global/DoktorOnix-ejb/EmailService!by.kursovaa.interfaces.EmailServiceRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
