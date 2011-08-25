/**
 * @(#)BaseException.java		    2006/03/21
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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * <p>
 * ������е��쳣���࣬RuntimeException��
 * </p>
 * 
 * @author WUZEWEN on 2006.03.21
 * @version 1.0
 * @see Object#equals(java.lang.Object)
 * @see Object#hashCode()
 * @see HashMap
 * @since JDK5
 */

public class BaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * By giving <code>BaseException</code> a reference to a Throwable object, *
	 * exception chaining can be enforced easily.
	 * 
	 * 
	 * 
	 */
	private Throwable previousThrowable = null;
	/**
	 * �쳣��Ϣ���ͣ�����ApplicationException����ʾ���ͣ�
	 * 		����InvalidSessionException�Ǳ�ʾ��¼֮���Ƿ�����ת��ǰһ��ҳ�档
	 */
	protected int showType = 0;

	/**
	 * ����ͨҳ����ʾ�쳣��Ϣ
	 */
	public static final int SHOW_TYPE_NORMAL = 0;
	/**
	 * �ԶԻ�����ʾ�쳣��Ϣ
	 */
	public static final int SHOW_TYPE_DIALOG = 1;

	public BaseException() {
	}

	public BaseException(String inMessage) {
		super(inMessage);
	}

	public BaseException(String inMessage, int showType) {
		super(inMessage);
		this.showType = showType;
	}
	
	public BaseException(String inMessage, Throwable inThrowable) {
		super(inMessage);
		this.previousThrowable = inThrowable;
	}

	public BaseException(Throwable inThrowable) {
		this.previousThrowable = inThrowable;
	}

	public void printStackTrace() {
		super.printStackTrace();
		if (this.previousThrowable != null) {
			this.previousThrowable.printStackTrace();
		}
	}

	public void printStackTrace(PrintStream inPrintStream) {
		super.printStackTrace(inPrintStream);
		if (this.previousThrowable != null) {
			this.previousThrowable.printStackTrace(inPrintStream);
		}
	}

	public void printStackTrace(PrintWriter inPrintWriter) {
		super.printStackTrace(inPrintWriter);
		if (this.previousThrowable != null) {
			this.previousThrowable.printStackTrace(inPrintWriter);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getShowType() {
		return showType;
	}

}
