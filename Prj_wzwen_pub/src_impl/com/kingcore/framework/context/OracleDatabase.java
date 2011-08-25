package com.kingcore.framework.context;

 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.RowSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wzw.util.DbUtils;

/**
 * <p>Oracle ���ݿ�jdbc���������ʵ���ࡣ</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class OracleDatabase implements DatabaseManager {

	/**
	 * ��־���� final ��
	 */
	protected static Logger log = Logger.getLogger( OracleDatabase.class);


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OracleDatabase odb = new OracleDatabase();
		System.out.println("" + odb.char2Date("2007-01-01"));
		System.out.println("" + odb.date2Char("beginTime"));
		System.out.println("" + odb.char2Datetime("2007-01-01 12:15:10"));
		System.out.println("" + odb.datetime2Char("beginTime"));

	}


    /**
     * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��for Oracle Only��
     * 
     * @deprecated �����÷�����Ϊ�����¼��ݣ�����ͳһʹ�� getIdentityValue ������
     * @param conn ���ݿ����Ӷ���
     * @param seqName ���ж�������
     * @return ���е���һ��ֵ
     * @throws SQLException ���ݿ����ʧ���쳣��
	public int getSequenceValue( String seqName, Connection conn ) throws SQLException {

		/// return ApplicationContext.getDatabaseManager().updateBlobColumn(tablename,
		
    	/// Connection conn = null;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	int nextVal=0;

        try {
        	/// conn = getConnection();
            pstmt = conn.prepareStatement( "SELECT "+seqName+".Nextval FROM DUAL");
            rs = pstmt.executeQuery();
            if(rs.next()){
            	nextVal = rs.getInt(1);
            }

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.error("Result in get Sequence Exception'SQL is:\nSELECT "+seqName+".Nextval FROM DUAL");
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	try{
        		if(rs!=null)
        			rs.close();
        		if(pstmt!=null)
        			pstmt.close();
//        		if(conn!=null)
//        			conn.close();
            }catch(SQLException e)
            {
	            log.fatal("��ִ�� getSequenceValue()����������ϢΪ��\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
        }        
    	return nextVal;
	}
     */
	
	
    /**
     * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��for Oracle Only��
     * 	 ����Oracle�����ʹ�����з��������õ�id,���Բ��ύ����˿��Թ���conn��
     * 
     * @param conn ���ݿ����Ӷ���
     * @param seqName ���ж�������
     * @return ���е���һ��ֵ
     * @throws SQLException ���ݿ����ʧ���쳣��
     */
	public long getIdentityValue( String tblName, Connection conn ) throws SQLException {
	   	
		
		/// return ApplicationContext.getDatabaseManager().updateBlobColumn(tablename,

		log.debug("---------------------wzw--2");
    	/// Connection conn = null;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	long nextVal=0;
    	boolean isConnCreated = false;		// ��ȡ�ĵط�һ��ͬ�� �ύ/�ع����رա�
    	log.debug("��ʼִ�� getIdentityValue");
        try {
        	if( conn==null || conn.isClosed() ){
        		conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();
        		isConnCreated = true ;
            	log.debug("��ȡ���µ�����");
        	}else{
        		log.debug("ʹ�����е�����");
        	}
        	
        	/// conn = getConnection();
            pstmt = conn.prepareStatement( "SELECT SEQ_"+tblName+".Nextval FROM DUAL");
    		log.debug("��ʼִ�в�ѯ");
            rs = pstmt.executeQuery();
    		log.debug("��ѯִ�����");
            if(rs.next()){
            	nextVal = rs.getLong(1);
            }

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.error("Result in get Sequence Exception'SQL is:\nSELECT SEQ"+tblName+".Nextval FROM DUAL", e);
			/// log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	log.debug("��ʼ�رն���");
    		if( isConnCreated ){		//�����˾͹ر�
    			wzw.util.DbUtils.closeQuietly(conn, pstmt, rs);
    		}else{
    			wzw.util.DbUtils.closeQuietly(null, pstmt, rs);
    			
    		}
            
        }        
    	return nextVal;
	}

	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#resultSet2RowSet(java.sql.ResultSet)
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException {

		// ����Oracle��ʵ����� Oracle10 ���ݿ�
		oracle.jdbc.rowset.OracleCachedRowSet crs= new oracle.jdbc.rowset.OracleCachedRowSet();
		
		// �����Ƿ���
		crs.populate( rs );
		
		//log.debug(" ----------------- ok ");
		//ResultSetWrap crs2 = new ResultSetWrap(rs);
		//crs2.
		
		return crs ;
	}


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
		sql =
			"Select t.* " +
			"  From (select Rownum As rowseq,t.* From ( "+sql+") t ) t " +
			" WHERE rowseq BETWEEN "+ offset +" AND "+ ( offset + row_count -1 );
		
		log.debug( sql );
		return sql ;
	}
	


	/**
	 * ��ָ�����ļ����ݸ��µ�Oracle���ݿ��BLOB�ֶΡ�
	 * @param tableName ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return �ɹ�����true��ʧ���׳��쳣
	 * @exception ������������쳣�����ⲿ�׳�
	 */	
	public boolean updateBlobColumn(String tableName, 
								   String picField, 
								   String sqlWhere, 
								   String strPath,
								   Connection conn ) throws Exception {
		
		oracle.sql.BLOB blb=null;

		boolean isConnCreated = false;
		
		try{
			//��ʼ�����ݿ������
			if(conn==null || conn.isClosed()){
				conn = DbUtils.getConnection();
				isConnCreated = true ;				// ������Լ���ȡ�����ӣ����Լ����� ��ȡ���ύ/�ع����ر� ��������֮��Ĳ��������򲻴����ύ���ع����رա�
			}
			
			conn.setAutoCommit(false) ;
			String str_sql="SELECT " + picField + " FROM " +   tableName +" "+ sqlWhere + " FOR UPDATE";
			PreparedStatement pstmt = conn.prepareStatement(str_sql);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) blb=(oracle.sql.BLOB)rset.getBlob(picField) ;
			//log.debug(blb.toString());
			String fileName = strPath;
			java.io.File f = new java.io.File(fileName);
				
			try{
				java.io.FileInputStream fin ;
				fin = new java.io.FileInputStream(f);     
				
				//log.info("file size = " + fin.available());
				pstmt = conn.prepareStatement("update " + tableName  + " set " + picField + "=? "  +  sqlWhere);
				
				OutputStream out = blb.getBinaryOutputStream();
				/// int count = -1, total = 0;
				byte[] data = new byte[(int)fin.available()];
				fin.read(data);
				out.write(data);
				fin.close();
				out.close();
				pstmt.setBlob(1,blb);
				pstmt.executeUpdate();
				pstmt.close();
				
				if(isConnCreated){		// �Լ������Ĳ��ύ
					conn.commit();
				}
				return   true ;
      
			}catch(java.io.FileNotFoundException e){
				if(isConnCreated){		// �Լ������ĲŻع�
					conn.rollback();
				}
				log.debug("debug", e);
				/// e.pri ntStackTrace();
				throw new Exception ("�ϴ����ļ�û�ҵ����������ļ�û���ϴ����������ϣ�����û������д�����ݿ�" + e.getMessage());
				
			}finally{
				if(isConnCreated && conn!=null){	// �Լ������ĲŹر�
					conn.close();      	
				}
				
			}
		}catch(Exception ex){
        	log.error( "Exception:"+ex.getMessage() );
			log.debug("debug", ex);
			/// ex.pri ntStackTrace();
			throw ex;
		}
	}

	/**
	 * ��ָ�����ļ����ݸ��µ�Oracle���ݿ��BLOB�ֶΡ�
	 * @param tableName ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return �ɹ�����true��ʧ���׳��쳣
	 * @exception ������������쳣�����ⲿ�׳�
	 */	
	public boolean updateBlobColumn(String tableName, 
								   String picField, 
								   String sqlWhere, 
								   String strPath) throws Exception {
		return updateBlobColumn( tableName, 
				     picField, 
				     sqlWhere, 
				     strPath,
				     null );
	}


	/**
	 * ���ַ������ݸ��µ�Oracle���ݿ��CLOB�ֶΡ�
	 *   CLOB(Character   Large   Object)   
	 *     ���ڴ洢��Ӧ�����ݿⶨ����ַ������ַ����ݡ���������long���ͣ�
	 *        
	 * @param tableName ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param content �ı�����
	 * @param conn ���ݿ����Ӷ��������
	 * @return �ɹ�����true��ʧ���׳��쳣
	 * @exception ������������쳣�����ⲿ�׳�
	 */
	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content,
			   					Connection conn) throws Exception {

		/// Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		String str_sql=null;
		//oracle.sql.CLOB clob=null;
		boolean isConnCreated = false;
		
		try{
			//��ʼ�����ݿ������
			if(conn==null || conn.isClosed()){
				conn = DbUtils.getConnection();
				isConnCreated = true ;
			}
			
			conn.setAutoCommit(false);  //connΪConnection����

			//set clob column to empty first.
			str_sql="UPDATE " + tableName + " SET " +   picField +"=empty_clob() "+ sqlWhere;
			pstmt = conn.prepareStatement( str_sql );
			pstmt.executeUpdate();
			
			//get clob column for update.
			str_sql="SELECT " + picField + " FROM " +   tableName +" "+ sqlWhere + " FOR UPDATE";
			
			pstmt = conn.prepareStatement( str_sql );
			rs = pstmt.executeQuery();
			BufferedWriter out = null;
			BufferedReader in = null;
			if(rs.next()){ 
				//oracle.sql.CLOB clob = (oracle.sql.CLOB)rs.getClob( picField );

				Object dsp = ApplicationContext.getInstance().getDataSourceManager();
				Object clob = null;
				// ��ʾ��ǰjdbc����
				log.debug("I'm \n" + dsp.getClass() +"\n" );
//							+ conn.getClass() +"\n" 
//							+ rs.getClass() +"\n" 
//							+ rs.getClob(picField).getClass() );
//				if( dsp instanceof WebLogicContainer)  {
//					
//					clob = rs.getClob( picField );
//					// wzw:��Ҫ��ӣ�/bea/weblogic/server/lib/weblogic.jar ��
//					log.info("need : /bea/weblogic/server/lib/weblogic.jar");
//					//out = new BufferedWriter( ((weblogic.jdbc.vendor.oracle.OracleThinClob)clob).getCharacterOutputStream());
//				
//				}else if( dsp instanceof TomcatContainer )  {
//					// wzw:��Ҫ��ӣ�Tomcat/common/lib/naming-factory-dbcp.jar ��
//					org.apache.tomcat.dbcp.dbcp.DelegatingResultSet drs = (org.apache.tomcat.dbcp.dbcp.DelegatingResultSet)rs;
//					clob = ((oracle.jdbc.OracleResultSet)drs.getDelegate()).getCLOB( picField );
//					out = new BufferedWriter( ( (oracle.sql.CLOB)clob).getCharacterOutputStream() );
//				
//				}else 
//				{
					
					oracle.jdbc.driver.OracleResultSet ors =
						 (oracle.jdbc.driver.OracleResultSet)rs;
					clob = ors.getClob(picField);
					// wzw :������ﱨNullPointException�����п������ݿ���ֶβ���Clob���ͣ�����Varchar2���ͣ������ṹ��
					out = new BufferedWriter( ( (oracle.sql.CLOB)clob).getCharacterOutputStream() );
					
//				}
				
				in = new BufferedReader(new StringReader(content));
				//log.debug( out.w );
				//log.debug( "content55="+content );
				
				//int c;
				
				//while ((c=in.read())!=-1) {
				//	log.debug( "c= "+c );
				//	out.write(c);
				//}
				
				//buffer to hold data to being written to the clob.
				char[] cBuffer = new char[512];	//((OracleThinClob)cl).getBufferSize()

				//Read data from file, write it to clob
				int iRead = 0;
				while( (iRead= in.read(cBuffer)) != -1 ) {
					out.write( cBuffer, 0, iRead); 
					
				}
				
				in.close();
				out.close();
			}
			
			// commit all.
			if( isConnCreated ){		// �Լ������Ĳ��ύ
				conn.commit();
			}
			
			return true;
			
		}catch (Exception e){
			if( isConnCreated ){		// �Լ������ĲŻع�
				conn.rollback();
			}
			log.error("SQL=" + str_sql + "\ncontent="+content );
			log.error("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			if(isConnCreated){		//// �Լ������ĲŹر�
				DbUtils.closeQuietly(conn, pstmt, rs);
			}else{
				DbUtils.closeQuietly(null, pstmt, rs);
			}
		}
	}

	/**
	 * ���ַ������ݸ��µ�Oracle���ݿ��CLOB�ֶΡ�
	 *   CLOB(Character   Large   Object)   
	 *     ���ڴ洢��Ӧ�����ݿⶨ����ַ������ַ����ݡ���������long���ͣ�
	 *        
	 * @param tableName ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param content �ı�����
	 * @return �ɹ�����true��ʧ���׳��쳣
	 * @exception ������������쳣�����ⲿ�׳�
	 */
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

	/**
	 * ���ַ������ݸ��µ�Oracle���ݿ��CLOB�ֶΡ�
	 * ����������
	 * @param tableName ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param content �ı�����
	 * @return �ɹ�����true��ʧ���׳��쳣
	 * @exception ������������쳣�����ⲿ�׳�
	 */
	public String getClobColumn(String sql) throws Exception { 
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		//oracle.sql.CLOB clob=null;
        StringBuffer sb = new StringBuffer();
		
		try{
			//��ʼ�����ݿ������
			conn = DbUtils.getConnection();
			conn.setAutoCommit(false);  //connΪConnection����

			pstmt = conn.prepareStatement( sql );
			rs = pstmt.executeQuery(sql);
			if (rs.next())
			{
				//weblogic.jdbc.vendor.oracle.OracleThinClob clob = (weblogic.jdbc.vendor.oracle.OracleThinClob)rs.getClob( 1 );
				Clob clb = rs.getClob( 1 );
			
				if (clb != null){
					//Reader is = clob.getString();//getCharacterInputStream();
		 
					BufferedReader br = new BufferedReader( clb.getCharacterStream() );
					String s = br.readLine();
					while (s != null) {
						sb.append( s ).append("\n");
						s = br.readLine();
					}
					return sb.toString();	//return clob.getChars(f,t);
				}
			}
			return "";

		}catch (Exception e){
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			wzw.util.DbUtils.closeQuietly(conn, pstmt, rs);
		
		}

	}


	/**
	 * 
	 * @return to_char( Brithday,'yyyy-mm-dd')
	 */
	public String date2Char(String colName) {
		return "to_char("+colName+", 'yyyy-mm-dd')";
	}


	/**
	 * 
	 * @return to_date('2006-01-01','yyyy-mm-dd')
	 */
	public String char2Date(String colValue) {
		return "to_date('"+colValue+"' , 'yyyy-mm-dd')";
	}


	/**
	 * 
	 * @return to_char( BeginTime, 'yyyy-mm-dd hh24:mi:ss')
	 */
	public String datetime2Char(String colName) {
		return "to_char("+colName+", 'yyyy-mm-dd hh24:mi:ss')";
	}
	
	/**
	 * 
	 * @return to_date( '2006-01-01 12:15:30', 'yyyy-mm-dd hh24:mi:ss')
	 */
	public String char2Datetime(String colValue) {
		return "to_date('"+colValue+"' , 'yyyy-mm-dd hh24:mi:ss')";
	}


	/**
	 * ���ػ�ȡ���ݿ�ϵͳʱ��� sql Ƭ�ϡ�
	 */
	public String sysDatetime() {
		return "sysdate";
	}
	

	/**
	 * ���ػ�ȡ���е� sql Ƭ�ϡ�
	 */
	public String identity(String tblName) {
		return "SEQ_"+tblName+".nextVal";
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#concat()
	 */
	public String concat(String str1, String str2) {
		return str1 + "||" + str2 ;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */
	public String switchNull(String exp1, String exp2) {
		return "nvl(" +exp1+ ", " +exp2+ ")";
	}


	/**
	 * Oracle ��ʱ��ʹ�������Ȳ�����ȡ�ķ�ʽ�������Ȼ�ȡ����룬��ΪOracle ���ṩ�Զ������С�
	 */
	public int getLastInsertIdentity( Connection conn  ) throws SQLException {
		throw new SQLException("Oracle �в�Ҫʹ�ñ����ԣ���ʹ�� getIdentityValue ������"); 
		// return -1;
	}


	/**
	 * ���ݵ�ǰ�����ݿ������������Ҫ��������ݲ���������ʽ�����⴦��
	 * 		����ַ�������(Varchar)������(Number)������(Date)�Ͷ�����(LOB)���Ͳ����������
	 * 		-- ��ǰ����� Oracle ���ݿ⣬�� ' ���� �滻Ϊ '' �����ܲ��뵽���ݿ��С�
	 * <pre>
	 * escape2Sql("ab'cd")			="ab''cd"
	 * escape2Sql("ab'c'd")			="ab''c''d"
	 * escape2Sql("ab''cd")			="ab''''cd"
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

//
//	public void setCachedResultSet(RowSet rs) throws Exception {
//		OracleResultSetWrap crs= new OracleResultSetWrap();
//		
//		this.rs = crs;
//		//this.rs.
//	}
//
//
//	public RowSet getCachedResultSet() throws Exception {
//		// TODO Auto-generated method stub
//		//return this.rs.clo
//		
//		return (RowSet)BeanUtils.cloneBean( this.rs );
//		//return null;
//	}

}

