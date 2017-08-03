/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.AspExtraworkWorkDone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class AspExtraworkWorkDoneFacade extends AbstractFacade<AspExtraworkWorkDone> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AspExtraworkWorkDoneFacade() {
        super(AspExtraworkWorkDone.class);
    }

    public AspExtraworkWorkDone merge(AspExtraworkWorkDone selected) {
        return em.merge(selected);
    }
    
}
