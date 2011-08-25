/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util ;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;

import wzw.lang.Base64;

/**
 * This class contains a number of static methods that can be used to
 * work with javax.servlet.Cookie objects.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class CookieUtils {

	/**
	 * ��־�������
	 */
	protected static Logger log = Logger.getLogger( com.kingcore.framework.util.CookieUtils.class);
	
    /**
     * Returns the value of the Cookie with the specified name,
     * or null of not found.
     *   ���������ҵ�����cookie������Ϊname ��cookie ֵ�� ��cookieֵ����Base64���롣
     * @param name cookie's name.
     * @param req response Object.
     * @return
     * @throws DecoderException 
     */
    public static String getCookieValue(String name, HttpServletRequest req) throws DecoderException {
		return getCookieValue(name ,req, true);
    }


    /**
     * Returns the value of the Cookie with the specified name,
     * or null of not found.
     *   ���������ҵ�����cookie������Ϊname ��cookie ֵ��
     * @param name cookie's name.
     * @param req response Object.
     * @param needEncode �Ƿ���Ҫ��Cookieֵ���룬Ĭ��true.
     * @return
     * @throws DecoderException 
     */
    public static String getCookieValue(String name, HttpServletRequest req, boolean needDecode) throws DecoderException {
//    	check input information.
		if(name==null){
			return null;
		}

        Cookie[] cookies = req.getCookies();
		if(cookies==null){// ���û���κ�cookie
			//out.print("none any cookie");
			return null;
		}
        String value = null;
        for (int i = 0; i < cookies.length; i++) {
            if( name.equals( cookies[i].getName() ) ) {
                try {
                	value = cookies[i].getValue();
                	if(needDecode){
    					value = Base64.decode( value, "utf-8" ) ;
                	}
                	
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					log.error("debug", e);
					//e.pri ntStackTrace();
				
				}	//new String( Hex.decode( cookies[i].getValue() ) );
                break;
            }
        }
        return value;
    }
    
    /**
     * Creates a Cookie with the specified name, value and max age,
     * and adds it to the response.
     * 		���cookies  ����Ĭ�϶�cookieֵ����Base64 ���롣
     * @param name cookie's name.
     * @param value cookie's value.
     * @param maxAge the time cookie been keeped. the unit is second.
     *      age of the cookie in seconds,������ʾ�ر��������ɾ��Cookie.
	 *		an integer specifying the maximum age of the cookie in seconds; 
	 *		if negative, means the cookie is not stored; if zero, deletes the cookie.
     * @param res response Object.
     */
    public static void sendCookie(String name, String value, int maxAge,
    		HttpServletResponse response) {
    	sendCookie(name, value, maxAge, response, true);
    }
    
    /**
     * 
     * Creates a Cookie with the specified name, value and max age,
     * and adds it to the response.
     * ���cookies  ��cookieֵ����Base64 ���롣
     * @param name cookie's name.
     * @param value cookie's value.
     * @param maxAge the time cookie been keeped. the unit is second.
     *      age of the cookie in seconds,������ʾ�ر��������ɾ��Cookie.
	 *		an integer specifying the maximum age of the cookie in seconds; 
	 *		if negative, means the cookie is not stored; if zero, deletes the cookie.
     * @param res response Object.
     * @param needEncode �Ƿ���Ҫ��Cookieֵ��Base64���룬Ĭ��true
     */
    public static void sendCookie(String name, String value, int maxAge,
			HttpServletResponse response, boolean needEncode) {
    	sendCookie(name, value, maxAge, response, needEncode, null);
    }

   /** 
    * Creates a Cookie with the specified name, value and max age,
    * and adds it to the response.
    * ���cookies  ��cookieֵ����Base64 ���롣
    * 	The form of the domain name is specified by RFC 2109. A domain name begins with a dot (.foo.com) 
    * 		and means that the cookie is visible to servers in a specified Domain Name System (DNS) zone 
    * 		(for example, www.foo.com, but not a.b.foo.com). By default, cookies are only returned to 
    * 		the server that sent them.
    * @param name cookie's name.
    * @param value cookie's value.
    * @param maxAge the time cookie been keeped. the unit is second.
    *      age of the cookie in seconds,������ʾ�ر��������ɾ��Cookie.
	 *		an integer specifying the maximum age of the cookie in seconds; 
	 *		if negative, means the cookie is not stored; if zero, deletes the cookie.
    * @param res response Object.
    * @param needEncode �Ƿ���Ҫ��Cookieֵ��Base64���룬Ĭ��true
    * @param domain Cookie's domain
    */
    public static void sendCookie(String name, String value, int maxAge,
    				HttpServletResponse response, boolean needEncode, String domain ) {

    	try {
    		if (needEncode) {
    			value = Base64.encode( value.getBytes("utf-8") );	//��Ӧ�ͻ��˽���
//    			 value = new String(Base64.encode( value.getBytes("utf-8")), "utf-8" );	//��ʹ��utf-8
			}
			//System.out.println("value = " + value);
	        Cookie cookie = new Cookie(name, value);//Hex.encode(value.getBytes()) );
	        cookie.setMaxAge(maxAge);
			cookie.setPath("/");
			if(domain!=null){
				cookie.setDomain( domain );	// ����domain
			}
	        response.addCookie(cookie);
	        
		} catch (UnsupportedEncodingException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
		
    }

    
    /**
     *  ���� domain ��clearCookie��
     * clear a cookie from client side.
     * @param name the name of cookie will be cleared.
     * @param response HttpServletResponse Object.
     */
    public static void clearCookie(String name, HttpServletResponse response){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    
    /**
     * ���� domain ��clearCookie��
     * 	The form of the domain name is specified by RFC 2109. A domain name begins with a dot (.foo.com) 
     * 		and means that the cookie is visible to servers in a specified Domain Name System (DNS) zone 
     * 		(for example, www.foo.com, but not a.b.foo.com). By default, cookies are only returned to 
     * 		the server that sent them.
     * @param name ��Ҫ���Cookie������
     * @param response ��Ӧ����
     * @param domain ��ָ�������Cookie�������ָ����Ĭ��Ϊ��ǰ��
     */
    public static void clearCookie(String name, HttpServletResponse response, String domain){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
		cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    /**
     * ���� domain,path ������ clearCookie��
     * 	The form of the domain name is specified by RFC 2109. A domain name begins with a dot (.foo.com) 
     * 		and means that the cookie is visible to servers in a specified Domain Name System (DNS) zone 
     * 		(for example, www.foo.com, but not a.b.foo.com). By default, cookies are only returned to 
     * 		the server that sent them.
     * @param name ��Ҫ���Cookie������
     * @param response ��Ӧ����
     * @param domain ��ָ�������Cookie�������ָ����Ĭ��Ϊ��ǰ��
     * @param path ��ָ��Ŀ¼���Cookie�������ָ����Ĭ��Ϊ��Ŀ¼
     */
    public static void clearCookie(String name, HttpServletResponse response, 
    							   String domain, String path){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath(path);
		cookie.setDomain(domain);
        response.addCookie(cookie);
    }
    
    
    /**
     * Returns true if a cookie with the specified name is
     * present in the request.
     * �ж��Ƿ�������Ϊname ��cookie�� ��cookiֵ����û��Ҫ��
     * @param name the name of the cookie will be checked.
     * @param req response Object.
     * @return
     */
    public static boolean isCookieSet(String name, HttpServletRequest req) {
        try {
//        	check input information.
    		if(name==null){
    			return false;
    		}

            Cookie[] cookies = req.getCookies();
    		if(cookies==null){// ���û���κ�cookie
    			//out.print("none any cookie");
    			return false;
    		}
            for (int i = 0; i < cookies.length; i++) {
                if( name.equals( cookies[i].getName() ) ) {
                    return true;
                }
            }
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
		return false;
    }
}
