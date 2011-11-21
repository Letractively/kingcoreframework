/**
 * Copyright (C) 2011 Kingcore Science & Technology CO,.LTD. All rights reserved.
 * XNS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * Copyright (C) 2002-2011 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.context;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>java���ļ���˵��...</p>
 * @author Zeven/wzw on Mar 3, 2009
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @since	JDK5
 */

public interface DataSourceManager {


    /**
     * ȱʡ��DataSource �����ơ�
     * @ depr ecated ���ڲ���Ĭ��ֵ��ע�����÷�ʽ������ʹ�� static ֵ��
     */
    ///public static String DEFAULT_DATASOURCE_NAME="jndi/jdbc";

    /**
     * <p>��ȡĬ�ϵ� DataSource.</p>
     * @return
     * @throws SQLException
     */
    //public DataSource getDataSource() throws SQLException;

    /**
     *   ������Ϊ�˱���ǰ��ֱ��ʹ��  dataSource.getConnnection������
     * ��ȡ����ΪdataSourceName�� DataSource.
     * @param dataSourceName
     * @return
     * @throws SQLException
     */
    //public DataSource getDataSource(String dataSourceName) throws SQLException;
    
	/**
	 * ��ȡָ�����Ƶ�����Դ�����ݿ����ӡ�
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String dataSourceName) throws SQLException;

	/**
	 * ��ȡϵͳĬ�ϵ�����Դ�����ݿ����ӡ�
	 * @return
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * 
	 * @param dataSourceName
	 */
	public void setDefaultDataSourceName(String dataSourceName);
	
	/**
	 * ��ȡdefaultDataSourceName.
	 * @return
	 */
	public String getDefaultDataSourceName();

	
	public void setConfigPath(String configPath);
	

//	public Connection getConnection(String name, long time) throws SQLException;
}
