/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM;

import by.Kursovaa.DBAvtushkoVM.Zakazy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vladimir
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class ZakazyFacade extends AbstractFacade<Zakazy> implements by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazyFacadeRemote {

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
