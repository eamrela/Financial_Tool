/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "subcontractors")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subcontractors.findAll", query = "SELECT s FROM Subcontractors s")
    , @NamedQuery(name = "Subcontractors.findBySubcontractorName", query = "SELECT s FROM Subcontractors s WHERE s.subcontractorName = :subcontractorName")})
public class Subcontractors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "subcontractor_name")
    private String subcontractorName;

    public Subcontractors() {
    }

    public Subcontractors(String subcontractorName) {
        this.subcontractorName = subcontractorName;
    }

    public String getSubcontractorName() {
        return subcontractorName;
    }

    public void setSubcontractorName(String subcontractorName) {
        this.subcontractorName = subcontractorName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subcontractorName != null ? subcontractorName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subcontractors)) {
            return false;
        }
        Subcontractors other = (Subcontractors) object;
        if ((this.subcontractorName == null && other.subcontractorName != null) || (this.subcontractorName != null && !this.subcontractorName.equals(other.subcontractorName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.Subcontractors[ subcontractorName=" + subcontractorName + " ]";
    }
    
}
