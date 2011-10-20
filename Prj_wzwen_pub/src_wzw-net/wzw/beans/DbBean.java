 
package wzw.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;

import wzw.sql.SqlUtils;
import wzw.util.DbUtils;

import com.kingcore.framework.bean.NavigableDataSet;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.bean.QueryDataSet;
import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DataSourceManager;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.MapResultSetExtractor;
import com.kingcore.framework.jdbc.PlainConnection;
import com.kingcore.framework.jdbc.RowSetResultSetExtractor;
import com.kingcore.framework.jdbc.TypeResultSetExtractor;


/**
 * <p>��װ��һЩ�����ݵĲ�����
 * 		��ȡ���ݿ����ӳص����ӣ�ִ���������£��ύ����ȵȡ�
 *   ��õ�DbBean�࣬ͨ������ ApplicationContext, ֧�ֶ�����Դע�룬���ṩ���ݿ�����֧�֣�
 *    �ο���DaoJdbcPlainImpl.java  DbUtils.java </p>
 * @author	WUZEWEN on 2006-4-13
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @since	JDK1.4
 */

public class  DbBean {
	
	private Connection conn = null;
	private Logger log = Logger.getLogger(DbBean.class);
	private static DataSourceManager dataSourceManager = null;
//	private  ResultSet rs = null;
//	public  Exception sqlexp=null;
//	private ResultSetMetaData resultmetadata = null;
//	private int ok=0;

	//private String dbpool;
	
	/**
	 * ��ΪDbBean���Դ������������poolNameʹ��˽�У�������static������������ҡ�
	 * ������DbBean���Էŵ�b/s�ṹ�У����û�ʹ��һ��VM���������Լ���poolName��
	 *    ComponentUtil.java��ֻ����C/S�ṹ�е�GUIϵͳ��һ�㲻����û�ʹ��һ��
	 * VM�����Լ�ʹ��poolName��static�ģ�Ҳֻ����һ���û�ʹ�á�
	 * 
	 */
	public static String DefaultPoolName="main";
	
	public static String getDefaultPoolName() {
		return DefaultPoolName;
	}

	/**
	 * you can set defaultPoolName for all DbBean instances.
	 * @param defaultPoolName
	 */
	public static void setDefaultPoolName(String defaultPoolName) {
		DefaultPoolName = defaultPoolName;
	}
	
	private String currentPoolName = DefaultPoolName;  //ÿ�ι���DbBeanʵ��ʱ���ʼ��

	public DbBean(){
		this(DefaultPoolName);
	}
	
	/**
	 * 
	 * @param poolName ʹ�õ������ݿ����ӳ�����
	 */
	public DbBean (String poolName){
		this.currentPoolName = poolName;
	}
	
	/**
	 * @return Returns the poolName.
	 */
	public String getPoolName() {
		return currentPoolName;
	}

	/**
	 * @param poolName The poolName to set.
	 */
	public void setPoolName(String poolName) {
		this.currentPoolName = poolName;
	}

	/**
	 * @return ���ݿ����Ӷ���conn
	 */
	public Connection getConnection() throws SQLException{
		return getConnection( DefaultPoolName );
	}


	/**
	 * �������ڲ����õ����ݿ���·�����ִ��һϵ�е�sql��䣬���ʧ�ܣ�����false�����׳�SQLException��
	 * @param vv sql statements collectioins
	 * @return return true if executeUpdate success,return false if executeUpdate failed.
	 * @throws SQLException 
	 */
	public int[] executeBatch(Vector<String> vv) throws SQLException{
		// �����ж�
		if( vv==null){
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < vv.size(); i++) {
			list.add(vv.get(i).toString());
		}
		return executeBatch(list);
	}

	/**
	 * <p>��doUpdate(Vector)�ã����߻ᵼ���쳣��Microsoft][ODBC Microsoft Access Driver] Could not update; currently locked</p>
	 * @author Zeven on 2011-3-9
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql) throws SQLException{
		// �����ж�
		if( sql==null){
			return true;
		}
		boolean isConnCreated = false;
		// ���ݲ�������sql��䣬��ִ�С��ύ�������׳��쳣
		Statement var_stmt = null; 
		try
		{
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				this.conn.setAutoCommit(true);
				log.debug("set AutoCommit to true,done.");  //ֻ�е��Դ����ʱ�����������Ϊһ�����ʾ��Ϣ�����
				isConnCreated = true;
			}else{
				log.debug("this.conn exists,so need not to get new one.");
			}
			var_stmt = this.conn.createStatement();
			log.debug("executeUpdate,sql="+sql); //ֻ�е��Դ����ʱ�����������Ϊһ�����ʾ��Ϣ�����
			var_stmt.executeUpdate(sql);
			//this.conn.commit();   //����Ҫ�ύ�������ȡ����conn�����Զ��ύ�����ʹ���Ѵ��ڵģ��򲻹��ύ��ع���
			
		} catch(SQLException se) {
			if(isConnCreated && this.conn!=null){
				wzw.util.DbUtils.rollbackQuietly( this.conn);
			}
			se.printStackTrace();
			throw se;
			
		} finally {
			wzw.util.DbUtils.closeQuietly( var_stmt );
			if(isConnCreated && this.conn!=null){
				this.conn.setAutoCommit(false);
				wzw.util.DbUtils.closeQuietly( this.conn );
			}
 
		}
		
		return true;
	}

 
// ------------------- copy from DaoJdbcPlainImpl.java
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
	public void freeConnection() throws SQLException{
		if(this.conn!=null) {
			this.conn.close();
			// Zeven ,����������á� һ����˵������close(),Ҳ�Ͳ�����ִ�����ݿ����ӡ�
			// ֻ��ʹ�� conn.close(),��������Ϊnull,�������ӳ��еĶ�����Ϊnull(��) �ˡ�
			// ��������Ͽ������ã������Ը�ֵΪnull���𣿣���
			//this.conn = null ;
		}
	}
 
	/**
	 * ��ʱ��ʹ�ã����ǲ���ֱ�ӵõ����� wzw on 2006-9-24 
	 */ 

	
	/**
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * @deprecated replaced by resultSet2RowSet(ResultSet rs)
	 * @param rs
	 * @return
	 * @throws SQLException 
	public RowSet populateResultSet(ResultSet rs) throws SQLException{
		return ((PooledConnection)conn).getDatabaseManager().resultSet2RowSet(rs);
	}
	 */

	/**
	 *  ��ȡOracle���ݿ�ָ�����е���һ������ֵ��
	 *    �������޶�����Oracle�����з�ʽ��������ʹ�ã��� getIdentityValue(tblName)�滻��
	 *  
	 * @deprecated �� getIdentityValue(tblName)�滻
	 * @param seqName ���ж������ƣ���"seq_employee"
	 * @return ���е���һ��ֵ
	 * @throws SQLException ���ݿ����ʧ���쳣��
	public int getSequenceValue( String seqName) 
	  throws SQLException {
		if(this.conn==null || this.conn.isClosed()) {
			return DBUtils.getSequenceValue( seqName);
		}else {
			/// return ((PooledConnection)conn).getDatabaseManager().getSequenceValue( seqName, conn);
			return DBUtils.getSequenceValue( seqName, conn);	// oracle ��ȡ���п��Թ�������
		}
	}
	 */

	/* ************************** @deprecated ��������� ************************************* */

	/// private DataSource dataSource = ApplicationContext.getInstance().getDataSourceProvider().getDataSource(); 
 
	/**
	 * չʾ����·��
	 */
   public static String showTrace(int maxdepth)
    {
      String stack="����·����\n";
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 1; i < Math.min(maxdepth + 1, trace.length); i++)
      {
        stack += "\t[" + trace[i].hashCode() + "]" + trace[i] + "\n";
      }
      return stack;
    }
    
//	/**
//	 * <pre>��ȡ���ݿ������ wzw on 2006-9-24
//	 * 		ֱ��ʹ���� conn.close() �Ͽ������ӡ�</pre>
//	 * 
//	 * @return
//	 * @throws SQLException 
//	 */
//	public Connection getConnection() throws SQLException{
//		if(this.conn==null || this.conn.isClosed() ) {
//			this.conn = ApplicationContext.getInstance().getDataSourceProvider().getConnection( );
//			log.debug("��ȡ������ʱhashcode="+this.conn.hashCode()+"������hashcode="+this.hashCode()+"������·����"+showTrace(8) ); //
//		}else{
//			log.debug("ʹ����������hashcode="+this.conn.hashCode()+"������hashcode="+this.hashCode()+"������·����"+showTrace(8) ); //
//		}
//		return this.conn;
//		
//	}
	
	/**
	 * <pre>��ȡ���ݿ������ wzw on 2006-9-24
	 * 		ֱ��ʹ���� conn.close() �Ͽ������ӡ�</pre>
	 * 
	 * @param poolName ���ӳض�������ƣ�û�в�����ʹ�����õ�Ĭ�����ӳ�
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String poolName) throws SQLException{
		// ��������ӳػ�ȡ���ӣ��ڲ�������ʱӦ��Ҫ�׳��쳣�ģ��������ӳ�ģ����û���ס�
		// ����ԭ�д��벻�䡣
		return ApplicationContext.getInstance().getDataSourceManager().getConnection(poolName);
		
//		if( DbBean.dataSourceManager!=null ){
//			return DbBean.dataSourceManager.getConnection(poolName);
//		}else{
//			throw new SQLException("no dataSourceManager!");
//		}
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
	
//	private RowSet doQuery(String sql, Connection conn )
//	 throws SQLException {
//		return DBUtils.doQuery(sql, conn );
//	}

	
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
//	public int executeUpdate( String sql ) throws SQLException {    
//		 
//		//return doUpdate(sql, null, null);
//		
//		if(this.conn==null || this.conn.isClosed()) {
//			return DbUtils.executeUpdate( sql );
//		}else {
//			return DbUtils.executeUpdate( sql , conn);
//			
//		}
//	}
	
	/**
	 * <pre> doUpdate ��PreparedStatement��ʽ��</pre>
	 * @param sql	����?��sql���
	 * @param args	���?�Ĳ�����������
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object[] args) throws SQLException {    
		return executeUpdate(sql, args, null);
	}
	
	
	/**
	 * <pre> doUpdate ��PreparedStatement��ʽ��</pre>
	 * @param sql	����?��sql���
	 * @param args	���?�Ĳ�����������
	 * @param argTypes �������������Ӧ�Ĳ�������, java.sql.Type
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object[] args, int[] argTypes) throws SQLException {    

		boolean isConnCreated = false;
		PreparedStatement ps = null;
		int intReturn = 0 ;
		try{
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				this.conn.setAutoCommit(true);
				log.debug("this.conn.setAutoCommit to true."); //���Դ���ʱ������
				isConnCreated = true;
			}else{
				log.debug("this.conn exists,so need not to get new one.");
			}
			 
			ps = this.conn.prepareStatement(sql);
			
			if(args!=null){		// has params or not??
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
			}
			
			intReturn = ps.executeUpdate();

//			if(isConnCreated && this.conn!=null){		// �ύ
//				this.conn.commit();  //����Ҫ�ύ�������ȡ����conn�����Զ��ύ�����ʹ���Ѵ��ڵģ��򲻹��ύ��ع���
//			}
			
		}catch(SQLException sqle){			
			if(isConnCreated && this.conn!=null){		// �ع�
				DbUtils.rollbackQuietly(this.conn);
			}
            log.fatal( "Result in update Exception'SQL is:\n"+sql + ". Message:" + sqle.getMessage() ) ;
            sqle.printStackTrace();
			throw sqle;
		
		}finally{
			if(isConnCreated && this.conn!=null){
				this.conn.setAutoCommit(false);
				DbUtils.closeQuietly(this.conn, ps, null);	// �ر�
			}else{
				DbUtils.closeQuietly( ps );
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
	public int[] executeBatch( List<String> list )
	  										throws SQLException {

		int[] retArr = null;
		boolean isConnCreated = false;
		Statement var_stmt = null;
		// ���ݲ�������sql��䣬��ִ�С��ύ�������׳��쳣
		try
		{
			if(this.conn==null || this.conn.isClosed()) {
				conn=this.getConnection();
				conn.setAutoCommit(true);
				log.debug("executeBatch -> this.conn.setAutoCommit to true.");
				isConnCreated = true;
			}else{
				log.debug("executeBatch -> this.conn exists,so need not to get new one.");
			}
			var_stmt = this.conn.createStatement();
			for (int i = 0; i < list.size(); i++) {
				var_stmt.addBatch(list.get(i));
			}
			retArr = var_stmt.executeBatch();
//			this.conn.commit();  //����Ҫ�ύ�������ȡ����conn�����Զ��ύ�����ʹ���Ѵ��ڵģ��򲻹��ύ��ع���
			
		} catch(SQLException se) {
			if(this.conn==null || this.conn.isClosed()) {
				wzw.util.DbUtils.closeQuietly(this.conn);
			}
			se.printStackTrace();
			throw se;
			
		} finally {
			wzw.util.DbUtils.closeQuietly( var_stmt );
			if(isConnCreated && this.conn!=null){
				this.conn.setAutoCommit(false);
				wzw.util.DbUtils.closeQuietly( this.conn );
			}
		}
		
		return retArr;
	}

	/**
	 * 
	 * <pre>���ݱ����ƺͲ�ѯ��������ȡ�������������ݵ�������
	 * 	Zeven on 2009-2-11Ϊ�˽��MySql���⣬���� tableName.toUpperCase()
	 * </pre>
	 * @deprecated ����ʹ�� queryForInt �滻��������
	 * @param tableName Ҫ��ѯ�ı�����
	 * @param condition �����������硰WHERE numCol>100��
	 * @return ��������������
	 * @throws SQLException ���ݿ�����쳣
	 */
	public int getSize(String tableName, String condition) throws SQLException {
		if(this.conn==null || this.conn.isClosed()) {
			return DbUtils.getSize(tableName.toUpperCase(), condition);
		}else {
			return DbUtils.getSize(tableName.toUpperCase(), condition, this.conn);
		}
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
	public int queryForInt(String sql ) throws SQLException {
		
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
	public int queryForInt(String sql, Object[] args, int[] argTypes ) throws SQLException {
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
	public String queryForString(String sql ) throws SQLException {

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
	public String queryForString(String sql, Object[] args, int[] argTypes ) throws SQLException {
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
	public long queryForLong(String sql ) throws SQLException {
	
		return queryForLong(sql, null,null);
		
//		// Zeven: ���ȷ�������Լ��ķ������� DbUtils����ķ����أ���
//		// 	 �漰��this.conn������ͳһ����ʱ����ð�ʵ�ַ���Dao���棬������԰�ʵ�ַ��� DbUtils.class���档
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForLong(sql);
//		}else {
//			return DBUtils.queryForLong(sql, this.conn);
//		}

	}

	public long queryForLong(String sql, Object[] args, int[] argTypes ) throws SQLException {
	
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
	public List<Map<String,Object>> queryForList(String sql_datas) throws SQLException {

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
	public List<Map<String,Object>> queryForList(String sql_datas, Object[] args, int[] argTypes) throws SQLException {

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
	public List<Object> queryForList(String sql_datas, Class<?> beanClass) throws SQLException {

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
	@SuppressWarnings("unchecked")
	public List<Object> queryForList(String sql_datas, Object[] args, int[] argTypes, Class beanClass) throws SQLException {

		return (List<Object>)queryBean(sql_datas, args, argTypes, beanClass, true );	
	}


	/**
	 * <pre> ִ��ָ����sql��䣬���������װΪList<Type>���󷵻ء�
	 *    ��Ҫʹ��PrepareStatement��ʽ��ʹ�����صķ����� </pre>
	 * @param sql_datas ��Ҫִ�в�ѯ��sql���
	 * @param type ����List�����е�java.sql.Types �еĻ�����������
	 * @return
	 * @throws SQLException 
	 */
	public List<Object> queryForList(String sql_datas, int type) throws SQLException {

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
	@SuppressWarnings("unchecked")
	public List<Object> queryForList(String sql_datas, Object[] args, int[] argTypes, int type) throws SQLException {

		return (List<Object>)query(sql_datas, args, argTypes, new TypeResultSetExtractor(type, true) );	
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
	public RowSet queryForRowSet( String sql) throws SQLException {
		
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
	public RowSet queryForRowSet( String sql, Object[] args, int[] argTypes) throws SQLException {

		// return this.doQuery( sql );	// not PreparedStatement
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
	public RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

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
	public RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas,
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
	public List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

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
	public List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas,
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
	public List<Object> queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Class<?> clazz) throws SQLException {

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
	public List<Object> queryForPagedList(Navigator navigator, String sql_count, String sql_datas, 
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
	public long getIdentityValue( String tblName ) throws SQLException{

		log.debug("---------------------wzw--1");
		//return ((PooledConnection)conn).getDatabaseManager().getIdentityValue(tblName.toUpperCase(), null);

		return -1L;
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
//	 * Zeven set 'public ' to 'public'.
//     * @deprecated replaced by getNextUniqueIDValue(String tblName)
//	 * 
//	 * ��ȡOracle���ݿ�ָ�����е���һ������ֵ��
//	 * 
//     * @param tblName ��Ҫʹ��id�ı�����ƣ���"tsys_flowtype"
//	 * @return ���е���һ��ֵ
//	 * @throws SQLException ���ݿ����ʧ���쳣��
//	 */
//	public int getNextUniqueIDValue( String tblName ) throws SQLException{
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
	public int getLastInsertIdentity(DatabaseManager databaseManager) throws SQLException {
		return databaseManager.getLastInsertIdentity( conn );
	}
	

	/**
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    ����ֱ���������ݿ�ķ�װ��ͳһ���� RowSet��CachedRowSet(wzw)ֻ��Ϊ����ط��Ĵ���
	 * </pre>   
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException{
		return ((PlainConnection)conn).getDatabaseManager().resultSet2RowSet(rs);
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
	public RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm) throws SQLException{
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
	public void addBatch(Statement stmt, List<String> slq_list) throws SQLException{
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
	public String escape2Sql(String str) {
		
		return ((PlainConnection)conn).getDatabaseManager().escape2Sql(str);
	}


	/**
	 * <pre>
	 * 	
	 * ���ݵ�ǰ�����ݿ��﷨���������������ַ�����SQL Ƭ�ϡ�
	 *   �Ľ�Ҫ�� public ���� �޸�Ϊ public ���͡�
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
	public String concat(String str1, String str2) {
		return ((PlainConnection)conn).getDatabaseManager().concat( str1, str2 );
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
	public String switchNull(String exp1, String exp2) {
		return ((PlainConnection)conn).getDatabaseManager().switchNull( exp1, exp2 );
	}

	
//	/**
//	 * ��ȡΨһ��ʶ���ı��ʽ��
//	 * 
//   * @deprecated replaced by nextUniqueID(String tblName)
//	 * @param tblName ��Ҫʹ�ñ�ʶ���ı���"tsys_flow","employee","demo_table"
//	 * @return
//	 */
//	public String nextUniqueID(String tblName) {
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
	public String identity(String tblName) {
		return ((PlainConnection)conn).getDatabaseManager().identity( tblName.toUpperCase() );
	}
	

	/**
	 * <pre>
	 * ��ȡϵͳʱ��ĺ������ʽ��
	 * </pre>
	 * 
	 * @return
	 */	
	public String sysDatetime() {
		return ((PlainConnection)conn).getDatabaseManager().sysDatetime() ;
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
	public String date2Char(String colName) {
		return ((PlainConnection)conn).getDatabaseManager().date2Char( colName );
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
	public String char2Date(String colValue) {
		return ((PlainConnection)conn).getDatabaseManager().char2Date( colValue );
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
	public String datetime2Char(String colName) {
		return ((PlainConnection)conn).getDatabaseManager().datetime2Char( colName );
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
	public String char2Datetime(String colValue) {
		return ((PlainConnection)conn).getDatabaseManager().char2Datetime( colValue );
	}

	
	/*
	 public void populate(Object bean, ResultSet rs) throws SQLException {
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
	
	
//	public DAO(DataSource ds) {
//	this.ds = ds;
//	}
	
//	public void setDataSource(DataSource ds) {
//	this.ds = ds;
//	}
	
	
//	public void close(ResultSet rs) {
//	if (rs != null) {
//	try {
//	rs.close();
//	} catch (SQLException e) {
//	}
//	rs = null;
//	}
//	}
	
//	public void close(PreparedStatement pstmt) {
//	if (pstmt != null) {
//	try {
//	pstmt.close();
//	} catch (SQLException e) {
//	}
//	pstmt = null;
//	}
//	}
	
//	public void close(Connection conn) {
//	if (conn != null) {
//	try {
//	conn.close();
//	} catch (SQLException e) {
//	log.debug("debug", e);
//	/// e.pri ntStackTrace();
//	}
//	conn = null;
//	}
//	}
	
//	public void rollback(Connection conn) {
//	if (conn != null) {
//	try {
//	conn.rollback();
//	} catch (SQLException e) {
//	log.debug("debug", e);
//	///e.pri ntStackTrace();
//	}
//	conn = null;
//	}
//	}
	
//	/**
//	 * @deprecated
//	 */
//	public String java2sqlName(String name) {
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
//	 * @deprecated
//	 * @param name
//	 * @return
//	 */
//	public String sql2javaName(String name) {
//		String column = "";
//		for (int i = 0; i < name.length(); i++) {
//			if (name.charAt(i) == '_') {
//				column += ++i<name.length()?String.valueOf(name.charAt(i)).toUpperCase():"";
//			} else {
//				column += name.charAt(i);
//			}
//		}
//		return column;
//	}
	
	
//	/**
//	 * Zeven set 'public ' to 'public'.
//	 * 
//	 * ��ѯ��ҳ��������, just for xxxJdbcDaoImpl��
//	 * @param sql_datas
//	 * @param sql_count
//	 * @param pageParams �����ҳ����Ϣ���� {rowCount, pageSize, pageNumber}
//	 * @return
//	 * @throws Exception
//	 */
//	public NavigableDataSet queryForNavigableRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new RowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableSqlRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams, Class elementType) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, RowMapper rowMapper) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	public NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, ResultSetExtractor rse) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	public NavigableDataSet queryForNavigableDataSet( String sql_datas,
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
	public NavigableDataSet executeQueryDataSet( Navigator navigator,
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
	public String createSqlForPage(Navigator navigator, String sql_count, String sql_datas ) throws SQLException {

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
		
		String sql = ((PlainConnection)conn).getDatabaseManager().getSubResultSetSql( sql_datas, 
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
	public String createSqlForCount( String sql_datas){
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
	public boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{
		

		if(this.conn==null || this.conn.isClosed()) {
			return ((PlainConnection)conn).getDatabaseManager().updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
					null);
		}else {
			return ((PlainConnection)conn).getDatabaseManager().updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
 					this.conn );
		}
		
	}
	

	/**
	 * <pre>
	 * 		Ϊ�˼��ݳ����ٸ�Ϊ public ���ͣ������ public ���͡�
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
	public boolean updateClobColumn(String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception{

		if(this.conn==null || this.conn.isClosed()) {
			return ((PlainConnection)conn).getDatabaseManager().updateClobColumn(  tablename, 
					  picField, 
 					  sqlWhere,
 					  content,
 					  null );
		}else {
			return ((PlainConnection)conn).getDatabaseManager().updateClobColumn(  tablename, 
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
	public String getClobColumn(String sql) throws Exception{
		return ((PlainConnection)conn).getDatabaseManager().getClobColumn( sql );	
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
	
	
//	/**
//	 * ���ڲ���ѯ��
//	 * @param sql
//	 * @return
//	 * @throws Exception
//	 */
//	private Page doInnerQuery(  String sql ) throws Exception
//	{
//		//log.debug("doQuery()�˴β�ѯ��sql���Ϊ��" + sql );
//		RowSet datas = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		//ResultSetMetaData rsmd = null;
//		int count = 0;
//		//int i = 0, colcount = 0;
//		//String[] colName;
//		boolean isConnCreated = false;
//
//		try
//		{
//			//.out.println("doQuery()�˴β�ѯ��sql���Ϊ��"  );
//			if( this.conn==null || this.conn.isClosed() ) {
//				// cann't using self's getConnection() method.
//				this.conn = getConnection();
//				isConnCreated = true;
//			}
//			
//			//.out.println("1---" +sql);
//			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//			//SQLUtils.fillStatement( pstmt, this.getParams() ) ;
//			//.out.println("2---" +sql);
//			rs = pstmt.executeQuery();
//			//.out.println("3---" +sql);
////			if(rs.next()){
////			rs.last();
////			//.out.println("size"+rs.getRow());
////			rs.beforeFirst();
////			}else{
////			.out.println("no cord");
////			}
//			//log.fatal("have executeQuery get hsdgs--------jyj") ;
//			
//			//.out.println(sql);
//			
//			//datas.populate( rs ) ;
//			
//			datas = this.resultSet2RowSet( rs );
//			
//			//.out.println("4---" +sql);
//			/////  count = datas.size() ;
//			//.out.println("5---" +sql);
//			//.out.println("count="+count);
//			
//			
//			//rsmd = set.getMetaData();
//			//colcount = rsmd.getColumnCount();
//			//colName = new String[colcount];
//			//for (i = 1; i <= colcount; i++)
//			//{
//			//    colName[i - 1] = rsmd.getColumnName(i);
//			//}
//			//
//			//set = pstmt.executeQuery();  twice??
//			//log.fatal("have executeQuery2 " + colName[1]);
//			//while (set.next())
//			//{
//			//    count++;
//			//   log.fatal("have executeQuery3 "+set.getObject(colName[1]));
//			//    if (count < start + 1 || count > end + 1)
//			//        continue;
//			//    DataBean bean = new DataBean();
//			//    for (i = 0; i < colcount; i++)
//			//    {
//			//        bean.put(colName[i], set.getObject(colName[i]));
//			//    }
//			//    datas.add(bean);
//			//}
//		}
//		catch (SQLException se)
//		{
//			log.fatal("doQuery()�г�SQLException���󣬴���Ϊ��" + se.getMessage() + "\n\tsql statement is:"+sql );
//			log.debug("debug", se);
//			/// se.pri ntStackTrace();
//			throw se;
//			///mapping.findForward("");
//		}
//		catch (Exception e)
//		{
//			log.fatal("doQuery()�г�Exception���󣬴���Ϊ��" + e.getMessage() + "\n\tsql statement is:"+sql );
//			log.debug( e.getMessage(), e );
//			/// e.pri ntStackTrace();
//			/// mapping.findForward("");
//			throw e;
//		}
//		finally
//		{
//			try
//			{
//				if (rs != null)
//				{
//					rs.close();
//				}
//				if (pstmt != null)
//				{
//					pstmt.close();
//				}
//				if ( isConnCreated && conn!=null )
//				{
//					conn.close();
//				}
//			}
//			catch (SQLException se)
//			{
//				log.fatal("doQuery��finally�йرճ�������Ϊ��" + se.getMessage());
//			}
//		}
//		//.out.println("str_pageSize "+ pageSize);
//		return new Page(datas, 0, 0, 0, count);
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
	public RowSet doQuery(String sql, Object[] args) throws SQLException {
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
	public RowSet doQuery(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (RowSet) query(sql, args, argTypes, new RowSetResultSetExtractor() );
	}
	 */
		

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
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				isConnCreated = true;
				log.debug("create a new this.conn for query.");
			}else{
				log.debug("this.conn exists, need not to create a new one.");
			}

			log.debug("--------------wzw--2------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--2------------"+ ((this.conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = this.conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = this.conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();

			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
//			if (rse==null) {
//				//
//			}
	        objReturn =  rse.extractData( rs);

		}finally{

			DbUtils.closeQuietly( ps );
			
			if(isConnCreated){				// ��ѯ��ֻ��Ҫ�رգ�����Ҫ�ύor�ع�
				log.debug("-----------1---wzw--��Ҫ�ر�conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------2---wzw--��Ҫ�ر�conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly(this.conn, stmt, rs);
        		log.debug("-----------3---wzw--��Ҫ�ر�conn end------------"+ (this.conn.isClosed()?"conn isClosed":"conn not closed") );
				log.debug("-----------4---wzw--��Ҫ�ر�conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------5---wzw--��Ҫ�ر�conn end------------"+ ((this.conn==null)?"is null":"not null") );
			}else{
				log.debug("--------------wzw--����Ҫ�ر�conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--����Ҫ�ر�conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly( null, stmt , rs);
				log.debug("--------------wzw--����Ҫ�ر�conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--����Ҫ�ر�conn end------------"+ ((this.conn==null)?"is null":"not null") );
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
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				isConnCreated = true;
			}

			log.debug("--------------wzw--2------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--2------------"+ ((this.conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = this.conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = this.conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();

			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
	        objReturn =  ApplicationContext.getInstance().getResultSetBeanExtractor().extractData(rs, beanClass, isList );

		}finally{

			DbUtils.closeQuietly( ps );
			
			if(isConnCreated){				// ��ѯ��ֻ��Ҫ�رգ�����Ҫ�ύor�ع�
				log.debug("-----------1---wzw--��Ҫ�ر�conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------2---wzw--��Ҫ�ر�conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly(this.conn, stmt, rs);
        		log.debug("-----------3---wzw--��Ҫ�ر�conn end------------"+ (this.conn.isClosed()?"conn isClosed":"conn not closed") );
				log.debug("-----------4---wzw--��Ҫ�ر�conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------5---wzw--��Ҫ�ر�conn end------------"+ ((this.conn==null)?"is null":"not null") );
			}else{
				log.debug("--------------wzw--����Ҫ�ر�conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--����Ҫ�ر�conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly( null, stmt , rs);
				log.debug("--------------wzw--����Ҫ�ر�conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--����Ҫ�ر�conn end------------"+ ((this.conn==null)?"is null":"not null") );
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
	private Object query(String sql, Object[] args, ResultSetExtractor rse) throws SQLException {
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
	public Object queryForType(String sql, int type ) throws SQLException {
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
	public Object queryForType(String sql, Object[] args, int[] argTypes, int type ) throws SQLException {

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
	public Object queryForBean(String sql, Class<?> beanClass ) throws SQLException {
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
	public Object queryForBean(String sql, Object[] args, int[] argTypes, Class<?> beanClass ) throws SQLException {
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
	public Map<String,Object> queryForMap(String sql ) throws SQLException {
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
	public Map<String,Object> queryForMap(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (Map<String,Object>) query(sql, args, argTypes, new MapResultSetExtractor(false) );  
	}
	public static DataSourceManager getDataSourceManager() {
		return DbBean.dataSourceManager;
	}
	public static void setDataSourceManager(DataSourceManager dataSourceManager) {
		DbBean.dataSourceManager = dataSourceManager;
	}  

}
