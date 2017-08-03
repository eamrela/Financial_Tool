/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.financialtool.custom;

import com.vodafone.financialtool.controllers.ExtraWorkController;
import com.vodafone.financialtool.controllers.util.JsfUtil;
import com.vodafone.financialtool.entities.ExtraWork;
import com.vodafone.financialtool.entities.ExtraworkAttachments;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.namespace.QName;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

/**
 *
 * @author eamrela
 */
@Named("exportManager")
@SessionScoped
public class Exporter implements Serializable{
    private Date fromDate;
    private Date toDate;
    
    //<editor-fold defaultstate="collapsed" desc="Headers">
    

     private final String[] activityHeadersFull_NoAttachment = {"Site","ASP","Region","VF Owner","Approval Status ","Domain",
                                "SubDomain","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price to VF","Unit Price to ASP",
                                "VF Total Price","ASP Total Price","UM","UM%","Business Approval","Region Approval","Domain Approval","Creator","Number of Attachments"};
     private final String[] activityHeadersFull_Attachment = {"Site","ASP","Region","VF Owner","Approval Status ","Domain",
                                "SubDomain","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price to VF","Unit Price to ASP",
                                "VF Total Price","ASP Total Price","UM","UM%","Business Approval","Region Approval","Domain Approval","Creator","Attachment(s)"};
     private final String[] activityHeadersVF = {"Site","Region","VF Owner","Approval Status ","Domain",
                                "SubDomain","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price",
                                "Total Price","Number of Attachments"};
     private final String[] activityHeadersVF_Attachments = {"Site","Region","VF Owner","Approval Status ","Domain",
                                "SubDomain","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price",
                                "Total Price","Attachment(s)"};
     private final String[] activityHeadersASP = {"Site","ASP","Region","Approval Status ","Domain",
                                "SubDomain","Date","Activity Code","Activity Description ","Activity details","Qty","Unit Price"
                                ,"ASP Total Price","Business Approval","Region Approval","Domain Approval","Creator","Number of Attachments"};
     //</editor-fold>
    static final String drawNS = "http://schemas.microsoft.com/office/drawing/2010/main";
    static final String relationshipsNS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
    
     @Inject
     private ExtraWorkController extraWorkController;
     
    public void exportActivity_BP_DO_FULL_NoATTACHMENT(){
        List<ExtraWork> activities = extraWorkController.getUserItemsByDate(fromDate,toDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Master Track");
        Row row = sheet.createRow(0);
        for (int i = 0; i < activityHeadersFull_NoAttachment.length; i++) {
            row.createCell(i).setCellValue(activityHeadersFull_NoAttachment[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i+1);
            int j=0;
            //Site
            row.createCell(j).setCellValue(activities.get(i).getSite()!=null?activities.get(i).getSite():"");
            j++;
            //ASP
            row.createCell(j).setCellValue(activities.get(i).getAsp()!=null?activities.get(i).getAsp():"");
            j++;
            //Region
            row.createCell(j).setCellValue(activities.get(i).getRegion()!=null?activities.get(i).getRegion():"");
            j++;
            //VF Owner
            row.createCell(j).setCellValue(activities.get(i).getCustomerOwner()!=null?activities.get(i).getCustomerOwner():"");
            j++;
            //Approval
            row.createCell(j).setCellValue(activities.get(i).getApprovalStatus()!=null?activities.get(i).getApprovalStatus():"");
            j++;
            //Domain
            row.createCell(j).setCellValue(activities.get(i).getDomainName()!=null?activities.get(i).getDomainName():"");
            j++;
            //Subdomain
            row.createCell(j).setCellValue(activities.get(i).getSubDomain()!=null?activities.get(i).getSubDomain():"");
            j++;
            //Date
            row.createCell(j).setCellValue(activities.get(i).getActivityDate()!=null?activities.get(i).getActivityDate():null);
            j++;
            //Code
            row.createCell(j).setCellValue(activities.get(i).getActivityCode()!=null?activities.get(i).getActivityCode():"");
            j++;
            //Description
            row.createCell(j).setCellValue(activities.get(i).getActivityDescription()!=null?activities.get(i).getActivityDescription():"");
            j++;
            //Details 
            row.createCell(j).setCellValue(activities.get(i).getActivityDetails()!=null?activities.get(i).getActivityDetails():"");
            j++;
            //QTY
            row.createCell(j).setCellValue(activities.get(i).getQty()!=null?activities.get(i).getQty():0);
            j++;
            //Unit Price VF
            row.createCell(j).setCellValue(activities.get(i).getUnitPriceVendor()!=null?activities.get(i).getUnitPriceVendor():0);
            j++;
            //Unit Price ASP
            row.createCell(j).setCellValue(activities.get(i).getUnitPriceAsp()!=null?activities.get(i).getUnitPriceAsp():0);
            j++;
            // Total Price VF
            row.createCell(j).setCellValue(activities.get(i).getTotalPriceVendor()!=null?activities.get(i).getTotalPriceVendor():0);
            j++;
            // Total Price ASP
            row.createCell(j).setCellValue(activities.get(i).getTotalPriceAsp()!=null?activities.get(i).getTotalPriceAsp():0);
            j++;
            // UM
            row.createCell(j).setCellValue(activities.get(i).getUm()!=null?activities.get(i).getUm():0);
            j++;
            // UM%
            row.createCell(j).setCellValue(activities.get(i).getUmPercent()!=null?activities.get(i).getUmPercent():0);
            j++;
            //BP Approval 
            row.createCell(j).setCellValue(activities.get(i).getBusinessProviderApproval()!=null?activities.get(i).getBusinessProviderApproval():false);
            j++;
            //Domain Approval 
            row.createCell(j).setCellValue(activities.get(i).getDomainOwnerApproval()!=null?activities.get(i).getDomainOwnerApproval():false);
            j++;
            //Domain Approval 
            row.createCell(j).setCellValue(activities.get(i).getDomainOwnerApproval()!=null?activities.get(i).getDomainOwnerApproval():false);
            j++;
            //Creator 
            row.createCell(j).setCellValue(activities.get(i).getCreator()!=null?activities.get(i).getCreator().getUserName():"");
            j++;
            //Attachments Number
             row.createCell(j).setCellValue(activities.get(i).getExtraworkAttachmentsCollection()!=null?activities.get(i).getExtraworkAttachmentsCollection().size():0);
            j++;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"Extra Work.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Extra Work Report is now exported");
        
    }
      
    public void exportActivity_BP_DO_FULL_ATTACHMENT(){
        try {
            List<ExtraWork> activities = extraWorkController.getUserItemsByDate(fromDate,toDate);
            
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Master Track");
            int imgPckId = addImageToWorkbook(workbook, "/home/poms/uploaded_data/pkg.png", Workbook.PICTURE_TYPE_PNG);
            String imgPckRelId = addImageToSheet(sheet, imgPckId, Workbook.PICTURE_TYPE_PNG);
            
            Row row = sheet.createRow(0);
            for (int i = 0; i < activityHeadersFull_Attachment.length; i++) {
                row.createCell(i).setCellValue(activityHeadersFull_Attachment[i]);
            }
            for (int i = 0; i < activities.size(); i++) {
                row = sheet.createRow(i+1);
                int j=0;
                //Site
                row.createCell(j).setCellValue(activities.get(i).getSite()!=null?activities.get(i).getSite():"");
                j++;
                //ASP
                row.createCell(j).setCellValue(activities.get(i).getAsp()!=null?activities.get(i).getAsp():"");
                j++;
                //Region
                row.createCell(j).setCellValue(activities.get(i).getRegion()!=null?activities.get(i).getRegion():"");
                j++;
                //VF Owner
                row.createCell(j).setCellValue(activities.get(i).getCustomerOwner()!=null?activities.get(i).getCustomerOwner():"");
                j++;
                //Approval
                row.createCell(j).setCellValue(activities.get(i).getApprovalStatus()!=null?activities.get(i).getApprovalStatus():"");
                j++;
                //Domain
                row.createCell(j).setCellValue(activities.get(i).getDomainName()!=null?activities.get(i).getDomainName():"");
                j++;
                //Subdomain
                row.createCell(j).setCellValue(activities.get(i).getSubDomain()!=null?activities.get(i).getSubDomain():"");
                j++;
                //Date
                row.createCell(j).setCellValue(activities.get(i).getActivityDate()!=null?activities.get(i).getActivityDate():null);
                j++;
                //Code
                row.createCell(j).setCellValue(activities.get(i).getActivityCode()!=null?activities.get(i).getActivityCode():"");
                j++;
                //Description
                row.createCell(j).setCellValue(activities.get(i).getActivityDescription()!=null?activities.get(i).getActivityDescription():"");
                j++;
                //Details
                row.createCell(j).setCellValue(activities.get(i).getActivityDetails()!=null?activities.get(i).getActivityDetails():"");
                j++;
                //QTY
                row.createCell(j).setCellValue(activities.get(i).getQty()!=null?activities.get(i).getQty():0);
                j++;
                //Unit Price VF
                row.createCell(j).setCellValue(activities.get(i).getUnitPriceVendor()!=null?activities.get(i).getUnitPriceVendor():0);
                j++;
                //Unit Price ASP
                row.createCell(j).setCellValue(activities.get(i).getUnitPriceAsp()!=null?activities.get(i).getUnitPriceAsp():0);
                j++;
                // Total Price VF
                row.createCell(j).setCellValue(activities.get(i).getTotalPriceVendor()!=null?activities.get(i).getTotalPriceVendor():0);
                j++;
                // Total Price ASP
                row.createCell(j).setCellValue(activities.get(i).getTotalPriceAsp()!=null?activities.get(i).getTotalPriceAsp():0);
                j++;
                // UM
                row.createCell(j).setCellValue(activities.get(i).getUm()!=null?activities.get(i).getUm():0);
                j++;
                // UM%
                row.createCell(j).setCellValue(activities.get(i).getUmPercent()!=null?activities.get(i).getUmPercent():0);
                j++;
                //BP Approval 
            row.createCell(j).setCellValue(activities.get(i).getBusinessProviderApproval()!=null?activities.get(i).getBusinessProviderApproval():false);
            j++;
            //Domain Approval 
            row.createCell(j).setCellValue(activities.get(i).getDomainOwnerApproval()!=null?activities.get(i).getDomainOwnerApproval():false);
            j++;
            //Domain Approval 
            row.createCell(j).setCellValue(activities.get(i).getDomainOwnerApproval()!=null?activities.get(i).getDomainOwnerApproval():false);
            j++;
                //Creator
                row.createCell(j).setCellValue(activities.get(i).getCreator()!=null?activities.get(i).getCreator().getUserName():"");
                j++;
                //Attachments
                if(activities.get(i).getExtraworkAttachmentsCollection()!=null){
                    if(activities.get(i).getExtraworkAttachmentsCollection().size()>0){
                        Object[] attachments = activities.get(i).getExtraworkAttachmentsCollection().toArray();
                        for (int k = 0; k < attachments.length; k++) {
                            try {
                                XSSFClientAnchor imgAnchor1 = new XSSFClientAnchor(0,0,0,0,(k+j),
                                        row.getRowNum(), (k +j+1), row.getRowNum() + 1);
                                String oleRelId1 = addFile(sheet,((ExtraworkAttachments)attachments[j]).getAttachmentLocation(),(i+j+activities.get(i).getId().intValue()+Math.random()) );
                                int shapeId1 = addImageToShape(sheet, imgAnchor1, imgPckId);
                                addObjectToShape(sheet, imgAnchor1, shapeId1, oleRelId1, imgPckRelId, "Objekt-Manager-Shellobjekt");
                            } catch (IOException ex) {
                                Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvalidFormatException ex) {
                                Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"Extra Work.xlsx\"");
            
            try {
                workbook.write(externalContext.getResponseOutputStream());
                externalContext.getResponseOutputStream().close();
            } catch (IOException ex) {
                Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
            }
            facesContext.responseComplete();
            JsfUtil.addSuccessMessage("Extra Work Report is now exported");
            
        } catch (IOException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void exportActivity_VF_NoATTACHMENT(){
        List<ExtraWork> activities = extraWorkController.getUserItemsByDate(fromDate,toDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Master Track");
        Row row = sheet.createRow(0);
        for (int i = 0; i < activityHeadersVF.length; i++) {
            row.createCell(i).setCellValue(activityHeadersVF[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i+1);
            int j=0;
            //Site
            row.createCell(j).setCellValue(activities.get(i).getSite()!=null?activities.get(i).getSite():"");
            j++;
            
            //Region
            row.createCell(j).setCellValue(activities.get(i).getRegion()!=null?activities.get(i).getRegion():"");
            j++;
            //VF Owner
            row.createCell(j).setCellValue(activities.get(i).getCustomerOwner()!=null?activities.get(i).getCustomerOwner():"");
            j++;
            //Approval
            row.createCell(j).setCellValue(activities.get(i).getApprovalStatus()!=null?activities.get(i).getApprovalStatus():"");
            j++;
            //Domain
            row.createCell(j).setCellValue(activities.get(i).getDomainName()!=null?activities.get(i).getDomainName():"");
            j++;
            //Subdomain
            row.createCell(j).setCellValue(activities.get(i).getSubDomain()!=null?activities.get(i).getSubDomain():"");
            j++;
            //Date
            row.createCell(j).setCellValue(activities.get(i).getActivityDate()!=null?activities.get(i).getActivityDate():null);
            j++;
            //Code
            row.createCell(j).setCellValue(activities.get(i).getActivityCode()!=null?activities.get(i).getActivityCode():"");
            j++;
            //Description
            row.createCell(j).setCellValue(activities.get(i).getActivityDescription()!=null?activities.get(i).getActivityDescription():"");
            j++;
            //Details 
            row.createCell(j).setCellValue(activities.get(i).getActivityDetails()!=null?activities.get(i).getActivityDetails():"");
            j++;
            //QTY
            row.createCell(j).setCellValue(activities.get(i).getQty()!=null?activities.get(i).getQty():0);
            j++;
            //Unit Price VF
            row.createCell(j).setCellValue(activities.get(i).getUnitPriceVendor()!=null?activities.get(i).getUnitPriceVendor():0);
            j++;
            // Total Price VF
            row.createCell(j).setCellValue(activities.get(i).getTotalPriceVendor()!=null?activities.get(i).getTotalPriceVendor():0);
            j++;
             row.createCell(j).setCellValue(activities.get(i).getExtraworkAttachmentsCollection()!=null?activities.get(i).getExtraworkAttachmentsCollection().size():0);
            j++;
            
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"Extra Work.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Extra Work Report is now exported");
        
    }
    
    public void exportActivity_ASP_NoATTACHMENT(){
        List<ExtraWork> activities = extraWorkController.getUserItemsByDate(fromDate,toDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Master Track");
        Row row = sheet.createRow(0);
        for (int i = 0; i < activityHeadersASP.length; i++) {
            row.createCell(i).setCellValue(activityHeadersASP[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i+1);
            int j=0;
            //Site
            row.createCell(j).setCellValue(activities.get(i).getSite()!=null?activities.get(i).getSite():"");
            j++;
            //ASP
            row.createCell(j).setCellValue(activities.get(i).getAsp()!=null?activities.get(i).getAsp():"");
            j++;
            //Region
            row.createCell(j).setCellValue(activities.get(i).getRegion()!=null?activities.get(i).getRegion():"");
            j++;
           //Approval
            row.createCell(j).setCellValue(activities.get(i).getApprovalStatus()!=null?activities.get(i).getApprovalStatus():"");
            j++;
            //Domain
            row.createCell(j).setCellValue(activities.get(i).getDomainName()!=null?activities.get(i).getDomainName():"");
            j++;
            //Subdomain
            row.createCell(j).setCellValue(activities.get(i).getSubDomain()!=null?activities.get(i).getSubDomain():"");
            j++;
            //Date
            row.createCell(j).setCellValue(activities.get(i).getActivityDate()!=null?activities.get(i).getActivityDate():null);
            j++;
            //Code
            row.createCell(j).setCellValue(activities.get(i).getActivityCode()!=null?activities.get(i).getActivityCode():"");
            j++;
            //Description
            row.createCell(j).setCellValue(activities.get(i).getActivityDescription()!=null?activities.get(i).getActivityDescription():"");
            j++;
            //Details 
            row.createCell(j).setCellValue(activities.get(i).getActivityDetails()!=null?activities.get(i).getActivityDetails():"");
            j++;
            //QTY
            row.createCell(j).setCellValue(activities.get(i).getQty()!=null?activities.get(i).getQty():0);
            j++;
            //Unit Price ASP
            row.createCell(j).setCellValue(activities.get(i).getUnitPriceAsp()!=null?activities.get(i).getUnitPriceAsp():0);
            j++;
            // Total Price ASP
            row.createCell(j).setCellValue(activities.get(i).getTotalPriceAsp()!=null?activities.get(i).getTotalPriceAsp():0);
            j++;
            //BP Approval 
            row.createCell(j).setCellValue(activities.get(i).getBusinessProviderApproval()!=null?activities.get(i).getBusinessProviderApproval():false);
            j++;
            //Domain Approval 
            row.createCell(j).setCellValue(activities.get(i).getDomainOwnerApproval()!=null?activities.get(i).getDomainOwnerApproval():false);
            j++;
            //Domain Approval 
            row.createCell(j).setCellValue(activities.get(i).getDomainOwnerApproval()!=null?activities.get(i).getDomainOwnerApproval():false);
            j++;
            //Creator 
            row.createCell(j).setCellValue(activities.get(i).getCreator()!=null?activities.get(i).getCreator().getUserName():"");
            j++;
            //Attachments Number
             row.createCell(j).setCellValue(activities.get(i).getExtraworkAttachmentsCollection()!=null?activities.get(i).getExtraworkAttachmentsCollection().size():0);
            j++;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"Extra Work.xlsx\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        facesContext.responseComplete();
        JsfUtil.addSuccessMessage("Extra Work Report is now exported");
        
    }
     
      
      //<editor-fold defaultstate="collapsed" desc="Helper Methods">
      private static String addFile(XSSFSheet sh, String filePath, double oleId) throws IOException, InvalidFormatException {
        File file = new File(filePath);
        FileInputStream fin = new FileInputStream(file);
        byte[] data;
        data = new byte[fin.available()];
        fin.read(data);
        Ole10Native ole10 = new Ole10Native(file.getAbsolutePath() ,file.getAbsolutePath(), file.getAbsolutePath(),data);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(500);
        ole10.writeOut(bos);

        POIFSFileSystem poifs = new POIFSFileSystem();
        poifs.getRoot().createDocument(Ole10Native.OLE10_NATIVE, new ByteArrayInputStream(bos.toByteArray()));

        poifs.getRoot().setStorageClsid(ClassID.OLE10_PACKAGE);


        final PackagePartName pnOLE = PackagingURIHelper.createPartName( "/xl/embeddings/oleObject"+oleId+Math.random()+".bin" );
        final PackagePart partOLE = sh.getWorkbook().getPackage().createPart( pnOLE, "application/vnd.openxmlformats-officedocument.oleObject" );
        PackageRelationship prOLE = sh.getPackagePart().addRelationship( pnOLE, TargetMode.INTERNAL, POIXMLDocument.OLE_OBJECT_REL_TYPE );
        OutputStream os = partOLE.getOutputStream();
        poifs.writeFilesystem(os);
        os.close();
        poifs.close();

        return prOLE.getId();

    }


    private static int addImageToWorkbook(XSSFWorkbook wb, String fileName, int pictureType) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        int imgId = wb.addPicture(fis, pictureType);
        fis.close();
        return imgId;
    }

    private static String addImageToSheet(XSSFSheet sh, int imgId, int pictureType) throws InvalidFormatException {
        final PackagePartName pnIMG  = PackagingURIHelper.createPartName( "/xl/media/image"+(imgId+1)+(pictureType == Workbook.PICTURE_TYPE_PNG ? ".png" : ".jpeg") );
        PackageRelationship prIMG = sh.getPackagePart().addRelationship( pnIMG, TargetMode.INTERNAL, PackageRelationshipTypes.IMAGE_PART );
        return prIMG.getId();
    }


    private static int addImageToShape(XSSFSheet sh, XSSFClientAnchor imgAnchor, int imgId) {
        XSSFDrawing pat = sh.createDrawingPatriarch();
        XSSFPicture pic = pat.createPicture(imgAnchor, imgId);

        CTPicture cPic = pic.getCTPicture();
        int shapeId = (int)cPic.getNvPicPr().getCNvPr().getId();
        cPic.getNvPicPr().getCNvPr().setHidden(true);
        CTOfficeArtExtensionList extLst = cPic.getNvPicPr().getCNvPicPr().addNewExtLst();
        // https://msdn.microsoft.com/en-us/library/dd911027(v=office.12).aspx
        CTOfficeArtExtension ext = extLst.addNewExt();
        ext.setUri("{63B3BB69-23CF-44E3-9099-C40C66FF867C}");
        XmlCursor cur = ext.newCursor();
        cur.toEndToken();
        cur.beginElement(new QName(drawNS, "compatExt", "a14"));
        cur.insertAttributeWithValue("spid", "_x0000_s"+shapeId);


        return shapeId;
    }



    private static void addObjectToShape(XSSFSheet sh, XSSFClientAnchor imgAnchor, int shapeId, String objRelId, String imgRelId, String progId) {
        CTWorksheet cwb = sh.getCTWorksheet();
        CTOleObjects oo = cwb.isSetOleObjects() ? cwb.getOleObjects() : cwb.addNewOleObjects();

        CTOleObject ole1 = oo.addNewOleObject();
        ole1.setProgId(progId);
        ole1.setShapeId(shapeId);
        ole1.setId(objRelId);


        XmlCursor cur1 = ole1.newCursor();
        cur1.toEndToken();
        cur1.beginElement("objectPr", XSSFRelation.NS_SPREADSHEETML);
        cur1.insertAttributeWithValue("id", relationshipsNS, imgRelId);
        cur1.insertAttributeWithValue("defaultSize", "0");
        cur1.beginElement("anchor", XSSFRelation.NS_SPREADSHEETML);
        cur1.insertAttributeWithValue("moveWithCells", "1");

        CTTwoCellAnchor anchor = CTTwoCellAnchor.Factory.newInstance();
        anchor.setFrom(imgAnchor.getFrom());
        anchor.setTo(imgAnchor.getTo());

        XmlCursor cur2 = anchor.newCursor();
        cur2.copyXmlContents(cur1);
        cur2.dispose();

        cur1.toParent();
        cur1.toFirstChild();
        cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "from"));
        cur1.toNextSibling();
        cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "to"));

        cur1.dispose();
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }
//</editor-fold>
}
