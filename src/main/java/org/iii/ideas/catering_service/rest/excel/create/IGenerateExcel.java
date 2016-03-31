package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.Map;

public interface IGenerateExcel {
	
	/**
	 * 產生Excel資料
	 * @return
	 * @throws ParseException TODO
	 */
	public Map<String, Object[]> generateExcelData() throws ParseException;
	
}
