/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.util;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * <p>�����ֵĸ������㡢���ȴ���</p>
 * <pre>
 * NumberUtils.add(1.2, 1.32)         =2.52
 * NumberUtils.add(1, 1.122)          =2.122
 * NumberUtils.sub(1.32, 1.2)         =0.12
 * NumberUtils.mul(2,  1.32)          =2.64
 * NumberUtils.div(1.32, 2)           =0.66
 * NumberUtils.div(1.32, 2, 1)        =0.7
 * NumberUtils.round(1.32525, 2)      =1.33
 * NumberUtils.round(1.32525, 3)      =1.325
 * </pre>
 * @author Zeven on 2007-2-10
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class NumberUtils {
	
	/*
	 * ȱʡ�Ľ�λ��Ϊ10���ơ�������2���ơ�8���ơ�16����
	 */
	private static final int DEF_DIV_SCALE = 10;
	
	/**
	 
	 * �ṩ��ȷ�ļӷ����㡣
	 
	 * @param v1 ������
	 
	 * @param v2 ����
	 
	 * @return ���������ĺ�
	 
	 */
	
	public static double add(double v1,double v2)
	{
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.add(b2).doubleValue();
		
	}
	
	/**
	 
	 * �ṩ��ȷ�ļ������㡣
	 
	 * @param v1 ������
	 
	 * @param v2 ����
	 
	 * @return ���������Ĳ�
	 
	 */
	
	public static double sub(double v1,double v2)
	{
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.subtract(b2).doubleValue();
		
	}
	
	
	/**
	 
	 * �ṩ��ȷ�ĳ˷����㡣
	 
	 * @param v1 ������
	 
	 * @param v2 ����
	 
	 * @return ���������Ļ�
	 
	 */
	
	public static double mul(double v1,double v2)
	{
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.multiply(b2).doubleValue();
		
	}
	
	
	/**
	 
	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ��
	 
	 * С�����Ժ�10λ���Ժ�������������롣
	 
	 * @param v1 ������
	 
	 * @param v2 ����
	 
	 * @return ������������
	 
	 */
	
	public static double div(double v1,double v2)
	{
		
		return div(v1,v2,DEF_DIV_SCALE);
		
	}
	
	
	
	/**
	 
	 * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ
	 
	 * �����ȣ��Ժ�������������롣
	 
	 * @param v1 ������
	 
	 * @param v2 ����
	 
	 * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
	 
	 * @return ������������
	 
	 */
	
	public static double div(double v1,double v2,int scale)
	{
		
		if(scale<0)
		{
			
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
			
		}
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	
	
	/**
	 * 
	 * �ṩ��ȷ��С��λ�������봦��
	 * 
	 * @param v
	 *            ��Ҫ�������������
	 * 
	 * @param scale
	 *            С���������λ
	 * 
	 * @return ���������Ľ��
	 * 
	 */
	
	public static double round(double v,int scale){
		
		if(scale<0)
		{
			
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
			
		}
		
		BigDecimal b = new BigDecimal(Double.toString(v));
		
		BigDecimal one = new BigDecimal("1");
		
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//test add
		System.out.println( StringUtils.rightPad("NumberUtils.add(1.2, 1.32) ", 35," ")
								+ "=" +NumberUtils.add(1.2, 1.32) );
		System.out.println( StringUtils.rightPad("NumberUtils.add(1, 1.122) ", 35," ")
				+ "=" +NumberUtils.add(1, 1.122) );
		
		//test sub
		System.out.println( StringUtils.rightPad("NumberUtils.sub(1.32, 1.2) ", 35," ")
								+ "=" +NumberUtils.sub(1.32, 1.2) );
		
		//test mul 
		System.out.println( StringUtils.rightPad("NumberUtils.mul(2,  1.32) ", 35," ")
								+ "=" +NumberUtils.mul(2,  1.32) );
		
		//test div
		System.out.println( StringUtils.rightPad("NumberUtils.div(1.32, 2) ", 35," ")
								+ "=" +NumberUtils.div(1.32, 2) );
		System.out.println( StringUtils.rightPad("NumberUtils.div(1.32, 2, 1) ", 35," ")
								+ "=" +NumberUtils.div(1.32, 2, 1) );
		
		//test round
		System.out.println( StringUtils.rightPad("NumberUtils.round(1.32525, 2) ", 35," ")
								+ "=" +NumberUtils.round(1.32525, 2) );
		System.out.println( StringUtils.rightPad("NumberUtils.round(1.32525, 3) ", 35," ")
								+ "=" +NumberUtils.round(1.32525, 3) );
		
	}
	
}
