/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.ActivityCode;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class ActivityCodeFacade extends AbstractFacade<ActivityCode> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityCodeFacade() {
        super(ActivityCode.class);
    }

    public List<ActivityCode> findAllByUser(String company) {
//        if(company.equals("Ericsson")){
//        return em.createNativeQuery("select * from activity_code", ActivityCode.class).getResultList();
//        }else{
//        return em.createNativeQuery("select * from activity_code where asp='"+company+"' ", ActivityCode.class).getResultList();
//        }
        return em.createNativeQuery("select * from activity_code", ActivityCode.class).getResultList();
    }
    
}
