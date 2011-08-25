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
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.RowSet;

import org.apache.log4j.Logger;

import wzw.lang.Escaper;
import wzw.text.Formatter;



/**
 * <p>��request(��ѡ) or session or pageContext or application ���е�RowSet����ĵ�һ�е�ĳ���е�ֵ�����
 * 		Ҳ����Ƕ�׵�IterateTag ��ǩ�У������ǰ�е�ĳ���е�ֵ��
 * �����Ժ�ʹ�� columnName,columnIndex �滻 property,columnIndex. 
 * 		** ����ֻ֧�ֶ�RowSet��ȡֵ����֧����������</p>
 * @author	WUZEWEN on 2006.09.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */
public class WriteTag extends TagSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * log4j��־����
     */
    protected final static Logger log = Logger.getLogger(com.kingcore.framework.tag.WriteTag.class);

	/**
	 * RowSet or DataSet ���������
	 */
	protected String name = null;
	/**
	 * RowSet or DataSet ����Ҫȡ����ĳ���е����֡�
	 */
	protected String property = null;
	/**
	 * RowSet or DataSet ����Ҫȡ����ĳ���е��±ꡣ
	 */
	protected String columnIndex = null ;
	private int columnIndexNumber ;
	
	private String columnType = null;	//�����ͣ��磺Ĭ��string,���� int, float, ...
	
	/**
	 * RowSet or DataSet ���������õ���Ĭ��Ϊrequest��
	 */
    protected String scope = null;

    /**
     * Escape Characterת��[����]�ַ�;
     * ��������ö��ֵ��
     *	htmlValue��ת��Ϊhtml ��Ҫ��������е� ',",&
     *  scriptValue��ת��ΪJScript����Ҫ��������е� ',",\,
     *  htmlValueForScript��ת��Ϊhtml����������ΪJScript��������Ҫ��� ',",\
     *  
     */
    protected String escape=null;
    
    /**
     * The format string to be used as format to convert
     * value to String.
     */
    protected String formatStr = null;

    public String getFormat() {
        return (this.formatStr);
    }

    public void setFormat(String formatStr) {
        this.formatStr = formatStr;
    }
    
	public static void main(String[] args) {
 
		Object obj = null;
		System.out.println(  obj instanceof String );
	}

	/**
	 * RowSet or DataSet ���������
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * RowSet or DataSet ����Ҫȡ����ĳ���е����֡�
	 */
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * RowSet or DataSet ����Ҫȡ����ĳ���е��±�ֵ��
	 * �����Ժ�ʹ�� columnName �滻 property.
	 */
	public String getColumnIndex() {

		return columnIndex;
	}
	
	public void setColumnIndex(String columnIndex ) {		
		this.columnIndex = columnIndex ;

		try {
			this.columnIndexNumber = Integer.parseInt( this.columnIndex );
		} catch(Exception e) {
			this.columnIndexNumber = -1;
		}
	}

	public int getColumnIndexNumber() {
		return this.columnIndexNumber ;
	}
	
	
	/**
	 * Zeven �Ժ���ʹ�� columnName ��� property��
	 * RowSet or DataSet ����Ҫȡ����ĳ���е����ơ�
	 */
	public String getColumnName() {
		return this.property ;
	}
	public void setColumnName(String columnName ) {		
		this.property = columnName;
	}
	
	/**
	 * RowSet or DataSet ���������õ���Ĭ��Ϊrequest��
	 */
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getEscape() {
		return escape;
	}

	public void setEscape(String escape) {
		this.escape = escape;
	}

	/**
	 * 
	 */
    public int doStartTag() throws JspException {

    	// log.debug( "  WriteTag ----------doStartTag " );
    	// �����Ƿ������ѭ����ǩ����
    	Tag tag=TagSupport.findAncestorWithClass(this, com.kingcore.framework.tag.IterateTag.class);
    	Object obj=null;
    	Object val="";

    	//1������Name���Ի�ȡ����
    	if(tag == null){
    		// ���û���ϲ��ǩ�������õĲ�����ȡ��
    		if(this.getScope()==null){
    			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    			obj=request.getAttribute( this.getName() );
				if(obj==null){
    				obj=pageContext.getAttribute( this.getName() );
				}

    		}else if(this.getScope().equals("session")){
    			obj=pageContext.getSession().getAttribute( this.getName() );

    		}else if(this.getScope().equals("page")){
    			obj=pageContext.getAttribute( this.getName() );

    		}else {
    			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    			obj=request.getAttribute( this.getName() );
    		}

    	}else{
			if(this.getProperty()==null && this.getColumnIndex()==null ){	//û������ֵ����������ֵ���������i���������ȡ RowSet ���� DataSet.
				if( pageContext.getAttribute(this.getName())!=null){
					val=pageContext.getAttribute(this.getName());
				}
			} else{
    			// ������ϲ��ǩ���ϲ��ǩ�Ƿ�����pageContex���еģ���pageContext��ȡ��
    			obj=pageContext.getAttribute(this.getName());
    			//obj=pageContext.getRequest().getAttribute(this.getName());
			}
    	}

    	//2�����ݶ��󡢶�������͡�Property���ԣ���ȡֵ��
        if( obj instanceof RowSet ){
    		RowSet crs = (RowSet)obj;
	        try {
	        	if(tag == null)
	            {
	        		//System.out.println(" outer tag is null");
	        		// ����ⲿ�б�ǩ�������ⲿ��ǩ�ı�ָ�룬�����Լ��ı�ָ��
//		        	crs.beforeFirst();
//					if(crs.next()){
//						if( crs.getObject(this.getProperty())!=null ){
//							val=crs.getObject( this.getProperty());
//			        		//System.out.println(" outer tag is not null " + val );
//						}
//			        }
					try{
		        		//�������б�ǩ����������Iterate��ǩ�ı�ָ�룬�Լ����ô���ָ��
						crs.absolute(1); // ���ܻ��׳��쳣
					}catch(Exception e){
						//��һ�в����ڣ�û�п�ȡ��ֵ��
						
					}
	            }
//	        	else{
//	        		//�������б�ǩ����������Iterate��ǩ�ı�ָ�룬�Լ����ô���ָ��
//					if( crs.getObject(this.getProperty())!=null ){
//						val=crs.getObject( this.getProperty());
//						//System.out.println(" outer tag is not null " + val );
//					}
//	            }
	        	
				if( this.getProperty()!=null ){
//					if("int".equalsIgnoreCase(this.getColumnType())){
//						val=crs.getInt( this.getProperty());
//					}		// ��ʱȡ��columnType��ʹ��format������int�������롣
					val=crs.getObject( this.getProperty());
					
				}else if( this.getColumnIndex()!=null ){	// ���� columnIndex �жϣ�����ȡ columnIndexNumber ��ֵ��
					val=crs.getObject( this.getColumnIndexNumber() );
				}

			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}

        }

        // �����Ϣ��ҳ��
        JspWriter out = pageContext.getOut();
        try {
        	if( val == null) {
            	out.write( "" );
                return EVAL_BODY_INCLUDE;        		
        	}
        	
        	if( this.getFormat()!=null ){		// �����������
        		val = Formatter.formatObject( val, this.getFormat()) ;
        	
        	}else if( this.getEscape()!=null ){							// ����ַ������ͣ�Ӧ�ò�����Formatһ��ʹ�ã��Ժ���Ը���
        		if( this.getEscape().equals("htmlValue")) {
        			val = Escaper.escape2Html( val.toString() );	//WebPageUtils.toHTMLValue(
        			
        		}else if( this.getEscape().equals("scriptValue")) {
        			val = Escaper.escape2JavaScript( val.toString() );	//WebPageUtils.toScriptValue(
        			
        		}else if( this.getEscape().equals("htmlValueForScript")) {
        			val = Escaper.escape2HtmlForJavaScript( val.toString() );	// WebPageUtils.toHTMLValueForScript( 
        			
        		}
        	}
        	
        	out.write( val.toString() );
        	
        } catch (IOException e) {
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
			throw new JspException(e.getMessage());
        }

        //System.out.print("\nval:" + val ) ;
        return EVAL_BODY_INCLUDE;
    }


    
	/**
	 * Releases all instance variables.
	 */
	public void release() {
		this.name = null;
		this.property = null;
		this.columnIndex = null;
		this.columnType = null;
		this.scope = null;
		this.formatStr=null;
		this.escape = null;
		
	    super.release();
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

}
