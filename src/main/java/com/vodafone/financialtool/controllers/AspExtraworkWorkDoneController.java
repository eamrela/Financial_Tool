package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.AspExtraworkWorkDone;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.AspExtraworkWorkDoneFacade;
import com.vodafone.financialtool.entities.AspExtraworkGrn;

import java.io.Serializable;
import java.util.Arrays;
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
import org.primefaces.event.RowEditEvent;

@Named("aspExtraworkWorkDoneController")
@SessionScoped
public class AspExtraworkWorkDoneController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.AspExtraworkWorkDoneFacade ejbFacade;
    private List<AspExtraworkWorkDone> items = null;
    private AspExtraworkWorkDone selected;

    @Inject
    private UsersController usersController;
    @Inject
    private AspExtraworkPoController extraWorkPoController;
    
    public AspExtraworkWorkDoneController() {
    }

    public AspExtraworkWorkDone getSelected() {
        return selected;
    }

    public void setSelected(AspExtraworkWorkDone selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspExtraworkWorkDoneFacade getFacade() {
        return ejbFacade;
    }

    public AspExtraworkWorkDone prepareCreate() {
        selected = new AspExtraworkWorkDone();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        selected.setPoNumber(extraWorkPoController.getSelectedUserPo());
        return selected;
    }

    public AspExtraworkWorkDone create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkWorkDoneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkWorkDoneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkWorkDoneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AspExtraworkWorkDone> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private AspExtraworkWorkDone persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
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
        return selected;
    }

    public AspExtraworkWorkDone getAspExtraworkWorkDone(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AspExtraworkWorkDone> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspExtraworkWorkDone> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AspExtraworkWorkDone.class)
    public static class AspExtraworkWorkDoneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspExtraworkWorkDoneController controller = (AspExtraworkWorkDoneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspExtraworkWorkDoneController");
            return controller.getAspExtraworkWorkDone(getKey(value));
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
            if (object instanceof AspExtraworkWorkDone) {
                AspExtraworkWorkDone o = (AspExtraworkWorkDone) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspExtraworkWorkDone.class.getName()});
                return null;
            }
        }

    }

   public void updateValues(boolean workDoneValue){
        if(selected!=null){
           if(selected.getWorkDonePercentage()!=null && !workDoneValue){
               selected.setWorkDoneValue(selected.getPoNumber().getPoValue()*(selected.getWorkDonePercentage()/100));
           }
           if(selected.getWorkDoneValue()!=null && workDoneValue){
               selected.setWorkDonePercentage((selected.getWorkDoneValue()/selected.getPoNumber().getPoValue())*100);
           }
           validateWorkDone();
        }
    }
    
   public void createWorkDone(){
    if(selected!=null){
        if(validateWorkDone()){
            updateValues(true);
            selected = create();
            if(extraWorkPoController.getSelectedUserPo().getAspExtraworkWorkDoneCollection()!=null){
                extraWorkPoController.getSelectedUserPo().getAspExtraworkWorkDoneCollection().add(selected);
            }else{
                extraWorkPoController.getSelectedUserPo().setAspExtraworkWorkDoneCollection(Arrays.asList(selected));
            }
            extraWorkPoController.updateEdit();
            prepareCreate();
        }
    }
    }  
     
   public void onRowEdit(RowEditEvent event) {
        setSelected((AspExtraworkWorkDone) event.getObject());
        if(validateWorkDone()){
        updateValues(true);
        update();
        extraWorkPoController.updateEdit();
        }
    }
   
   public boolean validateWorkDone(){
       if(selected!=null){
           if(selected.getWorkDoneValue()>selected.getPoNumber().getRemainingFromPo()){
               selected.setWorkDoneValue(0.0);
               JsfUtil.addErrorMessage("Workdone value can't exceed remaining from PO");
               return false;
           }else{
               return true;
           }
       }
       return false;
   }
}
