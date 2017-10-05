package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.AspExtraworkPo;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.AspExtraworkPoFacade;
import com.vodafone.financialtool.entities.AspExtraworkWorkDone;
import com.vodafone.financialtool.entities.CustomerExtraworkPo;
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

@Named("aspExtraworkPoController")
@SessionScoped
public class AspExtraworkPoController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.AspExtraworkPoFacade ejbFacade;
    private List<AspExtraworkPo> items = null;
    private AspExtraworkPo selected;
    private AspExtraworkPo selectedUserPo;

    private List<ExtraWork> selectedUserPoMatchingExtraWork;
    private List<ExtraWork> selectedUserPoSelectedCorrelation;
    private Double totalSelectedExtraWorkAmount;
    
    @Inject
    private UsersController usersController;
    @Inject
    private ExtraWorkController extraWorkController;
    @Inject
    private AspExtraworkWorkDoneController workDoneController;

    public Double getTotalSelectedExtraWorkAmount() {
        totalSelectedExtraWorkAmount = 0.0;
        if(selectedUserPoSelectedCorrelation!=null){
            for (ExtraWork selectedUserPoSelectedCorrelation1 : selectedUserPoSelectedCorrelation) {
                totalSelectedExtraWorkAmount+= selectedUserPoSelectedCorrelation1.getTotalPriceAsp();
            }
        }
        return totalSelectedExtraWorkAmount;
    }

    public void setTotalSelectedExtraWorkAmount(Double totalSelectedExtraWorkAmount) {
        this.totalSelectedExtraWorkAmount = totalSelectedExtraWorkAmount;
    }
    
    
    
    public AspExtraworkPoController() {
    }

    public AspExtraworkPo getSelectedUserPo() {
        return selectedUserPo;
    }

    public void setSelectedUserPo(AspExtraworkPo selectedUserPo) {
        this.selectedUserPo = selectedUserPo;
    }

    public List<ExtraWork> getSelectedUserPoMatchingExtraWork() {
        if(selectedUserPo!=null){
        selectedUserPoMatchingExtraWork = extraWorkController.getItemsMatchingPo(selectedUserPo);
        }
        return selectedUserPoMatchingExtraWork;
    }

    public void setSelectedUserPoMatchingExtraWork(List<ExtraWork> selectedUserPoMatchingExtraWork) {
        this.selectedUserPoMatchingExtraWork = selectedUserPoMatchingExtraWork;
    }

    public List<ExtraWork> getSelectedUserPoSelectedCorrelation() {
        return selectedUserPoSelectedCorrelation;
    }

    public void setSelectedUserPoSelectedCorrelation(List<ExtraWork> selectedUserPoSelectedCorrelation) {
        this.selectedUserPoSelectedCorrelation = selectedUserPoSelectedCorrelation;
    }

    
    public AspExtraworkPo getSelected() {
        return selected;
    }

    public void setSelected(AspExtraworkPo selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspExtraworkPoFacade getFacade() {
        return ejbFacade;
    }

    public AspExtraworkPo prepareCreate() {
        selected = new AspExtraworkPo();
        initializeEmbeddableKey();
        
         selected.setCreator(usersController.getLoggedInUser());
         selected.setCreationTime(new Date());
         
        
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkPoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkPoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkPoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedUserPoMatchingExtraWork = null;
            selectedUserPo = null;
        }
    }

    public List<AspExtraworkPo> getItems() {
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

    public AspExtraworkPo getAspExtraworkPo(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<AspExtraworkPo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspExtraworkPo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<AspExtraworkPo> getItemsMatchingPo(CustomerExtraworkPo selectedUserPo) {
        return getFacade().findMatchingPOs(selectedUserPo);
    }

    @FacesConverter(forClass = AspExtraworkPo.class)
    public static class AspExtraworkPoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspExtraworkPoController controller = (AspExtraworkPoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspExtraworkPoController");
            return controller.getAspExtraworkPo(getKey(value));
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
            if (object instanceof AspExtraworkPo) {
                AspExtraworkPo o = (AspExtraworkPo) object;
                return getStringKey(o.getPoNumber());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspExtraworkPo.class.getName()});
                return null;
            }
        }

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
                if(selectedUserPo.getAspExtraworkWorkDoneCollection()!=null){
                    Object[] workDoneCollection = selectedUserPo.getAspExtraworkWorkDoneCollection().toArray();
                    Double workDoneValue = 0.0;
                    for (Object workDoneCollection1 : workDoneCollection) {
                        workDoneValue += ((AspExtraworkWorkDone) workDoneCollection1).getWorkDoneValue();
                    }
                    selectedUserPo.setRemainingFromPo(selectedUserPo.getPoValue()-workDoneValue);
                }
            }
        } 
    }
    
    public void createAspPo(){
        if(selected!=null){
            updateValues();
            create();
            prepareCreate();
            try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/systemadmin/asp/view/view_asp_po.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void deletePo() {
        selected = selectedUserPo;
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspServicePoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            selectedUserPo = null;
            items = null;    // Invalidate list of items to trigger re-query.
            selectedUserPoMatchingExtraWork = null;
            selectedUserPo = null;
        }
    }

    public void updateEdit(){
        updateValuesEdit();
        getFacade().edit(selectedUserPo);
        JsfUtil.addSuccessMessage("PO Updated");
    }
    
     public void correlateExtraWork(){
        if(selectedUserPoSelectedCorrelation!=null && selectedUserPo!=null){
            for (ExtraWork extraWork : selectedUserPoSelectedCorrelation) {
                // Add Extra Work Correlation
                if(selectedUserPo.getExtraWorkCollection()!=null){
                    selectedUserPo.getExtraWorkCollection().add(extraWork);
                }else{
                    selectedUserPo.setExtraWorkCollection(new ArrayList<ExtraWork>());
                    selectedUserPo.getExtraWorkCollection().add(extraWork);
                }
                // Add Work done
                workDoneController.prepareCreate();
                workDoneController.getSelected().setPoNumber(selectedUserPo);
                workDoneController.getSelected().setWorkDoneDate(extraWork.getActivityDate());
                workDoneController.getSelected().setWorkDoneValue(extraWork.getTotalPriceAsp());
                workDoneController.updateValues(false);
                workDoneController.createWorkDone();
                
                extraWork.setCorrelated(Boolean.TRUE);
                extraWorkController.setSelected(extraWork);
                extraWorkController.update();
                
            }
        }
        selectedUserPoMatchingExtraWork = null;
        workDoneController.setSelected(null);
        extraWorkController.setSelected(null);
    }
     
     public void updateVFPrice(){
         if(selectedUserPo!=null){
             
         }
     }
}
