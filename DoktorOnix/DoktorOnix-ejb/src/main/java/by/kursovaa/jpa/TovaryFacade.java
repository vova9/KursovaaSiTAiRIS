/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.jpa;

import by.kursovaa.entity.Tovary;
import by.kursovaa.interfaces.TovaryFacadeRemote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vladimir
 */
@Stateless
public class TovaryFacade extends AbstractFacade<Tovary> implements TovaryFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TovaryFacade() {
        super(Tovary.class);
    }
}
