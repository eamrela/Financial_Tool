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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "email_notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmailNotification.findAll", query = "SELECT e FROM EmailNotification e")
    , @NamedQuery(name = "EmailNotification.findById", query = "SELECT e FROM EmailNotification e WHERE e.id = :id")
    , @NamedQuery(name = "EmailNotification.findByEmailSubject", query = "SELECT e FROM EmailNotification e WHERE e.emailSubject = :emailSubject")
    , @NamedQuery(name = "EmailNotification.findByEmailBody", query = "SELECT e FROM EmailNotification e WHERE e.emailBody = :emailBody")
    , @NamedQuery(name = "EmailNotification.findByEmailTo", query = "SELECT e FROM EmailNotification e WHERE e.emailTo = :emailTo")
    , @NamedQuery(name = "EmailNotification.findByEmailSent", query = "SELECT e FROM EmailNotification e WHERE e.emailSent = :emailSent")})
public class EmailNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "email_subject")
    private String emailSubject;
    @Size(max = 2147483647)
    @Column(name = "email_body")
    private String emailBody;
    @Size(max = 2147483647)
    @Column(name = "email_to")
    private String emailTo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "email_sent")
    private boolean emailSent;

    public EmailNotification() {
    }

    public EmailNotification(Long id) {
        this.id = id;
    }

    public EmailNotification(Long id, boolean emailSent) {
        this.id = id;
        this.emailSent = emailSent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
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
        if (!(object instanceof EmailNotification)) {
            return false;
        }
        EmailNotification other = (EmailNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.financialtool.beans.EmailNotification[ id=" + id + " ]";
    }
    
}
