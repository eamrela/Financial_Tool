package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.JvReporting;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.JvReportingFacade;
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

@Named("jvReportingController")
@SessionScoped
public class JvReportingController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.JvReportingFacade ejbFacade;
    private List<JvReporting> items = null;
    private JvReporting selected;
    private JvReporting selectedUserJv;
    
     @Inject
    private UsersController usersController;

    public JvReportingController() {
    }

    public JvReporting getSelected() {
        return selected;
    }

    public void setSelected(JvReporting selected) {
        this.selected = selected;
    }

    public JvReporting getSelectedUserJv() {
        return selectedUserJv;
    }

    public void setSelectedUserJv(JvReporting selectedUserJv) {
        this.selectedUserJv = selectedUserJv;
    }
    
    

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private JvReportingFacade getFacade() {
        return ejbFacade;
    }

    public JvReporting prepareCreate() {
        selected = new JvReporting();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("JvReportingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("JvReportingUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("JvReportingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<JvReporting> getItems() {
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

    public JvReporting getJvReporting(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<JvReporting> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<JvReporting> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = JvReporting.class)
    public static class JvReportingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            JvReportingController controller = (JvReportingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "jvReportingController");
            return controller.getJvReporting(getKey(value));
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
            if (object instanceof JvReporting) {
                JvReporting o = (JvReporting) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), JvReporting.class.getName()});
                return null;
            }
        }

    }
    
    public void createJV(){
        if(selected!=null){
            create();
            prepareCreate();
            try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/common/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteJv() {
        selected = selectedUserJv;
        persist(PersistAction.DELETE, "JV Deleted");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            selectedUserJv = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void updateEdit(){
        getFacade().edit(selectedUserJv);
        JsfUtil.addSuccessMessage("JV Updated");
    }
}
