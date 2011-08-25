/**
 * Copyright (C) 2002-2009 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.base.controller;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import wzw.util.DbUtils;

import com.kingcore.framework.exception.InvalidSessionException;
import com.kingcore.framework.exception.NoPrivilegeException;

/**
 * <p>dwr ��ǰ�δ�����Ļ��࣬����POJO��
 * 		�������ṩ�����ķ���������Ự��飬Ȩ�޼��ȡ���
 * 		����web��controller�㣬����controllerĿ¼����package����ȡ�</p>
 * 
 * @author Zeven on Dec 2, 2009
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class BaseDwr {

	private final static Logger log = Logger.getLogger(BaseDwr.class);

    /**
     * ���Ựʱ����Ч��
     * @param request �������
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request) throws Exception{
    	checkSessionValid( request, "userid");
    }
    
    /**
     * ���Ựʱ����Ч��
     * @param request �������
     * @param userObejctName ��ʶ�Ự��Ч�Ķ�������ƣ����ݴ˶���ȷ���Ự�Ƿ���Ч
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request,
				String userObejctName) throws Exception{
    	checkSessionValid( request, userObejctName,"user-login");
    }

    /**
     * ���Ựʱ����Ч�������Ч������ת����Ӧ��ҳ�档
     * @param request �������
     * @param userObejctName ��ʶ�Ự��Ч�Ķ�������ƣ����ݴ˶���ȷ���Ự�Ƿ���Ч
     * @param forwardName �Ự��Ч��ת����ҳ�棬��struts��config�����õ�ȫ��forwardֵ
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request,
    							String userObejctName,
    							String forwardName ) throws Exception{
    	checkSessionValid(request,
							userObejctName,
							forwardName , 0);
    }

    /**
     * ������Ҫ�ж��û���ȷ��¼�����һỰû�н����ĵط������ñ�������
     * @param request HttpServletRequest
     * @param userObejctName ����������ƣ�Ĭ��Ϊ userid
     * @param forwardName ��¼ҳ���Ӧ���õ�forwardName,Ĭ��Ϊuser-login
     * @param needBack ��¼֮���Ƿ���Ҫ���ص���ǰҳ��(0ΪNo��Ĭ��ֵ; 1ΪYes)
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request,
    							String userObejctName,
    							String forwardName, 
    							int needBack ) throws Exception{
    	//System.out.println("-----------go0 " + request.getQueryString() );
    	if(request.getSession().getAttribute(userObejctName)==null){
    		// ���û�û�е�½�������⴦��
				//throw new InvalidSessionException("û�е�½���߻Ự������" ,0, forwardName);
    		needBack = (needBack==1)?0:2;
    		throw new InvalidSessionException("û�е�½���߻Ự������" , needBack , forwardName);
    		 
    	} 
    }

    /**
     * ---------------------------checkPrivilege----------------- ���Ȩ�ޣ��Ժ�ŵ�ÿ����Ŀ��xxxBaseAction�д���
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣
     * ����û��Ƿ����Ȩ�ޡ�
     * 		���ȼ��Ự�Ƿ���ڣ�������ڣ��׳��Ự�����쳣���Զ���ת����½ҳ�档
     * 		����Ự����������Ƿ��в����ù��ܵ�Ȩ�ޣ����û�У�����ҳ����ʾ���Բ�����û�в����ù��ܵ�Ȩ�ޣ�����
     * @param request HttpServletRequest
     * @param empObejctName �û����ƣ�Ĭ��Ϊ "empid".
     * @param priviCode Ȩ�ޱ��
     * @param forwardName ��¼ҳ���Ӧ���õ�forwardName,Ĭ��Ϊuser-login
     * @throws Exception
     */
    protected void checkPrivilege(HttpServletRequest request,
    								String empObejctName, 
    								String priviCode,
    								String forwardName ) throws Exception{
    	//�ȼ���Ƿ��½�����ڼ��Ȩ�޶���Ҫ���ĻỰ��飬��¼֮����Ҫ��ת
    	//checkSessionValid(request,empObejctName);
    	if(request.getSession().getAttribute(empObejctName)==null){
    		// ���û�û�е�½�������⴦��
    		//if( userObejctName.equals("userid") ){
    			throw new InvalidSessionException("û�е�½���߻Ự������", 2 , forwardName);
    		//}
    	}
    	
    	boolean hasPrivi = hasPrivilege( request, empObejctName ,priviCode);
    	// ���Ȩ�޴���
    	
    	if(!hasPrivi){
    		throw new NoPrivilegeException("�Բ�����û�в����ù��ܵ�Ȩ�ޣ�");
    	}
    }
    
    /**
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣
     * @param request
     * @param empObejctName
     * @param priviCode
     * @throws Exception
     */
    protected void checkPrivilege(HttpServletRequest request,
			String empObejctName, 
			String priviCode) throws Exception{
    	checkPrivilege( request, empObejctName, priviCode, "user-login"); 
    }
    /**
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣
     * @param request
     * @param priviCode
     * @throws Exception
     */
    protected void checkPrivilege(HttpServletRequest request,String priviCode) throws Exception{
    	checkPrivilege( request, "empid" ,priviCode);
    }
    
    
    /**
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣
     * ����û��Ƿ���е�ǰ���ܲ�����Ȩ�ޡ�
     * 		���÷�ʽ����BaseAction�������У�ֱ�ӵ��ã��磺 hasPrivilege(request, "empid", "102001");
     * @param request
     * @param empObejctName ϵͳԱ����������ƣ����ݸ�������session�л�ȡֵ����"empid"��
     * @param privilageId ϵͳȨ��ͳһ�ı�š�
     * @return return true if the employee has privilage, else return false.
     * @throws Exception
     */
    protected boolean hasPrivilege(HttpServletRequest request,
    		String empObejctName, 
    		String privilageId ) throws Exception{

//  	���Ȩ�޴���, Ա������ɫ��Ȩ�ޡ�
    	boolean hasPrivi = false;
    	String empid = request.getSession().getAttribute(empObejctName).toString();
    	int count = DbUtils.getSize("Tper_Orgemp T1, Tper_Orgroleemp T2, Tper_Roleprivi T3",
    						"Where T1.Empid = T2.Empid And T2.Roleid = T3.Roleid And T3.Privilegeid = "+privilageId+" And t3.delflag=0 And t1.empid="+empid);
    	
    	if(count>0){
    		hasPrivi = true;
    	}
    	return hasPrivi;
    }
    
    /**
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣
     * ����Ƿ���Ȩ�ޡ�
     * @param request
     * @param privilageId
     * @return
     * @throws Exception
     */
    protected boolean hasPrivilege(HttpServletRequest request,String privilageId) throws Exception{
    	return hasPrivilege( request, "empid" ,privilageId);
    }
    
	
	/**
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣
	 * ajax������Ա���Ƿ���Ȩ��,ajax����ר�÷�����
	 * @param request
	 * @param response
	 * @param empObejctName
	 * @param priviCode
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean checkPrivilege(
									HttpServletRequest request,
									HttpServletResponse response,
									String empObejctName, 
									String priviCode) throws Exception{
		
		
		String msg ="��û�в����ù��ܵ�Ȩ��!";
		// ���Ȩ�޴���
		boolean hasPrivi = true ;
		try{
			// ����Ự��Ч����Ȩ�ޣ�������Ҫ���Ȩ��
			if(request.getSession().getAttribute(empObejctName)==null){
	    		hasPrivi = false;
				msg="����û�е�¼���ߵ�¼���ʱ��̫���������µ�¼!";
	    	}else {
				hasPrivi = hasPrivilege( request, empObejctName ,priviCode);
	    	}
			
		}catch(Exception e2){
			hasPrivi = false;			
			msg="���������쳣��["+e2.getMessage()+"]�����µ�½������ϵ����Ա!";
		}

		if( hasPrivi==false ){

			response.setContentType("text/xml; charset=UTF-8");
			PrintWriter printWriter=response.getWriter();
			printWriter.println("<response>");
			printWriter.println("<content>false</content>");
			printWriter.println("<content>"+msg+"</content>");
			printWriter.println("</response>");
			printWriter.close();
		}

		return hasPrivi;
	}

	/**
	 * <pre>
	 * ��Ҫ�������о���ʵ��Ȩ�޵ļ�顣�����ھ���ҵ���Action����ȥ���ǡ�
	 * </pre>
	 * @param request
	 * @param response
	 * @param priviCode
	 * @return
	 * @throws Exception
	 */
	protected boolean checkPrivilege(HttpServletRequest request,HttpServletResponse response,String priviCode) throws Exception {
		return checkPrivilege( request,response, "empid" ,priviCode);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
