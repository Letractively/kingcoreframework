package com.kingcore.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kingcore.framework.util.ConvertUtils;

/**
 * This class is a tag handler for custom action that replaces
 * HTML special characters in its body with the corresponding
 * HTML character entities.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class EncodeHTMLTag extends BodyTagSupport {

    /**
     * Reads the body content, converts all HTML special characters
     * with the corresponding HTML character entities, and adds the
     * result to the response body.
     */
    public int doAfterBody() throws JspException {
    	//��ȡ��Ϊ������
        BodyContent bc = getBodyContent();
        //��ȡout ����   ע�⣺��ʹ�� getout()����
        //����Ϊ����������������������һ��ֵBodyContent����
        JspWriter out = getPreviousOut();
        try {
            out.write(ConvertUtils.toHTMLString(bc.getString()));
        }
        catch (IOException e) {} // Ignore
        return SKIP_BODY;
    }
}
