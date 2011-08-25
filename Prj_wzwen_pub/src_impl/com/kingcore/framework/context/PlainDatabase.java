package com.kingcore.framework.context;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.RowSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wzw.util.DbUtils;

import com.sun.rowset.CachedRowSetImpl;

/**
 * <p>ȱʡ�� DatabaseManager ʵ�֣����Ա��̳С�</p>
 * @author Zeven on 2007-6-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class PlainDatabase implements DatabaseManager {

	/**
	 * �������־����
	 */
	protected final static Logger log = Logger.getLogger( PlainDatabase.class);


	
	/**
	 *  <p>�ṩ���ݿ�ͨ�õĻ�ȡ����ֵ����.
	 *  	Ҫ�����ݿ��ṩ�� getSequenceValue( tblName )�ĺ�����
	 *  	���ڲ��ñ��¼���еķ�ʽ��Ҫ�󵥶��ύ�����Զ�����ȡconn,��ʹ������Ҳ��ʹ��conn����������һʧ��
	 *  	���ݱ�����ȡ�����ǰ���кţ����Ҫ����һ����ͬʱʹ��getSequenceValue ������ȡֵ����ṹ���£�
	 *  <pre>
	 *  -- create table, use MySQL as example.
	 *  use mysql ;
	 *  drop table Tsys_Sequence ;
	 *  CREATE TABLE Tsys_Sequence (
  	 *  	Table_Name  varchar(60) NOT NULL,
  	 *  	Next_Value  bigint(20) not null,
  	 *  	PRIMARY KEY  (`Table_Name`)
	 *  );
	 *
	 * -- init
	 * delete from Tsys_Sequence where table_name='employee'; 
	 * insert into Tsys_Sequence (table_name,next_value) values('employee',1);
	 * 
	 * -- test
	 * select getSequenceValue('employee') ; 
	 * insert into employy(id, name) values( getSequenceValue('employee'), 'Mike') ;
	 * 
	 *  </pre>
	 *  	You can override this method in subclass.</p>
	 *  
	 * @param tblName ��Ҫʹ�����еı�Ψһ������Ϊnull
	 * @param conn ���ݿ����Ӷ��󣬿���Ϊnull��ʵ��������û��ʹ��������������ǵ�������Connection����
	 * @return ��ǰ��������ֵ
	 */
	
	public long getIdentityValue(String tblName, Connection p_conn ) throws SQLException {
		
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
				return ti;
				
			}else {
				throw new SQLException("cann't get Sequence Value. sql statement is : " + sql);
			} 

        } catch (SQLException e) {
    		if( conn!=null ){
                conn.rollback();
    		}
        	//this.rethrow(e, sql, params);
        	log.error("Result in Qurey Exception'SQL is:\n" + sql);
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	DbUtils.closeQuietly(conn, pstmt, rs);
        }
        
	}

	
	/**
	 * @deprecated  ����ʹ�� getIdentityValue �滻��������
	public int getSequenceValue(String tblName, Connection conn) throws SQLException {
		return new Long( getIdentityValue( tblName, conn )).intValue(); // ��ʱ����
		
	}
	 */
	
	/**
	 * <p>���ݵ�ǰ��ѯ��SQL ��䣬����һ����ȡ���в��������е�SQL��䣬һ���ڷ�ҳ��ѯ��ʹ�á�</p>
	 * 
	 * @param sql ԴSQL���
	 * @param offset ��ȡ�жε���ʼλ�ã���0��ʼ
	 * @param row_count ��Ҫ��ȡ������
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 *
	 * <p>����Sun��ʵ����Ϊȱʡ�Ĵ��� </p>
	 * @see com.kingcore.framework.context.DatabaseManager#resultSet2RowSet(java.sql.ResultSet)
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException {

		// ����Sun��ʵ����� Access2000��MySQL5 �� ���ݿ�
		CachedRowSetImpl crs= new CachedRowSetImpl();
		crs.populate( rs );	
		
		return crs ;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#updateBlobColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateBlobColumn(String tablename, String picField,
			String sqlWhere, String strPath, Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#updateBlobColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateBlobColumn(String tablename, String picField,
			String sqlWhere, String strPath) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#updateClobColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */

	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content,
			   					Connection conn) throws Exception{

		///Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		String str_sql=null;
		//oracle.sql.CLOB clob=null;
		
		try{
			//��ʼ�����ݿ������
			conn = DbUtils.getConnection();
			//conn.setAutoCommit(false);  //connΪConnection����

			//set clob column to empty first.
			str_sql="UPDATE " + tableName + " SET " +   picField +"=? "+ sqlWhere;
			pstmt = conn.prepareStatement( str_sql );
			pstmt.setString( 1, content );
			pstmt.executeUpdate();

			conn.commit();
			return true;
			
		}catch (Exception e){
			conn.rollback();
			log.error("SQL=" + str_sql + "\ncontent="+content );
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			DbUtils.closeQuietly(conn, pstmt, rs);
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
	 * @see com.kingcore.framework.context.DatabaseManager#getClobColumn(java.lang.String)
	 */
	public String getClobColumn(String sql) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		//oracle.sql.CLOB clob=null;
        ///StringBuffer sb = new StringBuffer();
		
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


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PlainDatabase pdb = new PlainDatabase();
		System.out.println("" + pdb.char2Date("2007-01-01"));
		System.out.println("" + pdb.date2Char("beginTime"));
		System.out.println("" + pdb.char2Datetime("2007-01-01 12:15:10"));
		System.out.println("" + pdb.datetime2Char("beginTime"));


	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#date2Char(java.lang.String)
	 */
	public String date2Char(String colName) {
		return colName;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#char2Date(java.lang.String)
	 */
	public String char2Date(String colValue) {
		return "'" + colValue + "'" ;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#datetime2Char(java.lang.String)
	 */
	public String datetime2Char(String colName) {
		return colName;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#char2Datetime(java.lang.String)
	 */
	public String char2Datetime(String colValue) {
		return "'" + colValue + "'" ;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#sysDatetime()
	 */
	public String sysDatetime() {
		return "now()";   // sysdate();
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#identity(java.lang.String)
	 */
	public String identity(String tblName) {
		return "getSequenceValue('"+tblName+"')";
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#concat()
	 */
	public String concat(String str1, String str2) {
		return "concat("+ str1 + "," + str2 +")";
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */

	public String switchNull(String exp1, String exp2) {
		return "isNull(" +exp1+ "," +exp2+ ")";
	}

	/**
	 * �����Ҫʹ�ã���������Ҫ�������е���ʵ�֡�
	 */
	public int getLastInsertIdentity( Connection conn  ) throws SQLException {
		return 0;
	}


	/**
	 * ���ݵ�ǰ�����ݿ������������Ҫ��������ݲ���������ʽ�����⴦��
	 * 		����ַ�������(Varchar)������(Number)������(Date)�Ͷ�����(LOB)���Ͳ����������
	 * 		-- ��ǰ����� Oracle ���ݿ⣬�� ' ���� �滻Ϊ '' �����ܲ��뵽���ݿ��С�
	 * 		-- ��� MySQL���ݿ⣬"'","\"��������ת���ַ�����Ҫ��Ϊ"\'","\\"
	 * <pre>
	 * escape2Sql("ab'cd")			="ab''cd"
	 * escape2Sql("ab'c'd")			="ab''c''d"
	 * escape2Sql("ab''c\\d")			="ab''''c\\\\d"
	 * </pre>
	 * @param src ��Ҫ���浽���ݿ��һ���ֶΡ�
	 * @return
	 */
	public String escape2Sql(String src) {
		if(src==null) {
			return null;
		}
		
		// ���Բο� return wzw.lang.Escaper.escape2Sql( src );		
		return StringUtils.replace(src, "'", "''");
	}

}

