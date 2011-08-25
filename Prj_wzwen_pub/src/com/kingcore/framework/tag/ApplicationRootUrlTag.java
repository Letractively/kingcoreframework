/*
 * @(#)AbsolutePathTag.java		    1.00 2004/04/16
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */

package com.kingcore.framework.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>�����Ŀ���Ƿ���jspҳ�棬���ɵ�ǰҳ��ľ���urlֵ��
 * 		���磺<img src='<woo:absolutePath/>/../images/logo.gif />   
 * 	 Ҳ���Բ���д��ÿ�����õ�Ԫ���ϣ���������ָ����ǰҳ���base href���磺
 * 			<base href="<woo:applicationRootUrl/>" tager="_self">
 * 				�����磺http://192.168.0.2:8080/pda/
 * 
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
 
public class ApplicationRootUrlTag extends TagSupport
{

    public int doStartTag() throws JspException {    	
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer sb_uri = new StringBuffer();


    	String path = request.getContextPath();  //��:"","/pda","/cms",...
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"
    			+request.getServerPort()+path ; //+"/";

    	
        JspWriter out = pageContext.getOut();
        try {
            out.write( basePath );
        } catch (IOException e) {
        }
        
        //System.out.print("\nAbsolutePathTag:" + sb_uri.toString()) ;
        return EVAL_BODY_INCLUDE;
    }
    
	/**
	 * Releases all instance variables.
	 */
	public void release() {
	    super.release();
	}

}
