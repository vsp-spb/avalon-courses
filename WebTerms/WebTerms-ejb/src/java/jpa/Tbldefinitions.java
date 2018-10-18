/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Vlad
 */
@Entity
@Table(name = "TBLDEFINITIONS", catalog = "", schema = "DEMO")
@NamedQueries({
    @NamedQuery(name = "Tbldefinitions.findAll", query = "SELECT t FROM Tbldefinitions t")
    , @NamedQuery(name = "Tbldefinitions.findById", query = "SELECT t FROM Tbldefinitions t WHERE t.id = :id")
    , @NamedQuery(name = "Tbldefinitions.findByDefinition", query = "SELECT t FROM Tbldefinitions t WHERE t.definition = :definition")})
public class Tbldefinitions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="definitionSeq", initialValue=7, allocationSize=1, sequenceName = "definition_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "definitionSeq")
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "DEFINITION")
    private String definition;

    @JoinColumn(name = "TERM_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Tblterms termId;

    public Tbldefinitions() {
    }

    public Tbldefinitions(Integer id) {
        this.id = id;
    }

    public Tbldefinitions(Integer id, String definition) {
        this.id = id;
        this.definition = definition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Tblterms getTermId() {
        return termId;
    }

    public void setTermId(Tblterms termId) {
        this.termId = termId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tbldefinitions)) {
            return false;
        }
        Tbldefinitions other = (Tbldefinitions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Tbldefinitions[ id=" + id + " ]";
    }
}
