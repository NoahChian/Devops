package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.iii.ideas.catering_service.rest.api.QueryAcceptSchoolKitchenBySchool;
import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;

/*
 * 匯出供餐廚房審核紀錄查詢結果,組合excel內容
 */
public class AuditSchoolkitchen implements IGenerateExcel {
	private Integer schoolId;
	private String approveStatus;
	private String startDate;
	private String endDate;

	public AuditSchoolkitchen(Integer schoolId, String approveStatus, String startDate, String endDate) {
		this.schoolId = schoolId;
		this.approveStatus = approveStatus;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();

		data.put("1", new Object[] { "序號", "學校名稱", "狀態", "動作", "申請日期", "最後更新者", "最後更新日期" });
		QueryAcceptSchoolKitchenBySchool qryAuditRecords = new QueryAcceptSchoolKitchenBySchool();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<AcceptschoolkitchenBO> acceptschoolkitchenboList = qryAuditRecords.processQuery(schoolId, approveStatus, startDate + " 00:00:00", endDate + " 23:59:59", sdf);
		if (acceptschoolkitchenboList != null && acceptschoolkitchenboList.size() > 0) {
			for (int i = 0; i < acceptschoolkitchenboList.size(); i++) {
				AcceptschoolkitchenBO acceptschoolkitchenBO = acceptschoolkitchenboList.get(i);
				Object[] objArray = new Object[7];
				objArray[0] = new Integer(i + 1);
				objArray[1] = acceptschoolkitchenBO.getSchoolName();
				objArray[2] = acceptschoolkitchenBO.getStatus();
				objArray[3] = acceptschoolkitchenBO.getAction();
				objArray[4] = acceptschoolkitchenBO.getCreateDateTime();
				objArray[5] = acceptschoolkitchenBO.getModifyUser();
				objArray[6] = acceptschoolkitchenBO.getModifyDateTime();
				data.put("" + (i + 2), objArray);
			}
		}
		return data;
	}

}
