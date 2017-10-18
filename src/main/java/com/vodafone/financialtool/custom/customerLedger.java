/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.entities.AspExtraworkGrn;
import com.vodafone.financialtool.entities.AspServicePo;
import com.vodafone.financialtool.entities.CreditNote;
import com.vodafone.financialtool.entities.CustomerExtraworkInvoice;
import com.vodafone.financialtool.entities.CustomerExtraworkMd;
import com.vodafone.financialtool.entities.CustomerExtraworkPo;
import com.vodafone.financialtool.entities.CustomerServiceInvoice;
import com.vodafone.financialtool.entities.CustomerServiceMd;
import com.vodafone.financialtool.entities.CustomerServicePo;
import com.vodafone.financialtool.entities.ExtraWork;
import com.vodafone.financialtool.entities.Penalty;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Named("customerLedgerController")
@SessionScoped
public class customerLedger implements Serializable {
    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private List<ExtraWork> extraWorkWithoutPo;
    
    private List<CustomerServicePo> remainingFromPOService;
    private List<CustomerExtraworkPo> remainingFromPOExtrawork;
    
    private List<CustomerServiceMd> mdReceivedService;
    private List<CustomerExtraworkMd> mdReceivedExtrawork;
    
    private List<CustomerServicePo> mdDeservedService;
    private List<CustomerExtraworkPo> mdDeservedExtrawork;
    
    private List<CustomerServiceInvoice> invoiceReceivedService;
    private List<CustomerExtraworkInvoice> invoiceReceivedExtrawork;
    
    private List<CustomerServicePo> invoiceDeservedService;
    private List<CustomerExtraworkPo> invoiceDeservedExtrawork;
    
    private List<CustomerServicePo> poReceivedService;
    private List<CustomerExtraworkPo> poReceivedExtrawork;
    
    private List<CreditNote> creditNotesDetails;
    private List<Penalty> penaltyDetails;

    private Double extraWorkNoPOASP=0.0;
    private Double extraWorkNoPOVF=0.0;
    private Double UMP=1.0;
    private Double overall=0.0;
    private Double remainingFromPO=0.0;
    private Double mdReceived=0.0;
    private Double mdDeserved=0.0;
    private Double invoiceReceived=0.0;
    private Double invoiceDeserved=0.0;
    private Double poReceived=0.0;
    private Double creditNotes=0.0;
    private Double penalty=0.0;

    
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<ExtraWork> getExtraWorkWithoutPo() {
        extraWorkWithoutPo = em.createNativeQuery(" select * from extra_work where  "
                    + "  domain_owner_approval is true ", ExtraWork.class).getResultList();
            for (ExtraWork x : extraWorkWithoutPo) {
                extraWorkNoPOASP+=x.getTotalPriceAsp();
                extraWorkNoPOVF+=x.getTotalPriceVendor();
            }
            try{
            UMP = ((extraWorkNoPOVF-extraWorkNoPOASP)/extraWorkNoPOVF);
            }catch(Exception r){
                r.printStackTrace();
            }
        return extraWorkWithoutPo;
    }

    public void setExtraWorkWithoutPo(List<ExtraWork> extraWorkWithoutPo) {
        this.extraWorkWithoutPo = extraWorkWithoutPo;
    }

    public List<CustomerServicePo> getRemainingFromPOService() {
        remainingFromPOService = em.createNativeQuery(" select * from customer_service_po "
                   + " where remaining_from_po > 100 ", CustomerServicePo.class).getResultList(); 
           for (CustomerServicePo x : remainingFromPOService) {
                remainingFromPO+=x.getRemainingFromPo();
            }
        return remainingFromPOService;
    }

    public void setRemainingFromPOService(List<CustomerServicePo> remainingFromPOService) {
        this.remainingFromPOService = remainingFromPOService;
    }

    public List<CustomerExtraworkPo> getRemainingFromPOExtrawork() {
        remainingFromPOExtrawork = em.createNativeQuery(" select * from customer_extrawork_po "
                   + " where remaining_from_po > 100 ", CustomerExtraworkPo.class).getResultList(); 
           for (CustomerExtraworkPo x : remainingFromPOExtrawork) {
                remainingFromPO+=x.getRemainingFromPo();
            }
        return remainingFromPOExtrawork;
    }

    public void setRemainingFromPOExtrawork(List<CustomerExtraworkPo> remainingFromPOExtrawork) {
        this.remainingFromPOExtrawork = remainingFromPOExtrawork;
    }

    public List<CustomerServiceMd> getMdReceivedService() {
        mdReceivedService = em.createNativeQuery(" select * from customer_service_md ", CustomerServiceMd.class).getResultList(); 
           for (CustomerServiceMd x : mdReceivedService) {
                mdReceived+=x.getMdValue();
            }
        return mdReceivedService;
    }

    public void setMdReceivedService(List<CustomerServiceMd> mdReceivedService) {
        this.mdReceivedService = mdReceivedService;
    }

    public List<CustomerExtraworkMd> getMdReceivedExtrawork() {
        mdReceivedExtrawork = em.createNativeQuery(" select * from customer_extrawork_md ", CustomerExtraworkMd.class).getResultList(); 
           for (CustomerExtraworkMd x : mdReceivedExtrawork) {
                mdReceived+=x.getMdValue();
            }
        return mdReceivedExtrawork;
    }

    public void setMdReceivedExtrawork(List<CustomerExtraworkMd> mdReceivedExtrawork) {
        this.mdReceivedExtrawork = mdReceivedExtrawork;
    }

    

    public List<CustomerServiceInvoice> getInvoiceReceivedService() {
        invoiceReceivedService = em.createNativeQuery(" select * from customer_service_invoice ", CustomerServiceInvoice.class).getResultList(); 
           for (CustomerServiceInvoice x : invoiceReceivedService) {
                invoiceReceived+=x.getInvoiceValue();
            }
        return invoiceReceivedService;
    }

    public void setInvoiceReceivedService(List<CustomerServiceInvoice> invoiceReceivedService) {
        this.invoiceReceivedService = invoiceReceivedService;
    }

    public List<CustomerExtraworkInvoice> getInvoiceReceivedExtrawork() {
        invoiceReceivedExtrawork = em.createNativeQuery(" select * from customer_extrawork_invoice ", CustomerExtraworkInvoice.class).getResultList(); 
           for (CustomerExtraworkInvoice x : invoiceReceivedExtrawork) {
                invoiceReceived+=x.getInvoiceValue();
            }
        return invoiceReceivedExtrawork;
    }

    public void setInvoiceReceivedExtrawork(List<CustomerExtraworkInvoice> invoiceReceivedExtrawork) {
        this.invoiceReceivedExtrawork = invoiceReceivedExtrawork;
    }

   

    

    public List<CustomerServicePo> getPoReceivedService() {
        poReceivedService = em.createNativeQuery("select * from customer_service_po"
                , CustomerServicePo.class).getResultList();    
         for (CustomerServicePo x : poReceivedService) {
                poReceived+=x.getPoValue();
            }
        return poReceivedService;
    }

    public void setPoReceivedService(List<CustomerServicePo> poReceivedService) {
        this.poReceivedService = poReceivedService;
    }

    public List<CustomerExtraworkPo> getPoReceivedExtrawork() {
        poReceivedExtrawork = em.createNativeQuery("select * from customer_service_po"
                , CustomerExtraworkPo.class).getResultList();    
         for (CustomerExtraworkPo x : poReceivedExtrawork) {
                poReceived+=x.getPoValue();
            }
        return poReceivedExtrawork;
    }

    public void setPoReceivedExtrawork(List<CustomerExtraworkPo> poReceivedExtrawork) {
        this.poReceivedExtrawork = poReceivedExtrawork;
    }

    public List<CreditNote> getCreditNotesDetails() {
         creditNotesDetails = em.createNativeQuery("  select * \n" +
                  " from credit_note  \n" +
                  " where cn_owner='Vodafone' and settled=false" , CreditNote.class).getResultList();  
          for (CreditNote x : creditNotesDetails) {
                creditNotes+=x.getCnValue();
            }
        return creditNotesDetails;
    }

    public void setCreditNotesDetails(List<CreditNote> creditNotesDetails) {
        this.creditNotesDetails = creditNotesDetails;
    }

    public List<Penalty> getPenaltyDetails() {
         penaltyDetails = em.createNativeQuery("  select * \n" +
                  " from penalty \n" +
                  " where pn_owner='Vodafone' and settled=false" , Penalty.class).getResultList();  
          for (Penalty x : penaltyDetails) {
                penalty+=x.getPnValue();
            }
        return penaltyDetails;
    }

    public void setPenaltyDetails(List<Penalty> penaltyDetails) {
        this.penaltyDetails = penaltyDetails;
    }

    public Double getUMP() {
        return UMP;
    }

    public void setUMP(Double UMP) {
        this.UMP = UMP;
    }

    public Double getOverall() {
        return overall;
    }

    public void setOverall(Double overall) {
        this.overall = overall;
    }

    

    public Double getRemainingFromPO() {
        return remainingFromPO;
    }

    public void setRemainingFromPO(Double remainingFromPO) {
        this.remainingFromPO = remainingFromPO;
    }

    public Double getMdReceived() {
        return mdReceived;
    }

    public void setMdReceived(Double mdReceived) {
        this.mdReceived = mdReceived;
    }

    public Double getMdDeserved() {
        return mdDeserved;
    }

    public void setMdDeserved(Double mdDeserved) {
        this.mdDeserved = mdDeserved;
    }

    public Double getInvoiceReceived() {
        return invoiceReceived;
    }

    public void setInvoiceReceived(Double invoiceReceived) {
        this.invoiceReceived = invoiceReceived;
    }

    public Double getInvoiceDeserved() {
        return invoiceDeserved;
    }

    public void setInvoiceDeserved(Double invoiceDeserved) {
        this.invoiceDeserved = invoiceDeserved;
    }

    public Double getPoReceived() {
        return poReceived;
    }

    public void setPoReceived(Double poReceived) {
        this.poReceived = poReceived;
    }

    public Double getCreditNotes() {
        return creditNotes;
    }

    public void setCreditNotes(Double creditNotes) {
        this.creditNotes = creditNotes;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Double getExtraWorkNoPOASP() {
        return extraWorkNoPOASP;
    }

    public Double getExtraWorkNoPOVF() {
        return extraWorkNoPOVF;
    }

    public List<CustomerServicePo> getMdDeservedService() {
        mdDeservedService = em.createNativeQuery(
                "select * from customer_service_po ewpo \n" +
                "where (\n" +
                "(po_value - remaining_from_po) - \n" +
                "COALESCE ((select sum(md_value) \n" +
                "from customer_service_md where po_number =  ewpo.po_number group by po_number),0)) > 0  \n",CustomerServicePo.class).getResultList();
        for (CustomerServicePo x : mdDeservedService) {
                mdDeserved+=x.getMdDeserved();
            }
        return mdDeservedService;
    }

    public void setMdDeservedService(List<CustomerServicePo> mdDeservedService) {
        this.mdDeservedService = mdDeservedService;
    }

    public List<CustomerExtraworkPo> getMdDeservedExtrawork() {
         mdDeservedExtrawork = em.createNativeQuery(
                "select * from customer_extrawork_po ewpo \n" +
                "where (\n" +
                "(po_value - remaining_from_po) - \n" +
                "COALESCE ((select sum(md_value) \n" +
                "from customer_extrawork_md where po_number =  ewpo.po_number group by po_number),0)) > 0  \n",CustomerExtraworkPo.class).getResultList();
        for (CustomerExtraworkPo x : mdDeservedExtrawork) {
                mdDeserved+=x.getMdDeserved();
            }
        return mdDeservedExtrawork;
    }

    public void setMdDeservedExtrawork(List<CustomerExtraworkPo> mdDeservedExtrawork) {
        this.mdDeservedExtrawork = mdDeservedExtrawork;
    }

    public List<CustomerServicePo> getInvoiceDeservedService() {
         invoiceDeservedService = em.createNativeQuery(
                "select * from customer_service_po ewpo \n" +
                "where po_number in  ( select po_number from customer_service_md  \n" +
"			group by po_number \n" +
"			having (sum(md_value) -  \n" +
"			COALESCE((select sum(invoice_value)  \n" +
"			from customer_service_invoice where po_numer = ewpo.po_number group by po_numer),0)) > 0 )",CustomerServicePo.class).getResultList();
        for (CustomerServicePo x : invoiceDeservedService) {
                invoiceDeserved+=x.getInvoiceDeserved();
            }
        return invoiceDeservedService;
    }

    public void setInvoiceDeservedService(List<CustomerServicePo> invoiceDeservedService) {
        this.invoiceDeservedService = invoiceDeservedService;
    }

    public List<CustomerExtraworkPo> getInvoiceDeservedExtrawork() {
         invoiceDeservedExtrawork = em.createNativeQuery(
                "select * from customer_extrawork_po ewpo \n" +
                "where po_number in  ( select po_number from customer_extrawork_md  \n" +
"			group by po_number \n" +
"			having (sum(md_value) -  \n" +
"			COALESCE((select sum(invoice_value)  \n" +
"			from customer_extrawork_invoice where po_numer = ewpo.po_number group by po_numer),0)) > 0 )",CustomerExtraworkPo.class).getResultList();
        for (CustomerExtraworkPo x : invoiceDeservedExtrawork) {
                invoiceDeserved+=x.getInvoiceDeserved();
            }
        return invoiceDeservedExtrawork;
    }

    public void setInvoiceDeservedExtrawork(List<CustomerExtraworkPo> invoiceDeservedExtrawork) {
        this.invoiceDeservedExtrawork = invoiceDeservedExtrawork;
    }
    
    
    

    public void callValues(){
        getExtraWorkWithoutPo();
        getRemainingFromPOService();
        getRemainingFromPOExtrawork();
        getMdReceivedService();
        getMdReceivedExtrawork();
        getInvoiceReceivedService();
        getInvoiceReceivedExtrawork();
        getPoReceivedService();
        getPoReceivedExtrawork();
        getCreditNotesDetails();
        getPenaltyDetails();
        getMdDeservedService();
        getMdDeservedExtrawork();
        getInvoiceDeservedService();
        getInvoiceDeservedExtrawork();
        overall = (extraWorkNoPOVF+mdDeserved+invoiceDeserved)-(creditNotes+penalty);
    }

    public void clearValues(){
        extraWorkNoPOVF=0.0;
        remainingFromPO=0.0;
        mdReceived=0.0;
        mdDeserved=0.0;
        invoiceReceived=0.0;
        invoiceDeserved=0.0;
        poReceived=0.0;
        UMP=1.0;
        creditNotes = 0.0;
        penalty=0.0;
        overall =0.0;
        callValues();
    }
}
