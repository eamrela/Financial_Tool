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
@Table(name = "penalty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Penalty.findAll", query = "SELECT p FROM Penalty p")
    , @NamedQuery(name = "Penalty.findById", query = "SELECT p FROM Penalty p WHERE p.id = :id")
    , @NamedQuery(name = "Penalty.findByPnDate", query = "SELECT p FROM Penalty p WHERE p.pnDate = :pnDate")
    , @NamedQuery(name = "Penalty.findByPnDescription", query = "SELECT p FROM Penalty p WHERE p.pnDescription = :pnDescription")
    , @NamedQuery(name = "Penalty.findByPnOwner", query = "SELECT p FROM Penalty p WHERE p.pnOwner = :pnOwner")
    , @NamedQuery(name = "Penalty.findByPnValue", query = "SELECT p FROM Penalty p WHERE p.pnValue = :pnValue")
    , @NamedQuery(name = "Penalty.findByCreationTime", query = "SELECT p FROM Penalty p WHERE p.creationTime = :creationTime")})
public class Penalty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "pn_date")
    @Temporal(TemporalType.DATE)
    private Date pnDate;
    @Size(max = 2147483647)
    @Column(name = "pn_description")
    private String pnDescription;
    @Column(name = "settled")
    private Boolean settled=false;
    @Size(max = 2147483647)
    @Column(name = "pn_owner")
    private String pnOwner="Vodafone";
    @Column(name = "pn_value")
    private Double pnValue;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Column(name = "settlement_po")
    private String settlementPo;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne
    private Users creator;

    public Penalty() {
    }

    public Penalty(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    
    public Date getPnDate() {
        return pnDate;
    }

    public void setPnDate(Date pnDate) {
        this.pnDate = pnDate;
    }

    public String getPnDescription() {
        return pnDescription;
    }

    public void setPnDescription(String pnDescription) {
        this.pnDescription = pnDescription;
    }

    public String getPnOwner() {
        return pnOwner;
    }

    public void setPnOwner(String pnOwner) {
        this.pnOwner = pnOwner;
    }

    public Double getPnValue() {
        return pnValue;
    }

    public void setPnValue(Double pnValue) {
        this.pnValue = pnValue;
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
        if (!(object instanceof Penalty)) {
            return false;
        }
        Penalty other = (Penalty) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tt.Penalty[ id=" + id + " ]";
    }

    public String getSettlementPo() {
        return settlementPo;
    }

    public void setSettlementPo(String settlementPo) {
        this.settlementPo = settlementPo;
    }
    
    
}
