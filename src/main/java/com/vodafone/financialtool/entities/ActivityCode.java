/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "activity_code")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityCode.findAll", query = "SELECT a FROM ActivityCode a")
    , @NamedQuery(name = "ActivityCode.findById", query = "SELECT a FROM ActivityCode a WHERE a.id = :id")
    , @NamedQuery(name = "ActivityCode.findByDescription", query = "SELECT a FROM ActivityCode a WHERE a.description = :description")
    , @NamedQuery(name = "ActivityCode.findByVendorPrice", query = "SELECT a FROM ActivityCode a WHERE a.vendorPrice = :vendorPrice")
    , @NamedQuery(name = "ActivityCode.findBySubcontractorPrice", query = "SELECT a FROM ActivityCode a WHERE a.subcontractorPrice = :subcontractorPrice")
    , @NamedQuery(name = "ActivityCode.findByUm", query = "SELECT a FROM ActivityCode a WHERE a.um = :um")
    , @NamedQuery(name = "ActivityCode.findByUmPercent", query = "SELECT a FROM ActivityCode a WHERE a.umPercent = :umPercent")})
public class ActivityCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "id")
    private String id;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "vendor_price")
    private Double vendorPrice;
    @Column(name = "subcontractor_price")
    private Double subcontractorPrice;
    @Column(name = "um")
    private Double um;
    @Column(name = "um_percent")
    private Double umPercent;

    public ActivityCode() {
    }

    public ActivityCode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getVendorPrice() {
        return vendorPrice;
    }

    public void setVendorPrice(Double vendorPrice) {
        this.vendorPrice = vendorPrice;
    }

    public Double getSubcontractorPrice() {
        return subcontractorPrice;
    }

    public void setSubcontractorPrice(Double subcontractorPrice) {
        this.subcontractorPrice = subcontractorPrice;
    }

    public Double getUm() {
        return um;
    }

    public void setUm(Double um) {
        this.um = um;
    }

    public Double getUmPercent() {
        return umPercent;
    }

    public void setUmPercent(Double umPercent) {
        this.umPercent = umPercent;
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
        if (!(object instanceof ActivityCode)) {
            return false;
        }
        ActivityCode other = (ActivityCode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id+" | "+description+" | "+subcontractorPrice;
    }
    
}
