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
@Table(name = "customer_po_chasers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerPoChasers.findAll", query = "SELECT c FROM CustomerPoChasers c")
    , @NamedQuery(name = "CustomerPoChasers.findByChaserName", query = "SELECT c FROM CustomerPoChasers c WHERE c.chaserName = :chaserName")})
public class CustomerPoChasers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "chaser_name")
    private String chaserName;

    public CustomerPoChasers() {
    }

    public CustomerPoChasers(String chaserName) {
        this.chaserName = chaserName;
    }

    public String getChaserName() {
        return chaserName;
    }

    public void setChaserName(String chaserName) {
        this.chaserName = chaserName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chaserName != null ? chaserName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerPoChasers)) {
            return false;
        }
        CustomerPoChasers other = (CustomerPoChasers) object;
        if ((this.chaserName == null && other.chaserName != null) || (this.chaserName != null && !this.chaserName.equals(other.chaserName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.CustomerPoChasers[ chaserName=" + chaserName + " ]";
    }
    
}
