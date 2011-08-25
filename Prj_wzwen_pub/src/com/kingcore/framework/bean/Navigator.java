/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.bean;

import java.io.Serializable;
import java.util.HashMap;

/** 
 * <p>���ýӿڱ�̣����пɵ���������ʵ�ֱ��ӿڶ���ĵ����������ɱ�NavigatorTag ��ǩʹ�á�</p>
 * @author	WUZEWEN on 2006.11.01
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */
public interface Navigator extends Serializable {
	
//	/**
//	 *  �������캯����
//	 * @param pageParams
//	 */
//	public Navigator(int[] pageParams) {
//		int rowCount = pageParams[0];
//		int pageSize = pageParams[1];
//		int pageNumber = pageParams[2];
//		
//		this.setPageSize( pageSize );
//		this.setCurrentPageIndex( pageNumber );
//		this.setRowCount(rowCount);
//		this.setPageCount( (rowCount - 1) / pageSize + 1 );
//	}


	
	/**
	 * ��ҳ��
	 * @deprecated wzw��pageCount��rowCount/pageSize�������������Ҫ���á�
	 * @return int
	 */
	public void setPageCount(int pageCount);
	
	/**
	 * ��ҳ��
	 * @return int
	 */
	public int getPageCount();
	
	
	/**
	 * ÿҳ��ʾ������
	 * @return int
	 */
	public int getPageSize();
	
   
	public void setPageSize(int pageSize);
	
	/**
	 * �ܵ�����
	 * @return int
	 */
	public int getRowCount();
	
	/**
	 * ������ַ
	 * @return
	 */
	public String getPath();

	public void setRowCount(int rowCount);
	
	public void setPath(String path);
	
	/**
	 *��ǰҳ��
	 */
	public void setPageNumber(int index);
	
	/**
	 * ��ǰҳ
	 * @return int
	 */
	public int getPageNumber();
	
	/**
	 * �ж��Ƿ�����ҳ
	 * @return boolean
	 */
	public boolean hasNextPage();

	/**
	 * �ж��Ƿ�����ҳ
	 * @return boolean
	 */
	public boolean hasPreviousPage();

	/**
	 * �ж��ǵ�һҳ
	 * @return boolean
	 */
	public boolean isFirstPage();
	
	/**
	 * �ж��Ƿ������һҳ
	 * @return boolean
	 */
	public boolean isLastPage();
	
//
//	/**
//	 * @deprecated ���ٴ���ÿҳ��������
//	 * <p>������Web���������棬ÿ�η�ҳ���������ݿ⣬
//	 * 		��Բ�ͬ��������Oracle,SQL Server���ò�ͬ�ķ�װʵ�֡�
//	 * 	wzw on 2006-11-28 ��onclick�¼��е�exit�޸�Ϊ'',ʹ��return Ҳ���С�</p>
//	 * @param commandName ��ҳʱ�õĵ�URL��������з�ҳ��Ϣ������������Ҫ�Ĳ���
//	 * @param rows ÿҳ��ʾ������
//	 * @return ��ѯ������Ϣhtml����
//	 */
//	
//	public String pagesPnfl(int rows,String commandName);  //���ݾɰ汾
//	

	/**
	 * <p>��ҳ������Ϣ�������������Ҫ��ҳ�Ķ���ʵ�ָýӿڣ�
	 * 		��������� Privious,Next,First,Last, toPage, ����������ӡ�</p>
	 * @param commandName ��ҳʱ�õĵ�URL��������з�ҳ��Ϣ������������Ҫ�Ĳ���
	 * @return ��ѯ������Ϣhtml����
	 */
	public String getPagesPnfl( );
	public String getPagesPnfl2( ); 
 

	/**
	 * ��ҳ
	 * @param int rows ÿҳ��ʾ���������
	 * @param java.lang.String commandName Command��URL
	 * @return java.lang.String
	 */
	public String getPagesPn( );

		
}
