/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Vlad
 */
@Local
public interface TbltermsFacadeLocal {

    void create(Tblterms tblterms);

    void edit(Tblterms tblterms);

    void remove(Tblterms tblterms);

    Tblterms find(Object id);
    
    Tblterms findByTerm(Object term);

    List<Tblterms> findAll();

    List<Tblterms> findRange(int[] range);

    int count();
    
}
