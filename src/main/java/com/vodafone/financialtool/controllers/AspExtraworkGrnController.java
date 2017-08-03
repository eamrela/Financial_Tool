package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.AspExtraworkGrn;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.AspExtraworkGrnFacade;

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

@Named("aspExtraworkGrnController")
@SessionScoped
public class AspExtraworkGrnController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.AspExtraworkGrnFacade ejbFacade;
    private List<AspExtraworkGrn> items = null;
    private AspExtraworkGrn selected;

    @Inject
    private UsersController usersController;
    @Inject
    private AspExtraworkPoController extraWorkPoController;
    
    public AspExtraworkGrnController() {
    }

    public AspExtraworkGrn getSelected() {
        return selected;
    }

    public void setSelected(AspExtraworkGrn selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AspExtraworkGrnFacade getFacade() {
        return ejbFacade;
    }

    public AspExtraworkGrn prepareCreate() {
        selected = new AspExtraworkGrn();
        initializeEmbeddableKey();
         selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        selected.setPoNumber(extraWorkPoController.getSelectedUserPo());
        return selected;
    }

    public AspExtraworkGrn create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkGrnCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkGrnUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AspExtraworkGrnDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AspExtraworkGrn> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private AspExtraworkGrn persist(PersistAction persistAction, String successMessage) {
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

    public AspExtraworkGrn getAspExtraworkGrn(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AspExtraworkGrn> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AspExtraworkGrn> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AspExtraworkGrn.class)
    public static class AspExtraworkGrnControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AspExtraworkGrnController controller = (AspExtraworkGrnController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aspExtraworkGrnController");
            return controller.getAspExtraworkGrn(getKey(value));
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
            if (object instanceof AspExtraworkGrn) {
                AspExtraworkGrn o = (AspExtraworkGrn) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AspExtraworkGrn.class.getName()});
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
            if(extraWorkPoController.getSelectedUserPo().getAspExtraworkGrnCollection()!=null){
                extraWorkPoController.getSelectedUserPo().getAspExtraworkGrnCollection().add(selected);
            }else{
                extraWorkPoController.getSelectedUserPo().setAspExtraworkGrnCollection(Arrays.asList(selected));
            }
            extraWorkPoController.updateEdit();
            prepareCreate();
        }
        }
    }
    
     public void onRowEdit(RowEditEvent event) {
        setSelected((AspExtraworkGrn) event.getObject());
        update();
    }
}
