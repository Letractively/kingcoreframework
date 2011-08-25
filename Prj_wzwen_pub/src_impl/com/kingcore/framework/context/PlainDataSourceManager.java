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

package com.kingcore.framework.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.kingcore.framework.jdbc.DataSourceProxy;
import com.kingcore.framework.jdbc.PlainConnection;


/**
 * <p>���ڶ�����ݿ�����DataSource�Ĺ�����󣬹������е�DataSource��������Ĭ�ϵ�DataSource���֡�
 * 			����ע�뷽ʽ(���ʹ��PlainDataSourceProvider ������������DataSource bean. �����ĺô��ǲ������÷�������jndi)��
 * 
 *    �ṩDataSource�Ķ�����Ҫʵ�ֵĽӿڡ�
 *    �ص㣺> *�ṩ������Դ(DataSouce)��
 *    	   > *�ṩĬ�ϵ�DataSource������Ҫÿ��Dao����ע��/����DataSource������DataSourceProvider���ɣ�
 *    	   > ������Դ�����ǲ�ͬ���͵ģ�
 *    		���������Դ�еĲ��÷�������jndi�ṩ���еĲ���Spring�����bean�ṩ�����߶�����ConnectionManager�ṩ��</p>
 *    
 * 		<!-- Construct DataSourceProvider -->
 * 		<bean id="dataSourceProvider" class="com.kingcore.framework.context.PlainDataSourceProvider">
 * 			<property name="defaultDataSourceName">
 * 				<value>jndi/jdbc</value>
 * 			</property>
 * 			<property name="dataSources">
 * 				<ref local="dataSourceMap" />
 * 			</property>
 * 		</bean>
 * 
 * 		<!-- Construct Map -->
 *		<bean id="dataSourceMap" class="java.util.HashMap">
 * 			<constructor-arg>
 *				<map>
 *					<entry key="jndi/jdbc" value-ref="dataSource-jndi/jdbc" />
 *					<entry key="jdbc2" value-ref="dataSource-jdbc2" />
 * 				</map>
 * 			</constructor-arg>
 *		</bean>
 *
 * 		<!-- Construct DataSource 1 -->
 *		<bean id="dataSource-jndi/jdbc" class="org.apache.commons.dbcp.BasicDataSource">  
 *			<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>   
 *			<property name="url" value="jdbc:mysql://127.0.0.1:3306/wzwdb"></property>   
 *			<property name="username" value="root"></property>   
 *			<property name="password" value="root"></property> 
 *		</bean> 
 * 		<!-- Construct DataSource 2 -->
 *		<bean id="dataSource-jdbc2" class="org.apache.commons.dbcp.BasicDataSource">  
 *			<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>   
 *			<property name="url" value="jdbc:mysql://127.0.0.1:3306/wzwdb2"></property>   
 *			<property name="username" value="root"></property>   
 *			<property name="password" value="root"></property> 
 *		</bean> 
 * 
 * 	�ܽ��������÷�ʽ��
 * 		1��ֱ��ʹ��web������jndi��ʽ����spring��ֻ��Ҫ����һ�� dataSourceProvider ���ɣ�����ÿ��dataSource������ server.xml, web.xml �����ã�
 * 						--- ��Ҫ����ʹ��jndi��ȡdataSource�������
 * 		2��ʹ�� DataSourceManager ��ʽ����spring��ֻ��Ҫ����һ�� dataSourceProvider ���ɣ�����ÿ��dataSource������ db.property �����ã�
 * 						--- ��Ҫ����ʹ�� com.kingcore.framework.jdbc.DataSourceManager �����ԣ��������ü򵥣�
 * 		3��ʹ�� PlainDataSourceProvider ��ʽ����spring��ֻ��Ҫ����һ�� dataSourceProvider��һ��dataSourceMap��
 * 						--- ��������ÿ��dataSourceҲ����spring�����ã��������ϸ������е�dataSource bean���� 
 * 							//Zeven:�ƺ��е���һ�٣�Ϊʲô��ֱ��ʹ��spring��getBean��Ϊʲô��һ��bean֮�ϵ�dataSource bean���������ô��
 * 							//ǿ�ҽ���ʹ�� PlainDataSourceProvider ��ʽ���á�
 * 
 * </p>
 * 
 * @author Zeven on 2007-6-9
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class PlainDataSourceManager implements DataSourceManager {

	private String defaultDataSourceName = null;
	private Map<String, DataSourceProxy> dataSources= null;
	private String configPath = null;

	
	/* (non-Javadoc)
	 *   ����ʵ�ʷ��ص���һ������dataSource���õ�Connection
	 *   ÿ��DataSource��Ӧһ��DatabaseManager��
	 * @see com.kingcore.framework.context.DataSourceProvider#getConnection(java.lang.String)
	 */
	public Connection getConnection(String dataSourceName) throws SQLException {
		DataSourceProxy plainDataSource = this.getPlainDataSource(dataSourceName);
		return new PlainConnection(plainDataSource.getDataSource().getConnection(),
				plainDataSource.getDatabaseManager());
		//return this.getDataSource(dataSourceName).getConnection();
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDataSource(java.lang.String)
	 */
	protected DataSourceProxy getPlainDataSource(String dataSourceName) throws SQLException {
		return this.getDataSources().get(dataSourceName);  //(DataSource)
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return this.getConnection(this.defaultDataSourceName);
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDataSource()
	 */
	protected DataSource getDataSource() throws SQLException {
		return this.getDataSource( this.defaultDataSourceName );
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDataSource(java.lang.String)
	 */
	protected DataSource getDataSource(String dataSourceName) throws SQLException {
		return this.getDataSources().get(dataSourceName).getDataSource();  //(DataSource)
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDefaultDataSourceName()
	 */
	public String getDefaultDataSourceName() {
		return this.defaultDataSourceName;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#setDefaultDataSourceName(java.lang.String)
	 */
	public void setDefaultDataSourceName(String dataSourceName) {
		this.defaultDataSourceName = dataSourceName;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public Map<String, DataSourceProxy> getDataSources() {
		return dataSources;
	}

	public void setDataSources(Map<String, DataSourceProxy> dataSources) {
		this.dataSources = dataSources;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		
	}

}
