package org.iii.ideas.catering_service.rest.excel.create;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredientDAO;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdataDAO;
import org.iii.ideas.catering_service.rest.api.QueryManageList;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CreateExcelFile2 {
	
	private Map<String, Object[]> data;
	private HashMap<String ,String> header =new HashMap<String,String>();
	private Integer kitchenid=0;
	public CreateExcelFile2(){}

	public String createExcel() throws ParseException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Data");
		
		int rowNum=0;
		String fPath="";
		
		//設定標題
		Row rowHeader = sheet.createRow(rowNum);
		int cellNum=0;
		for(Entry<String,String> item: header.entrySet()){
			Cell cell =rowHeader.createCell(cellNum++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(item.getValue());
		}

		int rownum = data.size();
		int loopi=1;
		//for (int loopi = 1; loopi <= rownum; loopi++) {
		for (Entry<String,Object[]> item:data.entrySet()){
			Row row = sheet.createRow(loopi - 1);
			Object[] objArr = item.getValue();
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (obj instanceof String) {
					cell.setCellValue((String) obj);
				} else if (obj instanceof Integer) {
					cell.setCellValue(String.valueOf(obj));
				}else if (obj instanceof Long) {  //20140812 KC 增加判斷long 因為uuid
					cell.setCellValue(String.valueOf(obj));
				} 	
				else {
					cell.setCellValue((String) obj);
				}
			}
			loopi++;
		}

		try{
			fPath = CateringServiceUtil.getDownloadPath("Excel",kitchenid);
			FileOutputStream out = new FileOutputStream(new File(fPath));
			workbook.write(out);
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return fPath;
	}
	
	public void setData(Map<String, Object[]> data2){
		this.data=data2;
	}
	public  void setDataHeader(HashMap<String, String> pHeader){
		/*String[] headerTextAry=new String[header.size()];
		for (Entry<String,String> item:header.entrySet()){
			headerTextAry
		}*/
		this.header=pHeader;
	}
	public void setKitchenid(Integer kid){
		this.kitchenid=kid;
	}
}
