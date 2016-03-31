package org.iii.ideas.catering_service.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertyGetter {
	
	public String getPropertyByKey(String key) throws IOException
	{
		Resource propertyResource = new ClassPathResource("shoppingwall.property");
		Properties props;
		
		props = PropertiesLoaderUtils.loadProperties(propertyResource);
		String valueString = props.getProperty(key);
		return valueString;
		
	}
}
