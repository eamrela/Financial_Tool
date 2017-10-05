package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.beans.CreditNoteFacade;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.entities.CreditNote;
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

@Named("creditNoteController")
@SessionScoped
public class CreditNoteController implements Serializable {

    @EJB
    private CreditNoteFacade ejbFacade;
    private List<CreditNote> items = null;
    private CreditNote selected;
    private CreditNote selectedUserCN;

     @Inject
    private UsersController usersController;
     
    public CreditNoteController() {
    }

    public CreditNote getSelected() {
        return selected;
    }

    public CreditNote getSelectedUserCN() {
        return selectedUserCN;
    }

    public void setSelectedUserCN(CreditNote selectedUserCN) {
        this.selectedUserCN = selectedUserCN;
    }

    
    public void setSelected(CreditNote selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CreditNoteFacade getFacade() {
        return ejbFacade;
    }

    public CreditNote prepareCreate() {
        selected = new CreditNote();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CreditNoteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CreditNoteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CreditNoteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            selectedUserCN = null;
        }
    }

    public List<CreditNote> getItems() {
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

    public CreditNote getCreditNote(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CreditNote> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CreditNote> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CreditNote.class)
    public static class CreditNoteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CreditNoteController controller = (CreditNoteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "creditNoteController");
            return controller.getCreditNote(getKey(value));
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
            if (object instanceof CreditNote) {
                CreditNote o = (CreditNote) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CreditNote.class.getName()});
                return null;
            }
        }

    }

    public void createCN(){
        if(selected!=null){
            create();
            prepareCreate();
            try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/systemadmin/credit_note/view/view_cn.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateEdit(){
        getFacade().edit(selectedUserCN);
        JsfUtil.addSuccessMessage("CN Updated");
    }
    
    public void deleteCN() {
        selected = selectedUserCN;
        persist(PersistAction.DELETE, "CN Deleted");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            selectedUserCN = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
}
