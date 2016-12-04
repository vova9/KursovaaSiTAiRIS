/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.jpa;

import by.kursovaa.entity.ZakazStatus;
import by.kursovaa.interfaces.ZakazStatusFacadeRemote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vladimir
 */
@Stateless
public class ZakazStatusFacade extends AbstractFacade<ZakazStatus> implements ZakazStatusFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZakazStatusFacade() {
        super(ZakazStatus.class);
    }

}
