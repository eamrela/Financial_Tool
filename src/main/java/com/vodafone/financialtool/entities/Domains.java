/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "domains")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domains.findAll", query = "SELECT d FROM Domains d")
    , @NamedQuery(name = "Domains.findByDomainName", query = "SELECT d FROM Domains d WHERE d.domainName = :domainName")
    , @NamedQuery(name = "Domains.findByDomainOwner", query = "SELECT d FROM Domains d WHERE d.domainOwner = :domainOwner")})
public class Domains implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "domain_name")
    private String domainName;
    @Size(max = 2147483647)
    @Column(name = "domain_owner")
    private String domainOwner;
    @JoinTable(name = "users_j_domains", joinColumns = {
        @JoinColumn(name = "domain_name", referencedColumnName = "domain_name")}, inverseJoinColumns = {
        @JoinColumn(name = "username", referencedColumnName = "user_name")})
    @ManyToMany
    private Collection<Users> usersCollection;

    public Domains() {
    }

    public Domains(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainOwner() {
        return domainOwner;
    }

    public void setDomainOwner(String domainOwner) {
        this.domainOwner = domainOwner;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (domainName != null ? domainName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domains)) {
            return false;
        }
        Domains other = (Domains) object;
        if ((this.domainName == null && other.domainName != null) || (this.domainName != null && !this.domainName.equals(other.domainName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.Domains[ domainName=" + domainName + " ]";
    }
    
}
