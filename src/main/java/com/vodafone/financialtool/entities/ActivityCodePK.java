/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author eamrela
 */
@Embeddable
public class ActivityCodePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "asp")
    private String asp;

    public ActivityCodePK() {
    }

    public ActivityCodePK(String id, String asp) {
        this.id = id;
        this.asp = asp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (asp != null ? asp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityCodePK)) {
            return false;
        }
        ActivityCodePK other = (ActivityCodePK) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.asp == null && other.asp != null) || (this.asp != null && !this.asp.equals(other.asp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.etisalat.dummy_ft.ActivityCodePK[ id=" + id + ", asp=" + asp + " ]";
    }
    
}
