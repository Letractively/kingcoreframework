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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.RowSet;

/**
 * <p> ���ݿ����ϵͳ�ṩ������Ĳ�����Ҫʵ�ֵĽӿڡ�</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface DatabaseManager {
	

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
	public String escape2Sql(String val);
	
	/**
	 * ���ݵ�ǰ�����ݿ��﷨��ת������Ϊnull�ı��ʽ(����sqlƬ��)��
	 * 
	 * <pre>
	 * 	
	 * 		Oracle ���ݿⷵ�� nvl(exp1, exp2);
	 * 		MySQL  ���ݿⷵ�� coalesce( exp1, exp2);
	 * 		SQLServer  ���ݿⷵ�� isNull(exp1, exp2);
	 * </pre>
	 * @param str1 �ַ�����������
	 * @param str2 �ַ�����������
	 * @return
	 */
	public String switchNull( String exp1, String exp2 );
	
	/**
	 * ���ݵ�ǰ�����ݿ��﷨���������������ַ�����sqlƬ�Ρ�
	 * 
	 * <pre>
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
	public String concat(String str1, String str2) ;
	
	/**
	 * ��ȡΨһ��ʶ���ı��ʽ(����sqlƬ��)��
	 * Zeven on 2009-01-17: ��sqlƬ������ǲ�֧������ģ�
	 * 		����ʹ��Oracle��seq��MySql��MyISAM���͵ı��¼����ֵ�� --ALTER TABLE `tsys_sequence` ENGINE=MYISAM;
	 * 		���λ�������У���ᷢ���������������⡣
	 * @param tblName
	 * @return
	 */
	public String identity(String tblName) ;
	
	/**
	 * ��ȡϵͳʱ��ĺ������ʽ(����sqlƬ��)��
	 * @return
	 */	
	public String sysDatetime() ;
	
	/**
	 * �ṩ������ʱ�����͵���תΪ�ַ����ͷ��صķ���(����sqlƬ��)
	 * @return
	 */
	
	public String datetime2Char( String colName ) ;
	
	/**
	 * �ṩ���ַ����͵���תΪ����ʱ�����ͷ��صķ���(����sqlƬ��)
	 * @return
	 */
	public String char2Datetime( String colValue) ;
	
	/**
	 * �ṩ������(����ʱ��)���͵���תΪ�ַ����ͷ��صķ���(����sqlƬ��)
	 * @return
	 */
	public String date2Char( String colName ) ;
	
	/**
	 * �ṩ���ַ����͵���תΪ����(����ʱ��)���ͷ��صķ���(����sqlƬ��)
	 * @return
	 */
	public String char2Date( String colValue) ;

	/**
	 *  ������Ҫ��ѯ��ĳ�������У���װ��ǰ��sql statement and return.
	 * @param sql ԭsql statement
	 * @param offset ��ʼλ��
	 * @param row_count ��Ҫ��ȡ������
	 * @return ��װ֮��� sql statement
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count);

	/**
	 * <p>�� jdbc �е� ResultSet ����ת��Ϊ RowSet ����</p>
	 * @param rs ��ҪתΪRowSet��ResultSet����
	 * @return RowSet����
	 * @throws SQLException
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException  ;

	
	/**
	 * <p>ÿ��һ�����һ������PK�����ݴ��������(��ñ������)���ص�ǰ���õ�����ֵ��
	 * 		����Oracle ����ʹ�ý������ж���ʹ�� seq_tblName.nextVal ��ȡ��
	 * 		�������������͵����ݿ⣬������Ӧ���ʺϵķ�����</p>
	 * 
	 * @param tblName
	 * @return
	 * @throws SQLException
	 */
	public long getIdentityValue( String tblName, Connection conn ) throws SQLException  ;

	
	/** 
	 * ʹ���Զ������в���֮�󣬻�ȡ�ղŲ����idֵ��
	 *  ����� MSS, MySQL ������һ�����н��������
	 *
	 */
	public int getLastInsertIdentity( Connection conn ) throws SQLException  ;


	/**
	 * �������ƴ���󱣴浽���ݿ�ı��С�
	 *   BLOB(Binary   Large   Object)   
	 *     �������洢�޽ṹ�Ķ��������ݡ���������row��long   row��
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txt","D:\\upload\\a.txt"
	 * @param conn
	 * @return
	 */	
	public  boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath,
								Connection conn ) throws Exception;
	

	/**
	 * �������ƴ���󱣴浽���ݿ�ı��С�
	 *   BLOB(Binary   Large   Object)   
	 *     �������洢�޽ṹ�Ķ��������ݡ���������row��long   row��
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param strPath Ҫ�������ݿ���ļ���ȫ·������ "D:/upload/a.txt","D:\\upload\\a.txt"
	 * @return
	 */	
	public  boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception;
	

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
	public boolean updateClobColumn(String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception;

	/**
	 * ���ַ��ʹ���󱣴浽���ݿ�ı��С�
	 *   CLOB(Character   Large   Object)   
	 *     ���ڴ洢��Ӧ�����ݿⶨ����ַ������ַ����ݡ���������long���ͣ�   
	 *      
	 * @param Tablename ������
	 * @param picField ������
	 * @param sqlWhere sql��where ��䣬�� "where id='123456'"
	 * @param Content Ҫ�������ݿ������
	 * @param conn ���������ύ/�ع�������Ƶ� Connection����
	 * @return
	 */
	public boolean updateClobColumn(String tablename, 
				String picField, 
				String sqlWhere,
				String content,
				Connection conn) throws Exception;

	/**
	 * ��ȡ�ַ��ʹ��������ݡ�
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public String getClobColumn(String sql) throws Exception;	   


}
