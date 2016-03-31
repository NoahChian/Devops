package org.iii.ideas.catering_service.tool;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.iii.ideas.catering_service.ws.schemav2.CateringServiceEndPointv2Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileRotate {
	private static final Logger log = LoggerFactory.getLogger(CateringServiceEndPointv2Impl.class);
	
	
	private static void retrieveFile(String dirPath){
		
		
	}
	
	
	//刪除檔案
	private static  void unlinkFile(String path){
		try{
			File target=new File(path);
			if (target.isDirectory()){
				FileUtils.deleteDirectory(target);
			}else{
				target.delete();
			}
		}catch (Exception ex){

			ex.printStackTrace();
		}
	}
}
