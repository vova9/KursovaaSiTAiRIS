/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.jpa;

import by.kursovaa.interfaces.KassaFacadeRemote;
import by.kursovaa.entity.Kassa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Vladimir
 */
@Stateless
public class KassaFacade extends AbstractFacade<Kassa> implements KassaFacadeRemote {

    @PersistenceContext(unitName = "DoktorOnix-ejb.PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KassaFacade() {
        super(Kassa.class);
    }

    @Override
    public Double summaAll() {
        Query query = em.createQuery("SELECT SUM(k.summa) FROM Kassa k");
        return (Double) query.getSingleResult();
    }

    @Override
    public Double profitAll() {
        Query query = em.createQuery("SELECT SUM(k.summa) FROM Kassa k WHERE k.tip = 'Приход'");
        return (Double) query.getSingleResult();
    }
}
