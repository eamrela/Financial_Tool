/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author eamrela
 */
@Entity
public class ExtraWorkWithoutPo implements Serializable {
    
    @Id
    @Column(name = "asp")
    private String aspName;
    @Column(name = "domain_name")
    private String domainName;
    @Column(name = "total_cos")
    private Double totalCOS;

    
    
    public String getAspName() {
        return aspName;
    }

    public void setAspName(String aspName) {
        this.aspName = aspName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Double getTotalCOS() {
        return totalCOS;
    }

    public void setTotalCOS(Double totalCOS) {
        this.totalCOS = totalCOS;
    }
    
    
}
