/*
 * Created on 2003-6-19
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.kingcore.framework.util;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author WUZEWEN
 */
public interface DatabaseInterface {
	/**
	 * ��־����
	 */
	public static final Log log = LogFactory.getLog(DatabaseInterface.class);
	/**
	 * �õ���Ӧ��sql���
	 * Create on 2003-6-20
	 * @param method
	 * @param parameteras
	 * @return
	 * @throws Throwable
	 */
	public String getSql(String method, Hashtable parameteras) throws Throwable;
}
