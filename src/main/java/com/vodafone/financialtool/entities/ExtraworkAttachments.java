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
@Table(name = "extrawork_attachments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExtraworkAttachments.findAll", query = "SELECT e FROM ExtraworkAttachments e")
    , @NamedQuery(name = "ExtraworkAttachments.findByAttachmentId", query = "SELECT e FROM ExtraworkAttachments e WHERE e.attachmentId = :attachmentId")
    , @NamedQuery(name = "ExtraworkAttachments.findByAttachmentName", query = "SELECT e FROM ExtraworkAttachments e WHERE e.attachmentName = :attachmentName")
    , @NamedQuery(name = "ExtraworkAttachments.findByAttachmentLocation", query = "SELECT e FROM ExtraworkAttachments e WHERE e.attachmentLocation = :attachmentLocation")
    , @NamedQuery(name = "ExtraworkAttachments.findByUploadedOn", query = "SELECT e FROM ExtraworkAttachments e WHERE e.uploadedOn = :uploadedOn")})
public class ExtraworkAttachments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "attachment_id")
    private Long attachmentId;
    @Size(max = 2147483647)
    @Column(name = "attachment_name")
    private String attachmentName;
    @Size(max = 2147483647)
    @Column(name = "attachment_location")
    private String attachmentLocation;
    @Column(name = "uploaded_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedOn;
    @JoinColumn(name = "extrawork_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExtraWork extraworkId;
    @JoinColumn(name = "uploaded_by", referencedColumnName = "user_name")
    @ManyToOne
    private Users uploadedBy;

    public ExtraworkAttachments() {
    }

    public ExtraworkAttachments(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentLocation() {
        return attachmentLocation;
    }

    public void setAttachmentLocation(String attachmentLocation) {
        this.attachmentLocation = attachmentLocation;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public ExtraWork getExtraworkId() {
        return extraworkId;
    }

    public void setExtraworkId(ExtraWork extraworkId) {
        this.extraworkId = extraworkId;
    }

    public Users getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Users uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attachmentId != null ? attachmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExtraworkAttachments)) {
            return false;
        }
        ExtraworkAttachments other = (ExtraworkAttachments) object;
        if ((this.attachmentId == null && other.attachmentId != null) || (this.attachmentId != null && !this.attachmentId.equals(other.attachmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.entities.ExtraworkAttachments[ attachmentId=" + attachmentId + " ]";
    }
    
}
