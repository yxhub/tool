package com.ymhj.yas.generater.pojo;

import java.sql.Connection;

public class Config {

	private  String moduleName = ""; // 对应模块名称
	
	private String path = "D:/exopt";
	 
	private  String bean_path = path+"/entity_bean";
 
    private  String mapper_path = path+"/entity_mapper";
 
    private  String xml_path = path+"/entity_mapper/xml";
    
    private  String sys_name = "payment";
 
    private  String bean_package = "com.ymhj.yas." + sys_name + ".domain" + moduleName + "";
 
    private  String mapper_package = "com.ymhj.yas." + sys_name + ".dao" + moduleName + "";
 
    private  String driverName = "com.mysql.jdbc.Driver";
 
    private  String user = "root";
 
    private  String password = "yuyang";
 
    private  String url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8";
 
    private String tableName = null;
 
    private String beanName = null;
 
    private String mapperName = null;
// 
    private Connection conn = null;
    
    /**/
    
    public String getPath() {
		return path;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getMapperName() {
		return mapperName;
	}
	public void setMapperName(String mapperName) {
		this.mapperName = mapperName;
	}
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public String getModuleName() {
		return moduleName;
	}
	public String getBean_path() {
		return bean_path;
	}
	public String getMapper_path() {
		return mapper_path;
	}
	public String getXml_path() {
		return xml_path;
	}
	public String getBean_package() {
		return bean_package;
	}
	public String getMapper_package() {
		return mapper_package;
	}
	public String getDriverName() {
		return driverName;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public String getUrl() {
		return url;
	}
}