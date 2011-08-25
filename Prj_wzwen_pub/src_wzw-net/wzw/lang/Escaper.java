/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.lang;

import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * <p> �����ַ�ת�崦������ 
 * 
 * 	�ο��б�
 * 		org.apache.commons.lang.* 
 * 
 * </p>
 * @author Zeven on 2007-6-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class Escaper {

	/**
	 * Zeven: Java �ַ����Ϊ xml �����ݡ�
	 * 	����xmlֵ�Ĵ�������һ�������� <text><![CDATA[��Ҫ���������]]></text>
	 * 
	 * org.apache.commons.lang.StringEscapeUtils.escapeXml(arg0);
	 *	 Escapes the characters in a String using XML entities.
	 * @param in
	 * @return
	 */
	public static String escape2Xml(String in) {
		return toXmlString( in ,false );
	}
	
//	/**
//	 * @deprecated replaced by escapeXml.
//	 * @param in
//	 * @return
//	 */
//	public String toXmlValue(String in) {
//		return toXmlString( in ,false );
//	}
	
	/**
	 *  same to escapeJavaScript( escapeXml(in) ).
	 * Java �ַ����Ϊ xml �����ݣ�����ΪScript��ֵ��ʹ���������������������� JScript,Java �� ��
	 * @param in
	 * @return
	 */
	public static String escape2XmlForJavaScript(String in) {
		return toXmlString( in, true);
	}
	
	/**
	 * Zeven: ��װһ���ֱ��ʹ�õ�����Ҫ�ã��Ƚ���Ӧ�䶯������䶯ֻ��Ҫ�ı�����࣬
	 * 			����Ҫ�ı���������ߴ���ʹ�õĵط��� 
	 * ��һ����ַ���תΪ HTML�ַ������롣
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	 */
	private static String toXmlString(String in, boolean asParam) {
		
		if( asParam ){

//			return StringEscapeUtils.escapeXml( 
//						StringEscapeUtils.escapeJavaScript(in) ) ;
			return StringEscapeUtils.escapeXml(escape2JavaScript(in) ) ;
		}else{
			return StringEscapeUtils.escapeXml(in) ;
			
		}
		
//		if(in==null) {
//			return null;
//		}
//	    StringBuffer out = new StringBuffer();
//	    for (int i = 0; in != null && i < in.length(); i++) {
//	        char c = in.charAt(i);
//	        if (c == '\'') {
//	        	//System.out.println("is '");
//				if(asParam){
//					out.append("\\&#39;");
//				}else{
//					out.append("&#39;");
//				}
//	        }
//	        else if (c == '\"') {
//				if(asParam){
//					out.append("\\&#34;");
//				}else{
//					out.append("&#34;");
//				}
//	        }
//	        else if (c == '<') {
//	            out.append("&lt;");
//	        }
//	        else if (c == '>') {
//	            out.append("&gt;");
//	        }
//	        else if (c == '&') {
//	            out.append("&amp;");
//	        }
//	        else if (c == '\\') {
//				if(asParam){
//					// if just as a html code 
//					out.append("\\\\");
//				}else {
//		            out.append( c );
//				}
//	        }
//	        else {
//	            out.append(c);
//	        }
//	    }
//	    return out.toString();
	
	}
	

	/**
	 * <p>Zeven : escape java string to javaScript string.
	 * 		> ֻ��Ҫ���� ' "" ���������ţ�������js��ͻ��
	 * 		> apache.common ��escapeJavaScript����ת�����ݹ��ࣻ
	 * 
	 * </p>
	 * @param in
	 * @return
	 */
	public static String escape2JavaScript(String in) {

		if(in==null) {
			return null;
		}

		return in.replaceAll("['|\\|\"]", "\\\\$0"); //wzw ������ʽ

//		///return org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(in);
//	    StringBuffer out = new StringBuffer();
//	    for (int i = 0; in != null && i < in.length(); i++) {
//	        char c = in.charAt(i);
//	        if (c == '\'') {
//	        	//System.out.println("is '");
//	            //out.append("\\&#39;");
//	            out.append("\\'");
//	        }
//	        else if (c == '\"') {
//	            //out.append("\\&#34;");
//	            out.append("\\\"");
//	        }
////	        else if (c == '<') {
////	            out.append("\\&lt;");
////	        }
////	        else if (c == '>') {
////	            out.append("\\&gt;");
////	        }
////	        else if (c == '&') {
////	            out.append("\\&amp;");
////	        }
//	        else if (c == '\\') {
//	            out.append("\\\\");
//	        }
//	        else {
//	            out.append(c);
//	        }
//	    }
//	    return out.toString();
	}

	
	/**
	 * Zeven : escape java string to sql string.
	 * 		˵����	��Ա�׼Sql �� д����
	 * 		  ����Oracle ���ݿ���ԣ�
	 * 		  ���Ƕ���MySql���ݿ⣬��Ҫת��"'"��"\"�������ã�
	 * @param in
	 * @return
	 */
	public static String escape2Sql(String in) {
		return org.apache.commons.lang.StringEscapeUtils.escapeSql(in);
	}
	
	
	/**
	 *
	 * Zeven: ��һ����ַ���תΪ HTML�ַ������룬��Ϊ��ǩ��ֵ��
	 * @asParam html�����Ƿ����Ϊһ������
	 * org.apache.commons.lang.StringEscapeUtils.escapeHtml(arg0);
	 *
	 */  
	public static String escape2Html(String in) {
		return toHtmlString( in, false);
	}
	
	
	/**
	 * escapeJavaScript( escapeHtml(in) )
	 * ��һ����ַ���תΪ HTML�ַ������룬����Ĵ�������Ϊjs�ı���ֵ��
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	 */
	public static String escape2HtmlForJavaScript(String in) {
		return toHtmlString( in, true);
	}

	/**
	 *
	 * ��һ����ַ���תΪ HTML�ַ������롣
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	 */
	private static String toHtmlString(String in, boolean asParam) {

		if( asParam ){

//			return StringEscapeUtils.escapeHtml( 
//						StringEscapeUtils.escapeJavaScript(in) ) ;
			
			return StringEscapeUtils.escapeHtml( escape2JavaScript(in) ) ;
			
		}else{
			return StringEscapeUtils.escapeHtml(in) ;
			
		}
		
//		if(in==null) {
//			return null;
//		}
//	    StringBuffer out = new StringBuffer();
//	    for (int i = 0; in != null && i < in.length(); i++) {
//	        char c = in.charAt(i);
//	        if (c == '\'') {
//	        	//System.out.println("is '");
//				if(asParam){
//					out.append("\\&#39;");
//				}else{
//					out.append("&#39;");
//				}
//	        }
//	        else if (c == '\"') {
//				if(asParam){
//					out.append("\\&#34;");
//				}else{
//					out.append("&#34;");
//				}
//	        }
//	        else if (c == '<') {
//	            out.append("&lt;");
//	        }
//	        else if (c == '>') {
//	            out.append("&gt;");
//	        }
//	        else if (c == '&') {
//	            out.append("&amp;");
//	        }
//	        else if (c == '\\') {
//				if(asParam){
//					// if just as a html code 
//					out.append("\\\\");
//				}else {
//		            out.append( c );
//				}
//	        }
//	        else {
//	            out.append(c);
//	        }
//	    }
//	    return out.toString();
	
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println( Escaper.escape2Html("\"") );
		System.out.println( Escaper.escape2Html("") );
		
		// java������ʽ��
		String in="fdfdsf'\\'''fd\"'";
		System.out.println( in.replaceAll("['|\\|\"]", "\\\\$0")); //wzw���滻'"\���ַ�
		
		System.out.println("fdff[face02]fdaf".replaceAll("\\[face([0-9]{2})\\]",
			"<img border=\"0\" src=\"/images/face/$1.gif\">"));  //wzw���滻�������

		System.out.println("abchttp://fa.com.cn.cn/ftd/dfa.fdfd fdf".replaceAll("(http|ftp|https)(://)\\w+\\.\\w+([\\.\\w+]*)([/\\w+]*)",
						"<a target='_blank' class='link_blueer12a' href='$0'>$0</a>"));  //wzw���滻testΪurl
		
		System.out.println("\"\'\\fsd");
		
		in = "select * from user where code='ab\\c'";
		System.out.println( Escaper.escape2Sql(in) );
		System.out.println( Escaper.escape2Xml("&") );
	}

}
