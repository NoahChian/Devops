package org.iii.ideas.catering_service.rest;

public enum WSError {
	//General Error
	  INTERNAL_ERROR(200,"系統內部錯誤"),
//	  MD5_CHECK_FAIL(100,"MD5檢查失敗"),
//	  AUTHENTICATE_USER_FAIL(101,"身份驗證失敗"),
//	  DUPLICATE_COMPANY_ID(102,"公司統編已存在系統中"),
//	  DUPLICATE_MANUFACTURE_ID(103,"產線編號已存在系統中"),	  
//	  PAYLOAD_IS_EMPTY(104,"無法存取上傳資料"),
//	  XML_VALIDATE_FAIL(105,"XML 驗證失敗"),
//	  GS1_FORMAT_ERROR(106,"產品一維條碼格式不正確"),
//	  DUPLICATE_PRODUCT(107,"產品重複"),
//	  UPDATE_BATCH_PRODUCT_FAIL(108,"無法修改批次生產資料"),
//	  WITHOUT_CCPDEVICE_NAME(109,"未填入DeviceCcpName"),
//	  WITHOUT_STD_VALUE(110,"未填入 LowerStd 或  CeilingStd 值"),
//	  DATE_FORMAT_ERROR(111,"日期格式錯誤，請填入 yyyy-MM-dd 之格式，如民國100年1月1號，請填入 2011-01-01"),
//	  COMPANY_NOT_EXIST_COM(112,"公司統編不存在，請先建立公司資料"),
//	  ACCOUNT_NOT_EXIST_FDA(113,"帳號尚未成功建立"),
//	  USER_ACCOUNT_REPEAT(1,"user account repeat"),
//	  RESOURCE_ID_REPEAT(2,"resource id repeat"),
//	  PACKAGE_ID_REPEAT(3,"package id repeat"),
//	  RESOURCE_ID_NOT_EXIST(4,"can not find resource id"),
//	  PACKAGE_ID_NOT_EXIST(5,"can not find package id"),	  
//	  RID_NOT_EXIST(6,"can not find rid"),
//	  MULTIPLE_RESOURCE_ID(7,"find at least two resrouce object with the same resource id"),
//	  BILL_NOT_EXIST(8,"can not find the bill")
	  
	  //Company upload errors  
//	  DUPLICATE_MANUFACTURE_USERACCOUNT(130,"使用者帳號已存在系統中"),
//	  NO_APPLIABLE_BATCHDATE(131,"沒有填入適當的批次日期"),
//	  SUPPLIER_HAS_NO_COMPANY_ID(132,"原料沒有帶入公司統編"),
//	  SUPPLIER_WITHOUT_APPLIABLE_BATCHDATE(133,"原料沒有填入適當的批次日期"),
//	  ATTACHED_FILE_NOT_EXIST(134,"附加檔案不存在"),
//	  MULTIPLE_UPLOAD_DATA(135,"此批號日期之產品已存在系統中了"),
//	  PRODUCT_NOT_EXIST(136,"此公司無此商品，請先新增商品資訊"),
//	  COMPANY_WITHOUT_MANUFACTURERACCOUNT(137,"此公司無此產線帳號，請先新增產線"),
//	  MULTIPLE_UPLOAD_GMP_DATA(138,"此批號日期之原料履歷已存在")
	  PRODUCT_NOT_EXIST(1,"商品不存在"),
	  ACCOUNT_NOT_EXIST(2,"會員不存在"),
	  DELIVERY_INFO_NOT_EXIST(3,"收件人資訊不存在"),
	  STORE_NOT_EXIST(4,"商店不存在"),
	  MULTIPLE_PRODUCT_BELONGS_TO(5,"找到兩筆以上相同的商品分店"),
	  MULTIPLE_PRPDUCT_OPTION_DATA(6,"找到兩筆以上相同的商品選項項目編號"),
	  MULTIPLE_COUPON(7, "找到兩筆以上相同的優惠券"),
	  PRODUCT_BELONGS_TO_NOT_EXIST(8, "檔次的分店不存在"),
	  PRODUCT_OPTION_DATA_NOT_EXIST(9, "檔次的選項項目不存在"),
	  COUPON_NOT_EXIST(10,"優惠券不存在"),
	  MULTIPLE_PRODUCT(11,"找到兩筆以上相同的商品"),
	  MULTIPLE_ACCOUNT(12, "找到兩筆以上相同的會員"),
	  COUPON_COLLECTION_NOT_EXIST(13, "優惠券收藏記錄不存在"),
	  MULTIPLE_COUPON_COLLECTION(14, "找到兩筆以上相同的優惠券收藏記錄"),
	  COUPON_ALREADY_REDEMPTION(15, "優惠券已兌換過"),
	  DIGITALSIGNAGE_NOT_EXIST(16, "看板不存在"),
	  MULTIPLE_DIGITALSIGNAGE(17,"找到兩筆以上相同的看板資訊"),
	  MULTIPLE_STORE(18,"找到兩筆以上相同的商店資訊"),
	  DIGITALSIGNAGE_TITLE_NOT_EXIST(19,"看板標題不存在"),
	  MULTIPLE_DIGITALSIGNAGE_TITLE(20, "找到兩筆以上相同的看板標題資訊"),
	  PRODUCT_ORDER_NOT_EXIST(21,"訂單不存在"),
	  INVOICE_MANAGEMENT_ID_NOT_EXIST(22,"發票資訊不存在"),
	  MULTIPLE_PRODUCT_ORDER(23, "找到兩筆以上相同的訂單"),
	  MULTIPLE_INVOICE_MANAGEMENT(24, "找到兩筆以上相同的發票資訊"),
	  PRODUCT_VOUCHER_NOT_EXIST(25,"憑證不存在"),
      PRODUCT_ORDER_VOUCHER_NOT_EXIST(26,"憑證不存在"),
	  VOUCHER_ID_NOT_EXITST(27,"該訂單無此憑證"),      
	  MULTIPLE_PRODUCT_VOUCHER(28, "找到兩筆以上相同的憑證訊"),
	  FB_ID_NOT_EXIST(29, "找到兩筆以上相同的憑證訊")
	  ;

	  private final int code;
	  private final String description;

	  private WSError(int code, String description) {
	    this.code = code;
	    this.description = description;
	  }

	  public String getDescription() {
	     return description;
	  }

	  public int getCode() {
	     return code;
	  }

	  @Override
	  public String toString() {
	    return code + ": " + description;
	  }
	}