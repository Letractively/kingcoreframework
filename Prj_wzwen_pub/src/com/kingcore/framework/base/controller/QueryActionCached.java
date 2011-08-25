/*
 * @(#)QueryActionCached.java		    2004/04/13
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

package com.kingcore.framework.base.controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kingcore.framework.Constants;
import com.sun.rowset.CachedRowSetImpl;
import com.kingcore.framework.bean.Page;
import com.kingcore.framework.bean.QueryDataSet;
import com.kingcore.framework.util.ObjectManager;
import com.kingcore.framework.util.SQLUtils;

/**
 * <p>��װ��һЩִ�в�ѯʱ�Ĳ���������ִ�в�ѯ�б��Acting�඼������չ���ࡣ</p>
 * @author	WUZEWEN on 2004.04.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */


public class QueryActionCached extends BaseAction
{
    private static Logger log = Logger.getLogger( QueryActionCached.class);
    /**
     * ѡ���ѯ
     */
    public static final String SELECT_SEARCH="selectsearch";
    public static final String SEARCH_VALUE="searchvalue";
    /**
     * ��ҳ��Ϊ
     */
    public static final String CHANGE_PAGE = "changepage";
    public static final String SEARCH_ACTION = "searchaction";
    /**
     * ������Ϊ
     */
    public static final String SORT_ACTION = "sortaction";
    /**
     * ȱʡ��Ϊ
     */
    public static final String DEAFLT_ACTION = "defalutaction";

    protected Object[] params = null ;
    //protected List list = null ;
    /**
     * ��ѯ�õ� Sql��䡣
     */
    private String sqlString = null ;

    /**
     * sqlString �����÷�����Ҫ��������ʵ�֡�
     */
    public void setSqlString( String sql )
    {
    }
    /**
     * QueryDataSet ʵ��������
     */
    private String dataSetName;

    public void setDataSetName(String name)
    {
        dataSetName = name;
    }

    public String getDataSetName()
    {
        return dataSetName;
    }

    /**
     * ��ʽ��sql���
     * @param sql:String
     * @return String
     */
    public static String formatSql(String sql)
    {
        String formatedSql = "";
        String tmpSql = sql.toUpperCase();
        int vSelect = 0;
        int vFrom = 0;
        int vWhere = 0;
        int vOrder = 0;
        int vGroup = 0;
        int vHaving = 0;
        int pos = 0;
        vSelect = tmpSql.indexOf("SELECT");
        vFrom = tmpSql.indexOf("FROM");
        if (vFrom > 0)
        {
            vSelect += 6;
            formatedSql = "select " + sql.substring(vSelect, vFrom) + " from ";
            vFrom += 4;
        }
        pos = vFrom;
        vWhere = tmpSql.indexOf("WHERE");
        if (vWhere > 0)
        {
            formatedSql = formatedSql + sql.substring(vFrom, vWhere) + "where";
            vWhere += 5;
        }
        else
        {
            log.debug("��sql��䣺(" + sql + ")���в�����where�����");
            return formatedSql;
        }
        pos = vWhere;
        vGroup = tmpSql.indexOf("GROUP");
        if (vGroup > 0)
        {
            formatedSql = formatedSql + sql.substring(vWhere, vGroup);
            vGroup = tmpSql.indexOf("BY", vGroup);
            if (vGroup < 0)
            {
                log.error("��sql��䣺(" + sql + ")����group ��û��by");
                return "";
            }
            vGroup += 2;
            pos = vGroup;
            vHaving = tmpSql.indexOf("HAVING");
            if (vHaving > 0)
            {
                formatedSql = formatedSql + sql.substring(vGroup, vHaving);
                vHaving += 6;
                pos = vHaving;
            }
        }
        vOrder = tmpSql.indexOf("ORDER");
        if (vOrder > 0)
        {
            formatedSql = formatedSql + sql.substring(pos, vOrder);
            vOrder = tmpSql.indexOf("BY", vOrder);
            if (vOrder < 0)
            {
                log.error("��sql��䣺(" + sql + ")����group ��û��by");
                return "";
            }
            pos = vOrder + 2;
        }
        formatedSql = formatedSql + sql.substring(pos);
        log.debug("��ʽ�����sql���Ϊ��" + formatedSql);
        return formatedSql;
    }

    /**
     * Action �������ڣ�����BaseAction�еķ�����
     * @param sql:String
     * @return String
     */
    public ActionForward executeAction(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        doInitialize(request);
    	//.out.print("\nabcdeb-") ;
        doExecute(mapping, form, request, response);
    	//.out.print("\nabcdec") ;
        return doForword(mapping, form, request, response);
    }

    /**
     * Action �����Ԥ���Ĵ���ռ�
     * @param sql:String
     * @return String
     */
    public void doInitialize(HttpServletRequest request) throws Exception
    {

    }

    public void doExecute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse respone)
        throws Exception
    {
    	//.out.print("\nabcdea-") ;
    	//���ݲ���ȷ�������ֲ���
        String action = getAction(request);
    	//.out.print("\nabcde555-" + action ) ;
        //����ҳ����
        if (action.equals(CHANGE_PAGE))
        {
            String numberString = getParameter(request, "pageNumber", "");
            if (!numberString.equals(""))
            {
                int pageNumber = Integer.parseInt(numberString.trim());
                if (gotoPage(mapping, request, pageNumber))
                {
                    return;
                }
            }
        }
        //�����ѯ
        if (action.equals(SEARCH_ACTION))
        {
            if (doSearchFirst(mapping, request))
            {
                return;
            }
        }
        //������
        if (action.equals(SORT_ACTION))
        {
            if (doSort(mapping, request))
            {
                return;
            }
        }
        //ȱʡ�Ĳ�ѯ
        String sql = this.createQuerySql(request);
        this.executeQuery(mapping, request, sql);
    }

    /**
     * ȱʡ�ķ��������Ը��Ǵ˷���\uFFFD
     */
    public ActionForward doForword(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        return (mapping.findForward("success"));
    }
    /**
     * ����sql����select�Ӿ䣬���С�select���Ѿ�����ʽ��������ϳ�sql����ʱ��Ĭ����ӣ�
     * ������Ͳ����ټӡ�select���ˡ�
     * @return String ����һ��������select����select�Ӿ�
     */
    public String createSelectString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * ����sql�������� @return String ����һ��������sql��ѯ���
     */
    public String createQueryString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * ����sql����from�Ӿ䣬���С�from���Ѿ�����ʽ��������ϳ�sql����ʱ��Ĭ����ӣ�
     * ������Ͳ����ټӡ�from���ˡ�
     * @return String ����һ��������from����from�Ӿ�
     */
    public String createFromString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * ����sql����where�Ӿ䣬���С�where���Ѿ�����ʽ��������ϳ�sql����ʱ��Ĭ����ӣ�
     * ������Ͳ����ټӡ�where���ˡ�
     * @return String ����һ��������where����where�Ӿ�
     */
    public String createWhereString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * ����sql����group by�Ӿ䣬���С�group by���Ѿ�����ʽ��������ϳ�sql����ʱ��Ĭ����ӣ�
     * ������Ͳ����ټӡ�group by���ˡ�
     * @return String ����һ��������group by����group by�Ӿ�
     */
    public String createGroupByString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * ����sql����having�Ӿ䣬���С�having���Ѿ�����ʽ��������ϳ�sql����ʱ��Ĭ����ӣ�
     * ������Ͳ����ټӡ�having���ˡ�
     * @return String ����һ��������having����having�Ӿ�
     */
    public String createHavingString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * ����sql����order by�Ӿ䣬���С�order by���Ѿ�����ʽ��������ϳ�sql����ʱ��Ĭ����ӣ�
     * ������Ͳ����ټӡ�order by���ˡ�
     * @return String ����һ��������order by����order by�Ӿ�
     */
    public String createOrderByString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * �������ֵ
     */
    public void setParam( Object obj)
    {
        setParams( new Object[]{obj} ) ;
    }

    /**
     * �������ֵ
     */
    public void setParams( Object[] objs)
    {
        this.params = objs ;
    }

    /**
     * �������ֵ
     */
    public void setList( List list)
    {
        this.params = list.toArray()  ;
    }

    /**
     * �������ֵ
     */
    protected Object[] getParams()
    {
        return this.params ;
    }
    /**
     * �������ֵ

    protected List getList()
    {
        return this.list ;
    }*/

    /**
     * ��ת��ָ��ҳ��
     * @param request:HttpServletRequest
     * @param pageNumber:int ָ����ת����һҳ
     * @return boolean �����Ƿ���ת�ɹ�
     */
    public boolean gotoPage(
        ActionMapping mapping,
        HttpServletRequest request,
        int pageNumber)
        throws Exception
    {
        String sql = null;
        QueryDataSet dataSet = getDataSetInstance(request);
		log.debug("name is "+getDataSetName() ) ;
        log.debug(dataSet.getPageCount()+":"+ pageNumber) ;
        if (dataSet.getPageCount() == 0)
        {
            return false;
        }
        if (pageNumber > dataSet.getPageCount())
        {
            pageNumber = dataSet.getPageCount();
        }
        if (pageNumber < 1)
        {
            pageNumber = 1;
        }
        log.debug("To QueryActionCached.gotoPage()" + pageNumber) ;
        if (dataSet.isNeedPaged(pageNumber))
        {
			log.debug("getPageCount()="+dataSet.getPageCount()) ;
            sql = dataSet.getLastSql();
            if (sql == null)
            {
                sql = createQuerySql(request);
            }
            executeQuery(mapping, request, sql); //����ȡ����
			log.debug("getPageCount()="+dataSet.getPageCount()) ;
            dataSet.turnToPage(pageNumber);
			//dataSet.setPageNumber( pageNumber ) ;
            dataSet.reIndex();
        }else
        {
        	//���¶�λָ��!!
            dataSet.reIndex();
        }
        return true;
    }
    /**
     * �õ���ǰ��Ӧ��ListView��ʵ��
     * @return ListView
     */
    public QueryDataSet getDataSetInstance(HttpServletRequest request)
    {
        return getDataSetInstance(request, getDataSetName());
    }

    /**
     * ���������б����ƣ��������DataSet
     * @param dataSetName:String
     * @return DataSet
     */
    public QueryDataSet getDataSetInstance(
        HttpServletRequest request,
        String dataSetName)
    {
        return (QueryDataSet)ObjectManager.getObjectInSession(
        		request,
        		dataSetName) ;
    }

    /**
    * ���ݲ������ƺ�׺����ò�����ֵ������
    * @param String suffix �������ƺ�׺
    * @return String[] ���֧�ַ���100��
    */
    public String[] getParameterWithSuffix(
        HttpServletRequest request,
        String suffix)
    {
        java.util.Enumeration names = request.getParameterNames();
        String[] subname = new String[100];
        int bound = 0;
        while (names.hasMoreElements())
        {
            String name = names.nextElement().toString();
            if (name.endsWith(suffix))
            {
                subname[bound] =
                    name.substring(0, name.length() - suffix.length());
                bound++;
            }
        }
        if (bound < 1)
        {
            return null;
        }
        String[] subname2 = new String[bound];
        for (int i = 0; i < bound; i++)
            subname2[i] = subname[i];
        return subname2;
    }
    /**
     * ���Ҫ��ѯ���ֶ���
     * @param request
     * @param searchName
     * @return String[]
     */
    protected String[] getSearchFieldNames(
        HttpServletRequest request,
        String searchName)
    {
        String tmp = ":" + searchName;
        String[] searchs = getParameterWithSuffix(request, tmp);
        String search = null;
        if (searchs == null)
        {
            tmp = tmp + ".y";
            searchs = getParameterWithSuffix(request, tmp);
        }
        if (searchs == null)
            return null;
        search = searchs[0];
        java.util.StringTokenizer s =
            new java.util.StringTokenizer(search, ",");
        int fieldCount = s.countTokens();
        String[] re = new String[fieldCount];
        int i = 0;
        while (s.hasMoreElements())
        {
            re[i] = s.nextToken();
            i++;
        }
        return re;
    }

    /**
     *	����Լ��������ȡ���ҵ��������ŵ�HashMap�з��ء�
     *
     *
     */
    public String[][] getSearchCondition(
        HttpServletRequest request,
        String searchName)
    {
    	String[][] ss = new String[6][2] ;
    	//HashMap hashmap = null ;
    	String suffix = ":"+searchName ;
    	String searchString = "" ;
        java.util.Enumeration names = request.getParameterNames();
        String[] subname = new String[100];
        int bound = 0;
        int i=0 ;
        while (names.hasMoreElements())
        {
            String name = names.nextElement().toString();
            if (name.endsWith(suffix))
            {
                ss[i][0] = name.substring(0, name.length() - suffix.length()) ;
            	ss[i++][1] = request.getParameter( name ) ;
                //hashmap.put(name.substring(0, name.length() - suffix.length()), value);
            }
        }

        return ss ;

    }

    /**
     * ����SQL��䣨Select��䣩�Ͳ������ƣ�search,hsearch,lsearch����ò�����䡣
     * �������ڣ�(2001-10-17 20:20:57)
     * @param java.lang.String theString SQL���
     * @param java.lang.String searchName
     * @return java.lang.String
     */
    public String getSearchString(
        HttpServletRequest request,
        String searchName)
    {
        QueryDataSet dataSet = this.getDataSetInstance(request);
        String mySelect =
            "select "
                + dataSet.getSelectString()
                + " from "
                + dataSet.getFromString();
        String myGroupBy = "";
        if (!dataSet.getGroupByString().equals(""))
        {
            myGroupBy = " group by " + dataSet.getGroupByString();
        }
        String sql = mySelect + " where 1=0 " + myGroupBy;
        HashMap colsInfo = getColumnsInfo(request, sql);

        String[] str = getSearchFieldNames(request, searchName);
        if (str == null)
        {
            log.debug("û���ҵ�Ҫ��ѯ���ֶΣ�");
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int cType = 0; //0:number,1:String
        boolean firstOne = true;
        for (int i = 0; i < str.length; i++)
        {
            String fvalue = getParameter(request, str[i], null);
            if (fvalue == null || fvalue.equals(""))
                continue;
            int ti = str[i].indexOf(".");
            Integer coltypeObject = null;
            if (ti > 0)
            {
                coltypeObject =
                    (Integer) colsInfo.get(str[i].substring(ti + 1));
            }
            else
                coltypeObject = (Integer) colsInfo.get(str[i]);
            if ((coltypeObject.intValue() == java.sql.Types.CHAR)
                || (coltypeObject.intValue() == java.sql.Types.LONGVARCHAR)
                || (coltypeObject.intValue() == java.sql.Types.VARCHAR))
                cType = 1;
            else
                cType = 0;
            if (firstOne)
                sb.append(" where (");
            else
                sb.append(" and (");
            sb.append(str[i]);
            if (cType == 0)
                sb.append(" = ");
            if (cType == 1)
                sb.append(" like '%");
            sb.append(fvalue);
            if (cType == 1)
                sb.append("%'");
            sb.append(")");
            firstOne = false;
        }

        if (sb.length() > 5)
        {
            return mySelect + sb.toString();
        }
        return "";
    }
    /**
    * ����SQL��䣬��ò�ѯ�е���������.
    * @param request: HttpServletRequest
    * @param mySql java.lang.String
    * @return java.util.Hashtable
    * @exception java.lang.Throwable
    */
    public HashMap getColumnsInfo(HttpServletRequest request, String mySql)
    {
        HashMap htable = new HashMap();
        Connection conn = null;
        Statement statement = null;
        ResultSet set = null;
        try
        {
            conn = wzw.util.DbUtils.getConnection(request);
            statement = conn.createStatement();
            set = statement.executeQuery(mySql);
            ResultSetMetaData rsmd = set.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++)
            {
                String cName = rsmd.getColumnName(i);
                int cType = rsmd.getColumnType(i);
                htable.put(cName, new Integer(cType));
            }
        }
        catch (Throwable e)
        {
			log.debug("debug", e);
            /// e.pri ntStackTrace();
        }
        finally
        {
            try
            {
                if (set != null)
                    set.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            }
            catch (java.sql.SQLException se)
            {
				log.debug("debug", se);
                /// se.pri ntStackTrace();
            }
        }
        return htable;
    }

    /**
    * ������õ���View���뵽Session�У���ҳ�棨Jsp������
    * @param viewName:String
    * @param view:BaseView
    * @return void
    */
    public void setDataSetInSession(
        HttpServletRequest request,
        String dsName,
        QueryDataSet newDs)
    {
        ObjectManager.setObjectInSession(request, dsName, newDs);
    }

    /**
     * �����ѯ--(����֮��)�ӵ�һ����ʼ���ҵ�һ��
     *	zewen.wu 2004.04.14 ��������ʹ�����ݿ⣬ֱ�Ӷ�λ��
     */
    public boolean doSearchFirst(ActionMapping mapping, HttpServletRequest request) throws SQLException
    {
    	String colName = (String)request.getParameter("selectsearch") ;

    	if (  colName==null)
    	{
    		log.debug("selectsearch is null") ;
    		return true ;
    	}

    	String value = (String)request.getParameter( "searchvalue") ;
    	QueryDataSet dataSet = this.getDataSetInstance( request) ;

		dataSet.beforeFirst() ;
		dataSet.setPageNumber( 1) ;
		dataSet.reIndex() ;

    	//search from the end for current page
    	boolean isFound = true ;
    	int i,addPageNum= 0 ;
    	int currentPageIndex ;
    	currentPageIndex = dataSet.getPageNumber() ;

    	while(true)
    	{
    		try{
    			if (!dataSet.next()) break ;
    		}catch( SQLException exp )
    		{
    			// to the end then break out!!
    			break ;
    		}
    		i= -1 ;
    		addPageNum ++ ;

            log.debug( (String.valueOf(dataSet.getObject(colName)))+"   "+ value  ) ;

    		if ( !(String.valueOf(dataSet.getObject(colName))).equals( value ))
    		{
    				continue ;
    		}

    		if (isFound)
    		{
    			dataSet.previous() ;
    			addPageNum--;
    			//if (addPageNum!=0)
    			//System.out.print("\naddPageNum= "+addPageNum ) ;
    			addPageNum = addPageNum/(dataSet.getPageSize())  ;
    			//System.out.print("\naddPageNum= "+addPageNum ) ;

    			addPageNum += currentPageIndex ;
    			dataSet.setPageNumber( (addPageNum>dataSet.getPageCount())?dataSet.getPageCount():addPageNum ) ;
    			dataSet.reIndex() ;
    			return true;
    		}

    	}

    	//dataSet.beforeFirst() ;   if not find then keep it!!
    	//if the param is currentPageIndex then keep it
    	//if the param is '1' then goto the first page and new begin.
		
		String str_pageNumber = request.getParameter("pageNumber");
		int i_pageNumber=1;
		if (str_pageNumber!=null){
			try{
				i_pageNumber=Integer.parseInt(str_pageNumber);
			}catch(NumberFormatException e){
				//���账��������ǰ��ֵ
			}
		}

    	dataSet.setPageNumber( i_pageNumber ) ;	//1  wzw on 2006-10-12
    	//���ִ�λ���⡣�����������ȥ�����
    	//removeObjectInSession( request, "searchCondition") ;
    	dataSet.reIndex() ;

    	log.debug("have not find a record") ;
    	this.addErrors("notfound",new ActionError("can't find!"));
    	return true ;
    }

    /**
     * �����ѯ--ѭ�������²���
     *	zewen.wu 2004.04.14 ��������ʹ�����ݿ⣬ֱ�Ӷ�λ��
     */
    public boolean doSearch(ActionMapping mapping, HttpServletRequest request) throws SQLException
    {
    	String[][] ss = new String[6][2] ;

    	log.debug("do Search") ;
    	ss = getSearchCondition( request, "search") ;
    	QueryDataSet dataSet = this.getDataSetInstance( request) ;
    	String newss = "";
    	for(int i=0; i<2; i++)
    		for(int j=0; j<6; j++ )
    			newss = newss + ss[j][i];

    	Object  oldss = (Object)ObjectManager.getObjectInSession( request,
    								"searchCondition" );
    	//if (oldobj==null) oldobj=new String("aa");
    	if (oldss==null)
    	{
    		ObjectManager.setObjectInSession( request,
    								"searchCondition",new String(newss)) ;
    	}
    	else if (!((String.valueOf(oldss)).equals( newss)))
    	{
    		//isSearchNext = false ;
    		//System.out.print("\n"+(String.valueOf(oldss))+"    "+ newss ) ;

    		ObjectManager.setObjectInSession( request,
    								"searchCondition",new String(newss)) ;

    		//System.out.print("\nanother find " ) ;

    		dataSet.beforeFirst() ;
    		dataSet.setPageNumber( 1) ;
    		dataSet.reIndex() ;
    	}

    	//search from the end for current page
    	boolean isFound = false ;
    	int i,addPageNum= 0 ;
    	int currentPageIndex ;
    	currentPageIndex = dataSet.getPageNumber() ;

    	log.debug("currentPageIndex= "+currentPageIndex ) ;
    	while(true)
    	{
    		try{
    			if (!dataSet.next()) break ;
    		}catch( SQLException exp )
    		{
    			// to the end then break out!!
    			break ;
    		}
    		i= -1 ;
    		addPageNum ++ ;
    		isFound = false ;
    		while(ss[++i][0]!="" && ss[i][0]!=null)
    		{
    			isFound = true ;
    			if (!(String.valueOf(dataSet.getObject(ss[i][0])).equals( String.valueOf(ss[i][1]))))
    			{
    				isFound = false ;
    				break ;
    			}
    			log.debug("currentPageIndex= "+currentPageIndex ) ;
    		}

    		if (isFound)
    		{
    			dataSet.previous() ;
    			addPageNum--;
                addPageNum = addPageNum/(dataSet.getPageSize())  ;
    			//System.out.print("\naddPageNum= "+addPageNum ) ;

    			if (oldss!=null && ((String.valueOf(oldss)).equals( newss)))
    			{
    				addPageNum++;
    				//log.debug("to +1 " ) ;
    			}
    			addPageNum += currentPageIndex ;
    			dataSet.setPageNumber( (addPageNum>dataSet.getPageCount())?dataSet.getPageCount():addPageNum ) ;
    			dataSet.reIndex() ;
    			return true;
    		}

    	}

    	//dataSet.beforeFirst() ;   if not find then keep it!!
    	//if the param is currentPageIndex then keep it
    	//if the param is '1' then goto the first page and new begin.

    	dataSet.setPageNumber( 1 ) ;
    	//���ִ�λ���⡣�����������ȥ�����
    	//removeObjectInSession( request, "searchCondition") ;
    	dataSet.reIndex() ;

    	log.debug("have not find a record") ;
    	this.addErrors("notfound",new ActionError("can't find!"));
    	return true ;
    }
    	/*
        String searchString = getSearchString(request, "search");

        if (searchString.length() < 5)
        {
            return false;
        }
        else
        {
            String whereString = createWhereString(request);
            String groupByString = createGroupByString(request);
            String havingString = createHavingString(request);
            String orderByString = createOrderByString(request);
            if (!whereString.equals(""))
            {
                searchString = searchString + " and " + whereString;
            }
            if (!groupByString.equals(""))
            {
                searchString = searchString + " group by " + groupByString;
                if (!havingString.equals(""))
                {
                    searchString = searchString + " having " + havingString;
                }
            }
            if (!orderByString.equals(""))
            {
                searchString = searchString + " order by " + orderByString;
            }
            getDataSetInstance(request).setLastSql(searchString);
            getDataSetInstance(request).resetCurrentPageIndex();
            getDataSetInstance(request).reIndex();
            try
            {
                executeQuery(mapping, request, searchString);
            }
            catch (Exception e)
            {
            }
        }
        return true;
        */

//    }
    /**
     * �õ�Action����
     */
    public String getAction(HttpServletRequest request)
    {
        String action = "";
        action = getParameter(request, "Action", DEAFLT_ACTION);
        return action;
    }
    /**
     *
     */
    public String createSqlString(HttpServletRequest request)
    {
        String sql = "";
        sql = createQueryString( request ) ;
        if (sql.length()>5)
        	return sql ;

        String selectString = "";
        String fromString = "";
        String whereString = "";
        String groupByString = "";
        String havingString = "";
        String orderByString = "";
        selectString = "select " + createSelectString(request);
        fromString = " from " + createFromString(request);
        whereString = createWhereString(request);
        if (!whereString.equals(""))
        {
            whereString = " where " + whereString;
        }
        groupByString = createGroupByString(request);
        if (!groupByString.equals(""))
        {
            groupByString = " group by " + groupByString;
        }
        havingString = createHavingString(request);
        if (!havingString.equals(""))
        {
            havingString = " having " + havingString;
        }
        orderByString = createOrderByString(request);
        if (!orderByString.equals(""))
        {
            orderByString = " order by " + orderByString;
        }
        sql =
            selectString
                + fromString
                + whereString
                + groupByString
                + havingString
                + orderByString;
        return sql;
    }

    /**
     *
     */
    public String createQuerySql(HttpServletRequest request)
    {
    	log.debug("createQuerySql()") ;
        QueryDataSet dataSet = getDataSetInstance(request);
        String sql = "";
        sql = createSqlString(request);
        dataSet.setLastSql(sql);
        dataSet.setFromString(createFromString(request));
        dataSet.setSelectString(createSelectString(request));
        dataSet.setWhereString(createWhereString(request));
        dataSet.setGroupByString(createGroupByString(request));
        dataSet.setHavingString(createHavingString(request));
        dataSet.setOrderByString(createOrderByString(request));
        return sql;
    }

    public String getSortString(HttpServletRequest request)
    {
        String sortString = "";
        String sortName = "";
        sortName = getParameter(request, "sortName", "");
        if (sortName.equals("") || sortName==null)
            return "";
        /*
        setObjectInSession(
            request,
            this.getClass().getName() + "sortName",
            sortName);
        */
        String direction = "";
        Object o =
            getObjectInSession(request, this.getClass().getName() + sortName);
        if (o != null)
            direction = String.valueOf(o).toUpperCase();
        if (direction.equals("DESC"))
            direction = "ASC";
        else
            direction = "DESC";
        setObjectInSession(
            request,
            this.getClass().getName() + sortName,
            direction);

        sortString = " order by " + " " + sortName + " " + direction;

        return sortString;
    }

    public void executeQuery(
        ActionMapping mapping,
        HttpServletRequest request,
        String sql)
        throws Exception
    {}

    public Page doQuery(
        ActionMapping mapping,
        HttpServletRequest request,
        String sql,
        int pageSize,
        int start,
        int end)
        throws Exception
    {
    	log.debug("doQuery()�˴β�ѯ��sql���Ϊ��" + sql );
        CachedRowSet datas = new CachedRowSetImpl();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //ResultSetMetaData rsmd = null;
        int count = 0;
        int i = 0, colcount = 0;
        //String[] colName;
        try
        {
            conn = wzw.util.DbUtils.getConnection(request);
            pstmt = conn.prepareStatement(sql);
            SQLUtils.fillStatement( pstmt, this.getParams() ) ;
            rs = pstmt.executeQuery();

            //log.fatal("have executeQuery get hsdgs--------jyj") ;

        	datas.populate( rs ) ;
        	count = datas.size() ;


            //rsmd = set.getMetaData();
            //colcount = rsmd.getColumnCount();
            //colName = new String[colcount];
            //for (i = 1; i <= colcount; i++)
            //{
            //    colName[i - 1] = rsmd.getColumnName(i);
            //}
            //
            //set = pstmt.executeQuery();  twice??
            //log.fatal("have executeQuery2 " + colName[1]);
            //while (set.next())
            //{
            //    count++;
            //   log.fatal("have executeQuery3 "+set.getObject(colName[1]));
            //    if (count < start + 1 || count > end + 1)
            //        continue;
            //    DataBean bean = new DataBean();
            //    for (i = 0; i < colcount; i++)
            //    {
            //        bean.put(colName[i], set.getObject(colName[i]));
            //    }
            //    datas.add(bean);
            //}
        }
        catch (SQLException se)
        {
            log.fatal("doQuery()�г�SQLException���󣬴���Ϊ��" + se.getMessage());
            mapping.findForward("");
        }
        catch (Exception e)
        {
            log.fatal("doQuery()�г�Exception���󣬴���Ϊ��" + e.getMessage());
            mapping.findForward("");
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (SQLException se)
            {
                log.fatal("doQuery��finally�йر�setʱ��������Ϊ��" + se.getMessage());
            }
            try
            {
                if (pstmt != null)
                {
                    pstmt.close();
                }
            }
            catch (SQLException se)
            {
                log.fatal("doQuery��finally�йر�pstmtʱ����");
            }
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException se)
            {
                log.fatal("doQuery��finally�йر�connʱ����");
            }
        }
        return new Page(datas, pageSize, start, end, count);
    }

    /**
    * �������ݵ������б�
    * �������ڣ�(2001-10-17 20:20:57)
    * @param DataBean
    * @return void
    */
    public void addData(HttpServletRequest request, Page page) throws Exception
    {
        try
        {
            ///// getDataSetInstance(request).addData(page);
        }
        catch (Exception e)
        {
            log.debug(e);
            throw new Exception(e);
        }
    }
    /**
     * �õ��û���¼��Ϣ
     * @param request
     */
    public Object getUserLoginBean(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        return session.getAttribute(Constants.USERLOGINBEAN);
    }
    /**
     * �����������
     * @param mapping: ActionMapping
     * @param request: HttpServletRequest
     */
    public boolean doSort(ActionMapping mapping, HttpServletRequest request)
    {
        String sql = null;
        String sortString = getSortString(request);
        sql = getDataSetInstance(request).getLastSql();
        if (sql != null && !sql.equals(""))
        {
            int order_pos = sql.indexOf(" order ");
            if (order_pos > 0)
                sql =
                    sql.substring(0, order_pos)
                        + " "
                        + sortString;
            else
                sql = sql + sortString;
        }else{
        	return false;
        }
        getDataSetInstance(request).setLastSql(sql);
        getDataSetInstance(request).reIndex();
        try
        {
            executeQuery(mapping, request, sql);
        }
        catch (Exception e)
        {
        }
        return true;
    }
}
