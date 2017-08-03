/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "customer_extrawork_po")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerExtraworkPo.findAll", query = "SELECT c FROM CustomerExtraworkPo c")
    , @NamedQuery(name = "CustomerExtraworkPo.findByPoNumber", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.poNumber = :poNumber")
    , @NamedQuery(name = "CustomerExtraworkPo.findByPoDate", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.poDate = :poDate")
    , @NamedQuery(name = "CustomerExtraworkPo.findByDomainName", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.domainName = :domainName")
    , @NamedQuery(name = "CustomerExtraworkPo.findBySubDomain", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.subDomain = :subDomain")
    , @NamedQuery(name = "CustomerExtraworkPo.findByPoDescription", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.poDescription = :poDescription")
    , @NamedQuery(name = "CustomerExtraworkPo.findByFactor", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.factor = :factor")
    , @NamedQuery(name = "CustomerExtraworkPo.findByServiceValue", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.serviceValue = :serviceValue")
    , @NamedQuery(name = "CustomerExtraworkPo.findByPoValue", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.poValue = :poValue")
    , @NamedQuery(name = "CustomerExtraworkPo.findByTaxesValue", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.taxesValue = :taxesValue")
    , @NamedQuery(name = "CustomerExtraworkPo.findByCreationTime", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.creationTime = :creationTime")
    , @NamedQuery(name = "CustomerExtraworkPo.findByPoOwner", query = "SELECT c FROM CustomerExtraworkPo c WHERE c.poOwner = :poOwner")})
public class CustomerExtraworkPo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "po_number")
    private String poNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "po_date")
    @Temporal(TemporalType.DATE)
    private Date poDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "domain_name")
    private String domainName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "sub_domain")
    private String subDomain;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "po_description")
    private String poDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "factor")
    private Double factor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "service_value")
    private Double serviceValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "po_value")
    private Double poValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "remaining_from_po")
    private Double remainingFromPo;
    @Column(name = "taxes_value")
    private Double taxesValue;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "po_owner")
    private String poOwner;
    @JoinTable(name = "customer_extrawork_po_j_extrawork", joinColumns = {
        @JoinColumn(name = "customer_extrawork_po_id", referencedColumnName = "po_number")}, inverseJoinColumns = {
        @JoinColumn(name = "extrawork_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ExtraWork> extraWorkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poNumber")
    private Collection<CustomerExtraworkMd> customerExtraworkMdCollection;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne(optional = false)
    private Users creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poNumber")
    private Collection<CustomerExtraworkWorkDone> customerExtraworkWorkDoneCollection;
    @Transient
    private Double mdDeserved;
    @Transient
    private Double totalWorkDone;

    public CustomerExtraworkPo() {
    }

    public CustomerExtraworkPo(String poNumber) {
        this.poNumber = poNumber;
    }

    public CustomerExtraworkPo(String poNumber, Date poDate, String domainName, String subDomain, String poDescription, Double factor, Double serviceValue, Double poValue, String poOwner) {
        this.poNumber = poNumber;
        this.poDate = poDate;
        this.domainName = domainName;
        this.subDomain = subDomain;
        this.poDescription = poDescription;
        this.factor = factor;
        this.serviceValue = serviceValue;
        this.poValue = poValue;
        this.poOwner = poOwner;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Date getPoDate() {
        return poDate;
    }

    public Double getRemainingFromPo() {
        return remainingFromPo;
    }

    public void setRemainingFromPo(Double remainingFromPo) {
        this.remainingFromPo = remainingFromPo;
    }

    
    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getPoDescription() {
        return poDescription;
    }

    public void setPoDescription(String poDescription) {
        this.poDescription = poDescription;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Double getServiceValue() {
        return serviceValue;
    }

    public void setServiceValue(Double serviceValue) {
        this.serviceValue = serviceValue;
    }

    public Double getPoValue() {
        return poValue;
    }

    public void setPoValue(Double poValue) {
        this.poValue = poValue;
    }

    public Double getTaxesValue() {
        return taxesValue;
    }

    public void setTaxesValue(Double taxesValue) {
        this.taxesValue = taxesValue;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getPoOwner() {
        return poOwner;
    }

    public void setPoOwner(String poOwner) {
        this.poOwner = poOwner;
    }

    @XmlTransient
    public Collection<ExtraWork> getExtraWorkCollection() {
        return extraWorkCollection;
    }

    public void setExtraWorkCollection(Collection<ExtraWork> extraWorkCollection) {
        this.extraWorkCollection = extraWorkCollection;
    }

    @XmlTransient
    public Collection<CustomerExtraworkMd> getCustomerExtraworkMdCollection() {
        return customerExtraworkMdCollection;
    }

    public void setCustomerExtraworkMdCollection(Collection<CustomerExtraworkMd> customerExtraworkMdCollection) {
        this.customerExtraworkMdCollection = customerExtraworkMdCollection;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    @XmlTransient
    public Collection<CustomerExtraworkWorkDone> getCustomerExtraworkWorkDoneCollection() {
        return customerExtraworkWorkDoneCollection;
    }

    public void setCustomerExtraworkWorkDoneCollection(Collection<CustomerExtraworkWorkDone> customerExtraworkWorkDoneCollection) {
        this.customerExtraworkWorkDoneCollection = customerExtraworkWorkDoneCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poNumber != null ? poNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerExtraworkPo)) {
            return false;
        }
        CustomerExtraworkPo other = (CustomerExtraworkPo) object;
        if ((this.poNumber == null && other.poNumber != null) || (this.poNumber != null && !this.poNumber.equals(other.poNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.CustomerExtraworkPo[ poNumber=" + poNumber + " ]";
    }

    public Double getMdDeserved() {
        Double actualWorkDone = poValue-remainingFromPo;
        Double mdCreatedValue = 0.0;
        if(getCustomerExtraworkMdCollection()!=null){
            Object[] mds = getCustomerExtraworkMdCollection().toArray();
            for (Object md : mds) {
                mdCreatedValue += ((CustomerExtraworkMd)md).getMdValue();
            }
            mdDeserved = actualWorkDone-mdCreatedValue;
        }
        return mdDeserved;
    }

    public Double getTotalWorkDone() {
        totalWorkDone = 0.0;
        if(getCustomerExtraworkWorkDoneCollection()!=null){
            Object[] workDone = getCustomerExtraworkWorkDoneCollection().toArray();
            for (Object wd : workDone) {
                totalWorkDone += ((CustomerExtraworkWorkDone)wd).getWorkDoneValue();
            }
        }
        return totalWorkDone;
    }
    
    
}
