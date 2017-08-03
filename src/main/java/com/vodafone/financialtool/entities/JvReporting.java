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
@Table(name = "jv_reporting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JvReporting.findAll", query = "SELECT j FROM JvReporting j")
    , @NamedQuery(name = "JvReporting.findById", query = "SELECT j FROM JvReporting j WHERE j.id = :id")
    , @NamedQuery(name = "JvReporting.findByJvDate", query = "SELECT j FROM JvReporting j WHERE j.jvDate = :jvDate")
    , @NamedQuery(name = "JvReporting.findByJvDescription", query = "SELECT j FROM JvReporting j WHERE j.jvDescription = :jvDescription")
    , @NamedQuery(name = "JvReporting.findByJvTeam", query = "SELECT j FROM JvReporting j WHERE j.jvTeam = :jvTeam")
    , @NamedQuery(name = "JvReporting.findByJvValue", query = "SELECT j FROM JvReporting j WHERE j.jvValue = :jvValue")
    , @NamedQuery(name = "JvReporting.findByCreationTime", query = "SELECT j FROM JvReporting j WHERE j.creationTime = :creationTime")})
public class JvReporting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "jv_date")
    @Temporal(TemporalType.DATE)
    private Date jvDate;
    @Size(max = 2147483647)
    @Column(name = "jv_description")
    private String jvDescription;
    @Size(max = 2147483647)
    @Column(name = "jv_team")
    private String jvTeam;
    @Column(name = "jv_value")
    private Double jvValue;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne
    private Users creator;

    public JvReporting() {
    }

    public JvReporting(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getJvDate() {
        return jvDate;
    }

    public void setJvDate(Date jvDate) {
        this.jvDate = jvDate;
    }

    public String getJvDescription() {
        return jvDescription;
    }

    public void setJvDescription(String jvDescription) {
        this.jvDescription = jvDescription;
    }

    public String getJvTeam() {
        return jvTeam;
    }

    public void setJvTeam(String jvTeam) {
        this.jvTeam = jvTeam;
    }

    public Double getJvValue() {
        return jvValue;
    }

    public void setJvValue(Double jvValue) {
        this.jvValue = jvValue;
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
        if (!(object instanceof JvReporting)) {
            return false;
        }
        JvReporting other = (JvReporting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.JvReporting[ id=" + id + " ]";
    }
    
}
