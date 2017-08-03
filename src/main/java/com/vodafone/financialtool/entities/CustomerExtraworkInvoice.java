/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "customer_extrawork_invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerExtraworkInvoice.findAll", query = "SELECT c FROM CustomerExtraworkInvoice c")
    , @NamedQuery(name = "CustomerExtraworkInvoice.findById", query = "SELECT c FROM CustomerExtraworkInvoice c WHERE c.id = :id")
    , @NamedQuery(name = "CustomerExtraworkInvoice.findByPoNumer", query = "SELECT c FROM CustomerExtraworkInvoice c WHERE c.poNumer = :poNumer")
    , @NamedQuery(name = "CustomerExtraworkInvoice.findByInvoiceNumber", query = "SELECT c FROM CustomerExtraworkInvoice c WHERE c.invoiceNumber = :invoiceNumber")
    , @NamedQuery(name = "CustomerExtraworkInvoice.findByInvoiceDate", query = "SELECT c FROM CustomerExtraworkInvoice c WHERE c.invoiceDate = :invoiceDate")
    , @NamedQuery(name = "CustomerExtraworkInvoice.findByInvoiceValue", query = "SELECT c FROM CustomerExtraworkInvoice c WHERE c.invoiceValue = :invoiceValue")
    , @NamedQuery(name = "CustomerExtraworkInvoice.findByCreationDate", query = "SELECT c FROM CustomerExtraworkInvoice c WHERE c.creationDate = :creationDate")})
public class CustomerExtraworkInvoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "po_numer")
    private String poNumer;
    @Size(max = 2147483647)
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "invoice_date")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    @Column(name = "invoice_value")
    private Double invoiceValue;
    @Size(max = 2147483647)
    @Column(name = "creation_date")
    private String creationDate;
    @JoinColumn(name = "md_number", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CustomerExtraworkMd mdNumber;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne
    private Users creator;

    public CustomerExtraworkInvoice() {
    }

    public CustomerExtraworkInvoice(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

   public void setId(Long id) {
        this.id = id;
    }

    public String getPoNumer() {
        return poNumer;
    }

    public void setPoNumer(String poNumer) {
        this.poNumer = poNumer;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public CustomerExtraworkMd getMdNumber() {
        return mdNumber;
    }

    public void setMdNumber(CustomerExtraworkMd mdNumber) {
        this.mdNumber = mdNumber;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerExtraworkInvoice)) {
            return false;
        }
        CustomerExtraworkInvoice other = (CustomerExtraworkInvoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.CustomerExtraworkInvoice[ id=" + id + " ]";
    }
    
}
