/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ConvertUtils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat();
    private static DecimalFormat numberFormat = new DecimalFormat();
	
	/**
	 * ���ݿ�����
	 */
	public static String database="ORA";			//Ĭ��Ϊoracle���ݿ⣬�������oracleΪgb2312���Ͳ���Ҫת���ˡ�
	
	/**
	 * �������ݿ�����
	 * 2003-6-5
	 * @param String ���ݿ�����
	 * @return void
	 * @throws Exception
	 */
	public static void setDatabase(String db) {
		database = db;
	}
	/**
	 * �������ݿ�����ת���ַ���
	 * Create on 2003-5-20
	 * @param String ��ת�����ַ���
	 * @return String
	 * @throws Exception
	 * Modify on 2003-6-5
	 */
	public static String ConvertString(String str) throws Exception {
		String convertString = "";
		byte[] tmp;
		try {
			if (database.equals("SYB")) {
				tmp = str.getBytes("ISO-8859-1");
				convertString = new String(tmp);
			} else {
				convertString = str;
			}
		} catch (Exception e) {
		}
		return convertString;
	}
	/**
	 * ��ʽ�����ֵİ�װ����
	 * Create on 2003-6-18
	 * @param Double ����
	 * @param int ������λ��ЧС�� 
	 * @return
	 */
	public static String formatNumber(Double d, int scalar) throws Exception {
		double temp = d.doubleValue();
		return formatNumber(temp, scalar);
	}
	/**
	 * ��ʽ�����ֵİ�װ����
	 * Create on 2003-6-18
	 * @param Float ����
	 * @param int ������λ��ЧС�� 
	 * @return
	 */
	public static String formatNumber(Float d, int scalar) throws Exception {
		float temp = d.floatValue();
		return formatNumber(temp, scalar);
	}
	/**
	 * ��ʽ������
	 * Create on 2003-6-18
	 * @param double ����
	 * @param int ������λ��ЧС��
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(double number, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(number);
	}
	/**
	 * ��ʽ������
	 * Create on 2003-6-18
	 * @param float ����
	 * @param int ������λ��ЧС��
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(float number, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(number);
	}
	/**
	 * ��ʽ�����
	 * @param double ���
	 * @param int ������λ��ЧС��
	 * @param int ����ʱ�Զ���λ����ı�׼
	 * @return String
	 */
	public static String formatMoney(double money, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(money);
	}
	/**
	 * ��ʽ�����
	 * @param String ���
	 * @param int ������λ��ЧС��
	 * @param int ����ʱ�Զ���λ����ı�׼
	 * @return String
	 */
	public static String formatMoney(float money, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(money);
	}
	/**
	 * ��ʽ�����
	 * @param String ���
	 * @param int ������λ��ЧС��
	 * @param boolean �Ƿ���
	 * @param int ����ʱ�Զ���λ����ı�׼
	 * @return String
	 */
	public static String formatMoney(
		String money,
		int scalar,
		boolean isround,
		int standard)
		throws Exception {
		String formatMoney = null;
		if (isround)
			formatMoney = round(money, scalar, standard);
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		java.text.NumberFormat nf = new java.text.DecimalFormat(format);
		return (nf.format(formatMoney));
	}
	
	/**
	 * ��ʽ�����
	 * @param double ���
	 * @param int ������λ��ЧС��
	 * @param boolean �Ƿ���
	 * @param int ����ʱ�Զ���λ����ı�׼
	 * @return String
	 */
	public static String formatMoney(
		double money,
		int scalar,
		boolean isround,
		int standard)
		throws Exception {
		String formatMoney = null;
		if (isround)
			formatMoney = round(String.valueOf(money), scalar, standard);
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		java.text.NumberFormat nf = new java.text.DecimalFormat(format);
		return (nf.format(formatMoney));
	}
	
	/**
	 * ��ʽ�����
	 * @param String ���
	 * @param int ������λ��ЧС��
	 * @param boolean �Ƿ���������
	 * @return String
	 */
	public static String formatMoney(String money, int scalar, boolean isround)
		throws Exception {
		return formatMoney(money, scalar, isround, 5);
	}
	/**
	 * ��ʽ�����
	 * @param String ���
	 * @param int ������λ��ЧС��
	 * @param boolean �Ƿ���������
	 * @return String
	 */
	public static String formatMoney(double money, int scalar, boolean isround)
		throws Exception {
		return formatMoney(String.valueOf(money), scalar, isround, 5);
	}
	/**
	 * ���뷽��
	 * ���Ը����Զ������ֵ��������룬
	 *  ���磺��6��Ϊ����ı�׼����ôС��6�Ķ������
	 *  ���ڻ����6�Ķ���λ��
	 * @param double ����Ҫ����������������ַ���
	 * @param int ����С�����λ
	 * @param int ��Ϊ��׼����
	 * @return String
	 */
	public static double round(double value, int scalar, int standard)
		throws Exception {
		return Double.parseDouble(
			round(String.valueOf(value), scalar, standard));
	}
	
	public static String round(String value, int scalar, int standard)
		throws Exception {

		String back = null;
		int point = value.indexOf(".");
		if (point > -1) {
			int len = value.length() - point + 1;
			if (len <= scalar) {
				back = value;
			} else {
				back = value.substring(0, point + scalar + 1);
				String vsTemp =
					value.substring(point + scalar + 1, point + scalar + 2);
				if (Integer.parseInt(vsTemp) >= standard) {
					back = String.valueOf(Integer.parseInt(back) + 1);
				}
			}
		} else {
			back = value;
		}
		return back;
	}
	/**
	 * ���Ľ��Ĵ�дת���İ�װ����
	 * @param String ���
	 * @return String
	 */
	public static String makeUpperCaseSum(String je) {
		return makeUpperCaseSum(Double.parseDouble(je));
	}
	/**
	 * ���Ľ��Ĵ�дת��
	 * @param double ���
	 * @return String
	 */
	public static String makeUpperCaseSum(double je) {

		if (je < 0)
			je *= -1;

		final String[] upper_number =
			{ "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��", "ʰ" };
		final String[] number_dw =
			{
				"",
				"Բ",
				"ʰ",
				"��",
				"Ǫ",
				"��",
				"ʰ",
				"��",
				"Ǫ",
				"��",
				"ʰ",
				"��",
				"Ǫ",
				"��" };
		String operate, upper_str, vsDx;
		int i, j, point_pos, int_len;
		if (je < 0.01)
			return "��Բ��";

		upper_str = "";

		DecimalFormat aFormat =
			(DecimalFormat) new java.text.DecimalFormat("##0.00");
		operate = new String(aFormat.format(je));

		point_pos = operate.indexOf(".");
		if (point_pos == -1)
			int_len = operate.length();
		else {
			if (operate.length() - point_pos <= 2)
				operate += "0";
			int_len = point_pos;
		}
		if (int_len > number_dw.length - 1)
			return "too long ������";

		if (je > 0) {
			for (i = 0; i < int_len; i++) {
				j = int_len - i;
				if (i > 0 && "0".equals(operate.substring(i, i + 1))) {
					String sss = number_dw[j];
					if (!"0".equals(operate.substring(i - 1, i))
						&& !"��".equals(sss)
						&& !"��".equals(sss)
						&& !"Բ".equals(sss))
						upper_str = upper_str + "��";
					else if (
						"��".equals(number_dw[j])
							|| "��".equals(number_dw[j])
							|| "Բ".equals(number_dw[j])) {
						int ssss = upper_str.length() - 1;
						String sssss = upper_str.substring(ssss);
						if (!sssss.equals("��")) {
							if (!upper_str
								.substring(upper_str.length() - 1)
								.equals("��")
								&& !upper_str.substring(
									upper_str.length() - 1).equals(
									"��")) {
								if (upper_str
									.substring(upper_str.length() - 1)
									.equals("ʰ")
									&& !number_dw[j].equals("Բ"))
									upper_str = upper_str + number_dw[j] + "��";
								else
									upper_str = upper_str + number_dw[j];
							}
						} else {
							if (!upper_str
								.substring(
									upper_str.length() - 2,
									upper_str.length() - 1)
								.equals("��")
								&& !upper_str.substring(
									upper_str.length() - 2,
									upper_str.length() - 1).equals(
									"��")
								|| number_dw[j].equals("Բ")) {
								upper_str =
									upper_str.substring(
										0,
										upper_str.length() - 1)
										+ number_dw[j];
								if (!number_dw[j].equals("Բ")) {
									upper_str = upper_str + "��";
								}
							}
						}
					}
				} else {
					if (!operate.substring(i, i + 1).equals("0")) {
						int k = Integer.parseInt(operate.substring(i, i + 1));
						upper_str =
							upper_str + upper_number[k - 1] + number_dw[j];
					}
				}
			}
		}
		if (point_pos > 0) {
			if (!operate.substring(point_pos + 1, point_pos + 2).equals("0")) {
				int k =
					Integer.parseInt(
						operate.substring(point_pos + 1, point_pos + 2));
				upper_str = upper_str + upper_number[k - 1] + "��";
				if (!operate
					.substring(point_pos + 2, point_pos + 3)
					.equals("0")) {
					int m =
						Integer.parseInt(
							operate.substring(point_pos + 2, point_pos + 3));
					upper_str = upper_str + upper_number[m - 1] + "��";
				}
			} else {
				if (!operate
					.substring(point_pos + 2, point_pos + 3)
					.equals("0")) {
					int k =
						Integer.parseInt(
							operate.substring(point_pos + 2, point_pos + 3));
					upper_str = upper_str + "��" + upper_number[k - 1] + "��";
				}
			}
		}

		if (!upper_str.substring(upper_str.length() - 1).equals("��")
			&& !upper_str.equals(""))
			upper_str = upper_str + "��";

		return upper_str;
	}
	
	/**
	 * ����Ӣ���ִ�ת���ɴ�Ӣ���ִ����þ�̬����
	 * 
	 * 
	 */
	public static String toTureAsciiStr(String str)
	{  // ���彫��Ӣ���ִ�ת���ɴ�Ӣ���ִ����þ�̬����
    	StringBuffer sb=new StringBuffer();
    	byte[] bt=str.getBytes();                       // ��Ҫת�����ַ���ת��Ϊ�ֽ���ʽ
    	for(int i=0;i<bt.length;i++){
    	  if(bt[i]<0){                                  // �ж��Ƿ�Ϊ���֣�������ȥ��λ1
    	    sb.append((char)(bt[i]&(0x7f)));
    	  }
      	  else{                                         // ��Ӣ���ַ���0����¼
        	sb.append((char)0);
        	sb.append((char)bt[i]);
      }
    }
    return  sb.toString();                          // ����ת�����Ӣ���ַ���
  }
	
	/**
	 *
	 * ����ת�����ִ���ԭ����
	 *
	 */
	public static String unToTrueAsciiStr(String str)
	{// ���彫��ת�����ִ���ԭ����
    	byte[] bt=str.getBytes() ;
    	int i;
    	int l=0;
    	int length=bt.length;
    	int j=0;
    	for(i=0;i<length;i++){                          // �ж��м���Ӣ���ַ���ȥ��Byte 0
    	  if(bt[i]==0){
    	    l++;
    	    }
    	}
    	byte[] bt2=new byte[length-l];                  // ���巵�ص��ֽ�����
    	for(i=0;i<length;i++){
    	  if(bt[i]==0){                                 // ��Ӣ���ַ�ȥ��0
    	    i++;
    	    bt2[j]=bt[i];
    	  }
    	  else{                                         // �Ǻ��ֲ��ϸ�λ1
    	    bt2[j]=(byte)(bt[i]|0x80);
      	}
      	j++;
    	}
    	String tt=new String(bt2);
    	return tt;                                     // ���ػ�ԭ����ַ���
  }

	/**��һ����ַ���תΪ HTML�ַ���
	 *
	 *
	 *
	 */  
	public static String toHTMLString(String in) {
	    StringBuffer out = new StringBuffer();
	    for (int i = 0; in != null && i < in.length(); i++) {
	        char c = in.charAt(i);
	        if (c == '\'') {
	            out.append("&#39;");
	        }
	        else if (c == '\"') {
	            out.append("&#34;");
	        }
	        else if (c == '<') {
	            out.append("&lt;");
	        }
	        else if (c == '>') {
	            out.append("&gt;");
	        }
	        else if (c == '&') {
	            out.append("&amp;");
	        }
	        else {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}
	
	/**
	 * Converts a String to a Date, using the specified pattern.
	 * (see java.text.SimpleDateFormat for pattern description)
	 *
	 * @param dateString the String to convert
	 * @param dateFormatPattern the pattern
	 * @return the corresponding Date
	 * @exception ParseException, if the String doesn't match the pattern
	 */
	public static Date toDate(String dateString, String dateFormatPattern) 
	    throws ParseException {
	    Date date = null;
	    if (dateFormatPattern == null) {
	        dateFormatPattern = "yyyy-MM-dd";
	    }
	    synchronized (dateFormat) { 
	        dateFormat.applyPattern(dateFormatPattern);
	        dateFormat.setLenient(false);
	        date = dateFormat.parse(dateString);
	    }
	    return date;
	}
	
	/**
	 * Converts a String to a Number, using the specified pattern.
	 * (see java.text.NumberFormat for pattern description)
	 *
	 * @param numString the String to convert
	 * @param numFormatPattern the pattern
	 * @return the corresponding Number
	 * @exception ParseException, if the String doesn't match the pattern
	 */
	public static Number toNumber(String numString, String numFormatPattern) 
	    throws ParseException {
	    Number number = null;
	    if (numFormatPattern == null) {
	        numFormatPattern = "######.##";
	    }
	    synchronized (numberFormat) { 
	        numberFormat.applyPattern(numFormatPattern);
	        number = numberFormat.parse(numString);
	    }
	    return number;
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// System.out.println( StringUtils.toUTF_8_2("user.jhtml?id=123&pw=a��b��c") );
		String str =null;
		str = "ab'cd";
		System.out.println( ConvertUtils.toHTMLString( str ) );
		str = "ab\"cd";
		System.out.println( ConvertUtils.toHTMLString( str ) );
		str = "ab\"'''\"cd";
		System.out.println( ConvertUtils.toHTMLString( str ) );
		
		//System.out.println("--" + "a'b''c".replaceAll("[']", "''"));

		System.out.println( "ab'cd".replaceAll("[\"]", "\\\"") );
		System.out.println( "ab'cd".replaceAll("[\"]", "\\\"") );
		System.out.println( "ab'cd".replaceAll("[\"]", "\\\"") );
		System.out.println( "--------------" );
		str = "ab'cd";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "'", "\\'") );

		str = "ab\"cd";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "\"", "\\\"") );

		str = "ab\"\"cd";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "\"", "\\\"") );

		str = "ab\"c\"d";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "\"", "\\\"") );
		
	}

}
