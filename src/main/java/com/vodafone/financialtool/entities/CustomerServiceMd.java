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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "customer_service_md")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerServiceMd.findAll", query = "SELECT c FROM CustomerServiceMd c")
    , @NamedQuery(name = "CustomerServiceMd.findById", query = "SELECT c FROM CustomerServiceMd c WHERE c.id = :id")
    , @NamedQuery(name = "CustomerServiceMd.findByMdNumber", query = "SELECT c FROM CustomerServiceMd c WHERE c.mdNumber = :mdNumber")
    , @NamedQuery(name = "CustomerServiceMd.findByMdDate", query = "SELECT c FROM CustomerServiceMd c WHERE c.mdDate = :mdDate")
    , @NamedQuery(name = "CustomerServiceMd.findByMdValue", query = "SELECT c FROM CustomerServiceMd c WHERE c.mdValue = :mdValue")
    , @NamedQuery(name = "CustomerServiceMd.findByCreationTime", query = "SELECT c FROM CustomerServiceMd c WHERE c.creationTime = :creationTime")})
public class CustomerServiceMd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "md_number")
    private String mdNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "md_date")
    @Temporal(TemporalType.DATE)
    private Date mdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "md_value")
    private Double mdValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @JoinColumn(name = "po_number", referencedColumnName = "po_number")
    @ManyToOne(optional = false)
    private CustomerServicePo poNumber;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne(optional = false)
    private Users creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdNumber")
    private Collection<CustomerServiceInvoice> customerServiceInvoiceCollection;

    public CustomerServiceMd() {
    }

    public CustomerServiceMd(Long id) {
        this.id = id;
    }

    public CustomerServiceMd(Long id, Date mdDate, Double mdValue, Date creationTime) {
        this.id = id;
        this.mdDate = mdDate;
        this.mdValue = mdValue;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMdNumber() {
        return mdNumber;
    }

    public void setMdNumber(String mdNumber) {
        this.mdNumber = mdNumber;
    }

    public Date getMdDate() {
        return mdDate;
    }

    public void setMdDate(Date mdDate) {
        this.mdDate = mdDate;
    }

    public Double getMdValue() {
        return mdValue;
    }

    public void setMdValue(Double mdValue) {
        this.mdValue = mdValue;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public CustomerServicePo getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(CustomerServicePo poNumber) {
        this.poNumber = poNumber;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    @XmlTransient
    public Collection<CustomerServiceInvoice> getCustomerServiceInvoiceCollection() {
        return customerServiceInvoiceCollection;
    }

    public void setCustomerServiceInvoiceCollection(Collection<CustomerServiceInvoice> customerServiceInvoiceCollection) {
        this.customerServiceInvoiceCollection = customerServiceInvoiceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerServiceMd)) {
            return false;
        }
        CustomerServiceMd other = (CustomerServiceMd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.CustomerServiceMd[ id=" + id + " ]";
    }
    
}
