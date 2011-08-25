/*
 * @(#)CachedQueryDataSet.java		    1.00 2004/04/13
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

package com.kingcore.framework.bean ;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

/**
 * @version		1.00 2004.04.13
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class CachedQueryDataSet implements Serializable
{
	/**
	 * ��־����
	 */
	public static Logger log = Logger.getLogger(com.kingcore.framework.bean.CachedQueryDataSet.class);

	/**
	* ͼƬ·��
	*/

	protected static String imgPath = "jsp/image/";

	/**
	* ����
	*/
	protected String primaryKey = "";

	/**
	* ������
	*/
	protected int rowCount;

	/**
	* ÿҳ��ʾ������
	*/
	protected int pageSize = 10;

	/**
	* ��ҳ��
	*/
	protected int pageCount;

	/**
	*��ǰҳ��
	*/
	protected int currentPageIndex = 1;

	/**
	* �����ĵ�һ����¼�к�
	*/
	protected int beginIndex;

	/**
	* ���������һ����¼�к�
	*/
	protected int endIndex;

	/**
	* һ�м�¼
	*/
	//protected DataBean data;

	/**
	* �������ݼ���
	*/
	protected CachedRowSet crs ;

	/**
	* ���һ�β�ѯ��SQL���
	*/
	protected String lastSql;
	/**
	* ���һ�β�ѯ��select�Ӿ䣨����select��)
	*/
	protected String selectString = "";
	/**
	* ���һ�β�ѯ��from�Ӿ䣨����from��)
	*/
	protected String fromString = "";
	/**
	* ���һ�β�ѯ��where�Ӿ䣨����where��)
	*/
	protected String whereString = "";
	/**
	* ���һ�β�ѯ��gruop by�Ӿ䣨����group by��)
	*/
	protected String groupByString = "";
	/**
	*  ���һ�β�ѯ��having�Ӿ䣨����having��)
	*/
	protected String havingString = "";
	/**
	* ���һ�β�ѯ��order by�Ӿ䣨����order by��)
	*/
	protected String orderByString = "";
	/**
	* ��ҳ���
	*/
	protected boolean isPaged;
	/**
	* ��ʱ����Ҫ�ı���
	*protected boolean isUpdate; //�����޸ġ�ɾ�����
	*protected boolean isInsert; //�������ӱ��
	*protected boolean isDetail; //������ϸ���
	*/

	/**
	* �趨ͼƬ�Ĵ��λ��
	*/
	public static void setImgPath(String path)
	{
	imgPath = path;
	}

	/**
	* ȡ��ͼƬ���λ��
	*/
	public static String getImgPath()
	{
	return imgPath;
	}

	/**
	*����primaryKey
	*/
	public void setPrimaryKey(String key)
	{
	primaryKey = key;
	}

	/**
	*��ȡprimaryKey
	*/
	public String getPrimaryKey()
	{
	return primaryKey;
	}

	/**
	 * ���ؿ�ʼ��
	 * @return int
	 */
	public int getBeginIndex()
	{
	    return beginIndex;
	}
	/**
	* ��ǰҳ
	* @return int
	*/
	public int getCurrentPageIndex()
	{
	    return currentPageIndex;
	}
	/**
	 *�õ����е��м�
	 * @return List
	 */
	public CachedRowSet getDatas()
	{
	    return this.crs ;
	}
	/**
	 *�õ����������һ����¼��
	 * @return int
	 */
	public int getEndIndex()
	{
	    return endIndex;
	}
	/**
	 * �����Ƿ��Ƿ�ҳ
	 * @return boolean
	 */
	public boolean getIsPaged()
	{
	    return isPaged;
	}
	/**
	 * �������һ��ִ�е�sql���
	 * @return String
	 */
	public String getLastSql()
	{
	    System.out.print("CachedQueryDataSet:getLastSql()  lastSql=" + lastSql);
	    return lastSql;
	}
	public void setLastSql(String sql)
	{
	    lastSql = sql;
	}
	public String getSelectString()
	{
	    return selectString;
	}
	public void setSelectString(String select)
	{
	    selectString = select;
	}
	public String getFromString()
	{
	    return fromString;
	}
	public void setFromString(String from)
	{
	    fromString = from;
	}
	public String getWhereString()
	{
	    return whereString;
	}
	public void setWhereString(String where)
	{
	    whereString = where;
	}
	public String getGroupByString()
	{
	    return groupByString;
	}
	public void setGroupByString(String groupBy)
	{
	    groupByString = groupBy;
	}
	public String getHavingString()
	{
	    return havingString;
	}
	public void setHavingString(String having)
	{
	    havingString = having;
	}
	public String getOrderByString()
	{
	    return orderByString;
	}
	public void setOrderByString(String orderBy)
	{
	    orderByString = orderBy;
	}
	/**
	* ��ҳ��
	* @return int
	*/
	public int getPageCount()
	{
	    return pageCount;
	}

	/**
	* ÿҳ��ʾ������
	* @return int
	*/
	public int getPageSize()
	{
	    return pageSize;
	}

	/**
	*��ǰҳ��
	*/
	public void setCurrentPageIndex(int index)
	{
	    currentPageIndex = index;
	}

	/**
	* ����ܵ�����
	* @return int
	*/
	public int getRowCount()
	{
	    return rowCount;
	}
	/**
	* ��õ�ǰ�к�
	* @return int
	*/
	public int getRow()
	{
		try{
			return crs.getRow();
		}catch( SQLException e){
			System.out.print("\nDataSet -- getrow execption "+e.getMessage()) ;
		}
		return 0 ;
	}
	/**
	* ��ҳ�����ÿҳʵ����ʾ���������÷�����Ҫ����JSP�е���
	* @return int
	*/
	public int getShowRows()
	{
	    if ( crs != null)
	    {
	        return crs.size( );
	    }
	    return 0;
	}
	/**
	* �ж��ǵ�һҳ
	* @return boolean
	*/
	protected boolean isFirstPage()
	{
	    if (currentPageIndex == 1)
	    {
	        return true;
	    }
	    return false;
	}
	/**
	* �ж��Ƿ������һҳ
	* @return boolean
	*/
	protected boolean isLastPage()
	{
	    if (currentPageIndex == getPageCount() )
	    {
	        return true;
	    }
	    return false;
	}
	/**
	* �ж��Ƿ�����ҳ
	* @return boolean
	*/
	protected boolean hasNextPage()
	{
	    if (currentPageIndex < pageCount)
	    {
	        return true;
	    }
	    return false;
	}
	/**
	* �ж��Ƿ�����ҳ
	* @return boolean
	*/
	protected boolean hasPreviousPage()
	{
	    if (currentPageIndex > 1)
	    {
	        return true;
	    }
	    return false;
	}
	/**
	 * �Ƿ���Ҫ��ҳ��ʾ
	 * @return boolean
	 */
	public boolean isNeedPaged(int forPageIndex)
	{
	    if (forPageIndex != currentPageIndex)
	    {
	        isPaged = true;
	    }
	    else
	    {
	        isPaged = false;
	    }
	    return isPaged;
	}
	/**
	* ת����ҳ
	* @return void
	*/
	public void nextPage()
	{
	    if (currentPageIndex < pageCount)
	        currentPageIndex++;
	}

	/**
	 * ����ҳ��
	 */
	public void reIndex()
	{
		if (crs.size()<=0) return ;
		if (currentPageIndex>getPageCount())
			setCurrentPageIndex( getPageCount()) ;
	    beginIndex = pageSize * (currentPageIndex - 1);
	    endIndex = pageSize * currentPageIndex - 1;
	    // crs ��ָ�����¶�λ zewen.wu
	    try
	    {
	    	if (beginIndex ==0)
	    		crs.beforeFirst() ;
	    	else
	    		crs.absolute( beginIndex ) ;

	    }catch( SQLException exp)
	    {
	    	System.out.print("Can't reIndex,beginIndex="+beginIndex ) ;
	    }
	}

	public void resetCurrentPageIndex()
	{
	    currentPageIndex = 1;
	}

	/**
	* ת��ĳҳ
	* @param int pageIndex
	* @return void
	*/
	public void turnToPage(int newPageIndex)
	{
	    currentPageIndex = newPageIndex;
	    isPaged = false;
	}



	/**
	* �������
	* @parma page:Page
	* @return void
	*/


	/**
	 * Constructor for QueryRowset.
	 */
	public CachedQueryDataSet() throws SQLException
	{
		doinit() ;
		this.crs = new CachedRowSetImpl() ;
	}

	public CachedQueryDataSet( CachedRowSet crs) throws SQLException
	{
		doinit() ;
		this.crs = crs ;
	}

	public CachedQueryDataSet( ResultSet rs) throws SQLException
	{
		doinit() ;
		this.crs.populate( rs ) ;
	}
	private void doinit()
	{
	    beginIndex = 0;
	    endIndex = pageSize - 1;
	    rowCount = 0;
	}

	/**
	* �������
	* @parma page:Page
	* @return void
	*/
	public void addData(Page page)
	{
	    if (page != null)
	    {
	    	rowCount = page.getCount();
	        crs = null;//   wzw on 2006-10-26  page.getDatas();
	    	pageCount = page.getPageCount();
	    	currentPageIndex = 1 ;

	        //	��ǰ������data�ڴ� ��ʹ�á�
	        //if (data == null && page.getDatas().size() != 0)
	        //{
	        //    data = (DataBean) page.getDatas().get(0);
	        //}
	    }
	    else
	    {
	    	try{
	    		crs = new CachedRowSetImpl();
	    	}
	    	catch( SQLException exp)
	    	{ }

	    }
	}

	/**
	 * �������
	 */

	public void setDataset( CachedRowSet crs)
	{
		this.crs = crs ;
	}

	public void setDataset( ResultSet rs) throws SQLException
	{
		this.crs.populate( rs ) ;
	}


	/**
	 *	��������
	 */
	public boolean absolute( int row) throws SQLException
	{
		return ( crs.absolute( row ) ) ;
	}

	public void beforeFirst() throws SQLException
	{
		crs.beforeFirst() ;
	}
	public void afterLast() throws SQLException
	{
		crs.afterLast() ;
	}

	public boolean first() throws SQLException
	{
		return (crs.first() ) ;
	}
	public boolean last() throws SQLException
	{
		return (crs.last() ) ;
	}

	public boolean previous() throws SQLException
	{
		return (crs.previous() ) ;
	}
	public boolean next() throws SQLException
	{
		return (crs.next() ) ;
	}


	/**
	 *	BigDecimal ��geter ������
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException
	{
		return ( crs.getBigDecimal( colNum ) ) ;
	}

	public BigDecimal getBigDecimal( String colName ) throws SQLException
	{
		return ( crs.getBigDecimal( colName)) ;
	}

	/**
	 *	Blob ��geter ������
	 */
	public Blob getBlob( int colNum) throws SQLException
	{
		return ( crs.getBlob( colNum )) ;
	}

	public Blob getBlob( String colName ) throws SQLException
	{
		return (crs.getBlob( colName )) ;
	}

	/**
	 *	Boolean ��geter ������
	 */
	public boolean getBoolean( int colNum) throws SQLException
	{
		return ( crs.getBoolean( colNum) ) ;
	}

	public boolean getBoolean( String colName) throws SQLException
	{
		return ( crs.getBoolean( colName ) ) ;
	}


	/**
	 *	Byte ��geter ������
	 */
	public byte getByte( int colNum ) throws SQLException
	{
		return ( crs.getByte( colNum ) ) ;
	}
	public byte getByte ( String colName ) throws SQLException
	{
		return ( crs.getByte( colName ) ) ;
	}


	/**
	 *	Byte[] ��geter ������
	 */
	public byte[] getBytes( int colNum ) throws SQLException
	{
		return ( crs.getBytes( colNum ) ) ;
	}
	public byte[] getBytes( String colName )  throws SQLException
	{
		return ( crs.getBytes( colName ) ) ;
	}

	/**
	 *	CharacterStream ��geter ������

	public CharacterStream getCharacterStream( int colNum)
	{
		return ( crs.getCharacterStream( colNum) ) ;
	}

	public CharacterStream getCharacterStream( String colName)
	{
		return ( crs.getCharacterStream( colName) ) ;
	}
	*/


	/**
	 *	Clob ��geter ������
	 */
	public Clob getClob( int colNum) throws SQLException
	{
		return ( crs.getClob( colNum ) ) ;
	}
	public Clob getClob ( String colName)  throws SQLException
	{
		return ( crs.getClob( colName ) ) ;
	}

	/**
	 *	Date ��geter ������
	 */
	public java.util.Date getDate( int colNum) throws SQLException
	{
		return ( crs.getDate( colNum ) ) ;
	}

	public java.util.Date getDate( String colName) throws SQLException
	{
		return ( crs.getDate( colName ) ) ;
	}

	/**
	 *	Double ��geter ������
	 */
	public double getDouble( int colNum ) throws SQLException
	{
		return ( crs.getDouble( colNum ) ) ;
	}
	public double getDouble( String colName ) throws SQLException
	{
		return ( crs.getDouble( colName ) ) ;
	}

	/**
	 *	Double ��geter ������
	 */
	public float getFloat( int colNum) throws SQLException
	{
		return ( crs.getFloat( colNum ) ) ;
	}
	public float getFloat( String colName) throws SQLException
	{
		return ( crs.getFloat( colName ) ) ;
	}

	/**
	 *	Integer ��geter ������
	 */
	public int getInt( int colNum) throws SQLException
	{
		return ( crs.getInt( colNum )) ;
	}
	public int getInt( String colName) throws SQLException
	{
		return ( crs.getInt( colName )) ;
	}

	/**
	 *	Long ��geter ������
	 */
	public long getLong( int colNum ) throws SQLException
	{
		return ( crs.getLong( colNum ) ) ;
	}
	public long getLong( String colName ) throws SQLException
	{
		return ( crs.getLong( colName ) ) ;
	}

	/**
	 *	Object ��geter ������
	 */
	public Object getObject( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object getObject( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}

	/**
	 *	����� ��geter ������
	 */
	public Object get( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object get( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}


	/**
	 *	Short ��geter ������
	 */
	public short getShort( int colNum ) throws SQLException
	{
		return ( crs.getShort( colNum )) ;
	}
	public short getShort( String colName )  throws SQLException
	{
		return ( crs.getShort( colName ) ) ;
	}

	/**
	 *	String ��geter ������
	 */
	public String getString( int colNum) throws SQLException
	{
		return ( crs.getString( colNum ) ) ;
	}
	public String getString( String colName ) throws SQLException
	{
		return ( crs.getString( colName ) ) ;
	}

	/**
	 *	Time ��geter ������
	 */
	public Time getTime( int colNum ) throws SQLException
	{
		return ( crs.getTime( colNum ) ) ;
	}
	public Time getTime( String colName ) throws SQLException
	{
		return ( crs.getTime( colName ) ) ;
	}

	/**
	 *	Timestamp ��geter ������
	 */
	public Timestamp getTimestamp( int colNum ) throws SQLException
	{
		return ( crs.getTimestamp( colNum ) ) ;
	}

	public Timestamp getTimestamp( String colName ) throws SQLException
	{
		return ( crs.getTimestamp( colName ) ) ;
	}

	//˵����������get������Ҫ����ӡ�


	/**
	 *	�����ܵ�����
	 */
	public int size()
	{
		return ( crs.size() ) ;
	}
	/**
	* ��ҳ
	* @param java.lang.String commandName Command��URL
	* @return java.lang.String
	*/
	public String pages(String commandName)
	{
	    return pages(getPageSize(), commandName);
	}

	/**
	* ��ҳ
	* @param int rows ÿҳ��ʾ���������
	* @param java.lang.String commandName Command��URL
	* @return java.lang.String
	*/
	public String pages(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(��"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",ҳ"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb
	    	.append("<td nowrap align=\"center\" width=\"7%\"><img name=\"selectall\" src=\"")
	        .append(imgPath)
	        .append("selectallno.gif\" ")
	    	.append("onclick=\"javascript:selectAll()\"></td>") ;
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isFirstPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=1")
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirst.gif")
	            .append("' title='��ҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirstno.gif")
	            .append("' title='��ҳ")
	            .append(title)
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageup.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageupno.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\"><img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedown.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedownno.gif")
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isLastPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append( String.valueOf( getPageCount() ) )
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelast.gif")
	            .append("' title='ĩҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelastno.gif")
	            .append("' title='ĩҳ")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("</td>") ;

	    sb
	        .append("<td valign=\"bottom\" nowrap>")
	        .append("��<b>")
	        .append(getPageCount())
	        .append("</b>ҳ����<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>ҳ")
	        .append("</td>");
	    sb
	        .append("<td valign=bottom nowrap>")
	        .append("&nbsp;��<b>")
	        .append(getRowCount())
	        .append("</b>��,��<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>����<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>��")
	        .append("</td>");   //getShowRows()��Ϊ getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td valign=\"bottom\" nowrap>")
	            .append("&nbsp;ת����<input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\" >ҳ")
	            .append("<img name='Image23' border='0' src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("' width='20' height='20' align='absbottom'  style='cursor:hand' onmouseout=\"this.src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("'\" onmouseover=\"this.src='")
	            .append(imgPath)
	            .append("go_h.gif")
	            .append("'\" onclick=\"javascript:location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + document.getElementById('")
	            .append(id)
	            .append("').value; \">")
	            .append("</td>");
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


	/**
	* ��ҳ
	* @param int rows ÿҳ��ʾ���������
	* @param java.lang.String commandName Command��URL
	* @return java.lang.String
	*/
	public String pagesPN(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(��"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",ҳ"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageup.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageupno.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\"><img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedown.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedownno.gif")
	            .append("'>");
	    }
	    //
	    sb
	        .append("<td valign=\"bottom\" nowrap>")
	        .append("��<b>")
	        .append(getPageCount())
	        .append("</b>ҳ����<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>ҳ")
	        .append("</td>");
	    sb
	        .append("<td valign=bottom nowrap>")
	        .append("&nbsp;��<b>")
	        .append(getRowCount())
	        .append("</b>��,��<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>����<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>��")
	        .append("</td>");   //getShowRows()��Ϊ getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td valign=\"bottom\" nowrap>")
	            .append("&nbsp;ת����<input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\" >ҳ")
	            .append("<img name='Image23' border='0' src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("' width='20' height='20' align='absbottom'  style='cursor:hand' onmouseout=\"this.src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("'\" onmouseover=\"this.src='")
	            .append(imgPath)
	            .append("go_h.gif")
	            .append("'\" onclick=\"javascript:location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + document.getElementById('")
	            .append(id)
	            .append("').value; \">")
	            .append("</td>");
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


	/**
	* ��ҳ
	* @param int rows ÿҳ��ʾ���������
	* @param java.lang.String commandName Command��URL
	* @return java.lang.String
	*/
	public String pagesPNFL(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(��"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",ҳ"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isFirstPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=1")
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirst.gif")
	            .append("' title='��ҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirstno.gif")
	            .append("' title='��ҳ")
	            .append(title)
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageup.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageupno.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\"><img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedown.gif")
	            .append("' title='��һҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedownno.gif")
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isLastPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append( String.valueOf( getPageCount() ) )
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelast.gif")
	            .append("' title='ĩҳ")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelastno.gif")
	            .append("' title='ĩҳ")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("</td>") ;

	    sb
	        .append("<td valign=\"bottom\" nowrap>")
	        .append("��<b>")
	        .append(getPageCount())
	        .append("</b>ҳ����<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>ҳ")
	        .append("</td>");
	    sb
	        .append("<td valign=bottom nowrap>")
	        .append("&nbsp;��<b>")
	        .append(getRowCount())
	        .append("</b>��,��<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>����<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>��")
	        .append("</td>");   //getShowRows()��Ϊ getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td valign=\"bottom\" nowrap>")
	            .append("&nbsp;ת����<input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\" >ҳ")
	            .append("<img name='Image23' border='0' src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("' width='20' height='20' align='absbottom'  style='cursor:hand' onmouseout=\"this.src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("'\" onmouseover=\"this.src='")
	            .append(imgPath)
	            .append("go_h.gif")
	            .append("'\" onclick=\"javascript:location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + document.getElementById('")
	            .append(id)
	            .append("').value; \">")
	            .append("</td>");
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


	//add wuzewen


	/**
	* ��ҳ
	* @param int rows ÿҳ��ʾ���������
	* @param java.lang.String commandName Command��URL
	* @return java.lang.String
	*/
	public String pagesPn(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(��"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",ҳ"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr valign=\"center\"><td></td>");
	    //
	    sb.append("<td nowrap valign=\"center\" align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\">[��һҳ]</a>");
	    }
	    else
	    {
	        sb
	            .append("[��һҳ]");
	    }
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\">[��һҳ]</a>");
	    }
	    else
	    {
	        sb
	            .append("[��һҳ]");
	    }
	    //
	    sb
	        .append("</td><td nowrap>")
	        .append("&nbsp;&nbsp;��<b>")
	        .append(getPageCount())
	        .append("</b>ҳ����<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>ҳ")
	        .append("</td>");
	    sb
	        .append("<td nowrap>")
	        .append("&nbsp;��<b>")
	        .append(getRowCount())
	        .append("</b>������<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>����<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>��")
	        .append("</td>");   //getShowRows()��Ϊ getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td align=right valign=\"center\" nowrap>")
	            .append("&nbsp;������</td><td><input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\"></td>")
	            .append("<td>ҳ</td>")
	            .append("<td><input type=button onclick=\"javascript:")
	            .append("((document.getElementById('")
	            .append(id)
	            .append("').value==''||document.getElementById('")
	            .append(id)
	            .append("').value==")
	            .append(currentPageIndex)
	            .append(")?(exit):(location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + ((document.getElementById('")
	            .append(id)
	            .append("').value=='')?")
	            .append(currentPageIndex)
	            .append(":")
	            .append("document.getElementById('")
	            .append(id)
	            .append("').value)))\" value=�鿴 class=input></td>") ;
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}



	/**
	* ��ҳ
	* @param int rows ÿҳ��ʾ���������
	* @param java.lang.String commandName Command��URL
	* @return java.lang.String
	*/
	public String pagesPnfl(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(��"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",ҳ"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isFirstPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=1")
	            .append("\">[�� ҳ]</a>");
	    }
	    else
	    {
	        sb
	            .append("[�� ҳ]");
	    }
	    //
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\">[��һҳ]</a>");
	    }
	    else
	    {
	        sb
	            .append("[��һҳ]");
	    }
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\">[��һҳ]</a>");
	    }
		else
		{
		    sb
		        .append("[��һҳ]");
		}
	    //
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isLastPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append( String.valueOf( getPageCount() ) )
	            .append("\">[ĩ ҳ]</a>");
	    }
	    else
	    {
	        sb
	            .append("[ĩ ҳ]");
	    }
	    //
	    sb
	        .append("</td><td valign=\"center\" nowrap>")
	        .append("&nbsp;&nbsp;��<b>")
	        .append(getPageCount())
	        .append("</b>ҳ����<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>ҳ")
	        .append("</td>");
	    sb
	        .append("<td valign=\"center\" nowrap>")
	        .append("&nbsp;��<b>")
	        .append(getRowCount())
	        .append("</b>������<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>����<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>��")
	        .append("</td>");   //getShowRows()��Ϊ getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td align=right valign=\"center\" nowrap>")
	            .append("&nbsp;������</td><td><input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\"></td>")
	            .append("<td>ҳ</td>")
	            .append("<td><input type=button onclick=\"javascript:")
	            .append("((document.getElementById('")
	            .append(id)
	            .append("').value==''||document.getElementById('")
	            .append(id)
	            .append("').value==")
	            .append(currentPageIndex)
	            .append(")?(exit):(location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + ((document.getElementById('")
	            .append(id)
	            .append("').value=='')?")
	            .append(currentPageIndex)
	            .append(":")
	            .append("document.getElementById('")
	            .append(id)
	            .append("').value)))\" value=�鿴 class=input></td>") ;
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


}
