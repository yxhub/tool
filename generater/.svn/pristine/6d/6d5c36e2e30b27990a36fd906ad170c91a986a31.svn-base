package com.ymhj.yas.generater.util;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.ymhj.yas.generater.pojo.TypeModel;

public class ProcessUtils {

	public static  String processType( String type ) {
        if ( type.indexOf(TypeModel.getTypeChar()) > -1 ) {
            return "String";
        } else if ( type.indexOf(TypeModel.getTypeBigint()) > -1 ) {
            return "Long";
        } else if ( type.indexOf(TypeModel.getTypeInt()) > -1 ) {
            return "Integer";
        } else if ( type.indexOf(TypeModel.getTypeDate()) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(TypeModel.getTypeText()) > -1 ) {
            return "String";
        } else if ( type.indexOf(TypeModel.getTypeTimestamp()) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(TypeModel.getTypeBit()) > -1 ) {
            return "Boolean";
        } else if ( type.indexOf(TypeModel.getTypeDecimal()) > -1 ) {
            return "java.math.BigDecimal";
        } else if ( type.indexOf(TypeModel.getTypeBlob()) > -1 ) {
            return "byte[]";
        }
        return null;
    }
 
 
    public static  String processField( String field ) {
        StringBuffer sb = new StringBuffer(field.length());
//        System.out.println("field!!!!!!!!!!!!:"+field);
        if(field.indexOf("_")>-1){
        	 String[] fields = field.split("_");
             String temp = null;
             sb.append(fields[0]);
             for ( int i = 1 ; i < fields.length ; i++ ) {
                 temp = fields[i].trim();
                 sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
             }
             return sb.toString();
        }else{
        	return field;
        }
    }
 
 
    /**
     *  将实体类名首字母改为小写
     *
     * @param beanName
     * @return 
     */
    public static  String processResultMapId( String beanName ) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }
    
    /**
     *  构建类上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException 
     */
    public static  BufferedWriter buildClassComment( BufferedWriter bw, String text ) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" **/");
        return bw;
    }
 
 
    /**
     *  构建方法上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException 
     */
    public static  BufferedWriter buildMethodComment( BufferedWriter bw, String text ) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t **/");
        return bw;
    }
    
    public static String toLowerCase(String str){
    	if("ID".equals(str) || "id".equals(str)){
    		return "id";
    	}
//    	if(str.length()<4){
//    		return str.toLowerCase();
//    	}
    	String res = null;
        char[] chars=new char[1];  
        chars[0]=str.charAt(0);  
        String temp=new String(chars); 
//        if(chars[0]>='A'  &&  chars[0]<='Z'){  
//            res = str.replaceFirst(temp,temp.toLowerCase());
//        }else{
//        	
//        }
        res = str.replaceFirst(temp,temp.toLowerCase());
        return res;
	}
    
	/**
	 * 以分隔符分组，除了第一个单词外，每个单词首字母大写（例：login_name to loginName）
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCase(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		str = str.toLowerCase();
		StringBuffer ret = new StringBuffer();
		String[] strs = StringUtils.split(str, "_");
		for (int i = 0; i < strs.length; i++) {
			String s = strs[i];
			if(i == 0){
				ret.append(s);
			} else{
				String res = null;
				char[] chars = new char[1];
				chars[0] = s.charAt(0);
				String temp = new String(chars);
				res = s.replaceFirst(temp, temp.toUpperCase());
				ret.append(res);
			}
		}
		return ret.toString();
	}
    
    
    public static void main(String[] args) {
		String key = "Thekey";
		toLowerCase(key);
	}
}
