package org.iii.ideas.catering_service.ws.endpoint;

public class CateringServiceResponseType {
	public final static String SUCCESS = "0";
	public final static String ERROR_DATAFORMAT = "100";
	public final static String ERROR_CANNOT_ACCESS_UPLOADDATE_DATA = "101";
	public final static String ERROR_PRODUCT_BARCODE_FORMAT = "102";
	public final static String PRODUCT_DUPLICATE = "103";
	public final static String ERROR_UNKNOWN = "104";
	public static String getMessage(String code){
		String msg="";
		if(code.equals(SUCCESS)){
			msg="上傳成功";
		}else if(code.equals(ERROR_DATAFORMAT)){
			msg="資料檢查失敗";
		}else if(code.equals(ERROR_CANNOT_ACCESS_UPLOADDATE_DATA)){
			msg="無法存取上傳資料";
		}else if(code.equals(ERROR_PRODUCT_BARCODE_FORMAT)){
			msg="產品一維條碼格式不正確";
		}else if(code.equals(PRODUCT_DUPLICATE)){
			msg="產品重複";
		}else if(code.equals(ERROR_UNKNOWN)){
			msg="其他錯誤";
		}
		return msg;
	}
}
