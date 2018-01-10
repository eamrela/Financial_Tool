/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.AspExtraworkPo;
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
public class AspExtraworkPoFacade extends AbstractFacade<AspExtraworkPo> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AspExtraworkPoFacade() {
        super(AspExtraworkPo.class);
    }

    public List<AspExtraworkPo> findMatchingPOs(CustomerExtraworkPo selectedUserPo) {
        return em.createNativeQuery(" select * " +
                                    " from asp_extrawork_po asppo " +
                                    " where  po_number not in " +
                                    " (select asp_extrawork_po_id from customer_extrawork_po_j_asp_extrawork_po) " +
                                    " and  " +
                                    " COALESCE(((select COALESCE((sum(total_price_vendor)-sum(total_price_asp))/sum(total_price_vendor),null) " +
                                    " from extra_work " +
                                    " where id in (select  extrawork_id from asp_extrawork_po_j_extrawork "
                                    + "where asp_extrawork_po_id = asppo.po_number))*po_value)+po_value,po_value) <= "
                                    +selectedUserPo.getRemainingFromPo()
                                    , AspExtraworkPo.class).getResultList();
    }

    public AspExtraworkPo merge(AspExtraworkPo selected) {
        return em.merge(selected);
    }
    
}
