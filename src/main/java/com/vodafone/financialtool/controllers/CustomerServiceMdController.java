package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.CustomerServiceMd;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.CustomerServiceMdFacade;
import com.vodafone.financialtool.entities.CustomerServiceInvoice;

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

@Named("customerServiceMdController")
@SessionScoped
public class CustomerServiceMdController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.CustomerServiceMdFacade ejbFacade;
    private List<CustomerServiceMd> items = null;
    private CustomerServiceMd selected;
    private CustomerServiceMd selectedUserMd;
    
    @Inject
    private UsersController usersController;
    @Inject
    private CustomerServicePoController servicePoController;
    
    public CustomerServiceMdController() {
    }

    public CustomerServiceMd getSelected() {
        return selected;
    }

    public CustomerServiceMd getSelectedUserMd() {
        return selectedUserMd;
    }

    public void setSelectedUserMd(CustomerServiceMd selectedUserMd) {
        this.selectedUserMd = selectedUserMd;
    }

    
    public void setSelected(CustomerServiceMd selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CustomerServiceMdFacade getFacade() {
        return ejbFacade;
    }

    public CustomerServiceMd prepareCreate() {
        selected = new CustomerServiceMd();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        selected.setPoNumber(servicePoController.getSelectedUserPo());
        return selected;
    }

    public CustomerServiceMd create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomerServiceMdCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomerServiceMdUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomerServiceMdDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CustomerServiceMd> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private CustomerServiceMd persist(PersistAction persistAction, String successMessage) {
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

    public CustomerServiceMd getCustomerServiceMd(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CustomerServiceMd> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CustomerServiceMd> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    

    @FacesConverter(forClass = CustomerServiceMd.class)
    public static class CustomerServiceMdControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerServiceMdController controller = (CustomerServiceMdController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerServiceMdController");
            return controller.getCustomerServiceMd(getKey(value));
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
            if (object instanceof CustomerServiceMd) {
                CustomerServiceMd o = (CustomerServiceMd) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CustomerServiceMd.class.getName()});
                return null;
            }
        }

    }

//    public void updateValuesEdit(){
//       if(selectedUserMd!=null){
//                if(selectedUserMd.getCustomerServiceInvoiceCollection()!=null){
//                    Object[] invoiceCollection = selectedUserMd.getCustomerServiceInvoiceCollection().toArray();
//                    Double invoiceValue = 0.0;
//                    for (Object invoice : invoiceCollection) {
//                        invoiceValue += ((CustomerServiceInvoice) invoice).getInvoiceValue();
//                    }
//                    selectedUserMd.setRemainingFromMd(selectedUserMd.getMdValue()-invoiceValue);
//                }
//        } 
//    }
    
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
            if(servicePoController.getSelectedUserPo().getCustomerServiceMdCollection()!=null){
                servicePoController.getSelectedUserPo().getCustomerServiceMdCollection().add(selected);
            }else{
                servicePoController.getSelectedUserPo().setCustomerServiceMdCollection(Arrays.asList(selected));
            }
            servicePoController.updateEdit();
            prepareCreate();
        }
        }
    }
    
    public void updateEdit() {
//        updateValuesEdit();
        getFacade().edit(selectedUserMd);
        JsfUtil.addSuccessMessage("PO Updated");
    }
    
    public void onRowEdit(RowEditEvent event) {
        setSelected((CustomerServiceMd) event.getObject());
        update();
    }
}
