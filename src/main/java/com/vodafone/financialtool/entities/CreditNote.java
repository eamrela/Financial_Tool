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
@Table(name = "credit_note")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CreditNote.findAll", query = "SELECT c FROM CreditNote c")
    , @NamedQuery(name = "CreditNote.findById", query = "SELECT c FROM CreditNote c WHERE c.id = :id")
    , @NamedQuery(name = "CreditNote.findByCnDate", query = "SELECT c FROM CreditNote c WHERE c.cnDate = :cnDate")
    , @NamedQuery(name = "CreditNote.findByCnDescription", query = "SELECT c FROM CreditNote c WHERE c.cnDescription = :cnDescription")
    , @NamedQuery(name = "CreditNote.findByCnOwner", query = "SELECT c FROM CreditNote c WHERE c.cnOwner = :cnOwner")
    , @NamedQuery(name = "CreditNote.findByCnValue", query = "SELECT c FROM CreditNote c WHERE c.cnValue = :cnValue")
    , @NamedQuery(name = "CreditNote.findByCreationTime", query = "SELECT c FROM CreditNote c WHERE c.creationTime = :creationTime")})
public class CreditNote implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cn_date")
    @Temporal(TemporalType.DATE)
    private Date cnDate;
    @Size(max = 2147483647)
    @Column(name = "cn_description")
    private String cnDescription;
    @Size(max = 2147483647)
    @Column(name = "cn_owner")
    private String cnOwner ="Vodafone";
    @Column(name = "settled")
    private Boolean settled=false;
    @Column(name = "cn_value")
    private Double cnValue;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Column(name = "settlement_po")
    private String settlementPo;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne
    private Users creator;

    public CreditNote() {
    }

    public CreditNote(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCnDate() {
        return cnDate;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }
    

    public void setCnDate(Date cnDate) {
        this.cnDate = cnDate;
    }

    public String getCnDescription() {
        return cnDescription;
    }

    public void setCnDescription(String cnDescription) {
        this.cnDescription = cnDescription;
    }

    public String getCnOwner() {
        if(cnOwner==null){
            cnOwner="Vodafone";
        }
        return cnOwner;
    }

    public void setCnOwner(String cnOwner) {
        this.cnOwner = cnOwner;
    }

    public Double getCnValue() {
        return cnValue;
    }

    public void setCnValue(Double cnValue) {
        this.cnValue = cnValue;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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
        if (!(object instanceof CreditNote)) {
            return false;
        }
        CreditNote other = (CreditNote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tt.CreditNote[ id=" + id + " ]";
    }

    public String getSettlementPo() {
        return settlementPo;
    }

    public void setSettlementPo(String settlementPo) {
        this.settlementPo = settlementPo;
    }
    
    
}
