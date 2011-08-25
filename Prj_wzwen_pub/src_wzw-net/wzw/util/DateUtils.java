/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * <p>���ڲ������߼���</p>
 * 
 * <pre>
 * getCurrentDate()                   =2007-06-07
 * getCurrentDate('.')                =2007.06.07
 * getCurrentTime()                   =11:13:44
 * getCurrentTime('.')                =11.13.44
 * getCerrentDateAndTime()            =2007-06-07 11:13:44
 * getYear(��2002-09-13��)              =2002
 * getYear(��2002-9-13��)               =2002
 * getYear(��2002-9-1��)                =2002
 * getMonth(��2002-09-13��)             =09
 * getMonth(��2002-9-13��)              =9
 * getMonth(��2002-9-1��)               =9
 * getDay(��2002-09-13��)               =13
 * getDay(��2002-9-13��)                =13
 * getDay(��2002-9-1��)                 =1
 * getDay(��2002-9-01��)                =01
 * getDay(��2002-9-1 12:02:05��)        =1
 * </pre>
 * 
 * @author WZWEN on 2006-09-01
 *
 */
public class DateUtils {	

	/**
	 * ��ȡ��ǰ���ڣ����ص�Ĭ�ϸ�ʽΪ yyyy-mm-dd
	 * @return ��ǰ�����ַ���
	 */
	public static String getCurrentDate(){
	    return getXXXDate(0,"-");
	}
	
	/**
	 * ��ȡ��ǰ���ڣ�����ָ��yyyy, mm, dd ֮������ӷ�
	 * @param sep �ָ������"-",".","/"
	 * @return
	 */
	public static String getCurrentDate( String sep)
	{
	    return getXXXDate(0,sep);
	}
	
	/**
	 * ��ȡ���ڣ�����һ��˽�з�����
	 * @param i ��Ҫ��ǰ����������λΪ��
	 * @param sep ���ӷ�����"-",".","/"
	 * @return
	 */
	private static String getXXXDate(int i, String sep)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(5, -i); //������ǰʱ�仹������i=0 is today
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + sep + formatString(k, 2) + sep + formatString(l, 2);
	}
	/**
	 * ���·ݣ����ڣ�������һλ��ʱ�򣬲��䵽ָ���ĳ��ȣ��� '2'����Ϊ'02'��
	 * @param i ��ʼ�ַ���
	 * @param j ���䵽�ĳ���
	 * @return
	 */
	private static String formatString(int i, int j)
	{
	    String s;
	    for (s = String.valueOf(i); s.length() < j; s = "0" + s);
	    return s;
	}

	/**
	 * ��ǰ�����£�������
	 * @param i ��ǰ������
	 * @param sep �ָ����
	 * @return
	 */
	private static String getYYYDate(int i, String sep)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(2, -i); //������ǰ�����»���������i=0 �Ǳ���
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + sep + formatString(k, 2) + sep + formatString(l, 2);
	}
	
	/**
	 * ��ȡ��ǰʱ�䣬Ĭ�ϸ�ʽΪ hh:mi:ss
	 * @return ��ǰʱ���ַ������� '12:15:59'
	 */
	public static String getCurrentTime(){
	     return getXXXTime(":");
	}
	
	/** 
	 * ��ȡ��ǰʱ��
	 * @param sep ָ��Сʱ�����ӡ���֮��������ַ���
	 * @return
	 */
	public static String getCurrentTime(String sep){
	     return getXXXTime(sep);

	}
	private static String getXXXTime(String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = ":";
	    Calendar calendar = Calendar.getInstance();
	    int h = calendar.get(11);
	    int m = calendar.get(12);
	    int s = calendar.get(13);
	    return formatString(h, 2) + x + formatString(m, 2) + x + formatString(s, 2);
	}
	
	/**
	 * ��ȡ���ڼ�ʱ��
	 * @return �������ں�ʱ���ַ������� '2006-09-12 11:25:30'
	 */
	public static String getCerrentDateTime()
	{
	    return getCurrentDate()+" "+getCurrentTime();	//.substring(0,4)
	}


	/**
	 * ���ز���number����ǰ������ ����ʽΪyyyymmdd��ʽ day �������ʾ��ǰ����
	 * @param day ��ǰ������
	 * @return
	 */
	public static String getDateBeforeDay(int day){
	      String  dateStr = getXXXDate(day,"-");
	      return dateStr;
	}
	/**	 * 
	 * ���ز���number����ǰ������ ����ʽΪyyyymmdd��ʽ day �������ʾ��ǰ����
	 * @param day ��ǰ������
	 * @param sep �ָ��
	 * @return
	 */
	public static String getDateBeforeDay(int day,String sep){
	      String  dateStr = getXXXDate(day,sep);
	      return dateStr;
	}
	
	/**
	 * ���ز���number����ǰ������ ����ʽΪyyyymmdd��ʽ
	 * @param month
	 * @return
	 */
	public static String getDateBeforeMonth(int month){
	      String  dateStr = getYYYDate(month,"-");
	      return dateStr;
	}
	
	/**
	 * ��ȡ�����ַ����� year ���֣������ַ���һ����ʹ��to_char( DateColn,'yyyy-mm-dd')
	 * @param str_date ���ڣ��� 2006-09-20��Ĭ��ʹ��'-'����
	 * @return
	 */
	public static String getYear(String str_date){
		return getYear(str_date,"-");
	}
	public static String getYear(String str_date,String sep){
		return str_date.substring( 0, str_date.indexOf(sep));
	}

	/**
	 * ��ȡ�����ַ����� month ���֣������ַ���һ����ʹ��to_char( DateColn,'yyyy-mm-dd')
	 * @param str_date ���ڣ��� 2006-09-20��Ĭ��ʹ��'-'����
	 * @return
	 */
	public static String getMonth(String str_date){
		return getMonth(str_date,"-");
	}
	public static String getMonth(String str_date,String sep){
		return str_date.substring( str_date.indexOf(sep)+1, str_date.lastIndexOf(sep) );
	}

	/**
	 * ��ȡ�����ַ����� day ���֣������ַ���һ����ʹ��to_char( DateColn,'yyyy-mm-dd')
	 * @param str_date ���ڣ��� 2006-09-20��Ĭ��ʹ��'-'����
	 * @return
	 */
	public static String getDay(String str_date){
		return getDay(str_date,"-");
	}
	public static String getDay(String str_date,String sep){
		if(str_date.indexOf(" ")>0){
			return str_date.substring( str_date.lastIndexOf(sep)+1,str_date.indexOf(" "));			
		}else{
			return str_date.substring( str_date.lastIndexOf(sep)+1 );			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// ����̬�����������£�
		int pad = 35;
		System.out.println( StringUtils.rightPad("getCurrentDate()", pad," ") + "="
												+ getCurrentDate());
		
		System.out.println( StringUtils.rightPad("getCurrentDate('.')", pad," ") + "="
												+ getCurrentDate("."));
		
		System.out.println( StringUtils.rightPad("getCurrentTime()", pad," ") + "="
												+ getCurrentTime());
		System.out.println( StringUtils.rightPad("getCurrentTime('.')", pad," ") + "="
												+ getCurrentTime("."));
		
		System.out.println( StringUtils.rightPad("getCerrentDateAndTime()", pad," ") + "="
												+ getCerrentDateTime());

		System.out.println( StringUtils.rightPad("getYear(��2002-09-13��)", pad," ") + "="
												+ getYear("2002-09-13"));
		System.out.println( StringUtils.rightPad("getYear(��2002-9-13��)", pad," ") + "="
												+ getYear("2002-9-13"));
		System.out.println( StringUtils.rightPad("getYear(��2002-9-1��)", pad," ") + "="
												+ getYear("2002-9-1"));

		System.out.println( StringUtils.rightPad("getMonth(��2002-09-13��)", pad," ") + "="
												+ getMonth("2002-09-13"));
		System.out.println( StringUtils.rightPad("getMonth(��2002-9-13��)", pad," ") + "="
												+ getMonth("2002-9-13"));
		System.out.println( StringUtils.rightPad("getMonth(��2002-9-1��)", pad," ") + "="
												+ getMonth("2002-9-1"));
		
		System.out.println( StringUtils.rightPad("getDay(��2002-09-13��)", pad," ") + "="
												+ getDay("2002-09-13"));
		System.out.println( StringUtils.rightPad("getDay(��2002-9-13��)", pad," ") + "="
												+ getDay("2002-9-13"));
		System.out.println( StringUtils.rightPad("getDay(��2002-9-1��)", pad," ") + "="
												+ getDay("2002-9-1"));
		System.out.println( StringUtils.rightPad("getDay(��2002-9-01��)", pad," ") + "="
												+ getDay("2002-9-01"));
		System.out.println( StringUtils.rightPad("getDay(��2002-9-1 12:02:05��)", pad," ") + "="
												+ getDay("2002-9-1 12:02:05"));
		
	}

}
