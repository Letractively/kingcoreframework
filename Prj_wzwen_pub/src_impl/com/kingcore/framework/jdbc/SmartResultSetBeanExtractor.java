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

package com.kingcore.framework.jdbc;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * <p>�Զ�����õ�ʵ�ַ�ʽ�����չ���������ݿ����������ҵ�Bean����������</p>
 * @author Zeven on 2006-9-16
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class SmartResultSetBeanExtractor implements ResultSetBeanExtractor {


    protected static Logger log=Logger.getLogger( SmartResultSetBeanExtractor.class );

	public Object extractData(ResultSet rs, Class<?> beanClass, boolean isList)
		           throws SQLException{
		try {
			return bindDataToDto(rs, beanClass, isList);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	/*
	 * ��ResultSet�󶨵�JavaBean  
	 *   
	 * @param ResultSet  
	 * @param Class��JavaBean��  
	 * @return bean  
	 */   
	private Object bindDataToDto(ResultSet rs, Class<?> bean, boolean isList) throws SQLException, InstantiationException, IllegalAccessException {  // throws Exception  
 
	    //ȡ��Method����   
	    Method[] methods = bean.getClass().getMethods();  

	    //ȡ��ResultSet������   
	    ResultSetMetaData rsmd = rs.getMetaData();   
	    int columnsCount = rsmd.getColumnCount();   
	    String[] columnNames = new String[columnsCount];   
	    for (int i = 0; i < columnsCount; i++) {   
	        columnNames[i] = formatColumnLabel(rsmd.getColumnLabel(i + 1));   
	    }  

	    Object obj = null;
	    List<Object> objList = new ArrayList<Object>();
	    //����ResultSet   
	    while (rs.next()) {   
	        //����, ��ResultSet�󶨵�JavaBean   
	    	obj = bean.newInstance();
	        for (int i = 0; i < columnNames.length; i++) {   
	            //ȡ��Set����   
	            String setMethodName = "set" + columnNames[i];   
	            //����Method   
	            for (int j = 0; j < methods.length; j++) {   
	                if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
	                    setMethodName = methods[j].getName();   
	                    Object value = rs.getObject(columnNames[i]);  

	                    //ʵ��Set����   
	                    try {   
	                        //JavaBean�ڲ����Ժ�ResultSet��һ��ʱ��   
	                        Method setMethod = bean.getClass().getMethod(   
	                                setMethodName, value.getClass());   
	                        setMethod.invoke(obj, value);   
	                    } catch (Exception e) {   
	                        //JavaBean�ڲ����Ժ�ResultSet�в�һ��ʱ��ʹ��String������ֵ��   
	                        Method setMethod;
							try {
								setMethod = bean.getClass().getMethod(   
								        setMethodName, String.class);
		                        setMethod.invoke(obj, value.toString());   
		                        
							} catch (Exception e1) {
								log.warn("��������ʧ�ܣ�beanName="+bean.getName()+" ����="+setMethodName,
												e1);
							}
	                    }   
	                }   
	            }   
	        }
	        if (isList) {
				objList.add(obj);
			} else {
				return obj ;
			}
	    }
	    return objList;   
	}

	/**
	 * �����ݿ��������ʽΪBean�������������Ը����Լ��Ĺ���over write��������
	 *    �������ݿ����� user_name -->������ userName
	 *                user_name -->������ user_name
	 * @param columnLabel
	 * @return
	 */
	public String formatColumnLabel(String columnLabel) {
		return columnLabel;
	}
	

}
