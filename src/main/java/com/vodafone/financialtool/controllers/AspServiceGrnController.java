package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.AspServiceGrn;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.AspServiceGrnFacade;

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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;

@Named("aspServiceGrnController")
@SessionScoped
public class AspServiceGrnController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.AspServiceGrnFacade ejbFacade;
    private List<AspServiceGrn> items = null;
    private AspServiceGrn selected;
    @Inject
    private UsersController usersController;
    @Inject
    private AspServicePoController servicePoController;

    public AspServiceGrnController() {
    }

    public AspServiceGrn getSelected() {
        return selected;
    }

    public void setSelected(AspServiceGrn selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspServiceGrnFacade getFacade() {
        return ejbFacade;
    }

    public AspServiceGrn prepareCreate() {
        selected = new AspServiceGrn();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        selected.setPoNumber(servicePoController.getSelectedUserPo());
        return selected;
    }

    public AspServiceGrn create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspServiceGrnCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspServiceGrnUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspServiceGrnDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AspServiceGrn> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private AspServiceGrn persist(PersistAction persistAction, String successMessage) {
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

    public AspServiceGrn getAspServiceGrn(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AspServiceGrn> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspServiceGrn> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AspServiceGrn.class)
    public static class AspServiceGrnControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspServiceGrnController controller = (AspServiceGrnController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspServiceGrnController");
            return controller.getAspServiceGrn(getKey(value));
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
            if (object instanceof AspServiceGrn) {
                AspServiceGrn o = (AspServiceGrn) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspServiceGrn.class.getName()});
                return null;
            }
        }

    }

    public boolean validateGrnValue(){
        if(selected!=null){
            if(selected.getPoNumber()!=null && selected.getGrnValue()!=null){
                if((selected.getGrnValue()>selected.getPoNumber().getGrnDeserved())||selected.getGrnValue()==0.0){
                    selected.setGrnValue(0.0);
                    JsfUtil.addErrorMessage("GRN Value can't exceed the GRN Deserved");
                    return false;
                }else{
                return true;
                }
            }
        }
        return false;
    }
    
    public void createGrn(){
        if(selected!=null){
            if(validateGrnValue()){
            selected = create();
            if(servicePoController.getSelectedUserPo().getAspServiceGrnCollection()!=null){
                servicePoController.getSelectedUserPo().getAspServiceGrnCollection().add(selected);
            }else{
                servicePoController.getSelectedUserPo().setAspServiceGrnCollection(Arrays.asList(selected));
            }
            servicePoController.updateEdit();
            prepareCreate();
        }
        }
    }
    
    public void onRowEdit(RowEditEvent event) {
        setSelected((AspServiceGrn) event.getObject());
        update();
    }
}
