package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.iii.ideas.catering_service.rest.api.QuerySfschoolproductsetBySchool;
import org.iii.ideas.catering_service.rest.bo.SfSchoolproductsetBO;

public class AuditSchoolproductset implements IGenerateExcel {
	private Integer schoolId;
	private String approveStatus;
	private String startDate;
	private String endDate;

	public AuditSchoolproductset(Integer schoolId, String approveStatus, String startDate, String endDate) {
		this.schoolId = schoolId;
		this.approveStatus = approveStatus;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		
		data.put("1", new Object[] { "序號", "產品名稱", "上架日", "下架日", "狀態", "申請日期", "最後更新者", "最後更新日期" });
		QuerySfschoolproductsetBySchool qryAuditRecords = new QuerySfschoolproductsetBySchool();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SfSchoolproductsetBO> sfschoolproductsetboList = qryAuditRecords.processQuery(schoolId, approveStatus, startDate + " 00:00:00", endDate + " 23:59:59", sdf);
		if (sfschoolproductsetboList != null && sfschoolproductsetboList.size() > 0) {
			for (int i = 0; i < sfschoolproductsetboList.size(); i++) {
				SfSchoolproductsetBO sfschoolproductsetBO = sfschoolproductsetboList.get(i);
				Object[] objArray = new Object[8];
				objArray[0] = new Integer(i + 1);
				objArray[1] = sfschoolproductsetBO.getProductName();
				objArray[2] = sfschoolproductsetBO.getOnShelfDate();
				objArray[3] = sfschoolproductsetBO.getOffShelfDate();
				objArray[4] = sfschoolproductsetBO.getStatus();
				objArray[5] = sfschoolproductsetBO.getCreateDateTime();
				objArray[6] = sfschoolproductsetBO.getModifyUser();
				objArray[7] = sfschoolproductsetBO.getModifyDateTime();
				data.put("" + (i + 2), objArray);
			}
		}
		return data;
	}
}
