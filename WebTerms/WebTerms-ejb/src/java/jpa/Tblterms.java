/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Vlad
 */
@Entity
@Table(name = "TBLTERMS", catalog = "", schema = "DEMO")
@NamedQueries({
    @NamedQuery(name = "Tblterms.findAll", query = "SELECT t FROM Tblterms t")
    , @NamedQuery(name = "Tblterms.findById", query = "SELECT t FROM Tblterms t WHERE t.id = :id")
    , @NamedQuery(name = "Tblterms.findByTerm", query = "SELECT t FROM Tblterms t WHERE t.term = :term")
    , @NamedQuery(name = "Tblterms.deleteByTerm", query = "DELETE FROM Tblterms t WHERE t.term = :term")})
public class Tblterms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="termSeq", initialValue=4, allocationSize=1, sequenceName = "term_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "termSeq")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TERM")
    private String term;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termId")
    private List<Tbldefinitions> tbldefinitionsList;

    public Tblterms() {
    }

    public Tblterms(Integer id) {
        this.id = id;
    }

    public Tblterms(Integer id, String term) {
        this.id = id;
        this.term = term;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<Tbldefinitions> getTbldefinitionsList() {
        return tbldefinitionsList;
    }

    public void setTbldefinitionsList(List<Tbldefinitions> tbldefinitionsList) {
        this.tbldefinitionsList = tbldefinitionsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tblterms)) {
            return false;
        }
        Tblterms other = (Tblterms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Tblterms[ id=" + id + " ]";
    }
}
