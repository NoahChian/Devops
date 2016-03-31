package org.iii.ideas.catering_service.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
/*
 * Excel 下載主程式
 */
@Deprecated
public class CreateExcelFile {
	private Logger log = Logger.getLogger(CreateExcelFile.class);
	private String filePrefixName="Default";
	private String requestFileType="";
	public  String createFile(String fileNameX,int kid) throws UnsupportedEncodingException, ParseException {
		String fPath = "";
		String fileguide="";
		
		log.debug("Download Args:"+fileNameX);
		if(fileNameX.indexOf("menu")==-1 && fileNameX.indexOf("supplier")==-1 && fileNameX.indexOf("schoolingredient")==-1 && fileNameX.indexOf("vegetable")==-1){
			String fileName = fileNameX.replace("-", "/");
			byte[] decoded = Base64.decodeBase64(fileName);
			fileguide = new String(decoded, "UTF-8");
		//	log.debug("加入參數: " + fileguide);
		}else{
			fileguide=fileNameX;
		}
		
		String[] names = fileguide.split("&");
		for (String name : names) {
			log.debug("Download Arg:"+name);
		}
		String filename = "";// 檔案名稱
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Data");
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		// This data needs to be written (Object[])
		// ///////////////////////RIC食材供應商供貨資訊(supplyInfo)
		String searchFileType = new String(names[0]);
		//String filePrefixName ="";
		this.requestFileType = searchFileType;
		log.debug("Download file type:"+searchFileType);
		if (searchFileType.equals("supplyInfo")) {
			filename = new String(names[0]) + new String(names[1]);// 檔案名稱為下載類型與供應商名稱
			data.put("1", new Object[] { "編號", "資料建立日期", "盒餐業者名稱/自立午餐學校名稱", "聯絡電話", "地址", "食材", "批號", "有效日期",
					"進貨日期", "生產日期" });
			data.put("2", new Object[] { "1", "2013/12/11", "永和國小", "6607-2141", "新北市中和路", "高麗菜", "", "", "", "" });
			data.put("3", new Object[] { "2", "2013/12/11", "第一家食材公司", "05076416", "6607-2141", "高麗菜", "", "", "",
					"" });
			this.setFilePrefixName("供應商Test1");
		} else if (searchFileType.equals("abnormalsearch")) {
			filename = new String(names[0]) + new String(names[1]);// 檔案名稱為下載類型與供應商名稱
			data.put("1", new Object[] { "編號", "供應商名稱", "食材", "批號", "有效日期", "進貨日期", "生產日期", "團膳業者名稱", "供餐日期",
					"供餐學校", "菜色名稱", "異常說明" });
			data.put("2", new Object[] { "1", "第一家食材公司", "雞腿", "", "", "2013/08/30", "", "", "2013/09/01", "永和國小",
					"烤雞腿", "異常" });
			this.setFilePrefixName("供應商Test2");
		} else if (searchFileType.equals("supplier")) { //供應商excel 下載
			SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
			Session session = sessionFactory.openSession();
			data.put("1", new Object[] {"供應商名稱", "負責人", "公司統編","地址","電話","認證標章"});
			Criteria criteria = session.createCriteria(Supplier.class);
			criteria.add( Restrictions.eq("id.kitchenId", kid) );
			//criteria.addOrder( Order.asc("menuDate") );
			
			int row=2;
			List<Supplier> supplier_list = criteria.list();
			Iterator<Supplier> iterator = supplier_list.iterator();
			while (iterator.hasNext()) {
				Supplier supplier = iterator.next();
				log.debug("func:"+searchFileType+" Row:"+String.valueOf(row));
				data.put(String.valueOf(row), new Object[] {supplier.getSupplierName(), supplier.getOwnner(), supplier.getCompanyId(), supplier.getSupplierAdress(), 
						supplier.getSupplierTel(), supplier.getSupplierCertification()});
				row++;
			}
			session.close();
			this.setFilePrefixName("供應商");
		}else if (searchFileType.equals("schoolingredient")) {//學校食材Excel匯出
			SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
			Session session = sessionFactory.openSession();
			String begDate = names[1].trim().replace("-", "/");
			String endDate = names[2].trim().replace("-", "/");
			
			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
			
			data.put("1", new Object[] {"供餐日期", "學校", "菜色名稱","食材名稱","進貨日期","生產日期", "有效日期", "批號","品牌(製造商)","供應商統編","食材認證標章","認證號碼"});
			String HQL = "select b.menuDate, s.schoolName, d.dishName, i.ingredientName, i.stockDate, i.manufactureDate, "
					+ "i.expirationDate, i.lotNumber, i.brand, i.supplierId, i.sourceCertification, i.certificationId ,i.supplierCompanyId "
					+ "from School s,Batchdata b,Ingredientbatchdata i , Dish d "
					+ "where s.schoolId = b.schoolId "
					+ " and b.batchDataId=i.batchDataId "
					+ " and i.dishId= d.dishId "
					+ " and b.kitchenId = :kitchenId "
					+ " and d.kitchenId = b.kitchenId "
					+ " and b.menuDate between :begDate and :endDate"
					+ " order by b.menuDate, s.schoolName, d.dishName, i.ingredientName, i.stockDate, i.manufactureDate,i.expirationDate, i.lotNumber, i.brand, i.sourceCertification, i.certificationId";
			
			log.debug("Download 學校食材 日期:"+begDate+"-"+endDate+" KitchenId:"+kid);
			Query ingredientFileQuery = session.createQuery(HQL);
			ingredientFileQuery.setParameter("kitchenId", kid);
			ingredientFileQuery.setParameter("begDate", begDate);
			ingredientFileQuery.setParameter("endDate", endDate);
			List ingredientFileFormat = ingredientFileQuery.list();// Schoolkitchen
			Iterator<Object[]> ingredientFileIterator = ingredientFileFormat.iterator();
			int row=2;
			while (ingredientFileIterator.hasNext()) {
				Object[] obj = ingredientFileIterator.next();
				String stockDate = obj[4]==null?"":CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",(Timestamp) obj[4]);
				String manufactureDate = obj[5]==null?"":CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",(Timestamp) obj[5]);
				String expirationDate =obj[6]==null?"": CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",(Timestamp) obj[6]);
				Integer supplierId = (Integer) obj[9];
				String supplierCompanyId = (String) obj[12];
				Supplier supplier = HibernateUtil.querySupplierById(session, kid, supplierId);
				String companyId="";
				if(CateringServiceUtil.isEmpty(supplierCompanyId)){
					companyId = supplier == null?"":supplier.getCompanyId();
				}else{
					companyId = supplierCompanyId;
				}
				
				
				data.put(String.valueOf(row), new Object[] {
					obj[0], 
					obj[1], 
					obj[2], 
					obj[3], 
					stockDate, 
					manufactureDate,
					expirationDate, 
					obj[7], 
					obj[8], 
					companyId, 
					obj[10],
					obj[11]
					});
				row++;
			}
			
			
			session.close();
			this.setFilePrefixName("學校食材");

		} else if (searchFileType.equals("vegetable")) {//菜色資料匯出
			SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
			Session session = sessionFactory.openSession();

			data.put("1", new Object[] {"菜色名稱", "食材名稱", "供應商名稱","品牌(製造商)"});
		
			
			String HQL = "select d.dishName, i.ingredientName, i.supplierId, i.brand, i.supplierCompanyId from Dish d, Ingredient i "
					+ "where d.dishId = i.dishId "
					+ "and d.kitchenId = :kitchenId "
					+ "order by d.dishName, i.ingredientName, i.brand";

			Query ingredientFileQuery = session.createQuery(HQL);
			ingredientFileQuery.setParameter("kitchenId", kid);
			List ingredientFileFormat = ingredientFileQuery.list();// Schoolkitchen
			Iterator<Object[]> ingredientFileIterator = ingredientFileFormat.iterator();
			int row=2;
			while (ingredientFileIterator.hasNext()) {
				Object[] obj = ingredientFileIterator.next();
				String dishName = (String) (obj[0]==null?"": obj[0]);
				String ingredientName = (String) (obj[1]==null?"": obj[1]);
				String supplierCompanyId = (String) (obj[4]==null?"": obj[4]);
				Supplier supplier = HibernateUtil.querySupplierById(session, kid, (Integer)obj[2] );
				String supplierName ="";
				//如果supplier id 存在就以它為主
				if(supplier!=null){
					supplierName=supplier.getSupplierName();
				}else{
					//如果supplier id 不存在就以companyId為主
					if(!CateringServiceUtil.isEmpty(supplierCompanyId)){
						supplier = HibernateUtil.querySupplierByCompanyId(session, kid, supplierCompanyId);
						if(supplier!=null){
							supplierName = supplier.getCompanyId();
						}
					}
				}
				
				//String supplierName = (String) (obj[2]==null?"": obj[2]);
				String brand = (String) (obj[3]==null?"": obj[3]);
				data.put(String.valueOf(row), new Object[] {
					dishName, 
					ingredientName, 
					supplierName, 
					brand	
					});
				row++;
			}
			session.close();
			this.setFilePrefixName("學校菜色");
		} else if(searchFileType.equals("menu")){//學校菜單Excel匯出
			SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
			Session session = sessionFactory.openSession();

	
			data.put("1", new Object[] { "學校", "日期", "主食一","主食二", "主菜", "主菜一", "主菜二","主菜三", "副菜一", "副菜二", "副菜三",
					"副菜四","副菜五","副菜六","蔬菜","湯品","附餐一","附餐二","全榖根莖","豆魚肉蛋", "蔬菜","油脂與堅果種子","水果", "乳品","熱量" });
			String sid = names[1].trim();
			String begDate = names[2].trim().replace("-", "/");
			String endDate = names[3].trim().replace("-", "/");
			
			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
			
			//int kitchenId = kid;//昌港
			
			Criteria criteria = session.createCriteria(Batchdata.class);
			criteria.add( Restrictions.between("menuDate", begDate, endDate) );
			criteria.add( Restrictions.eq("schoolId", Integer.valueOf( sid )) );
			criteria.add( Restrictions.eq("kitchenId", kid) );
			criteria.addOrder( Order.asc("menuDate") ).addOrder(Order.asc("schoolId"));
			int row=2;
			List<Batchdata> Menus = criteria.list();
			Iterator<Batchdata> iterator = Menus.iterator();
			while (iterator.hasNext()) {
				Batchdata menu = iterator.next();
				data.put(String.valueOf(row), new Object[] {  HibernateUtil.querySchoolNameById(session, menu.getSchoolId()),menu.getMenuDate(), HibernateUtil.queryDishNameById( session, menu.getMainFoodId()) ,HibernateUtil.queryDishNameById( session, menu.getMainFood1id()) ,
						HibernateUtil.queryDishNameById( session, menu.getMainDishId()  ), HibernateUtil.queryDishNameById( session, menu.getMainDish1id()  ), HibernateUtil.queryDishNameById( session, menu.getMainDish2id()  ),HibernateUtil.queryDishNameById( session, menu.getMainDish3id()  ),
						HibernateUtil.queryDishNameById( session, menu.getSubDish1id()  ), HibernateUtil.queryDishNameById( session, menu.getSubDish2id()  ), HibernateUtil.queryDishNameById( session, menu.getSubDish3id()  ),
						HibernateUtil.queryDishNameById( session, menu.getSubDish4id()  ),HibernateUtil.queryDishNameById( session, menu.getSubDish5id()  ),HibernateUtil.queryDishNameById( session, menu.getSubDish6id()  ),
						HibernateUtil.queryDishNameById( session, menu.getVegetableId() ),//蔬菜
						HibernateUtil.queryDishNameById( session, menu.getSoupId() ),//湯品
						HibernateUtil.queryDishNameById( session, menu.getDessertId()),//附餐一
						HibernateUtil.queryDishNameById( session, menu.getDessert1id()),//附餐二
						menu.getTypeGrains(),//全榖根莖
						menu.getTypeMeatBeans(),//豆魚肉蛋
						menu.getTypeVegetable(),//蔬菜
						menu.getTypeOil(),//油脂與堅果種子
						menu.getTypeFruit(),//水果
						menu.getTypeMilk(),//乳品
						menu.getCalorie()});
				row++;
			}
			session.close();
			this.setFilePrefixName("學校菜單");
		}

		// Iterate over data and write to sheet
		int rownum = data.size();
		for(int loopi=1;loopi<=rownum;loopi++){
			Row row = sheet.createRow(loopi-1);
			Object[] objArr = data.get( String.valueOf(loopi) );
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (obj instanceof String){
					cell.setCellValue((String) obj);
				}else if (obj instanceof Integer){
					cell.setCellValue( String.valueOf(obj));
					//cell.setCellValue((Integer) obj);
				}else{
					cell.setCellValue((String) obj);
				}
			}
		}
		/*
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		*/
		try {
			//Write the workbook in file system
			//java.util.Date date = new java.util.Date();
			//Timestamp ts = new Timestamp(date.getTime());
			//String currentDay = CateringServiceUtil.converTimestampToStr("yyyyMMdd", ts);
			//String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
			//fPath = uploadPatch + "download/"+searchFileType+"_"+ kid + "_" + currentDay + ".xlsx";
			/*
			fPath = "D:\\workspace2/Cateringservice/WebContent/WEB-INF/jsp/XLS/";// xls file path
			fPath += filename;// file name
			// fPath+="file";//file name
			fPath += ".xlsx"; // file type
			*/
			fPath = CateringServiceUtil.getDownloadPath(this.getRequestFileType());
		//	System.out.println("excel fPath:"+fPath +"   "+this.getFilePrefixName()+"   "+this.getRequestFileType());
			FileOutputStream out = new FileOutputStream(new File(fPath));
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fPath;
	}
	public String getFilePrefixName() {
		return filePrefixName;
	}
	public void setFilePrefixName(String filePrefixName) {
		this.filePrefixName = filePrefixName;
	}
	public String getRequestFileType() {
		return requestFileType;
	}
	public void setRequestFileType(String requestFileType) {
		this.requestFileType = requestFileType;
	}
}