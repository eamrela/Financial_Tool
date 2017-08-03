package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.CustomerExtraworkMd;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.CustomerExtraworkMdFacade;
import com.vodafone.financialtool.entities.CustomerServiceMd;

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

@Named("customerExtraworkMdController")
@SessionScoped
public class CustomerExtraworkMdController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.CustomerExtraworkMdFacade ejbFacade;
    private List<CustomerExtraworkMd> items = null;
    private CustomerExtraworkMd selected;
    @Inject
    private UsersController usersController;
    @Inject
    private CustomerExtraworkPoController extraWorkPoController;

    public CustomerExtraworkMdController() {
    }

    public CustomerExtraworkMd getSelected() {
        return selected;
    }

    public void setSelected(CustomerExtraworkMd selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CustomerExtraworkMdFacade getFacade() {
        return ejbFacade;
    }

    public CustomerExtraworkMd prepareCreate() {
        selected = new CustomerExtraworkMd();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        selected.setPoNumber(extraWorkPoController.getSelectedUserPo());
        return selected;
    }

    public CustomerExtraworkMd create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkMdCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkMdUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkMdDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CustomerExtraworkMd> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private CustomerExtraworkMd persist(PersistAction persistAction, String successMessage) {
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

    public CustomerExtraworkMd getCustomerExtraworkMd(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CustomerExtraworkMd> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CustomerExtraworkMd> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CustomerExtraworkMd.class)
    public static class CustomerExtraworkMdControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerExtraworkMdController controller = (CustomerExtraworkMdController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerExtraworkMdController");
            return controller.getCustomerExtraworkMd(getKey(value));
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
            if (object instanceof CustomerExtraworkMd) {
                CustomerExtraworkMd o = (CustomerExtraworkMd) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CustomerExtraworkMd.class.getName()});
                return null;
            }
        }

    }

      public boolean validateMdValue(){
        if(selected!=null){
            if(selected.getPoNumber()!=null && selected.getMdValue()!=null){
                if((selected.getMdValue()>selected.getPoNumber().getMdDeserved())||selected.getMdValue()==0.0){
                    selected.setMdValue(0.0);
                    JsfUtil.addErrorMessage("MD Value can't exceed the MD Deserved");
                    return false;
                }else{
                return true;
                }
            }
        }
        return false;
    }
    
    public void createMd(){
        if(selected!=null){
            if(validateMdValue()){
            selected = create();
            if(extraWorkPoController.getSelectedUserPo().getCustomerExtraworkMdCollection()!=null){
                extraWorkPoController.getSelectedUserPo().getCustomerExtraworkMdCollection().add(selected);
            }else{
                extraWorkPoController.getSelectedUserPo().setCustomerExtraworkMdCollection(Arrays.asList(selected));
            }
            extraWorkPoController.updateEdit();
            prepareCreate();
        }
        }
    }
    
    public void onRowEdit(RowEditEvent event) {
        setSelected((CustomerExtraworkMd) event.getObject());
        update();
    }
}
