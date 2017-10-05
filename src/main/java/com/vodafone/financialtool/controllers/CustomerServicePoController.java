package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.CustomerServicePo;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.CustomerServicePoFacade;
import com.vodafone.financialtool.entities.CustomerServiceWorkDone;
import java.io.IOException;

import java.io.Serializable;
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

@Named("customerServicePoController")
@SessionScoped
public class CustomerServicePoController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.CustomerServicePoFacade ejbFacade;
    private List<CustomerServicePo> items = null;
    private CustomerServicePo selected;
    private CustomerServicePo selectedUserPo;
    
    @Inject
    private UsersController usersController;

    public CustomerServicePoController() {
    }

    public CustomerServicePo getSelected() {
        return selected;
    }

    public void setSelected(CustomerServicePo selected) {
        this.selected = selected;
    }

    public CustomerServicePo getSelectedUserPo() {
        return selectedUserPo;
    }

    public void setSelectedUserPo(CustomerServicePo selectedUserPo) {
        this.selectedUserPo = selectedUserPo;
    }

    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CustomerServicePoFacade getFacade() {
        return ejbFacade;
    }

    public CustomerServicePo prepareCreate() {
        selected = new CustomerServicePo();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomerServicePoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomerServicePoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomerServicePoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CustomerServicePo> getItems() {
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

    public CustomerServicePo getCustomerServicePo(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<CustomerServicePo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CustomerServicePo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CustomerServicePo.class)
    public static class CustomerServicePoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerServicePoController controller = (CustomerServicePoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerServicePoController");
            return controller.getCustomerServicePo(getKey(value));
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
            if (object instanceof CustomerServicePo) {
                CustomerServicePo o = (CustomerServicePo) object;
                return getStringKey(o.getPoNumber());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CustomerServicePo.class.getName()});
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
                if(selectedUserPo.getCustomerServiceWorkDoneCollection()!=null){
                    Object[] workDoneCollection = selectedUserPo.getCustomerServiceWorkDoneCollection().toArray();
                    Double workDoneValue = 0.0;
                    for (Object workDoneCollection1 : workDoneCollection) {
                        workDoneValue += ((CustomerServiceWorkDone) workDoneCollection1).getWorkDoneValue();
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
    
    public void deletePo() {
        selected = selectedUserPo;
        persist(PersistAction.DELETE, "VF PO Deleted");
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
}
