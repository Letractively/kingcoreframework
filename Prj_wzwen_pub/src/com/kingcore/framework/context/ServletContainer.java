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

package com.kingcore.framework.context;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Servlet ��������ͳһ�ӿڡ�</p>
 * @author Zeven on 2007-9-3
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface ServletContainer {
	
	/**
	 * ���ݲ�ͬ��Servlet���������� request.getContextPath() ͳһ��׼��ֵ��
	 * Zeven��ʵ�������������Ӧ�ó����й�ϵ������Tomcat���棬���� path="" �� path="/" 
	 * 		����Ϊ�Ǹ�Ŀ¼������һ�����ؿգ�һ�����ء�/����
	 *    WebLogic��������ã�
	 *      ...
	 *      <context-root>/</context-root>
	 *    </weblogic-web-app>
	 * 	
	 * 	��������Ϊ���� Ϊ��á�
	 *      ��ǰservlet�����汾�»��������������޹أ������������йء�
	 *      �ܽ᣺����ͨ��path���Ե�ֵ��һ�㷵�صĶ����ԡ�/����β��
	 *     
	 * @deprecated ��ǰservlet�����汾�»��������������޹أ������������йء�
	 * @param request
	 * @return
	 */
	public String getContextPath(HttpServletRequest request) ;
	

	public String getServletPath(HttpServletRequest request) ;
	
	
	/**
	 * ���ݲ�ͬ��Servlet�������ڱ�include����ҳ����� contentType��ֵ��
	 * 		�磺text/html;charset=utf-8
	 * 		   text/xml;charset=utf-8
	 *	       text/html;charset=gb2312
	 * @param request
	 * @return
	 */
	public void setPageContentTypeIndividually(HttpServletResponse response,String contentType) ;
	
	/**
	 * @deprecated replaced by setPageContentTypeIndividually.
	 * @param response
	 * @param contentType
	 */
	public void setIncludedPageContentType(HttpServletResponse response,String contentType) ;
	
}
