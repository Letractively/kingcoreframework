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

package com.kingcore.framework.base.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jca.cci.InvalidResultSetAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import wzw.util.DbUtils;

import com.kingcore.framework.base.dao.DaoJdbc;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.PlainResultSetBeanExtractor;
import com.kingcore.framework.jdbc.RowSetResultSetExtractor;

/**
 * <pre>
 * 	���ࣺDao Jdbc Base Object.
 * 		> ���� Spring JdbcDaoSupport����Ҫ����;  
 * 		> ����ʹ��Spring������������ֶ���������;
 * 	   	> �ʺϵĿ����ṹΪ���㣺 Struts + Spring + JdbcDao;
 *		> �������ݿ��������֧�֣� wzw.util.DbUtils.class
 * 	
 * 	DaoJdbcSpringImpl.java
 * </pre>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class DaoJdbcSpringImpl  extends JdbcDaoSupport 
		implements DaoJdbc, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ��־����
	 */
	protected static Logger log = Logger.getLogger( DaoJdbcSpringImpl.class );

	
	/**
	 * 
	 * <pre>��ȡϵͳ���д���</pre>
	 * @author Zeven on 2008-6-4
	 * @param tblName
	 * @return
	 */
	protected long getIdentityValue(String tblName){
    	try {
			return ApplicationContext.getInstance().getDatabaseManager().getIdentityValue( tblName.toUpperCase(), this.getConnection());
			
    	} catch (SQLException e) {
			
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("��ȡ����Ϊ["+tblName+"]�ı��ϵͳ���в�������!", e);
		}
		
	}

	/**
	 * <pre>
	 * 		Ϊ�˼��ݳ����ٸ�Ϊ public ���ͣ������ protected ���͡�
	 * 
	 * ���ַ��ʹ���󱣴浽���ݿ�ı��С�
	 *   CLOB(Character   Large   Object)   
	 *     ���ڴ洢��Ӧ�����ݿⶨ����ַ������ַ����ݡ���������long���ͣ� 
	 * </pre>  
	 *      
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param Content Ҫ�������ݿ������
	 * @return
	 */	
	protected boolean updateClobColumn(String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content){

		try {
			return ApplicationContext.getInstance().getDatabaseManager().updateClobColumn(  tablename, 
					  picField, 
					  sqlWhere,
					  content,
					  this.getConnection() );
			
		} catch (CannotGetJdbcConnectionException e) {
			throw e;
			
		} catch (Exception e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("���±�["+tablename+"]��Clob�ֶ�["+picField+"]��������!", e); 
		}
		
	} 

	/**
	 * 
	 * <pre>�൱����ǰ�� doQuery ������
	 * 	ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @author Zeven on 2008-5-26
	 * @param sql
	 * @return
	 */
	protected RowSet queryForRowSet(String sql) {
		return (RowSet)this.getJdbcTemplate().query(sql,  new RowSetResultSetExtractor() );
	}

	
	/**
	 * <pre>
	 * ����PrepareStatement��ʽִ��ָ����sql��䣬���������װΪRowSet���󷵻ء�
	 * 
	 * </pre>
	 * 
	 * @param sql ��Ҫִ�в�ѯ��sql���
	 * @param args PrepareStatement��ʽ���õĲ���ֵ����
	 * @param argTypes PrepareStatement��ʽ���õĲ���ֵ�����Ӧ����������
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForRowSet(String sql, Object[] args, int[] argTypes ) {
		//this.getJdbcTemplate().query(String sql, Object[] args, int[] argTypes, new RowSetResultSetExtractor()));
		return (RowSet)this.getJdbcTemplate().query( sql,  args, argTypes, new RowSetResultSetExtractor() );
	}

	
	/**
	 * 
	 * <pre>��ȡָ����ҳ��RowSet��
	 * 	ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @author Zeven on 2008-5-26
	 * @param navigator ��ҳ��Ϣ����[Paginated]
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @return
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas) {

		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
		
		return this.queryForRowSet( sql_datas );	// not PreparedStatement
		
	}

	
	/**
	 * 
	 * <pre>��ȡָ����ҳ��RowSet��</pre>
	 * @author Zeven on 2008-5-26
	 * @param navigator ��ҳ��Ϣ����[Paginated]
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @param args ��������
	 * @param argTypes ������������
	 * @return
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas, Object[] args, int[] argTypes) {

		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
		
		return this.queryForRowSet( sql_datas , args, argTypes);	//PreparedStatement
		
	}


	/**
	 * <pre>
	 *  ִ��ָ����sql��䣬���������װΪList<Map>���󷵻ء�
	 *    ��Ҫʹ��PrepareStatement��ʽ��ʹ�����صķ����� 
	 * </pre>
	 *  
	 * @param sql ��Ҫִ�в�ѯ��sql���
	 * @return
	 * @throws SQLException
	 */
	protected List queryForList(String sql ) {
		return this.getJdbcTemplate().queryForList(sql);
	}

	
	/**
	 * <pre>
	 * ����PrepareStatement��ʽִ��ָ����sql��䣬���������װΪList<Map>���󷵻ء�
	 * </pre>
	 * 
	 * @param sql ��Ҫִ�в�ѯ��sql���
	 * @param args PrepareStatement��ʽ���õĲ���ֵ����
	 * @param argTypes PrepareStatement��ʽ���õĲ���ֵ�����Ӧ����������
	 * @return
	 * @throws SQLException
	 */
	protected List queryForList(String sql, Object[] args, int[] argTypes) {
		return this.getJdbcTemplate().queryForList(sql, args, argTypes);
	}
	
	
	/**
	 * <pre>��ȡָ����ҳ��List<ModelObject>��
	 * 	    ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @param navigator ��ҳ��Ϣ����[Paginated]
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @param clazz List����Ķ��󣬶�Ӧһ�����ݣ�����Ϊnull
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Class clazz) {

		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.queryForList(sql_datas, clazz);	// return List<Bean>
		
	}

	
	/**
	 * <pre>��ȡָ����ҳ��List<ModelObject>�� ��PreparedStatement��ʽ��</pre>
	 * @param navigator ��ҳ��Ϣ����[Paginated]
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @param args �������� 
	 * @param argTypes ������������
	 * @param clazz List��ÿ�ж����ģ�Ͷ���javaBean
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Object[] args, int[] argTypes, Class clazz) {

		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.queryForList(sql_datas, args, argTypes, clazz);	// return List<Bean>
		
	}
	
	/**
	 * <pre>
	 * ִ��ָ����sql��䣬������List��Ϊ�����ÿһ��ӳ��һ��beanClass���� 
	 * 	ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����
	 * </pre>
	 * 
	 * @param sql_datas ��Ҫִ�е�sql statement
	 * @param beanClass map one row to beanClass
	 * @return
	 */
	protected List<?> queryForList(String sql_datas, Class beanClass) {
		return (List<?>)this.getJdbcTemplate().query(sql_datas,  new PlainResultSetBeanExtractor(beanClass,true) );
	}
 
	 
	/**
	 * <pre>
	 * ִ��ָ����sql��䣬������List��Ϊ�����ÿһ�ж�Ӧһ��beanClass����
	 * </pre>
	 * 
	 * @param sql_datas ��Ҫִ�е�sql statement
	 * @param args ��������
	 * @param argTypes ������������
	 * @param beanClass map one row to beanClass
	 * @return
	 */
	protected List queryForList(String sql_datas, Object[] args, int[] argTypes, Class beanClass) {
		return (List)this.getJdbcTemplate().query(sql_datas, args, argTypes, new PlainResultSetBeanExtractor(beanClass,true) );
	}

	
	/**
	 * <pre>��ȡָ����ҳ��List��ÿ��ʹ��һ��Map��װ���ݡ�
	 * 	ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @param navigator ��ҳ��Ϣ����[Paginated]
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas) {
		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.getJdbcTemplate().queryForList(sql_datas);		// return List<Map>
		
	}

	/**
	 * <pre>��ȡָ����ҳ��List��ÿ��ʹ��һ��Map��װ���ݣ���List<Map>��
	 * 		PreparedStatement��ʽ��</pre>
	 * @param navigator ��ҳ��Ϣ����[Paginated]
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @param args ��������
	 * @param argTypes ������������
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Object[] args, int[] argTypes) {
		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.getJdbcTemplate().queryForList(sql_datas, args, argTypes);		// return List<Map>
		
	}
	
	/**
	 * <pre>
	 * ���ݱ����ƺͲ�ѯ��������ȡ�������������ݵ�������
	 *
	 * </pre>
	 * 
	 * @param tableName Ҫ��ѯ�ı�����
	 * @param condition �����������硰WHERE numCol>100��
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected int getSize(String tableName, String condition) {
		//return DBUtils.getSize(tableName, condition, this.getConnection() );

		return this.getJdbcTemplate().queryForInt( "SELECT count(*) FROM "+tableName.toUpperCase()+" "+condition ) ;
		
	}
	

	/**
	 * <pre>��ȡ������������ֵ��</pre>
	 * just for MySQL,get id that insert last.
	 * 
	 * @return
	 */
	protected int getLastInsertIdentity() {
		try {
			return ApplicationContext.getInstance().getDatabaseManager().getLastInsertIdentity( this.getConnection() );
			
		} catch (CannotGetJdbcConnectionException e) {
			throw e;

		} catch (SQLException e) {

			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("ִ�� getLastInsertIdentity ʱ��������!", e);
		
		}
	}


	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * </pre>
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs) throws SQLException{
		try{
			return ApplicationContext.getInstance().getDatabaseManager().resultSet2RowSet(rs);
			
		} catch (SQLException e) {
			
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("ResultSet ת��Ϊ RowSet ʱ�����쳣!!", e);
		}
		
	}

	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * </pre>
	 * 
	 * @param rs
	 * @param dbm �ض������ݿ������
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm){
		try {
			return dbm.resultSet2RowSet(rs);
			
		} catch (SQLException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidResultSetAccessException("ResultSet ת��Ϊ RowSet ʱ�����쳣!!", e);
		
		}
	}


	/**
	 * <pre>
	 * add sql statements to a java.sql.Statement Object.
	 * </pre>
	 * 
	 * @author Zeven on 2007-04-24.
	 * @param stmt
	 * @param slq_list
	 * @throws SQLException
	 */
	protected void addBatch(Statement stmt, List slq_list) {
		try {
			DbUtils.addBatch( stmt, slq_list);
			
		} catch (SQLException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new DataAccessResourceFailureException("Exception when add list of sql to Statement Object!!!",e);
		
		}
		
	}

	
	/**
	 * <pre>
	 * ���ݵ�ǰ�����ݿ������������Ҫ��������������⴦��
	 * 		����ַ�������(Varchar)������(Number)������(Date)�Ͷ�����(LOB)���Ͳ����������
	 * 		escape2Sql �滻 convertString ������
	 * 		-- ��ǰ����� Oracle ���ݿ⣬�� ' ���� �滻Ϊ '' �����ܲ��뵽���ݿ��С�
	 * DBUtils.convertString("ab'cd")			="ab''cd"
	 * DBUtils.convertString("ab'c'd")			="ab''c''d"
	 * DBUtils.convertString("ab''cd")			="ab''''cd"
	 * </pre>
	 * 
	 * @param src ��Ҫ���浽���ݿ��һ���ֶΡ�
	 * @return
	 */
	protected String escape2Sql(String src) {
		
		return ApplicationContext.getInstance().getDatabaseManager().escape2Sql(src);
	}

//	/**
//	 * @deprecated replaced by escape2Sql method.
//	 * @param src
//	 * @return
//	 */
//	protected String convertString(String src) {
//		
//		return ApplicationContext.getInstance().getDatabaseManager().escape2Sql(src);
//	}


	/**
	 * <pre>
	 * ���ݵ�ǰ�����ݿ��﷨���������������ַ�����SQL Ƭ�ϡ�
	 * 	
	 * 		Oracle ���ݿⷵ�� str1 +"||"+ str2;
	 * 			concat( userid, username) = userid||username;
	 * 			concat( '001',  'admin' ) = '001'||'admin';
	 * 		MySQL  ���ݿⷵ�� concat(str1, str2);
	 * 			concat( userid, username) = concat(userid,username);
	 * 			concat( '001',  'admin' ) = concat('001' ,'admin');
	 * 		SQLServer  ���ݿⷵ�� str1 +"+"+ str2;
	 * 			concat( userid, username) = userid + "+" + username);
	 * 			concat( '001',  'admin' ) = '001' + "+" + 'admin');
	 * </pre>
	 * @param str1 �ַ�����������
	 * @param str2 �ַ�����������
	 * @return
	 */
	protected String concat(String str1, String str2) {
		return ApplicationContext.getInstance().getDatabaseManager().concat( str1, str2 );
	}


	/**
	 * <pre>
	 * ���ݵ�ǰ�����ݿ��﷨��ת������Ϊnull�ı��ʽ��
	 * 
	 * 		Oracle ���ݿⷵ�� nvl(exp1, exp2);
	 * 		MySQL  ���ݿⷵ�� coalesce( exp1, exp2);
	 * 		SQLServer  ���ݿⷵ�� isNull(exp1, exp2);
	 * </pre>
	 * @param str1 �ַ�����������
	 * @param str2 �ַ�����������
	 * @return
	 */
	protected String switchNull(String exp1, String exp2) {
		return ApplicationContext.getInstance().getDatabaseManager().switchNull( exp1, exp2 );
	}

	
//	/**
//	 * ��ȡΨһ��ʶ���ı��ʽ��
//	 * 
//   * @deprecated replaced by nextUniqueID(String tblName)
//	 * @param tblName ��Ҫʹ�ñ�ʶ���ı���"tsys_flow","employee","demo_table"
//	 * @return
//	 */
//	protected String nextUniqueID(String tblName) {
//		return ApplicationContext.getInstance().getDatabaseManager().identity( tblName );
//	}


	/**
	 * <pre>
	 * ��ȡΨһ��ʶ���ı��ʽ��
	 * </pre>
	 * 
	 * @param tblName ��Ҫʹ�ñ�ʶ���ı���"tsys_flow","employee","demo_table"
	 * @return
	 */
	protected String identity(String tblName) {
		return ApplicationContext.getInstance().getDatabaseManager().identity( tblName.toUpperCase() );
	}
	

	/**
	 * <pre>
	 * ��ȡϵͳʱ��ĺ������ʽ��
	 * </pre>
	 * @return
	 */	
	protected String sysDatetime() {
		return ApplicationContext.getInstance().getDatabaseManager().sysDatetime() ;
	}
	
	
	/**
	 * <pre>
	 *  ���ؽ����ݿ���������תΪ�ַ����͵ķ�������Ҫ�� select �����ʹ�á�
	 * 
	 * 	�� Oracle ����  to_char(birthday,'yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colName ������
	 * @return
	 */
	protected String date2Char(String colName) {
		return ApplicationContext.getInstance().getDatabaseManager().date2Char( colName );
	}

	/**
	 * <pre>
	 *  ���ؽ�java StringֵתΪ���ݿ��������͵ķ�������Ҫ�� insert,update ��ʹ�á�
	 * 
	 * 	�� Oracle ����  to_date('2007-01-01','yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colValue Ҫ�����е�ֵ
	 * @return
	 */
	protected String char2Date(String colValue) {
		return ApplicationContext.getInstance().getDatabaseManager().char2Date( colValue );
	}


	/**
	 * <pre>
	 * ���ؽ����ݿ�����ʱ������תΪ�ַ����͵ķ�������Ҫ�� select �����ʹ�á�
	 * 
	 * 	�� Oracle ����  to_char( beginTime,'yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colName ������
	 * @return
	 */
	protected String datetime2Char(String colName) {
		return ApplicationContext.getInstance().getDatabaseManager().datetime2Char( colName );
	}

	/**
	 * <pre>
	 * ���ؽ�java StringֵתΪ���ݿ�����ʱ�����͵ķ�������Ҫ�� insert,update ��ʹ�á�
	 * 
	 * <pre>
	 * 	�� Oracle ����  to_date('2007-01-01','yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colValue Ҫ�����е�ֵ
	 * @return
	 */
	protected String char2Datetime(String colValue) {
		return ApplicationContext.getInstance().getDatabaseManager().char2Datetime( colValue );
	}

	/**
	 * <pre>
	 * ��ȡ�ַ��ʹ��������ݡ�
	 * </pre>
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected String getClobColumn(String sql) {
		try {
			return ApplicationContext.getInstance().getDatabaseManager().getClobColumn( sql );
			
		} catch (Exception e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("��ȡ���Clob�ֶβ�������sql���Ϊ:["+sql+"]!", e);
		}	
	}

	
	/**
	 *  <pre>���ݷ�ҳ��Ϣ���޸Ĳ�ѯ���ݵ� sql statement.
	 *  	������֧�ֵ����ݿ���ͳһ�Ĵ��� ��ͬ�����ݿ����ʹ�����ͬ��
	 *     --- ����Ϊprotected������ʹ��private����Ϊ�˱��ں������ĺ���ķ�ҳ���Ʒ���ʲôλ�á�</pre>
	 * @param sql ��װ֮ǰ�����ݲ�ѯ statement
	 * @param dataSet �������壬������ȡ������Ϣ
	 * @return ����ȡ����Ϣ���а�װ֮��� sql statement.
	 */
	protected String createSqlForPage(Navigator navigator, String sql_count, String sql_datas ) {

		// �Ƿ������������Ϣ
		int rowCount = navigator.getRowCount();
		int pageSize = navigator.getPageSize();
		int pageNumber = navigator.getPageNumber();		
		//String action = "query";
		
		//����ҳ����
		if ( rowCount<0 )
		{
			// �Ȳ������
			log.debug("get rowCount and"
					+"\n\tpageSize="  +pageSize
					+"\n\tpageNumber="+pageNumber
					+"\n\tsql="+sql_count);

			rowCount = this.getJdbcTemplate().queryForInt( sql_count );	
			navigator.setRowCount(rowCount);	// ����������
		}
		
		String sql = ApplicationContext.getInstance().getDatabaseManager().getSubResultSetSql( sql_datas, 
				pageSize*(pageNumber-1)+1,  pageSize  );

		return sql;
	}

	/**
	 *  <p>wzw:������ȡ��������sql�������ṩһ��ֱ�Ӹ��ݲ�ѯ���ݵ�sql���ɲ�ѯ�������ļ򵥷�����
	 *  	�ʺϿ���ʵ�ֹ��ܣ���������Ч�ʵͣ���Ҫ�Ż���
	 *     �ڴ��뷭ҳ��ѯ�Ĳ�������ʱʹ�á�
	 *  </p>  
	 * @param sql_datas
	 * @return
	 */
	protected String createSqlForCount( String sql_datas){
		return "Select Count(*) from ("+sql_datas+")";
	}

	/**
	 * <pre>
	 * Zeven: ʹ��AopΪ���������ݿ����񣬲�Ҫʹ���������������������������Connection�����ò�������conn�ķ�ʽ��
	 * </pre>
	 * @deprecated ��Ҫʹ�����������
	 */
	public void setConnection(Connection conn) {
		System.out.println("*** Use Aop for Jdbc translate, not use this setConnection method!!!!!");
	}

	protected String currentDataSourceName = ApplicationContext.getInstance().getDataSourceManager().getDefaultDataSourceName();
	public String getDataSourceName() {
		return this.currentDataSourceName;
	}
	
}
