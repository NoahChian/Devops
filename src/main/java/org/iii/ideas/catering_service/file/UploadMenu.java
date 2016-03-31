package org.iii.ideas.catering_service.file;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
/*
 * 上傳菜單主程式
 */
public class UploadMenu {
	private Session dbSession = null;
	private boolean overwrite = false;
	private HashMap<String, Integer> menuDateMap = new HashMap<String, Integer>();
	private Logger log = Logger.getLogger(this.getClass());
	public UploadMenu(Session session) {
		this.dbSession = session;
	}
	
	
	
	public void compareMenuAndBatchdata(Menu menu ,Batchdata batchdata) throws Exception{
		if(menu.getMainFoodId() != batchdata.getMainFoodId()  && menu.getMainFoodId() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 主食一內容不一致!");
		}else{
			menu.setMainFoodId(batchdata.getMainFoodId());
		}
		
		if(menu.getMainFood1id() != batchdata.getMainFood1id()  && menu.getMainFood1id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 主食二內容不一致!");
		}else{
			menu.setMainFood1id(batchdata.getMainFood1id());
		}
		
		if(menu.getMainDishId() != batchdata.getMainDishId()  && menu.getMainDishId() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 主菜一內容不一致!");
		}else{
			menu.setMainDishId(batchdata.getMainDishId());
		}
		
		if(menu.getMainDish1id() != batchdata.getMainDish1id()  && menu.getMainDish1id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 主菜二內容不一致!");
		}else{
			menu.setMainDish1id(batchdata.getMainDish1id());
		}
		
		if(menu.getMainDish2id() != batchdata.getMainDish2id()  && menu.getMainDish2id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 主菜三內容不一致!");
		}else{
			menu.setMainDish2id(batchdata.getMainDish2id());
		}
		
		if(menu.getMainDish3id() != batchdata.getMainDish3id()  && menu.getMainDish3id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 主菜四內容不一致!");
		}else{
			menu.setMainDish3id(batchdata.getMainDish3id());
		}
		
		if(menu.getSubDish1id() != batchdata.getSubDish1id()  && menu.getSubDish1id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副菜一內容不一致!");
		}else{
			menu.setSubDish1id(batchdata.getSubDish1id());
		}
		
		if(menu.getSubDish2id() != batchdata.getSubDish2id()  && menu.getSubDish2id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副菜二內容不一致!");
		}else{
			menu.setSubDish2id(batchdata.getSubDish2id());
		}
		
		if(menu.getSubDish3id() != batchdata.getSubDish3id()  && menu.getSubDish3id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副菜三內容不一致!");
		}else{
			menu.setSubDish3id(batchdata.getSubDish3id());
		}
		
		if(menu.getSubDish4id() != batchdata.getSubDish4id()  && menu.getSubDish4id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副菜四內容不一致!");
		}else{
			menu.setSubDish4id(batchdata.getSubDish4id());
		}
		
		if(menu.getSubDish5id() != batchdata.getSubDish5id()  && menu.getSubDish5id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副菜五內容不一致!");
		}else{
			menu.setSubDish5id(batchdata.getSubDish5id());
		}
		
		if(menu.getSubDish6id() != batchdata.getSubDish6id()  && menu.getSubDish6id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副菜五內容不一致!");
		}else{
			menu.setSubDish6id(batchdata.getSubDish6id());
		}
		
		if(menu.getDessertId() != batchdata.getDessertId()  && menu.getDessertId() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副餐一內容不一致!");
		}else{
			menu.setDessertId(batchdata.getDessertId());
		}
		
		if(menu.getDessert1id() != batchdata.getDessert1id()  && menu.getDessert1id() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 副餐二內容不一致!");
		}else{
			menu.setDessert1id(batchdata.getDessert1id());
		}
		
		if(menu.getSoupId() != batchdata.getSoupId()  && menu.getSoupId() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 湯品內容不一致!");
		}else{
			menu.setSoupId(batchdata.getSoupId());
		}
		
		if(menu.getVegetableId() != batchdata.getVegetableId()  && menu.getVegetableId() !=0){
			throw new Exception("日期:"+menu.getMenuDate()+" 蔬菜內容不一致!");
		}else{
			menu.setVegetableId(batchdata.getVegetableId());
		}
		
		if(  (!menu.getTypeFruit().equals(batchdata.getTypeFruit()))  &&  (!menu.getTypeFruit().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 水果內容不一致!");
		}else{
			menu.setTypeFruit(batchdata.getTypeFruit());
		}
		
		if(  (!menu.getTypeGrains().equals(batchdata.getTypeGrains()))  &&  (!menu.getTypeGrains().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 全榖根莖內容不一致!");
		}else{
			menu.setTypeGrains(batchdata.getTypeGrains());
		}
		
		if(  (!menu.getTypeMeatBeans().equals(batchdata.getTypeMeatBeans()))  &&  (!menu.getTypeMeatBeans().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 豆魚肉蛋內容不一致!");
		}else{
			menu.setTypeMeatBeans(batchdata.getTypeMeatBeans());
		}
		
		if(  (!menu.getTypeMilk().equals(batchdata.getTypeMilk()))  &&  (!menu.getTypeMilk().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 乳品內容不一致!");
		}else{
			menu.setTypeMilk(batchdata.getTypeMilk());
		}
		
		if(  (!menu.getTypeOil().equals(batchdata.getTypeOil()))  &&  (!menu.getTypeOil().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 油脂與堅果種子內容不一致!");
		}else{
			menu.setTypeOil(batchdata.getTypeOil());
		}
		
		if(  (!menu.getTypeVegetable().equals(batchdata.getTypeVegetable()))  &&  (!menu.getTypeVegetable().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 蔬菜內容不一致!");
		}else{
			menu.setTypeVegetable(batchdata.getTypeVegetable());
		}
		
		if(  (!menu.getCalorie().equals(batchdata.getCalorie()))  &&  (!menu.getCalorie().equals("")) ){
			throw new Exception("日期:"+menu.getMenuDate()+" 熱量內容不一致!");
		}else{
			menu.setCalorie(batchdata.getCalorie());
		}
		
		
		
	}

	public int insMenu(Session session,  Batchdata batchdata, String SchoolName) throws Exception {
		Kitchen tmpKitchen = HibernateUtil.queryKitchenById(session, batchdata.getKitchenId());
		//如果為overwrite 就清除舊舊資料
		if (this.isOverwrite()) {
			//清除這間學校當天的所有batchdata 資料
			HibernateUtil.deleteBatchdataByUK(session, batchdata.getKitchenId(),batchdata.getSchoolId(), batchdata.getMenuDate(),batchdata.getLotNumber());
			log.debug("清除菜單內容! 團膳業者或自立廚房:"+tmpKitchen.getKitchenName() +" 日期:"+batchdata.getMenuDate()+" 學校:"+SchoolName);
			//System.out.println("清除菜單內容! 團膳業者或自立廚房:"+tmpKitchen.getKitchenName() +" 日期:"+batchdata.getMenuDate()+" 學校:"+SchoolName);
		}else if(HibernateUtil.queryBatchdataByUK(session, batchdata.getKitchenId(),batchdata.getSchoolId(), batchdata.getMenuDate(),batchdata.getLotNumber())!=null){
			log.debug("菜單重覆內容! 團膳業者或自立廚房:"+tmpKitchen.getKitchenName() +" 日期:"+batchdata.getMenuDate());
			//System.out.println("菜單重覆內容! 團膳業者或自立廚房:"+tmpKitchen.getKitchenName() +" 日期:"+batchdata.getMenuDate());
			return -1;
		}
		//以日期為單位,put 資料到array 中,計算menu 是否有被清除
		if(menuDateMap.get(batchdata.getMenuDate())==null){
			menuDateMap.put(batchdata.getMenuDate(), 1);
		}else{
			menuDateMap.put(batchdata.getMenuDate(), menuDateMap.get(batchdata.getMenuDate())+1);
		}
		
		batchdata.setUploadDateTime(CateringServiceUtil.getCurrentTimestamp());
		/*
		if(HibernateUtil.queryBatchdataByUK(session, batchdata.getKitchenId(), batchdata.getSchoolId(), batchdata.getMenuDate(), batchdata.getLotNumber()) != null){
			log.debug("菜單重覆內容! 團膳業者或自立廚房:"+tmpKitchen.getKitchenName() +" 日期:"+batchdata.getMenuDate()+" 學校:"+SchoolName);
			return -1;
		}else{
			log.debug("新增菜單內容! 團膳業者或自立廚房:"+tmpKitchen.getKitchenName() +" 日期:"+batchdata.getMenuDate()+" 學校:"+SchoolName);
		}
		*/
		HibernateUtil.saveBatchdata(session, batchdata);
		return 1;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
}
