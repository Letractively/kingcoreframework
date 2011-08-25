/*
 * @(#)DataBean.java        1.00 2004/04/09
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


package com.kingcore.framework.base.view;


import java.util.HashMap;

/**
 * @version		1.00 2004.04.09
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class DataBean {


	/* A class implementation comment can go here. */


	/** classVar1 documentation comment */
	private HashMap map;
	private String primaryKey;

	protected int flag = 0;

	public final static int INSERT = 0;
	public final static int UPDATE = 1;
	public final static int DELETE = 2;

	/**
	 * classVar2 documentation comment that happens to be
	 * more than one line long
	 */

	/** instanceVar1 documentation comment */

	/** instanceVar2 documentation comment */

	/** instanceVar3 documentation comment */


	/**
	 * DataBean constructor comment.
	 */
	public DataBean() {
	    super();
	    map = new HashMap();
	}


	/**
	 * �˴����뷽��������
	 * �������ڣ�(2002-7-16 13:34:29)
	 * @return java.lang.Object
	 */
	public Object get(Object key) {
	    return map.get(key);
	}
	/**
	 * �˴����뷽��������
	 * �������ڣ�(2002-7-31 20:05:55)
	 * @return int
	 */
	public int getFlag() {
		return flag;
	}
	public String getPrimaryKey(){
		return (String)map.get(primaryKey);
	}
	/**
	 * @param key:String ��Ӧ���ݿ���е��ֶ�����
	 */
	public void setPrimaryKey(String key){
		primaryKey = key;
	}
	/**
	 * �˴����뷽��������
	 * @return java.lang.Object
	 */
	public void put(Object key, Object value) {
	    map.put(key, value);
	}
	/**
	 * �˴����뷽��������
	 * @param newFlag int
	 */
	public void setFlag(int newFlag) {
		flag = newFlag;
	}
	public String toString() {
	    return "";
	}
}



