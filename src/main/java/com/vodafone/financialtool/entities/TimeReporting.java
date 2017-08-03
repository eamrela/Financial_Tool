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
@Table(name = "time_reporting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TimeReporting.findAll", query = "SELECT t FROM TimeReporting t")
    , @NamedQuery(name = "TimeReporting.findById", query = "SELECT t FROM TimeReporting t WHERE t.id = :id")
    , @NamedQuery(name = "TimeReporting.findByReportingDate", query = "SELECT t FROM TimeReporting t WHERE t.reportingDate = :reportingDate")
    , @NamedQuery(name = "TimeReporting.findByReportingDescription", query = "SELECT t FROM TimeReporting t WHERE t.reportingDescription = :reportingDescription")
    , @NamedQuery(name = "TimeReporting.findByReportingValue", query = "SELECT t FROM TimeReporting t WHERE t.reportingValue = :reportingValue")
    , @NamedQuery(name = "TimeReporting.findByCreationTime", query = "SELECT t FROM TimeReporting t WHERE t.creationTime = :creationTime")
    , @NamedQuery(name = "TimeReporting.findByReportingTeam", query = "SELECT t FROM TimeReporting t WHERE t.reportingTeam = :reportingTeam")})
public class TimeReporting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reporting_date")
    @Temporal(TemporalType.DATE)
    private Date reportingDate;
    @Size(max = 2147483647)
    @Column(name = "reporting_description")
    private String reportingDescription;
    @Column(name = "reporting_value")
    private Double reportingValue;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Size(max = 2147483647)
    @Column(name = "reporting_team")
    private String reportingTeam;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne
    private Users creator;

    public TimeReporting() {
    }

    public TimeReporting(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getReportingDescription() {
        return reportingDescription;
    }

    public void setReportingDescription(String reportingDescription) {
        this.reportingDescription = reportingDescription;
    }

    public Double getReportingValue() {
        return reportingValue;
    }

    public void setReportingValue(Double reportingValue) {
        this.reportingValue = reportingValue;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getReportingTeam() {
        return reportingTeam;
    }

    public void setReportingTeam(String reportingTeam) {
        this.reportingTeam = reportingTeam;
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
        if (!(object instanceof TimeReporting)) {
            return false;
        }
        TimeReporting other = (TimeReporting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.TimeReporting[ id=" + id + " ]";
    }
    
}
