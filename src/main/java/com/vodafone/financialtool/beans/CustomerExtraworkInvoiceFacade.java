/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.CustomerExtraworkInvoice;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class CustomerExtraworkInvoiceFacade extends AbstractFacade<CustomerExtraworkInvoice> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerExtraworkInvoiceFacade() {
        super(CustomerExtraworkInvoice.class);
    }

    public CustomerExtraworkInvoice merge(CustomerExtraworkInvoice selected) {
        return em.merge(selected);
    }
    
}
