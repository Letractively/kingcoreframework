/*
 * @(#)UpdateAction..java		    2004/04/21
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */

package com.kingcore.framework.base.controller ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import wzw.util.DbUtils;

import com.kingcore.framework.util.SQLUtils;

//import com.wuzewen.pub.exception.UnsupportedConversionException;
//import com.wuzewen.pub.bean.Value ;
//import com.wuzewen.pub.bean.value.* ;

/**
 * <p>��װ��һЩִ�и������ݵĲ���������ִ�и��²�����Acting�඼������չ���ࡣ
 *        ע�⣺�����ṩ�ĸ������ݿ�ķ���������������������ͳһ���ơ�</p>
 * @author	WUZEWEN on 2004.04.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class UpdateAction extends BaseAction {

    protected static Logger log = Logger.getLogger( UpdateAction.class);
    
	//
	//˵���������в��ô���conntion������Ϊ����������ͳһ�ύ������һ��
	//

    //the constrution function!
    public UpdateAction()
    {
    	super() ;
    }

    /**
     * Fill PreparedStatement with Object[]
     */
    protected void fillStatement(PreparedStatement stmt )
        throws SQLException {
    }


    /**
     * get PreparedStatement instance
     */
    protected PreparedStatement prepareStatement(Connection conn, String sql)
        throws SQLException {

        return conn.prepareStatement(sql);
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
    public RowSet doQuery(String sql)
        throws SQLException {
   	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	RowSet crs= null;

        try {
        	conn = wzw.util.DbUtils.getConnection();
            pstmt = conn.prepareStatement( sql);

            rs = pstmt.executeQuery();
 			//crs.populate( rs ) ;
 			crs = DbUtils.resultSet2RowSet( rs );

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	try{
        		if(rs!=null)
        			rs.close();
        		if(pstmt!=null)
        			pstmt.close();
        		if(conn!=null)
        			conn.close();
            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateAction.doQuery����Exception����������ϢΪ��\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return crs;
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
    public RowSet doQuery(Connection conn, String sql)
        throws SQLException {

        return this.doQuery(conn, sql, (Object[]) null);
    }

    /**
     * Execute an SQL SELECT query with a single replacement parameter.  The
     * caller is responsible for connection cleanup.
     *
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param param The replacement parameter.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
    public RowSet doQuery(
        Connection conn,
        String sql,
        Object param)
        throws SQLException {

        return this.doQuery(conn, sql, new Object[] { param });
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for connection cleanup.
     *
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param params The replacement parameters.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */

    public RowSet doQuery(
        Connection conn,
        String sql,
        Object[] params)
        throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        RowSet crs= null ;

        try {
            stmt = this.prepareStatement(conn, sql);
            SQLUtils.fillStatement(stmt, params);

            rs = this.wrap(stmt.executeQuery());
 			//crs.populate( rs ) ;
 			crs = DbUtils.resultSet2RowSet( rs );

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
        	try{
        		org.apache.commons.dbutils.DbUtils.close(rs);
        		org.apache.commons.dbutils.DbUtils.close(stmt);
            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateAction.doQuery����Exception����������ϢΪ��\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return crs;
    }

    public RowSet doQuery(
        Connection conn,
        String sql,
        List list )
        throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        RowSet crs= null ;

        try {
            stmt = this.prepareStatement(conn, sql);
            SQLUtils.fillStatement(stmt, list);

            rs = this.wrap(stmt.executeQuery());
 			//crs.populate( rs ) ;
 			crs = DbUtils.resultSet2RowSet( rs );

        } catch (SQLException e) {
            this.rethrow(e, sql, list);

        } finally {
        	try{
        		org.apache.commons.dbutils.DbUtils.close(rs);
        		org.apache.commons.dbutils.DbUtils.close(stmt);
            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateAction.doQuery����Exception����������ϢΪ��\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return crs;
    }


    /**
     * Execute an SQL SELECT query with a single replacement parameter.  The
     * caller is responsible for connection cleanup.
     *
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param param The replacement parameter.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
    public Object doQuery(
        Connection conn,
        String sql,
        Object param,
        ResultSetHandler rsh)
        throws SQLException {

        return this.doQuery(conn, sql, new Object[] { param }, rsh);
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for connection cleanup.
     *
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param params The replacement parameters.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
    public Object doQuery(
        Connection conn,
        String sql,
        Object[] params,
        ResultSetHandler rsh)
        throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Object result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            SQLUtils.fillStatement(stmt, params);

            rs = this.wrap(stmt.executeQuery());

            result = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
        	org.apache.commons.dbutils.DbUtils.close(rs);
        	org.apache.commons.dbutils.DbUtils.close(stmt);
        }

        return result;
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
    public Object doQuery(Connection conn, String sql, ResultSetHandler rsh)
        throws SQLException {

        return this.doQuery(conn, sql, (Object[]) null, rsh);
    }

    /**
     * Executes the given SELECT SQL with a single replacement parameter.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param sql The SQL statement to execute.
     * @param param The replacement parameter.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     *
     * @return An object generated by the handler.
     * @throws SQLException

    public Object query(String sql, Object param, ResultSetHandler rsh)
        throws SQLException {

        return this.query(sql, new Object[] { param }, rsh);
    }
	*/
    /**
     * Executes the given SELECT SQL query and returns a result object.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param sql The SQL statement to execute.
     * @param params Initialize the PreparedStatement's IN parameters with
     * this array.
     *
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     *
     * @return An object generated by the handler.
     * @throws SQLException

    public Object query(String sql, Object[] params, ResultSetHandler rsh)
        throws SQLException {

        Connection conn = this.getConnection();

        try {
            return this.query(conn, sql, params, rsh);

        } finally {
            DbUtils.close(conn);
        }
    }*/

    /**
     * Executes the given SELECT SQL without any replacement parameters.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     *
     * @return An object generated by the handler.
     * @throws SQLException

    public Object query(String sql, ResultSetHandler rsh) throws SQLException {
        return this.query(sql, (Object[]) null, rsh);
    }*/

    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause The original exception that will be chained to the new
     * exception when it's rethrown.
     *
     * @param sql The query that was executing when the exception happened.
     *
     * @param params The query replacement paramaters; <code>null</code> is a
     * valid value to pass in.
     *
     * @throws SQLException
     */
    protected void rethrow(SQLException cause, String sql, Object[] params)
        throws SQLException {

        StringBuffer msg = new StringBuffer(cause.getMessage());

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.asList(params));
        }

        SQLException e = new SQLException(msg.toString());
        e.setNextException(cause);

        throw e;
    }


    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause The original exception that will be chained to the new
     * exception when it's rethrown.
     *
     * @param sql The query that was executing when the exception happened.
     *
     * @param params The query replacement paramaters; <code>null</code> is a
     * valid value to pass in.
     *
     * @throws SQLException
     */
    protected void rethrow(SQLException cause, String sql, List list)
        throws SQLException {

        StringBuffer msg = new StringBuffer(cause.getMessage());

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (list == null) {
            msg.append("[]");
        } else {
            msg.append( list ) ;
        }

        SQLException e = new SQLException(msg.toString());
        e.setNextException(cause);

        throw e;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters�����ұ����ṩ������.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate( String sql ) throws SQLException {
 
        return DbUtils.executeUpdate(sql);        
    }

    
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(Connection conn, String sql) throws SQLException {
        return this.doUpdate(conn, sql, (Object[]) null);
    }
    
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query with a single replacement
     * parameter.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(Connection conn,  String sql, Object param)
        throws SQLException {

        return this.doUpdate(conn, sql, new Object[] { param });
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(Connection conn, String sql, Object[] params)
        throws SQLException {

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            SQLUtils.fillStatement(stmt, params);

            rows = stmt.executeUpdate();
            //conn.commit();
        	//log.fatal("fillStatement3");

        } catch (SQLException e) {
            //conn.rollback();
            this.rethrow(e, sql, params);

        } finally {
        	try{
        		org.apache.commons.dbutils.DbUtils.close(stmt);

            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateAction.doUpdate() ����Exception����������ϢΪ��\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return rows;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(Connection conn, String sql, List list)
        throws SQLException {

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            SQLUtils.fillStatement(stmt, list);

            rows = stmt.executeUpdate();
            //conn.commit();
        	//log.fatal("fillStatement3");

        } catch (SQLException e) {
            //conn.rollback();
            this.rethrow(e, sql, list);

        } finally {
        	try{
        		org.apache.commons.dbutils.DbUtils.close(stmt);

            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateAction.doUpdate() ����Exception����������ϢΪ��\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return rows;
    }



    /**
     * ִ�����������ұ��������������.
     * @param conn ���ݿ����Ӷ���
     * @param allsql Ҫִ�е�sql�����ɵ����顣
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public int[] doBatch( List list )
        throws SQLException {
        return DbUtils.executeBatch( list );
    }
    
    /**
     * Execute a batch of sql statements.
     * @param conn ���ݿ����Ӷ���
     * @param allsql Ҫִ�е�sql�����ɵ����顣
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public int[] doBatch(Connection conn, String[] allsql)
        throws SQLException {

        PreparedStatement pstmt = null;
        String sql=null;
        int returns[];
        try {
            pstmt = conn.prepareStatement( sql);
            for(int i=0;i<allsql.length;i++){
                pstmt.addBatch(allsql[i]);
            }

            returns = pstmt.executeBatch();
            //conn.commit() ;
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            //conn.rollback();
            log.fatal(this.getClass().getName()+" "+ e.getMessage() ) ;
            //this.rethrow(e, sql, list);

        } finally {
            try{
                if(pstmt!=null)
                    pstmt.close() ;
                //DbUtils.close(stmt);
            }catch(SQLException e)
            {
                log.fatal("��ִ��UpdateBean.doUpdate() ����Exception����������ϢΪ��\n", e);
                this.addErrors(new ActionError("error.database.deal"));
            }
        }
        return null;
    }

    /**
     * Execute a batch of sql statements.
     * @param conn ���ݿ����Ӷ���
     * @param List Ҫִ�е�sql�����ɵ�ʵ����List�ӿڵĶ���
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public int[] doBatch(Connection conn, List allsql)
        throws SQLException {
        if(allsql==null){
            return null;
        }
        return doBatch(conn, (String[])allsql.toArray());
    }


    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement without
     * any replacement parameters. The <code>Connection</code> is retrieved
     * from the <code>DataSource</code> set in the constructor.
     *
     * @param sql The SQL statement to execute.
     * @throws SQLException
     * @return The number of rows updated.

    public int update(Connection conn, String sql) throws SQLException {
        return this.update(sql, (Object[]) null);
    }
	*/
    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement with
     * a single replacement parameter.  The <code>Connection</code> is
     * retrieved from the <code>DataSource</code> set in the constructor.
     *
     * @param sql The SQL statement to execute.
     * @param param The replacement parameter.
     * @throws SQLException
     * @return The number of rows updated.

    public int update(Connection conn, String sql, Object param) throws SQLException {
        return this.update(sql, new Object[] { param });
    }
	*/
    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.
     *
     * @param sql The SQL statement to execute.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * parameters.
     * @throws SQLException
     * @return The number of rows updated.

    public int update(Connection conn, String sql, Object[] params) throws SQLException {

        Connection conn = this.getConnection( request) ;

        try {
            return this.update(request, sql, params);

        } finally {
            DbUtils.close(conn);
        }
    }
	*/

    
    /**
     * Wrap the <code>ResultSet</code> in a decorator before processing it.
     * This implementation returns the <code>ResultSet</code> it is given
     * without any decoration.
     *
     * <p>
     * Often, the implementation of this method can be done in an anonymous
     * inner class like this:
     * </p>
     * <pre>
     * QueryRunner run = new QueryRunner() {
     *     protected ResultSet wrap(ResultSet rs) {
     *         return StringTrimmedResultSet.wrap(rs);
     *     }
     * };
     * </pre>
     *
     * @param rs The <code>ResultSet</code> to decorate; never
     * <code>null</code>.
     * @return The <code>ResultSet</code> wrapped in some decorator.
     */
    public ResultSet wrap(ResultSet rs) {
        return rs;
    }

    /**
     * ��ȡ��һ������ֵ��
     * @param seqName Oracle���ж�������
     * @return ��һ������ֵ
     * @throws SQLException ���ݿ�����쳣
     */
    public int getSequenceValue(String seqName) throws SQLException {
    	return DbUtils.getSequenceValue( seqName );
    }

    /**
     * 
     */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
}
