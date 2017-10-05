/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.entities.AspExtraworkGrn;
import com.vodafone.financialtool.entities.AspExtraworkPo;
import com.vodafone.financialtool.entities.AspServicePo;
import com.vodafone.financialtool.entities.CustomerExtraworkInvoice;
import com.vodafone.financialtool.entities.CustomerExtraworkPo;
import com.vodafone.financialtool.entities.CustomerServicePo;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Named("simulatorController")
@SessionScoped
public class SimulatorController implements Serializable{
    
    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Double NetSales=0.0;
    private Double CostOfSales=0.0;
    private Double margin=0.0;
    private Double marginPercentage=0.0;
    private Date startDate;
    private Date endDate;
    private List<AspServicePo> aspService;
    private List<CustomerServicePo> customerService;
    private List<AspExtraworkPo> aspExtrawork;
    private List<CustomerExtraworkPo> customerExtrawork;
    private List<AspExtraworkGrn> aspExtraworkGrn;
    private List<CustomerExtraworkInvoice> customerExtraworkInvoice;

    public void refreshData(){
        if(startDate!=null && endDate!=null){
            getAspService();
            getCustomerService();
        }else{
            JsfUtil.addErrorMessage("You need to enter dates");
        }
        
    }

    public List<AspServicePo> getAspService() {
        if(startDate!=null && endDate!=null){
        aspService = em.createNativeQuery("select * from asp_service_po "
                + " where po_date between '"+sdf.format(startDate)+"' and '"+sdf.format(endDate)+"' ", AspServicePo.class).getResultList();
        }
        return aspService;
    }

    public List<CustomerServicePo> getCustomerService() {
        if(startDate!=null && endDate!=null){
        customerService = em.createNativeQuery("select * from customer_service_po "
                + " where po_date between '"+sdf.format(startDate)+"' and '"+sdf.format(endDate)+"' ", CustomerServicePo.class).getResultList();
        }
        return customerService;
    }

    public List<AspExtraworkPo> getAspExtrawork() {
        if(startDate!=null && endDate!=null){
         aspExtrawork = em.createNativeQuery("select * from asp_extrawork_po "
                 + " where po_date between '"+sdf.format(startDate)+"' and '"+sdf.format(endDate)+"'", AspExtraworkPo.class).getResultList();
        }
        return aspExtrawork;
    }

    public List<CustomerExtraworkPo> getCustomerExtrawork() {
        if(startDate!=null && endDate!=null){
         customerExtrawork = em.createNativeQuery("select * from customer_extrawork_po "
                 + " where po_date between '"+sdf.format(startDate)+"' and '"+sdf.format(endDate)+"'", CustomerExtraworkPo.class).getResultList();
        }
        return customerExtrawork;
    }

    public List<AspExtraworkGrn> getAspExtraworkGrn() {
        return aspExtraworkGrn;
    }

    public List<CustomerExtraworkInvoice> getCustomerExtraworkInvoice() {
        return customerExtraworkInvoice;
    }
    
    
    
    
    
    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    
    public Double getNetSales() {
        return NetSales;
    }

    public void setNetSales(Double NetSales) {
        this.NetSales = NetSales;
    }

    public Double getCostOfSales() {
        return CostOfSales;
    }

    public void setCostOfSales(Double CostOfSales) {
        this.CostOfSales = CostOfSales;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getMarginPercentage() {
        return marginPercentage;
    }

    public void setMarginPercentage(Double marginPercentage) {
        this.marginPercentage = marginPercentage;
    }
    
    
    
    
    
    
    
}
