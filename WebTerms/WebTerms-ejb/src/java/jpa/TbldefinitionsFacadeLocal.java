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
public interface TbldefinitionsFacadeLocal {

    void create(Tbldefinitions tbldefinitions);

    void edit(Tbldefinitions tbldefinitions);

    void remove(Tbldefinitions tbldefinitions);

    Tbldefinitions find(Object id);

    List<Tbldefinitions> findAll();

    List<Tbldefinitions> findRange(int[] range);

    int count();
    
}
