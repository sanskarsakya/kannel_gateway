/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author puzansakya
 */
@Entity
@Table(name = "tbl_reports")
@NamedQueries({
    @NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")})
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id")
    private Integer id;    
    @Size(min = 1, max = 15)
    @Column(name = "msisdn")
    private String msisdn;    
    @Size(min = 1, max = 65535)
    @Column(name = "message")
    private String message;
    @Column(name = "sent_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentTime;
    @Column(name = "report_received_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportReceivedTime;    
    @Size(min = 1, max = 50)
    @Column(name = "delivery_report")
    private String deliveryReport;

    public Report() {
    }

    public Report(Integer id) {
        this.id = id;
    }

    public Report(Integer id, String msisdn, String message, String deliveryReport) {
        this.id = id;
        this.msisdn = msisdn;
        this.message = message;
        this.deliveryReport = deliveryReport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Date getReportReceivedTime() {
        return reportReceivedTime;
    }

    public void setReportReceivedTime(Date reportReceivedTime) {
        this.reportReceivedTime = reportReceivedTime;
    }

    public String getDeliveryReport() {
        return deliveryReport;
    }

    public void setDeliveryReport(String deliveryReport) {
        this.deliveryReport = deliveryReport;
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
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.puzan.smsfinal.Entity.Report[ id=" + id + " ]";
    }
    
}
