/*
 * $Id: CompareTagBase.java 54929 2004-10-16 16:38:42Z germuska $ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.kingcore.framework.tag;


import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import com.kingcore.framework.bean.DataSet;


/**
 * Abstract base class for comparison tags.  Concrete subclasses need only
 * define values for desired1 and desired2.
 *
 * @version $Rev: 54929 $ $Date: 2004-10-16 17:38:42 +0100 (Sat, 16 Oct 2004) $
 */

public abstract class CompareTagBase extends ConditionalTagBase {

	protected static Logger log = Logger.getLogger( com.kingcore.framework.tag.CompareTagBase.class);
	
    // ----------------------------------------------------- Instance Variables


    /**
     * We will do a double/float comparison.
     */
    protected static final int DOUBLE_COMPARE = 0;


    /**
     * We will do a long/int comparison.
     */
    protected static final int LONG_COMPARE = 1;


    /**
     * We will do a String comparison.
     */
    protected static final int STRING_COMPARE = 2;


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
     MessageResources.getMessageResources
        ("org.apache.struts.taglib.logic.LocalStrings");


    // ------------------------------------------------------------ Properties


    /**
     * The value to which the variable specified by other attributes of this
     * tag will be compared.
     */
    public String value = null;

    public String getValue() {
        return (this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        value = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Evaluate the condition that is being tested by this particular tag,
     * and return <code>true</code> if the nested body content of this tag
     * should be evaluated, or <code>false</code> if it should be skipped.
     * This method must be implemented by concrete subclasses.
     *
     * @exception JspException if a JSP exception occurs
     */
    protected abstract boolean condition() throws JspException;


    /**
     * Zeven.Woo on 2006-11-23
     * @param desired1 First desired value for a true result (-1, 0, +1)
     * @param desired2 Second desired value for a true result (-1, 0, +1)
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean condition(int desired1, int desired2)
        throws JspException {

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
    				obj=request.getAttribute( this.getName() ); //pageContext
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
			if(this.getProperty()==null && this.getColumnIndex()==null ){	//û������ֵ���������i���������ȡ RowSet ���� DataSet.
				if( pageContext.getAttribute(this.getName())!=null){
					val=pageContext.getAttribute(this.getName());//pageContext
				}
			} else{
    			// ������ϲ��ǩ���ϲ��ǩ�Ƿ�����pageContex���еģ���pageContext��ȡ��
    			obj=pageContext.getAttribute(this.getName());//pageContext.getRequest().
			}
    	}

    	//2�����ݶ��󡢶�������͡�Property���ԣ���ȡֵ��
        if(obj!=null && obj instanceof RowSet ){
    		RowSet crs = (RowSet)obj;
	        try {
	        	if(tag == null)
	            {
					try{
		        		//�������б�ǩ����������Iterate��ǩ�ı�ָ�룬�Լ����ô���ָ��
						crs.absolute(1); // ���ܻ��׳��쳣
					}catch(Exception e){
						//��һ�в����ڣ�û�п�ȡ��ֵ��
					}

	            }
	        	
				if( this.getProperty()!=null ){
					val=crs.getObject( this.getProperty());
				}else if( this.getColumnIndex() !=null ){
					val=crs.getObject( this.getColumnIndexNumber() );
				}

			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}

        } else if(obj!=null && obj instanceof DataSet ){
        	//û�й����Ľӿڣ�RowSet��DataSetֻ�ø��Դ���
        	DataSet qds = (DataSet)obj;
	        try {
	        	if(tag == null)
	            {
					try{
		        		//�������б�ǩ����������Iterate��ǩ�ı�ָ�룬�Լ����ô���ָ��
						qds.absolute(1); // ���ܻ��׳��쳣
					}catch(Exception e){
						//��һ�в����ڣ�û�п�ȡ��ֵ��
					}

	            }

				if( this.getProperty()!=null ){
					val=qds.getObject( this.getProperty());
				}else if( this.getColumnIndex()!=null ){
					val=qds.getObject( this.getColumnIndexNumber() );
				}
				
			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}
        }

    // Perform the appropriate comparison
    int result = 0;
    
    // ���� null ֵ���
    if(val==null) {
    	val = "";
    }
    result = val.toString().compareTo( getValue() );

    // Normalize the result
    if (result < 0)
        result = -1;
    else if (result > 0)
        result = +1;

    // Return true if the result matches either desired value
    return ((result == desired1) || (result == desired2));

    }

}

