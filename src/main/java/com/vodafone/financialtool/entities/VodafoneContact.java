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
@Table(name = "vodafone_contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VodafoneContact.findAll", query = "SELECT v FROM VodafoneContact v")
    , @NamedQuery(name = "VodafoneContact.findByContactName", query = "SELECT v FROM VodafoneContact v WHERE v.contactName = :contactName")})
public class VodafoneContact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "contact_name")
    private String contactName;

    public VodafoneContact() {
    }

    public VodafoneContact(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactName != null ? contactName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VodafoneContact)) {
            return false;
        }
        VodafoneContact other = (VodafoneContact) object;
        if ((this.contactName == null && other.contactName != null) || (this.contactName != null && !this.contactName.equals(other.contactName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.VodafoneContact[ contactName=" + contactName + " ]";
    }
    
}
