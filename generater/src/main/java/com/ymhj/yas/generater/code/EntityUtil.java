package com.ymhj.yas.generater.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ymhj.yas.generater.pojo.Config;
import com.ymhj.yas.generater.util.BuildUtis;
import com.ymhj.yas.generater.util.MyBatisAnnotation;
import com.ymhj.yas.generater.util.ProcessUtils;

/**
 * @ClassName: TC
 * @Description: EntityUtil 执行类 (包含Main函数)
 * @author: wangHaiyang
 * @date 2016年4月12日
 */
public class EntityUtil {

	/**
	 **
	 ** 使用前请将moduleName更改为自己模块的名称即可（一般情况下与数据库名一致），其他无须改动。
	 **
	 ***********************************************************
	 */

	private Config instance = new Config();

	private void init() throws ClassNotFoundException, SQLException {
		// String driverName = PropertiesConfig.get("db.driverName");
		// String url = PropertiesConfig.get("db.url");
		// String user = PropertiesConfig.get("db.user");
		// String password = PropertiesConfig.get("db.pwd");
		Class.forName(instance.getDriverName());
		instance.setConn(DriverManager.getConnection(instance.getUrl(), instance.getUser(), instance.getPassword()));
		System.out.println("初始化数据库完毕...");
	}

	
	public void fileBasePath(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 获取所有的表
	 *
	 * @return
	 * @throws SQLException
	 */
	private List<String> getTables() throws SQLException {
		List<String> tables = new ArrayList<String>();
		PreparedStatement pstate = instance.getConn().prepareStatement("show tables");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString(1);
			// if ( tableName.toLowerCase().startsWith("yy_") ) {
			tables.add(tableName);
			// }
		}
		System.out.println("获取DB所有的表：" + tables);
		return tables;
	}

	private void processTable(String table) {
		StringBuffer sb = new StringBuffer(table.length());
		String tableNew = table.toLowerCase();
		String[] tables = tableNew.split("_");
		for (String item : tables) {
			sb.append(item.substring(0, 1).toUpperCase()).append(item.substring(1));
		}
		instance.setBeanName(sb.toString());
		instance.setMapperName(instance.getBeanName() + "Dao");
	}

	/**
	 * 生成实体类
	 *
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException
	 */
	private void buildEntityBean(List<String> columns, List<String> types, List<String> comments, String tableComment)
			throws IOException {
		fileBasePath(instance.getPath()); // 判断根目录
		fileBasePath(instance.getBean_path()); // 判断目标目录

		File beanFile = new File(instance.getBean_path(), instance.getBeanName() + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
		bw.write("package " + instance.getBean_package() + ";");
		bw.newLine();
		bw.newLine();
		bw.write("import java.io.Serializable;");
		// bw.write("import lombok.Data;");
		// bw.write("import javax.persistence.Entity;");
		bw = ProcessUtils.buildClassComment(bw, tableComment);
		bw.newLine();
		bw.write("@SuppressWarnings(\"serial\")");
		bw.newLine();
		// bw.write("@Entity");
		// bw.write("@Data");
		// bw.newLine();
		bw.write("public class " + instance.getBeanName() + " implements Serializable {");
		bw.newLine();
		bw.newLine();
		int size = columns.size();
		for (int i = 0; i < size; i++) {
			bw.write("\t/**" + comments.get(i) + "**/");
			bw.newLine();
			bw.write("\tprivate " + ProcessUtils.processType(types.get(i)) + " "
//					+ ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(i))) + ";");
					+ ProcessUtils.toUpperCase(columns.get(i)) + ";");
			bw.newLine();
			bw.newLine();
		}
		// 生成get 和 set方法
		String tempField = null;
		String _tempField = null;
		String tempType = null;
		for (int i = 0; i < size; i++) {
			tempType = ProcessUtils.processType(types.get(i));
//			_tempField = ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(i)));
			_tempField = ProcessUtils.toUpperCase(columns.get(i));
			tempField = _tempField.substring(0, 1).toUpperCase() + _tempField.substring(1);
			// bw.write("\tpublic void set" + tempField + "(" + tempType + " _"
			// + _tempField + "){");
			bw.write("\tpublic void set" + tempField + "(" + tempType + " " + _tempField + "){");
			bw.newLine();
			// bw.write("\t\tthis." + _tempField + "=_" + _tempField + ";");
			bw.write("\t\tthis." + _tempField + " = " + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
			bw.newLine();
			bw.write("\tpublic " + tempType + " get" + tempField + "(){");
			bw.newLine();
			bw.write("\t\treturn this." + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
			bw.newLine();
		}
		bw.write("}");
		bw.newLine();
		bw.flush();
		bw.close();
		System.out.println("生成实体 【"+instance.getBeanName() +"】 结束!");
	}

	/**
	 * 构建实体类映射XML文件
	 *
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException
	 */
	private void buildMapperXml(List<String> columns, List<String> types, List<String> comments) throws IOException {
		File folder = new File(instance.getXml_path());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String mapperName = instance.getMapperName();
		mapperName = mapperName.replaceAll("Dao", "Mapper");
		File mapperXmlFile = new File(instance.getXml_path(), mapperName + ".xml");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bw.newLine();
		bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
		bw.newLine();
		bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		bw.newLine();
		bw.write("<mapper namespace=\"" + instance.getMapper_package() + ".I" + instance.getMapperName() + "\">");
		bw.newLine();
		bw.newLine();

		bw.write("\t<!--实体映射-->");
		bw.newLine();
		bw.write("\t<resultMap id=\"" + ProcessUtils.processResultMapId(instance.getBeanName()) + "ResultMap\" type=\""
				+ instance.getBean_package() + "." + instance.getBeanName() + "\">");
		bw.newLine();
		bw.write("\t\t<!--" + comments.get(0) + "-->");
		bw.newLine();
//		bw.write("\t\t<id property=\"" + ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(0)))
		bw.write("\t\t<id property=\"" + ProcessUtils.toUpperCase(columns.get(0))
				+ "\" column=\"" + columns.get(0) + "\" />");
		bw.newLine();
		int size = columns.size();
		for (int i = 1; i < size; i++) {
			bw.write("\t\t<!--" + comments.get(i) + "-->");
			bw.newLine();
//			bw.write("\t\t<result property=\"" + ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(i)))
			bw.write("\t\t<result property=\"" + ProcessUtils.toUpperCase(columns.get(i))
					+ "\" column=\"" + columns.get(i) + "\" />");
			bw.newLine();
		}
		bw.write("\t</resultMap>");

		bw.newLine();
		bw.newLine();
		bw.newLine();

		// 下面开始写SqlMapper中的方法
		// this.outputSqlMapperMethod(bw, columns, types);
		buildSQL(bw, columns, types);

		bw.write("</mapper>");
		bw.flush();
		bw.close();
	}

	private void buildSQL(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
		int size = columns.size();
		// 通用结果列
		bw.write("\t<!-- 通用查询结果列-->");
		bw.newLine();
		bw.write("\t<sql id=\"Base_Column_List\">");
		bw.newLine();

		bw.write("\t\t ID,");
		for (int i = 1; i < size; i++) {
			bw.write(columns.get(i));
			if (i != size - 1) {
				bw.write(",");
			}
		}

		bw.newLine();
		bw.write("\t</sql>");
		bw.newLine();
		bw.newLine();

		// 查询（根据主键ID查询）
		bw.write("\t<!-- 查询（根据主键ID查询） -->");
		bw.newLine();
		bw.write("\t<select id=\"getById\" resultMap=\"" + ProcessUtils.processResultMapId(instance.getBeanName())
				+ "ResultMap\" parameterType=\"java.lang." + ProcessUtils.processType(types.get(0)) + "\">");
		bw.newLine();
		bw.write("\t\t SELECT");
		bw.newLine();
		bw.write("\t\t <include refid=\"Base_Column_List\" />");
		bw.newLine();
		bw.write("\t\t FROM " + instance.getTableName());
		bw.newLine();
		bw.write("\t\t WHERE " + columns.get(0) + " = #{"
//				+ ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(0))) + "}");
				+ ProcessUtils.toUpperCase(columns.get(0)) + "}");
		bw.newLine();
		bw.write("\t</select>");
		bw.newLine();
		bw.newLine();
		// 查询完

		// --------------- Select 多条件查询方法（匹配有值的字段）
		bw.write("\t<!-- 多条件查询 （匹配有值的字段）-->");
		bw.newLine();
		bw.write("\t<select id=\"selectBySelective\" resultMap=\""
				+ ProcessUtils.processResultMapId(instance.getBeanName()) + "ResultMap\" parameterType=\""
				+ instance.getBean_package() + "." + instance.getBeanName() + "\">");
		bw.newLine();
		bw.write("\t\t SELECT ");
		bw.newLine();
		bw.write("\t\t <include refid=\"Base_Column_List\" />");
		bw.newLine();
		bw.write("\t\t FROM " + instance.getTableName());
		bw.newLine();
		bw.write("\t\t WHERE 1=1 ");
		bw.newLine();
		String pojoField = null;
		for (int i = 0; i < size; i++) {
//			pojoField = ProcessUtils.processField(columns.get(i));
//			pojoField = ProcessUtils.toLowerCase(pojoField);
			pojoField = ProcessUtils.toUpperCase(columns.get(i));
			bw.write("\t\t\t<if test=\"" + pojoField + " != null\">");
			bw.newLine();
			bw.write("\t\t\t\t AND " + columns.get(i) + " = #{" + pojoField + "}");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}

		bw.newLine();
		bw.write("\t</select>");
		bw.newLine();
		bw.newLine();
		// --------------- 完毕

		// --------------- Select 多条件查询方法（匹配有值的字段）
		bw.write("\t<!-- 多条件查询 （匹配有值的字段）-->");
		bw.newLine();
		bw.write("\t<select id=\"searchPager\" resultMap=\"" + ProcessUtils.processResultMapId(instance.getBeanName())
				+ "ResultMap\" parameterType=\"" + instance.getBean_package() + "." + instance.getBeanName() + "\">");
		bw.newLine();
		bw.write("\t\t SELECT ");
		bw.newLine();
		bw.write("\t\t <include refid=\"Base_Column_List\" />");
		bw.newLine();
		bw.write("\t\t FROM " + instance.getTableName());
		bw.newLine();
		bw.write("\t\t WHERE 1=1 ");
		bw.newLine();
		String pojoField2 = null;
		for (int i = 0; i < size; i++) {
//			pojoField2 = ProcessUtils.processField(columns.get(i));
//			pojoField2 = ProcessUtils.toLowerCase(pojoField2);
			pojoField2 = ProcessUtils.toUpperCase(columns.get(i));
			bw.write("\t\t\t<if test=\"" + pojoField2 + " != null\">");
			bw.newLine();
			bw.write("\t\t\t\t AND " + columns.get(i) + " = #{" + pojoField2 + "}");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}

		bw.newLine();
		bw.write("\t\t order by  modify_time  desc ");
		bw.write("\t</select>");
		bw.newLine();
		bw.newLine();
		// --------------- 完毕

		// 删除（根据主键ID删除）
		bw.write("\t<!--删除：根据主键ID删除-->");
		bw.newLine();
		bw.write("\t<delete id=\"deleteById\" parameterType=\"java.lang." + ProcessUtils.processType(types.get(0))
				+ "\">");
		bw.newLine();
		bw.write("\t\t DELETE FROM " + instance.getTableName());
		bw.newLine();
		bw.write("\t\t WHERE " + columns.get(0) + " = #{"
//				+ ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(0))) + "}");
				+ ProcessUtils.toUpperCase(columns.get(0)) + "}");
		bw.newLine();
		bw.write("\t</delete>");
		bw.newLine();
		bw.newLine();
		// 删除完

		// 添加insert方法
		bw.write("\t<!-- 添加 -->");
		bw.newLine();
		// processResultMapId(instance.getBeanName())
		// instance.getBean_package() + "." + instance.getBeanName()
		bw.write("\t<insert id=\"save\" parameterType=\"" + instance.getBean_package() + "." + instance.getBeanName()
				+ "\">");
		bw.newLine();
		bw.write("\t\t INSERT INTO " + instance.getTableName());
		bw.newLine();
		bw.write(" \t\t(");
		for (int i = 0; i < size; i++) {
			bw.write(columns.get(i));
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.newLine();
		bw.write("\t\t VALUES ");
		bw.newLine();
		bw.write(" \t\t(");
		for (int i = 0; i < size; i++) {
//			bw.write("#{" + ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(i))) + "}");
			bw.write("#{" + ProcessUtils.toUpperCase(columns.get(i)) + "}");
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.newLine();
		bw.write("\t</insert>");
		bw.newLine();
		bw.newLine();
		// 添加insert完

		// --------------- insert方法（匹配有值的字段）
		bw.write("\t<!-- 添加 （匹配有值的字段）-->");
		bw.newLine();
		bw.write("\t<insert id=\"saveSelective\" parameterType=\"" + instance.getBean_package() + "."
				+ instance.getBeanName() + "\">");
		bw.newLine();
		bw.write("\t\t INSERT INTO " + instance.getTableName());
		bw.newLine();
		bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		bw.newLine();

		String tempField = null;
		for (int i = 0; i < size; i++) {
//			tempField = ProcessUtils.processField(columns.get(i));
//			tempField = ProcessUtils.toLowerCase(tempField);
			tempField = ProcessUtils.toUpperCase(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
			bw.newLine();
			bw.write("\t\t\t\t " + columns.get(i) + ",");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}

		bw.newLine();
		bw.write("\t\t </trim>");
		bw.newLine();

		bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
		bw.newLine();

		tempField = null;
		for (int i = 0; i < size; i++) {
//			tempField = ProcessUtils.processField(columns.get(i));
//			tempField = ProcessUtils.toLowerCase(tempField);
			tempField = ProcessUtils.toUpperCase(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
			bw.newLine();
			bw.write("\t\t\t\t #{" + tempField + "},");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}

		bw.write("\t\t </trim>");
		bw.newLine();
		bw.write("\t</insert>");
		bw.newLine();
		bw.newLine();
		// --------------- 完毕

		// 修改update方法
		bw.write("\t<!-- 修 改-->");
		bw.newLine();
		bw.write("\t<update id=\"updateByIdSelective\" parameterType=\"" + instance.getBean_package() + "."
				+ instance.getBeanName() + "\">");
		bw.newLine();
		bw.write("\t\t UPDATE " + instance.getTableName());
		bw.newLine();
		bw.write(" \t\t <set> ");
		bw.newLine();

		tempField = null;
		for (int i = 1; i < size; i++) {
//			tempField = ProcessUtils.processField(columns.get(i));
//			tempField = ProcessUtils.toLowerCase(tempField);
			tempField = ProcessUtils.toUpperCase(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
			bw.newLine();
			bw.write("\t\t\t\t " + columns.get(i) + " = #{" + tempField + "},");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}

		bw.newLine();
		bw.write(" \t\t </set>");
		bw.newLine();
		bw.write("\t\t WHERE " + columns.get(0) + " = #{"
//				+ ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(0))) + "}");
				+ ProcessUtils.toUpperCase(columns.get(0)) + "}");
		bw.newLine();
		bw.write("\t</update>");
		bw.newLine();
		bw.newLine();
		// update方法完毕

		// ----- 修改（匹配有值的字段）
		bw.write("\t<!-- 修 改-->");
		bw.newLine();
		bw.write("\t<update id=\"updateById\" parameterType=\"" + instance.getBean_package() + "."
				+ instance.getBeanName() + "\">");
		bw.newLine();
		bw.write("\t\t UPDATE " + instance.getTableName());
		bw.newLine();
		bw.write("\t\t SET ");

		bw.newLine();
		tempField = null;
		for (int i = 1; i < size; i++) {
//			tempField = ProcessUtils.processField(columns.get(i));
//			tempField = ProcessUtils.toLowerCase(tempField);
			tempField = ProcessUtils.toUpperCase(columns.get(i));
			bw.write("\t\t\t " + columns.get(i) + " = #{" + tempField + "}");
			if (i != size - 1) {
				bw.write(",");
			}
			bw.newLine();
		}

		bw.write("\t\t WHERE " + columns.get(0) + " = #{"
//				+ ProcessUtils.toLowerCase(ProcessUtils.processField(columns.get(0))) + "}");
				+ ProcessUtils.toUpperCase(columns.get(0)) + "}");
		bw.newLine();
		bw.write("\t</update>");
		bw.newLine();
		bw.newLine();
	}

	/**
	 * 获取所有的数据库表注释
	 *
	 * @return
	 * @throws SQLException
	 */
	private Map<String, String> getTableComment() throws SQLException {
		Map<String, String> maps = new HashMap<String, String>();
		PreparedStatement pstate = instance.getConn().prepareStatement("show table status");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString("NAME");
			String comment = results.getString("COMMENT");
			maps.put(tableName, comment);
		}
		return maps;
	}

	public void generate() throws ClassNotFoundException, SQLException, IOException {
		init();
		String prefix = "show full fields from ";
		List<String> columns = null;
		List<String> types = null;
		List<String> comments = null;
		PreparedStatement pstate = null;
		List<String> tables = getTables();
		Map<String, String> tableComments = getTableComment();
		for (String table : tables) {
			columns = new ArrayList<String>();
			types = new ArrayList<String>();
			comments = new ArrayList<String>();
			pstate = instance.getConn().prepareStatement(prefix + table);
			ResultSet results = pstate.executeQuery();
			while (results.next()) {
				columns.add(results.getString("FIELD"));
				types.add(results.getString("TYPE"));
				comments.add(results.getString("COMMENT"));
			}
			instance.setTableName(table);
			processTable(table);
			// this.outputBaseBean();
			String tableComment = tableComments.get(instance.getTableName());
			buildEntityBean(columns, types, comments, tableComment); // 创建实体
			MyBatisAnnotation myBatisAnnotation = new MyBatisAnnotation(instance.getTableName(), columns, types);
			BuildUtis buildUtils = new BuildUtis(instance, myBatisAnnotation);
			buildUtils.buildDao(); // 创建DAO

			buildMapperXml(columns, types, comments); // 创建SQL - MAP
		}
		instance.getConn().close();
	}

	public static void main(String[] args) {
		try {
			new EntityUtil().generate();
			// 自动打开生成文件的目录
			Runtime.getRuntime().exec("cmd /c start explorer D:\\");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
