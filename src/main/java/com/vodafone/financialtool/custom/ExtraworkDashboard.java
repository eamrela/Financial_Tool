/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.controllers.UsersController;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *
 * @author eamrela
 */
@Named("extraWorkdashboardController")
@SessionScoped
public class ExtraworkDashboard implements Serializable {
    
    @PersistenceContext(unitName = "com.vodafone_FinancialTool_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DateTime now;
    
    
    @Inject
    private UsersController usersController;
    
    //<editor-fold defaultstate="collapsed" desc="Figures">
    private Double totalCreatedASP=0.0;
    private Double totalApprovedASP=0.0;
    private Double totalRejectedASP=0.0;
    private Double totalNotAssignedASP=0.0;
    
    private Double totalCreatedEricsson=0.0;
    private Double totalPendingBP=0.0;
    private Double totalPendingRO=0.0;
    private Double totalPendingDO=0.0;

    private List<Object[]> usersPending;
//</editor-fold>
    
    private HorizontalBarChartModel horizontalBarModel;
    
    public void initialize(){
        now = DateTime.now();
        populateFigures();
        createPendingChart();
      
    }
    
    public void populateFigures(){
        try{
            totalCreatedASP = ((Long)em.createNativeQuery(
                    " select count(*) from extra_work where creator in " +
                    " (select username from users_j_roles where role_name like '%ASP%') " +
                    " and domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") ").getSingleResult()).doubleValue();
            System.out.println(" select count(*) from extra_work where creator in " +
                    " (select username from users_j_roles where role_name like '%ASP%') " +
                    " and domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") ");
            
            totalApprovedASP = ((Long)em.createNativeQuery("select count(*) from extra_work where creator in " +
                    " (select username from users_j_roles where role_name like '%ASP%') " +
                    " and domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") "
                            + " and business_provider_approval is true ").getSingleResult()).doubleValue();
            
            totalRejectedASP = ((Long)em.createNativeQuery("select count(*) from extra_work where creator in " +
                    " (select username from users_j_roles where role_name like '%ASP%') " +
                    " and domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") "
                            + " and business_provider_approval is false ").getSingleResult()).doubleValue();
            totalNotAssignedASP = ((Long)em.createNativeQuery("select count(*) from extra_work where creator in " +
                    " (select username from users_j_roles where role_name like '%ASP%') " +
                    " and domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") "
                            + " and creator = assignment_group").getSingleResult()).doubleValue();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            totalCreatedEricsson = ((Long)em.createNativeQuery(
                    " select count(*) from extra_work where creator in " +
                    " (select username from users_j_roles where role_name not like '%ASP%') " +
                    " and domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") ").getSingleResult()).doubleValue();
            totalPendingBP = ((Long)em.createNativeQuery(
                    " select count(*) from extra_work where domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") "
                            + " and business_provider_approval is false ").getSingleResult()).doubleValue();
            totalPendingRO = ((Long)em.createNativeQuery(
                    " select count(*) from extra_work where domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") "
                            + " and business_provider_approval is true and region_approval is false ").getSingleResult()).doubleValue();
            totalPendingDO = ((Long)em.createNativeQuery(
                    " select count(*) from extra_work where domain_name in ("+usersController.getLoggedInUserDomains()+") " +
                    " and region in ("+usersController.getLoggedInUserRegions()+") "
                            + " and business_provider_approval is true and region_approval is true "
                            + " and domain_owner_approval is false ").getSingleResult()).doubleValue();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void createPendingChart(){
        horizontalBarModel = new HorizontalBarChartModel();
 
        List<Object[]> usersPending = em.createNativeQuery("select assignment_group,count(assignment_group) from extra_work \n" +
                                                            "where business_provider_approval is false\n" +
                                                            "and region_approval is false\n" +
                                                            "and domain_owner_approval is false\n" +
                                                            "group by assignment_group").getResultList();
        ChartSeries users = new ChartSeries();
        users.setLabel("UserName");
        for (Object[] usersPending1 : usersPending) {
             users.set(usersPending1[0], (Number) usersPending1[1]);
        }
        
       
       

 
        horizontalBarModel.addSeries(users);
         
        horizontalBarModel.setTitle("Pending/User");
        horizontalBarModel.setLegendPosition("e");
         
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("# of Requests");
        xAxis.setMin(0);
        xAxis.setMax(100);
        xAxis.setTickInterval("1");
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("User"); 
    }

    public List<Object[]> getUsersPending() {
        usersPending = em.createNativeQuery("select assignment_group,count(assignment_group) from extra_work \n" +
                                                            "where business_provider_approval is false\n" +
                                                            "and region_approval is false\n" +
                                                            "and domain_owner_approval is false\n" +
                                                            "group by assignment_group").getResultList();
        return usersPending;
    }

    public void setUsersPending(List<Object[]> usersPending) {
        this.usersPending = usersPending;
    }
    
    


    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public Double getTotalCreatedASP() {
        return totalCreatedASP;
    }

    public void setTotalCreatedASP(Double totalCreatedASP) {
        this.totalCreatedASP = totalCreatedASP;
    }

    public Double getTotalApprovedASP() {
        return totalApprovedASP;
    }

    public void setTotalApprovedASP(Double totalApprovedASP) {
        this.totalApprovedASP = totalApprovedASP;
    }

    public Double getTotalRejectedASP() {
        return totalRejectedASP;
    }

    public void setTotalRejectedASP(Double totalRejectedASP) {
        this.totalRejectedASP = totalRejectedASP;
    }

    public Double getTotalNotAssignedASP() {
        return totalNotAssignedASP;
    }

    public void setTotalNotAssignedASP(Double totalNotAssignedASP) {
        this.totalNotAssignedASP = totalNotAssignedASP;
    }

    public Double getTotalCreatedEricsson() {
        return totalCreatedEricsson;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    
    public void setTotalCreatedEricsson(Double totalCreatedEricsson) {
        this.totalCreatedEricsson = totalCreatedEricsson;
    }

    public Double getTotalPendingBP() {
        return totalPendingBP;
    }

    public void setTotalPendingBP(Double totalPendingBP) {
        this.totalPendingBP = totalPendingBP;
    }

    public Double getTotalPendingRO() {
        return totalPendingRO;
    }

    public void setTotalPendingRO(Double totalPendingRO) {
        this.totalPendingRO = totalPendingRO;
    }

    public Double getTotalPendingDO() {
        return totalPendingDO;
    }

    
    public void setTotalPendingDO(Double totalPendingDO) {
        this.totalPendingDO = totalPendingDO;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public DateTime getNow() {
        return now;
    }

    public void setNow(DateTime now) {
        this.now = now;
    }
//</editor-fold>
}
