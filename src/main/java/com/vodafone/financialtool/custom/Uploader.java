/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.controllers.DomainsController;
import com.vodafone.financialtool.controllers.ExtraWorkController;
import com.vodafone.financialtool.controllers.SubDomainController;
import com.vodafone.financialtool.controllers.SubcontractorsController;
import com.vodafone.financialtool.controllers.UsersController;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.entities.ExtraWork;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author eamrela
 */
@Named("uploadManager")
@SessionScoped
public class Uploader implements Serializable{
    
    private String errors;
    
    @Inject
    private SubcontractorsController aspController;
    @Inject
    private DomainsController domainController;
    @Inject
    private SubDomainController subdomainController;
    @Inject
    private UsersController usersController;
    @Inject
    private ExtraWorkController extraWorkController;
    
    public void uploadExtraWork(FileUploadEvent event){
        List<ExtraWork> extras = new ArrayList<>();
        try {
            errors = "";
            XSSFWorkbook myWorkBook = new XSSFWorkbook (event.getFile().getInputstream());
            XSSFSheet sheet = myWorkBook.getSheetAt(0);
            if(sheet!=null){
              Iterator<Row> rowIterator = sheet.iterator();
                ExtraWork extraWork = null;
               while (rowIterator.hasNext()) {
                  Row row = rowIterator.next();
                  int i = 0;
                      extraWork = new ExtraWork();
                      // Site
                      extraWork.setSite(getCellValue(row.getCell(i)));
                      i++;
                      // ASP
                      extraWork.setAsp(getCellValue(row.getCell(i)));
                      i++;
                      //Region
                      extraWork.setRegion(getCellValue(row.getCell(i)));
                      i++;
                      //Customer Owner
                      extraWork.setCustomerOwner(getCellValue(row.getCell(i)));
                      i++;
                      //Approval status
                      extraWork.setApprovalStatus(getCellValue(row.getCell(i)));
                      i++;
                      //Domain
                      extraWork.setDomainName(getCellValue(row.getCell(i)));
                      i++;
                      //sub Domain
                      extraWork.setSubDomain(getCellValue(row.getCell(i)));
                      i++;
                      //Date
                      if(row.getCell(i).getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
                        extraWork.setActivityDate(getDateCellValue(row.getCell(i))); // date
                      }else{
                        errors+="Row:"+(row.getRowNum()+1)+" Activity date is not recognized, Current Value: "+getCellValue(row.getCell(i))+"\n";
                        continue;
                       }
                      i++;
                      //catalog code
                      extraWork.setActivityCode(getCellValue(row.getCell(i)));
                      i++;
                      //catalog Description 
                      extraWork.setActivityDescription(getCellValue(row.getCell(i)));
                      i++;
                      //details 
                      extraWork.setActivityDetails(getCellValue(row.getCell(i)));
                      i++;
                      //qty 
                      extraWork.setQty(Double.parseDouble(getCellValue(row.getCell(i))!=""?getCellValue(row.getCell(i)):"0"));
                      i++;
                      //unit price ASP
                      extraWork.setUnitPriceAsp(Double.parseDouble(getCellValue(row.getCell(i))!=""?getCellValue(row.getCell(i)):"0"));
                      i++;
                      //unit price vf
                      extraWork.setUnitPriceVendor(Double.parseDouble(getCellValue(row.getCell(i))!=""?getCellValue(row.getCell(i)):"0"));
                      i++;
                      
                      extras.add(extraWork);
               }
               
               errors = validateExtraWork(extras);
               if(!(errors.length()>0)){
                   // save
                   updateAndAdd(extras);
               }else{
                   JsfUtil.addErrorMessage("Sheet couldn't be uploaded, please check faulty rows.");
               }
            }
        } catch (IOException ex) {
            Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    public String validateExtraWork(List<ExtraWork> extras){
        String error="";
        int row = 0;
        for (ExtraWork extra : extras) {
            row++;
            if(extra.getActivityDate()==null){
                error += "Row: "+row+" Activity Data can't be empty!\n";
            }
            if(extra.getActivityDescription()==null){
                error += "Row: "+row+" Activity Description can't be empty!, Row: "+row+"\n";
            }
            if(extra.getActivityDetails()==null){
                error += "Row: "+row+" Activity Details can't be empty!, Row: "+row+"\n";
            }
            if(aspController.getSubcontractors(extra.getAsp())==null){
                error += "Row: "+row+" ASP can't be empty or not recognized, Row: "+row+"\n";
            }
            if(domainController.getDomains(extra.getDomainName().trim())==null){
                error += "Row: "+row+" Domain can't be empty or not recognized, Row: "+row+"\n";
            }
            if(extra.getQty()==null || extra.getQty()==0){
                error += "Row: "+row+" Qty can't be empty or zero!\n";
            }
            if(extra.getUnitPriceAsp()==null){
                error += "Row: "+row+" unit price ASP can't be empty!\n";
            }
            if(extra.getUnitPriceVendor()==null || extra.getUnitPriceVendor()==0){
                error += "Row: "+row+" unit price VF can't be empty or zero!\n";
            }
            if(extra.getSite()==null){
                error += "Row: "+row+" Site can't be empty!\n";
            }
            if(subdomainController.getSubDomain(extra.getSubDomain().trim())==null){
            error += "Row: "+row+" Sub-Domain is empty/not recognized\n";
            }

        }
        return error;
    }
    
    public void updateAndAdd(List<ExtraWork> extras){
        for (ExtraWork extra : extras) {
            //total price asp
            extra.setTotalPriceAsp(extra.getQty()*extra.getUnitPriceAsp());
            //total price vf 
            extra.setTotalPriceVendor(extra.getQty()*extra.getUnitPriceVendor());
            //um
            extra.setUm(extra.getTotalPriceVendor()-extra.getTotalPriceAsp());
            //um%
            extra.setUmPercent((extra.getUm()/extra.getTotalPriceVendor())*100);
            //creator
            extra.setCreator(usersController.getLoggedInUser());
            //creation time 
            extra.setCreationTime(new Date());
            //assignemnt 
            extra.setAssignmentGroup(extra.getCreator().getUserName());
            
            if(usersController.getLoggedInUserRole().contains("BP")){
              //bp approval 
            extra.setBusinessProviderApproval(Boolean.FALSE);
            //region approval 
            extra.setRegionApproval(Boolean.FALSE);
            //domian approval 
            extra.setDomainOwnerApproval(Boolean.FALSE);  
            }
            if(usersController.getLoggedInUserRole().contains("RO")){
              //bp approval 
            extra.setBusinessProviderApproval(Boolean.TRUE);
            //region approval 
            extra.setRegionApproval(Boolean.FALSE);
            //domian approval 
            extra.setDomainOwnerApproval(Boolean.FALSE);  
            }
            if(usersController.getLoggedInUserRole().contains("DO")){
             //bp approval 
            extra.setBusinessProviderApproval(Boolean.TRUE);
            //region approval 
            extra.setRegionApproval(Boolean.TRUE);
            //domian approval 
            extra.setDomainOwnerApproval(Boolean.FALSE);   
            }
            //correlate 
            extra.setCorrelated(Boolean.FALSE);
            //last assignment 
            extra.setLastAssignment(extra.getAssignmentGroup());
        }
        
        extraWorkController.uploadExtrawork(extras);
    }
    
    public String getCellValue(Cell cell){
        if(cell==null){
            return "";
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }
    
    public Date getDateCellValue(Cell cell){
        
        return cell.getDateCellValue();
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    
}
