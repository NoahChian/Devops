package org.iii.ideas.catering_service.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class SystemConfiguration {
	static Logger log= Logger.getLogger(SystemConfiguration.class);
	
	static HashMap <String,String> args = new HashMap <String,String>();
	//public static String cacheServer = "127.0.0.1:11211";
	
	
	public SystemConfiguration() {
		log = Logger.getLogger(SystemConfiguration.class);
	}
	
	public static String writeReciveWS(String messageId,String xml,String msg,Date currentDate,String type) throws Exception{
		String rev_path = getConfig("fcloud_in_ws");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateFormat_yyyymm = new SimpleDateFormat("yyyyMM");
		DateFormat dateFormat_dd = new SimpleDateFormat("dd");
        
        Date date = currentDate;
        

		//加入資料夾分類
        //資料夾分類規則更改為ws/dyyyymm/dd ex. ws/d201503/31/
		rev_path += "/d"+dateFormat_yyyymm.format(date)+"/"+dateFormat_dd.format(date)+"/";
        
		dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
		
        File f = new File(rev_path);
		if (f.exists()==false) {
			new File(rev_path).mkdirs();
		}
		String path = rev_path+File.separator+dateFormat.format(date)+"-"+messageId+"-"+msg;
        String filename = path+"-"+type+".ws";
		log.info("Save xml file:"+filename);
		FileOutputStream fileOutputStream = new FileOutputStream(filename);
		Writer out = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
		out.write(xml);
		out.close();
		return filename;
	}
	
	public static String getConfig(String arg) throws Exception{
		String ret = null;
		try {
			if(args.get(arg)!=null){
				return args.get(arg);
			}else{				
				ret = getContextValue(arg);			
				args.put(arg, ret);
			}
		}
		catch (NamingException e) {
			log.warn(e.getMessage());
			throw new Exception ("系統參數未設定:"+arg);
		}
		return ret;
	}

	private synchronized  static String getContextValue(String arg) throws NamingException {
		InitialContext initialContext = new javax.naming.InitialContext();
		return (String) initialContext.lookup("java:comp/env/"+arg);
	}
	
}
