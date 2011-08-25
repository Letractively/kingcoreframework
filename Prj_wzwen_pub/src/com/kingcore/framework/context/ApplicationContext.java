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

import org.apache.log4j.Logger;

import com.kingcore.framework.jdbc.ResultSetBeanExtractor;




/**
 * <p> ����������ܵ����л������ã��������¼��㣺
 * 		1��DataSource ���ṩ�ߣ�
 * 		2�����ݿ����ϵͳ��
 *    ���ó���ӿڡ�ע�뷽ʽ����������ƽ̨��
 * 	</p>
 * 
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ApplicationContext {

    /**
     * log4j��־����
     */
    protected static Logger log=Logger.getLogger( com.kingcore.framework.context.ApplicationContext.class );

    /**
     * ���������ģʽ�滻ȫ��̬����������spring ����
     */
	private static ApplicationContext instance;
	
	/**
	 * Ĭ��ΪWebLogic �����ṩ����Դ����������Tomcat�������Լ���д��ConnectionPool������ע�뷽ʽ��
	 */
	private DataSourceManager dataSourceManager = null;
	

	private String defaultDataSourceName = null;
	
	/**
	 * ��������ļ��ĸ�Ŀ¼��
	 */
	private String configPath = null;

	/**
	 * Ĭ��ʹ�õ�����Դ���ơ�
	 */
	//private String defaultDataSourceName = null;

	/**
	 * @deprecated databaseManager�������ApplicationContext������������DataSource������
	 * Ĭ��Ϊ Oracle10i �����ṩ����Դ����������MySQL������ע�뷽ʽ��
	 */
	private DatabaseManager databaseManager = null;
	

	/**
	 * Ĭ��ΪWebLogic ��������������Tomcat������
	 */
	private ServletContainer servletContainer = null;
	
	
	public static ApplicationContext getInstance() {
		if (instance == null) {
			instance = new ApplicationContext();
		}
		return instance;
	}
	
	/**
	 * ��������Ҹ���Ĭ��ֵ�� �ᱻspring ���ã��������ι���!!!!!!!!!!!!
	 *
	 */
	private ApplicationContext() {
		//ע��������ʵ�ִ��ӿڣ��������ھ���ʵ�֣���Ҫ��ϵͳһ��Ҫ��ʼ��Щ����
		//����ע�룬���߱�д�������setter��
		//this.databaseManager = new OracleDatabase();
		//WebLogicContainer webLogic = new WebLogicContainer();
		//this.dataSourceManager = webLogic;
		//this.servletContainer = webLogic;

		instance = this ;
	}
	
	/**
	 * getter for dataSourceProvider.
	 * @return
	 */
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	/**
	 * setter for dataSourceProvider.
	 * @return
	 */
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	/**
	 * 
	 *  ���������Ϊ�˼��ݲ��ִ��룬����spring jdbcʵ�֡�
	 * @deprecated databaseManager�������ApplicationContext������������DataSource������
	 * getter.
	 * @return
	 */
	public DatabaseManager getDatabaseManager() {
		return this.databaseManager;
	}
	
	/**
	 * setter. 
	 * @deprecated databaseManager�������ApplicationContext������������DataSource������
	 * @param databaseManager
	 */
	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ServletContainer getServletContainer() {
		return servletContainer;
	}

	public void setServletContainer(ServletContainer servletContainer) {
		this.servletContainer = servletContainer;
	}

//	public String getDefaultDataSourceName() {
//		return defaultDataSourceName;
//	}
//
//	private void setDefaultDataSourceName(String defaultDataSourceName) {
//		this.defaultDataSourceName = defaultDataSourceName;
//	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		if (this.dataSourceManager!=null) {
			this.dataSourceManager.setConfigPath(configPath);
		}
	}
	
	public void initContext(DataSourceManager dataSourceManager,	
							String configPath,
							String defaultDataSourceName){
		//��ʼ DataSourceManager�����á�
		this.setDataSourceManager(dataSourceManager);
		this.setConfigPath(configPath);
		this.defaultDataSourceName = defaultDataSourceName;

		this.dataSourceManager.setConfigPath(configPath);
		this.dataSourceManager.setDefaultDataSourceName(defaultDataSourceName);
	}

	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	private ResultSetBeanExtractor resultSetBeanExtractor = null;
	public ResultSetBeanExtractor getResultSetBeanExtractor() {
		return this.resultSetBeanExtractor;
	}

	public void setResultSetBeanExtractor(
			ResultSetBeanExtractor resultSetBeanExtractor) {
		this.resultSetBeanExtractor = resultSetBeanExtractor;
	} 

}
