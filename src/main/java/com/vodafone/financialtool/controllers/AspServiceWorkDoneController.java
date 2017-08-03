package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.AspServiceWorkDone;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.AspServiceWorkDoneFacade;
import com.vodafone.financialtool.entities.AspExtraworkWorkDone;
import java.io.IOException;

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

@Named("aspServiceWorkDoneController")
@SessionScoped
public class AspServiceWorkDoneController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.AspServiceWorkDoneFacade ejbFacade;
    private List<AspServiceWorkDone> items = null;
    private AspServiceWorkDone selected;
    
    @Inject
    private UsersController usersController;
    @Inject
    private AspServicePoController servicePoController;

    public AspServiceWorkDoneController() {
    }

    public AspServiceWorkDone getSelected() {
        return selected;
    }

    public void setSelected(AspServiceWorkDone selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspServiceWorkDoneFacade getFacade() {
        return ejbFacade;
    }

    public AspServiceWorkDone prepareCreate() {
        selected = new AspServiceWorkDone();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        selected.setPoNumber(servicePoController.getSelectedUserPo());
        return selected;
    }

    public AspServiceWorkDone create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspServiceWorkDoneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspServiceWorkDoneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspServiceWorkDoneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AspServiceWorkDone> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private AspServiceWorkDone persist(PersistAction persistAction, String successMessage) {
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

    public AspServiceWorkDone getAspServiceWorkDone(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AspServiceWorkDone> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspServiceWorkDone> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AspServiceWorkDone.class)
    public static class AspServiceWorkDoneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspServiceWorkDoneController controller = (AspServiceWorkDoneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspServiceWorkDoneController");
            return controller.getAspServiceWorkDone(getKey(value));
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
            if (object instanceof AspServiceWorkDone) {
                AspServiceWorkDone o = (AspServiceWorkDone) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspServiceWorkDone.class.getName()});
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
        }
    }
    
    public void createWorkDone(){
        if(selected!=null){
            updateValues(true);
            selected = create();
            if(servicePoController.getSelectedUserPo().getAspServiceWorkDoneCollection()!=null){
                servicePoController.getSelectedUserPo().getAspServiceWorkDoneCollection().add(selected);
                
            }else{
                servicePoController.getSelectedUserPo().setAspServiceWorkDoneCollection(Arrays.asList(selected));
            }
            servicePoController.updateEdit();
            prepareCreate();
        }
    }  
    
    public void onRowEdit(RowEditEvent event) {
        setSelected((AspServiceWorkDone) event.getObject());
        updateValues(true);
        update();
        servicePoController.updateEdit();
    }
    
}
