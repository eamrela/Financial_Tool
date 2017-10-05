/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.beans;

import com.vodafone.financialtool.entities.AspExtraworkPo;
import com.vodafone.financialtool.entities.ExtraWork;
import com.vodafone.financialtool.entities.Users;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class ExtraWorkFacade extends AbstractFacade<ExtraWork> {

    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExtraWorkFacade() {
        super(ExtraWork.class);
    }

    public ExtraWork merge(ExtraWork selected) {
        return em.merge(selected);
    }

    public List<ExtraWork> findUserItems(String loggedInUserRole, String loggedInUserRegions, String loggedInUserDomains,String userName,String company) {
        if(loggedInUserRole.contains("ASP") && !loggedInUserRole.contains("ADMIN")){
            return em.createNativeQuery("select * from extra_work where creator='"+userName+"' or assignment_group = '"+userName+"'", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("ASP_ADMIN")){
            return em.createNativeQuery("select * from extra_work where "
                    + " (creator in (select user_name from users where company='"+company+"')) "
                            + "or (assignment_group in (select user_name from users where company='"+company+"'))", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("SYSADMIN")){
            return em.createNativeQuery("select * from extra_work ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("BUDGET_CONTROLLER")){
            return em.createNativeQuery("select * from extra_work ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("BP")){
            return em.createNativeQuery("select * from extra_work where (creator='"+userName+"' or assignment_group = '"+userName+"') ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("RO")){
            return em.createNativeQuery("select * from extra_work where (creator='"+userName+"' or assignment_group = '"+userName+"') "
                    + "or (region in ("+loggedInUserRegions+") )", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("DO")){
          return em.createNativeQuery("select * from extra_work where (creator='"+userName+"' or assignment_group = '"+userName+"') "
                    + "or (domain_name in ("+loggedInUserDomains+") )", ExtraWork.class).getResultList();
        }
        return null;
    }

    public List<ExtraWork> findUserItemsByDate(String loggedInUserRole, String loggedInUserRegions, String loggedInUserDomains, String userName, Date start, Date end,String company) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(loggedInUserRole.contains("ASP") && !loggedInUserRole.contains("ADMIN")){
            return em.createNativeQuery("select * from extra_work where (creator='"+userName+"' or assignment_group = '"+userName+"') "
                    + " and (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' ) ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("ASP_ADMIN")){
            return em.createNativeQuery("select * from extra_work where "
                    + " ((creator in (select user_name from users where company='"+company+"')) "
                            + "or (assignment_group in (select user_name from users where company='"+company+"'))) "
                                    + "  and (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' ) ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("SYSADMIN")){
            return em.createNativeQuery("select * from extra_work  where (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' )", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("BUDGET_CONTROLLER")){
            return em.createNativeQuery("select * from extra_work where  (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' )", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("BP")){
            return em.createNativeQuery("select * from extra_work where (creator='"+userName+"' or assignment_group = '"+userName+"') "
                    + "  and (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' ) ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("RO")){
            return em.createNativeQuery("select * from extra_work where ((creator='"+userName+"' or assignment_group = '"+userName+"') "
                    + "or (region in ("+loggedInUserRegions+") )) "
                            + "  and (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' ) ", ExtraWork.class).getResultList();
        }
        if(loggedInUserRole.contains("DO")){
          return em.createNativeQuery("select * from extra_work where ((creator='"+userName+"' or assignment_group = '"+userName+"') "
                    + "or (domain_name in ("+loggedInUserDomains+") )) "
                            + "  and (activity_date between '"+sdf.format(start)+"' and '"+sdf.format(end)+"' ) ", ExtraWork.class).getResultList();
        }
        return null;    
    }

    public List<ExtraWork> findItemsMatchingPo(AspExtraworkPo selectedUserPo) {
        return em.createNativeQuery("select * from extra_work where asp='"+selectedUserPo.getSubcontractor()+"' "
                + "  and correlated is false  and total_price_asp < "+selectedUserPo.getRemainingFromPo(), ExtraWork.class).getResultList();
    }

    
    
}
