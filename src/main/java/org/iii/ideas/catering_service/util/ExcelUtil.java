package org.iii.ideas.catering_service.util;

import java.sql.Timestamp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
/**
 * 
 * @author Raymond 2014/05/12
 *
 */
public class ExcelUtil {
	
	/**
	 * 依照Excel cell type 回傳cell值
	 * @param cell
	 * @return
	 * @throws Exception
	 */
	public static String getCellValue(XSSFCell cell) throws Exception {
		if(cell==null)
			return null;
		String cellValue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			throw new Exception("欄位格式有誤,請確認所有欄位為文字格式");
			// break;
		case Cell.CELL_TYPE_ERROR:
			throw new Exception("欄位格式有誤,請確認所有欄位為文字格式");
			// break;
		case Cell.CELL_TYPE_FORMULA:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cellValue = cell.getStringCellValue().trim();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			XSSFCell tmpCell = cell;
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(tmpCell)) {
				try {
					cellValue = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", new Timestamp(cell.getDateCellValue().getTime()));
				} catch (Exception e) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellValue = cell.getStringCellValue().trim();
				}
			} else if (org.apache.poi.ss.usermodel.DateUtil.isCellInternalDateFormatted(tmpCell)) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue().trim();
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue().trim();
				cellValue = CateringServiceUtil.excelNumber(cellValue, "#.#");
			}

			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue().trim();
			break;
		default:
			throw new Exception("不明欄位格式,請確認所有欄位為文字格式");
		}

		return cellValue;
	}
}
