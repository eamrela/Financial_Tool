package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.ExtraWork;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.ExtraWorkFacade;
import com.vodafone.financialtool.entities.ActivityCode;
import com.vodafone.financialtool.entities.Configurations;
import com.vodafone.financialtool.entities.ConfigurationsPK;
import com.vodafone.financialtool.entities.EmailNotification;
import com.vodafone.financialtool.entities.ExtraworkAttachments;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.IOUtils;

@Named("extraWorkController")
@SessionScoped
public class ExtraWorkController implements Serializable {

    
    @EJB
    private com.vodafone.financialtool.beans.ExtraWorkFacade ejbFacade;
    private List<ExtraWork> items = null;
    private List<ExtraWork> userItems = null;
    private ExtraWork selected;
    private ExtraWork selectedUserItem;
    private ExtraworkAttachments selectedUserItemAttachment;
    private ActivityCode selectedActivityCode;

    @Inject
    private UsersController usersController;
    @Inject
    private ExtraworkAttachmentsController extraWorkAttachments;
    @Inject
    private ActivityCodeController activityCodeController;
    @Inject
    private ConfigurationsController configurationController;
    @Inject
    private DomainsController domainController;
    @Inject
    private EmailNotificationController emailNotificationController;
    @Inject
    private RegionsController regionsController;
    
    private List<UploadedFile> files;
    private Configurations conf = null;
    private String rejectionReason;
    
    
    
    public ExtraWorkController() {
    }

    public ExtraWork getSelected() {
        return selected;
    }

    public ExtraWork getSelectedUserItem() {
        return selectedUserItem;
    }

    public void setSelectedUserItem(ExtraWork selectedUserItem) {
        this.selectedUserItem = selectedUserItem;
    }

    public ExtraworkAttachments getSelectedUserItemAttachment() {
        return selectedUserItemAttachment;
    }

    public void setSelectedUserItemAttachment(ExtraworkAttachments selectedUserItemAttachment) {
        this.selectedUserItemAttachment = selectedUserItemAttachment;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    
    
     public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> file) {
        this.files = file;
    }
    
    public void setSelected(ExtraWork selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ExtraWorkFacade getFacade() {
        return ejbFacade;
    }

    public ExtraWork prepareCreate() {
        selected = new ExtraWork();
        initializeEmbeddableKey();
        return selected;
    }

    public ExtraWork prepareCreateASP(){
        selected = new ExtraWork();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setApprovalStatus("Approved");
        selected.setAsp(usersController.getLoggedInUser().getCompany());
        
        selected.setDomainOwnerApproval(Boolean.FALSE);
        selected.setRegionApproval(Boolean.FALSE);
        selected.setBusinessProviderApproval(Boolean.FALSE);
        
        selected.setInvoiced(Boolean.FALSE);
        selected.setCorrelated(Boolean.FALSE);
        selected.setCreationTime(new Date());
       
        activityCodeController.setSelected(null);
        selectedActivityCode = null;
        files = new ArrayList<>();
        conf = configurationController.getConfigurations(new ConfigurationsPK("UPLOAD_PATH", "PROD"));
        return selected;
    }
    
    public ExtraWork prepareCreateBP(){
        selected = new ExtraWork();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        
        selected.setDomainOwnerApproval(Boolean.FALSE);
        selected.setRegionApproval(Boolean.FALSE);
        selected.setBusinessProviderApproval(Boolean.FALSE);
        
        selected.setInvoiced(Boolean.FALSE);
        selected.setCorrelated(Boolean.FALSE);
        selected.setCreationTime(new Date());
       
        activityCodeController.setSelected(null);
        selectedActivityCode = null;
        files = new ArrayList<>();
        conf = configurationController.getConfigurations(new ConfigurationsPK("UPLOAD_PATH", "PROD"));
        return selected;
    }
    
     public ExtraWork prepareCreateRO(){
        selected = new ExtraWork();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        
        selected.setDomainOwnerApproval(Boolean.FALSE);
        selected.setRegionApproval(Boolean.FALSE);
        selected.setBusinessProviderApproval(Boolean.TRUE);
        
        selected.setInvoiced(Boolean.FALSE);
        selected.setCorrelated(Boolean.FALSE);
        selected.setCreationTime(new Date());
       
        activityCodeController.setSelected(null);
        selectedActivityCode = null;
        files = new ArrayList<>();
        conf = configurationController.getConfigurations(new ConfigurationsPK("UPLOAD_PATH", "PROD"));
        return selected;
    }
    
    public ExtraWork prepareCreateDO(){
        selected = new ExtraWork();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        
        selected.setDomainOwnerApproval(Boolean.FALSE);
        selected.setRegionApproval(Boolean.TRUE);
        selected.setBusinessProviderApproval(Boolean.TRUE);
        
        selected.setInvoiced(Boolean.FALSE);
        selected.setCorrelated(Boolean.FALSE);
        selected.setCreationTime(new Date());
       
        activityCodeController.setSelected(null);
        selectedActivityCode = null;
        files = new ArrayList<>();
        conf = configurationController.getConfigurations(new ConfigurationsPK("UPLOAD_PATH", "PROD"));
        return selected;
    }
    
    public ExtraWork create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ExtraWorkCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            userItems = null;
        }
        return selected;
    }
    
    public void createExtraWork(){
        if(selected!=null){
            //additional attributes
            selected.setLastAssignment(usersController.getLoggedInUser().getUserName());
            selected.setAssignmentGroup(usersController.getLoggedInUser().getUserName());
            //creation
            updateValues();
            selected = create();
            //attachment
            for (UploadedFile file : files) {
                save(file);
            }
            EmailNotification email = new EmailNotification();
            email.setEmailSubject("FT | Creation Confirmation Extra Work | "+selected.getId());
            email.setEmailBody("Extra Work: "+selected.getId()+"\nExtra Work Details: "+selected.getActivityDetails()+" "+selected.getActivityDescription()
                                +"\n\n Created on: "+new Date()+"\nCreated By: "+usersController.getLoggedInUser().getUserName());
            email.setEmailSent(false);
            email.setEmailTo(selected.getCreator().getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            JsfUtil.addSuccessMessage("Extra Work Created!");
            
        prepareCreateASP();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/common/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ExtraWorkUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ExtraWorkDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            userItems = null;
        }
    }

    public List<ExtraWork> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<ExtraWork> getUserItems() {
       if (userItems == null) {
            userItems = getFacade().findUserItems(usersController.getLoggedInUserRole(),
                                                  usersController.getLoggedInUserRegions(),
                                                  usersController.getLoggedInUserDomains(),
                                                  usersController.getLoggedInUser().getUserName());
        }
        return userItems;
    }

    
    private ExtraWork persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
                return selected;
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
        return null;
    }

    public ExtraWork getExtraWork(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ExtraWork> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ExtraWork> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<ExtraWork> getUserItemsByDate(Date start,Date end) {
        return getFacade().findUserItemsByDate(usersController.getLoggedInUserRole(),
                                                  usersController.getLoggedInUserRegions(),
                                                  usersController.getLoggedInUserDomains(),
                                                  usersController.getLoggedInUser().getUserName(),
                                                  start,
                                                  end);
    }

    public void uploadExtrawork(List<ExtraWork> extras) {
        for (ExtraWork extra : extras) {
            setSelected(extra);
            create();
        }
    }

    @FacesConverter(forClass = ExtraWork.class)
    public static class ExtraWorkControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ExtraWorkController controller = (ExtraWorkController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "extraWorkController");
            return controller.getExtraWork(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ExtraWork) {
                ExtraWork o = (ExtraWork) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ExtraWork.class.getName()});
                return null;
            }
        }

    }

    public ActivityCode getSelectedActivityCode() {
        return selectedActivityCode;
    }

    public void setSelectedActivityCode(ActivityCode selectedActivityCode) {
        this.selectedActivityCode = selectedActivityCode;
    }

    public void onItemSelectSetSite(SelectEvent event){
    if(selected!=null){
        selected.setSite((String) event.getObject());
    }
    }
    
    public void onItemSelectSetSiteEdit(SelectEvent event){
        if(selectedUserItem!=null){
        selectedUserItem.setSite((String) event.getObject());
    }
    }
    
    public void onItemSelectSetActivityCode(SelectEvent event){
    if(selected!=null){
        selectedActivityCode = (ActivityCode) event.getObject();
        selected.setActivityCode((String) selectedActivityCode.getId());
        selected.setActivityDescription((String) selectedActivityCode.getDescription());
        selected.setUnitPriceAsp(selectedActivityCode.getSubcontractorPrice());
        selected.setUnitPriceVendor(selectedActivityCode.getVendorPrice());
        
        updateValues();
        }
    }
    
    
    public void onItemSelectSetActivityCodeEdit(SelectEvent event){
    if(selectedUserItem!=null){
        selectedActivityCode = (ActivityCode) event.getObject();
        selectedUserItem.setActivityCode((String) selectedActivityCode.getId());
        selectedUserItem.setActivityDescription((String) selectedActivityCode.getDescription());
        selectedUserItem.setUnitPriceAsp(selectedActivityCode.getSubcontractorPrice());
        selectedUserItem.setUnitPriceVendor(selectedActivityCode.getVendorPrice());
        
        updateValuesEdit();
        }
    }
    
    public void updateValues(){
        // Total Price ASP
        if(selected.getQty()!=null && selected.getUnitPriceAsp()!=null){
            selected.setTotalPriceAsp(selected.getQty() * selected.getUnitPriceAsp());
        }
        // Total Price Vendor
        if(selected.getQty()!=null && selected.getUnitPriceVendor()!=null){
            selected.setTotalPriceVendor(selected.getQty() * selected.getUnitPriceVendor());
        }
        // UM
        if(selected.getQty()!=null && selected.getTotalPriceAsp()!=null && selected.getTotalPriceVendor()!=null){
            selected.setUm(selected.getTotalPriceVendor()- selected.getTotalPriceAsp());
        }
        // UM%
        if(selected.getQty()!=null && selected.getUm()!=null && selected.getTotalPriceVendor()!=null){
            selected.setUmPercent(Double.valueOf(Math.round((selected.getUm()/selected.getTotalPriceVendor())*100.0)));
        }
        
        
    }
    
    public double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}
    public void updateValuesEdit(){
        // Total Price ASP
        if(selectedUserItem.getQty()!=null && selectedUserItem.getUnitPriceAsp()!=null){
            selectedUserItem.setTotalPriceAsp(selectedUserItem.getQty() * selectedUserItem.getUnitPriceAsp());
        }
        // Total Price Vendor
        if(selectedUserItem.getQty()!=null && selectedUserItem.getUnitPriceVendor()!=null){
            selectedUserItem.setTotalPriceVendor(selectedUserItem.getQty() * selectedUserItem.getUnitPriceVendor());
        }
        // UM
        if(selectedUserItem.getUnitPriceAsp()!=null && selectedUserItem.getUnitPriceVendor()!=null){
            selectedUserItem.setUm(selectedUserItem.getUnitPriceVendor() - selectedUserItem.getUnitPriceAsp());
        }
        // UM%
        if(selectedUserItem.getUm()!=null && selectedUserItem.getTotalPriceVendor()!=null){
            selectedUserItem.setUmPercent((selectedUserItem.getUm()/selectedUserItem.getTotalPriceVendor())*100.0);
        }
        
        
    }
    
    public void save(UploadedFile file) {

    
    if(conf!=null && selected!=null){
        try {
            String uploadPath = conf.getConfValue();
            new File(uploadPath+"/"+selected.getId()).mkdirs();
            File f = new File(uploadPath+"/"+selected.getId()+"/"+file.getFileName());
            OutputStream outputStream;
            outputStream = new FileOutputStream(f);
            IOUtils.copy(file.getInputstream(), outputStream);
            outputStream.close();
            
            extraWorkAttachments.prepareCreate();
            extraWorkAttachments.getSelected().setExtraworkId(selected);
            extraWorkAttachments.getSelected().setAttachmentLocation(f.getAbsolutePath());
            extraWorkAttachments.getSelected().setAttachmentName(f.getName());
            extraWorkAttachments.getSelected().setUploadedBy(usersController.getLoggedInUser());
            extraWorkAttachments.getSelected().setUploadedOn(new Date());
                    
                    
            if(selected.getExtraworkAttachmentsCollection()!=null){
                selected.getExtraworkAttachmentsCollection().add(extraWorkAttachments.create());
                update();
            }else{
                selected.setExtraworkAttachmentsCollection(new ArrayList<ExtraworkAttachments>());
                selected.getExtraworkAttachmentsCollection().add(extraWorkAttachments.create());
                update();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    }
    
    public void addAttachmentEdit(FileUploadEvent event) {
            if(selectedUserItem!=null){
        try {
            conf = configurationController.getConfigurations(new ConfigurationsPK("UPLOAD_PATH", "PROD"));
            String uploadPath = conf.getConfValue();
            new File(uploadPath+"/"+selectedUserItem.getId()).mkdirs();
            File f = new File(uploadPath+"/"+selectedUserItem.getId()+"/"+event.getFile().getFileName());
            OutputStream outputStream;
            outputStream = new FileOutputStream(f);
            IOUtils.copy(event.getFile().getInputstream(), outputStream);
            outputStream.close();
            
            extraWorkAttachments.prepareCreate();
            extraWorkAttachments.getSelected().setExtraworkId(selectedUserItem);
            extraWorkAttachments.getSelected().setAttachmentLocation(f.getAbsolutePath());
            extraWorkAttachments.getSelected().setAttachmentName(f.getName());
            extraWorkAttachments.getSelected().setUploadedBy(usersController.getLoggedInUser());
            extraWorkAttachments.getSelected().setUploadedOn(new Date());
                    
                    
            if(selectedUserItem.getExtraworkAttachmentsCollection()!=null){
                selectedUserItem.getExtraworkAttachmentsCollection().add(extraWorkAttachments.create());
                updateEdit();
            }else{
                selectedUserItem.setExtraworkAttachmentsCollection(new ArrayList<ExtraworkAttachments>());
                selectedUserItem.getExtraworkAttachmentsCollection().add(extraWorkAttachments.create());
                updateEdit();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    }
    
    public void addAttachment(FileUploadEvent event){
        System.out.println("Adding File");
           if(event.getFile() != null) {
               files.add(event.getFile());
           }
    }
    
    public void removeAttachment(UploadedFile file){
        System.out.println("Removing File");
        files.remove(file);
    }
     
    public void downloadAttachment(ExtraworkAttachments item){
        if(selectedUserItemAttachment!=null){
            try {

                System.out.println("Downloading Attachment");
                File FB = new File(item.getAttachmentLocation());
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext =  facesContext.getExternalContext();
                externalContext.responseReset();
                
                externalContext.setResponseContentType("application/csv");
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\""+FB.getName()+""+"\"");
                
                FileInputStream fin = new FileInputStream(FB);
                OutputStream output = externalContext.getResponseOutputStream();
                byte[] data;
                data = new byte[fin.available()];
                fin.read(data);
                output.write(data);
                output.flush();
                output.close();
                facesContext.responseComplete();
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void deleteAttachment(ExtraworkAttachments item){
         System.out.println("Deleting Attachment");
         selectedUserItem.getExtraworkAttachmentsCollection().remove(item);
         extraWorkAttachments.setSelected(item);
         extraWorkAttachments.destroy();
    }
    
    public void deleteExtaWork(){
        if(selectedUserItem!=null){
            EmailNotification email = new EmailNotification();
            email.setEmailSubject("FT | Deleted Extra Work | "+selectedUserItem.getId());
            email.setEmailBody("Extra Work: "+selectedUserItem.getId()+"\nExtra Work Details: "+selectedUserItem.getActivityDetails()+" "+selectedUserItem.getActivityDescription()
                                +"\n\n Deleted on: "+new Date()+"\nDeleted By: "+usersController.getLoggedInUser().getUserName());
            email.setEmailSent(false);
            setSelected(selectedUserItem);
            destroy();
            setSelected(null);
            // Creator
            email.setEmailTo(selectedUserItem.getCreator().getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            
        }
    }
    
    public void updateEdit(){
        System.out.println("Updating Extra Work");
        if(selectedUserItem!=null){
            setSelected(selectedUserItem);
            update();
        }
    }
    
    public void onChange() {
         System.out.println("Selected");
         JsfUtil.addSuccessMessage(selectedUserItem.getAssignmentGroup());
    }
    
    public boolean validateAndApproveBP(){
        System.out.println("Validating and Approving");
        if(selectedUserItem!=null){
            boolean good = true;
            String error = "";
            if(selectedUserItem.getActivityDate()==null){
                good = false;
                error += "Activity Data can't be empty!\n";
            }
            if(selectedUserItem.getActivityDescription()==null){
                good = false;
                error += "Activity Description can't be empty!\n";
            }
            if(selectedUserItem.getActivityDescription()==null){
                good = false;
                error += "Activity Description can't be empty!\n";
            }
            if(selectedUserItem.getActivityDetails()==null){
                good = false;
                error += "Activity Details can't be empty!\n";
            }
            if(selectedUserItem.getAsp()==null){
                good = false;
                error += "ASP can't be empty!\n";
            }
            if(selectedUserItem.getCustomerOwner()==null){
                good = false;
                error += "Customer Owner can't be empty!\n";
            }
            if(selectedUserItem.getDomainName()==null){
                good = false;
                error += "Domain can't be empty!\n";
            }
            if(selectedUserItem.getQty()==null){
                good = false;
                error += "Qty can't be empty!\n";
            }
            if(selectedUserItem.getSite()==null){
                good = false;
                error += "Site can't be empty!\n";
            }
            if(selectedUserItem.getSubDomain()==null){
                good = false;
                error += "Sub-Domain can't be empty!\n";
            }
            if(selectedUserItem.getTotalPriceAsp()==null){
                good = false;
                error += "Total Price ASP can't be empty!\n";
            }
            if(selectedUserItem.getTotalPriceVendor()==null){
                good = false;
                error += "Total Price Customer can't be empty!\n";
            }
           
            if(!good){
                JsfUtil.addErrorMessage(error);
                return good;
            }else{
            selectedUserItem.setBusinessProviderApproval(true);
            selectedUserItem.setLastAssignment(selectedUserItem.getAssignmentGroup());
            
            if(selectedUserItem.getDomainName().contains("CM")){
            selectedUserItem.setAssignmentGroup(regionsController.getRegions(selectedUserItem.getRegion()).getRegionOwner());
            }else{
            selectedUserItem.setRegionApproval(true);
            selectedUserItem.setAssignmentGroup(domainController.getDomains(selectedUserItem.getDomainName()).getDomainOwner());
            }
                System.out.println("Assigning on "+selectedUserItem.getAssignmentGroup());
            
            JsfUtil.addSuccessMessage("Approved");
            updateEdit();
            EmailNotification email = new EmailNotification();
            email.setEmailSubject("FT | Approved Extra Work | "+selectedUserItem.getId());
            email.setEmailBody("Extra Work: "+selectedUserItem.getId()+"\nExtra Work Details: "+selectedUserItem.getActivityDetails()+" "+selectedUserItem.getActivityDescription()
                                +"\n\n Approved on: "+new Date()+"\nApproved By: "+usersController.getLoggedInUser().getUserName()
                                +"\nApproval Type: Business\nNew Assignement: Region Owner");
            email.setEmailSent(false);
            email.setEmailTo(selectedUserItem.getCreator().getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            email.setEmailTo(usersController.getUsers(selectedUserItem.getAssignmentGroup()).getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            return good;
            }
        }
        return false;
    }
    
    public boolean validateAndApproveRO(){
        System.out.println("Validating and Approving");
        if(selectedUserItem!=null){
            boolean good = true;
            String error = "";
            if(selectedUserItem.getActivityDate()==null){
                good = false;
                error += "Activity Data can't be empty!\n";
            }
            if(selectedUserItem.getActivityDescription()==null){
                good = false;
                error += "Activity Description can't be empty!\n";
            }
            if(selectedUserItem.getActivityDescription()==null){
                good = false;
                error += "Activity Description can't be empty!\n";
            }
            if(selectedUserItem.getActivityDetails()==null){
                good = false;
                error += "Activity Details can't be empty!\n";
            }
            if(selectedUserItem.getAsp()==null){
                good = false;
                error += "ASP can't be empty!\n";
            }
            if(selectedUserItem.getCustomerOwner()==null){
                good = false;
                error += "Customer Owner can't be empty!\n";
            }
            if(selectedUserItem.getDomainName()==null){
                good = false;
                error += "Domain can't be empty!\n";
            }
            if(selectedUserItem.getQty()==null){
                good = false;
                error += "Qty can't be empty!\n";
            }
            if(selectedUserItem.getSite()==null){
                good = false;
                error += "Site can't be empty!\n";
            }
            if(selectedUserItem.getSubDomain()==null){
                good = false;
                error += "Sub-Domain can't be empty!\n";
            }
            if(selectedUserItem.getTotalPriceAsp()==null){
                good = false;
                error += "Total Price ASP can't be empty!\n";
            }
            if(selectedUserItem.getTotalPriceVendor()==null){
                good = false;
                error += "Total Price Customer can't be empty!\n";
            }
           
            if(!good){
                JsfUtil.addErrorMessage(error);
                return good;
            }else{
            selectedUserItem.setBusinessProviderApproval(true);
            selectedUserItem.setRegionApproval(true);
            selectedUserItem.setLastAssignment(selectedUserItem.getAssignmentGroup());
            selectedUserItem.setAssignmentGroup(domainController.getDomains(selectedUserItem.getDomainName()).getDomainOwner());
                System.out.println("Assigning on "+selectedUserItem.getAssignmentGroup());
            JsfUtil.addSuccessMessage("Approved");
            updateEdit();
            EmailNotification email = new EmailNotification();
            email.setEmailSubject("FT | Approved Extra Work | "+selectedUserItem.getId());
            email.setEmailBody("Extra Work: "+selectedUserItem.getId()+"\nExtra Work Details: "+selectedUserItem.getActivityDetails()+" "+selectedUserItem.getActivityDescription()
                                +"\n\n Approved on: "+new Date()+"\nApproved By: "+usersController.getLoggedInUser().getUserName()
                                +"\nApproval Type: Regional\nNew Assignement: Region Ownwer");
            email.setEmailSent(false);
            email.setEmailTo(selectedUserItem.getCreator().getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            email.setEmailTo(usersController.getUsers(selectedUserItem.getAssignmentGroup()).getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            return good;
            }
        }
        return false;
    }
    
    public boolean validateAndApproveDO(){
        System.out.println("Validating and Approving");
        if(selectedUserItem!=null){
            boolean good = true;
            String error = "";
            if(selectedUserItem.getActivityDate()==null){
                good = false;
                error += "Activity Data can't be empty!\n";
            }
            if(selectedUserItem.getActivityDescription()==null){
                good = false;
                error += "Activity Description can't be empty!\n";
            }
            if(selectedUserItem.getActivityDescription()==null){
                good = false;
                error += "Activity Description can't be empty!\n";
            }
            if(selectedUserItem.getActivityDetails()==null){
                good = false;
                error += "Activity Details can't be empty!\n";
            }
            if(selectedUserItem.getAsp()==null){
                good = false;
                error += "ASP can't be empty!\n";
            }
            if(selectedUserItem.getCustomerOwner()==null){
                good = false;
                error += "Customer Owner can't be empty!\n";
            }
            if(selectedUserItem.getDomainName()==null){
                good = false;
                error += "Domain can't be empty!\n";
            }
            if(selectedUserItem.getQty()==null){
                good = false;
                error += "Qty can't be empty!\n";
            }
            if(selectedUserItem.getSite()==null){
                good = false;
                error += "Site can't be empty!\n";
            }
            if(selectedUserItem.getSubDomain()==null){
                good = false;
                error += "Sub-Domain can't be empty!\n";
            }
            if(selectedUserItem.getTotalPriceAsp()==null){
                good = false;
                error += "Total Price ASP can't be empty!\n";
            }
            if(selectedUserItem.getTotalPriceVendor()==null){
                good = false;
                error += "Total Price Customer can't be empty!\n";
            }
           
            if(!good){
                JsfUtil.addErrorMessage(error);
                return good;
            }else{
            selectedUserItem.setBusinessProviderApproval(true);
            selectedUserItem.setRegionApproval(true);
            selectedUserItem.setDomainOwnerApproval(true);
            JsfUtil.addSuccessMessage("Approved");
            updateEdit();
            EmailNotification email = new EmailNotification();
            email.setEmailSubject("FT | Approved Extra Work | "+selectedUserItem.getId());
            email.setEmailBody("Extra Work: "+selectedUserItem.getId()+"\nExtra Work Details: "+selectedUserItem.getActivityDetails()+" "+selectedUserItem.getActivityDescription()
                                +"\n\n Approved on: "+new Date()+"\nApproved By: "+usersController.getLoggedInUser().getUserName()
                                +"\nApproval Type: Domain");
            email.setEmailSent(false);
            email.setEmailTo(selectedUserItem.getCreator().getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            return good;
            }
        }
        return false;
    }
    
    public void rejectExtraWork(){
        if(selectedUserItem!=null){
        if(rejectionReason!=null){
            if(usersController.getLoggedInUserRole().contains("DO")){
                selectedUserItem.setRegionApproval(Boolean.FALSE);
            }
            else if(usersController.getLoggedInUserRole().contains("BP")){
                selectedUserItem.setDomainOwnerApproval(Boolean.FALSE);
            }
            else if(usersController.getLoggedInUserRole().contains("RO")){
                selectedUserItem.setDomainOwnerApproval(Boolean.FALSE);
            }
            selectedUserItem.setActivityDetails(selectedUserItem.getActivityDetails()+"\n Rejection Reason: "+rejectionReason);
            if(selectedUserItem.getLastAssignment()==null){
            selectedUserItem.setAssignmentGroup(selectedUserItem.getCreator().getUserName());
            }else{
            selectedUserItem.setAssignmentGroup(selectedUserItem.getLastAssignment());
            }
            updateEdit();
            EmailNotification email = new EmailNotification();
            email.setEmailSubject("FT | Rejected Extra Work | "+selectedUserItem.getId());
            email.setEmailBody("Extra Work: "+selectedUserItem.getId()+"\nExtra Work Details: "+selectedUserItem.getActivityDetails()+" "+selectedUserItem.getActivityDescription()
                                +"\n\n Rejected on: "+new Date()+"\nRejected By: "+usersController.getLoggedInUser().getUserName()
                                +"\nRejection Reason: "+rejectionReason);
            email.setEmailSent(false);
            email.setEmailTo(selectedUserItem.getCreator().getUserEmail());
            emailNotificationController.setSelected(email);
            emailNotificationController.create();
            if(selectedUserItem.getLastAssignment()!=null){
                email.setEmailTo(usersController.getUsers(selectedUserItem.getLastAssignment()).getUserEmail());
                emailNotificationController.setSelected(email);
                emailNotificationController.create();
            }
            rejectionReason = null;
        }else{
            JsfUtil.addErrorMessage("Please enter the rejection reason");
        }
        }
    }
}
