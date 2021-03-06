package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.TimeReporting;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.TimeReportingFacade;
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

@Named("timeReportingController")
@SessionScoped
public class TimeReportingController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.TimeReportingFacade ejbFacade;
    private List<TimeReporting> items = null;
    private TimeReporting selected;
    private TimeReporting selectedUserTr;
    
    @Inject
    private UsersController usersController;

    public TimeReportingController() {
    }

    public TimeReporting getSelected() {
        return selected;
    }

    public void setSelected(TimeReporting selected) {
        this.selected = selected;
    }

    public TimeReporting getSelectedUserTr() {
        return selectedUserTr;
    }

    public void setSelectedUserTr(TimeReporting selectedUserTr) {
        this.selectedUserTr = selectedUserTr;
    }

    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TimeReportingFacade getFacade() {
        return ejbFacade;
    }

    public TimeReporting prepareCreate() {
        selected = new TimeReporting();
        initializeEmbeddableKey();
        selected.setCreator(usersController.getLoggedInUser());
        selected.setCreationTime(new Date());
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TimeReportingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TimeReportingUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TimeReportingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<TimeReporting> getItems() {
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

    public TimeReporting getTimeReporting(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<TimeReporting> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TimeReporting> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = TimeReporting.class)
    public static class TimeReportingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TimeReportingController controller = (TimeReportingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "timeReportingController");
            return controller.getTimeReporting(getKey(value));
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
            if (object instanceof TimeReporting) {
                TimeReporting o = (TimeReporting) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TimeReporting.class.getName()});
                return null;
            }
        }

    }

    public void createTR(){
        if(selected!=null){
            create();
            prepareCreate();
            try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/FinancialTool/app/systemadmin/time_reporting/view/view_time_reporting.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExtraWorkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     public void deleteTr() {
        selected = selectedUserTr;
        persist(PersistAction.DELETE, "TR Deleted");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            selectedUserTr = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
     
     public void updateEdit(){
        getFacade().edit(selectedUserTr);
        JsfUtil.addSuccessMessage("JV Updated");
    }
}
