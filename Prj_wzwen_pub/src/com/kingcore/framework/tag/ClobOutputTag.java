package com.kingcore.framework.tag ;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * 
 * @modify WUZEWEN 2005.04.02 ���� value.trim() ;
 *
 */
 
public class ClobOutputTag extends TagSupport
{
    /**
    * Ҫ��ѯ��sql���
    */
    protected String sql = null;
    /**
     * ȡClob�ֶε�sql���
     */

    public void setSql(String sql)
    {
        this.sql = sql;
    }


    public int doStartTag() throws JspException
    {

        return this.SKIP_BODY;
    }
    
   	/**
	 * Releases all instance variables.
	 */
	public void release() {

		sql = null;

	    super.release();
	}


}
