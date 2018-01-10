package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.CustomerExtraworkPo;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.CustomerExtraworkPoFacade;
import com.vodafone.financialtool.entities.AspExtraworkPo;
import com.vodafone.financialtool.entities.AspServicePo;
import com.vodafone.financialtool.entities.CustomerExtraworkWorkDone;
import com.vodafone.financialtool.entities.ExtraWork;
import java.io.IOException;

import java.io.Serializable;
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("customerExtraworkPoController")
@SessionScoped
public class CustomerExtraworkPoController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.CustomerExtraworkPoFacade ejbFacade;
    private List<CustomerExtraworkPo> items = null;
    private List<CustomerExtraworkPo> availableEarlyStart = null;
    private CustomerExtraworkPo selected;
    private CustomerExtraworkPo selectedUserPo;
    
    private List<AspExtraworkPo> selectedUserPoMatchingAspPo;
    private List<AspExtraworkPo> selectedUserPoSelectedCorrelation;
    private Double totalSelectedAspPOs;
    
    @Inject
    private UsersController usersController;
    @Inject
    private AspExtraworkPoController aspPoController;
    @Inject
    private CustomerExtraworkWorkDoneController workDoneController;
    

    public CustomerExtraworkPoController() {
    }

    public Double getTotalSelectedAspPOs() {
         totalSelectedAspPOs = 0.0;
        if(selectedUserPoSelectedCorrelation!=null){
            for (AspExtraworkPo aspPo : selectedUserPoSelectedCorrelation) {
                totalSelectedAspPOs+= (aspPo.getPoValue()+(aspPo.getPoValue()*(aspPo.getMargin())));
            }
        }
        return totalSelectedAspPOs;
    }

    public void setTotalSelectedAspPOs(Double totalSelectedAspPOs) {
        this.totalSelectedAspPOs = totalSelectedAspPOs;
    }

    public List<CustomerExtraworkPo> getAvailableEarlyStart() {
        availableEarlyStart = getFacade().findAvailableEalyStart();
        return availableEarlyStart;
    }
    
    
    
    public CustomerExtraworkPo getSelected() {
        return selected;
    }

    public void setSelected(CustomerExtraworkPo selected) {
        this.selected = selected;
    }

    public CustomerExtraworkPo getSelectedUserPo() {
        return selectedUserPo;
    }

    public void setSelectedUserPo(CustomerExtraworkPo selectedUserPo) {
        this.selectedUserPo = selectedUserPo;
    }
    
    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CustomerExtraworkPoFacade getFacade() {
        return ejbFacade;
    }

    public CustomerExtraworkPo prepareCreate() {
        selected = new CustomerExtraworkPo();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkPoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkPoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkPoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CustomerExtraworkPo> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
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
    }

    public CustomerExtraworkPo getCustomerExtraworkPo(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<CustomerExtraworkPo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CustomerExtraworkPo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CustomerExtraworkPo.class)
    public static class CustomerExtraworkPoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerExtraworkPoController controller = (CustomerExtraworkPoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerExtraworkPoController");
            return controller.getCustomerExtraworkPo(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CustomerExtraworkPo) {
                CustomerExtraworkPo o = (CustomerExtraworkPo) object;
                return getStringKey(o.getPoNumber());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CustomerExtraworkPo.class.getName()});
                return null;
            }
        }

    }

    public void setSelectedUserPoSelectedCorrelation(List<AspExtraworkPo> selectedUserPoSelectedCorrelation) {
        this.selectedUserPoSelectedCorrelation = selectedUserPoSelectedCorrelation;
    }

    
    public void updateValues(){
        if(selected!=null){
            if(selected.getFactor()!=null && selected.getServiceValue()!=null){
                selected.setPoValue(selected.getFactor()*selected.getServiceValue());
                selected.setRemainingFromPo(selected.getPoValue());
            }
        }
    }
    
    public void updateValuesEdit(){
       if(selectedUserPo!=null){
            if(selectedUserPo.getFactor()!=null && selectedUserPo.getServiceValue()!=null){
                selectedUserPo.setPoValue(selectedUserPo.getFactor()*selectedUserPo.getServiceValue());
                if(selectedUserPo.getCustomerExtraworkWorkDoneCollection()!=null){
                    Object[] workDoneCollection = selectedUserPo.getCustomerExtraworkWorkDoneCollection().toArray();
                    Double workDoneValue = 0.0;
                    for (Object workDoneCollection1 : workDoneCollection) {
                        workDoneValue += ((CustomerExtraworkWorkDone) workDoneCollection1).getWorkDoneValue();
                    }
                    selectedUserPo.setRemainingFromPo(selectedUserPo.getPoValue()-workDoneValue);
                }
            }
        } 
    }
    
    public void createVFPo(){
        if(selected!=null){
            updateValues();
            create();
            prepareCreate();
            try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/systemadmin/customer/view/view_vf_po.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void createEalyStart(){
        if(selected!=null){
            selected.setEarlyStart(true);
            updateValues();
            create();
            prepareCreate();
            try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/systemadmin/customer/view/view_vf_po.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void deletePo() {
        selected = selectedUserPo;
        persist(PersistAction.DELETE, "VF PO deleted");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            selectedUserPo = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void updateEdit(){
        updateValuesEdit();
        getFacade().edit(selectedUserPo);
        JsfUtil.addSuccessMessage("PO Updated");
    }

    public List<AspExtraworkPo> getSelectedUserPoSelectedCorrelation() {
        return selectedUserPoSelectedCorrelation;
    }

    
    public List<AspExtraworkPo> getSelectedUserPoMatchingAspPo() {
        if(selectedUserPo!=null){
        selectedUserPoMatchingAspPo = aspPoController.getItemsMatchingPo(selectedUserPo);
        }
        return selectedUserPoMatchingAspPo;
    }

    public void correlatePoService(AspServicePo aspPo){
        if(selected!=null && aspPo!=null){
            if(aspPo.getPoValue()<=selected.getRemainingFromPo()){
                if(selected.getAspServicePoCollection()!=null){
                    selected.getAspServicePoCollection().add(aspPo);
                }else{
                    selected.setAspServicePoCollection(new ArrayList<AspServicePo>());
                    selected.getAspServicePoCollection().add(aspPo);
                }
                // Add Work done
                workDoneController.prepareCreate();
                workDoneController.getSelected().setPoNumber(selected);
                workDoneController.getSelected().setWorkDoneDate(aspPo.getPoDate());
                workDoneController.getSelected().setWorkDoneValue(aspPo.getPoValue());
                workDoneController.updateValues(false);
                selectedUserPo = selected;
                workDoneController.createWorkDone();
            }
        }
    }
    public void correlatePoExtrawork(AspExtraworkPo aspPo){
        if(selected!=null && aspPo!=null){
            if(aspPo.getPoValue()<=selected.getRemainingFromPo()){
                if(selected.getAspExtaworkPoCollection()!=null){
                    selected.getAspExtaworkPoCollection().add(aspPo);
                }else{
                    selected.setAspExtaworkPoCollection(new ArrayList<AspExtraworkPo>());
                    selected.getAspExtaworkPoCollection().add(aspPo);
                }
                
                // Add Work done
                workDoneController.prepareCreate();
                workDoneController.getSelected().setPoNumber(selected);
                workDoneController.getSelected().setWorkDoneDate(aspPo.getPoDate());
                workDoneController.getSelected().setWorkDoneValue(aspPo.getPoValue());
                workDoneController.updateValues(false);
                selectedUserPo = selected;
                workDoneController.createWorkDone();
            }
        }
    }
    public void correlateAspPo(){
         
        if(selectedUserPoSelectedCorrelation!=null && selectedUserPo!=null){
            if(getTotalSelectedAspPOs()<=selectedUserPo.getRemainingFromPo()){
            for (AspExtraworkPo aspPo : selectedUserPoSelectedCorrelation) {
                // Add Extra Work Correlation
                if(selectedUserPo.getAspExtaworkPoCollection()!=null){
                    selectedUserPo.getAspExtaworkPoCollection().add(aspPo);
                }else{
                    selectedUserPo.setAspExtaworkPoCollection(new ArrayList<AspExtraworkPo>());
                    selectedUserPo.getAspExtaworkPoCollection().add(aspPo);
                }
                // Add Work done
                workDoneController.prepareCreate();
                workDoneController.getSelected().setPoNumber(selectedUserPo);
                workDoneController.getSelected().setWorkDoneDate(aspPo.getPoDate());
                workDoneController.getSelected().setWorkDoneValue(getTotalSelectedAspPOs());
                workDoneController.updateValues(false);
                workDoneController.createWorkDone();
                
            }
            }else{
                JsfUtil.addErrorMessage("Value is more than the remaining from PO");
            }
    }
   }
    
}
