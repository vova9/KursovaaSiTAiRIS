/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.jpa;

import by.kursovaa.interfaces.PolzovateliFacadeRemote;
import by.kursovaa.entity.Polzovateli;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Vladimir
 */
@Stateless
public class PolzovateliFacade extends AbstractFacade<Polzovateli> implements PolzovateliFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PolzovateliFacade() {
        super(Polzovateli.class);
    }

    @Override
    public Polzovateli findByUserName(String username) {
        Query query = em.createNamedQuery("Polzovateli.findByLogin");
        query.setParameter("login", username);
        Polzovateli user = (Polzovateli) query.getSingleResult();
        return user;
    }
}
