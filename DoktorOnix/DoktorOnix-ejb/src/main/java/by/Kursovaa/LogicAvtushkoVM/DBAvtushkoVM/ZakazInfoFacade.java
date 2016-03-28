/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM;

import by.Kursovaa.DBAvtushkoVM.ZakazInfo;
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
public class ZakazInfoFacade extends AbstractFacade<ZakazInfo> implements by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.ZakazInfoFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZakazInfoFacade() {
        super(ZakazInfo.class);
    }
}
