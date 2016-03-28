/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.controllerAvtushkoVM;

import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.KassaFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.KategoriiFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.KlientyFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.PolzovateliFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.TovaryFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazInfoFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazStatusFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazyFacadeRemote;
import by.Kursovaa.LogicAvtushkoVM.EMailRemote;
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

    public PolzovateliFacadeRemote lookupPolzovateliFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (PolzovateliFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/PolzovateliFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.PolzovateliFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KassaFacadeRemote lookupKassaFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KassaFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KassaFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.KassaFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KategoriiFacadeRemote lookupKategoriiFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KategoriiFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KategoriiFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.KategoriiFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KlientyFacadeRemote lookupKlientyFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (KlientyFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/KlientyFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.KlientyFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public TovaryFacadeRemote lookupTovaryFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (TovaryFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/TovaryFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.TovaryFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ZakazInfoFacadeRemote lookupZakazInfoFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazInfoFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazInfoFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazInfoFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazStatusFacadeRemote lookupZakazStatusFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazStatusFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazStatusFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazStatusFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ZakazyFacadeRemote lookupZakazyFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (ZakazyFacadeRemote) c.lookup("java:global/DoktorOnix-ejb/ZakazyFacade!by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazyFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public EMailRemote lookupEMailRemote() {
        try {
            Context c = new InitialContext();
            return (EMailRemote) c.lookup("java:global/DoktorOnix-ejb/EMail!by.Kursovaa.LogicAvtushkoVM.EMailRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public static Ejb getInterface() {
        return new Ejb();
    }
}
