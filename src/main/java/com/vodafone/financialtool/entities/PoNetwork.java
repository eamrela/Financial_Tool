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
@Table(name = "po_network")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoNetwork.findAll", query = "SELECT p FROM PoNetwork p")
    , @NamedQuery(name = "PoNetwork.findByNetworkName", query = "SELECT p FROM PoNetwork p WHERE p.networkName = :networkName")
    , @NamedQuery(name = "PoNetwork.findByIncluded", query = "SELECT p FROM PoNetwork p WHERE p.included = :included")})
public class PoNetwork implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "network_name")
    private String networkName;
    @Column(name = "included")
    private Boolean included;

    public PoNetwork() {
    }

    public PoNetwork(String networkName) {
        this.networkName = networkName;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public Boolean getIncluded() {
        return included;
    }

    public void setIncluded(Boolean included) {
        this.included = included;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (networkName != null ? networkName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoNetwork)) {
            return false;
        }
        PoNetwork other = (PoNetwork) object;
        if ((this.networkName == null && other.networkName != null) || (this.networkName != null && !this.networkName.equals(other.networkName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.etisalat.dummy_ft.PoNetwork[ networkName=" + networkName + " ]";
    }
    
}
