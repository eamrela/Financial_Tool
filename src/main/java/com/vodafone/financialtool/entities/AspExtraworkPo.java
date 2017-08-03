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
@Table(name = "asp_extrawork_po")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspExtraworkPo.findAll", query = "SELECT a FROM AspExtraworkPo a")
    , @NamedQuery(name = "AspExtraworkPo.findByPoNumber", query = "SELECT a FROM AspExtraworkPo a WHERE a.poNumber = :poNumber")
    , @NamedQuery(name = "AspExtraworkPo.findByPoDate", query = "SELECT a FROM AspExtraworkPo a WHERE a.poDate = :poDate")
    , @NamedQuery(name = "AspExtraworkPo.findByDomainName", query = "SELECT a FROM AspExtraworkPo a WHERE a.domainName = :domainName")
    , @NamedQuery(name = "AspExtraworkPo.findBySubDomain", query = "SELECT a FROM AspExtraworkPo a WHERE a.subDomain = :subDomain")
    , @NamedQuery(name = "AspExtraworkPo.findBySubcontractor", query = "SELECT a FROM AspExtraworkPo a WHERE a.subcontractor = :subcontractor")
    , @NamedQuery(name = "AspExtraworkPo.findByPoDescription", query = "SELECT a FROM AspExtraworkPo a WHERE a.poDescription = :poDescription")
    , @NamedQuery(name = "AspExtraworkPo.findByFactor", query = "SELECT a FROM AspExtraworkPo a WHERE a.factor = :factor")
    , @NamedQuery(name = "AspExtraworkPo.findByServiceValue", query = "SELECT a FROM AspExtraworkPo a WHERE a.serviceValue = :serviceValue")
    , @NamedQuery(name = "AspExtraworkPo.findByPoValue", query = "SELECT a FROM AspExtraworkPo a WHERE a.poValue = :poValue")
    , @NamedQuery(name = "AspExtraworkPo.findByTaxesValue", query = "SELECT a FROM AspExtraworkPo a WHERE a.taxesValue = :taxesValue")
    , @NamedQuery(name = "AspExtraworkPo.findByCreationTime", query = "SELECT a FROM AspExtraworkPo a WHERE a.creationTime = :creationTime")
    , @NamedQuery(name = "AspExtraworkPo.findByPoOwner", query = "SELECT a FROM AspExtraworkPo a WHERE a.poOwner = :poOwner")})
public class AspExtraworkPo implements Serializable {

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
    @Column(name = "subcontractor")
    private String subcontractor;
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
    @JoinTable(name = "asp_extrawork_po_j_extrawork", joinColumns = {
        @JoinColumn(name = "asp_extrawork_po_id", referencedColumnName = "po_number")}, inverseJoinColumns = {
        @JoinColumn(name = "extrawork_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ExtraWork> extraWorkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poNumber")
    private Collection<AspExtraworkWorkDone> aspExtraworkWorkDoneCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poNumber")
    private Collection<AspExtraworkGrn> aspExtraworkGrnCollection;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne(optional = false)
    private Users creator;
    @Transient
    private Double grnDeserved;
    @Transient
    private Double totalWorkDone;

    public AspExtraworkPo() {
    }

    public AspExtraworkPo(String poNumber) {
        this.poNumber = poNumber;
    }

    public AspExtraworkPo(String poNumber, Date poDate, String domainName, String subDomain, String subcontractor, String poDescription, Double factor, Double serviceValue, Double poValue, String poOwner) {
        this.poNumber = poNumber;
        this.poDate = poDate;
        this.domainName = domainName;
        this.subDomain = subDomain;
        this.subcontractor = subcontractor;
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

    public Double getRemainingFromPo() {
        return remainingFromPo;
    }

    public void setRemainingFromPo(Double remainingFromPo) {
        this.remainingFromPo = remainingFromPo;
    }

    
    public Date getPoDate() {
        return poDate;
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

    public String getSubcontractor() {
        return subcontractor;
    }

    public void setSubcontractor(String subcontractor) {
        this.subcontractor = subcontractor;
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
    public Collection<AspExtraworkWorkDone> getAspExtraworkWorkDoneCollection() {
        return aspExtraworkWorkDoneCollection;
    }

    public void setAspExtraworkWorkDoneCollection(Collection<AspExtraworkWorkDone> aspExtraworkWorkDoneCollection) {
        this.aspExtraworkWorkDoneCollection = aspExtraworkWorkDoneCollection;
    }

    @XmlTransient
    public Collection<AspExtraworkGrn> getAspExtraworkGrnCollection() {
        return aspExtraworkGrnCollection;
    }

    @Transient
    public Double getGrnDeserved() {
        Double actualWorkDone = poValue-remainingFromPo;
        Double grnCreatedValue = 0.0;
        if(getAspExtraworkGrnCollection()!=null){
            Object[] grns = getAspExtraworkGrnCollection().toArray();
            for (Object grn : grns) {
                grnCreatedValue += ((AspExtraworkGrn)grn).getGrnValue();
            }
            grnDeserved = actualWorkDone-grnCreatedValue;
        }
        return grnDeserved;
    }

    @Transient
    public Double getTotalWorkDone() {
        totalWorkDone = 0.0;
        if(getAspExtraworkWorkDoneCollection()!=null){
            Object[] workDone = getAspExtraworkWorkDoneCollection().toArray();
            for (Object wd : workDone) {
                totalWorkDone += ((AspExtraworkWorkDone)wd).getWorkDoneValue();
            }
        }
        return totalWorkDone;
    }
    
    

    
    public void setAspExtraworkGrnCollection(Collection<AspExtraworkGrn> aspExtraworkGrnCollection) {
        this.aspExtraworkGrnCollection = aspExtraworkGrnCollection;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
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
        if (!(object instanceof AspExtraworkPo)) {
            return false;
        }
        AspExtraworkPo other = (AspExtraworkPo) object;
        if ((this.poNumber == null && other.poNumber != null) || (this.poNumber != null && !this.poNumber.equals(other.poNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.AspExtraworkPo[ poNumber=" + poNumber + " ]";
    }
    
}
