package com.java.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Reads a properties file from workspace.
 * and provides methods to operate on the read file.
 * @author shrjoshi
 *
 */
public class ReadPropertiesFile {
	
	private static Properties properties=null;
	
	/**
	 * Return the value for a specific key in the properties file.
	 * 
	 * @param key - the property key 
	 * @return empty String if no value was found for the key, else return the actual value.
	 */
	public static String getPropValues(String key){
		String value = ""; 
		try {
			if(properties==null){
				File file = new File(System.getProperty("user.dir")+"/resource/propertiesFile/inputdata.properties");
				FileInputStream fileInput = new FileInputStream(file);
				
				properties = new Properties();
				properties.load(fileInput);
				fileInput.close();
			}
			value = properties.getProperty(key);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return value;
	}
}
