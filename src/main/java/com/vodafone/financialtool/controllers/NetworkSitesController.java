package com.vodafone.financialtool.controllers;

import com.vodafone.financialtool.entities.NetworkSites;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.controllers.util.JsfUtil.PersistAction;
import com.vodafone.financialtool.beans.NetworkSitesFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("networkSitesController")
@SessionScoped
public class NetworkSitesController implements Serializable {

    @EJB
    private com.vodafone.financialtool.beans.NetworkSitesFacade ejbFacade;
    private List<NetworkSites> items = null;
    private NetworkSites selected;

    public NetworkSitesController() {
    }

    public NetworkSites getSelected() {
        return selected;
    }

    public void setSelected(NetworkSites selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private NetworkSitesFacade getFacade() {
        return ejbFacade;
    }

    public NetworkSites prepareCreate() {
        selected = new NetworkSites();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("NetworkSitesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("NetworkSitesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("NetworkSitesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<NetworkSites> getItems() {
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

    public NetworkSites getNetworkSites(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<NetworkSites> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<NetworkSites> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("NetworkSitesControllerConverter")
    public static class NetworkSitesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NetworkSitesController controller = (NetworkSitesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "networkSitesController");
            return controller.getNetworkSites(getKey(value));
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
            if (object instanceof NetworkSites) {
                NetworkSites o = (NetworkSites) object;
                return getStringKey(o.getSiteId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), NetworkSites.class.getName()});
                return null;
            }
        }

    }

    public List<NetworkSites> autoCompleteSite(String query) {
        List<NetworkSites> allSites = getItems();
        List<NetworkSites> filteredSites = new ArrayList<>();
         
        for (int i = 0; i < allSites.size(); i++) {
            NetworkSites code = allSites.get(i);
            if(code.getSiteId().toLowerCase().contains(query)) {
                filteredSites.add(code);
            }
        }
        if(filteredSites.isEmpty()){
            filteredSites.add(new NetworkSites(query));
        }
        return filteredSites;
    }
}
