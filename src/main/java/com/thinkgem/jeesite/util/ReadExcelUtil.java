package com.thinkgem.jeesite.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ReadExcelUtil {
	
	private String name;
	private String office_id;
	private String mobile;
	
	
	
	 public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getOffice_id() {
		return office_id;
	}


	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}


	public List<ReadExcelUtil> getData(String path){
		 Workbook readwb = null;
		 
		 
		 List<ReadExcelUtil> list = new ArrayList<ReadExcelUtil>();
		 try{
			 InputStream instream = new FileInputStream(path);
			 readwb = Workbook.getWorkbook(instream);
			 Sheet readsheet = readwb.getSheet(0); 
			 int rsColumns = readsheet.getColumns(); 
			 int rsRows = readsheet.getRows(); 
			 for (int i = 1; i < rsRows; i++)   
				  
	            {   
				 ReadExcelUtil read = new ReadExcelUtil();
	                    Cell cell = readsheet.getCell(1, i);
	                    Cell off = readsheet.getCell(2,i);
	                    Cell mobile = readsheet.getCell(11,i);
	                    read.setName(cell.getContents());
	                    read.setOffice_id(off.getContents());
	                    read.setMobile(mobile.getContents());
	                    System.out.print(cell.getContents() + " "); 
	                    System.out.print(off.getContents() + " "); 
	                    list.add(read);
	                System.out.println();   
	            }   
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}finally {
			readwb.close();   
		}
		return list;
		 
	 }
}
