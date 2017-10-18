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
@Table(name = "ns_simulation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NsSimulation.findAll", query = "SELECT n FROM NsSimulation n")
    , @NamedQuery(name = "NsSimulation.findById", query = "SELECT n FROM NsSimulation n WHERE n.id = :id")
    , @NamedQuery(name = "NsSimulation.findByMonthNo", query = "SELECT n FROM NsSimulation n WHERE n.monthNo = :monthNo")
    , @NamedQuery(name = "NsSimulation.findByTypeOfNs", query = "SELECT n FROM NsSimulation n WHERE n.typeOfNs = :typeOfNs")
    , @NamedQuery(name = "NsSimulation.findByPoNumber", query = "SELECT n FROM NsSimulation n WHERE n.poNumber = :poNumber")
    , @NamedQuery(name = "NsSimulation.findByTypeNumber", query = "SELECT n FROM NsSimulation n WHERE n.typeNumber = :typeNumber")
    , @NamedQuery(name = "NsSimulation.findByInvoiceValue", query = "SELECT n FROM NsSimulation n WHERE n.invoiceValue = :invoiceValue")
    , @NamedQuery(name = "NsSimulation.findByCreator", query = "SELECT n FROM NsSimulation n WHERE n.creator = :creator")
    , @NamedQuery(name = "NsSimulation.findByCreationTime", query = "SELECT n FROM NsSimulation n WHERE n.creationTime = :creationTime")
    , @NamedQuery(name = "NsSimulation.findByNsComment", query = "SELECT n FROM NsSimulation n WHERE n.nsComment = :nsComment")
    , @NamedQuery(name = "NsSimulation.findByTypeOfType", query = "SELECT n FROM NsSimulation n WHERE n.typeOfType = :typeOfType")})
public class NsSimulation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "month_no")
    private Short monthNo;
    @Size(max = 2147483647)
    @Column(name = "type_of_ns")
    private String typeOfNs;
    @Size(max = 2147483647)
    @Column(name = "po_number")
    private String poNumber;
    @Size(max = 2147483647)
    @Column(name = "type_number")
    private String typeNumber;
    @Column(name = "invoice_value")
    private Double invoiceValue;
    @Size(max = 2147483647)
    @Column(name = "creator")
    private String creator;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Size(max = 2147483647)
    @Column(name = "ns_comment")
    private String nsComment;
    @Size(max = 2147483647)
    @Column(name = "type_of_type")
    private String typeOfType;
    @Column(name = "editable")
    private Boolean editable = true;
    @Column(name = "description")
    private String description;

    public NsSimulation() {
    }

    public NsSimulation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getMonthNo() {
        return monthNo;
    }

    public void setMonthNo(Short monthNo) {
        this.monthNo = monthNo;
    }

    public String getTypeOfNs() {
        return typeOfNs;
    }

    public void setTypeOfNs(String typeOfNs) {
        this.typeOfNs = typeOfNs;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getNsComment() {
        return nsComment;
    }

    public void setNsComment(String nsComment) {
        this.nsComment = nsComment;
    }

    public String getTypeOfType() {
        return typeOfType;
    }

    public void setTypeOfType(String typeOfType) {
        this.typeOfType = typeOfType;
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
        if (!(object instanceof NsSimulation)) {
            return false;
        }
        NsSimulation other = (NsSimulation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "new2.NsSimulation[ id=" + id + " ]";
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
