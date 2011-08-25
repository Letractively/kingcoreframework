/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.core.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kingcore.framework.base.controller.BaseAction;


/**
 * <p>�����ϵͳ�ṩ�ļ�������ܣ�����ֱ������ļ����ݺʹ�ӡ�ļ����ݵ�ҳ�����ַ�ʽ��</p>
 * @author Zeven on 2007-12-4
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class FileProviderAction extends BaseAction {

	private Logger log = Logger.getLogger( this.getClass() );
	
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.base.controller.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) throws Exception {


		// ���ø�ʽ��var url = "/pda/core/fileProvider.jhtml?isPre=y&path="+file_path+"&type=dataFile&file="+ encodeURIComponent(fileName);
		// http://190.10.2.20:7001/fileProvider?file=20061228/1167279014828.jpg&type=appeal_file 
		// �������ݿ��¼������Ϊ "20061228/1167279014828.jpg"�����·������ type ��ֵ�������ļ��л�ȡ��
		//request.setCharacterEncoding("UTF-8");
		
		String fileName=request.getParameter("file");				// �ļ�����
		///String projectName = request.getParameter("prjectName");	// ��Ŀ����
		String type=request.getParameter("type");			// �ļ�����
		String forwardName=request.getParameter("forwardName");
		String isPre=request.getParameter("isPre");			// �Ƿ����pre��ʽ�����ҳ��

		if(forwardName==null){
			forwardName="common.login";
		}
		//this.checkSessionValid(request, "adminCode", forwardName );
		
		String outputFilePath = null; //PdaContext.getOutputFilePath() + File.separator + projectName;	//ͨ���ļ��������ⲿ�Ա��صķ���·����
		String fileDirectory = null;
		if("dataFile".equalsIgnoreCase(type) ) {
			fileDirectory = outputFilePath + File.separator + "data";
		}else if("tableFile".equalsIgnoreCase(type) ) {
			fileDirectory = outputFilePath ;	//+ File.separator + "";
		}
		
		log.debug( fileDirectory  );
		if(fileName==null){
			throw new ServletException("file name is null");
		}
		
		try{
			//fileName=fileName.replaceAll("\\","/");
			//fileDirectory = this.getServlet().getServletContext().getInitParameter( fileDirectory );
			//log.debug("------fileDirectory--11111-----" + fileDirectory);
			
			
			//fileDirectory="D:/temp";
			File fileObj=new File( fileDirectory + File.separator + fileName);
			log.debug( "file is "+fileObj.getAbsolutePath() );
			if(!(fileObj).exists()){
				throw new ServletException("file not exist");
			}
			
			if("Y".equalsIgnoreCase(isPre)) {  // ��������Ԥ�����
				response.setContentType("text/html;charset=gb2312");  // ���������������
				PrintWriter pw=response.getWriter();
				pw.write( "<pre>");
				pw.write( FileUtils.readFileToString( fileObj , "gb2312") );
				pw.write( "</pre>");
				return null;
			} // ������������
	
			java.io.BufferedInputStream bis=new java.io.BufferedInputStream(
											new java.io.FileInputStream(fileObj));
			//���
			//set contentType
//			String mimeType=request.getSession().getServletContext().getMimeType(fileName);
//			if(mimeType==null){
//				throw new ServletException("�Ƿ����ļ���չ����");
//			}else{
//				response.setContentType(mimeType); 		
//			}

			//response.setContentType("text/html"); 
			response.setContentType("text/html;charset=gb2312"); // ���������������
			//output bytes
			ServletOutputStream sos = response.getOutputStream(); 
			int blobsize = 1024; 
			byte[] blobbytes = new byte[blobsize]; 
			int bytesRead = 0; 
			//read() Returns: the number of bytes read, or -1 if the end of the stream has been reached. 
			while ((bytesRead = bis.read(blobbytes))>0) { 
				sos.write(blobbytes, 0, bytesRead); 
			} 
		
			//response.setContentType("text/html");
			sos.flush(); 
			//inputimage.close(); 
		}catch(java.lang.Exception e){
			//throw new ServletException(e) ;
			request.setAttribute("common.failDialog.message","����֤�ļ�����...");
			return mapping.findForward("common.failDialog");		//���ʾĿ¼���߼�
		}
		
		return null;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
