/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM;

import by.Kursovaa.DBAvtushkoVM.Polzovateli;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vladimir
 */
@Stateless
public class PolzovateliFacade extends AbstractFacade<Polzovateli> implements by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM.PolzovateliFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PolzovateliFacade() {
        super(Polzovateli.class);
    }
}
