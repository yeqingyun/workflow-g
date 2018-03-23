package com.gionee.gniflow.biz.service.impl.sfm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;




import org.springframework.stereotype.Service;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.gionee.gniflow.biz.sfm.SfmService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.util.PropertyHolder;
@Service
public class SfmServiceImpl implements SfmService {

	private static Map<String,Supplier>  suppliersInfoMap = new HashMap<String, Supplier>();
	private static List<Supplier>  suppliersInfo = new ArrayList<Supplier>();
	private static final String suppliersXls = PropertyHolder.getContextProperty("sfm.suppliers.excel");
	@PostConstruct
	public void init(){
		
		Workbook workbook = null;
		InputStream ips = null;
		try {
			ips = SfmServiceImpl.class.getClassLoader().getResourceAsStream(SfmServiceImpl.suppliersXls);
			workbook = Workbook.getWorkbook(ips);
			Sheet[] sheets = workbook.getSheets();
			for(Sheet sheet : sheets){
				int rsColumns = sheet.getColumns();   
				int rsRows = sheet.getRows();
				for (int i = 1; i < rsRows; i++)   
	            {   
					Supplier supplier  = new Supplier();
					Cell supplieNameCell = sheet.getCell(0, i); 
					Cell usernameCell = sheet.getCell(1, i);
					Cell emailCell = sheet.getCell(2, i);
					Cell categoryCell = sheet.getCell(3, i);
					if(!emailCell.getContents().trim().equals("")){
						supplier.setCategory(categoryCell.getContents());
						supplier.setEmail(emailCell.getContents());
						supplier.setSupplierName(supplieNameCell.getContents());
						supplier.setUsername(usernameCell.getContents());
						suppliersInfo.add(supplier);
						suppliersInfoMap.put(supplier.getEmail(), supplier);
					}
					
	            }  
			}
			
		} catch (Exception e) {
			//System.out.println(SfmServiceImpl.suppliersXls);
			e.printStackTrace();
			throw new RuntimeException("供应商信息加载失败",e);
		}finally{
			if(workbook!=null){
				workbook.close();
			}
			if(ips!=null){
				try {
					ips.close();
				} catch (IOException e) {
					throw new RuntimeException("供应商信息加载失败",e);
				}
			}
			
		}   
	}
	
	
	private void insertExcel(Supplier supplier) throws Exception{
		Workbook workbook = null;
		WritableWorkbook wwb = null;
		OutputStream ops = null;
		InputStream ips = null;
		try {
			URL url = SfmServiceImpl.class.getClassLoader().getResource(SfmServiceImpl.suppliersXls);
			File file = new File(URLDecoder.decode(url.getFile(),"utf8"));
			workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheets()[0];
			int rsRows = sheet.getRows();
			File tempfile = new File("temp.xls");
			wwb = Workbook.createWorkbook(tempfile, workbook);
			WritableSheet writesheet = wwb.getSheet(0);
			Label label = new Label(0,rsRows,supplier.getSupplierName());
			writesheet.addCell(label);
            label = new Label(1,rsRows,supplier.getUsername());
            writesheet.addCell(label);
            label = new Label(2,rsRows,supplier.getUsername());
            writesheet.addCell(label);
            label = new Label(3,rsRows,supplier.getCategory());
            writesheet.addCell(label);
            wwb.write();
            wwb.close();
            wwb = null;
            workbook.close();
            workbook = null;
            file.delete();
            tempfile.renameTo(file);
		} catch (Exception e) {
			throw new Exception("供应商信息加载失败",e);
		}finally{
			if(workbook!=null){
				workbook.close();
			}
			if(wwb!=null){
				try {
					wwb.close();
				} catch (WriteException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(ips!=null){
				try {
					ips.close();
				} catch (IOException e) {
					throw new RuntimeException("供应商信息加载失败",e);
				}
			}
			if(ops!=null){
				try {
					ops.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}   
	} 
	
	
	@Override
	public List<Supplier> getSuppliersInfo(Integer page, Integer rows) {
		int start = (page-1)*rows;
		int end = start+rows;
		List<Supplier> suppliers = new ArrayList<Supplier>();
		for(int i=start;i<end;i++){
			suppliers.add(suppliersInfo.get(i));
		}
		return suppliers;
	}

	@Override
	public int getCount() {
		return suppliersInfo.size();
	}

	@Override
	public Supplier getSupplierByEmail(String email) {
		return suppliersInfoMap.get(email);
	}

	@Override
	public AjaxJson addSupplier(Supplier supplier) {
		AjaxJson ajaxJson = new AjaxJson(true,"插入成功");
		try {
			if(supplier.getEmail()!=null){
				insertExcel(supplier);
				suppliersInfo.add(supplier);
				suppliersInfoMap.put(supplier.getEmail(), supplier);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg(e.getMessage());
		}
		return ajaxJson;
	}

}
