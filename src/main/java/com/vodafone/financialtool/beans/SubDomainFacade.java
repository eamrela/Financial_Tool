/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.SubDomain;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class SubDomainFacade extends AbstractFacade<SubDomain> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubDomainFacade() {
        super(SubDomain.class);
    }

    public List<SubDomain> findExtaWork() {
        return em.createNativeQuery("select * from sub_domain where applicable='Extra_Work'", SubDomain.class).getResultList();
    }
    
}
