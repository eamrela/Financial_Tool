/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author eamrela
 */
@Entity
public class AspGrnDeserved implements Serializable {
    
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "subcontractor")
    private String aspName;
    @Column(name = "grn_deserved")
    private Double totalCOS;

    public String getAspName() {
        return aspName;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    
    public void setAspName(String aspName) {
        this.aspName = aspName;
    }

    public Double getTotalCOS() {
        return totalCOS;
    }

    public void setTotalCOS(Double totalCOS) {
        this.totalCOS = totalCOS;
    }
    
    
}
