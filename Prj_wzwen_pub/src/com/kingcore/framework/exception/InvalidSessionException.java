/**
 * @(#)ApplicationException.java		    2006/03/21
 *
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Zeven.woo
 */

package com.kingcore.framework.exception;

import java.util.HashMap;

/** 
 * <p>Ӧ�ó����쳣������Ӧ�ó����쳣��չ���ࡣ</p>
 * @author	WUZEWEN on 2006.03.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */


public class InvalidSessionException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ��¼ҳ���Ӧ�� ActionForward ��Ӧ�����ƣ���struts MVC�����ļ������á�
	 */
	protected String forwardName = "user-login";		// the default forwardName.

	/**
	 * <p>δ��֤�ĻỰ�쳣��</p>
	 * @param showType �쳣��������
	 * @param inMessage �쳣��Ϣ����
	 * @param forwardName ActionForward ��Ӧ������
	 */
	public InvalidSessionException(String inMessage,int showType, String forwardName) {
		super(inMessage, showType);  //
		this.forwardName = forwardName ;
	}
	public InvalidSessionException(String inMessage,int showType) {
		super(inMessage, showType);
	}
	public InvalidSessionException(String inMessage) {
		super(inMessage);
	}
	
	public InvalidSessionException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getForwardName() {
		return forwardName;
	}
	public void setForwardName(String forwardName) {
		this.forwardName = forwardName;
	}

}
