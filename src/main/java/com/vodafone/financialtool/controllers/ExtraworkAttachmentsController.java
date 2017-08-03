package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.ExtraworkAttachments;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.ExtraworkAttachmentsFacade;
import com.vodafone.financialtool.entities.Configurations;
import com.vodafone.financialtool.entities.ConfigurationsPK;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.nio.file.Files;
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
import javax.servlet.http.Part;

@Named("extraworkAttachmentsController")
@SessionScoped
public class ExtraworkAttachmentsController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.ExtraworkAttachmentsFacade ejbFacade;
    private List<ExtraworkAttachments> items = null;
    private ExtraworkAttachments selected;
    
   
    
    
    
    public ExtraworkAttachmentsController() {
    }

   

    
    public ExtraworkAttachments getSelected() {
        return selected;
    }

    public void setSelected(ExtraworkAttachments selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ExtraworkAttachmentsFacade getFacade() {
        return ejbFacade;
    }

    public ExtraworkAttachments prepareCreate() {
        selected = new ExtraworkAttachments();
        initializeEmbeddableKey();
        return selected;
    }

    public ExtraworkAttachments create() {
        selected =  persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ExtraworkAttachmentsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return selected;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ExtraworkAttachmentsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ExtraworkAttachmentsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ExtraworkAttachments> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private ExtraworkAttachments persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
                return selected;
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
        return null;
    }

    public ExtraworkAttachments getExtraworkAttachments(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ExtraworkAttachments> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ExtraworkAttachments> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ExtraworkAttachments.class)
    public static class ExtraworkAttachmentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ExtraworkAttachmentsController controller = (ExtraworkAttachmentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "extraworkAttachmentsController");
            return controller.getExtraworkAttachments(getKey(value));
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
            if (object instanceof ExtraworkAttachments) {
                ExtraworkAttachments o = (ExtraworkAttachments) object;
                return getStringKey(o.getAttachmentId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ExtraworkAttachments.class.getName()});
                return null;
            }
        }

    }

   
}
