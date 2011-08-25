/*
 * @(#)QueryDataSet.java		    1.00 2004/04/13
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

 
/**
 * <p>ʵ���˿����л����ɵ����Ľӿڡ�</p>
 * @version		1.00 2004.04.13
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class QueryDataSet implements NavigableDataSet, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * ��־����
	 */
	public static Logger log = Logger.getLogger(com.kingcore.framework.bean.QueryDataSet.class);
	
	
//	/**
//	 * �����uri�ַ��������� /user/userDeal.jhtml?Action=add&id=123
//	 * ���������ɵ�����Ϣ��
//	 */
//	protected String commandName = null; 
	
	
	/**
	 * ͼƬ·��
	 */
	
	protected static String imgPath = "jsp/image/";
	
	/**
	 * ����
	 */
	protected String primaryKey = "";
	
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
	 * �������ݼ��ϣ�֧��3�� RowSet, List, DataSet(=RowSet and List)
	 */
	protected Object dataObject ;	
	protected RowSet crs ;
	protected RowSet datas ;
	private List dataList ;	// add by Zeven on 2008-08-16�����Ᵽ��List������List<Bean>, List<Map>������dataList�Ĳ���������ֱ���Ȼ�datList��
	

	protected Navigator navigator = null;
	
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


	private String path;
	
	/**
	 * ��ʱ����Ҫ�ı���
	 *protected boolean isUpdate; //�����޸ġ�ɾ�����
	 *protected boolean isInsert; //�������ӱ��
	 *protected boolean isDetail; //������ϸ���
	 */
	
	/**
	 * ���е�һ��ʹ��List���������ݵķ�����
	 */
	public List getDataList() {
		return this.dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	
	/**
	 * �趨ͼƬ�Ĵ��λ��
	 */
	public void setImgPath(String path)
	{
		this.imgPath = path;
	}
	
	/**
	 * ȡ��ͼƬ���λ��
	 */
	public String getImgPath()
	{
		return this.imgPath;
	}
	
	/**
	 *����primaryKey
	 */
	public void setPrimaryKey(String key)
	{
		this.primaryKey = key;
	}
	
	/**
	 *��ȡprimaryKey
	 */
	public String getPrimaryKey()
	{
		return this.primaryKey;
	}
	
	/**
	 * ���ؿ�ʼ��
	 * @return int
	 */
	public int getBeginIndex()
	{
		return this.beginIndex;
	}
	
	/**
	 * ��ǰҳ
	 * @return int
	 */
	public int getPageNumber()
	{
		return this.navigator.getPageNumber();
	}
	
	/**
	 *�õ����е��м�
	 * @return List<Map>,List<Bean>,RowSet,SqlRowSet
	 */
	public RowSet getDatas()
	{
		return this.crs;
	}
	
	/**
	 *�õ����������һ����¼��
	 * @return int
	 */
	public int getEndIndex()
	{
		return this.endIndex;
	}
	
	/**
	 * �����Ƿ��Ƿ�ҳ
	 * @return boolean
	 */
	public boolean getIsPaged()
	{
		return this.isPaged;
	}
	/**
	 * �������һ��ִ�е�sql���
	 * @return String
	 */
	public String getLastSql()
	{
		//.out.print("QueryDataSet:getLastSql()  lastSql=" + lastSql);
		return this.lastSql;
	}
	public void setLastSql(String sql)
	{
		this.lastSql = sql;
	}
	public String getSelectString()
	{
		return this.selectString;
	}
	public void setSelectString(String select)
	{
		this.selectString = select;
	}
	public String getFromString()
	{
		return this.fromString;
	}
	public void setFromString(String from)
	{
		this.fromString = from;
	}
	public String getWhereString()
	{
		return this.whereString;
	}
	public void setWhereString(String where)
	{
		this.whereString = where;
	}
	public String getGroupByString()
	{
		return this.groupByString;
	}
	public void setGroupByString(String groupBy)
	{
		this.groupByString = groupBy;
	}
	public String getHavingString()
	{
		return this.havingString;
	}
	public void setHavingString(String having)
	{
		this.havingString = having;
	}
	public String getOrderByString()
	{
		return this.orderByString;
	}
	public void setOrderByString(String orderBy)
	{
		this.orderByString = orderBy;
	}
	
	/**
	 * ��ҳ��
	 * @return int
	 */
	public void setPageCount(int pageCount)
	{
		this.navigator.setPageCount(pageCount);
	}

	/**
	 * ��ҳ��, �������Action�����С�
	 * @return int
	 */
	public int getPageCount()
	{
		return this.navigator.getPageCount();
	}
	
	/**
	 * ÿҳ��ʾ������
	 * @return int
	 */
	public int getPageSize()
	{
		return this.navigator.getPageSize();
	}


	/**
	 * 
	 */
	public void setPageSize(int pageSize) {
		this.navigator.setPageSize(pageSize);
	}
	
	/**
	 * �ܵ�����
	 * @return int
	 */
	public int getRowCount()
	{
		return this.navigator.getRowCount();
	}
	
	public void setRowCount(int rowCount) {
		this.navigator.setRowCount(rowCount);
	}
	
	/**
	 *��ǰҳ��
	 */
	public void setPageNumber(int index)
	{
		this.navigator.setPageNumber( index );
	}
	
	/**
	 * ��õ�ǰ�к�
	 * @return int
	 */
	public int getRow()
	{
		try{
			return this.crs.getRow();
		}catch( SQLException e){
			log.error("\nDataSet -- getrow execption "+e.getMessage()) ;
		}
		return 0 ;
	}

	
	/**
	 * ��ҳ�����ÿҳʵ����ʾ���������÷�����Ҫ����JSP�е���
	 * @deprecated
	 * @return int
	 */
	public int getShowRows()
	{
		return this.navigator.getPageNumber()==getPageCount()?( getRowCount()-(getPageCount()-1)*getPageSize() ):getPageSize() ;
	}

	/**
	 * �ж��ǵ�һҳ
	 * @return boolean
	 */
	public boolean isFirstPage()
	{
		return this.navigator.isFirstPage();
		
	}
	
	/**
	 * �ж��Ƿ������һҳ
	 * @return boolean
	 */
	public boolean isLastPage()
	{
		return this.navigator.isLastPage();
	}
	
	/**
	 * �ж��Ƿ�����ҳ
	 * @return boolean
	 */
	public boolean hasNextPage()
	{		
		return this.navigator.hasNextPage();
	}
	
	/**
	 * �ж��Ƿ�����ҳ
	 * @return boolean
	 */
	public boolean hasPreviousPage()
	{
		return this.navigator.hasPreviousPage();
	}
	
	/**
	 * �Ƿ���Ҫ��ҳ��ʾ
	 * @return boolean
	 */
	public boolean isNeedPaged(int forPageIndex)
	{
		if (forPageIndex != this.navigator.getPageNumber() )  //this.currentPageIndex)
		{
			this.isPaged = true;
		}
		else
		{
			this.isPaged = false;
		}
		return this.isPaged;
	}
	
	/**
	 * ת����ҳ
	 * @return void
	 */
	public void nextPage()
	{
//		if ( this.currentPageIndex < this.pageCount)
//			this.currentPageIndex++;

		if ( this.navigator.getPageNumber() < this.navigator.getPageCount() ){
			this.navigator.setPageNumber( this.navigator.getPageNumber()+1 ) ;
		}
	}
	
	/**
	 * ����ҳ��
	 */
	public void reIndex()
	{
		try {
			this.crs.beforeFirst();
			
		} catch (SQLException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
	}
	
	public void resetCurrentPageIndex()
	{
		//currentPageIndex = 1;
	}
	
	/**
	 * ת��ĳҳ
	 * @param int pageIndex
	 * @return void
	 */
	public void turnToPage(int newPageIndex)
	{
		//currentPageIndex = newPageIndex;
		this.isPaged = false;
	}
	
	/**
	 * �������
	 * @parma page:Page
	 * @return void
	 */
	
	
	/**
	 * Constructor for QueryRowset.
	 */
	public QueryDataSet() throws SQLException
	{
		doinit() ;
		// wzw on 2007-06-05
		// ����һ�� RowSet ��������������������ݣ������Ĺ���һ��û�б�Ҫ
		// log.debug("Error: the code has been drop by Zeven on 2007-06-05."); 
		// this.crs = new RowSet() ;
		

	}

	public QueryDataSet( RowSet crs) throws SQLException
	{
		doinit() ;
		this.crs = crs ;
	}
	
	
	public QueryDataSet( ResultSet rs) throws SQLException
	{
		log.debug(" QueryDateSet ResultSet rs" );
		doinit() ;
		//this.crs.populate( rs ) ;
	}
	

	/**
	 * Zeven on 2008-05-27�� ��������Ϣ��Dao�Ƶ�Controller�С�
	 * @param pageParams
	 * @param datas
	 * @throws SQLException
	 */
	public QueryDataSet( Navigator navigator, String path, RowSet datas) 
	{
		/// this.datas = datas;
		this.crs = datas;
		this.path = path;
		this.navigator = navigator;
		
		doinit() ;
	}

	/**
	 * Zeven on 2008-08-16�� ��������Ϣ��Dao�Ƶ�Controller�С�
	 * @param pageParams
	 * @param datas
	 * @throws SQLException
	 */
	public QueryDataSet( Navigator navigator, String path, List dataList) 
	{
		/// this.datas = datas;
		this.dataList = dataList;
		this.crs = datas;
		this.path = path;

		this.navigator = navigator;
		
		doinit() ;
	}

	
	/**
	 * ��ʼ������Ϣ��
	 *
	 */
	private void doinit()
	{
		this.beginIndex = 0;
		this.endIndex = getPageSize() - 1;
		if( this.navigator.getRowCount()<0){
			this.navigator.setRowCount( 0 ) ;
		}
	}
	
	
	/**
	 * �������
	 */
	
	public void setDataset( RowSet crs)
	{
		this.crs = crs ;
	}
	
	public void setDataset( ResultSet rs) throws SQLException
	{
		log.debug("do setDataset( ResultSet rs) throws SQLException");
		// this.crs.populate( rs ) ;
	}
	
	
	/**
	 *	�������� --------------------------
	 */
	public boolean absolute( int row) throws SQLException
	{
		return ( this.crs.absolute( row ) ) ;
	}
	
	public void beforeFirst() throws SQLException
	{
		this.crs.beforeFirst() ;
	}
	public void afterLast() throws SQLException
	{
		this.crs.afterLast() ;
	}
	
	public boolean first() throws SQLException
	{
		return ( this.crs.first() ) ;
	}
	public boolean last() throws SQLException
	{
		return ( this.crs.last() ) ;
	}
	
	public boolean previous() throws SQLException
	{
		return ( this.crs.previous() ) ;
	}
	public boolean next() throws SQLException
	{
		//log.debug("======================= "+this.crs.getClass() );
		return ( this.crs.next() ) ;
	}
	
	
	/**
	 *	BigDecimal ��geter ������
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException
	{
		return ( this.crs.getBigDecimal( colNum ) ) ;
	}
	
	public BigDecimal getBigDecimal( String colName ) throws SQLException
	{
		return ( this.crs.getBigDecimal( colName)) ;
	}
	
	/**
	 *	Blob ��geter ������
	 */
	public Blob getBlob( int colNum) throws SQLException
	{
		return ( this.crs.getBlob( colNum )) ;
	}
	
	public Blob getBlob( String colName ) throws SQLException
	{
		return ( this.crs.getBlob( colName )) ;
	}
	
	/**
	 *	Boolean ��geter ������
	 */
	public boolean getBoolean( int colNum) throws SQLException
	{
		return ( this.crs.getBoolean( colNum) ) ;
	}
	
	public boolean getBoolean( String colName) throws SQLException
	{
		return ( this.crs.getBoolean( colName ) ) ;
	}
	
	
	/**
	 *	Byte ��geter ������
	 */
	public byte getByte( int colNum ) throws SQLException
	{
		return ( this.crs.getByte( colNum ) ) ;
	}
	public byte getByte ( String colName ) throws SQLException
	{
		return ( this.crs.getByte( colName ) ) ;
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
	public Date getDate( int colNum) throws SQLException
	{
		return ( crs.getDate( colNum ) ) ;
	}
	
	public Date getDate( String colName) throws SQLException
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
	
//	/**
//	 *	����� ��geter ��������getObejct��ͬ���ڽӿ���ȥ���ˡ�
//	 */
//	public Object get( int colNum) throws SQLException
//	{
//		return ( crs.getObject( colNum) ) ;
//	}
//	public Object get( String colName)  throws SQLException
//	{
//		return ( crs.getObject( colName ) ) ;
//	}
//	
	
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
	
	// --------------------------------------------  get ���� end 
	//˵����������get������Ҫ����ӡ�
	
	
	/**
	 *	�����ܵ�����
	 */
	public int size()
	{
		if ( this.crs instanceof List ) {
			return ((List)this.crs).size();
			
		}else{
			return this.navigator.getRowCount();
			
		}   // ����ֻ����ʱʹ��
		
		// eturn rowCount ;
	}


	/**
	* <p>������Web���������棬ÿ�η�ҳ���������ݿ⣬
	* 		��Բ�ͬ��������Oracle,SQL Server���ò�ͬ�ķ�װʵ�֡�
	* 	wzw on 2006-11-28 ��onclick�¼��е�exit�޸�Ϊ'',ʹ��return Ҳ���С�</p>
	* @param commandName ��ҳʱ�õĵ�URL��������з�ҳ��Ϣ������������Ҫ�Ĳ���
	* @return ��ѯ������Ϣhtml����
	*/
	public String getPagesPnfl( )
	{
		return this.navigator.getPagesPnfl( ); //8, commandName
	}
	public String getPagesPnfl2( )
	{
		return this.navigator.getPagesPnfl2( );//commandName
	}


	public String getPagesPn( ) {

		return this.navigator.getPagesPn( ); //commandName
	}
 
	
	/**
	 * Zeven:����ҳ����ã�ֱ��ʹ�� dataObject��������Ҫ����ָ����dataList����crs����
	 */
	public Object getDataObject() {
		if(this.crs==null){
			return this.dataList;
		}else{
			return this.crs;
		}
	}

	public String getPath() {
		return this.path;
	}

	/**
	 * wzw 
	 */
	public void setPath(String path) {
		this.path = path ;
		this.navigator.setPath(path);  //�޸ĵ��������path����
		
	}

}
