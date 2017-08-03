/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByUserName", query = "SELECT u FROM Users u WHERE u.userName = :userName")
    , @NamedQuery(name = "Users.findByUserPassword", query = "SELECT u FROM Users u WHERE u.userPassword = :userPassword")
    , @NamedQuery(name = "Users.findByUserEmail", query = "SELECT u FROM Users u WHERE u.userEmail = :userEmail")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 2147483647)
    @Column(name = "user_password")
    private String userPassword;
    @Size(max = 2147483647)
    @Column(name = "user_email")
    private String userEmail;
    @Size(max = 2147483647)
    @Column(name = "company")
    private String company;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<UserGroups> userGroupsCollection;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<Regions> regionsCollection;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<UserRoles> userRolesCollection;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<Domains> domainsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<AspServiceGrn> aspServiceGrnCollection;
    @OneToMany(mappedBy = "creator")
    private Collection<CustomerExtraworkInvoice> customerExtraworkInvoiceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<AspServicePo> aspServicePoCollection;
    @OneToMany(mappedBy = "uploadedBy")
    private Collection<ExtraworkAttachments> extraworkAttachmentsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<CustomerServiceMd> customerServiceMdCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<AspExtraworkWorkDone> aspExtraworkWorkDoneCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<AspExtraworkGrn> aspExtraworkGrnCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<CustomerExtraworkMd> customerExtraworkMdCollection;
    @OneToMany(mappedBy = "creator")
    private Collection<JvReporting> jvReportingCollection;
    @OneToMany(mappedBy = "creator")
    private Collection<CustomerServiceInvoice> customerServiceInvoiceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<AspServiceWorkDone> aspServiceWorkDoneCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<CustomerExtraworkPo> customerExtraworkPoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<CustomerServicePo> customerServicePoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<CustomerExtraworkWorkDone> customerExtraworkWorkDoneCollection;
    @OneToMany(mappedBy = "creator")
    private Collection<TimeReporting> timeReportingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<ExtraWork> extraWorkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<CustomerServiceWorkDone> customerServiceWorkDoneCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Collection<AspExtraworkPo> aspExtraworkPoCollection;

    public Users() {
    }

    public Users(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @XmlTransient
    public Collection<UserGroups> getUserGroupsCollection() {
        return userGroupsCollection;
    }

    public void setUserGroupsCollection(Collection<UserGroups> userGroupsCollection) {
        this.userGroupsCollection = userGroupsCollection;
    }

    @XmlTransient
    public Collection<Regions> getRegionsCollection() {
        return regionsCollection;
    }

    public void setRegionsCollection(Collection<Regions> regionsCollection) {
        this.regionsCollection = regionsCollection;
    }

    @XmlTransient
    public Collection<UserRoles> getUserRolesCollection() {
        return userRolesCollection;
    }

    public void setUserRolesCollection(Collection<UserRoles> userRolesCollection) {
        this.userRolesCollection = userRolesCollection;
    }

    @XmlTransient
    public Collection<Domains> getDomainsCollection() {
        return domainsCollection;
    }

    public void setDomainsCollection(Collection<Domains> domainsCollection) {
        this.domainsCollection = domainsCollection;
    }

    @XmlTransient
    public Collection<AspServiceGrn> getAspServiceGrnCollection() {
        return aspServiceGrnCollection;
    }

    public void setAspServiceGrnCollection(Collection<AspServiceGrn> aspServiceGrnCollection) {
        this.aspServiceGrnCollection = aspServiceGrnCollection;
    }

    @XmlTransient
    public Collection<CustomerExtraworkInvoice> getCustomerExtraworkInvoiceCollection() {
        return customerExtraworkInvoiceCollection;
    }

    public void setCustomerExtraworkInvoiceCollection(Collection<CustomerExtraworkInvoice> customerExtraworkInvoiceCollection) {
        this.customerExtraworkInvoiceCollection = customerExtraworkInvoiceCollection;
    }

    @XmlTransient
    public Collection<AspServicePo> getAspServicePoCollection() {
        return aspServicePoCollection;
    }

    public void setAspServicePoCollection(Collection<AspServicePo> aspServicePoCollection) {
        this.aspServicePoCollection = aspServicePoCollection;
    }

    @XmlTransient
    public Collection<ExtraworkAttachments> getExtraworkAttachmentsCollection() {
        return extraworkAttachmentsCollection;
    }

    public void setExtraworkAttachmentsCollection(Collection<ExtraworkAttachments> extraworkAttachmentsCollection) {
        this.extraworkAttachmentsCollection = extraworkAttachmentsCollection;
    }

    @XmlTransient
    public Collection<CustomerServiceMd> getCustomerServiceMdCollection() {
        return customerServiceMdCollection;
    }

    public void setCustomerServiceMdCollection(Collection<CustomerServiceMd> customerServiceMdCollection) {
        this.customerServiceMdCollection = customerServiceMdCollection;
    }

    @XmlTransient
    public Collection<AspExtraworkWorkDone> getAspExtraworkWorkDoneCollection() {
        return aspExtraworkWorkDoneCollection;
    }

    public void setAspExtraworkWorkDoneCollection(Collection<AspExtraworkWorkDone> aspExtraworkWorkDoneCollection) {
        this.aspExtraworkWorkDoneCollection = aspExtraworkWorkDoneCollection;
    }

    @XmlTransient
    public Collection<AspExtraworkGrn> getAspExtraworkGrnCollection() {
        return aspExtraworkGrnCollection;
    }

    public void setAspExtraworkGrnCollection(Collection<AspExtraworkGrn> aspExtraworkGrnCollection) {
        this.aspExtraworkGrnCollection = aspExtraworkGrnCollection;
    }

    @XmlTransient
    public Collection<CustomerExtraworkMd> getCustomerExtraworkMdCollection() {
        return customerExtraworkMdCollection;
    }

    public void setCustomerExtraworkMdCollection(Collection<CustomerExtraworkMd> customerExtraworkMdCollection) {
        this.customerExtraworkMdCollection = customerExtraworkMdCollection;
    }

    @XmlTransient
    public Collection<JvReporting> getJvReportingCollection() {
        return jvReportingCollection;
    }

    public void setJvReportingCollection(Collection<JvReporting> jvReportingCollection) {
        this.jvReportingCollection = jvReportingCollection;
    }

    @XmlTransient
    public Collection<CustomerServiceInvoice> getCustomerServiceInvoiceCollection() {
        return customerServiceInvoiceCollection;
    }

    public void setCustomerServiceInvoiceCollection(Collection<CustomerServiceInvoice> customerServiceInvoiceCollection) {
        this.customerServiceInvoiceCollection = customerServiceInvoiceCollection;
    }

    @XmlTransient
    public Collection<AspServiceWorkDone> getAspServiceWorkDoneCollection() {
        return aspServiceWorkDoneCollection;
    }

    public void setAspServiceWorkDoneCollection(Collection<AspServiceWorkDone> aspServiceWorkDoneCollection) {
        this.aspServiceWorkDoneCollection = aspServiceWorkDoneCollection;
    }

    @XmlTransient
    public Collection<CustomerExtraworkPo> getCustomerExtraworkPoCollection() {
        return customerExtraworkPoCollection;
    }

    public void setCustomerExtraworkPoCollection(Collection<CustomerExtraworkPo> customerExtraworkPoCollection) {
        this.customerExtraworkPoCollection = customerExtraworkPoCollection;
    }

    @XmlTransient
    public Collection<CustomerServicePo> getCustomerServicePoCollection() {
        return customerServicePoCollection;
    }

    public void setCustomerServicePoCollection(Collection<CustomerServicePo> customerServicePoCollection) {
        this.customerServicePoCollection = customerServicePoCollection;
    }

    @XmlTransient
    public Collection<CustomerExtraworkWorkDone> getCustomerExtraworkWorkDoneCollection() {
        return customerExtraworkWorkDoneCollection;
    }

    public void setCustomerExtraworkWorkDoneCollection(Collection<CustomerExtraworkWorkDone> customerExtraworkWorkDoneCollection) {
        this.customerExtraworkWorkDoneCollection = customerExtraworkWorkDoneCollection;
    }

    @XmlTransient
    public Collection<TimeReporting> getTimeReportingCollection() {
        return timeReportingCollection;
    }

    public void setTimeReportingCollection(Collection<TimeReporting> timeReportingCollection) {
        this.timeReportingCollection = timeReportingCollection;
    }

    @XmlTransient
    public Collection<ExtraWork> getExtraWorkCollection() {
        return extraWorkCollection;
    }

    public void setExtraWorkCollection(Collection<ExtraWork> extraWorkCollection) {
        this.extraWorkCollection = extraWorkCollection;
    }

    @XmlTransient
    public Collection<CustomerServiceWorkDone> getCustomerServiceWorkDoneCollection() {
        return customerServiceWorkDoneCollection;
    }

    public void setCustomerServiceWorkDoneCollection(Collection<CustomerServiceWorkDone> customerServiceWorkDoneCollection) {
        this.customerServiceWorkDoneCollection = customerServiceWorkDoneCollection;
    }

    @XmlTransient
    public Collection<AspExtraworkPo> getAspExtraworkPoCollection() {
        return aspExtraworkPoCollection;
    }

    public void setAspExtraworkPoCollection(Collection<AspExtraworkPo> aspExtraworkPoCollection) {
        this.aspExtraworkPoCollection = aspExtraworkPoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.Users[ userName=" + userName + " ]";
    }
    
}
