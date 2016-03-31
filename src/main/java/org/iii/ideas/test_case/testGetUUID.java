package org.iii.ideas.test_case;

import org.iii.ideas.catering_service.service.CateringServiceLoader;

public class testGetUUID {
	public static void main(String args[]) throws Exception{
		int i =10;
		while(i>0){
			System.out.println(i);
			System.out.println("get Seqence:"+CateringServiceLoader.getSeqNo());
			i--;
			
		}
	}
}
