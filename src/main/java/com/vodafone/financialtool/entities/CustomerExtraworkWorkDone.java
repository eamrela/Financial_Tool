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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "customer_extrawork_work_done")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerExtraworkWorkDone.findAll", query = "SELECT c FROM CustomerExtraworkWorkDone c")
    , @NamedQuery(name = "CustomerExtraworkWorkDone.findById", query = "SELECT c FROM CustomerExtraworkWorkDone c WHERE c.id = :id")
    , @NamedQuery(name = "CustomerExtraworkWorkDone.findByWorkDoneDate", query = "SELECT c FROM CustomerExtraworkWorkDone c WHERE c.workDoneDate = :workDoneDate")
    , @NamedQuery(name = "CustomerExtraworkWorkDone.findByWorkDonePercentage", query = "SELECT c FROM CustomerExtraworkWorkDone c WHERE c.workDonePercentage = :workDonePercentage")
    , @NamedQuery(name = "CustomerExtraworkWorkDone.findByWorkDoneValue", query = "SELECT c FROM CustomerExtraworkWorkDone c WHERE c.workDoneValue = :workDoneValue")
    , @NamedQuery(name = "CustomerExtraworkWorkDone.findByCreationTime", query = "SELECT c FROM CustomerExtraworkWorkDone c WHERE c.creationTime = :creationTime")})
public class CustomerExtraworkWorkDone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "work_done_date")
    @Temporal(TemporalType.DATE)
    private Date workDoneDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "work_done_percentage")
    private Double workDonePercentage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "work_done_value")
    private Double workDoneValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @JoinColumn(name = "po_number", referencedColumnName = "po_number")
    @ManyToOne(optional = false)
    private CustomerExtraworkPo poNumber;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne(optional = false)
    private Users creator;

    public CustomerExtraworkWorkDone() {
    }

    public CustomerExtraworkWorkDone(Long id) {
        this.id = id;
    }

    public CustomerExtraworkWorkDone(Long id, Date workDoneDate, Double workDonePercentage, Double workDoneValue, Date creationTime) {
        this.id = id;
        this.workDoneDate = workDoneDate;
        this.workDonePercentage = workDonePercentage;
        this.workDoneValue = workDoneValue;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getWorkDoneDate() {
        return workDoneDate;
    }

    public void setWorkDoneDate(Date workDoneDate) {
        this.workDoneDate = workDoneDate;
    }

    public Double getWorkDonePercentage() {
        return workDonePercentage;
    }

    public void setWorkDonePercentage(Double workDonePercentage) {
        this.workDonePercentage = workDonePercentage;
    }

    public Double getWorkDoneValue() {
        return workDoneValue;
    }

    public void setWorkDoneValue(Double workDoneValue) {
        this.workDoneValue = workDoneValue;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public CustomerExtraworkPo getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(CustomerExtraworkPo poNumber) {
        this.poNumber = poNumber;
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
        if (!(object instanceof CustomerExtraworkWorkDone)) {
            return false;
        }
        CustomerExtraworkWorkDone other = (CustomerExtraworkWorkDone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.CustomerExtraworkWorkDone[ id=" + id + " ]";
    }
    
}
