/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.CustomerExtraworkPo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class CustomerExtraworkPoFacade extends AbstractFacade<CustomerExtraworkPo> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerExtraworkPoFacade() {
        super(CustomerExtraworkPo.class);
    }

    public List<CustomerExtraworkPo> findAvailableEalyStart() {
       return em.createNativeQuery(" select * " +
                                   " from customer_extrawork_po " +
                                   " where early_start is true " + 
                                   " and remaining_from_po > 100 ", CustomerExtraworkPo.class).getResultList();
    }
    
}
