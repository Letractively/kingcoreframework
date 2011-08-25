/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.core.service;

import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * <p>���ࣺ��Ŀ�е� HandlerLocator ���󣬵���spring bean����������������ࡣ</p>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ServiceLocator {
	
	private static ApplicationContext applicationContext = null;

//	private static ApplicationContext applicationContext =
//		new ClassPathXmlApplicationContext(
//			new String[] { "classpath*:/conf/spring-config.xml",
//					   "classpath*:/conf/zeven-framework-*.xml" });

	public ServiceLocator() {
	}

	public static synchronized Object getBean(String beanName) {
		return (Object) applicationContext.getBean(beanName);
	}

	public static synchronized Object getService(String beanName) {
		return (Object) applicationContext.getBean(beanName);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * ���� servletContext ����ʼ applicationContext��������ֱ������application��
	 *    �Ǽ��������õ� servletContext ��ȥ�ģ�ԭ����������������������Servlet��ʼ�����б�������
	 *    
	 *    ?? ʹ��Liston/Servlet����Spring����Context�Ĳ����ǣ�WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
	 *    ?? ʹ��ContextLoaderPlugIn����Spring����Context�Ĳ����ǣ�ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX+ModuleConfig.getPrefix()��������鿴Դ���룩
	 * @param servletContext
	 */
	public static void initApplicationContext(ServletContext servletContext) {
		//WebApplicationContext applContext = DelegatingActionUtils.findRequiredWebApplicationContext( servletContext,  null );	// ��δ���Ҳ����
		//ApplicationContext applContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE); // ��δ���Ҳ����
		WebApplicationContext applContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);	// ��δ���Ҳ����
		applicationContext = applContext;
	}
	
}
