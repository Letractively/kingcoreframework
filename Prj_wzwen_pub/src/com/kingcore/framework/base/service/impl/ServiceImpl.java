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

package com.kingcore.framework.base.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.kingcore.framework.base.service.Service;
import com.kingcore.framework.context.ApplicationContext;

/**
 * <p>java���ļ���˵��...</p>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class ServiceImpl implements Service{


    /**
     * log4j��־����
     */
    protected static Logger log=Logger.getLogger( ServiceImpl.class );

    /**
     * ���������ģʽ�滻ȫ��̬����������spring ����
     */
	private static ServiceImpl instance;
	

    /**
     * <p>��ȡ�������󣬲ο��������������������ȥ������Ӧ��
     * 		����getInstance() ��ʵ�ֵ������ģʽ��
     * </p>
     * @return 
     */
	public static ServiceImpl getInstanceExample(){
		return instance;
	}

	
	/**
	 * <p>��ȡ���ݿ�����ᣬ�Ա����ҵ���߼��������ݿ���������</p>
	 * 
	 * @param poolName ���ӳض�������ơ�
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException{
		
		return ApplicationContext.getInstance().getDataSourceManager().getConnection( );
		
	}
	
	/**
	 * <p>��ȡ���ݿ�����ᣬ�Ա����ҵ���߼��������ݿ���������</p>
	 * 
	 * 
	 * @param poolName ���ӳض�������ơ�
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(String poolName) throws SQLException{
		
		return ApplicationContext.getInstance().getDataSourceManager().getConnection(poolName);
		
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
