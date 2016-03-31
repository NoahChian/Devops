package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryIngredientListByDate extends AbstractApiInterface<QueryIngredientListByDateRequest, QueryIngredientListByDateResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7158368662511894691L;

	@Override
	public void process() throws NamingException, ParseException {
		String menuDate = this.requestObj.getMenuDate();

		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		List<Long> dishIdList = new ArrayList<Long>();
		List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, this.getKitchenId(), menuDate);
		Iterator<Batchdata> batchdataIterator = batchdataList.iterator();
		while (batchdataIterator.hasNext()) {
			Batchdata tmpBatchdata = batchdataIterator.next();
			if (tmpBatchdata.getMainFoodId() == 0 && tmpBatchdata.getSrcType().equals("A")) {
				continue;
			} // 20140521 Raymond 先排除台北市資料,目前台北市是已SOAP方式把資料傳入
			dishIdList = HibernateUtil.getDishIdByBatchdata(dishIdList, tmpBatchdata);
		}

		Iterator<Long> dishIdIterator = dishIdList.iterator();
		while (dishIdIterator.hasNext()) {
			Long dishId = dishIdIterator.next();
			String dishName = HibernateUtil.queryDishNameById(this.dbSession, dishId);
			if (!dishName.equals("")) {
				DishsObject dishObj = new DishsObject();
				dishObj.setDishId(dishId);
				dishObj.setName(dishName);
				this.responseObj.getDishs().add(dishObj);
			}
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
