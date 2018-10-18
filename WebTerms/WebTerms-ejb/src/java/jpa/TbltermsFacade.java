/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vlad
 */
@Stateless
public class TbltermsFacade extends AbstractFacade<Tblterms> implements TbltermsFacadeLocal {

    @PersistenceContext(unitName = "WebTerms-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TbltermsFacade() {
        super(Tblterms.class);
    }

    @Override
    public Tblterms findByTerm(Object term) {
        List<Tblterms> list = em.createNamedQuery("Tblterms.findByTerm")
                .setParameter("term", term)
                .getResultList();
        return !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public void deleteByTerm(Object term) {
        em.createNamedQuery("Tblterms.deleteByTerm")
                .setParameter("term", term).executeUpdate();
    }

}
