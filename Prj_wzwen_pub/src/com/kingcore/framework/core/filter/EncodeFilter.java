/**
 * Copyright (C) 2006 ChangSha WangWin Science & Technology CO,.LTD. All rights reserved.
 * WangWin PROPRIETA RY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kingcore.framework.core.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
/**
 * @author ouy Jul 24, 2007 11:48:03 AM
 * @see Object#equals(java.lang.Object) 
 * @since JDK5 
 */
public class EncodeFilter implements Filter {

	private String encoding;
	private static final Logger log = Logger.getLogger(EncodeFilter.class);
	
	public void init(FilterConfig filterConfig) throws ServletException {

		this.encoding=filterConfig.getInitParameter("encoding");
		if(this.encoding==null || this.encoding.equals("")){		// Zeven����doFilter�����Ƶ�init������ֻ��Ҫ�ж�һ�μ��ɡ�
			this.encoding = "UTF-8";	//
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		request.setCharacterEncoding(this.encoding);

		try{
			filterChain.doFilter(request,response);
			
		}catch (Exception e){
			
			// Zeven on 2009-2-20 ������ʾ��Ϣ���Ǹ����������⣬���������
			String previousURL=((HttpServletRequest)request).getRequestURI()+"?";// +request.getQueryString();
			
			Enumeration er = request.getParameterNames();
			String name=null;
			while(er.hasMoreElements()){
				name=er.nextElement().toString() ;
				previousURL += name + "="+ java.net.URLEncoder.encode(request.getParameter(name), "utf-8") + "&";	//"%26";--��Ը���url������������
			}
			
			log.info(e.getMessage()+"---Referer="+((HttpServletRequest)request).getHeader("Referer")+"---previousURL="+previousURL );	// Ϊ�˲��ڿ���̨�����Ϣ���쳣����ʹ��info���𣬲�ʹ��error����
		}
		
	}

//	/**
//	 * �ж���Щ����Ҫ���������
//	 * @param uri
//	 * @return
//	 */
//	
//	private boolean isNeedCreateHtml(String uri) {
//		return uri.indexOf("shop/shopDeal.jhtml_VV")>-1 
//		    || uri.indexOf("market/marketDeal.jhtml_VV")>-1 ;
//	}

	public void destroy() {
		this.encoding=null;
	}

}

