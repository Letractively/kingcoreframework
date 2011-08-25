package com.kingcore.framework.context;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wzw.util.DbUtils;

/**
 * <p> MySQL ���ݿ�jdbc ���Բ�����ʵ���ࡣ</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class MySqlDatabase extends PlainDatabase {
	
	protected final static Logger log = Logger.getLogger( MySqlDatabase.class);


	/**
	 *  ������Ҫ��ѯ��ĳ�������У���װ��ǰ��sql statement and return.
	 * @param sql ԭsql statement
	 * @param offset ��ʼλ�ã���һ��Ϊ[1]
	 * @param row_count ��Ҫ��ȡ������
	 * @return ��װ֮��� sql statement
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count) {

		if(sql==null) {
			return null;
		}
		sql += " LIMIT "+ (offset-1) +"," + row_count ; 
		
		log.debug( sql );
		return sql ;
	}


	/**
	 * ���ļ����浽���ݿ�ı��С�
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return
	 */	
	public  boolean  updateBlobColumn(String Tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{

		return true ;
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
	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content,
			   					Connection conn) throws Exception{

		/// Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		String str_sql=null;
		//oracle.sql.CLOB clob=null;
		boolean isConnCreated = false;
		
		try{
			//��ʼ�����ݿ������
			if(conn==null){
				conn = DbUtils.getConnection();
				isConnCreated = true;
			}
			//conn.setAutoCommit(false);  //connΪConnection����

			//set clob column to empty first.
			str_sql="UPDATE " + tableName + " SET " +   picField +"=? "+ sqlWhere;
			pstmt = conn.prepareStatement( str_sql );
			pstmt.setString( 1, content );
			pstmt.executeUpdate();
			
			if(isConnCreated){	//�Լ����������Ӿ��ύ�����򲻴�����������
				conn.commit();
			}
			return true;
			
		}catch (Exception e){
			if(isConnCreated){	//�Լ����������Ӿͻع������򲻴�����������
				conn.rollback();
			}
			
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			log.info("SQL=" + str_sql + "\ncontent="+content );
			throw e;
			
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(conn, pstmt, rs);
			}else{
				DbUtils.closeQuietly(null, pstmt, rs);
				
			}
		}
	}


	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception {
		return updateClobColumn(tableName, 
					  picField, 
   					  sqlWhere,
   					  content,
   					  null);
	}
	
	/* (non-Javadoc)
	 * ����������
	 * @see com.kingcore.framework.context.DatabaseManager#getClobColumn(java.lang.String)
	 */
	public String getClobColumn(String sql) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		//oracle.sql.CLOB clob=null;
        //StringBuffer sb = new StringBuffer();
		
		try{
			//��ʼ�����ݿ������
			conn = DbUtils.getConnection();
			conn.setAutoCommit(false);  //connΪConnection����

			pstmt = conn.prepareStatement( sql );
			rs = pstmt.executeQuery(sql);
			if (rs.next())
			{
				//weblogic.jdbc.vendor.oracle.OracleThinClob clob = (weblogic.jdbc.vendor.oracle.OracleThinClob)rs.getClob( 1 );
				//Clob clb = rs.getClob( 1 );
				return rs.getString( 1 );
			}
			return "";

		}catch (Exception e){
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			DbUtils.closeQuietly(conn, pstmt, rs);			
		}
		
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#sysDatetime()
	 */
	public String sysDatetime() {
		return "now()";   // sysdate();
	}
	

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */
	public String switchNull(String exp1, String exp2) {
		return "coalesce(" +exp1+ "," +exp2+ ")";
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#date2Char(java.lang.String)
	 */
	public String date2Char(String colName) {
		return "DATE_FORMAT("+colName+",'%Y-%m-%d')";
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#datetime2Char(java.lang.String)
	 */
	public String datetime2Char(String colName) {
		return "DATE_FORMAT("+colName+",'%Y-%m-%d %H:%i:%s')";
	}


	/**
	 * just for MySQL,get id that insert last.
	 * 
     * @param conn ���ݿ����Ӷ��󣬲���Ϊnull��
	 * @return last insert id
	 */
	public int getLastInsertIdentity(Connection conn) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql=null;
		try {
			sql = "select last_insert_id()";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			int id = rs.getInt(1);
			return id;

		} catch (SQLException sqle) {
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
			log.info("Result in getLastInsertIdentity Exception'SQL is:\n" + sql);
        	throw sqle;
		} finally {
			DbUtils.closeQuietly(null, pstmt, rs);
		}
		
	}
	

	/**
	 * ���ݵ�ǰ�����ݿ������������Ҫ��������ݲ���������ʽ�����⴦��
	 * 		����ַ�������(Varchar)������(Number)������(Date)�Ͷ�����(LOB)���Ͳ����������
	 * 		-- ��ǰ����� Oracle ���ݿ⣬�� ' ���� �滻Ϊ '' �����ܲ��뵽���ݿ��С�
	 * 		-- ��� MySQL���ݿ⣬"'","\"��������ת���ַ�����Ҫ��Ϊ"\'","\\"
	 * <pre>
	 * escape2Sql("ab'cd")			="ab''cd"
	 * escape2Sql("ab'c'd")			="ab''c''d"
	 * escape2Sql("ab''cd")			="ab''''cd"
	 * </pre>
	 * @param src ��Ҫ���浽���ݿ��һ���ֶΡ�
	 * @return
	 */
//	public String convertString(String src) {
//		if(src==null) {
//			return null;
//		}
//		return StringUtils.replace( StringUtils.replace(src, "'", "''"), "\\","\\\\");
//	}

	public String escape2Sql(String src) {
		if(src==null) {
			return null;
		}
		return StringUtils.replace( StringUtils.replace(src, "'", "''"), "\\","\\\\");
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#identity(java.lang.String)
	 * Zeven on 2009-01-17: MySql�������⴦��Ϊ�˴��������⣬������ʵ�ʷ��ص���һ����ȡ�����ݿ�ֵ��������SqlƬ�Ρ�
	 *					 ��tsys_sequence������Ϊ MYISAM���͵ķ�ʽȡ���ˡ�
	 * 
	 */
	public String identity(String tblName) {
		
		Connection conn = null ;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	String sql = null;
    	
        try {
        	// �Լ���ȡһ�����ӣ����������Ӳ����Ƿ����
        	conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();
        	
        	sql = "Select getSequenceValue('"+tblName.trim()+"')";
        	log.debug(sql);
        	
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			conn.commit() ;				// �ύ����������

			if( rs.next() ) {
	        	int ti = rs.getInt(1);
				return String.valueOf(ti);	//Zeven on 2009-01-17
				
			}else {
				throw new SQLException("cann't get Sequence Value. sql statement is : " + sql);
			} 
			
        } catch (SQLException e) {
    		if( conn!=null ){
                try {
					conn.rollback();
				} catch (SQLException e1) {
		        	log.error("rollback exception'SQL is:\n" + sql);
				}
    		}
        	log.error("Result in Qurey Exception'SQL is:\n" + sql);
			log.debug("debug", e);
        	/// e.pri ntStackTrace();

        } finally {
            DbUtils.closeQuietly(conn, pstmt, rs);
            
        }		
        return null;
        
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println( StringUtils.replace( "abc'fd\\fds", "\\","\\\\") );
		MySqlDatabase mysql = new MySqlDatabase();
		String a = "a\\";
		System.out.println(" --- " + mysql.escape2Sql( a ));
		System.out.println(" --- " + mysql.escape2Sql( "abc'fd\\fds" ));
		System.out.println(" -- " + mysql.escape2Sql( "abc'fd\\f'''\\'\\''\\ds" ));

	}
	
}
