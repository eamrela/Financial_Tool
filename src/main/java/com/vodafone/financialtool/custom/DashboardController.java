/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.entities.AspExtraworkGrn;
import com.vodafone.financialtool.entities.AspExtraworkPo;
import com.vodafone.financialtool.entities.AspServiceGrn;
import com.vodafone.financialtool.entities.AspServicePo;
import com.vodafone.financialtool.entities.CreditNote;
import com.vodafone.financialtool.entities.CustomerExtraworkInvoice;
import com.vodafone.financialtool.entities.CustomerExtraworkPo;
import com.vodafone.financialtool.entities.CustomerServiceInvoice;
import com.vodafone.financialtool.entities.CustomerServicePo;
import com.vodafone.financialtool.entities.ExtraWork;
import com.vodafone.financialtool.entities.JvReporting;
import com.vodafone.financialtool.entities.Penalty;
import com.vodafone.financialtool.entities.TimeReporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;

/**
 *
 * @author eamrela
 */
@Named("dashboardController")
@SessionScoped
public class DashboardController implements Serializable {
    
    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DateTime now;
    private Date startMonth;
    //<editor-fold defaultstate="collapsed" desc="TopFigures">
    private Double netSales;
    private List<CustomerServiceInvoice> netSalesService;
    private List<CustomerExtraworkInvoice> netSalesExtra;
    
    
    private Double costOfSales;
    private List<AspServiceGrn> costOfSalesService;
    private List<AspExtraworkGrn> costOfSalesExtra;
    private List<JvReporting> costOfSalesJV;
    private List<TimeReporting> costOfSalesTR;
    private Double margin;
    private Double marginPercent;
    
    private String netSalesTrend;
    private String costOfSalesTrend;
    private String marginTrend;
    private String marginPercentTrend;
    
    private List<CreditNote> creditNotesVF;
    private List<CreditNote> creditNotesASP;
    private List<Penalty> penalitiesVF;
    private List<Penalty> penalitiesASP;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Financial Analysis">
    private TreeMap<Integer,Double> netSalesAnalysis = new TreeMap<>();
    private TreeMap<Integer,Double> costOfSalesAnalysis = new TreeMap<>();
    private String netSalesDataset;
    private String costOfSalesDataset;
    private List<Object[]> topNetSalesAchievers = new ArrayList<>();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CommittedCost">
    private List<Object[]> aspCommittedCost = new ArrayList<>();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Summary Reports">
    private List<ExtraWork> extraWorkDetails; 
    private List<ExtraWorkWithoutPo> extraWorkTotal; 
    private List<AspExtraworkPo> grnDeservedDetailsExtrawork; 
    private List<AspServicePo> grnDeservedDetailsService; 
    private List<AspGrnDeserved> grnDeservedTotal; 
    private List<CustomerExtraworkPo> mdDeservedDetailsExtrawork; 
    private List<CustomerServicePo> mdDeservedDetailsService; 
    private Double mdDeservedTotal; 
    private Double invoiceDeservedTotal; 
//</editor-fold>
    public void initialize(){
        if(startMonth==null){
        now = DateTime.now();
        }else{
            now = new DateTime(startMonth);
        }
        getCosAndNS();
        calculateMargin();
        calculateFinancialAnalysis();
        calculateTopAchievers();
        calculateCommittedCost();
        calculateCNAndPN();
    }

   
    
    
    
   

    
    
    
    
    
    
    
    
    
    
    public void calculateCNAndPN(){
        creditNotesVF = em.createNativeQuery("select * from credit_note where cn_owner='Vodafone' "
                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true ", CreditNote.class).getResultList();
        creditNotesASP = em.createNativeQuery("select * from credit_note where cn_owner!='Vodafone' "
                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settlement_po is not null ", CreditNote.class).getResultList();
        penalitiesVF = em.createNativeQuery("select * from penalty where pn_owner='Vodafone' and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true ", Penalty.class).getResultList();
        penalitiesASP = em.createNativeQuery("select * from penalty where pn_owner!='Vodafone' and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settlement_po is not null  ", Penalty.class).getResultList();
    }

    public void getCosAndNS(){
        try{
        netSales = ((BigDecimal) em.createNativeQuery(   "select sum(ns) " +
                                                    "from  " +
                                                    "(select COALESCE(sum(invoice_value),0) ns " +
                                                    "from customer_service_invoice " +
                                                    "where invoice_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' " 
                                                + " and po_numer in (select po_number from customer_service_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) "+
                                                    "union " +
                                                    "select COALESCE(sum(invoice_value),0) ns " +
                                                    "from customer_extrawork_invoice " +
                                                    "where invoice_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                                 + " and po_numer in (select po_number from customer_extrawork_po where "
                                                            + " network_name !='Support Network' and (early_start is null or early_start=false) ) "
                                + " union "
                                + "select COALESCE(sum(cn_value),0)*-1 ns from credit_note where cn_owner='Vodafone' "
                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true "
                          + " union "
                                + "select COALESCE(sum(pn_value),0)*-1 ns from penalty where pn_owner='Vodafone' "
                + " and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true "
                                + " ) a").getSingleResult()).doubleValue();
        }catch(Exception e){
            e.printStackTrace();
            netSales = 0.0;
        }
        
         try{
        costOfSales = ((BigDecimal) em.createNativeQuery( " select sum(cos) " +
                                            " from  " +
                                            " (select COALESCE(sum(grn_value),0) cos " +
                                            " from asp_service_grn " +
                                            " where grn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' " +
                                " and po_number in (select po_number from asp_service_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) "+
                                            " union " +
                                            " select COALESCE(sum(grn_value),0) cos " +
                                            " from asp_extrawork_grn " +
                                            " where grn_date between "
                + " '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"'"+
                                 " and po_number in (select po_number from asp_extrawork_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) "+
                                            " union " +
                                            " select COALESCE(sum(jv_value),0) cos " +
                                            " from jv_reporting " +
                                            " where jv_date between "
                + " '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"'"+
                                            " union " +
                                            " select COALESCE(sum(reporting_value),0) cos " +
                                            " from time_reporting " +
                                            " where reporting_date between "
                + " '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"'"
                                
//                                + " union "
//                                + "select COALESCE(sum(cn_value),0)*-1 ns from credit_note where cn_owner!='Vodafone' "
//                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
//                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settlement_po is not null  "
//                                + " union "
//                                + "select COALESCE(sum(pn_value),0)*-1 ns from penalty where pn_owner!='Vodafone' "
//                + " and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
//                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settlement_po is not null  "
                                + " ) a").getSingleResult()).doubleValue();
        }catch(Exception e){
            e.printStackTrace();
            costOfSales = 0.0;
        }
    }
    
    public void calculateMargin(){
        if(costOfSales!=0.0 && netSales!=0.0){
            margin = netSales-costOfSales;
            marginPercent = (margin/netSales);
        }else if(costOfSales==0.0){
            margin = netSales;
            marginPercent = 1.0;
        }
    }
    
    public void calculateFinancialAnalysis(){
        try{
            List<Object[]> netSalesList = em.createNativeQuery(
                    " select month_no::integer,round(sum(ns)/1000000,2) " +
                    " from  " +
                    " (select extract(month from invoice_date) month_no,sum(invoice_value) ns " +
                    " from customer_service_invoice " +
                    " where invoice_date between '"+sdf.format(now.monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate())+"' " +
                    " and po_numer in (select po_number from customer_service_po where "
                                                            + " network_name !='Support Network' and (early_start is null or early_start=false) ) "+
                    " group by month_no " +
                    " union " +
                    " select extract(month from invoice_date) month_no,sum(invoice_value) ns " +
                    " from customer_extrawork_invoice " +
                    " where invoice_date between '"+sdf.format(now.monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate())+"'  " +
                     " and po_numer in (select po_number from customer_extrawork_po where "
                                                            + " network_name !='Support Network' and (early_start is null or early_start=false) ) "
                                + " group by month_no "+
                    " union "
                    + "select extract(month from cn_date) month_no , COALESCE(sum(cn_value),0)*-1 ns "
                    + " from credit_note where cn_owner='Vodafone' "
                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true "
                                            + " group by month_no "+
                     " union "
                    + "select extract(month from pn_date) month_no , COALESCE(sum(pn_value),0)*-1 ns "
                    + " from penalty where pn_owner='Vodafone' "
                + " and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true "
                                            + " group by month_no "
                                + "  ) a " +
                    " group by month_no ").getResultList();
            
            List<Object[]> costOfSalesList = em.createNativeQuery(
                    " select month_no::integer,round(sum(cos)/1000000,2) " +
                    " from " +
                    " (select extract(month from grn_date) month_no,sum(grn_value) cos " +
                    " from asp_service_grn " +
                    " where grn_date between '"+sdf.format(now.monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate())+"'  " +
                     " and po_number in (select po_number from asp_service_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) "+
                    " group by month_no " +
                    " union " +
                    " select extract(month from grn_date) month_no,sum(grn_value) cos " +
                    " from asp_extrawork_grn " +
                    " where grn_date between '"+sdf.format(now.monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate())+"'  " +
                     " and po_number in (select po_number from asp_extrawork_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) "+
                    " group by month_no "
                    + " union "
                    + " select extract(month from jv_date) month_no,sum(jv_value)"
                    + " from jv_reporting "
                    + "where jv_date between '"+sdf.format(now.monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate())+"' "
                    + " and '"+sdf.format(now.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate())+"'  " +
                    " group by month_no "
                    + " union "
                    + " select extract(month from reporting_date) month_no,sum(reporting_value)"
                    + " from time_reporting "
                    + "where reporting_date between '"+sdf.format(now.monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().toDate())+"' "
                    + " and '"+sdf.format(now.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().toDate())+"'  " +
                    " group by month_no "+
//                    " union "
//                    + "select extract(month from cn_date) month_no , COALESCE(sum(cn_value),0)*-1 ns "
//                    + " from credit_note where cn_owner!='Vodafone' "
//                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
//                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true "
//                                            + " group by month_no "+
//                    " union "
//                    + "select extract(month from pn_date) month_no , COALESCE(sum(pn_value),0)*-1 ns "
//                    + " from penalty where pn_owner!='Vodafone' "
//                + " and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
//                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true "
//                                            + " group by month_no "+
                                 ") a " +
                    " group by month_no ").getResultList();
            
            for (Object[] netSalesList1 : netSalesList) {
                netSalesAnalysis.put((Integer)netSalesList1[0], ((BigDecimal)netSalesList1[1]).doubleValue());
            }
            for (Object[] costOfSalesList1 : costOfSalesList) {
                costOfSalesAnalysis.put((Integer)costOfSalesList1[0], ((BigDecimal)costOfSalesList1[1]).doubleValue());
            }
            netSalesDataset = "";
            costOfSalesDataset = "";
            for (int i = 1; i < 13; i++) {
                if(netSalesAnalysis.containsKey(i)){
                    netSalesDataset += netSalesAnalysis.get(i)+",";
                }else{
                    netSalesDataset += "0,";
                }
                if(costOfSalesAnalysis.containsKey(i)){
                     costOfSalesDataset += costOfSalesAnalysis.get(i)+",";
                }else{
                    costOfSalesDataset += "0,";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            netSalesDataset = "0,0,0,0,0,0,0,0,0,0,0,0";
            costOfSalesDataset = "0,0,0,0,0,0,0,0,0,0,0,0";
        }
    }
    
    public void calculateTopAchievers(){
       try{
           topNetSalesAchievers = em.createNativeQuery(
                   " select last_assignment,sum(total_price_vendor) " +
                   " from extra_work " +
                   " where domain_owner_approval is true " +
                   " group by last_assignment " +
                   " limit 5 ").getResultList();
       } catch(Exception e){
           e.printStackTrace();
           topNetSalesAchievers = new ArrayList<>();
       }
    }
     
    public void calculateCommittedCost(){
        try{
           aspCommittedCost = em.createNativeQuery(
                   " select subcontractor,sum(committed_cost) " +
                    " from ( " +
                    " select subcontractor, " +
                    " (( select sum(work_done_value) " +
                    "  from asp_extrawork_work_done " +
                    "  where po_number = asppoextra.po_number) - " +
                    "  ( select sum(grn_value)  " +
                    "  from asp_extrawork_grn " +
                    "  where po_number = asppoextra.po_number)) committed_cost " +
                    " from asp_extrawork_po asppoextra " +
                    " union " +
                    " select subcontractor, " +
                    " (( select sum(work_done_value)  " +
                    "  from asp_service_work_done " +
                    "  where po_number = aspposervice.po_number) - " +
                    "  ( select sum(grn_value)  " +
                    "  from asp_service_grn " +
                    "  where po_number = aspposervice.po_number)) committed_cost " +
                    " from asp_extrawork_po aspposervice " +
                    " union " +
                    " select asp subcontractor,sum(total_price_asp) committed_cost " +
                    " from extra_work " +
                    " where domain_owner_approval is true " +
                    " and correlated is false " +
                    " group by asp) a " +
                    " group by subcontractor " +
                    " having sum(committed_cost) > 0"
           ).getResultList();

        }catch(Exception e){
            e.printStackTrace();
            aspCommittedCost = new ArrayList<>();
        }
    }

    public List<CustomerServiceInvoice> getNetSalesService() {
        netSalesService = em.createNativeQuery("select * " +
                                                "from customer_service_invoice " +
                                                "where invoice_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                 + " and po_numer in (select po_number from customer_service_po where "
                    + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", CustomerServiceInvoice.class).getResultList();
        return netSalesService;
    }

    public List<CustomerExtraworkInvoice> getNetSalesExtra() {
         netSalesExtra = em.createNativeQuery("select * " +
                                                "from customer_extrawork_invoice " +
                                                "where invoice_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                                             + " and po_numer in (select po_number from customer_extrawork_po where "
                            + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", CustomerExtraworkInvoice.class).getResultList();
        return netSalesExtra;
    }

    public List<AspServiceGrn> getCostOfSalesService() {
        costOfSalesService = em.createNativeQuery( "select * " +
                                            " from asp_service_grn " +
                                            " where grn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                                +  " and po_number in (select po_number from asp_service_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", AspServiceGrn.class).getResultList();
        return costOfSalesService;
    }

    public List<JvReporting> getCostOfSalesJV() {
        costOfSalesJV = em.createNativeQuery( "select * " +
                                            " from jv_reporting " +
                                            " where jv_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' ", JvReporting.class).getResultList();
        return costOfSalesJV;
    }

    public List<TimeReporting> getCostOfSalesTR() {
        costOfSalesTR = em.createNativeQuery( "select * " +
                                            " from time_reporting " +
                                            " where reporting_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' ", TimeReporting.class).getResultList();
        return costOfSalesTR;
    }
    
    
    

    public List<AspExtraworkGrn> getCostOfSalesExtra() {
         costOfSalesExtra = em.createNativeQuery( "select * " +
                                            " from asp_extrawork_grn " +
                                            " where grn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"'  "
                                +  " and po_number in (select po_number from asp_extrawork_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", AspExtraworkGrn.class).getResultList();
        return costOfSalesExtra;
    }

    public List<ExtraWorkWithoutPo> getExtraWorkTotal() {
        
        extraWorkTotal = em.createNativeQuery(" select asp,domain_name,sum(total_price_asp) as total_cos " +
                                                " from extra_work " +
                                                " where domain_owner_approval is true " +
                                                " and correlated is false  " +
                                                " group by asp,domain_name ", ExtraWorkWithoutPo.class).getResultList();
        return extraWorkTotal;
    }

    public List<ExtraWork> getExtraWorkDetails() {
          extraWorkDetails = em.createNativeQuery(" select * " +
                                                " from extra_work " +
                                                " where domain_owner_approval is true " +
                                                " and correlated is false  ", ExtraWork.class).getResultList();
        return extraWorkDetails;
    }
    
    public List<AspGrnDeserved> getGrnDeservedTotal() {
          //<editor-fold defaultstate="collapsed" desc="comment">
        
        grnDeservedTotal = em.createNativeQuery("select * from (\n" +
" select subcontractor,(sum(work_done)-sum(grn_created)) grn_deserved\n" +
" from ( \n" +
" select po_number,subcontractor,(po_value - remaining_from_po) work_done,COALESCE((select sum(grn_value) from asp_service_grn where po_number = ewspo.po_number group by po_number),0) grn_created\n" +
"  from asp_service_po ewspo\n" +
"union \n" +
" select po_number,subcontractor,(po_value - remaining_from_po) work_done,COALESCE((select sum(grn_value) from asp_extrawork_grn where po_number = ewspo.po_number group by po_number),0) grn_created\n" +
"  from asp_extrawork_po ewspo\n" +
"  ) a\n" +
" group by subcontractor\n" +
" ) b \n" +
" where grn_deserved > 100", AspGrnDeserved.class).getResultList();
            //</editor-fold>
        return grnDeservedTotal;
    }

    public List<AspExtraworkPo> getGrnDeservedDetailsExtrawork() {
        grnDeservedDetailsExtrawork = em.createNativeQuery(
                                            "  select * \n" +
" from asp_extrawork_po spo \n" +
" where ((po_value - remaining_from_po) - COALESCE((select sum(grn_value) from asp_extrawork_grn where po_number = spo.po_number group by po_number),0))>100 " 
                                            , AspExtraworkPo.class).getResultList();
        return grnDeservedDetailsExtrawork;
    }

    public List<AspServicePo> getGrnDeservedDetailsService() {
        grnDeservedDetailsService = em.createNativeQuery(
                                            "  select * \n" +
" from asp_service_po spo \n" +
" where ((po_value - remaining_from_po) - COALESCE((select sum(grn_value) from asp_service_grn where po_number = spo.po_number group by po_number),0))>100 " 
                                            , AspExtraworkPo.class).getResultList();
        return grnDeservedDetailsService;
    }

    public List<CustomerExtraworkPo> getMdDeservedDetailsExtrawork() {
        mdDeservedDetailsExtrawork = em.createNativeQuery(
                "select * from customer_extrawork_po ewpo \n" +
"where (\n" +
"(po_value - remaining_from_po) - \n" +
"COALESCE ((select sum(md_value) \n" +
"from customer_extrawork_md where po_number =  ewpo.po_number group by po_number),0)) > 0  \n" +
"or po_number in  ( select po_number from customer_extrawork_md  \n" +
"			group by po_number \n" +
"			having (sum(md_value) -  \n" +
"			COALESCE((select sum(invoice_value)  \n" +
"			from customer_extrawork_invoice where po_numer = ewpo.po_number group by po_numer),0)) > 0 )",CustomerExtraworkPo.class).getResultList();
        return mdDeservedDetailsExtrawork; 
    }

    public List<CustomerServicePo> getMdDeservedDetailsService() {
         mdDeservedDetailsService = em.createNativeQuery(
                "select * from customer_service_po ewpo \n" +
"where (\n" +
"(po_value - remaining_from_po) - \n" +
"COALESCE ((select sum(md_value) \n" +
"from customer_service_md where po_number =  ewpo.po_number group by po_number),0)) > 0  \n" +
"or po_number in  ( select po_number from customer_service_md  \n" +
"			group by po_number \n" +
"			having (sum(md_value) -  \n" +
"			COALESCE((select sum(invoice_value)  \n" +
"			from customer_service_invoice where po_numer = ewpo.po_number group by po_numer),0)) > 0 )",CustomerServicePo.class).getResultList();
        return mdDeservedDetailsService; 
    }

    public Double getInvoiceDeservedTotal() {
        invoiceDeservedTotal = ((BigDecimal)em.createNativeQuery(
                            "select   sum(invoice_deserved) invoice_deserved " +
                            "from ( " +
                            "select COALESCE ((sum(md_value)-sum(invoice_created)),0) invoice_deserved " +
                            "from ( " +
                            "select COALESCE (sum(md_value),0) md_value, " +
                            "       COALESCE ((select sum(invoice_value) from customer_extrawork_invoice where po_numer = ewmd.po_number group by po_numer),0) invoice_created " +
                            "from customer_extrawork_md ewmd " +
                            "group by po_number " +
                            "union " +
                            "select COALESCE (sum(md_value),0) md_value, " +
                            "       COALESCE ((select sum(invoice_value) from customer_service_invoice where po_numer = smd.po_number group by po_numer),0) invoice_created " +
                            "from customer_service_md smd " +
                            "group by po_number) a )b " +
                            "where invoice_deserved > 0 ").getSingleResult()).doubleValue();
        return invoiceDeservedTotal;
    }

    public Double getMdDeservedTotal() {
        mdDeservedTotal = ((BigDecimal)em.createNativeQuery(
                            "select COALESCE( sum(md_deserved),0) md_deserved " +
                            "from ( " +
                            "select (sum(actual_work_done)-sum(md_created)) md_deserved " +
                            "from ( " +
                            "select (po_value - remaining_from_po) actual_work_done, " +
                            "	COALESCE( (select sum(md_value) from customer_extrawork_md where po_number = ewpo.po_number group by po_number),0) md_created " +
                            "from customer_extrawork_po ewpo " +
                            "union " +
                            "select 	(po_value - remaining_from_po) actual_work_done, " +
                            "	COALESCE( (select sum(md_value) from customer_service_md where po_number = ewspo.po_number group by po_number),0) md_created " +
                            "from customer_service_po ewspo) a )b " +
                            "where md_deserved > 0").getSingleResult()).doubleValue();
        return mdDeservedTotal;
    }
    
    
    
    
    
    
    
    //<editor-fold defaultstate="collapsed" desc="Setter/Getter">
    public List<Object[]> getAspCommittedCost() {
        return aspCommittedCost;
    }

    public Date getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Date startMonth) {
        this.startMonth = startMonth;
    }
    
    public List<CreditNote> getCreditNotesVF() {
        return creditNotesVF;
    }

    public List<CreditNote> getCreditNotesASP() {
        return creditNotesASP;
    }

    public List<Penalty> getPenalitiesVF() {
        return penalitiesVF;
    }

    public List<Penalty> getPenalitiesASP() {
        return penalitiesASP;
    }

    

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<Object[]> getTopNetSalesAchievers() {
        return topNetSalesAchievers;
    }

    
    public String getCostOfSalesDataset() {
        return costOfSalesDataset;
    }

    public String getNetSalesDataset() {
        return netSalesDataset;
    }

    
    public Double getNetSales() {
        return netSales;
    }

    public void setNetSales(Double netSales) {
        this.netSales = netSales;
    }

    public Double getCostOfSales() {
        return costOfSales;
    }

    public void setCostOfSales(Double costOfSales) {
        this.costOfSales = costOfSales;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getMarginPercent() {
        return marginPercent;
    }

    public void setMarginPercent(Double marginPercent) {
        this.marginPercent = marginPercent;
    }

    public String getNetSalesTrend() {
        return netSalesTrend;
    }

    public void setNetSalesTrend(String netSalesTrend) {
        this.netSalesTrend = netSalesTrend;
    }

    public String getCostOfSalesTrend() {
        return costOfSalesTrend;
    }

    public void setCostOfSalesTrend(String costOfSalesTrend) {
        this.costOfSalesTrend = costOfSalesTrend;
    }

    public String getMarginTrend() {
        return marginTrend;
    }

    public void setMarginTrend(String marginTrend) {
        this.marginTrend = marginTrend;
    }

    public String getMarginPercentTrend() {
        return marginPercentTrend;
    }

    public void setMarginPercentTrend(String marginPercentTrend) {
        this.marginPercentTrend = marginPercentTrend;
    }
    
    //</editor-fold>
}
