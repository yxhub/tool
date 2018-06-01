package com.ymhj.yas.generater.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.ymhj.yas.generater.pojo.Config;

public class BuildUtis {
	
	private Config instance;
	private MyBatisAnnotation myBatisInvoke;
	
	public BuildUtis(Config instance,MyBatisAnnotation myBatisInvoke){
		this.instance = instance;
		this.myBatisInvoke = myBatisInvoke;
	}
	
	/**
     * 构建Dao文件
     * 不带注解类的
     * @throws IOException 
     */
    public void buildDao() throws IOException {
        File folder = new File(instance.getMapper_path());
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File mapperFile = new File(instance.getMapper_path(), "I" + instance.getMapperName() + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + instance.getMapper_package() + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + instance.getBean_package() + "." + instance.getBeanName() + ";");
        bw.newLine();
        bw.write("import org.apache.ibatis.annotations.Param;");
        bw.newLine();
        bw.write("import com.ymhj.yjf.base.IBaseMapper;");
        bw.newLine();
        bw.write("import com.ymhj.yjf.annotation.MyBatisRepository;");
        bw = ProcessUtils.buildClassComment(bw, instance.getMapperName() + "数据库操作接口类");
        bw.newLine();
        bw.newLine();
//        bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
        bw.write("@MyBatisRepository");
        bw.newLine();
        //public interface MemberMapper<Member> extends BaseMapper<Member>{
//        bw.write("public interface " + instance.getMapperName() + "{");
        bw.write("public interface I" + instance.getMapperName()+"<"+instance.getBeanName()+"> extends IBaseMapper<"+instance.getBeanName()+"> " + "{");
        bw.newLine();
        // ----------定义Mapper中的方法Begin----------
        /*      selectById          */
//        bw = ProcessUtils.buildMethodComment(bw, "查询（根据主键ID查询）");
//        bw.newLine();
//        bw.write(myBatisInvoke.selectById());
//        bw.newLine();
//        bw.write(myBatisInvoke.options());
//        bw.newLine();
//        bw.write("\t" + instance.getBeanName() + "  selectById (@Param(\"id\") Long id );");
//        bw.newLine();
//        
//        /*      deleteById          */
//        bw = ProcessUtils.buildMethodComment(bw, "删除（根据主键ID删除）");
//        bw.newLine();
//        bw.write("\t" + "int deleteById (@Param(\"id\") Long id );");
//        bw.newLine();
//        /*                */
//        bw = ProcessUtils.buildMethodComment(bw, "添加");
//        bw.newLine();
//        bw.write("\t" + "int insert( " + instance.getBeanName() + " record );");
//        bw.newLine();
//        /*                */
//        
//        bw = ProcessUtils.buildMethodComment(bw, "添加 （匹配有值的字段）");
//        bw.newLine();
//        bw.write("\t" + "int insertSelective( " + instance.getBeanName() + " record );");
//        bw.newLine();
//        /*                */
//        
//        bw = ProcessUtils.buildMethodComment(bw, "修改 （匹配有值的字段）");
//        bw.newLine();
//        bw.write("\t" + "int updateByIdSelective( " + instance.getBeanName() + " record );");
//        bw.newLine();
//        /*                */
//        
//        bw = ProcessUtils.buildMethodComment(bw, "修改（根据主键ID修改）");
//        bw.newLine();
//        bw.write("\t" + "int updateById ( " + instance.getBeanName() + " record );");
//        bw.newLine();
 
        // ----------定义Mapper中的方法End----------
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }
    
    public void buildDaoAnnotation() throws IOException{
    	File folder = new File(instance.getMapper_path());
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File mapperFile = new File(instance.getMapper_path(), instance.getMapperName() + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + instance.getMapper_package() + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + instance.getBean_package() + "." + instance.getBeanName() + ";");
        bw.newLine();
        bw.write("import org.apache.ibatis.annotations.Param;");
        bw.newLine();
        bw.write("import com.kyj.kjf.common.annotation.MyBatisRepository;");
        bw = ProcessUtils.buildClassComment(bw, instance.getMapperName() + "数据库操作接口类");
        bw.newLine();
        bw.newLine();
        //      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
        bw.write("@MyBatisRepository");
        bw.newLine();
        //public interface MemberMapper<Member> extends BaseMapper<Member>{
        bw.write("public interface " + instance.getMapperName() + "{");
//        bw.write("public interface " + instance.getMapperName()+"<"+instance.getBeanName()+"> extends BaseMapper<"+instance.getBeanName()+"> " + "{");
        bw.newLine();
        bw.newLine();
        // ----------定义Mapper中的方法Begin----------
        bw = ProcessUtils.buildMethodComment(bw, "查询（根据主键ID查询）");
        bw.newLine();
//        bw.write("\t" + instance.getBeanName() + "  selectById (@Param(\"id\") Long id );");
//        bw.newLine();
//        bw = buildMethodComment(bw, "删除（根据主键ID删除）");
//        bw.newLine();
//        bw.write("\t" + "int deleteById (@Param(\"id\") Long id );");
//        bw.newLine();
//        bw = buildMethodComment(bw, "添加");
//        bw.newLine();
//        bw.write("\t" + "int insert( " + instance.getBeanName() + " record );");
//        bw.newLine();
//        bw = buildMethodComment(bw, "添加 （匹配有值的字段）");
//        bw.newLine();
//        bw.write("\t" + "int insertSelective( " + instance.getBeanName() + " record );");
//        bw.newLine();
//        bw = buildMethodComment(bw, "修改 （匹配有值的字段）");
//        bw.newLine();
//        bw.write("\t" + "int updateByIdSelective( " + instance.getBeanName() + " record );");
//        bw.newLine();
//        bw = buildMethodComment(bw, "修改（根据主键ID修改）");
//        bw.newLine();
//        bw.write("\t" + "int updateById ( " + instance.getBeanName() + " record );");
//        bw.newLine();
 
        // ----------定义Mapper中的方法End----------
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }
    

    
}
