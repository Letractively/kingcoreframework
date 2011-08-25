/**
 * @(#)BaseAction..java		    2004/03/21
 *
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Zeven.woo
 */

package com.kingcore.framework.base.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import wzw.util.DbUtils;

import com.kingcore.framework.bean.NavigableDataSet;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.bean.QueryDataSet;
import com.kingcore.framework.bean.impl.WebPageNavigator;
import com.kingcore.framework.exception.ApplicationException;
import com.kingcore.framework.exception.InvalidSessionException;
import com.kingcore.framework.exception.NoPrivilegeException;
import com.kingcore.framework.exception.SystemException;


/**
 * <pre>
 *   ��չStruts����е�Action�࣬���Ǻ�����һЩ������������Action����չ���࣬<br>
 *       �������ٸ��Ǵ����executeAction ������
 * 
 * ����** ֱ�Ӷ����ṩ���񣬲���Ҫʹ�ýӿڡ�
 * 		> �ʺϻ��� Struts MVC �Ŀ���;
 *		> ����web��controller�㣬��ws��ͬһ����εģ�����ֱ�ӵ���Dao(2��)��Ҳ���Ե���Service(3�㣬��Service����Dao);
 *		> �౾����������̰߳�ȫ��;
 *		> ���෽��֧�ֲ鿴��	wzw.util.HttpUtils
 *							javax.servlet.http.HttpUtils
 *
 * 
 * </pre>
 * @author	WUZEWEN on 2004.03.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class BaseAction extends Action
{

    public static final String CHANGE_PAGE = "changepage";
    /**
     * ������Ϣ����ļ��϶���
     */
    public final ActionErrors errors = new ActionErrors();
    
    /**
     * log4j��־����  ������ protected ��Ϊ private. 
     */
    protected static Logger log = Logger.getLogger(com.kingcore.framework.base.controller.BaseAction.class);

	/**
     * <p>��Action �Լ��������д�����Ϣ�Ĵ�������</p>
     * @author WUZEWEN on 2004-3-25
     * @param property �����������
     * @param error ������Ϣ����
     * @throws
     */
    protected void addErrors(String property, ActionError error)
    {
        errors.add(property, error);
    }

    protected void addErrors(ActionError error)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, error);
    }

    protected void addErrors(String key)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, new ActionError(key));
    }

    protected void clearErrors(){
    	errors.clear();
    }

    protected void saveError(HttpServletRequest request)
    {
        this.saveErrors(request, errors);
    }

	/**
     * <pre>this method is to override the same function in struts1.2 frame</pre>
     * @author WUZEWEN on 2004-3-25
     * @param ActionMapping mapping
     * @param ActionForm form
     * @return ActionForward forward
     * @throws Exception
     */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
					throws IOException, ServletException, Exception {
    	clearErrors();

    	try{
    		return executeAction(mapping, form, request, response);

    	} catch (ApplicationException ea) {
    		if(ea.getShowType()==0){
    			request.setAttribute("key.global.exception", ea);
    			return (mapping.findForward("application-exception"));
    		}else{
    			request.setAttribute("key.global.exception", ea);
    			return (mapping.findForward("application-exception-dialog"));
    		}
    		
    	} catch (NoPrivilegeException es) {
			request.setAttribute("key.global.exception", es);
			return (mapping.findForward("application-noPrivilegeException"));

    	}  catch (SystemException es) {
			request.setAttribute("key.global.exception", es);
			return (mapping.findForward("system-exception"));

    	} catch (InvalidSessionException e){

    		if( e.getShowType()==2 ) {	// ����Ҫ�Զ���ת
    			return mapping.findForward( e.getForwardName() ); //"user-login"
    		}
    		
    		String previousURL=request.getRequestURI()+"?";// +request.getQueryString();
    		
    		Enumeration er = request.getParameterNames();
    		String name=null;
    		while(er.hasMoreElements()){
    			name=er.nextElement().toString() ;
    			previousURL += name + "="+ java.net.URLEncoder.encode(request.getParameter(name), "utf-8") + "&";	//"%26";--��Ը���url������������
    		}
    		
    		log.debug( "previousURL is "+ previousURL);
    		request.setAttribute("previousURL", wzw.lang.Base64.encode(previousURL.getBytes("utf-8") ) );
    		return (mapping.findForward(e.getForwardName()));  // "user-login"
    		
    	} catch (Exception e) {
			e.getStackTrace();		// ���һ�Ρ�
			throw e;
    	}
	}

	/**
     * <pre>to compatible to old struts frame!
     *		����ʵ����execute�����У���ϸ��Ϣ��execute������</pre>
     * @author WUZEWEN on 2004-3-25
     */
    public ActionForward perform(ActionMapping mapping,
						         ActionForm form,
						         HttpServletRequest request,
						         HttpServletResponse response)
    						throws IOException, ServletException, Exception
    {
    	return execute(mapping,form,request,response);
    }

	/**
	 * <pre>�������Ǳ�Action��execute or perform�������ã���Action��������
	 *			��Ҫ override this method to do business logic!</pre>
	 * @author WUZEWEN on 2004-3-25
	 * @param mapping ����requestĿ���ӳ��
	 * @param form ҳ������bean
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ����ɹ�������һ��ActionForward�����������Ҫҳ����ת�򷵻�null
	 * @throws Exception ����ʧ��ʱ
	 */
    public abstract ActionForward executeAction(
						        ActionMapping mapping,
						        ActionForm form,
						        HttpServletRequest request,
						        HttpServletResponse response)
        					throws Exception ;

	/**
     * <p>�����Ƿ�װ�Ķ����ݿ�����ķ�����</p>
     * @deprecated ����һ�� DAO ���󣬶����ڸ�����ֱ�Ӳ������ݿ�
     * 
     * @author WUZEWEN on 2004-3-26
     * @return һ�����ݿ����Ӷ���
     * @throws ��ȡ����ʧ��ʱ���׳�SQLException��
    protected Connection getConnection() throws SQLException
    {
    	return wzw.util.DBUtils.getConnection();
    }
     */


	/**
     * <p>�����Ƿ�װ�Ķ����ݿ�����ķ�����</p>
     * @deprecated ����һ�� DAO ���󣬶����ڸ�����ֱ�Ӳ������ݿ�
     * 
     * @author WUZEWEN on 2004-3-26
     * @param request �������
     * @return һ�����ݿ����Ӷ���
     * @throws ��ȡ����ʧ��ʱ���׳�SQLException��
    protected Connection getConnection(HttpServletRequest request)
        										throws SQLException
    {
    	//wzw on 2005-11-17 ����application�л�ȡDataSource,
    	//������ϵͳ��DBUtils��ͳһ�������ӣ�����������Լ�JNDI����̬���

    	//wzw on 2006-09-06 ����jndi��ȡ���ݿ�����
    	return wzw.util.DBUtils.getConnection(request);

    }
     */

    /**
     * @deprecated ҳ����ת���Ʋ�ȡ��������
     * @param request
     * @return
    protected String getDatabaseType(HttpServletRequest request){
   		ServletContext context = request.getSession().getServletContext();
   		return (String)context.getAttribute(Constants.DATABASE_TYPE);
   	}
     */


	/**
     * <p>��request,session���еĶ���Ļ�ȡ,����Ƕ���򷵻ص�һ��ֵ��</p>
     * @author WUZEWEN on 2004-3-26
     * @param request �������
     * @param parameterName ��������
     * @param defaultValue ����ȱʡֵ
     * @return �����ֵ��
     */
   	protected String getParameter(
        					HttpServletRequest request,
        					String parameterName,
        					String defaultValue)
    {
        return wzw.util.HttpUtils.getParameter(request, parameterName, defaultValue);
    }
   	
    /**
     * <p>��ȡSession �еĶ���</p>
     * @param request HttpServletRequest
     * @param objectName ��������
     * @return Object
     */
    protected Object getObjectInSession(
        HttpServletRequest request,
        String objectName)
    {
    	return wzw.util.HttpUtils.getObjectInSession( request, objectName) ;
    }
    /**
     * <p>�Ƴ�Session�еĶ���</p>
     * @param request HttpServletRequest
     * @param objectName ��������
     */
    protected void removeObjectInSession(
        HttpServletRequest request,
        String objectName)
    {
    	wzw.util.HttpUtils.removeObjectInSession( request, objectName ) ;
    }

    /**
     * <p>���ö���Session��ȥ��</p>
     * @param request HttpServletRequest
     * @param objectName ��������
     * @param obj ���������
     */
    protected void setObjectInSession(
        HttpServletRequest request,
        String objectName,
        Object obj)
    {
    	wzw.util.HttpUtils.setObjectInSession(request, objectName, obj ) ;
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for connection cleanup.
     * @deprecated ���ݿ��ѯ���������ڿ��Ʋ㴦�����ݿ������������Dao�㴦��
     * @param sql The query to execute.
     * @return The object returned by the Service.
     * @throws SQLException
    protected RowSet doQuery(String sql)
        throws SQLException {
		return DBUtils.doQuery(sql);
    }
     */

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
     * <pre>
     *   �޸�Ϊprivate���� by wzw���򻯿�����
     *   ���Ҫ��ѯҳ��ķ�ҳ��Ϣ�� ע�⣺����������ϵͳ��������rowCount,pageSize,pageNumber��
     * 	 ���������Ҫ���ڿ��Ʋ��ȡ��ҳ��Ϣ��Ȼ�󴫵�->Server->Dao�㣬�����洦��
     * 	int[0]�������� 	rowCount
     * 	int[1]��ÿҳ����	pageSize
     * 	int[2]����ҳ��	pageNumber
     * </pre>
     * @param request
     * @return
     */
    private int[] getPageParameter(HttpServletRequest request) {

//		String action = request.getParameter("Action");
//		log.debug(" param action is " + action);
		
		// page control parameters.
		int[] pageParams = new int[3];
		String str_rowCount = request.getParameter("rowCount");
		String str_pageSize = request.getParameter("pageSize");
		String str_pageNumber = request.getParameter("pageNumber");
		
		//get rowCount value.
		int rowCount=-1;
		if(str_rowCount!=null && !str_rowCount.trim().equals("") ){
			try{
				rowCount = Integer.parseInt(str_rowCount);
			}catch(Exception te){
				log.warn("warning!! parameter rowCount is " + str_rowCount);
				log.debug("warn", te);
				//te.pri ntStackTrace();   Zeven on 2008-05-21 ��̨������Ϣ���ε��������������������
				//throw te;
			}
		}
		
		//get pageSize value.
		int pageSize=30;
		if(str_pageSize!=null && !str_pageSize.trim().equals("") ){ 
			try{
				pageSize = Integer.parseInt(str_pageSize);
			}catch(Exception te){
				log.fatal("warning! pageSize is " + str_pageSize);
				log.debug("fatal", te);
				//te.pri ntStackTrace();	//   Zeven on 2008-05-21 ��̨������Ϣ���ε��������������������
				//throw te;
			} 
		}
		
		//get pageNumber value.
		int pageNumber=1;
		if(str_pageNumber!=null && !str_pageNumber.trim().equals("") ){ 
			try{
				pageNumber = Integer.parseInt(str_pageNumber);
			}catch(Exception te){
				log.fatal("warning! pageNumber is " + str_pageNumber);
				log.debug("fatal", te);
				//te.pri ntStackTrace();	//   Zeven on 2008-05-21 ��̨������Ϣ���ε��������������������
				
				pageNumber=1;
				//// this.setPageNumber(1);
			} 
		}
		
		pageParams[0] = rowCount;
		pageParams[1] = pageSize;
		pageParams[2] = pageNumber;
		
		return pageParams;
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
	
//	/**
//	 * ����Webҳ��ʹ�õĿɷ�ҳ������Ϣ��
//	 * @deprecated ����ʹ�� getNavigableDataSet(int[] pageParams, List dataList) or getNavigableDataSet(int[] pageParams, RowSet rowSet)��
//	 * @param pageParams
//	 * @param datas
//	 * @return
//	 */
//	protected NavigableDataSet getNavigableDataSet(int[] pageParams, Object datas){
//		if( datas instanceof RowSet){
//			return new QueryDataSet(pageParams , (RowSet)datas );
//		
//		}else if( datas instanceof List){
//			log.fatal("not complete!!!!");
//			return new QueryDataSet(pageParams , (List)datas ); //(List)datas
//		}
//		
//		return null;
//	}
	
	/**
	 * <pre>
	 * ����Webҳ��ʹ�õĴ��пɷ�ҳ��������Ϣ�����ݼ��Ķ���
	 * 	��Ҳ����ʹ�á�getNavigator��������������ȡ������Ϣ����
	 * 
	 * ʹ�ý���--------------------------��
	 * 	 ��̨���Ʋ���룺
	 * 		...
	 * 		Navigator navigator = this.createNavigator(...);
	 * 		Object stockOrders = this.getNavigableDataSet(navigator, dataList);
	 * 		...
	 * 		request.setAttribute("stockOrders", stockOrders);
	 * 		...
	 * 
	 *   ҳ��View����룺
	 * 		...
	 * 		<td><woo:navigator name="stockOrders" path="${path}" type="pnfl" scope="request"/></td>
	 * 		...
	 * </pre>
	 * @param pageParams ��ҳ������Ϣ����ʹ�� getPageParameter(HttpServletRequest) ������ȡ�����ҿɱ��޸�
	 * @param dataList ���ݼ�����
	 * @return
	 */
	protected NavigableDataSet getNavigableDataSet(HttpServletRequest request, String path, List dataList){
		Navigator navigator = this.createNavigator(request, path);
		return new QueryDataSet(navigator , path, dataList ); //(List)datas
	}

	/**
	 * 
	 * <pre>
	 * ����Webҳ��ʹ�õĴ��пɷ�ҳ��������Ϣ�ĺ����ݼ��Ķ���
	 * 	��Ҳ����ʹ�á�getNavigator��������������ȡ������Ϣ����
	 * 
	 * ʹ�ý���--------------------------��
	 * 	 ��̨���Ʋ���룺
	 * 		...
	 * 		Navigator navigator = this.createNavigator(...);
	 * 		Object stockOrders = this.getNavigableDataSet(navigator, crs);
	 * 		...
	 * 		request.setAttribute("stockOrders", stockOrders);
	 * 		...
	 * 
	 *   ҳ��View����룺
	 * 		...
	 * 		<td><woo:navigator name="stockOrders" path="${path}" type="pnfl" scope="request"/></td>
	 * 		...
	 *  </pre>
	 * @param pageParams ��ҳ������Ϣ����ʹ�� getPageParameter(HttpServletRequest) ������ȡ�����ҿɱ��޸�
	 * @param dataList ���ݼ�����
	 * @return
	 */
	protected NavigableDataSet getNavigableDataSet(HttpServletRequest request, String path, RowSet rowSet){
		Navigator navigator = this.createNavigator(request, path);
		return new QueryDataSet(navigator , path, rowSet ); //(List)datas
	}
	

	/**
	 * <pre>
	 * ����Webҳ�浼����ҳ���� ע�⣺����������ϵͳ��������rowCount,pageSize,pageNumber��
	 *    �����ǷŻص����ĵ�������,�����ݶ�����롣
	 *         ��getNavigableDataSet�����������ϵ�����������ݶ���Ϊһ�����󷵻�.
	 * 		���Ը������ѡ�� getNavigator �� getNavigableDataSet ������
	 * 	   ������Լ��ĵ�����������д�Լ��ĵ�����(�̳�WebPageNavigator)�����Ǳ�������
	 * ʹ�ý���--------------------------��
	 * 	 ��̨���Ʋ���룺
	 * 		...
	 * 		Navigable nvg = this.createNavigator(pageParams);
	 * 		...
	 * 		...
	 * 		request.setAttribute("stockOrdersNvg", nvg);
	 * 		...
	 * 
	 *   ҳ��View����룺
	 * 		...
	 * 		<td><woo:navigator name="stockOrdersNvg" path="${path}" type="pnfl" scope="request"/></td>
	 * 		...
	 *  </pre>
	 * @param path �����url·��
	 * @vision 2.0
	 * @return
	 */
	protected Navigator createNavigator(HttpServletRequest request, String path ){
		int[] pageParams = this.getPageParameter(request); //wzw�������ڲ�
		return new WebPageNavigator( pageParams, path ); //(List)datas
	}
    
}
