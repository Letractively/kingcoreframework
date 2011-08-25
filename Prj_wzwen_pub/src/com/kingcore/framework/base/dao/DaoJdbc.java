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

package com.kingcore.framework.base.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.kingcore.framework.context.ApplicationContext;

/**
 * <p>���ࣺDao Jdbc first interface.</p>
 * @author Zeven on 2008-6-4
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface DaoJdbc {

	//Connection getConnection() throws SQLException;
	
	/**
	 * Dao����֮�乲��Connection�����ʺ� DaoJdbcPlainImpl�����ʺ�DaoJdbcSpringImpl��
	 */
	void setConnection(Connection conn);

	String getDataSourceName();  //��ȡ��ǰDataSourceName
}