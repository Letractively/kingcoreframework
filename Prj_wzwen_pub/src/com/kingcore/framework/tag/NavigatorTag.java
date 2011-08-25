/**
 * Copyright (C) 2002-2006 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.tag;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.kingcore.framework.bean.Navigator;


/** 
 * <p>��request or session or pageContext ���е�NavigableDataSet����ʵ����Navigable�ӿڵĶ��󣩵ĵ�����Ϣ��
 *		�������ǰ��ҳ�档
 *		 ��ǩ�� type ����ֵĿǰ��������pnfl, pnfl_2���ֱ������ͬ���ĵ�������
 *	</p>
 * @author	WUZEWEN on 2006.11.01
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */
public class NavigatorTag extends TagSupport {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger( com.kingcore.framework.tag.NavigatorTag.class);
	
	/**
	 * ʵ����Navigable�ӿڵĶ��������
	 */
	protected String name = null;	
	/**
	 * Ҫ���õ�Navigable����ĵ�������
	 */
	protected String type = "pnfl";
	/**
	 * Navigable�ӿڵĶ�����ڵ���Ĭ��"request"
	 */
    protected String scope = null;
    /**
     * Navigable����·��
     */
    protected String path = null;

    private String onClick = null;
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	/**
	 * ʵ����Navigable�ӿڵĶ��������
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Navigable�ӿڵĶ�����ڵ���
	 */
    public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * Ҫ���õ�Navigable����ĵ�������
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}
    /**
     * Navigable����·��
     */
	public String getPath() {
		return path;
	}
	
	/**
	 * ��ȡjspҳ���ǩ��path���ԣ������java����������pageContext�У���������ƣ�ʹ��${paraname}���롣
	 * 		��jspҳ��ʹ�� pageContext.setAttribute("path", pathValue);
	 * 				<woo:navegator name="goodsDataSet" path="${path}" type="pnfl"/>
	 */
	public void setPath(String path) {
		if(path==null)
			return;
		path=path.trim();
		if(path.trim().startsWith("${")){
			String str_t=path.substring(2,path.length()-1);
			//System.out.println(str_t);
			if(pageContext.getAttribute(str_t)==null){
				System.out.println("cann't find "+str_t+" in scope pageContext.");
			}
   			this.path = pageContext.getAttribute(str_t).toString();
    	}else{
    		this.path = path;
    	}

	}
	
	public int doStartTag() throws JspException {  
    	Object obj=null;
    	
    	//1������Name���ԣ���ȡ����
    	if(this.scope == null){
    		// ���û���ϲ��ǩ�������õĲ�����ȡ��
    		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    		obj=request.getAttribute( this.getName() );
    	} else if (this.scope.equals("session")){
    		// ��sessionȡ��
    		obj=pageContext.getSession().getAttribute(this.getName());
    	} else if (this.scope.equals("page")){
    		// ������ϲ��ǩ�����ϲ��ǩȡ��
    		obj=pageContext.getAttribute(this.getName());
    	} else {
    		// ���û���ϲ��ǩ�������õĲ�����ȡ��
    		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    		obj=request.getAttribute( this.getName() );
    	}
    	
        String val="";
        // ���ݵ������ͣ����ö�����Ӧ�ĵ���������  path/commandName ���������뵼�����ˣ�
        // ������jsp�����Ƿ���action�����á�
        if(obj!=null && obj instanceof Navigator ){
        	if(this.getType().equals("pnfl")){
        		val = ((Navigator)obj).getPagesPnfl( );
        		
        	}else if(this.getType().equals("pnfl_2")){
        		val = ((Navigator)obj).getPagesPnfl2( );
        		
        	}else if(this.getType().equals("pn")){
        		val = ((Navigator)obj).getPagesPn( );        	
        		
        	}else{
        		val = ((Navigator)obj).getPagesPnfl( );

        	}
        }

        JspWriter out = pageContext.getOut();
        try {
            out.write(val);
        } catch (IOException e) {
			log.error("debug", e);
        	/// e.pri ntStackTrace();
        	throw new JspException(e.getMessage());
        }

        //System.out.print("\nAbsolutePathTag:" + sb_uri.toString()) ;
        return EVAL_BODY_INCLUDE;
    }

	/**
	 * Releases all instance variables.
	 */
	public void release() {
		this.name = null;	
		this.type = null;
		this.scope = null;
		this.path = null;
		
	    super.release();
	}
}
