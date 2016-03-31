package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class CustomerQueryMonthBySchool  extends AbstractApiInterface<CustomerQueryMonthBySchoolRequest, CustomerQueryMonthBySchoolResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		int sid = this.requestObj.getSid();
		
		
		Timestamp currentTS =  CateringServiceUtil.getCurrentTimestamp();
		String currentTSStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", currentTS);
		//------------計算是否有前後一天------------
		String HQL = "";
		String beforeMidHQL = "";
		//因應未來菜單可以顯示，未來菜單的月份仍可被搜尋。 modify by ellis 20150113
//		if(this.getUsername() == null){
//			HQL = "select distinct substring(id.menuDate,1,7)  as yearMonth from Batchdata where id.schoolId = :sid and  id.menuDate <= :menuDate order by id.menuDate desc";
//		}else{
			HQL = "select distinct substring(id.menuDate,1,7)  as yearMonth from Batchdata where id.schoolId = :sid order by id.menuDate desc";
//		}
		
		
		Query query = dbSession.createQuery(HQL);
//		if(this.getUsername() == null){
//			query.setParameter("menuDate", currentTSStr);
//		}
		query.setParameter("sid", sid);
       
		List results = query.list();
        Iterator<String> iterator = results.iterator();
        while(iterator.hasNext()){
			YearMonthObject ym = new YearMonthObject();
			String obj = iterator.next();
			ym.setYearMonth(obj);
			this.responseObj.getDate().add(ym);
		}
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
		
		
	}

}
