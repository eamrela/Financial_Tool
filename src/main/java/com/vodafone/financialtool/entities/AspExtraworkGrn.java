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
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "asp_extrawork_grn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AspExtraworkGrn.findAll", query = "SELECT a FROM AspExtraworkGrn a")
    , @NamedQuery(name = "AspExtraworkGrn.findById", query = "SELECT a FROM AspExtraworkGrn a WHERE a.id = :id")
    , @NamedQuery(name = "AspExtraworkGrn.findByGrnNumber", query = "SELECT a FROM AspExtraworkGrn a WHERE a.grnNumber = :grnNumber")
    , @NamedQuery(name = "AspExtraworkGrn.findByGrnDate", query = "SELECT a FROM AspExtraworkGrn a WHERE a.grnDate = :grnDate")
    , @NamedQuery(name = "AspExtraworkGrn.findByGrnValue", query = "SELECT a FROM AspExtraworkGrn a WHERE a.grnValue = :grnValue")
    , @NamedQuery(name = "AspExtraworkGrn.findByCreationTime", query = "SELECT a FROM AspExtraworkGrn a WHERE a.creationTime = :creationTime")})
public class AspExtraworkGrn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "grn_number")
    private String grnNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grn_date")
    @Temporal(TemporalType.DATE)
    private Date grnDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grn_value")
    private Double grnValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @JoinColumn(name = "po_number", referencedColumnName = "po_number")
    @ManyToOne(optional = false , fetch = FetchType.EAGER)
    private AspExtraworkPo poNumber;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne(optional = false , fetch = FetchType.EAGER)
    private Users creator;

    public AspExtraworkGrn() {
    }

    public AspExtraworkGrn(Long id) {
        this.id = id;
    }

    public AspExtraworkGrn(Long id, Date grnDate, Double grnValue, Date creationTime) {
        this.id = id;
        this.grnDate = grnDate;
        this.grnValue = grnValue;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public Date getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(Date grnDate) {
        this.grnDate = grnDate;
    }

    public Double getGrnValue() {
        return grnValue;
    }

    public void setGrnValue(Double grnValue) {
        this.grnValue = grnValue;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public AspExtraworkPo getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(AspExtraworkPo poNumber) {
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
        if (!(object instanceof AspExtraworkGrn)) {
            return false;
        }
        AspExtraworkGrn other = (AspExtraworkGrn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.AspExtraworkGrn[ id=" + id + " ]";
    }
    
}
