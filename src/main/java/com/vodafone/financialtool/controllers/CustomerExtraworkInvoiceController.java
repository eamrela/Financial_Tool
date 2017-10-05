package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.CustomerExtraworkInvoice;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.CustomerExtraworkInvoiceFacade;
import com.vodafone.financialtool.entities.CustomerExtraworkPo;

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

@Named("customerExtraworkInvoiceController")
@SessionScoped
public class CustomerExtraworkInvoiceController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.CustomerExtraworkInvoiceFacade ejbFacade;
    private List<CustomerExtraworkInvoice> items = null;
    private CustomerExtraworkInvoice selected;
    
    @Inject
    private UsersController usersController;
    @Inject
    private CustomerExtraworkPoController extraWorkPoController;

    public CustomerExtraworkInvoiceController() {
    }

    public CustomerExtraworkInvoice getSelected() {
        return selected;
    }

    public void setSelected(CustomerExtraworkInvoice selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CustomerExtraworkInvoiceFacade getFacade() {
        return ejbFacade;
    }

    public CustomerExtraworkInvoice prepareCreate() {
        selected = new CustomerExtraworkInvoice();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationDate(new Date());
        selected.setPoNumer(extraWorkPoController.getSelectedUserPo());
        return selected;
    }

    public CustomerExtraworkInvoice create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkInvoiceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkInvoiceUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomerExtraworkInvoiceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CustomerExtraworkInvoice> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private CustomerExtraworkInvoice persist(PersistAction persistAction, String successMessage) {
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

    public CustomerExtraworkInvoice getCustomerExtraworkInvoice(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CustomerExtraworkInvoice> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CustomerExtraworkInvoice> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CustomerExtraworkInvoice.class)
    public static class CustomerExtraworkInvoiceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerExtraworkInvoiceController controller = (CustomerExtraworkInvoiceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerExtraworkInvoiceController");
            return controller.getCustomerExtraworkInvoice(getKey(value));
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
            if (object instanceof CustomerExtraworkInvoice) {
                CustomerExtraworkInvoice o = (CustomerExtraworkInvoice) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CustomerExtraworkInvoice.class.getName()});
                return null;
            }
        }

    }

   public boolean validateInvoiceValue(){
        if(selected!=null){
            if(selected.getPoNumer()!=null && selected.getInvoiceValue()!=null){
                if((selected.getInvoiceValue()>selected.getPoNumer().getInvoiceDeserved())||selected.getInvoiceValue()==0.0){
                    selected.setInvoiceValue(0.0);
                    JsfUtil.addErrorMessage("Invoice Value can't exceed the Invoice Deserved");
                    return false;
                }else{
                return true;
                }
            }
        }
        return false;
    }
    
    public void createInvoice(){
        if(selected!=null){
            if(validateInvoiceValue()){
            selected = create();
            if(extraWorkPoController.getSelectedUserPo().getCustomerExtraworkInvoiceCollection()!=null){
                extraWorkPoController.getSelectedUserPo().getCustomerExtraworkInvoiceCollection().add(selected);
            }else{
                extraWorkPoController.getSelectedUserPo().setCustomerExtraworkInvoiceCollection(Arrays.asList(selected));
            }
            extraWorkPoController.updateEdit();
            prepareCreate();
        }
        }
    }
    
    public void onRowEdit(RowEditEvent event) {
        
        setSelected((CustomerExtraworkInvoice) event.getObject());
        update();
    }
}
