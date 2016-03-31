package org.iii.ideas.catering_service.rest.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.IngredientCertificate;
import org.iii.ideas.catering_service.dao.IngredientCertificateDAO;
import org.iii.ideas.catering_service.util.XmlUtil;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

public class CasQueryService extends AbstractApiInterface<CasQueryServiceRequest, CasQueryServiceResponse> {
	public CasQueryService(){}
	
	public CasQueryService(Session dbSession) {
		this.dbSession = dbSession;
	}

	@Override
	public void process() throws NamingException  {		
		String certNo = this.requestObj.getCertNo();
		String certType = "CAS";
		
		IngredientCertificateDAO icDao = new IngredientCertificateDAO();
		IngredientCertificate ic = icDao.queryIngredientCertificateByCertNo(certNo, certType);
		
		if (ic == null) {
			switch (certType) {
			case "CAS":
				if(!certNo.isEmpty()){
					//20140827 SOAP問題先行MARK
					try{
						String json = queryCompany(certNo);
					
						if(json != "" && json != null){
							IngredientCertificateObject obj = transJsonStringToIngredientCertificateObject(json);
							if(obj!=null){
								ic = new IngredientCertificate();
								ic.setCertNo(certNo);
								ic.setCertType(certType);
								ic.setSourceJson(json);
								icDao.updateIngredientCertificate(ic);
								ic = icDao.queryIngredientCertificateByCertNo(certNo, certType);
							}
						}
					} catch (NamingException e) {
						// #13440 查詢CAS資料的API在傳入錯誤的CAS編號後會回傳錯誤訊息
						log.debug(e.getMessage());
						this.responseObj.setMsg("查無資料");
						this.responseObj.setResStatus(0);
					}
				}
				break;
			case "吉園圃":
				break;
			default:
				break;
			}
		}
		
		try {
			if(ic != null && ic.getSourceXml() != null) {
				this.responseObj.setResStatus(1);
				this.responseObj.setIngredientCertificateObject(transXmlStringToIngredientCertificateObject(ic.getSourceXml()));
			} else if(ic != null && ic.getSourceJson() != null) {
				this.responseObj.setResStatus(1);
				this.responseObj.setIngredientCertificateObject(transJsonStringToIngredientCertificateObject(ic.getSourceJson()));
			} else {
				this.responseObj.setMsg("查無資料");
				this.responseObj.setResStatus(0);
			}
		}catch (Exception e) {
			log.error("Connect to TCBK SOAP error" , e);
			this.responseObj.setMsg(e.getMessage());;
			this.responseObj.setResStatus(0);
		}
	}
	
	public String queryCompany(String certNo) throws NamingException{
		String returnStr = "";
		String USER_AGENT = "Mozilla/5.0";

//		3.DB沒資料, 則呼叫CAS OpenData取得資料,轉成json格式 , 存入 ingredientcertificate , 並回傳json
		try {
			String url = "http://data.coa.gov.tw/Service/OpenData/CASdata.aspx?$top=1&$skip=0&$filter=ProductNo+like+"+certNo;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");				
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setDoOutput(true);
			
			int responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			returnStr = response.toString();
			
			//只撈一筆,移除陣列表示
			if(returnStr !=null){
				returnStr = returnStr.replace("[", "");
				returnStr = returnStr.replace("]", "");
			}
			if ("".equals(returnStr.trim())) {
				returnStr = "";
			}
		} catch (Exception e) {
			log.error("Connect to OPEN DATA error" , e);
		}
		
		if("".equals(returnStr) || returnStr ==null){
			throw new NamingException("Data not found ( certNo = "+certNo+" )");
		}
		return returnStr;
	}
	
	//轉換IngredientCertificate source XML to object
	private IngredientCertificateObject transJsonStringToIngredientCertificateObject(String json) throws NamingException{
		IngredientCertificateObject ico = new IngredientCertificateObject();
		
		JSONObject obj;
		if(json != "" && json != null){
			obj = new JSONObject(json);
			ico.setCertNo(obj.getString("ProductNo"));
			ico.setCertType("CAS");
			ico.setCompanyId("");//opendata 目前沒回傳統一編號
			ico.setCompanyName(obj.getString("CompanyName"));
			ico.setAddress(obj.getString("CompanyAddr"));
			ico.setDirector(obj.getString("CompanyManager"));
			ico.setFax(obj.getString("CompanyFax"));
			ico.setTel(obj.getString("CompanyTel"));
			ico.setWebsite(obj.getString("CompanyURL"));
		} else {
			return null;
		}
		return ico;
	}
	
	//轉換IngredientCertificate source XML to object
	private IngredientCertificateObject transXmlStringToIngredientCertificateObject(String xml) throws Exception{
		IngredientCertificateObject ico = new IngredientCertificateObject();
		Document doc = XmlUtil.loadXML(xml);
		String result = XmlUtil.getNodeValue(doc, "Result");
		
		if(result != null && result.equals("Success")){
			ico.setCertNo(XmlUtil.getNodeValue(doc, "Emblem_ID"));
			ico.setCompanyId(XmlUtil.getNodeValue(doc, "CompanyId"));
			ico.setCompanyName(XmlUtil.getNodeValue(doc, "Name"));
			ico.setAddress(XmlUtil.getNodeValue(doc, "Address"));
			ico.setDirector(XmlUtil.getNodeValue(doc, "Director"));
			ico.setFax(XmlUtil.getNodeValue(doc, "Fax"));
			ico.setTel(XmlUtil.getNodeValue(doc, "Tel"));
			ico.setWebsite(XmlUtil.getNodeValue(doc, "Website"));
		}else{
			return null;
		}
		return ico;
	}
}