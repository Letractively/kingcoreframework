/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util ;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p>��String������д���Ĺ����࣬���еķ������Ǿ�̬(static)�ģ�����Ҫ����
 *			����ʵ���Ϳ��Ե��á�</p>
 * @author	WUZEWEN on 2004-09-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK1.4
 **/


public class StringUtils {
	
	/**
	 * wzw: ת�� txt to link
	 * @param txt
	 * @return
	 */
	public static String convert2Link(String txt){
		return txt.replaceAll("(http|ftp|https)(://)\\w+\\.\\w+([\\.\\w+]*)([/\\w+]*)[\\.\\w+]*[?\\w+=\\w+][&\\w+=\\w+]*",
						"<a target='_blank' class='link_blueer12a' href='$0'>$0</a>"); //wzw:���������ı�	

	}
	
	/**
	 * default Construtor.
	 *
	 */
	public StringUtils(){
	}
	
	
	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 *
	 * @param bytes an array of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	private static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
	
	/*
	 private static  String  toUTF_8_2(String inPara) throws Exception{
	 
	 char temChr;
	 String rtStr ="";
	 int i_char=0;
	 //java.net.
	  for(int i=0;i<inPara.length();i++)
	  {
	  temChr=inPara.charAt(i);
	  i_char = temChr + 0;
	  if( i_char<128 && i_char>0){
	  rtStr += temChr;
	  }else{
	  
	  //rtStr += "%"+toHex( temChr );
	   rtStr += java.net.URLEncoder.encode( temChr+"" );
	   } 
	   }
	   return rtStr;
	   
	   }
	   */
	
	/** toHex
	 *
	 * A function (method) in Java to convert integers (type int) to a hex string.
	 * This is one of my early contributions to Java. Back in Jan '96, the
	 * java.lang.Integer class lacked a toHexString method. This was a common
	 * request on Java newgroups and web pages of Java developers. Instead of
	 * making a package, I just wrote a function that people could cut and paste
	 * into their programs. Now that java.lang.Integer has a toHexString method,
	 * this is obsolete
	 *
	 * @version 1.00 1996/Feb/02
	 * Status: Obsolete. Use toHexString method of java.lang.integer.
	 *
	 * @author Rajiv Pant (Betul)   http://rajiv.org   betul@rajiv.org
	 *
	 */
	
	protected static  String toHex(int n)
	{
		String h = "" ;
		int r=0;
		int nn=n ;
		do
		{
			r=nn % 16 ;
			nn= nn / 16 ;
			switch (r)
			{
			case 10: h = "A" + h; break ;
			case 11: h = "B" + h; break ;
			case 12: h = "C" + h; break ;
			case 13: h = "D" + h; break ;
			case 14: h = "E" + h; break ;
			case 15: h = "F" + h; break ;
			default: h = r + h; break ;
			}
		}
		while (nn > 0) ;
		return h ;
	}
	
	
	/**
	 * <p>��ָ���ַ��������е��ض��ַ��滻Ϊ�µ��ַ������õ��ǵݹ鷽����
	 * 		this is as same as the StringUtils.replace method in common-lang.jar(Apache) file.</p>
	 * 
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
	 * @param text ��Ҫ�滻���ַ���
	 * @param repl ���滻���ַ�
	 * @param with �滻���ַ�
	 * @return �滻֮����ַ���
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}
	/**
	 * <p>��ָ���ַ��������е��ض��ַ��滻Ϊ�µ��ַ������õ��ǵݹ鷽����
	 * 		this is as same as the StringUtils.replace method in common-lang.jar(Apache) file.</p>
	 * 
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
	 * @param text ��Ҫ�滻���ַ���
	 * @param repl ���滻���ַ�
	 * @param with �滻���ַ�
     * @param max  maximum number of values to replace, or <code>-1</code> if no maximum
	 * @return �滻֮����ַ���
	 */
	public static String replace(String text, String repl, String with, int max) {    
		if (text == null || isEmpty(repl) || with == null || max == 0) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0, end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buf.append(text.substring(start));
        return buf.toString();  
	} 
	
	
	/**
	 * <p>���ַ�������ת��Ϊ�������</p>
	 * 
	 * <pre>
	 * StringUtils.stringToArray("a,b,c", ",")		= {"a","b","c"}
	 * StringUtils.stringToArray("1_2_3", "_")		= {"1","2","3"}
	 * StringUtils.stringToArray("a$b$c", "$")		= {"a","b","c"}
	 * </pre>
	 * 
	 * @param s Ҫת��Ϊ������ַ���
	 * @param c �ָ����ַ���
	 * @return ת��֮����������
	 */
	public static String[] stringToArray(String s,String c) {
		int i,j,len;
		String source,tmp;
		String[] value=null;
		Vector<String> v;
		if (s==null || s.length()<1)
			return value;
		v = new Vector<String>();
		source = s;
		j = 0;
		len = c.length();
		i = source.indexOf(c);
		//String.getto
		//tmp = new String[100];
		
		while (i>=0) {
			//tmp[j] = source.substring(0,i);
			tmp = source.substring(0,i);
			v.addElement(tmp);
			j = j + 1;
			source = source.substring(i+len,source.length());
			i = source.indexOf(c);
		}
		
		//tmp[j++] = source;
		tmp = source;
		v.addElement(tmp);
		j++;
		value = new String[j];
		for (i=0;i<j;i++)
			value[i] = (String)v.elementAt(i);

		//value = (String[]) v.toArray();   //Zeven on 2007-06-07
		
		return value;
	}
	
	/**
	 * <p>�����ַ�����</p>
	 * @deprecated 
	 * @param s Ҫ���ܵ��ַ���
	 * @throws Exception
	 * @return ����֮����ַ���
	 */
	public static  String  toSecretString(String s) throws Exception{
		byte[] bString=s.getBytes();
		byte b=0x68;
		for (int i=0;i<bString.length;i++)
			bString[i]=(byte)(bString[i]^b);
		return new String(bString,"GBK");
	}
	
	/**
	 * <P>Translate a string from ISO-8859-1 to GB2312 character set.</p>
	 * @param s Ҫת�����ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  toGB2312(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("ISO-8859-1");
		String sTmpStr = new String(temp_b,"GB2312");
		return sTmpStr;
	}
	
	/**
	 * <p>Translate a string from ISO-8859-1 to UTF-8 character set.</p>
	 * @param s Ҫת�����ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  toUTF_8(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("ISO-8859-1");
		String sTmpStr = new String(temp_b,"UTF-8");
		return sTmpStr;
	}
	
	/**
	 * <p>Translate a string from GB2312 to ISO-8859-1 character set.</p>
	 * @param s Ҫת�����ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  toISO(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("GB2312");
		String sTmpStr = new String(temp_b,"ISO-8859-1");
		return sTmpStr;
	}
	
	/**
	 * <p>Translate a string from ISO-8859-1 to GBK character set.</p>
	 * @param s Ҫת�����ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  toGBK(String s) throws Exception{
		return toGBK(s, "ISO-8859-1");
	}
	
	/**
	 * <p>Translate a string from GB2312 to ISO-8859-1 character set.</p>
	 * @param s Ҫת�����ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  gbk2ISO(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("GB2312");
		String sTmpStr = new String(temp_b,"ISO-8859-1");
		return sTmpStr;
	}
	
	
	/**
	 * <p>Translate a string from ISO-8859-1 to GBK character set.</p>
	 * @param s Ҫת�����ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  iso2GBK(String s) throws Exception{
		return toGBK(s, "ISO-8859-1");
	}
	
	/**
	 * <p>Translate a string from ISO-8859-1 to GBK character set.</p>
	 * @param s Ҫת�����ַ���
	 * @param lang ���ڵ��ַ���
	 * @throws Exception
	 * @return ת��֮����ַ���
	 */
	public static  String  toGBK(String s,String lang) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes(lang);
		String sTmpStr = new String(temp_b,"GBK");
		return sTmpStr;
	}
	
	/** 
	 * <p>���ַ���ת����Unicode�룬��ʽΪ\\uXXXX\\uXXXX</p> 
	 * @param strText ��ת�����ַ���(��Ӣ�ġ�����)
	 * @return ת�����Unicode���ַ��� 
	 */ 
	public static String toUnicode(String strText) throws UnsupportedEncodingException{
		char c; 
		String strRet = "" ; 
		int intAsc; 
		String strHex;
		
		for ( int i = 0; i < strText.length(); i++ ){
			c = strText.charAt(i); 
			intAsc = (int)c; 
			// System.out.println(i+ "  intAsc ss="+intAsc);
			if(intAsc>128){ 
				strHex = Integer.toHexString(intAsc); 
				strRet = strRet + "\\u" + strHex.toUpperCase(); 	//&#x
			} else { 
				strHex = Integer.toHexString(intAsc); 
				strRet = strRet + "\\u00" + strHex.toUpperCase(); 	//&#x
			}
		}
		return strRet;
	}
	
	/**
	 * <p>��ȡ�ַ����ֽڳ��ȣ���Ϊ���뵽���ݿ���ʱ��һ�������ַ��൱������Ӣ���ַ���</p>
	 * 
	 * <pre>
	 * StringUtils.getByteLength("abc")		= 3
	 * StringUtils.getByteLength("����")		= 4
	 * StringUtils.getByteLength("����abc")	= 7
	 * StringUtils.getByteLength(null)		= 0
	 * StringUtils.getByteLength("")		= 0
	 * </pre>
	 * @param str ��Ҫ���㳤�ȵ��ַ�������
	 * @return �ַ������ֽڳ�
	 */
	public static int getByteLength(String str){
		if( str==null) {
			return 0;
		}
		int count=0;
		for(int i=0; i<str.length() ; i++){
			//if(hello.value.charCodeAt(i)<=8192)
			if(str.charAt(i)<128 && str.charAt(i)>0){
				
				count+=1;//alert("���ֽڷ�:"+hello.value.charAt(i));
			}else{
				count+=2;// alert("˫�ֽڷ�:"+hello.value.charAt(i));
			}
		}
		return count ;
	}
	
	
	/**
	 * <p>���һ�� String ����Ϊnull ����"".</p>
	 *
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * <p>˵��: ����������String ����ִ��trims����.</p>
	 *
	 * @param str  Ҫ�����ַ���
	 * @return <code>true</code> ����ַ����Ȳ���null��Ҳ����""��
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * <p>�������ַ������ַ����г����˶��ٴ�.</p>
	 *
	 * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
	 *
	 * <pre>
	 * StringUtils.countMatches(null, *)       = 0
	 * StringUtils.countMatches("", *)         = 0
	 * StringUtils.countMatches("abba", null)  = 0
	 * StringUtils.countMatches("abba", "")    = 0
	 * StringUtils.countMatches("abba", "a")   = 2
	 * StringUtils.countMatches("abba", "ab")  = 1
	 * StringUtils.countMatches("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param str  �������ַ����������� null
	 * @param sub  ������ֶ��ٴε��ַ����������� null
	 * @return ���ֵĴ����������null������0��
	 */
	public static int countMatches(String str, String sub) {
		if (isEmpty(str) || isEmpty(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// System.out.println( StringUtils.toUTF_8_2("user.jhtml?id=123&pw=a��b��c") );
		
		System.out.println( StringUtils.encodeHex("��".getBytes() ) );
		
		System.out.println("--" + StringUtils.replace("a'b''c", "'", "''"));
	}
}

