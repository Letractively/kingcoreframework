/**
 * Copyright (C) 2006 ChangSha WangWin Science & Technology CO,.LTD. All rights reserved.
 * WangWin PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.RowSet;

/**
 * <p>���Ե��������ݼ��Ͻӿڣ�
 * 		 -- extends RowSet�����Ҫʵ�� RowSet ��������Ҫ��ʵ�ֵ�������� 160 �����ҵķ�����</p>
 * @author Zeven on 2006-7-20
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface NavigableDataSet extends DataSet, Navigator{

	
	/**
	 * �趨ͼƬ�Ĵ��λ��, the method cann't be static.
	 */
	public void setImgPath(String path);
	
	/**
	 * ȡ��ͼƬ���λ��
	 */
	public String getImgPath();

	
	/**
	 *����primaryKey
	 */
	///public void setPrimaryKey(String key);
	
	/**
	 *��ȡprimaryKey
	 */
	///public String getPrimaryKey();
	
	/**
	 * ���ؿ�ʼ��
	 * @return int
	 */
	public int getBeginIndex();
	

	/**
	 * ���е�һ��ʹ��List���������ݵķ�����
	 */
	public List getDataList();
	public void setDataList(List dataList);
	
	/**
	 *�õ����е��м�
	 * @return List
	 */
	public RowSet getDatas();

	/**
	 *�õ��������ݼ����󣬰��� dataList, crs��
	 * @return List
	 */
	public Object getDataObject();
	
	/**
	 *�õ����������һ����¼��
	 * @return int
	 */
	public int getEndIndex();
	
	/**
	 * �����Ƿ��Ƿ�ҳ
	 * @return boolean
	 */
	public boolean getIsPaged();
	
	/**
	 * �������һ��ִ�е�sql���
	 * @return String
	 */
	public String getLastSql();
	
	public void setLastSql(String sql);
	
	public String getSelectString();
	
	public void setSelectString(String select);
	
	public String getFromString();
	
	public void setFromString(String from);
	
	public String getWhereString();
	
	public void setWhereString(String where);
	
	public String getGroupByString();
	
	public void setGroupByString(String groupBy);
	
	public String getHavingString();
	
	public void setHavingString(String having);
	
	public String getOrderByString();
	
	public void setOrderByString(String orderBy);

	
	/**
	 * ��ҳ�����ÿҳʵ����ʾ���������÷�����Ҫ����JSP�е���
	 * @return int
	 */
	public int getShowRows();
	
	/**
	 * �Ƿ���Ҫ��ҳ��ʾ
	 * @return boolean
	 */
	public boolean isNeedPaged(int forPageIndex);
	
	/**
	 * ת����ҳ
	 * @return void
	 */
	public void nextPage();
	
	/**
	 * ����ҳ��
	 */
	public void reIndex();
	
	public void resetCurrentPageIndex();
	
	/**
	 * ת��ĳҳ
	 * @param int pageIndex
	 * @return void
	 */
	public void turnToPage(int newPageIndex);
	
	
	
	/**
	 * �������
	 * @parma page:Page
	 * @return void
	 */
	
 
	
//	private void doinit()
//	{
//		beginIndex = 0;
//		endIndex = pageSize - 1;
//		rowCount = 0;
//	}
	
	/**
	 * �������
	 * @parma page:Page
	 * @return void
//	 */
//	public void addData(Page page);
	
	
	/**
	 * �������
	 */
	
	public void setDataset( RowSet crs);
	
	public void setDataset( ResultSet rs) throws SQLException;
	
	
	/**
	 *	��������, �ƶ���DataSet�С�
	 */

//	/**
//	 * ��ҳ
//	 * @param java.lang.String commandName Command��URL
//	 * @return java.lang.String
//	 */
//	public String pages(String commandName);
//	
//	/**
//	 * ��ҳ
//	 * @param int rows ÿҳ��ʾ���������
//	 * @param java.lang.String commandName Command��URL
//	 * @return java.lang.String
//	 */
//	public String pages(int rows, String commandName);
//	
	
//	/**
//	 * ��ҳ
//	 * @param int rows ÿҳ��ʾ���������
//	 * @param java.lang.String commandName Command��URL
//	 * @return java.lang.String
//	 */
//	public String pagesPN(int rows, String commandName);
//	
//	
//	/**
//	 * ��ҳ
//	 * @param int rows ÿҳ��ʾ���������
//	 * @param java.lang.String commandName Command��URL
//	 * @return java.lang.String
//	 */
//	public String pagesPNFL(int rows, String commandName);
//	
//	
	//add wuzewen
	
	
}
