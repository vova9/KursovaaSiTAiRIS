/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM.dbAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Zakazy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vladimir
 */
@Stateless
public class ZakazyFacade extends AbstractFacade<Zakazy> implements by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.ZakazyFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZakazyFacade() {
        super(Zakazy.class);
    }
    
}
