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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


import com.kingcore.framework.jdbc.ConnectionManager;

/**
 * <p>Tomcat �����ṩ�����е����ݡ�����DataSource �ȡ�֧�ֶ�����Դ��
 * 		��ǰ���Tomcat5.5�� ֧�������汾��Ҳ�������¼��������汾���������Լ���д��ע�롣</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TomcatContainer implements ServletContainer,DataSourceManager {

	/**
	 * ��־����
	 */
	protected static Logger log=Logger.getLogger(com.kingcore.framework.context.TomcatContainer.class);
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * ����DataSource�����ã�֧�ֶ�����Դ������ÿ�λ�ȡ
	 */
	private Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

    /**
     * ��ȡĬ�ϵ� DataSource.
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource() throws SQLException{
    	return getDataSource(  getDefaultDataSourceName() );
    }

    /**
     * ��ȡ����ΪdataSourceName�� DataSource.
     * @param dataSourceName
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource(String dataSourceName) throws SQLException{

    	// �Ƿ����Ѿ���ȡ��DataSource����
    	DataSource ods = dataSourceMap.get(dataSourceName);
		if(ods!=null){
			return ods;
		}

		DataSource ds = null;
//		1.�������ĸ���JNDI��ȡDataSource
		Context initCtx = null;
		try {
			initCtx = new InitialContext(); //����������ʵ��
		
//			get DataSource by different Servlet Container Type.
			// Tomcat5 ��Ҫ������Ĵ���
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			ds = (DataSource)envCtx.lookup( dataSourceName );
			dataSourceMap.put(dataSourceName, ds);    // ��������
			return ds ;
			
		}catch (NamingException ex) {
			log.fatal("cant get the new InitialContext()") ;
			log.debug("debug", ex);
			/// ex.pri ntStackTrace();
			throw new SQLException( ex.getMessage() );

		}catch (Exception ex) {
			log.fatal("cant get the new InitialContext()") ;
			log.debug("debug", ex);
			/// ex.pri ntStackTrace();
			throw new SQLException( ex.getMessage() );
		}

    }

	/**
	 * ��ȡϵͳĬ�ϵ�����Դ�����ݿ����ӡ�
	 * @return
	 */
	public Connection getConnection() throws SQLException {
		return getConnection( getDefaultDataSourceName() );
	}

	/**
	 * ��ȡָ�����Ƶ�����Դ�����ݿ����ӡ�
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String dataSourceName)
				throws SQLException {
		
		Connection conn = null ;
		
		//	1.�������ĸ���JNDI��ȡDataSource
		DataSource ds = getDataSource( dataSourceName );
					
		if (ds != null){
			conn = ds.getConnection();
			if( conn==null ){
				log.debug("ʹ��jndi��ʽ��ȡ����Ϊ[" +dataSourceName+ "]��DataSource�е�ConnectionΪnull��");
			}
			conn.setAutoCommit(false);		// Weblogic��DataSource��JNDIĬ��Ϊ true.
			//log.debug("obtain a connection:   "+conn);
			return conn;
			
		}else{
			log.debug("get DateSource from JNDI failed!");
			log.debug("ʹ��jndi��ʽ��ȡ����Ϊ[" +dataSourceName+ "]��DataSourceʧ��");
		}
			
		
		//	2.���jndi��û�У��Ӿ�̬���л�ȡ
		if(conn==null){
			try{
				conn = ConnectionManager.getInstance().getConnection() ;
			}catch(SQLException ex){
				log.debug("debug", ex);
				/// ex.pri ntStackTrace();
				throw new SQLException("DBUtils.getConnection():"+ex.getMessage());

			}
//			java.sql.Connection conn1 = ConnectionManager.getConnection() ;
//			java.sql.Connection conn2 = ConnectionManager.getConnection() ;
//			java.sql.Connection conn3 = ConnectionManager.getConnection() ;
//			java.sql.Connection conn4 = ConnectionManager.getConnection() ;
			
//			conn1.close() ;
//			conn2.close() ;
//			conn3.close() ;
//			conn4.close() ;
		}
		return conn ; 
		
	}

	/**
	 * Tomcat �����µ� request.getContextPath() ���صı�׼������
	 */
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}

	/**
	 * Tomcat ������included ����ҳ��Ҫָ�� contentType��
	 */
	public void setIncludedPageContentType(HttpServletResponse response,String contentType) {
		response.setContentType( contentType );
	}
	
    /**
     * ȱʡ��DataSource �����ơ�
     */
	protected String defaultDataSourceName = "jndi/jdbc";
	private String configPath;
 

	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	public void setDefaultDataSourceName(String dataSourceName) {
		this.defaultDataSourceName = dataSourceName;
		
	}

	public String getServletPath(HttpServletRequest request) {
		return request.getServletPath();
	}
	
	/**
	 * Tomcat5.5Ҫ������ÿ��ҳ���ContextType����������ôҳ��ᱨ��
	 */
	public void setPageContentTypeIndividually(HttpServletResponse response, String contentType) {
		response.setContentType( contentType );
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		
	}

}
