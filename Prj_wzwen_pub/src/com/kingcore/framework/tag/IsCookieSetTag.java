package com.kingcore.framework.tag ;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kingcore.framework.util.CookieUtils;

/* wuzewen   2003.04.25
 *
 *  ���������ж��Ƿ���ڸ����Ƶ�cookie����������ҳ����� checked��
 *
 */
public class IsCookieSetTag extends BodyTagSupport
{
	public String name  ;
	private String  value ;
	//��ȡ����
	public void setName( String name )
	{
		this.name = name   ;    // name Ϊ��ʱweb����������jspҳ��ͨ����
	}

	//ִ����Ϊ
	public int doEndTag(  )
	{
		String checked ;
		HttpServletRequest req =
		                  (HttpServletRequest) pageContext.getRequest();
		checked = CookieUtils.isCookieSet(name, req ) ? "checked" : "";
		try{
		    pageContext.getOut().println( checked ) ;
	    }
	    catch( IOException  e ) {}   // Ignore
        return EVAL_PAGE;
	}

	//��ȡ��Ϊ��
	public int doAfterBody( )
	{
        //BodyContent bodyContent = getBodyContent();
        //��BodyTagSuppor ���Ѿ��õ�bodyContect
        JspWriter out = getPreviousOut();
		value = bodyContent.getString() ;
		return  SKIP_BODY ;
	}
    /**
     * Releases all instance variables.
     */
    public void release() {
        name = null;
        super.release();
    }

    /*  BodyTagSupport ʵ�ֵķ���
    public void setPageContext (PageContext  pageContext) ;
    public int  doStartPage()
    public int  doInitBody()
    public int  doAfterBody()
    */
}
