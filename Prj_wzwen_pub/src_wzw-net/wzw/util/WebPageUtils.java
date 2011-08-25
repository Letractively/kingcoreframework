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

package wzw.util;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import wzw.lang.Escaper;

import com.kingcore.framework.context.ApplicationContext;


/**
 * <p>jsp webҳ��Ĺ����ࡣ</p>
 * 
 * <pre>
 * WebPageUtils.toHTMLValue( a"b'"c'd' )     =a&#34;b&#39;&#34;c&#39;d&#39;
 * WebPageUtils.toHTMLValueForScript( a"b'"c'd' )     =a\&#34;b\&#39;\&#34;c\&#39;d\&#39;
 * WebPageUtils.toScriptValue( a"b'"c'd' )     =a\"b\'\"c\'d\'
 * </pre>
 * 
 * @author Zeven on 2007-6-9
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class WebPageUtils {


	
	/**
	 *
	 * ��һ����ַ���תΪ HTML�ַ������룬��Ϊ��ǩ��ֵ��
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	 */  
	public static String escape2Html(String in) {
		return Escaper.escape2Html( in );
	}
	/**
	 * @deprecated replace by escape2Html(String in).
	 * @param in
	 * @return
	public static String toHTMLValue(String in) {
		return Escaper.escape2Html( in );
	}
	 */

	/**
	 * @deprecated replaced by to escape2Html.
	 * ��һ����ַ���תΪ HTML�ַ������롣
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	public static String toHTMLString(String in) {
		return Escaper.escape2Html( in );
	}
	 */  
	
	/**
	 *
	 * ��һ����ַ���תΪ HTML�ַ������룬����Ĵ�������Ϊjs�ı���ֵ��
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	 */
	public static String escape2HtmlForJavaScript(String in) {
		return Escaper.escape2Html( Escaper.escape2JavaScript(in) );
	}
	
	/**
	 * @deprecated replace by escape2HtmlForJavaScript(String in).
	 * @param in
	 * @return
	public static String toHTMLValueForScript(String in) {
		return Escaper.escape2Html( Escaper.escape2JavaScript(in) );
	}
	 */
	
	/**
	 * @deprecated replaced by escape2HtmlForJavaScript(String in).
	 * ��һ����ַ���תΪ HTML�ַ������룬����Ĵ�������Ϊjs�ı�����
	 * @asParam html�����Ƿ����Ϊһ������
	 *
	public static String toHTMLStringForScript(String in) {
		return Escaper.escape2HtmlForJavaScript( in );
	}
	 */
	
//	
//	/**
//	 *
//	 * ��һ����ַ���תΪ HTML�ַ������롣
//	 * @deprecated 
//	 * @asParam html�����Ƿ����Ϊһ������
//	 *
//	 */
//	public static String toHTMLString(String in, boolean asParam) {
//		return Escaper.toHTMLValue(in , asParam );
//	}

	/**
	 *
	 * ��һ����ַ���תΪ JScript��JavaScript ���ַ�������ֵ��
	 *
	 */  
	public static String escape2JavaScript(String in) {
		return Escaper.escape2JavaScript( in );
	}
	/**
	 * @deprecated replace by escape2JavaScript(String in).
	 * @param in
	 * @return
	public static String toScriptValue(String in) {
		return Escaper.escape2JavaScript( in );
	}
	 */

	/**
	 * @deprecated replace by escape2JavaScript(String in).
	 * Zeven on 2008-05-08 ���޸�Ϊʹ���Ѿ����˵ĸ������ apache common ����
	 * 	���������ʹ���Լ���װ�ģ�����ֱ��ʹ�� common����ģ��Է���Ӧ���������ı䶯��
	 * ��һ����ַ���תΪ JScript��JavaScript ���ַ���������
	 *
	public static String toScriptString(String in) {

		return Escaper.escape2JavaScript( in );
		// return org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(in);
		
//		if(in==null) {
//			return null;
//		}
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
	 */ 


	/**
	 * ���ݲ�ͬ��Servlet���������� request.getServletPath() ͳһ��׼��ֵ��
	 * @param request
	 * @return
	 */
	public static String getServletPath(HttpServletRequest request) {
		return ApplicationContext.getInstance().getServletContainer().getServletPath( request );
	}
	
	/**
	 * @deprecated useless method, it's not different in different Servlet Container.
	 * @param request
	 * @return
	public static String getContextPath(HttpServletRequest request) {
		return ApplicationContext.getInstance().getServletContainer().getContextPath( request );	
	}
	 */

	/**
	 * <p>��ȡ��������˳��request, sessioin, application���ҵ������ء�</p>
	 * @param name
	 * @param request
	 * @param session
	 * @param servletContext
	 * @return
	 */
	public static Object getAttribute(String name, 
						HttpServletRequest request,
						HttpSession session,
						ServletContext servletContext ){
		
		return request.getAttribute(name)!=null?request.getAttribute(name):(session.getAttribute(name)!=null?session.getAttribute(name):(servletContext.getAttribute(name)));
	}

	
	/**
	 * ���ݲ�ͬ��Servlet���������ø����jspҳ���contentType���ڱ�include����ҳ����� contentType��ֵ��
	 * 		�磺text/html;charset=utf-8
	 * 		   text/xml;charset=utf-8
	 *	       text/html;charset=gb2312
	 * @param request
	 * @return
	 */
	public static void setPageContentTypeIndividually(HttpServletResponse response,String contentType) {
		ApplicationContext.getInstance().getServletContainer().setPageContentTypeIndividually(response, contentType );	
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int pad = 45;
		String str =null;
		str = "a\"b'\"c'd'";

		str = "\\a\"b'\"c'd'\\";
		System.out.println(str);
	}

}
