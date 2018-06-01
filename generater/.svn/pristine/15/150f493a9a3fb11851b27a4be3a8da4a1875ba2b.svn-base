package com.ymhj.yas.generater.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesConfig {
	private static final Map<String,String> config = new HashMap<String,String>();
	public static void loadProperties(){
		Properties prop =  new  Properties();    
        InputStream in = Object. class .getResourceAsStream( "/generater-code.properties" );    
         try  {    
            prop.load(in);   
            Set<Entry<Object, Object>> propSet = prop.entrySet();
            Iterator<Entry<Object, Object>> iterator = propSet.iterator();
            while(iterator.hasNext()){
            	Entry<Object, Object> en = iterator.next();
            	config.put(toString(en.getKey()),toString(en.getValue()));
            }
            
            System.out.println(config);
            
        }  catch  (IOException e) {    
            e.printStackTrace();    
        }
	}
	
	public static Map<String,String> config(){
		if(config.size() == 0){
			loadProperties();
		}
		return config;
	}
	
	public static String get(String key){
		System.out.println(config());
		return config().get(key);
	}
	
	
	
	public static String toString(Object obj){
		if(null == obj){
			return null;
		}
		return String.valueOf(obj);
	}
	
	public static void main(String[] args) {
		/*InputStream in = PropertiesConfig.class.getResourceAsStream("/generater-code.properties");
		System.out.println(in);*/
		String driverName = PropertiesConfig.get("db.driverName");
		System.out.println(driverName);
		
	}
}
