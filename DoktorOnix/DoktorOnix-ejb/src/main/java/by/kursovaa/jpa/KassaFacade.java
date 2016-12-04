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
        Double summaAll = 0.0;
        Query query = em.createQuery("SELECT SUM(k.summa) FROM Kassa k");
        summaAll = (Double) query.getSingleResult();
        return summaAll;
    }

    @Override
    public Double profitAll() {
        Double profitAll = 0.0;
        Query query = em.createQuery("SELECT SUM(k.summa) FROM Kassa k WHERE k.tip = 'Приход'");
        profitAll = (Double) query.getSingleResult();
        return profitAll;
    }
}
