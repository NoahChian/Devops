package org.iii.ideas.catering_service.rest.excel.create;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Map;

public class CreateExcelData {

	private IGenerateExcel generateExcel;

	/**
	 * Constructor
	 * 
	 * @param generateExcel
	 */
	private CreateExcelData() {
		
	}
	
	protected CreateExcelData(IGenerateExcel generateExcel) {
		this.generateExcel = generateExcel;
	}

	public static CreateExcelData getInstance(IGenerateExcel generateExcel) {
		CreateExcelData result = new CreateExcelData(generateExcel);
		return result;
	}
	
	public Map<String, Object[]> doGenerateData() throws UnsupportedEncodingException, ParseException {

		return generateExcel.generateExcelData();
	}

}
