/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.entities;

import java.io.Serializable;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "extra_work")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExtraWork.findAll", query = "SELECT e FROM ExtraWork e")
    , @NamedQuery(name = "ExtraWork.findById", query = "SELECT e FROM ExtraWork e WHERE e.id = :id")
    , @NamedQuery(name = "ExtraWork.findBySite", query = "SELECT e FROM ExtraWork e WHERE e.site = :site")
    , @NamedQuery(name = "ExtraWork.findByAsp", query = "SELECT e FROM ExtraWork e WHERE e.asp = :asp")
    , @NamedQuery(name = "ExtraWork.findByRegion", query = "SELECT e FROM ExtraWork e WHERE e.region = :region")
    , @NamedQuery(name = "ExtraWork.findByCustomerOwner", query = "SELECT e FROM ExtraWork e WHERE e.customerOwner = :customerOwner")
    , @NamedQuery(name = "ExtraWork.findByApprovalStatus", query = "SELECT e FROM ExtraWork e WHERE e.approvalStatus = :approvalStatus")
    , @NamedQuery(name = "ExtraWork.findByDomainName", query = "SELECT e FROM ExtraWork e WHERE e.domainName = :domainName")
    , @NamedQuery(name = "ExtraWork.findBySubDomain", query = "SELECT e FROM ExtraWork e WHERE e.subDomain = :subDomain")
    , @NamedQuery(name = "ExtraWork.findByActivityDate", query = "SELECT e FROM ExtraWork e WHERE e.activityDate = :activityDate")
    , @NamedQuery(name = "ExtraWork.findByActivityCode", query = "SELECT e FROM ExtraWork e WHERE e.activityCode = :activityCode")
    , @NamedQuery(name = "ExtraWork.findByActivityDescription", query = "SELECT e FROM ExtraWork e WHERE e.activityDescription = :activityDescription")
    , @NamedQuery(name = "ExtraWork.findByActivityDetails", query = "SELECT e FROM ExtraWork e WHERE e.activityDetails = :activityDetails")
    , @NamedQuery(name = "ExtraWork.findByQty", query = "SELECT e FROM ExtraWork e WHERE e.qty = :qty")
    , @NamedQuery(name = "ExtraWork.findByUnitPriceAsp", query = "SELECT e FROM ExtraWork e WHERE e.unitPriceAsp = :unitPriceAsp")
    , @NamedQuery(name = "ExtraWork.findByUnitPriceVendor", query = "SELECT e FROM ExtraWork e WHERE e.unitPriceVendor = :unitPriceVendor")
    , @NamedQuery(name = "ExtraWork.findByTotalPriceAsp", query = "SELECT e FROM ExtraWork e WHERE e.totalPriceAsp = :totalPriceAsp")
    , @NamedQuery(name = "ExtraWork.findByTotalPriceVendor", query = "SELECT e FROM ExtraWork e WHERE e.totalPriceVendor = :totalPriceVendor")
    , @NamedQuery(name = "ExtraWork.findByUm", query = "SELECT e FROM ExtraWork e WHERE e.um = :um")
    , @NamedQuery(name = "ExtraWork.findByUmPercent", query = "SELECT e FROM ExtraWork e WHERE e.umPercent = :umPercent")
    , @NamedQuery(name = "ExtraWork.findByCreationTime", query = "SELECT e FROM ExtraWork e WHERE e.creationTime = :creationTime")
    , @NamedQuery(name = "ExtraWork.findByAssignmentGroup", query = "SELECT e FROM ExtraWork e WHERE e.assignmentGroup = :assignmentGroup")
    , @NamedQuery(name = "ExtraWork.findByRegionApproval", query = "SELECT e FROM ExtraWork e WHERE e.regionApproval = :regionApproval")
    , @NamedQuery(name = "ExtraWork.findByDomainOwnerApproval", query = "SELECT e FROM ExtraWork e WHERE e.domainOwnerApproval = :domainOwnerApproval")
    , @NamedQuery(name = "ExtraWork.findByInvoiced", query = "SELECT e FROM ExtraWork e WHERE e.invoiced = :invoiced")
    , @NamedQuery(name = "ExtraWork.findByCorrelated", query = "SELECT e FROM ExtraWork e WHERE e.correlated = :correlated")})
public class ExtraWork implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "site")
    private String site;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "asp")
    private String asp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "region")
    private String region;
    @Basic(optional = true)
    @Size(min = 1, max = 2147483647)
    @Column(name = "customer_owner")
    private String customerOwner;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "approval_status")
    private String approvalStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "domain_name")
    private String domainName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "sub_domain")
    private String subDomain;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activity_date")
    @Temporal(TemporalType.DATE)
    private Date activityDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "activity_code")
    private String activityCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "activity_description")
    private String activityDescription;
    @Size(max = 2147483647)
    @Column(name = "activity_details")
    private String activityDetails;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private Double qty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unit_price_asp")
    private Double unitPriceAsp = 0.0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unit_price_vendor")
    private Double unitPriceVendor = 0.0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_price_asp")
    private Double totalPriceAsp = 0.0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_price_vendor")
    private Double totalPriceVendor=0.0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "um")
    private Double um=0.0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "um_percent")
    private Double umPercent=0.0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "assignment_group")
    private String assignmentGroup;
    @Column(name = "last_assignment")
    private String lastAssignment;
    @Column(name = "business_provider_approval")
    private Boolean businessProviderApproval;
    @Column(name = "region_approval")
    private Boolean regionApproval;
    @Column(name = "domain_owner_approval")
    private Boolean domainOwnerApproval;
    @Column(name = "invoiced")
    private Boolean invoiced;
    @Column(name = "correlated")
    private Boolean correlated;
    @ManyToMany(mappedBy = "extraWorkCollection")
    private Collection<CustomerExtraworkPo> customerExtraworkPoCollection;
    @ManyToMany(mappedBy = "extraWorkCollection")
    private Collection<AspExtraworkPo> aspExtraworkPoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "extraworkId")
    private Collection<ExtraworkAttachments> extraworkAttachmentsCollection;
    @JoinColumn(name = "creator", referencedColumnName = "user_name")
    @ManyToOne(optional = false)
    private Users creator;
    

    public ExtraWork() {
    }

    public ExtraWork(Long id) {
        this.id = id;
    }

    public ExtraWork(Long id, String site, String asp, String region, String customerOwner, String approvalStatus, String domainName, String subDomain, Date activityDate, String activityCode, String activityDescription, Double qty, Double unitPriceAsp, Double unitPriceVendor, Double totalPriceAsp, Double totalPriceVendor, Double um, Double umPercent, Date creationTime, String assignmentGroup) {
        this.id = id;
        this.site = site;
        this.asp = asp;
        this.region = region;
        this.customerOwner = customerOwner;
        this.approvalStatus = approvalStatus;
        this.domainName = domainName;
        this.subDomain = subDomain;
        this.activityDate = activityDate;
        this.activityCode = activityCode;
        this.activityDescription = activityDescription;
        this.qty = qty;
        this.unitPriceAsp = unitPriceAsp;
        this.unitPriceVendor = unitPriceVendor;
        this.totalPriceAsp = totalPriceAsp;
        this.totalPriceVendor = totalPriceVendor;
        this.um = um;
        this.umPercent = umPercent;
        this.creationTime = creationTime;
        this.assignmentGroup = assignmentGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCustomerOwner() {
        return customerOwner;
    }

    public void setCustomerOwner(String customerOwner) {
        this.customerOwner = customerOwner;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(String activityDetails) {
        this.activityDetails = activityDetails;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getUnitPriceAsp() {
        return unitPriceAsp;
    }

    public void setUnitPriceAsp(Double unitPriceAsp) {
        this.unitPriceAsp = unitPriceAsp;
    }

    public Double getUnitPriceVendor() {
        return unitPriceVendor;
    }

    public void setUnitPriceVendor(Double unitPriceVendor) {
        this.unitPriceVendor = unitPriceVendor;
    }

    public Double getTotalPriceAsp() {
        return totalPriceAsp;
    }

    public void setTotalPriceAsp(Double totalPriceAsp) {
        this.totalPriceAsp = totalPriceAsp;
    }

    public Double getTotalPriceVendor() {
        return totalPriceVendor;
    }

    public void setTotalPriceVendor(Double totalPriceVendor) {
        this.totalPriceVendor = totalPriceVendor;
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getAssignmentGroup() {
        return assignmentGroup;
    }

    public void setAssignmentGroup(String assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }

    public Boolean getRegionApproval() {
        return regionApproval;
    }

    public void setRegionApproval(Boolean regionApproval) {
        this.regionApproval = regionApproval;
    }

    public Boolean getDomainOwnerApproval() {
        return domainOwnerApproval;
    }

    public void setDomainOwnerApproval(Boolean domainOwnerApproval) {
        this.domainOwnerApproval = domainOwnerApproval;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    public Boolean getCorrelated() {
        return correlated;
    }

    public void setCorrelated(Boolean correlated) {
        this.correlated = correlated;
    }

    @XmlTransient
    public Collection<CustomerExtraworkPo> getCustomerExtraworkPoCollection() {
        return customerExtraworkPoCollection;
    }

    public void setCustomerExtraworkPoCollection(Collection<CustomerExtraworkPo> customerExtraworkPoCollection) {
        this.customerExtraworkPoCollection = customerExtraworkPoCollection;
    }

    @XmlTransient
    public Collection<AspExtraworkPo> getAspExtraworkPoCollection() {
        return aspExtraworkPoCollection;
    }

    public void setAspExtraworkPoCollection(Collection<AspExtraworkPo> aspExtraworkPoCollection) {
        this.aspExtraworkPoCollection = aspExtraworkPoCollection;
    }

    @XmlTransient
    public Collection<ExtraworkAttachments> getExtraworkAttachmentsCollection() {
        return extraworkAttachmentsCollection;
    }

    public void setExtraworkAttachmentsCollection(Collection<ExtraworkAttachments> extraworkAttachmentsCollection) {
        this.extraworkAttachmentsCollection = extraworkAttachmentsCollection;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public String getLastAssignment() {
        return lastAssignment;
    }

    public void setLastAssignment(String lastAssignment) {
        this.lastAssignment = lastAssignment;
    }

    public Boolean getBusinessProviderApproval() {
        return businessProviderApproval;
    }

    public void setBusinessProviderApproval(Boolean businessProviderApproval) {
        this.businessProviderApproval = businessProviderApproval;
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
        if (!(object instanceof ExtraWork)) {
            return false;
        }
        ExtraWork other = (ExtraWork) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.ExtraWork[ id=" + id + " ]";
    }
    
}
