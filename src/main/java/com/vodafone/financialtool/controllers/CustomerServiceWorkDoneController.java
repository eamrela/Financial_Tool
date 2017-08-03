package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.CustomerServiceWorkDone;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.CustomerServiceWorkDoneFacade;

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

@Named("customerServiceWorkDoneController")
@SessionScoped
public class CustomerServiceWorkDoneController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.CustomerServiceWorkDoneFacade ejbFacade;
    private List<CustomerServiceWorkDone> items = null;
    private CustomerServiceWorkDone selected;
    @Inject
    private UsersController usersController;
    @Inject
    private CustomerServicePoController servicePoController;

    public CustomerServiceWorkDoneController() {
    }

    public CustomerServiceWorkDone getSelected() {
        return selected;
    }

    public void setSelected(CustomerServiceWorkDone selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CustomerServiceWorkDoneFacade getFacade() {
        return ejbFacade;
    }

    public CustomerServiceWorkDone prepareCreate() {
        selected = new CustomerServiceWorkDone();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
         selected.setCreationTime(new Date());
         selected.setPoNumber(servicePoController.getSelectedUserPo());
        return selected;
    }

    public CustomerServiceWorkDone create() {
        selected = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomerServiceWorkDoneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomerServiceWorkDoneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomerServiceWorkDoneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CustomerServiceWorkDone> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private CustomerServiceWorkDone persist(PersistAction persistAction, String successMessage) {
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

    public CustomerServiceWorkDone getCustomerServiceWorkDone(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CustomerServiceWorkDone> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CustomerServiceWorkDone> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CustomerServiceWorkDone.class)
    public static class CustomerServiceWorkDoneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerServiceWorkDoneController controller = (CustomerServiceWorkDoneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerServiceWorkDoneController");
            return controller.getCustomerServiceWorkDone(getKey(value));
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
            if (object instanceof CustomerServiceWorkDone) {
                CustomerServiceWorkDone o = (CustomerServiceWorkDone) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CustomerServiceWorkDone.class.getName()});
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
            if(servicePoController.getSelectedUserPo().getCustomerServiceWorkDoneCollection()!=null){
                servicePoController.getSelectedUserPo().getCustomerServiceWorkDoneCollection().add(selected);
            }else{
                servicePoController.getSelectedUserPo().setCustomerServiceWorkDoneCollection(Arrays.asList(selected));
            }
            servicePoController.updateEdit();
            prepareCreate();
        }
    }    
   
    public void onRowEdit(RowEditEvent event) {
        setSelected((CustomerServiceWorkDone) event.getObject());
        updateValues(true);
        update();
        servicePoController.updateEdit();
    }
}
