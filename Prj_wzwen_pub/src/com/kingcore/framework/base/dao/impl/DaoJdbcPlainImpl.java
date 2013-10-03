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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import wzw.sql.SqlUtils;
import wzw.util.DbUtils;

import com.kingcore.framework.base.dao.DaoJdbc;
import com.kingcore.framework.bean.NavigableDataSet;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.bean.QueryDataSet;
import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.MapResultSetExtractor;
import com.kingcore.framework.jdbc.PlainConnection;
import com.kingcore.framework.jdbc.RowSetResultSetExtractor;
import com.kingcore.framework.jdbc.TypeResultSetExtractor;
import com.kingcore.framework.transaction.ConnectionTransactionManager;
import com.kingcore.framework.transaction.TransactionType;

/**
 * <pre>
 *	��Ա�����������Լ���������ķ�ʽ��
 *		> �ʺϼ򵥿��ٿ������������κ��������������;
 * 		> ����Ҫ���ã���Ҫ�Լ���ȡ���ӳ�Ա���ֶ���������;
 * 		> �ʺϵĿ����ṹΪ���㣺 Struts + JdbcDao ;
 *		> ���г�Ա����conn���漰��connʹ�õĵط������Ƿ��̰߳�ȫ�ģ�һ�㲻Ҫʹ�õ���ģʽ;
 *		> �������ݿ��������֧�֣� wzw.util.DbUtils.class
 *
 *		>getConnection() ���ص��� autocommit=true�ģ����ⲿ�Լ�����conn�ģ���һ��������conn����
 *                         �����÷��������ȿ��Ƿ����������ã���ο��Ƿ���this.conn��Ա������Ҫ�Ĵ����µ�conn��
 *		>�����Ҫʹ�úõ��������һ����ʹ��tm.begin,�������Լ���ȡconn��
 *
 * 	</pre>
 * @author Zeven on 2008-8-20
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class DaoJdbcPlainImpl 
			implements DaoJdbc , Serializable {

	
	/* ************************** ������@deprecated ������ ************************************* */

	public static final String CHANGE_PAGE = "changepage";
	 
	/**
	 * <pre>�����ͷ����ݿ������ wzw on 2006-9-24
	 * �����ͷ�֮�󣬸������Ͳ���ִ�����ݿ�����ˣ�
	 * 		������ʹ�� getConnection()����֮��Ҳ����ִ�����ݿ�����ˣ�
	 * 		����һ��ҵ�����DAO�ӿ�ʼ�����������Ҫ����ʹ��Connection��
	 * 		��ֻʹ��һ��Connection��������ʹ�ö��������ʹ������һ��ҵ�������ǧ��Ҫ�ͷ����ٻ�ȡ��
	 * 		������ȫ������Connection��</pre>
	 * @deprecated ֱ��ʹ�� this.conn.close() �����ر�����
	 * 
	 */
	protected void freeConnection() throws SQLException{
		if(this.conn!=null) {
			this.conn.close();
			// Zeven ,����������á� һ����˵������close(),Ҳ�Ͳ�����ִ�����ݿ����ӡ�
			// ֻ��ʹ�� conn.close(),��������Ϊnull,�������ӳ��еĶ�����Ϊnull(��) �ˡ�
			// ��������Ͽ������ã������Ը�ֵΪnull���𣿣���
			//this.conn = null ;
		}
	}


	/**
	 * <pre>ִ���б��ѯ����</pre>
	 * @deprecated this method is replace by another one����ʹ�� doQueryDataSet( String sql_datas,String sql_count, int[] pageParams) ����.
	 * @param request web request object
	 * @param sql_datas SQL statement for query datas
	 * @param sql_count SQL statement for query row number
	 * @throws Exception
	 
	protected NavigableDataSet doQueryDataSet(HttpServletRequest request,
			String sql_datas,
			String sql_count) throws Exception {
		
		return doQueryDataSet( sql_datas, sql_count, getPageParameter(request) );
	}
	 */

	/**
	 * ��ʱ��ʹ�ã����ǲ���ֱ�ӵõ����� wzw on 2006-9-24
	 * @deprecated
	 */
	protected DataSource getDataSource() throws SQLException{
		return null;
	}

	
	/**
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * @deprecated replaced by resultSet2RowSet(ResultSet rs)
	 * @param rs
	 * @return
	 * @throws SQLException 
	protected RowSet populateResultSet(ResultSet rs) throws SQLException{
		return ((PooledConnection)conn).getDatabaseManager().resultSet2RowSet(rs);
	}
	 */

	/* ************************** @deprecated ��������� ************************************* */

	private static final long serialVersionUID = 1L;
	
	/**
	 * ��־��¼����
	 */
	protected final static Logger log = Logger.getLogger( DaoJdbcPlainImpl.class);
	
	/// private DataSource dataSource = ApplicationContext.getInstance().getDataSourceProvider().getDataSource(); 

	/// public boolean hasConnection = false;
	//protected DataSource ds;

	/**
	 * չʾ����·��
	 */
    static String showTrace(int maxdepth)
    {
      String stack="����·����\n";
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 1; i < Math.min(maxdepth + 1, trace.length); i++)
      {
        stack += "\t[" + trace[i].hashCode() + "]" + trace[i] + "\n";
      }
      return stack;
    }
    
	/**
	 * <pre>��ȡ���ݿ������ wzw on 2006-9-24
	 * 		ֱ��ʹ���� conn.close() �Ͽ������ӡ�</pre>
	 * 
	 * @return
	 * @throws SQLException 
	 */
	protected Connection getConnection() throws SQLException{
		return this.getConnection( this.getDataSourceName() );
	}

	/**
	 * ���������õ����Ӷ���һ��Ҫע��������ƺ��̰߳�ȫ��
	 *  >��conn��������defaultDataSorceName��Ӧ��DS��Ҳ����������DataSource��
	 *    this.conn ��Ӧ�� this.currentDataSourceName�����ﲻ����Map<String,Connection>�ĸ��ӷ�ʽ��
	 *  >��connͨ����Ա���ã�ʵ�ּ򵥵�conn������������Ҫ�����������ʽ��
	 *  		����ʹ��TransactionManager���߻�ȡconn�Լ����ơ�
	 */
	protected Connection conn = null ;
	
	/**
	 * <pre>��ȡ���ݿ������ wzw on 2006-9-24
	 * 		ֱ��ʹ���� conn.close() �Ͽ������ӡ�</pre>
	 * 
	 * @param poolName ���ӳض�������ƣ�û�в�����ʹ�����õ�Ĭ�����ӳ�
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(String dataSourceName) throws SQLException{
		if(this.conn==null || this.conn.isClosed() ) {
			this.conn = ApplicationContext.getInstance().getDataSourceManager().getConnection(dataSourceName);
		}
		this.conn.setAutoCommit(true);   //Ĭ��Ϊ�Զ��ύ
		this.setDataSourceName(dataSourceName); //ͬ��this.conn����dataSourceNameֵ
		return this.conn;
	}

	/**
	 * �����������ʽ�µ�conn��ȡ��
	 * 	> ֧������Ƕ��;
	 *  > ��DataSource�ĸ�������;
	 *  > ������������µ�connection��ȡ������ʱ�����⿪�š�
	 *  
	 *  > ͨ���������ַ�ʽ��ȡ����connection���ݲ�ͬ�Ļ������в�ͬ�ص㣬��Ҫ�����ֻ�����һ�������񻷾�����һ���������񻷾����ֱ��������£�
	 *  > ��������񻷾��»�ȡconnection����ôconnection���������������е���������һ��Ҳ����˵connection���������������Ļ���������ύ���ύ��Ҳ��������������Ļع����ع�������connection�Ļع�Ҳ�ᵼ����������������Ļع���ͬʱconnection��setAutocommit������close����������Ӱ�����������񣬾���˵���������á�
	 *  > �����񻷾��»�ȡ���ݿ����Ӻ󣬲�����ʾ�ر�connection��Ҳ����Ҫ��ʾcommit��������Ҫ��ʾrollback��������ύ�����ӵĹر����ⲿ�����Զ��رպ��ύ��
	 *  > ��û������Ļ����»�ȡconnectionʱ��connection����ԭʼ��jdbc connection���������Ժͷ�����
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection(TransactionType transType, String dataSourceName) throws SQLException{
		Connection l_conn = ConnectionTransactionManager.getCurrentConnection(transType, dataSourceName);
		return l_conn;
	}

	private Connection getConnection(TransactionType transType) throws SQLException{
		return getConnection(transType, this.getDataSourceName());
	}
	
	/**
	 * <pre>���ⲿ���� DAO ����� conn ��Աֵ��
	 * 		������һ��DAO��������һ��DAO��ͬʱ����Connection�������</pre>
	 * 
	 * @param p_conn
	 */
	public void setConnection( Connection p_conn ) {
		this.conn = p_conn ;
	}

	
	/**
	 * <pre>
	 * Execute an SQL SELECT query without any replacement parameters.  The
	 * caller is responsible for connection cleanup.
	 * 	�� Prepared ��ʽ����Ҫʹ�� Prepared��ʽ��������������� queryForRowSet
	 * </pre>
	 * 
	 * @param sql The query to execute.
	 * @return The object returned by the handler.
	 * @throws SQLException
	public RowSet doQuery(String sql)
	 throws SQLException {

		log.debug("--------------wzw--------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
		RowSet rs = (RowSet) query(sql, null, new RowSetResultSetExtractor() );
		log.debug("--------------wzw--------------"+ ((this.conn==null)?"is null":"not null") );
		
		return rs;

		// drop by Zeven on 2008-11-05
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.doQuery( sql );
//		}else {
//			return DBUtils.doQuery(sql, conn );
//		}
	}
	 */

	
	/**
	 * 
	 * <pre>Execute an SQL INSERT, UPDATE, or DELETE query without replacement
	 *    ִ�������������������⣺
	 * 		1�����DAO����ǰ��conn��Ա������ô�������������κ��ύ/�ع����ر����ˣ�
	 * 		2�����DAO����ǰ���ұ�������������⣬���ǲ�������conn��Ա����
	 * 					������DBBean��Ϊ��̬����ʱ��������������conn��Ա���µ��̳߳�ͻ���⣻
	 * 
	 * 		�� PreparedStatement��ʽ��ʹ��Prepared��ʽ�����������ط�����
	 * 	</pre>
	 *
	 * @param conn The connection to use to run the query.
	 * @param sql The SQL to execute.
	 * @return The number of rows updated.
	 * @throws SQLException
	 */
	protected int executeUpdate( String sql ) throws SQLException {
		return executeUpdate(sql, null, null);
	}
	
	/**
	 * <pre> doUpdate ��PreparedStatement��ʽ��</pre>
	 * @param sql	����?��sql���
	 * @param args	���?�Ĳ�����������
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(String sql, Object[] args) throws SQLException {    
		return executeUpdate(sql, args, null);
	}
	
	
	/**
	 * <pre> doUpdate ��PreparedStatement��ʽ��
	 *    ���ӵ������ݣ�ʹ��log4j�򿪻��߹رյ��������Ϣ��</pre>
	 * @param sql	����?��sql���
	 * @param args	���?�Ĳ�����������
	 * @param argTypes �������������Ӧ�Ĳ�������, java.sql.Type
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(String sql, Object[] args, int[] argTypes) throws SQLException {    

		boolean isConnCreated = false;
		PreparedStatement ps = null;
		Statement stmt = null;
		Connection l_conn = null;
		int intReturn = 0 ;
		try{
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : �������ݿ�������� -------------------------------------------------->
			 
			if(args!=null){		// has params or not??
				ps = l_conn.prepareStatement(sql);
				if(argTypes==null){
					log.debug("---------argTypes is null and args.length is:"+args.length);
					SqlUtils.setStatementArg(ps, args);
				}else{
					log.debug("---------argTypes is not null and args.length is:"+args.length);
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
				intReturn = ps.executeUpdate();
		        stmt = ps; //���ں���رա�
				
			}else{
				stmt = l_conn.createStatement();
				intReturn = stmt.executeUpdate(sql);
			}

			// end : �������ݿ�������� --------------------------------------------------<			
			if(isConnCreated && l_conn!=null){		// �ύ
				log.debug("---------isConnCreated is true,so commit conn.");
				l_conn.commit();
			}
			log.debug("---------end of all main process.");
			
		}catch(SQLException sqle){			
			if(isConnCreated && l_conn!=null){		// �ع�
				l_conn.rollback();
			}
            log.fatal( "Result in update Exception'SQL is:\n"+sql + ". Message:" + sqle.getMessage() ) ;
			log.debug("debug", sqle);
			throw sqle;
		
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(l_conn, stmt, null);	// �ر�
			}else{
				DbUtils.closeQuietly( stmt );
			}
		}
		
        return intReturn;
	}

	
	/**
	 * 
	 * <pre>ִ�������������������⣺
	 * 		1�����DAO����ǰ��conn��Ա������ô�������������κ��ύ/�ع����ر����ˣ�
	 * 		2�����DAO����ǰ���ұ�������������⣬���ǲ�������conn��Ա����
	 * 					������DBBean��Ϊ��̬����ʱ��������������conn��Ա���µ��̳߳�ͻ���⣻
	 * 
	 * 	</pre>
	 * @param conn ���ݿ����Ӷ���
	 * @param allsql Ҫִ�е�sql�����ɵ����顣
	 * @throws ִ��������ʧ�ܡ�
	 * @return ÿ��sql���Ӱ���������ɵ����顣
	 */
	protected int[] executeBatch( List<String> list )
	  throws SQLException {

		int[] returns = null;
		
		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		Statement stmt = null;
		Connection l_conn = null;
		try{
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : �������ݿ�������� -------------------------------------------------->
        	
        	stmt = l_conn.createStatement();
            addBatch( stmt, list);
            returns = stmt.executeBatch();

			// end : �������ݿ�������� --------------------------------------------------<
            if( isConnCreated && l_conn!=null){
            	l_conn.commit() ;	 	//�����ⲿ��������ӣ����ύ�����ع������ر�
            }
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            if( isConnCreated && l_conn!=null){
            	l_conn.rollback(); 	 //�����ⲿ��������ӣ����ύ�����ع������ر�
            }
            
        	String info= "Result in doBatch Exception'SQLs is:" ;
        	for(int i=0;i<list.size();i++){
        		info +=list.get(i).toString()+";";
        	}
            log.fatal(DbUtils.class.getName()+" "+ e.getMessage() + ". " + info) ;			
			log.debug("debug", e);
            /// e.pri ntStackTrace();
            throw e;

        } finally {
            if( isConnCreated && l_conn!=null){
            	wzw.util.DbUtils.closeQuietly(l_conn, stmt, null);
            }else{
            	wzw.util.DbUtils.closeQuietly( stmt );
            }
        }
        
	}

	/**
	 * 
	 * <pre>���ݱ����ƺͲ�ѯ��������ȡ�������������ݵ�������
	 * 	Zeven on 2009-2-11Ϊ�˽��MySql���⣬���� tableName.toUpperCase()
	 * </pre>
	 * 
	 * @param tableName Ҫ��ѯ�ı�����
	 * @param condition �����������硰WHERE numCol>100��
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected int getSize(String tableName, String condition) throws SQLException {
		
		String sql = "SELECT count(*) FROM "+tableName+" "+condition;
		return this.queryForInt(sql);
		
//		if(this.conn==null || this.conn.isClosed()) {
//			return DbUtils.getSize(tableName.toUpperCase(), condition);
//		}else {
//			return DbUtils.getSize(tableName.toUpperCase(), condition, this.conn);
//		}
	}

	
	/**
	 * <pre>����sql statement����ȡ�������������ݵ�������
	 *   ���� queryForType ��ѯ�� Type.INT �������
	 * 	��PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql sql statement
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected int queryForInt(String sql ) throws SQLException {
		
		return queryForInt(sql, null, null);
//		// Zeven: ���ȷ�������Լ��ķ������� DbUtils����ķ����أ���
//		// 	 �漰��this.conn������ͳһ����ʱ����ð�ʵ�ַ���Dao���棬������԰�ʵ�ַ��� DbUtils.class���档
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForInt(sql);
//		}else {
//			return DBUtils.queryForInt(sql, this.conn);
//		}
	}


	/**
	 * <pre>
	 * ����sql statement����ȡ�������������ݵ�������
	 *   ���� queryForType ��ѯ�� Type.INT �������
	 * 	ΪPreparedStatement��ʽ��
	 * </pre>
	 *
	 * @param sql sql statement
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected int queryForInt(String sql, Object[] args, int[] argTypes ) throws SQLException {
		Integer iobj = (Integer)query(sql, args, argTypes, new TypeResultSetExtractor( Types.INTEGER, false) ); 
		return iobj.intValue();
	}

	
	/**
	 * <pre>
	 *  ����sql statement����ȡ�������������ݵ��ַ�����
	 *   ���� queryForType ��ѯ�� Type.VARCHAR �������
	 * 	��PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql sql statement
	 * @return �ַ���
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected String queryForString(String sql ) throws SQLException {

		return queryForString(sql, null, null);
	}


	/**
	 * <pre>
	 *  ����sql statement����ȡ�������������ݵ��ַ�����
	 *   ���� queryForType ��ѯ�� Type.VARCHAR �������
	 * 	ΪPreparedStatement��ʽ��
	 * </pre>
	 * @param sql sql statement
	 * @return �ַ���
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected String queryForString(String sql, Object[] args, int[] argTypes ) throws SQLException {
		String str = (String)query(sql, args, argTypes, new TypeResultSetExtractor( Types.VARCHAR, false) ); 
		return str;
	}
		
	/**
	 * 
	 * ����sql statement����ȡ�������������ݵ�������
	 *
	 * @param sql sql statement
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected long queryForLong(String sql ) throws SQLException {
	
		return queryForLong(sql, null,null);
		
//		// Zeven: ���ȷ�������Լ��ķ������� DbUtils����ķ����أ���
//		// 	 �漰��this.conn������ͳһ����ʱ����ð�ʵ�ַ���Dao���棬������԰�ʵ�ַ��� DbUtils.class���档
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForLong(sql);
//		}else {
//			return DBUtils.queryForLong(sql, this.conn);
//		}

	}

	protected long queryForLong(String sql, Object[] args, int[] argTypes ) throws SQLException {
	
		Long lobj = (Long)query(sql, args, argTypes, new TypeResultSetExtractor( Types.BIGINT, false) ); 
		return lobj.longValue();
	}

	
	/**
	 * <pre>
	 *  ִ��ָ����sql��䣬���������װΪList<Map>���󷵻ء�
	 *    ��Ҫʹ��PrepareStatement��ʽ��ʹ�����صķ�����
	 * </pre>   
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> queryForList(String sql_datas) throws SQLException {

		return queryForList(sql_datas, null, null );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toMapList(rs);
		
	}
	

	/**
	 * <pre>����PrepareStatement��ʽִ��ָ����sql��䣬���������װΪList<Map>���󷵻ء�
	 * </pre>
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @param args PrepareStatement��ʽ���õĲ���ֵ����
	 * @param argTypes PrepareStatement��ʽ���õĲ���ֵ�����Ӧ����������
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> queryForList(String sql_datas, Object[] args, int[] argTypes) throws SQLException {

		return (List<Map<String,Object>>)query(sql_datas, args, argTypes, new MapResultSetExtractor(true) );		
	}
	
	
	/**
	 * <pre>
	 *   ִ��ָ����sql��䣬���������װΪList<Bean>���󷵻ء�
	 *    ��Ҫʹ��PrepareStatement��ʽ��ʹ�����صķ�����
	 * </pre>   
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @param modelObject ����List�����е�Bean��������
	 * @return
	 * @throws SQLException 
	 */
	protected List<?> queryForList(String sql_datas, Class<?> beanClass) throws SQLException {

		return queryForList(sql_datas, null, null, beanClass );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toBeanList(rs, modelObject);
		
	}


	/**
	 * 	����PrepareStatement��ʽִ��ָ����sql��䣬���������װΪList<Bean>���󷵻ء�
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @param args PrepareStatement��ʽ���õĲ���ֵ����
	 * @param argTypes PrepareStatement��ʽ���õĲ���ֵ�����Ӧ����������
	 * @param modelObject ����List�����е�Bean��������
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForList(String sql_datas, Object[] args, int[] argTypes, Class<?> beanClass) throws SQLException {

		return (List<?>)queryBean(sql_datas, args, argTypes, beanClass, true );	
	}


	/**
	 * <pre> ִ��ָ����sql��䣬���������װΪList<Type>���󷵻ء�
	 *    ��Ҫʹ��PrepareStatement��ʽ��ʹ�����صķ����� </pre>
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @param type ����List�����е�java.sql.Types �еĻ�����������
	 * @return
	 * @throws SQLException 
	 */
	protected List<?> queryForList(String sql_datas, int type) throws SQLException {

		return queryForList(sql_datas, null, null, type );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toBeanList(rs, modelObject);
		
	}


	/**
	 * 	����PrepareStatement��ʽִ��ָ����sql��䣬���������װΪList<Type>���󷵻ء�
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @param args PrepareStatement��ʽ���õĲ���ֵ����
	 * @param argTypes PrepareStatement��ʽ���õĲ���ֵ�����Ӧ����������
	 * @param type ����List�����е�java.sql.Types �еĻ�����������
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForList(String sql_datas, Object[] args, int[] argTypes, int type) throws SQLException {

		return (List<?>)query(sql_datas, args, argTypes, new TypeResultSetExtractor(type, true) );	
	}

	
	/**
	 * <pre>
	 * ִ��ָ����sql��䣬���������װΪRowSet���󷵻ء�
	 *    ��Ҫʹ��PrepareStatement��ʽ��ʹ�����صķ�����
	 * </pre>   
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForRowSet( String sql) throws SQLException {
		
		return queryForRowSet(sql, null, null );
	}

	
	/**
	 * <pre>
	 * ����PrepareStatement��ʽִ��ָ����sql��䣬���������װΪRowSet���󷵻ء�
	 * </pre>
	 * @param sql ��Ҫִ�в�ѯ��sql���
	 * @param args PrepareStatement��ʽ���õĲ���ֵ����
	 * @param argTypes PrepareStatement��ʽ���õĲ���ֵ�����Ӧ����������
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForRowSet( String sql, Object[] args, int[] argTypes) throws SQLException {

		return (RowSet) query(sql, args, argTypes, new RowSetResultSetExtractor() );
	}
	
	
	/**
	 * 
	 * <pre>��ȡָ����ҳ��RowSet��
	 * 	ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @author Zeven on 2008-5-26
	 * @param navigator ��ҳ��Ϣ����
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );		
		return queryForRowSet(sql);
		
	}

	/**
	 * PrepareStatement��ʽ��queryForPagedRowSet��
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas,
					Object[] args, int[] argTypes) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return queryForRowSet(sql, args, argTypes);
		
	}
	
	
	/**
	 * <pre>��ȡָ����ҳ��List��ÿ��ʹ��һ��Map��װ���ݣ�����������ΪList<Map>��
	 * 	ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @param navigator ��ҳ��Ϣ����
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @return
	 */
	protected List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql);
	
	}

	/**
	 * <pre>��ȡָ����ҳ��List��ÿ��ʹ��һ��Map��װ���ݣ�����������ΪList<Map>��
	 * 	 ʹ��PreparedStatement��ʽ��</pre>
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas,
					Object[] args, int[] argTypes) throws SQLException {
		
		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, args, argTypes);
	}

	
	/**
	 * <pre>��ȡָ����ҳ��List<JavaBean>��
	 * 	    ʹ��Statement��������PreparedStatement����Ҫʹ��PreparedStatement��ʹ�����صķ�����</pre>
	 * @param navigator ��ҳ��Ϣ����
	 * @param sql_count ��ȡ��������sql���
	 * @param sql_datas ��ȡ���ݵ�sql���
	 * @param clazz List����Ķ��󣬶�Ӧһ�����ݣ�����Ϊnull
	 * @return
	 */
	protected List<?> queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Class<?> clazz) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, clazz);
	}
	

	/**
	 * <pre>��ȡָ����ҳ��List<JavaBean>��
	 * 	    ʹ��PreparedStatement��ʽ��</pre>
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForPagedList(Navigator navigator, String sql_count, String sql_datas, 
				Object[] args, int[] argTypes, Class<?> clazz) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, args, argTypes, clazz);
	}
	
	
	/**
	 * 
	 * <pre>
	 *  ��ȡ��һ��Ψһ��ʶֵ��
	 * 	Zeven on 2009-2-11Ϊ�˽��MySql���⣬���� tableName.toUpperCase()
	 * </pre>
	 * 
     * @param tblName ��Ҫʹ��id�ı�����ƣ���"tsys_flowtype"
	 * @return ���е���һ��ֵ
	 * @throws SQLException ���ݿ����ʧ���쳣��
	 */
	protected long identity( String tblName ) {
		
		DatabaseManager databaseManager = null;
		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		//Statement stmt = null;
		Connection l_conn = null;
		//int intReturn = 0 ;
		try{
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : �������ݿ�������� -------------------------------------------------->
		
			if(l_conn instanceof PlainConnection){
				log.debug("---------get DatabaseManager from PlainConnection.");
				databaseManager = ((PlainConnection)l_conn).getDatabaseManager();
			}else{
				log.debug("---------get DatabaseManager from ApplicationContext.");
				databaseManager = ApplicationContext.getInstance().getDatabaseManager();
			}
			
			return databaseManager.getIdentityValue(tblName.toUpperCase(), l_conn);

			// end : �������ݿ�������� --------------------------------------------------<
			
		}catch(SQLException sqle){
            log.error("debug", sqle);
			// throw sqle;
		
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(l_conn );	// �ر�
			} 
		}
		
		return -1; // exception happen
		
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.getIdentityValue( tblName);
//		}else {
//			return DBUtils.getIdentityValue( tblName);
//			//return DBUtils.getIdentityValue( tblName, conn);	//, conn ������������Ϊ�����ܻ��Ǽ���ȥ�����ݾ����������
//			
//			/// return ((PooledConnection)conn).getDatabaseManager().getSequenceValue( seqName, conn);
//			// return DBUtils.getIdentityValue( tblName, conn);
//		}
		
	}
	

//	/**
//	 * Zeven set 'public ' to 'protected'.
//     * @deprecated replaced by getNextUniqueIDValue(String tblName)
//	 * 
//	 * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��
//	 * 
//     * @param tblName ��Ҫʹ��id�ı�����ƣ���"tsys_flowtype"
//	 * @return ���е���һ��ֵ
//	 * @throws SQLException ���ݿ����ʧ���쳣��
//	 */
//	protected int getNextUniqueIDValue( String tblName ) throws SQLException{
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.getIdentityValue( tblName);
//		}else {
//			return DBUtils.getIdentityValue( tblName);
//		}
//		
//	}


	/**
	 * <pre>��ȡ������������ֵ��</pre>
	 * just for MySQL,get id that insert last.
	 * 
	 * @return
	 */
	protected int getLastInsertIdentity(DatabaseManager databaseManager ) throws SQLException {

		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		//Statement stmt = null;
		Connection l_conn = null;
		//int intReturn = 0 ;
		try{
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : �������ݿ�������� -------------------------------------------------->
				
			return databaseManager.getLastInsertIdentity( l_conn );

			// end : �������ݿ�������� --------------------------------------------------<
		}catch(SQLException e){
			log.error(e);
			throw e;
			
		}finally{
			if (isConnCreated ) {
				wzw.util.DbUtils.closeQuietly(l_conn);
			}
		}
	}
	

	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * </pre>   
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs) throws SQLException{
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.resultSet2RowSet(rs);
	}
	
	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * </pre>   
	 * @param rs
	 * @param dbm �ض������ݿ������
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm) throws SQLException{
		return dbm.resultSet2RowSet(rs);
	}


	/**
	 * 
	 * add sql statements to a java.sql.Statement Object.
	 * @author Zeven on 2007-04-24.
	 * @param stmt
	 * @param slq_list
	 * @throws SQLException
	 */
	protected void addBatch(Statement stmt, List<String> slq_list) throws SQLException{
		DbUtils.addBatch( stmt, slq_list);
		
	}
	
	
	/**
	 * <pre>
	 * ���ݵ�ǰ�����ݿ������������Ҫ��������������⴦��
	 * 		����ַ�������(Varchar)������(Number)������(Date)�Ͷ�����(LOB)���Ͳ����������
	 * 		escape2Sql �滻 convertString ������
	 * 		-- ��ǰ����� Oracle ���ݿ⣬�� ' ���� �滻Ϊ '' �����ܲ��뵽���ݿ��С�
	 * 
	 * DBUtils.convertString("ab'cd")			="ab''cd"
	 * DBUtils.convertString("ab'c'd")			="ab''c''d"
	 * DBUtils.convertString("ab''cd")			="ab''''cd"
	 * </pre>
	 * @param src ��Ҫ���浽���ݿ��һ���ֶΡ�
	 * @return
	 */
	protected String escape2Sql(String str) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.escape2Sql(str);
	}


	/**
	 * <pre>
	 * 	
	 * ���ݵ�ǰ�����ݿ��﷨���������������ַ�����SQL Ƭ�ϡ�
	 *   �Ľ�Ҫ�� public ���� �޸�Ϊ protected ���͡�
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
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.concat( str1, str2 );
	}


	/**
	 * <pre>
	 * 	
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
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.switchNull( exp1, exp2 );
	}

	
//	/**
//	 * ��ȡΨһ��ʶ���ı��ʽ��
//	 * 
//   * @deprecated replaced by nextUniqueID(String tblName)
//	 * @param tblName ��Ҫʹ�ñ�ʶ���ı���"tsys_flow","employee","demo_table"
//	 * @return
//	 */
//	protected String nextUniqueID(String tblName) {
//		return ((PooledConnection)conn).getDatabaseManager().identity( tblName );
//	}


	/**
	 * <pre>
	 * 	
	 * ��ȡΨһ��ʶ���ı��ʽ��
	 * 	Zeven on 2009-2-11Ϊ�˽��MySql���⣬���� tableName.toUpperCase()
	 * </pre>
	 * 
	 * @param tblName ��Ҫʹ�ñ�ʶ���ı���"tsys_flow","employee","demo_table"
	 * @return
	 */
	protected String getIdentityValue(String tblName) {
		return this.identity(tblName)+"";
	}
	

	/**
	 * <pre>
	 * ��ȡϵͳʱ��ĺ������ʽ��
	 * </pre>
	 * 
	 * @return
	 */	
	protected String sysDatetime() {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.sysDatetime() ;
		//return ((PlainConnection)this.conn).getDatabaseManager().sysDatetime() ;
	}
	
	
	protected DatabaseManager getCurrentDatabaseManager() { 
		
		DatabaseManager databaseManager = null;
		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		//Statement stmt = null;
		Connection l_conn = null;
		//int intReturn = 0 ;
		try{
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : �������ݿ�������� -------------------------------------------------->
		
			if(l_conn instanceof PlainConnection){
				log.debug("---------get DatabaseManager from PlainConnection.");
				databaseManager = ((PlainConnection)l_conn).getDatabaseManager();
			}else{
				log.debug("---------get DatabaseManager from ApplicationContext.");
				databaseManager = ApplicationContext.getInstance().getDatabaseManager();
			}
			
			// end : �������ݿ�������� --------------------------------------------------<
			
		}catch(SQLException sqle){
            log.error("debug", sqle);
			// throw sqle;
		
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(l_conn );	// �ر�
			} 
		}
		
        return databaseManager;
	}


	/**
	 * <pre>���ؽ����ݿ���������תΪ�ַ����͵�sqlƬ�εķ�������Ҫ�� select �����ʹ�á�
	 * 
	 * 	�� Oracle ����  to_char(birthday,'yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colName ������
	 * @return
	 */
	protected String date2Char(String colName) {

		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.date2Char( colName );
	}

	/**
	 * <pre>���ؽ�java StringֵתΪ���ݿ��������͵�sqlƬ�εķ�������Ҫ�� insert,update ��ʹ�á�
	 * 
	 * 	�� Oracle ����  to_date('2007-01-01','yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colValue Ҫ�����е�ֵ
	 * @return
	 */
	protected String char2Date(String colValue) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.char2Date( colValue );
	}

	/**
	 * <pre>���ؽ����ݿ�����ʱ������תΪ�ַ����͵�sqlƬ�εķ�������Ҫ�� select �����ʹ�á�
	 * 
	 * 	�� Oracle ����  to_char( beginTime,'yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colName ������
	 * @return
	 */
	protected String datetime2Char(String colName) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.datetime2Char( colName );
	}

	/**
	 * <pre>���ؽ�java StringֵתΪ���ݿ�����ʱ�����͵�sqlƬ�εķ�������Ҫ�� insert,update ��ʹ�á�
	 * 
	 * 	�� Oracle ����  to_date('2007-01-01','yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colValue Ҫ�����е�ֵ
	 * @return
	 */
	protected String char2Datetime(String colValue) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.char2Datetime( colValue );
	}

	
	/*
	 protected void populate(Object bean, ResultSet rs) throws SQLException {
	 ResultSetMetaData metaData = rs.getMetaData();
	 int ncolumns = metaData.getColumnCount();
	 
	 HashMap properties = new HashMap();
	 // Scroll to next record and pump into hashmap
	  for (int i=1; i<=ncolumns ; i++) {
	  properties.put(sql2javaName(metaData.getColumnName(i)), rs.getString(i));
	  }
	  // Set the corresponding properties of our bean
	   try {
	   BeanUtils.populate(bean, properties);
	   } catch (InvocationTargetException ite) {
	   throw new SQLException("BeanUtils.populate threw " + ite.toString());
	   } catch (IllegalAccessException iae) {
	   throw new SQLException("BeanUtils.populate threw " + iae.toString());
	   }
	   }
	   
	   public int getSize(String tableName, String condition) throws SQLException {
	   Connection conn = null;
	   PreparedStatement pstmt = null;
	   ResultSet rs = null;
	   try {
	   String sql = "SELECT count(*) FROM "+tableName+" "+condition;
	   conn = ds.getConnection();
	   pstmt = conn.prepareStatement(sql);
	   rs = pstmt.executeQuery();
	   rs.next();
	   int size = rs.getInt(1);
	   close(rs);
	   close(pstmt);
	   return size;
	   } catch (SQLException sqle) {
	   close(rs);
	   close(pstmt);
	   rollback(conn);
	   log.debug("debug", sqle);
	   /// sqle.pri ntStackTrace();
	   throw sqle;
	   } finally {
	   close(conn);
	   }
	   }
	   
	   */
	
	 
//	/**
//	 * @deprecated
//	 */
//	protected String java2sqlName(String name) {
//		String column = "";
//		for (int i = 0; i < name.length(); i++) {
//			if (i < name.length() - 1 && (name.charAt(i) >= 'a' && name.charAt(i) <= 'z') &&
//					(name.charAt(i + 1) >= 'A' && name.charAt(i + 1) <= 'Z')) {
//				column += name.charAt(i) + "_";
//			} else {
//				column += name.charAt(i);
//			}
//		}
//		return column.toLowerCase();
//	}
 
	
//	/**
//	 * Zeven set 'public ' to 'protected'.
//	 * 
//	 * ��ѯ��ҳ��������, just for xxxJdbcDaoImpl��
//	 * @param sql_datas
//	 * @param sql_count
//	 * @param pageParams �����ҳ����Ϣ���� {rowCount, pageSize, pageNumber}
//	 * @return
//	 * @throws Exception
//	 */
//	protected NavigableDataSet queryForNavigableRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new RowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableSqlRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams, Class elementType) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, RowMapper rowMapper) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	protected NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, ResultSetExtractor rse) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	protected NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams ) throws Exception
//	{
//		// Ĭ�ϵ������ݶ����� RowSet�������ܡ�
//		return doQueryDataSet( sql_datas, sql_count,pageParams );
//	}
	
	/**
	 * <pre>
	 * ��ѯ��ҳ�������ݡ�
	 * 
	 * </pre>
	 * @deprecated wzw:����ʹ�� queryForPaged... ����������ҳ���뵼��������ϡ�
	 * @param pageParams �����ҳ����Ϣ���� {rowCount, pageSize, pageNumber}
	 * @param sql_count ��ѯ��������sql���
	 * @param sql_datas ��ѯ���ݵ�sql���
	 * 
	 */
	protected NavigableDataSet executeQueryDataSet( Navigator navigator,
			String sql_count, String sql_datas) throws Exception
	{
		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
		RowSet crs = this.queryForRowSet( sql_datas );
		NavigableDataSet dataSet = new QueryDataSet(navigator,null,crs);	//
		
		return dataSet;	// not PreparedStatement

//		-------------------------- drop by Zeven on 2008-09-19		
//		int rowCount = pageParams[0];
//		int pageSize = pageParams[1];
//		int pageNumber = pageParams[2];
//		
//		NavigableDataSet dataSet = new QueryDataSet();	//
//		dataSet.setPageSize( pageSize );
//		dataSet.setCurrentPageIndex( pageNumber );
//		
//		//String action = "query";
//		
//		//����ҳ����
//		String sql = null;
//		if ( rowCount<0 )
//		{
//			// �Ȳ������
//			sql = sql_count ;
//			log.debug("get rowCount and"
//					+"\n\tpageSize="  +pageSize
//					+"\n\tpageNumber="+pageNumber
//					+"\n\tsql="+sql);
//
//			//.out.println("-------------------- this -3------1 ");
//			RowSet rs=this.doQuery(sql);
//			if(rs.next()){
//				rowCount = rs.getInt(1);
//			}	
//		}
//		
//		// ��һ������ dataSet ����Ϣ
//		dataSet.setRowCount(rowCount);
//		dataSet.setPageCount( (rowCount - 1) / pageSize + 1 );
//		
//		//������ѯ
//		//������
//		//ȱʡ�Ĳ�ѯ
//
//		// �ٲ�ѯ���ݼ�
//		sql = this.createQuerySql(sql_datas, dataSet) ;
//		log.debug("get datas rowCount="+rowCount+" and \n\tsql="+sql );
//
//		dataSet.addData( doInnerQuery( sql) );
//		return dataSet ;
//		/// this.executeQuery(mapping, request, sql);
		
	}
 

	/**
	 *  <pre>���ݷ�ҳ��Ϣ���޸Ĳ�ѯ���ݵ� sql statement.
	 *  	������֧�ֵ����ݿ���ͳһ�Ĵ��� ��ͬ�����ݿ����ʹ�����ͬ��
	 *      navigator ������in/out���ͣ����е�ֵ���ܱ��޸ģ�������������Ϣ��
	 *      >>>ע�⣬����Ĳ��� (navigator,sql_count,sql_datas) �� doQueryDataSet(sql_datas,sql_count,navigator) �෴�ˡ�
	 *     --- ����Ϊprotected������ʹ��private����Ϊ�˱��ں������ĺ���ķ�ҳ���Ʒ���ʲôλ�á�</pre>
	 * @param sql ��װ֮ǰ�����ݲ�ѯ statement
	 * @param dataSet �������壬������ȡ������Ϣ
	 * @return ����ȡ����Ϣ���а�װ֮��� sql statement.
	 * @throws SQLException 
	 */
	protected String createSqlForPage(Navigator navigator, String sql_count, String sql_datas ) throws SQLException {

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

			rowCount = this.queryForInt( sql_count );	
			//pageParams[0] = rowCount;	// ����������
			navigator.setRowCount(rowCount);
		}

		DatabaseManager dbm = getCurrentDatabaseManager();
		String sql = dbm.getSubResultSetSql( sql_datas, pageSize*(pageNumber-1)+1,  pageSize  );

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
	 * �������ƴ���󱣴浽���ݿ�ı��С�
	 *   BLOB(Binary   Large   Object)   
	 *     �������洢�޽ṹ�Ķ��������ݡ���������row��long   row��
	 * </pre>
	 * 
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return
	 */	
	protected boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{
		
		DatabaseManager dbm = getCurrentDatabaseManager();
		if(this.conn==null || this.conn.isClosed()) {
			return dbm.updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
					null);
		}else {
			return dbm.updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
 					this.conn );
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
			   					String content) throws Exception{

		DatabaseManager dbm = getCurrentDatabaseManager();
		if(this.conn==null || this.conn.isClosed()) {
			return dbm.updateClobColumn(  tablename, 
					  picField, 
 					  sqlWhere,
 					  content,
 					  null );
		}else {
			return dbm.updateClobColumn(  tablename, 
					  picField, 
 					  sqlWhere,
 					  content,
 					  this.conn );
		}
		
	} 

	
	/**
	 * <pre>
	 * ��ȡ�ַ��ʹ��������ݡ�
	 * 
	 * </pre>
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected String getClobColumn(String sql) throws Exception{
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.getClobColumn( sql );	
	}

//	/**
//	 *  <p>���ݷ�ҳ��Ϣ���޸Ĳ�ѯ���ݵ� sql statement.
//	 *  	������֧�ֵ����ݿ���ͳһ�Ĵ��� ��ͬ�����ݿ����ʹ�����ͬ�� </p>
//	 * @param sql ��װ֮ǰ�����ݲ�ѯ statement
//	 * @param dataSet �������壬������ȡ������Ϣ
//	 * @return ����ȡ����Ϣ���а�װ֮��� sql statement.
//	 */
//	private String createQuerySql(String sql, NavigableDataSet dataSet) {
//
////		//String orderByString="";
////		sql =
////			"Select t.* " +
////			"  From (select Rownum As rowseq,t.* From ( "+sql+") t ) t " +
////			" WHERE rowseq BETWEEN "+(dataSet.getPageSize()*(dataSet.getCurrentPageIndex()-1)+1)+" AND "+dataSet.getPageSize()*(dataSet.getCurrentPageIndex());
////		log.debug("---"+sql);
//
//		sql = ((PooledConnection)conn).getDatabaseManager().getSubResultSetSql( sql, 
//							dataSet.getPageSize()*(dataSet.getCurrentPageIndex()-1)+1, 
//							dataSet.getPageSize()  );
//
//		return sql;
//	} 

	/** 
	 * <pre>
	 * ******************** ִ�в�ѯ�ɵ��õ��� ***************************
	 * doQuery �� Prepared ��ʽ��
	 * </pre>
	 * 
	 * @param sql	����?��sql���
	 * @param args	���?�Ĳ�����������
	 * @return
	 * @throws SQLException
	protected RowSet doQuery(String sql, Object[] args) throws SQLException {
		return (RowSet) query(sql, args, new RowSetResultSetExtractor() );
	}
	 */
	
	/**
	 * <pre>
	 * doQuery �� Prepared ��ʽ��
	 * </pre>
	 * 
	 * @param sql	����?��sql���
	 * @param args	���?�Ĳ�����������
	 * @param argTypes �������������Ӧ�Ĳ�������, ȡֵΪjava.sql.Type������
	 * @return
	 * @throws SQLException
	protected RowSet doQuery(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (RowSet) query(sql, args, argTypes, new RowSetResultSetExtractor() );
	}
	 */
		
	//map ������conn��֧�ֶ�dataSourceͬʱ����
	///protected Map<String,Connection> connsMap = new HashMap<String,Connection>(); 
	
	/** 
	 * <pre>
	 * ******************** ִ�в�ѯ������ִ���� ***************************
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rse
	 * @return
	 * @throws SQLException
	 */
	private Object query(String sql, Object[] args, int[] argTypes, ResultSetExtractor rse) throws SQLException {
		
		boolean isConnCreated = false;
		Connection l_conn = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION ); //���������ȼ�������
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : �������ݿ�������� -------------------------------------------------->

			log.debug("--------------wzw--2------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--2------------"+ ((l_conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = l_conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = l_conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();
		        stmt = ps; //���ں���رա�
			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
	        objReturn =  rse.extractData(rs);

			// begin : �������ݿ�������� --------------------------------------------------<
	        
		}finally{

			//DbUtils.closeQuietly( ps );
			if(isConnCreated){				// ��ѯ��ֻ��Ҫ�رգ�����Ҫ�ύor�ع�
				log.debug("-----------1---wzw--��Ҫ�ر�conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly(l_conn, stmt, rs);
			}else{
				log.debug("-----------1---wzw--����Ҫ�ر�conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly( null, stmt , rs);
			}
		}
		
        return objReturn;
	}

	

	/** 
	 * <pre>
	 *   ��ӦBean/BeanList�Ĳ�ѯ��Ϊʲô������һ���ײ㷽��������ҪĿ���ǿ���ʹ��һ���̶���
	 *  ResultSetExtractor��������������ϵͳ��ע�룬������Ҫÿ���½�һ�������ڴ��ݲ�����
	 *     
	 * ******************** ִ�в�ѯ������ִ���� ***************************
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rse
	 * @return
	 * @throws SQLException
	 */
	private Object queryBean(String sql, Object[] args, int[] argTypes, Class<?> beanClass,boolean isList) throws SQLException {  //ResultSetExtractor rse
		
		boolean isConnCreated = false;
		PreparedStatement ps = null;
		Connection l_conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			//�Ȳ����Ƿ�������������������������û��������this.conn��Ա����
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION ); //���������ȼ�������
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}

			log.debug("--------------wzw--2------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--2------------"+ ((l_conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = l_conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = l_conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();
		        stmt = ps; //���ں���رա�
			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
	        objReturn =  ApplicationContext.getInstance().getResultSetBeanExtractor().extractData(rs, beanClass, isList );

		}finally{

			//DbUtils.closeQuietly( ps );
			if(isConnCreated){				// ��ѯ��ֻ��Ҫ�رգ�����Ҫ�ύor�ع�
				log.debug("-----------1---wzw--��Ҫ�ر�conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly(l_conn, stmt, rs);
			}else{
				log.debug("--------------wzw--����Ҫ�ر�conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly( null, stmt , rs);
			}
		}
		
        return objReturn;
	}

	
	/** 
	 * <pre>
	 * ******************** ִ�в�ѯ������ִ���� ***************************
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param rse
	 * @return
	 * @throws SQLException
	 */ 
	@SuppressWarnings("unused")
	private Object query(String sql, Object[] args, ResultSetExtractor rse) throws DataAccessException, SQLException {
		return query(sql, args, null, rse);
	}
	

	/**
	 * <pre>
	 * ִ��ָ����sql statement��������ָ�������ͣ�
	 * 	�� Integer, Long, Float, Double, Timestamp, Date, Time�ȡ�
	 *  �� PreparedStatement��ʽ��
	 * </pre>
	 *
	 * @param sql sql statement
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	protected Object queryForType(String sql, int type ) throws SQLException {
		return queryForType(sql, null, null, type);
	}

	/**
	 * <pre>
	 * ִ��ָ����sql statement��������ָ�������ͣ�
	 * 	�� Integer, Long, Float, Double, Timestamp, Date, Time�ȡ�
	 *    PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param type java.sql.Tpyes �����ֵ����ʾ��������
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForType(String sql, Object[] args, int[] argTypes, int type ) throws SQLException {

		return query(sql, args, argTypes, new TypeResultSetExtractor(type, false) ); 
	}
	


	/**
	 * <pre>
	 * 	ִ��ָ����sql statement��������ָ�������͵�JavaBean����
	 *    ��PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql
	 * @param Class ���ض��������
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForBean(String sql, Class<?> beanClass ) throws SQLException {
		return queryForBean(sql, null, null, beanClass);
	}

	/**
	 * <pre>
	 * 	ִ��ָ����sql statement��������ָ�������͵�JavaBean����
	 *    PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param Class ���ض��������
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForBean(String sql, Object[] args, int[] argTypes, Class<?> beanClass ) throws SQLException {
		return queryBean(sql, args, argTypes, beanClass, false );  
	}	


	/**
	 * <pre>
	 * 	ִ��ָ����sql statement��������ָ����Map����
	 *    ��PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql
	 * @return ��һ�����ݷ�װ��Map����
	 * @throws SQLException
	 */
	protected Map<String, ?> queryForMap(String sql ) throws SQLException {
		return queryForMap(sql, null, null);
	}

	/**
	 * <pre>
	 * 	ִ��ָ����sql statement��������ָ����Map����
	 *    PreparedStatement��ʽ��
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return ��һ�����ݷ�װ��Map����
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, ?> queryForMap(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (Map<String, ?>) query(sql, args, argTypes, new MapResultSetExtractor(false) );  
	}

	//Ŀǰ��dataSourceName����ʼĬ��Ϊϵͳ�����Ĭ��ֵ��
	protected String currentDataSourceName = ApplicationContext.getInstance().getDataSourceManager().getDefaultDataSourceName();
	protected void setDataSourceName(String dataSourceName) {
		this.currentDataSourceName = dataSourceName;
		
	}
	public String getDataSourceName() {
		return this.currentDataSourceName;
	}
	private void setDataSourceNameToDefault() {
		this.setDataSourceName(ApplicationContext.getInstance().getDataSourceManager().getDefaultDataSourceName());		
	}
	
}

