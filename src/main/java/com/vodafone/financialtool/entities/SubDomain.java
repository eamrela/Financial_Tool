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
@Table(name = "sub_domain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubDomain.findAll", query = "SELECT s FROM SubDomain s")
    , @NamedQuery(name = "SubDomain.findBySubdomainName", query = "SELECT s FROM SubDomain s WHERE s.subdomainName = :subdomainName")})
public class SubDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "subdomain_name")
    private String subdomainName;

    public SubDomain() {
    }

    public SubDomain(String subdomainName) {
        this.subdomainName = subdomainName;
    }

    public String getSubdomainName() {
        return subdomainName;
    }

    public void setSubdomainName(String subdomainName) {
        this.subdomainName = subdomainName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subdomainName != null ? subdomainName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubDomain)) {
            return false;
        }
        SubDomain other = (SubDomain) object;
        if ((this.subdomainName == null && other.subdomainName != null) || (this.subdomainName != null && !this.subdomainName.equals(other.subdomainName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.SubDomain[ subdomainName=" + subdomainName + " ]";
    }
    
}
