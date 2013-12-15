/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util;



import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.PlainConnection;

/**
 * <p>
 *    > DbUtils.java ���������ݿ�����ķ�������֧�ָ������񣬶��ǵ�����ġ�
 *   ���ݿ⹤���࣬�ṩ��ȡ���ݿ����ӵȹ��ܣ����������϶��Ǿ�̬��(static)��
 *		����Ҫ������ʵ���Ϳ��Ե��÷�����ͨ�õ����ݿ⹤�ߣ����������ݿ����͡�
 *  ������е����ݿ��������������Դ���������ô�Ϳ�����ȫ�������������ݿ⡣
 *    ӳ����Э����
 *    	org.apache.commons.dbutils.DbUtils
 *    				.closeQuietly ;
 *    	org.apache.commons.dbutils.BeanProcessor
 *    				.toBean ;
 *    				.toBeanList ;
 *  
 *  	org.apache.commons.dbutils.handlers Classes  
 *  		ArrayHandler 
 *  		ArrayListHandler 
 *  		BeanHandler 
 *  		BeanListHandler 
 *  		ColumnListHandler 
 *  		KeyedHandler 
 *  		MapHandler 
 *  		MapListHandler 
 *  		ScalarHandler 
 *  	
 *  
 *   DBUtils �ķ������������棺
 *   	1���������ݿ�ͨ�õ�jdbc������DBUtils�Լ�ʵ�֣� 
 *   	2���������ݿ����еĲ������� ApplicationContext.getInstance().getDatabaseManager() �����ṩ��
 *  
 *	  ���ݿ⹤��������������أ�
 *		1������Դ�ṩ�����ߣ�
 *		2�����ݿ���������ͣ�
 *	   ��Щ��ص�����������ע��ķ�ʽ�����������ԡ�</p>
 *
 * @author	WUZEWEN on 2004-09-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 * 
 **/

public class DbUtils {

	
//    /**
//     * ȱʡ��DataSource ��JNDI�����ơ�
//     */
//    //public static String JNDI_DATASOURCE="jndi/jdbc";

    /**
     * log4j��־����
     */
    protected static Logger log=Logger.getLogger(wzw.util.DbUtils.class);

    /**
	 *	���캯����
	 */
	public DbUtils() {
	}

	/**
     * <p>��ȡ���ݿ����ӣ�ʹ�õ�ǰȱʡ������ ��
     * <UL>
     *    <LI>��jndi�л�ȡ��DataSource�ٻ��Connection����
     *    <LI>��һ����ģʽ����̬������ȡDataSource�ٻ��Connection����
     *    <LI>��scope=application�л�ȡ��DataSource�ٻ��Connection����
     * </UL></p>
     * @author WUZEWEN on 2005-07-24
     * @return conn һ��ʵ����Connection�ӿڵĶ���
     * @exception SQLException
     */
	public static Connection getConnection()
				throws SQLException {
		//log.debug("----------getConnection DBUtils 101."+ApplicationContext.getInstance().getDataSourceProvider().getClass().toString());
    	log.debug( "--0--2");
		return ApplicationContext.getInstance().getDataSourceManager().getConnection();
	 
	}
	
	/**
     * <p>����ָ�������֣���ȡ���ݿ����ӣ�֧�����Ӷ������Դ��
     * 		�����ƿ����������ṩ�� DataSource ��jndi ���ƣ�Ҳ������ ConnectionPool �����ơ�
     * <UL>
     *    <LI>��jndi�л�ȡ��DataSource�ٻ��Connection����
     *    <LI>��һ����ģʽ����̬������ȡDataSource�ٻ��Connection����
     *    <LI>��scope=application�л�ȡ��DataSource�ٻ��Connection����
     * </UL></p>
     * 
	 * @param sourceName Դ��DataSource ��jndi ���ƣ������� ConnectionPool ������
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String dataSourceName)
				throws SQLException {

		return ApplicationContext.getInstance().getDataSourceManager().getConnection(dataSourceName);
	}


	
    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for connection cleanup.
     *
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
	
    public static RowSet executeQuery(String sql)
     throws SQLException {

    	Connection conn = null ;
    	
        try {

        	log.debug(sql+"--0--1");
            conn = getConnection();		// �����ȡ�����������ύ/�ع����رա�
            return executeQuery(sql, conn);

        } finally {
        	try{
        		if(conn!=null)
        			conn.close();
        		
            }catch(SQLException e)
            {
	            log.fatal("��ִ��DBUtils.doQuery����������ϢΪ��\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }          
        }
	}
    
    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for connection cleanup.
     *
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
	public static RowSet executeQuery(String sql, Connection conn)
      throws SQLException {

    	log.debug(sql+"--1"); 
    	//�������
    	if( conn==null || conn.isClosed()){	
    		throw new SQLException("Connection Object is null or is closed!");
    	}

    	log.debug(sql+"--2");
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	RowSet crs= null;

        try {
        	log.debug(sql);

            /// conn = getConnection();
        	pstmt = conn.prepareStatement( sql);
            rs = pstmt.executeQuery();
            //crs.populate( rs ) ;
            crs = resultSet2RowSet(((PlainConnection)conn).getDatabaseManager(),  rs ) ;

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.debug("Result in Qurey Exception'SQL is:\n" + sql, e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	
        	DbUtils.closeQuietly(null, pstmt, rs);
        	
        }

        return crs;
	}
	
	
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters�����ұ����ṩ������,�����������д��û�е��� doUpdate(List list).
     *
     * @param sql The SQL to execute
     * @return The number of rows updated
     * @throws SQLException ���ݿ�����쳣 
     */  
    public static int executeUpdate( String sql ) throws SQLException {

        //return doUpdate(sql, null);
    	
    	Connection conn = null; 

        try {
        	conn = getConnection(); //��ȡ���ύ���ع����ر� ����һ���ط�ͳһ���ƣ�
        	int val = executeUpdate(sql, conn);
        	conn.commit();
            
        	return val;
            //log.debug("doUpdate commit success!");

        } catch(SQLException sqle){
        	DbUtils.rollbackQuietly(conn);
//        	conn.rollback();
        	throw sqle ;
        	
    	} finally {
            try{
                if(conn!=null)
                    conn.close() ;

            }catch(SQLException e)
            {
                log.fatal("��ִ��DBUtils.doUpdate() ����Exception����������ϢΪ��\n", e);
				/// log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
        }
        //return i_returns;    
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters�����ұ����ṩ������,�����������д��û�е��� doUpdate(List list).
     *
     * @param conn The connection to use to run the query
     * @param sql The SQL to execute
     * @return The number of rows updated
     * @throws SQLException ���ݿ�����쳣
     */  
    public static int executeUpdate( String sql ,Connection conn) throws SQLException {    	 

    	//�������
    	if( conn==null || conn.isClosed()){	
    		throw new SQLException("Connection Object is null or is closed!");
    	}
    	
        PreparedStatement pstmt = null;
        int i_returns;
        try {
        	/////conn = getConnection();  //��ȡ���ύ���ع����ر� ����һ���ط�ͳһ���ƣ�
            pstmt = conn.prepareStatement( sql);
            i_returns = pstmt.executeUpdate();
            //log.debug("pstmt.executeUpdate success!");
            return i_returns;

        } catch (SQLException e) {
            log.fatal( "Result in update Exception'SQL is:\n"+sql + ". Message:" + e.getMessage(), e ) ;
			/// log.debug("debug", e);
            /// e.pri ntStackTrace();
            /// System.out.println("Result in update Exception'SQL is:\n"+sql );
            throw e;
            //this.rethrow(e, sql, list);

        } finally {
            try{
                if(pstmt!=null)
                    pstmt.close() ;
            }catch(SQLException e)
            {
                log.fatal("��ִ��DBUtils.doUpdate() ����Exception����������ϢΪ��\n", e);
				///log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
        }
        //return i_returns;        
    }
	

    /**
     * ִ�����������ұ��������������.
     *
     * @param allsql Ҫִ�е�sql�����ɵ����顣
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public static int[] executeBatch( List<String> list )
      throws SQLException {

    	//return doBatch(list, null);
    	
    	Connection conn = null;
        try {
        	conn = getConnection();  //��ȡ���ύ���ع����ر� ����һ���ط�ͳһ���ƣ�
        	
            int ret[] = executeBatch(list, conn);	// �����ڲ�����
            
            conn.commit();
            return ret;
        } catch(SQLException e){
        	conn.rollback();
        	throw e;
        	
        } finally {
            try{
                if(conn!=null)
                    conn.close() ;

            }catch(SQLException e)
            {
                log.fatal("��ִ��DBUtils.doBatch() ����������ϢΪ��\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
        }
        // return null;
    }

    /**
     * ִ�����������ұ��������������.
     * @param conn ���ݿ����Ӷ���
     * @param allsql Ҫִ�е�sql�����ɵ����顣
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public static int[] executeBatch( List<String> list, Connection conn )
        throws SQLException {

    	//�������
    	if( conn==null || conn.isClosed()){	
    		throw new SQLException("Connection Object is null or is closed!");
    	}
    	
        Statement stmt = null;
        int returns[];
        boolean isConnCreated = false;
        try {
        	if( conn==null || conn.isClosed()){		// �����ܵ��������������д����
            	conn = getConnection();  //��ȡ���ύ���ع����ر� ����һ���ط�ͳһ���ƣ�
            	isConnCreated = true;
        	}
        	
        	stmt = conn.createStatement();
            addBatch( stmt, list);
            returns = stmt.executeBatch();
            
            if( isConnCreated ){
            	conn.commit() ;	 	//�����ⲿ��������ӣ����ύ�����ع������ر�
            }
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            if( isConnCreated ){
            	conn.rollback(); 	 //�����ⲿ��������ӣ����ύ�����ع������ر�
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
            try{
                if(stmt!=null)
                    stmt.close() ;
            }catch(SQLException e)
            {		
                log.fatal("��ִ��DBUtils.doBatch() ����������ϢΪ��\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
            
            try{
                if(isConnCreated && conn!=null)
                    conn.close() ; 	 //�����ⲿ��������ӣ����ύ�����ع������ر�

            }catch(SQLException e)
            {		
                log.fatal("��ִ��DBUtils.doBatch() ����������ϢΪ��\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
        }
        // return null;
    }

    
    /**
     * ���ݱ����ƺͲ�ѯ��������ȡ�������������ݵ�������
     *
     * @param tableName Ҫ��ѯ�ı�����
     * @param condition �����������硰WHERE numCol>100��
     * @return ��������������
     * @throws SQLException ���ݿ�����쳣
     */
	public static int getSize(String tableName, String condition) throws SQLException {
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			return getSize(tableName, condition, conn);

		} finally {
            try{
				if(conn!=null) {
					conn.close() ;
				}
            }catch(SQLException e)
            {		
                log.fatal("��ִ��DBUtils.getSize() ����Exception����������ϢΪ��\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
	}

	
    /**
     * ���ݱ����ƺͲ�ѯ��������ȡ�������������ݵ�������
     *
     * @param conn ���ݿ����Ӷ���
     * @param tableName Ҫ��ѯ�ı�����
     * @param condition �����������硰WHERE numCol>100��
     * @return ��������������
     * @throws SQLException ���ݿ�����쳣
     */
	public static int getSize(String tableName, String condition, Connection conn) throws SQLException {
		
		/// Connection conn = null;
		/// PreparedStatement pstmt = null;
		/// ResultSet rs = null;
		String sql=null;
		sql = "SELECT count(*) FROM "+tableName+" "+condition;
		return queryForInt(sql, conn);
	
	}
	

	/**
	 * ���� Spring �� queryForInt ������ִ��һ��ָ������䲢����һ��int���ݡ�
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int queryForInt(String sql) throws SQLException {
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			return queryForInt(sql, conn);

		} finally {
            try{
				if(conn!=null) {
					conn.close() ;
				}
            }catch(SQLException e)
            {		
                log.fatal("��ִ��DBUtils.queryForInt() ����Exception����������ϢΪ��\n", e);
				log.debug( e.getMessage(), e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
	}

	public static int queryForInt(String sql, Connection conn) throws SQLException {
		
		/// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			/// conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				throw new SQLException("cann't queryForInt(), sql statement is : " + sql);
			}
			
		} catch (SQLException sqle) {
			log.info( "Result in getSize Exception'SQL is:\n" + sql );
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
        	throw sqle;
        	
		} finally {

        	DbUtils.closeQuietly(null, pstmt, rs);
		}
	}
	

	/**
	 * ���� Spring �� queryForLong ������ִ��һ��ָ������䲢����һ��long���ݡ�
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static long queryForLong(String sql) throws SQLException {
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			return queryForLong(sql, conn);

		} finally {
            try{
				if(conn!=null) {
					conn.close() ;
				}
            }catch(SQLException e)
            {		
                log.fatal("��ִ��DBUtils.queryForInt() ����Exception����������ϢΪ��\n", e);
				log.debug( e.getMessage(), e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
	}

	public static long queryForLong(String sql, Connection conn) throws SQLException {
		
		/// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			/// conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}else {
				throw new SQLException("cann't queryForLong(), sql statement is : " + sql);
			}
			
		} catch (SQLException sqle) {
			log.info( "Result in getSize Exception'SQL is:\n" + sql );
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
        	throw sqle;
        	
		} finally {

        	DbUtils.closeQuietly(null, pstmt, rs);
		}
	}
	
	
    /**
     * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��for Oracle Only��
     * 
     * @param seqName ���ж�������
     * @return ���е���һ��ֵ
     * @throws SQLException ���ݿ����ʧ���쳣��
     */
	public static int getSequenceValue( String seqName) throws SQLException {

    	Connection conn = null;
        try {
        	conn = getConnection();
        	return getSequenceValue(seqName, conn);

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.info("Result in get Sequence Exception'SQL is:\nSELECT "+seqName+".Nextval FROM DUAL");
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	try{
        		if(conn!=null)
        			conn.close();
            }catch(SQLException e)
            {
	            log.fatal("��ִ��DBUtils.getSequenceValue()����������ϢΪ��\n", e);
	            log.debug(e.getMessage(), e);
            	/// e.pri ntStackTrace();
            }
        } 
	}
	

    /**
     * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��for Oracle Only��
     * 
     * @param conn ���ݿ����Ӷ���
     * @param seqName ���ж�������
     * @return ���е���һ��ֵ
     * @throws SQLException ���ݿ����ʧ���쳣��
     */
	public static int getSequenceValue( String seqName, Connection conn ) throws SQLException {
	   	
		return new Long(((PlainConnection)conn).getDatabaseManager().getIdentityValue( seqName, conn)).intValue() ; //��ʱ����
    	//return new Long(ApplicationContext.getInstance().getDatabaseManager().getIdentityValue( seqName, conn)).intValue(); //��ʱ����
	}


    /**
     * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��for Oracle Only��
     * 
     * @param tblName ��Ҫʹ��id�ı������
     * @return ���е���һ��ֵ
     * @throws SQLException ���ݿ����ʧ���쳣��
     */
	public static long getIdentityValue( String tblName) throws SQLException {

		return getIdentityValue(tblName, null);
//    	Connection conn = null;
//        try {
//        	conn = getConnection();
//        	conn.setAutoCommit( true );		// �Զ��ύ��������
//        	return getIdentityValue(tblName, conn);
//
//        } catch (SQLException e) {
//            //this.rethrow(e, sql, params);
//        	e.pri ntStackTrace();
//        	/// System.out.println("Result in get Sequence Exception'SQL is:\nSELECT "+seqName+".Nextval FROM DUAL");
//        	throw e;
//
//        } finally {
//        	try{
//        		if(conn!=null) {
//        			conn.setAutoCommit( false );
//        			conn.close();
//        		}
//            }catch(SQLException e)
//            {
//            	e.pri ntStackTrace();
//	            log.fatal("��ִ��DBUtils.getSequenceValue()����������ϢΪ��\n", e);
//            }
//        } 
	}
	

    /**
     * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��for Oracle Only��
     * 
     * @param conn ���ݿ����Ӷ���
     * @param tblName ��Ҫʹ��id�ı������
     * @return ���е���һ��ֵ
     * @throws SQLException ���ݿ����ʧ���쳣��
     */
	public static long getIdentityValue( String tblName, Connection conn ) throws SQLException {
	   	
		return ((PlainConnection)conn).getDatabaseManager().getIdentityValue( tblName, conn);
    	//return ApplicationContext.getInstance().getDatabaseManager().getIdentityValue( tblName, conn);
	}
	
	/**
	 * ��һ��List���϶����е�����SQL���װ�ص� Statement�С�
	 * @param stmt ʵ����Statement�ӿڵĶ���
	 * @param list ʵ����List�ӿڵĶ���
	 * @throws SQLException
	 */
	public static void addBatch(Statement stmt, List<String> list) throws SQLException{
		if(list==null){
			return ;
		}
		for(int i=0;i<list.size();i++){
            stmt.addBatch(list.get(i));  //.toString()
        }
	}

	/**
	 * ���ݵ�ǰ�����ݿ������������Ҫ��������ݲ���������ʽ�����⴦��
	 * 		����ַ�������(Varchar)������(Number)������(Date)�Ͷ�����(LOB)���Ͳ����������
	 * 		-- ��ǰ����� Oracle ���ݿ⣬�� ' ���� �滻Ϊ '' �����ܲ��뵽���ݿ��С�
	 * <pre>
	 * DBUtils.escape2Sql("ab'cd")			="ab''cd"
	 * DBUtils.escape2Sql("ab'c'd")			="ab''c''d"
	 * DBUtils.escape2Sql("ab''cd")			="ab''''cd"
	 * </pre>
	 *   ���飺����������ʹ��DBUtils���ã�����ʹ��DAO, DBBean������û���ֱ��ͨ��DatabaseManager������á�
	 * @param src ��Ҫ���浽���ݿ��һ���ֶΡ�
	 * @return
	 */
	public static String escape2Sql(DatabaseManager databaseManager, String src ) {
		return databaseManager.escape2Sql(src);
		//return ApplicationContext.getInstance().getDatabaseManager().escape2Sql(src);
	}
	
	
	/**
	 * populate ResulteSet Object to RowSet Object, all implements below:  
	 *   Oracle 10i:  oracle.jdbc.rowset.OracleCachedRowSet
	 *	 MS SQL 2000: sun.jdbc.rowset.CachedRowSet
	 *	 Access 2000: sun.jdbc.rowset.CachedRowSet
	 *
	 *	  Zeven on 2007-06-06, this static method manager which RowSet's implement used by System.
	 *		It's safe for mutil thread case.
	 *		
	 *		���ݵ�ǰϵͳ���õ����ݿ����͹��� RowSet ����
	 *		֧�ֶ����ݿ����ͨ���ݵ� ResultSet to RowSet ����
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static RowSet resultSet2RowSet(DatabaseManager databaseManager, ResultSet rs ) throws SQLException {
		//ApplicationContext.getInstance().getDatabaseManager()
		//return resultSet2RowSet(rs, databaseManager );
		return databaseManager.resultSet2RowSet( rs );
	}
	
	/**
	 *  ���ݴ�������ݿ����͹�����Ӧ�� RowSet �����Խ�������п������Ӷ���������ݵ����⣬
	 *  	������Servlet Container �� Web ����Ͳ������ж���������
	 * @deprecated ��ʹ�� resultSet2RowSet(DatabaseManager databaseManager, ResultSet rs )
	 * @param rs ��Ҫװ�� RowSet �� ResultSet ��������
	 * @param dbms_type ö��ֵ���ο� com.kingcore.framework.Constants.DBMS_Type_...
	 * @return
	 * @throws SQLException
	 * @see com.kingcore.framework.Constants.DBMS_Type_...
	 */
//	public static RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm) throws SQLException {
//		return dbm.resultSet2RowSet( rs );
//	}
	public static RowSet resultSet2RowSet(ResultSet rs ) throws SQLException {
		DatabaseManager dbm = ApplicationContext.getInstance().getDatabaseManager();
		return dbm.resultSet2RowSet( rs );
	}

	/**
	 * �������ƴ���󱣴浽���ݿ�ı��С�
	 *   BLOB(Binary   Large   Object)   
	 *     �������洢�޽ṹ�Ķ��������ݡ���������row��long   row��
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return
	 */	
	public static boolean  updateBlobColumn(Connection conn,String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{
		
		//ApplicationContext.getInstance().getDatabaseManager()
		return ((PlainConnection)conn).getDatabaseManager().updateBlobColumn(tablename,
																			picField,
																			sqlWhere,
																			strPath);
	}
	

	/**
	 * ���ַ��ʹ���󱣴浽���ݿ�ı��С�
	 *   CLOB(Character   Large   Object)   
	 *     ���ڴ洢��Ӧ�����ݿⶨ����ַ������ַ����ݡ���������long���ͣ�   
	 *      
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param Content Ҫ�������ݿ������
	 * @return
	 */	
	public static boolean updateClobColumn(Connection conn, String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception{
		//ApplicationContext.getInstance().getDatabaseManager()
		return ((PlainConnection)conn).getDatabaseManager().updateClobColumn( tablename, 
														   					picField, 
														   					sqlWhere,
														   					content);
	}

	/**
	 * ��ȡ�ַ��ʹ��������ݡ�
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static String getClobColumn(DatabaseManager databaseManager, String sql) throws Exception{
		//ApplicationContext.getInstance().getDatabaseManager()
		return databaseManager.getClobColumn( sql );	
	}
	
	// begin ********************************************************************
	// like org.apache.commons.dbutils.DbUtils.class
	//   �� closeQuietly �Ĳ�֮ͬ�����ڻ����̨�����Ϣ��ͬʱҲ��log4j��Ϣ�����
	// **************************************************************************
    public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs)
    {
    	
    	closeQuietly(rs);
    	closeQuietly(stmt);
    	closeQuietly(conn);
    }

    public static void closeQuietly(ResultSet rs)
    {
        try
        {
        	if(rs != null)
	        {
	            rs.close();
	        }
        }
        catch(SQLException sqle) {
        	log.info("�����rs�ر�ʱ�����쳣����Ϣ��"+ sqle.getMessage(), sqle );
			/// log.debug("debug", sqle );
        	/// sqle.pri ntStackTrace();
        }
    }

    public static void closeQuietly(Statement stmt)
    {
        try
        {
        	if(stmt != null)
	        {
        		stmt.close();
	        }
        }
        catch(SQLException sqle) {
        	log.info("stmt�ر�ʱ�����쳣����Ϣ��"+ sqle.getMessage(), sqle );
			/// log.debug("debug", sqle );
        	/// sqle.pri ntStackTrace();
        }
    }
    
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
     * Connection �ع������׳��쳣��
     * @param conn
     */
    public static void rollbackQuietly(Connection conn)
    {
        try
        {
        	if(conn != null && !conn.isClosed())  // �����ظ��ر��쳣
	        {
        		conn.rollback();
	        }
        }
        catch(SQLException sqle) {
        	log.info("���ݿ�����conn�ع�ʱ�����쳣����Ϣ��"+ sqle.getMessage(), sqle );
        }
    }

    /**
     * Connection �ύ�����׳��쳣��
     * @param conn
     */
    public static void commitQuietly(Connection conn)
    {
        try
        {
        	if(conn != null && !conn.isClosed())  // �����ظ��ر��쳣
	        {
        		conn.commit();
	        }
        }
        catch(SQLException sqle) {
        	log.info("���ݿ�����conn�ύʱ�����쳣����Ϣ��"+ sqle.getMessage(), sqle );
        }
    }
    
    
    /**
     * Connection �رգ����׳��쳣��
     * @param conn
     */
    public static void closeQuietly(Connection conn)
    {
        try
        {
        	log.debug(conn != null?"not null":"is null"); //
		
        	if(conn != null && !conn.isClosed())  // �����ظ��ر��쳣
	        {
        		log.debug("���ӹر�ʱhashcode="+conn.hashCode()+""+"������·����"+showTrace(8) ); //
        		//log.debug(conn.isClosed()?"---a---conn isClosed":"conn not closed");
        		//log.debug(conn.getClass());
        		conn.close();
        		//log.debug(conn.isClosed()?"---b---conn isClosed":"conn not closed");
        		
	        }
        }
        catch(SQLException sqle) {
        	log.info("���ݿ�����conn�ر�ʱ�����쳣����Ϣ��"+ sqle.getMessage(), sqle );
			/// log.debug("debug", sqle );
        	/// sqle.pri ntStackTrace();
        }
    }

	// end **********************************************************************
	// like org.apache.commons.dbutils.DbUtils.class
	// **************************************************************************
	

    /**
     * ��ȡ���ݿ����ӡ�
     * <UL>
     *    <LI>��scope=application�л�ȡ��DataSource�ٻ��Connection����
     * </UL>
     * @author WUZEWEN on 2005-07-24
     * @deprecated ����ʹ�������� jndi ������ �Զ���� ConnnectionPool �Ļ�ȡ���ݿ����ӣ��������application�����л�ȡ��
     * @param  request:������󣬴�scope=application��ȡconnר��
     * @return Connection conn:һ��ʵ����Connection�ӿڵĶ���
     * @exception no exception 
     */
	public static Connection getConnection(javax.servlet.http.HttpServletRequest request )
							throws SQLException {
		//o1.get DataSource from scope=application
		DataSource datasource = null;
        Connection conn = null;

        String DATA_SOURCE_KEY_IN_STRUTS1 = "org.apache.struts.action.DATA_SOURCE";
		datasource = (DataSource)HttpUtils.getObjectInApplication( request, DATA_SOURCE_KEY_IN_STRUTS1 ) ;
		if(datasource!=null)	//��ʹ��ǰһ��Ҫ��null�ж�
			conn = datasource.getConnection();
		//if cann't find ds in application,get it in jndi...
		if(conn==null){
			conn = getConnection() ;   //���ﲻ����this.getConn
		}

		return conn;
	}

	// ʹ������������Ϊ�˽ӿ���ʵ�ַ��롣 ͬʱ����ǿ�������ע��������õġ�
	private static final String Database_Name_PlainDatabase  = "com.kingcore.framework.context.PlainDatabase";
	private static final String Database_Name_OracleDatabase = "com.kingcore.framework.context.OracleDatabase";
	private static final String Database_Name_MySqlDatabase  = "com.kingcore.framework.context.MySqlDatabase";
	
	public static String getDatabaseManagerNameByDriver(String url) {
		if(url==null)
			return null;
		url = url.toLowerCase();
		String className = null;
		if(url.indexOf("sun.jdbc.odbc")>-1){
			className = Database_Name_PlainDatabase;
		}else if(url.indexOf("oracle")>-1){
			className = Database_Name_OracleDatabase;
		}if(url.indexOf("mysql")>-1){
			className = Database_Name_MySqlDatabase;
		}
		return className;
	}

	public static void main(String[] args) throws URISyntaxException {
		java.net.URI u = new java.net.URI("");
		u.getQuery();
		System.out.println(  DbUtils.escape2Sql(null, "a'b'c") );
		
	}
}
