/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vlad
 */
@Stateless
public class TbldefinitionsFacade extends AbstractFacade<Tbldefinitions> implements TbldefinitionsFacadeLocal {

    @PersistenceContext(unitName = "WebTerms-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TbldefinitionsFacade() {
        super(Tbldefinitions.class);
    }
    
}
