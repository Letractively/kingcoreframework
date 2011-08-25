/**
 * Copyright (C) 2002-2011 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import wzw.lang.ObjectUtil;
import wzw.util.DbUtils;

import com.kingcore.framework.context.DatabaseManager;

/**
 * <p>java���ļ���˵��...</p>
 * @author Zeven on Jul 31, 2011
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ConnectionPool implements DataSource { //���ӳ�����
	
	private int checkedOut;
	private Vector<Connection> freeConnections;
	private int minConn;
	private int maxConn;
	private String driver;
	private String name;
	private String user;
	private String password;
	private String url;
	private String databaseManagerName;
	
	private final static Logger log = Logger.getLogger(ConnectionPool.class);
	public synchronized void freeConnection(Connection conn) {
		freeConnections.addElement(conn);
		checkedOut--;
		log.info( (new StringBuffer("�ͷź����ӳ�")).append(name)
						.append("���õ�")
						.append(checkedOut)
						.append("������") );
		notifyAll();
	}

	/**
	 * by Zeven on 2009-03-05
	 * @param conn
	 */
	public boolean isConnectionClosed(Connection conn) {
		return freeConnections.contains(conn);
		
	}
	
	/**
	 * get a connection from a Connection Collection. 
	 * @return
	 */
	public synchronized Connection getConnection() throws SQLException{
		Connection conn = null;
		if (freeConnections.size() > 0) {
			conn = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			try {
				if (conn == null || conn.isClosed()) {
					log.info( (new StringBuffer("�����ӳ�")).append(name)
								.append("ɾ��һ����Ч����") );
					conn = getConnection();
					return conn ;
				}
			} catch (SQLException e) {
				log.info( (new StringBuffer("�����ӳ�")).append(name)
								.append("ɾ��һ����Ч����") );
				conn = getConnection();
			}
		} else if (maxConn == 0 || checkedOut < maxConn) {
			conn = newConnection();
		} else {
			log.info( (new StringBuffer("���ӳ�")).append(name)
							.append("���������Ѿ���ʹ����") );
		}

		if (conn != null) {
			checkedOut++;
			log.info( (new StringBuffer("����ʹ��")).append(name)
							.append("���ӳ��е�")
							.append(checkedOut)
							.append("������")
							.append(showTrace(10)) );
		}
		// ÿ������Ϊ���Զ��ύ��ϵͳ���������ֶ��������������ʹ��ǰ��ʹ��ʱ����Ϊ���Զ���ʽ��
		// ��������ҲӦ�ÿ��������ݿ������ã�
		conn.setAutoCommit(false);
		return conn;
	}

    String showTrace(int maxdepth)
    {
      String stack="����·����\n";
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 1; i < Math.min(maxdepth + 1, trace.length); i++)
      {
        stack += "\t[" + trace[i].hashCode() + "]" + trace[i] + "\n";
      }
      return stack;
    }
    
	public synchronized Connection getConnection(long timeout) throws SQLException {
		long startTime = (new Date()).getTime();
		Connection conn;
		while ( (conn = getConnection()) == null) {
			try {
				wait(timeout);
			}
			catch (InterruptedException interruptedexception) {}
			if ( (new Date()).getTime() - startTime >= timeout) {
				return null;
			}
		}
		return conn;
	}

	/**
	 * release all Connections from Collection.
	 *
	 */
	public synchronized void release() {
		for (Enumeration<Connection> allConnections = freeConnections
				.elements(); allConnections.hasMoreElements();) {
			Connection conn = (Connection) allConnections.nextElement();
			try {
				conn.close();
				log.info((new StringBuffer("�ر����ӳ�")).append(name).append(
						"�е�һ������"));
			} catch (SQLException e) {
				log.info((new StringBuffer("�޷��ر����ӳ�")).append(name).append(
						"�е�����").append("\n").append(e.getMessage()));
			}
		}

		freeConnections.removeAllElements();
//		DriverManager.deregisterDriver(this.driver);
//		log.info( (new StringBuffer("����JDBC�������� ")).
//				append(driver.getClass().getName()).
//				append("��ע��") );
	}
	
	/**
	 * create a new Connection object.
	 * 
	 * @return
	 */
	private Connection newConnection() throws SQLException{
		Connection conn = null;
		try {
			if (user == null) {
				conn = DriverManager.getConnection(url);
			} else {
				conn = DriverManager.getConnection(url, user, password);
			}
			log.info( (new StringBuffer("���ӳ�")).append(name)
								.append("����һ���µ�����") );
		} catch (SQLException e) {
			log.info( (new StringBuffer("���ӳ�")).append(name)
					.append("�޷���������URL������: ")
					.append(url)
					.append("\n")
					.append(e.getMessage())
					.toString() );
			//return null;
			throw new SQLException( (new StringBuffer("���ӳ�")).append(name)
								.append("�޷���������URL������: ")
								.append(url)
								.append("\n")
								.append(e.getMessage())
								.toString() );
		}
		//return conn;  URL
		//create databaseManager object by driver(url)
		String databaseManagerName = null;
		if (this.databaseManagerName!=null) {
			databaseManagerName = this.databaseManagerName;
		}else{
			databaseManagerName = DbUtils.getDatabaseManagerNameByDriver( this.driver );
		}
		DatabaseManager databaseManager = (DatabaseManager) ObjectUtil.createObjectByName(databaseManagerName);
		return new PooledConnection(conn, this.name, databaseManager);
	}
	 

	/**
	 * Constructor of DBConnectionPool Object. 
	 * @param name the pool name
	 * @param databaseManagerName TODO
	 * @param URL the database connection url
	 * @param user user for login database
	 * @param password password for login database
	 * @param maxConn the maximal connection ojbect number in the pool
	 * @throws SQLException 
	 */ 
	public ConnectionPool(String name, String driver, String databaseManagerName,
			String URL, String user, String password, int maxConnections, int minConnections) throws SQLException {
		freeConnections = new Vector<Connection>();
		this.name = name;
		this.driver = driver;
		this.databaseManagerName = databaseManagerName;
		this.url = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConnections;
		this.minConn = minConnections;
		
		this.init();
	}

	/**
     * ���ӳش���ʱ���ݲ���������ʼ�����Ӷ���
	 * @throws SQLException 
     */
    protected void init() throws SQLException  
    {
        try
        {
            Class.forName(driver);
            for (int i = 0; i < minConn ; i++)
            {
                Connection con = newConnection();
                freeConnections.add(con);
            }
        } catch(ClassNotFoundException cex) {
            log.error( "ConnectionPool:init()::ClassNotFoundException--"+cex.toString());
        
        } 
    }

	/***************************************************
	 * by Zeven
	 * below is implements of DataSource.
	 * 
	 ***************************************************/
	/**
	 * @deprecated
	 */
	public int getLoginTimeout() throws SQLException { 
		return 0;
	}

	public void setLoginTimeout(int seconds) throws SQLException { 
		
	}

	/**
	 * @deprecated
	 */
	public PrintWriter getLogWriter() throws SQLException { 
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException { 
		
	}

	/**
	 * @deprecated
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
