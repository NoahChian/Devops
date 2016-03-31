package org.iii.ideas.catering_service.util;

import java.util.ArrayList;
import java.util.List;

public class TypeUtil {
	public static List<String> longListToStringList(List<Long> longList){
		List<String> stringList = new ArrayList<String>();
		for(Long val : longList){
			stringList.add(String.valueOf(val));
		}
		return stringList;
	}
}
