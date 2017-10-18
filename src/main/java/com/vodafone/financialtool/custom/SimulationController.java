/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.controllers.UsersController;
import com.vodafone.financialtool.entities.AspExtraworkGrn;
import com.vodafone.financialtool.entities.AspExtraworkPo;
import com.vodafone.financialtool.entities.AspServiceGrn;
import com.vodafone.financialtool.entities.AspServicePo;
import com.vodafone.financialtool.entities.CosSimulation;
import com.vodafone.financialtool.entities.CreditNote;
import com.vodafone.financialtool.entities.CustomerExtraworkInvoice;
import com.vodafone.financialtool.entities.CustomerExtraworkMd;
import com.vodafone.financialtool.entities.CustomerExtraworkPo;
import com.vodafone.financialtool.entities.CustomerServiceInvoice;
import com.vodafone.financialtool.entities.CustomerServiceMd;
import com.vodafone.financialtool.entities.CustomerServicePo;
import com.vodafone.financialtool.entities.ExtraWork;
import com.vodafone.financialtool.entities.JvReporting;
import com.vodafone.financialtool.entities.NsSimulation;
import com.vodafone.financialtool.entities.Penalty;
import com.vodafone.financialtool.entities.Subcontractors;
import com.vodafone.financialtool.entities.TimeReporting;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author eamrela
 */
@Named("simulationController")
@SessionScoped
public class SimulationController implements Serializable{
   
        
    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @Inject
    private UsersController usersController;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DateTime now;
    private Date startMonth;
    private Integer counterCos;
    private Integer counterNs;
    private Boolean VFDataCalled=false;

    //<editor-fold defaultstate="collapsed" desc="ASP">
    private Subcontractors asp;
    private List<AspServicePo> AspremainingFromPOService;
    private List<AspExtraworkPo> AspremainingFromPOExtrawork;
    private List<AspServicePo> AspgrnDeservedService;
    private List<AspExtraworkPo> AspgrnDeservedExtrawork;
    private AspServicePo selectedAspServicePO;
    private AspExtraworkPo selectedAspExtraworkPO;
    
    
    private Double AspremainingFromPO=0.0;
    private Double Aspgrndeserved=0.0;
    private Double AsppoReceived=0.0;
    private Double AspcreditNotes=0.0;
    private Double Asppenalty=0.0;
    
    
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Customer">
    
    private List<CustomerServicePo> VFremainingFromPOService;
    private List<CustomerExtraworkPo> VFremainingFromPOExtrawork;
    private List<CustomerServicePo> VFmdDeservedService;
    private List<CustomerExtraworkPo> VFmdDeservedExtrawork;
    private List<CustomerServicePo> VFinvoiceDeservedService;
    private List<CustomerExtraworkPo> VFinvoiceDeservedExtrawork;
    private List<CreditNote> VFcreditNotesDetails;
    private List<Penalty> VFpenaltyDetails;
    private CustomerServicePo selectedCustomerPoService;
    private CustomerExtraworkPo selectedCustomerPoExtrawork;
    private Double VFremainingFromPO=0.0;
    private Double VFmdDeserved=0.0;
    private Double VFinvoiceDeserved=0.0;
    private Double VFcreditNotes=0.0;
    private Double VFpenalty=0.0;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Simulation">
    private List<NsSimulation> NSRecord;
    private List<CosSimulation> COSRecord;
    private Double totalNS;
    private Double totalCOS;
    private Double totalUM;
    private Double totalUMPercent;
//</editor-fold>
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    
    //<editor-fold defaultstate="collapsed" desc="ASP">
    
    public List<AspServicePo> getAspremainingFromPOService() {
        if(asp!=null){
           AspremainingFromPOService = em.createNativeQuery(" select * from asp_service_po "
                   + " where remaining_from_po > 100 and subcontractor='"+asp.getSubcontractorName()+"' and po_number not in ("+getCOSIds()+") ", AspServicePo.class).getResultList(); 
           for (AspServicePo x : AspremainingFromPOService) {
                AspremainingFromPO+=x.getRemainingFromPo();
            }
        }
        return AspremainingFromPOService;
    }

    public void setAspremainingFromPOService(List<AspServicePo> remainingFromPOService) {
        this.AspremainingFromPOService = remainingFromPOService;
    }

    public List<AspExtraworkPo> getAspremainingFromPOExtrawork() {
        if(asp!=null){
            AspremainingFromPOExtrawork = em.createNativeQuery(" select * from asp_extrawork_po "
                   + " where remaining_from_po > 100 and subcontractor='"+asp.getSubcontractorName()+"' and po_number not in ("+getCOSIds()+") ", AspExtraworkPo.class).getResultList();
            for (AspExtraworkPo x : AspremainingFromPOExtrawork) {
                AspremainingFromPO+=x.getRemainingFromPo();
            }
        }
        return AspremainingFromPOExtrawork;
    }

    public void setAspremainingFromPOExtrawork(List<AspExtraworkPo> remainingFromPOExtrawork) {
        this.AspremainingFromPOExtrawork = remainingFromPOExtrawork;
    }

    public List<AspServicePo> getAspgrnDeservedService() {
        if(asp!=null){
          AspgrnDeservedService = em.createNativeQuery(
                  "  select * \n" +
                  " from asp_service_po spo \n" +
                  " where ((po_value - remaining_from_po) - "
                          + "COALESCE((select sum(grn_value) from asp_service_grn where po_number = spo.po_number group by po_number),0))>100 "
                          + " and subcontractor='"+asp.getSubcontractorName()+"' "
                                  + "  and po_number not in ("+getCOSIds()+") " 
                                            , AspServicePo.class).getResultList(); 
          for (AspServicePo x : AspgrnDeservedService) {
                Aspgrndeserved+=x.getGrnDeserved();
            }
        }
        return AspgrnDeservedService;
    }

    public void setAspgrnDeservedService(List<AspServicePo> grnDeservedService) {
        this.AspgrnDeservedService = grnDeservedService;
    }

    public List<AspExtraworkPo> getAspgrnDeservedExtrawork() {
        if(asp!=null){
          AspgrnDeservedExtrawork = em.createNativeQuery("  select * \n" +
                  " from asp_extrawork_po spo \n" +
                  " where ((po_value - remaining_from_po) - "
                          + "COALESCE((select sum(grn_value) from asp_extrawork_grn where po_number = spo.po_number group by po_number),0))>100 "
                          + " and subcontractor='"+asp.getSubcontractorName()+"' "
                                  + "  and po_number not in ("+getCOSIds()+") " , AspExtraworkPo.class).getResultList();  
          for (AspExtraworkPo x : AspgrnDeservedExtrawork) {
                Aspgrndeserved+=x.getGrnDeserved();
            }
        }
        return AspgrnDeservedExtrawork;
    }

    public void setAspgrnDeservedExtrawork(List<AspExtraworkPo> grnDeservedExtrawork) {
        this.AspgrnDeservedExtrawork = grnDeservedExtrawork;
    }

    public void setAsp(Subcontractors asp)  {
        this.asp = asp;
        clearValuesASP();
        callValuesASP();
    }
    
     public Subcontractors getAsp() {
        return asp;
    }

    public Double getAspremainingFromPO() {
        return AspremainingFromPO;
    }

    public void setAspremainingFromPO(Double remainingFromPO) {
        this.AspremainingFromPO = remainingFromPO;
    }

    public Double getAspgrndeserved() {
        return Aspgrndeserved;
    }

    public void setAspgrndeserved(Double grndeserved) {
        this.Aspgrndeserved = grndeserved;
    }

    public Double getAsppoReceived() {
        return AsppoReceived;
    }

    public void setAsppoReceived(Double poReceived) {
        this.AsppoReceived = poReceived;
    }

    public Double getAspcreditNotes() {
        return AspcreditNotes;
    }

    public void setAspcreditNotes(Double creditNotes) {
        this.AspcreditNotes = creditNotes;
    }

    public Double getAsppenalty() {
        return Asppenalty;
    }

    public void setAsppenalty(Double penalty) {
        this.Asppenalty = penalty;
    }

   
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="VF">
    
    public List<CustomerServicePo> getVFremainingFromPOService() {
        if(!VFDataCalled){
        VFremainingFromPOService = em.createNativeQuery(" select * from customer_service_po "
                   + " where remaining_from_po > 100 ", CustomerServicePo.class).getResultList(); 
           for (CustomerServicePo x : VFremainingFromPOService) {
                VFremainingFromPO+=x.getRemainingFromPo();
            }
        }
        return VFremainingFromPOService;
    }

    public void setVFremainingFromPOService(List<CustomerServicePo> remainingFromPOService) {
        this.VFremainingFromPOService = remainingFromPOService;
    }

    public List<CustomerExtraworkPo> getVFremainingFromPOExtrawork() {
        if(!VFDataCalled){
        VFremainingFromPOExtrawork = em.createNativeQuery(" select * from customer_extrawork_po "
                   + " where remaining_from_po > 100 ", CustomerExtraworkPo.class).getResultList(); 
           for (CustomerExtraworkPo x : VFremainingFromPOExtrawork) {
                VFremainingFromPO+=x.getRemainingFromPo();
            }
        }
        return VFremainingFromPOExtrawork;
    }

    public void setVFremainingFromPOExtrawork(List<CustomerExtraworkPo> remainingFromPOExtrawork) {
        this.VFremainingFromPOExtrawork = remainingFromPOExtrawork;
    }

    public List<CreditNote> getVFcreditNotesDetails() {
        if(!VFDataCalled){
         VFcreditNotesDetails = em.createNativeQuery("  select * \n" +
                  " from credit_note  \n" +
                  " where cn_owner='Vodafone' and settled=false" , CreditNote.class).getResultList();  
          for (CreditNote x : VFcreditNotesDetails) {
                VFcreditNotes+=x.getCnValue();
            }
        }
        return VFcreditNotesDetails;
    }

    public void setVFcreditNotesDetails(List<CreditNote> creditNotesDetails) {
        this.VFcreditNotesDetails = creditNotesDetails;
    }

    public List<Penalty> getVFpenaltyDetails() {
        if(!VFDataCalled){
         VFpenaltyDetails = em.createNativeQuery("  select * \n" +
                  " from penalty \n" +
                  " where pn_owner='Vodafone' and settled=false" , Penalty.class).getResultList();  
          for (Penalty x : VFpenaltyDetails) {
                VFpenalty+=x.getPnValue();
            }
        }
        return VFpenaltyDetails;
    }

    public void setVFpenaltyDetails(List<Penalty> penaltyDetails) {
        this.VFpenaltyDetails = penaltyDetails;
    }

    public Double getVFremainingFromPO() {
        return VFremainingFromPO;
    }

    public void setVFremainingFromPO(Double remainingFromPO) {
        this.VFremainingFromPO = remainingFromPO;
    }
    
    public Double getVFmdDeserved() {
        return VFmdDeserved;
    }

    public void setVFmdDeserved(Double mdDeserved) {
        this.VFmdDeserved = mdDeserved;
    }

    public Double getVFinvoiceDeserved() {
        return VFinvoiceDeserved;
    }

    public void setVFinvoiceDeserved(Double invoiceDeserved) {
        this.VFinvoiceDeserved = invoiceDeserved;
    }

    public Double getVFcreditNotes() {
        return VFcreditNotes;
    }

    public void setVFcreditNotes(Double creditNotes) {
        this.VFcreditNotes = creditNotes;
    }

    public Double getVFpenalty() {
        return VFpenalty;
    }

    public void setVFpenalty(Double penalty) {
        this.VFpenalty = penalty;
    }

    public List<CustomerServicePo> getVFmdDeservedService() {
        if(!VFDataCalled){
        VFmdDeservedService = em.createNativeQuery(
                "select * from customer_service_po ewpo \n" +
                "where (\n" +
                "(po_value - remaining_from_po) - \n" +
                "COALESCE ((select sum(md_value) \n" +
                "from customer_service_md where po_number =  ewpo.po_number group by po_number),0)) > 0  \n",CustomerServicePo.class).getResultList();
        for (CustomerServicePo x : VFmdDeservedService) {
                VFmdDeserved+=x.getMdDeserved();
            }
        }
        return VFmdDeservedService;
    }

    public void setVFmdDeservedService(List<CustomerServicePo> mdDeservedService) {
        this.VFmdDeservedService = mdDeservedService;
    }

    public List<CustomerExtraworkPo> getVFmdDeservedExtrawork() {
        if(!VFDataCalled){
         VFmdDeservedExtrawork = em.createNativeQuery(
                "select * from customer_extrawork_po ewpo \n" +
                "where (\n" +
                "(po_value - remaining_from_po) - \n" +
                "COALESCE ((select sum(md_value) \n" +
                "from customer_extrawork_md where po_number =  ewpo.po_number group by po_number),0)) > 0  \n",CustomerExtraworkPo.class).getResultList();
        for (CustomerExtraworkPo x : VFmdDeservedExtrawork) {
                VFmdDeserved+=x.getMdDeserved();
            }
        }
        return VFmdDeservedExtrawork;
    }

    public void setVFmdDeservedExtrawork(List<CustomerExtraworkPo> mdDeservedExtrawork) {
        this.VFmdDeservedExtrawork = mdDeservedExtrawork;
    }

    public List<CustomerServicePo> getVFinvoiceDeservedService() {
        if(!VFDataCalled){
         VFinvoiceDeservedService = em.createNativeQuery(
                "select * from customer_service_po ewpo \n" +
                "where po_number in  ( select po_number from customer_service_md  \n" +
"			group by po_number \n" +
"			having (sum(md_value) -  \n" +
"			COALESCE((select sum(invoice_value)  \n" +
"			from customer_service_invoice where po_numer = ewpo.po_number group by po_numer),0)) > 0 )",CustomerServicePo.class).getResultList();
        for (CustomerServicePo x : VFinvoiceDeservedService) {
                VFinvoiceDeserved+=x.getInvoiceDeserved();
            }
        }
        return VFinvoiceDeservedService;
    }

    public void setvinvoiceDeservedService(List<CustomerServicePo> invoiceDeservedService) {
        this.VFinvoiceDeservedService = invoiceDeservedService;
    }

    public List<CustomerExtraworkPo> getVFinvoiceDeservedExtrawork() {
        if(!VFDataCalled){
         VFinvoiceDeservedExtrawork = em.createNativeQuery(
                "select * from customer_extrawork_po ewpo \n" +
                "where po_number in  ( select po_number from customer_extrawork_md  \n" +
"			group by po_number \n" +
"			having (sum(md_value) -  \n" +
"			COALESCE((select sum(invoice_value)  \n" +
"			from customer_extrawork_invoice where po_numer = ewpo.po_number group by po_numer),0)) > 0 )",CustomerExtraworkPo.class).getResultList();
        for (CustomerExtraworkPo x : VFinvoiceDeservedExtrawork) {
                VFinvoiceDeserved+=x.getInvoiceDeserved();
            }
        }
        return VFinvoiceDeservedExtrawork;
    }

    public void setVFinvoiceDeservedExtrawork(List<CustomerExtraworkPo> invoiceDeservedExtrawork) {
        this.VFinvoiceDeservedExtrawork = invoiceDeservedExtrawork;
    }
    
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="SimulationData">
    public void collectNS(){
        if(!VFDataCalled){
        List<CustomerServiceInvoice> netSalesService = null;
        List<CustomerExtraworkInvoice> netSalesExtra = null;
        NSRecord = new ArrayList<>();

        NsSimulation NS = null;
        netSalesService = em.createNativeQuery("select * " +
                                        "from customer_service_invoice " +
                                        "where invoice_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                                    + " and po_numer in (select po_number from customer_service_po where "
                    + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", CustomerServiceInvoice.class).getResultList();
        // Net Sales Service
        for (CustomerServiceInvoice netSale : netSalesService) {
            counterNs++;
            NS = new NsSimulation(counterNs.longValue());
            NS.setCreationTime(new Date());
            NS.setCreator(usersController.getLoggedInUser().getUserName());
            NS.setTypeOfNs("PO");
            NS.setTypeOfType("Service");
            NS.setPoNumber(netSale.getPoNumer().getPoNumber());
            NS.setTypeNumber(netSale.getInvoiceNumber());
            NS.setInvoiceValue(netSale.getInvoiceValue());
            NS.setEditable(false);
            NS.setDescription(netSale.getPoNumer().getPoDescription());
            NSRecord.add(NS);
        }
        netSalesExtra = em.createNativeQuery("select * " +
                                                "from customer_extrawork_invoice " +
                                                "where invoice_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                                             + " and po_numer in (select po_number from customer_extrawork_po where "
                            + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", CustomerExtraworkInvoice.class).getResultList();
        // Net Sales Extra Work
        for (CustomerExtraworkInvoice netSale : netSalesExtra) {
            counterNs++;
            NS = new NsSimulation(counterNs.longValue());
            NS.setCreationTime(new Date());
            NS.setCreator(usersController.getLoggedInUser().getUserName());
            NS.setTypeOfNs("PO");
            NS.setTypeOfType("Extrawork");
            NS.setPoNumber(netSale.getPoNumer().getPoNumber());
            NS.setTypeNumber(netSale.getInvoiceNumber());
            NS.setInvoiceValue(netSale.getInvoiceValue());
            NS.setEditable(false);
            NS.setDescription(netSale.getPoNumer().getPoDescription());
            NSRecord.add(NS);
        }
        List<CreditNote> creditNotesVF;
        creditNotesVF = em.createNativeQuery("select * from credit_note where cn_owner='Vodafone' "
                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true ", CreditNote.class).getResultList();
        // Credit Notes
        for (CreditNote creditNotesVF1 : creditNotesVF) {
            counterNs++;
            NS = new NsSimulation(counterNs.longValue());
            NS.setCreationTime(new Date());
            NS.setCreator(usersController.getLoggedInUser().getUserName());
            NS.setTypeOfNs("CN");
            NS.setTypeOfType(creditNotesVF1.getCnOwner());
            NS.setPoNumber("Credit Note");
            NS.setDescription(creditNotesVF1.getCnDescription());
            NS.setInvoiceValue(creditNotesVF1.getCnValue());
            NS.setEditable(false);
            NSRecord.add(NS);
        }
        // Penalties
        List<Penalty> penalitiesVF;
        penalitiesVF = em.createNativeQuery("select * from penalty where pn_owner='Vodafone' and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settled is true ", Penalty.class).getResultList();
        for (Penalty penalitiesVF1 : penalitiesVF) {
            counterNs++;
            NS = new NsSimulation(counterNs.longValue());
            NS.setCreationTime(new Date());
            NS.setCreator(usersController.getLoggedInUser().getUserName());
            NS.setTypeOfNs("PN");
            NS.setTypeOfType(penalitiesVF1.getPnOwner());
            NS.setPoNumber("Penalty");
            NS.setDescription(penalitiesVF1.getPnDescription());
            NS.setInvoiceValue(penalitiesVF1.getPnValue());
            NS.setEditable(false);
            NSRecord.add(NS);
        }
        }
    }
    
     public void collectCOS(){
        List<AspServiceGrn> costSalesService = null;
        List<AspExtraworkGrn> costSalesExtra = null;
        COSRecord = new ArrayList<>();
        CosSimulation COS = null;
        costSalesService = em.createNativeQuery( "select * " +
                                            " from asp_service_grn " +
                                            " where grn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' "
                                +  " and po_number in (select po_number from asp_service_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", AspServiceGrn.class).getResultList();
        // COS Sales Service
        for (AspServiceGrn costSale : costSalesService) {
            counterCos++;
            COS = new CosSimulation(counterCos.longValue());
            COS.setCreationTime(new Date());
            COS.setCreator(usersController.getLoggedInUser().getUserName());
            COS.setTypeOfNs("PO");
            COS.setTypeOfType("Service");
            COS.setPoNumber(costSale.getPoNumber().getPoNumber());
            COS.setTypeNumber(costSale.getGrnNumber());
            COS.setInvoiceValue(costSale.getGrnValue());
            COS.setEditable(false);
            COS.setAsp(costSale.getPoNumber().getSubcontractor());
            COS.setDescription(costSale.getPoNumber().getPoDescription());
            COSRecord.add(COS);
        }
        costSalesExtra = em.createNativeQuery( "select * " +
                                            " from asp_extrawork_grn " +
                                            " where grn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"'  "
                                +  " and po_number in (select po_number from asp_extrawork_po where "
                        + " network_name !='Support Network' and (early_start is null or early_start=false) ) ", AspExtraworkGrn.class).getResultList();
        // COS Sales Extra Work
        for (AspExtraworkGrn costSale : costSalesExtra) {
            counterCos++;
            COS = new CosSimulation(counterCos.longValue());
            COS.setCreationTime(new Date());
            COS.setCreator(usersController.getLoggedInUser().getUserName());
            COS.setTypeOfNs("PO");
            COS.setTypeOfType("Extrawork");
            COS.setPoNumber(costSale.getPoNumber().getPoNumber());
            COS.setTypeNumber(costSale.getGrnNumber());
            COS.setInvoiceValue(costSale.getGrnValue());
            COS.setEditable(false);
            COS.setAsp(costSale.getPoNumber().getSubcontractor());
            COS.setDescription(costSale.getPoNumber().getPoDescription());
            COSRecord.add(COS);
        }
        List<CreditNote> creditNotesVF;
        creditNotesVF = em.createNativeQuery("select * from credit_note where cn_owner!='Vodafone' "
                + " and cn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settlement_po is not null ", CreditNote.class).getResultList();
        // Credit Notes
        for (CreditNote creditNotesAsp : creditNotesVF) {
            counterCos++;
            COS = new CosSimulation(counterCos.longValue());
            COS.setCreationTime(new Date());
            COS.setCreator(usersController.getLoggedInUser().getUserName());
            COS.setTypeOfNs("CN");
            COS.setTypeOfType(creditNotesAsp.getCnOwner());
            COS.setPoNumber("Credit Note");
            COS.setDescription(creditNotesAsp.getCnDescription());
            COS.setInvoiceValue(creditNotesAsp.getCnValue());
            COS.setEditable(false);
            COS.setAsp(creditNotesAsp.getCnOwner());
            COSRecord.add(COS);
        }
        List<Penalty> penalitiesASP;
        penalitiesASP = em.createNativeQuery("select * from penalty where pn_owner!='Vodafone' and pn_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                                    + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' and settlement_po is not null  ", Penalty.class).getResultList();
        // Penalties     
        for (Penalty penalitiesAsp : penalitiesASP) {
            counterCos++;
            COS = new CosSimulation(counterCos.longValue());
            COS.setCreationTime(new Date());
            COS.setCreator(usersController.getLoggedInUser().getUserName());
            COS.setTypeOfNs("PN");
            COS.setTypeOfType(penalitiesAsp.getPnOwner());
            COS.setPoNumber("Penalty");
            COS.setDescription(penalitiesAsp.getPnDescription());
            COS.setInvoiceValue(penalitiesAsp.getPnValue());
            COS.setEditable(false);
            COS.setAsp(penalitiesAsp.getPnOwner());
            COSRecord.add(COS);
        }
       List<JvReporting> jvASP = em.createNativeQuery( "select * " +
                                            " from jv_reporting " +
                                            " where jv_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                        + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' ", JvReporting.class).getResultList();  
       // JVs 
       for (JvReporting jvASP1 : jvASP) {
            counterCos++;
            COS = new CosSimulation(counterCos.longValue());
            COS.setCreationTime(new Date());
            COS.setCreator(usersController.getLoggedInUser().getUserName());
            COS.setTypeOfNs("JV");
            COS.setTypeOfType(jvASP1.getJvTeam());
            COS.setPoNumber("JV");
            COS.setDescription(jvASP1.getJvDescription());
            COS.setInvoiceValue(jvASP1.getJvValue());
            COS.setEditable(false);
            COS.setAsp(jvASP1.getJvTeam());
            COSRecord.add(COS);
        }
       List<TimeReporting> trASP = em.createNativeQuery( "select * " +
                                                " from time_reporting " +
                                                " where reporting_date between '"+sdf.format(now.dayOfMonth().withMinimumValue().toDate())+"' "
                            + " and '"+sdf.format(now.dayOfMonth().withMaximumValue().toDate())+"' ", TimeReporting.class).getResultList();
       // TRs 
       for (TimeReporting trASP1 : trASP) {
            counterCos++;
            COS = new CosSimulation(counterCos.longValue());
            COS.setCreationTime(new Date());
            COS.setCreator(usersController.getLoggedInUser().getUserName());
            COS.setTypeOfNs("TR");
            COS.setTypeOfType(trASP1.getReportingTeam());
            COS.setPoNumber("Time Reporting");
            COS.setDescription(trASP1.getReportingDescription());
            COS.setInvoiceValue(trASP1.getReportingValue());
            COS.setEditable(false);
            COS.setAsp(trASP1.getReportingTeam());
            COSRecord.add(COS);
        }
    }
//</editor-fold>
    
    public void addNS(){
        counterNs++;
        NSRecord.add(new NsSimulation(counterNs.longValue()));
    }
    
    public void addCOS(){
        counterCos++;
        COSRecord.add(new CosSimulation(counterCos.longValue()));
    }

    public void onNsEdit(RowEditEvent event) {
        getTotalNS();
    }
         
    public void onCosEdit(RowEditEvent event) {
        getTotalCOS();
    }
     
    // NS Tables
    public void onRowDblClck_InvoiceDeserved_Service(final SelectEvent event) {
        CustomerServicePo obj = (CustomerServicePo) event.getObject();
        NsSimulation ns = new NsSimulation();
        counterNs++;
        ns.setId(counterNs.longValue());
        ns.setPoNumber(obj.getPoNumber());
        ns.setInvoiceValue(obj.getInvoiceDeserved());
        ns.setTypeOfNs("PO");
        ns.setTypeOfType("Service");
        ns.setTypeNumber("To be created");
        ns.setDescription(obj.getPoDescription());
        ns.setNsComment("PO Exists, need to invoice this amount");
        NSRecord.add(ns);
        VFinvoiceDeservedService.remove(obj);
        VFinvoiceDeserved -= ns.getInvoiceValue();
        getTotalNS();
    
    }
    public void onRowDblClck_InvoiceDeserved_Extawork(final SelectEvent event) {
        CustomerExtraworkPo obj = (CustomerExtraworkPo) event.getObject();
        NsSimulation ns = new NsSimulation();
        counterNs++;
        ns.setId(counterNs.longValue());
        ns.setPoNumber(obj.getPoNumber());
        ns.setInvoiceValue(obj.getInvoiceDeserved());
        ns.setTypeOfNs("PO");
        ns.setTypeOfType("Service");
        ns.setTypeNumber("To be created");
        ns.setDescription(obj.getPoDescription());
        ns.setNsComment("PO Exists, need to invoice this amount");
        NSRecord.add(ns);
        VFinvoiceDeservedExtrawork.remove(obj);
        VFinvoiceDeserved -= ns.getInvoiceValue();
        getTotalNS();
    }
    public void onRowDblClck_MdDeserved_Service(final SelectEvent event) {
        CustomerServicePo obj = (CustomerServicePo) event.getObject();
        NsSimulation ns = new NsSimulation();
        counterNs++;
        ns.setId(counterNs.longValue());
        ns.setPoNumber(obj.getPoNumber());
        ns.setInvoiceValue(obj.getMdDeserved());
        ns.setTypeOfNs("PO");
        ns.setTypeOfType("Service");
        ns.setTypeNumber("To be created");
        ns.setDescription(obj.getPoDescription());
        ns.setNsComment("PO Exists, need to invoice this amount");
        NSRecord.add(ns);
        VFmdDeservedService.remove(obj);
        VFmdDeserved -= ns.getInvoiceValue();
        getTotalNS();
    }
    public void onRowDblClck_MdDeserved_Extawork(final SelectEvent event) {
        CustomerExtraworkPo obj = (CustomerExtraworkPo) event.getObject();
        NsSimulation ns = new NsSimulation();
        counterNs++;
        ns.setId(counterNs.longValue());
        ns.setPoNumber(obj.getPoNumber());
        ns.setInvoiceValue(obj.getMdDeserved());
        ns.setTypeOfNs("PO");
        ns.setTypeOfType("Service");
        ns.setTypeNumber("To be created");
        ns.setDescription(obj.getPoDescription());
        ns.setNsComment("PO Exists, need to invoice this amount");
        NSRecord.add(ns);
        VFmdDeservedExtrawork.remove(obj);
        VFmdDeserved -= ns.getInvoiceValue();
        getTotalNS();
    }
    
    // COS Tables
    public void onRowDblClck_GrnDeserved_Service(final SelectEvent event) {
        AspServicePo obj = (AspServicePo) event.getObject();
        CosSimulation cos = new CosSimulation();
        counterCos++;
        cos.setId(counterCos.longValue());
        cos.setPoNumber(obj.getPoNumber());
        cos.setInvoiceValue(obj.getGrnDeserved());
        cos.setTypeOfNs("PO");
        cos.setTypeOfType("Service");
        cos.setTypeNumber("To be created");
        cos.setDescription(obj.getPoDescription());
        cos.setNsComment("PO Exists, need to GRN this amount");
        cos.setAsp(obj.getSubcontractor());
        COSRecord.add(cos);
        AspgrnDeservedService.remove(obj);
        Aspgrndeserved -= cos.getInvoiceValue();
        getTotalCOS();
    
    }
    public void onRowDblClck_GrnDeserved_Extawork(final SelectEvent event) {
        AspExtraworkPo obj = (AspExtraworkPo) event.getObject();
        CosSimulation cos = new CosSimulation();
        counterCos++;
        cos.setId(counterCos.longValue());
        cos.setPoNumber(obj.getPoNumber());
        cos.setInvoiceValue(obj.getGrnDeserved());
        cos.setTypeOfNs("PO");
        cos.setTypeOfType("Service");
        cos.setTypeNumber("To be created");
        cos.setDescription(obj.getPoDescription());
        cos.setNsComment("PO Exists, need to GTN this amount");
        cos.setAsp(obj.getSubcontractor());
        COSRecord.add(cos);
        AspgrnDeservedExtrawork.remove(obj);
        Aspgrndeserved -= cos.getInvoiceValue();
        getTotalCOS();
    }
    public void onRowDblClck_RemainingFromPo_Service(final SelectEvent event) {
        AspServicePo obj = (AspServicePo) event.getObject();
        CosSimulation cos = new CosSimulation();
        counterCos++;
        cos.setId(counterCos.longValue());
        cos.setPoNumber(obj.getPoNumber());
        cos.setInvoiceValue(obj.getRemainingFromPo());
        cos.setTypeOfNs("PO");
        cos.setTypeOfType("Service");
        cos.setTypeNumber("To be created");
        cos.setDescription(obj.getPoDescription());
        cos.setNsComment("PO Exists, need to GRN this amount");
        cos.setAsp(obj.getSubcontractor());
        COSRecord.add(cos);
        AspremainingFromPOService.remove(obj);
        AspremainingFromPO -= cos.getInvoiceValue();
        getTotalCOS();
    }
    public void onRowDblClck_RemainingFromPo_Extawork(final SelectEvent event) {
        AspExtraworkPo obj = (AspExtraworkPo) event.getObject();
        CosSimulation cos = new CosSimulation();
        counterCos++;
        cos.setId(counterCos.longValue());
        cos.setPoNumber(obj.getPoNumber());
        cos.setInvoiceValue(obj.getRemainingFromPo()    );
        cos.setTypeOfNs("PO");
        cos.setTypeOfType("Service");
        cos.setTypeNumber("To be created");
        cos.setDescription(obj.getPoDescription());
        cos.setNsComment("PO Exists, need to GRN this amount");
        cos.setAsp(obj.getSubcontractor());
        COSRecord.add(cos);
        AspremainingFromPOExtrawork.remove(obj);
        AspremainingFromPO -= cos.getInvoiceValue();
        getTotalCOS();
    }
    
    
    
    
    
    public void clearValuesASP(){
        AspremainingFromPO=0.0;
        Aspgrndeserved=0.0;
        AsppoReceived=0.0;
        AspcreditNotes = 0.0;
        Asppenalty=0.0;
    }
    
    public void callValuesASP(){
        getAspgrnDeservedService();
        getAspgrnDeservedExtrawork();
        getAspremainingFromPOService();
        getAspremainingFromPOExtrawork();
    }
    
    @PostConstruct
    public void init(){
         if(!VFDataCalled){
        VFremainingFromPO=0.0;
        VFmdDeserved=0.0;
        VFinvoiceDeserved=0.0;
        VFcreditNotes = 0.0;
        VFpenalty=0.0;
        
        counterCos = 0;
        counterNs = 0;
             
             
         System.out.println("Post Construct");
         if(startMonth==null){
        now = DateTime.now();
        }else{
            now = new DateTime(startMonth);
        }
        
        getVFremainingFromPOService();
        getVFremainingFromPOExtrawork();
        getVFcreditNotesDetails();
        getVFpenaltyDetails();
        getVFmdDeservedService();
        getVFmdDeservedExtrawork();
        getVFinvoiceDeservedService();
        getVFinvoiceDeservedExtrawork();
        
        collectNS();
        collectCOS();
        VFDataCalled = true;
        }    
     }

    public List<CosSimulation> getCOSRecord() {
        return COSRecord;
    }

    public List<NsSimulation> getNSRecord() {
        return NSRecord;
    }

    public Double getTotalCOS() {
        totalCOS = 0.0;
        for (CosSimulation CosRecord1 : COSRecord) {
            if(CosRecord1.getTypeOfNs()!=null && CosRecord1.getInvoiceValue()!=null){
            if(CosRecord1.getTypeOfNs().equals("PO") || 
                    CosRecord1.getTypeOfNs().equals("JV") ||
                        CosRecord1.getTypeOfNs().equals("TR")){
            totalCOS += CosRecord1.getInvoiceValue();
            }
            }
        }
        return totalCOS;
    }

    public Double getTotalNS() {
        totalNS = 0.0;
        for (NsSimulation NSRecord1 : NSRecord) {
            if(NSRecord1.getTypeOfNs()!=null && NSRecord1.getInvoiceValue()!=null){
            if(NSRecord1.getTypeOfNs().equals("PO")){
            totalNS += NSRecord1.getInvoiceValue();
            }else{
            totalNS -= NSRecord1.getInvoiceValue();    
            }
            }
        }
        return totalNS;
    }

    public Double getTotalUM(){
        getTotalNS();
        getTotalCOS();
        if(totalCOS==0){
            totalUM = totalNS;
            totalUMPercent=1.0;
        }else{
            totalUM = totalNS-totalCOS;
            totalUMPercent = totalUM/totalNS;
        }
        return totalUM;
    }

    public Double getTotalUMPercent() {
        return totalUMPercent;
    }
    
    
    
    
    public CustomerExtraworkPo getSelectedCustomerPoExtrawork() {
        return selectedCustomerPoExtrawork;
    }

    public void setSelectedCustomerPoExtrawork(CustomerExtraworkPo selectedCustomerPoExtrawork) {
        this.selectedCustomerPoExtrawork = selectedCustomerPoExtrawork;
    }
    
    
    public CustomerServicePo getSelectedCustomerPoService() {
        return selectedCustomerPoService;
    }

    public void setSelectedCustomerPoService(CustomerServicePo selectedCustomerPoService) {
        this.selectedCustomerPoService = selectedCustomerPoService;
    }

    public AspExtraworkPo getSelectedAspExtraworkPO() {
        return selectedAspExtraworkPO;
    }

    public void setSelectedAspExtraworkPO(AspExtraworkPo selectedAspExtraworkPO) {
        this.selectedAspExtraworkPO = selectedAspExtraworkPO;
    }

   

    public void setSelectedAspServicePO(AspServicePo selectedAspServicePO) {
        this.selectedAspServicePO = selectedAspServicePO;
    }

    public AspServicePo getSelectedAspServicePO() {
        return selectedAspServicePO;
    }
   
    public String getCOSIds(){
        String COSIds = "";
        if(!COSRecord.isEmpty()){
        for (CosSimulation COSRecord1 : COSRecord) {
            COSIds += "'"+COSRecord1.getPoNumber()+"',";
        }
        COSIds = COSIds.substring(0,COSIds.length()-1);
        }else{
            return "''";
        }
        
        return COSIds;
    }
    
}
