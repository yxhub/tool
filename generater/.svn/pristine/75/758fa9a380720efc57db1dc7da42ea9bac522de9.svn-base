package com.ymhj.yas.generater.util;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.type.JdbcType;

public class MyBatisAnnotation {

	private String tableName; // 表名
	private List<String> columns; // 列
	@SuppressWarnings("unused")
	private List<String> types; // 参数类型
	
	public MyBatisAnnotation(String tableName,List<String> columns,List<String> types){
		this.tableName = tableName;
		this.columns = columns;
		this.types = types; 
	}
	
	public String options(){
		return "@Options(useCache = true, flushCache = false, timeout = 10000);";
	}
	
	public String results(){
		/*
		 *@Results(value = {  
            @Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.BIGINT),  
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR) })   
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("@Results").append("(").append("value").append("= { ").append("\n");
		sb.append("@Result(id = true, property = \"id\", column = \"id\", javaType = Integer.class, jdbcType = JdbcType.BIGINT),");
		for ( int i = 1 ; i < columns.size() ; i++ ) {
			sb.append("@Result").append("(").append("property =").append("\"").append(ProcessUtils.processField(columns.get(i))).append("\"");
			sb.append(",").append("column = ").append("\"").append(columns.get(i)).append("\"");
		}
		sb.append("})");
		return null;
	}
	
	public String selectById(){
		StringBuilder sb = new StringBuilder();
		
		//@Select("SELECT id,	name,	age, address FROM tbl_member WHERE name = #{name}")
		sb.append("@Select").append("(").append("\"").append("SELECT ");
		String condition = ""; // 组合查询条件
		for ( int i = 1 ; i < columns.size() ; i++ ) {
			condition+=columns.get(i)+",";
		}
		condition = condition.substring(0, condition.length()-1);
		System.out.println("###:"+condition);
		sb.append(condition).append(" FROM ");
		sb.append(tableName);
		sb.append(" WHERE ").append(columns.get(0)).append(" =#{").append(ProcessUtils.processField(columns.get(0))).append("}");
		sb.append("\"").append(")");
		System.out.println("输出SQL："+sb.toString());
		
		return sb.toString();
	}
	
	public String deleteById(){
		return null;
	}
	
	public static void main(String[] args) {
		String d= "name, age, address,";
		d = d.substring(0, d.length()-1);
		System.out.println(d);
	}
}
