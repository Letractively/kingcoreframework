/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.bean.impl;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.kingcore.framework.bean.Navigator;

/**
 * <p>web ҳ��ʹ�õĵ�����ҳ����֧��getter,��ҳ���ʹ�ñ�ǩ��ȡ��</p>
 * @author Zeven on 2008-10-2
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class WebPageNavigator implements Navigator {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��־����
	 */
	public static Logger log = Logger.getLogger(com.kingcore.framework.bean.impl.WebPageNavigator.class);
	
	/**
	 * �����ĵ�һ����¼�к�
	 */
	protected int beginIndex;
	
	/**
	 * ���������һ����¼�к�
	 */
	protected int endIndex;
	
	/**
	 * һ�м�¼
	 */
	//protected DataBean data;

	/**
	 * ��ҳ���
	 */
	protected boolean isPaged;
	
	/**
	 * 
	 */
	private static int nvlCount=6;
	
	/**
	 *  wzw ��ʼ��������������Ϣ
	 * @param pageParams
	 */
	public WebPageNavigator(int[] pageParams, String path) {

		int rowCount = pageParams[0];
		int pageSize = pageParams[1];
		int pageNumber = pageParams[2];
		
		if(pageSize<1 || pageNumber<1 || rowCount<0){
			log.fatal("������ʼʧ�ܣ����ȵ��� getPageParameter��getNavigableDataSet ��������֮����ñ�����");
		}
		
		this.setPageSize( pageSize );
		this.setPageNumber(pageNumber);
		//this.setCurrentPageIndex( pageNumber );
		this.setRowCount(rowCount);
		this.setPageCount( (rowCount - 1) / pageSize + 1 );
		this.setPath(path);
		 
		doinit() ;
	}

	private WebPageNavigator(){
		
	}


	/**
	 * ��ʼ������Ϣ��
	 *
	 */
	private void doinit()
	{
		this.beginIndex = 0;
		this.endIndex = getPageSize() - 1;
		if( this.rowCount<0){
			this.rowCount = 0;
		}
	}

	public String getPagesPn() {
		if(getRowCount()<1){
			return "<div class='tab_bot_box'>��ǰû�м�¼��</div>";	//wzw on 2006-11-15, ���һ�����ݶ�û�У������������Ϣ�� 
		}
		
		String commandName = this.path;
		//log.debug("pagesPnfl )))))))) "+ getPageCount() );
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
		pageSize = getPageSize();	//;rows;
		//pageCount = (getRowCount() - 1) / rows + 1;
//		String title =
//			"(��"
//			+ ((currentPageIndex - 1) * pageSize + 1)
//			+ "/"
//			+ getRowCount()
//			+ ",ҳ"
//			+ getPageNumber()
//			+ "/"
//			+ getPageCount()
//			+ ")";

		StringBuffer pubHref = new StringBuffer();
		pubHref
			.append("<a onclick=\"javaScript:if(typeof(navigatorClick)=='function'){navigatorClick(this);}\" href=\"")
			.append(commandName)
			.append((commandName.indexOf("?")>0)?"&":"?")
			.append("Action=")
			.append(action)
			.append("&rowCount=")
			.append(rowCount)
			.append("&pageSize=")
			.append(pageSize);
			
		StringBuffer sb = new StringBuffer();

		sb.append( "<div class='tab_bot_box'>");
		
//		sb.append( "<div class='tab_bot_box'>")
//			.append("<span class='list_all'>")			
//			.append("��")
//			.append(getPageNumber())
//			.append("/")
//			.append(getPageCount())
//			.append("ҳ��")
//			.append((currentPageIndex - 1) * pageSize + 1)
//			.append("-")
//			.append( (((currentPageIndex - 1) * pageSize + getPageSize())<this.rowCount)?((currentPageIndex - 1) * pageSize + getPageSize()):this.rowCount)
//			.append("����")
//			.append(getRowCount())
//			.append("��")	
//			.append("</span>");
			
//		if (!isFirstPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=1")
//			.append("\">1</a>");	//�� ҳ
//		}
//		else
//		{
//			sb.append("<span >�� ҳ</span>");
//		}
		//
		if (hasPreviousPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex - 1))
			.append("\" class=\"page_turn\">&lt; ��һҳ</a>");
		}
		else
		{
			sb
			.append("<span class=\"no_page_turn\">&lt; ��һҳ</span>"); 
		}

//		// ...234567891011...
//		int fpi = currentPageIndex- nvlCount/2;
//		if(fpi>=pageCount-nvlCount){
//			fpi=pageCount-nvlCount;
//		}
//		if(fpi<1){
//			fpi=1;
//		}
//		if(fpi>1){
//			
//			if(1==currentPageIndex){
//				sb
//				.append( 1 );
//			}else{
//				sb
//				.append( pubHref )
//				.append("&pageNumber=1")
//				.append("\">1</a>");
//				
//			}
//			
//			if(fpi>2){
//				sb
//				.append("...");
//			}
//			
//		}
		
//		int iCnt=0;
//		while(iCnt< nvlCount && fpi<=pageCount){
//			if(fpi==currentPageIndex){
//				sb
//				.append("<span class='list_on_page'>"+ fpi++ +"</span>");
//				
//			}else{
//					
//				sb
//				.append( pubHref )
//				.append("&pageNumber=")
//				.append( fpi )
//				.append("\">"+ fpi++ +"</a>");
//			}
//			
//			iCnt++;
//		}
//		if(pageCount>=fpi){
//			if(pageCount>fpi){
//				sb
//				.append("...");
//			}
//			
//			if(pageCount==currentPageIndex){
//				sb
//				.append( pageCount );
//			}else{
//				sb
//				.append( pubHref )
//				.append("&pageNumber=")
//				.append( String.valueOf( getPageCount() ) )
//				.append("\">"+pageCount+"</a>");
//			}
//		}
		
		//
		if (hasNextPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex + 1))
			.append("\" class=\"page_turn\">��һҳ&gt;</a>");
		}
		else
		{
			sb
			.append("<span class=\"no_page_turn\">��һҳ&gt;</span>");
		}

		//
//		if (!isLastPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=")
//			.append( String.valueOf( getPageCount() ) )
//			.append("\">"+pageCount+"</a>");
//		}
//		else
//		{
//			sb
//			.append("<span >ĩ ҳ</span>");
//		}
 
		
//		if (pageCount > 1 && false)
//		{
//			String id = "toPage";	//commandName + "pageNumber";
//				//name=\"pageNumber\" id=\"toPage\" 
//			sb
//			.append("<td align=right valign=\"center\" nowrap>")
//			.append("&nbsp;������</td><td><input id='")
//			.append(id)
//			.append("' size=\"4\"></td>")
//			.append("<td>ҳ</td>")
//			.append("<td><input type=button onclick=\"javascript:")
//			.append("((document.getElementById('")
//			.append(id)
//			.append("').value==''||document.getElementById('")
//			.append(id)
//			.append("').value==")
//			.append(currentPageIndex)
//			.append(")?(''):(location.href='")
//			.append(commandName)
//			.append((commandName.indexOf("?")>0)?"&":"?")
//			.append("Action=")
//			.append(action)
//			.append("&pageSize=")
//			.append(pageSize)
//			.append("&pageNumber=' + ((document.getElementById('")
//			.append(id)
//			.append("').value=='')?")
//			.append(currentPageIndex)
//			.append(":")
//			.append("document.getElementById('")
//			.append(id)
//			.append("').value)))\" value=�鿴 class=input></td>") ;
//		}
		
		sb.append("</div>");
		return sb.toString(); 
	}

	
//  // -----------------------  ���뵽������
//	/**
//	 * �ж��Ƿ�����ҳ
//	 * @return boolean
//	 */
//	public boolean hasNextPage()
//	{
//		//log.debug(currentPageIndex+ "  "+  pageCount );
//		//log.debug(currentPageIndex+ "  "+  getPageCount() );
//		if ( this.currentPageIndex < getPageCount() )
//		{
//			return true;
//		}
//		return false;
//	} 
//
//	/**
//	 * �ж��Ƿ�����ҳ
//	 * @return boolean
//	 */
//	public boolean hasPreviousPage()
//	{
//		if ( this.currentPageIndex > 1 )
//		{
//			return true;
//		}
//		return false;
//	}
//	

	
	/**
	* <p>������Web���������棬ÿ�η�ҳ���������ݿ⣬
	* 		��Բ�ͬ��������Oracle,SQL Server���ò�ͬ�ķ�װʵ�֡�
	* 	wzw on 2006-11-28 ��onclick�¼��е�exit�޸�Ϊ'',ʹ��return Ҳ���С�</p>
	* @param commandName ��ҳʱ�õĵ�URL��������з�ҳ��Ϣ������������Ҫ�Ĳ���
	* @return ��ѯ������Ϣhtml����
	*/
	public String getPagesPnfl( )
	{
		if(getRowCount()<1){
			return "<div class='tab_bot_box'>��ǰû�м�¼��</div>";	//wzw on 2006-11-15, ���һ�����ݶ�û�У������������Ϣ�� 
		}
		
		String commandName = this.path;
		//log.debug("pagesPnfl )))))))) "+ getPageCount() );
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
		pageSize = getPageSize();	//;rows;
		//pageCount = (getRowCount() - 1) / rows + 1;
		String title =
			"(��"
			+ ((currentPageIndex - 1) * pageSize + 1)
			+ "/"
			+ getRowCount()
			+ ",ҳ"
			+ getPageNumber()
			+ "/"
			+ getPageCount()
			+ ")";

		StringBuffer pubHref = new StringBuffer();
		pubHref
			.append("<a onclick=\"javaScript:if(typeof(navigatorClick)=='function'){navigatorClick(this);}\" href=\"")
			.append(commandName)
			.append((commandName.indexOf("?")>0)?"&":"?")
			.append("Action=")
			.append(action)
			.append("&rowCount=")
			.append(rowCount)
			.append("&pageSize=")
			.append(pageSize);
			
		StringBuffer sb = new StringBuffer();
		
		sb.append( "<div class='tab_bot_box'>")
			.append("<span class='list_all'>")			
			.append("��")
			.append(getPageNumber())
			.append("/")
			.append(getPageCount())
			.append("ҳ��")
			.append((currentPageIndex - 1) * pageSize + 1)
			.append("-")
			.append( (((currentPageIndex - 1) * pageSize + getPageSize())<this.rowCount)?((currentPageIndex - 1) * pageSize + getPageSize()):this.rowCount)
			.append("����")
			.append(getRowCount())
			.append("��")	
			.append("</span>");
			
//		if (!isFirstPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=1")
//			.append("\">1</a>");	//�� ҳ
//		}
//		else
//		{
//			sb.append("<span >�� ҳ</span>");
//		}
		//
		if (hasPreviousPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex - 1))
			.append("\">&lt; ��һҳ</a>");
		}
		else
		{
			sb
			.append("<span >&lt; ��һҳ</span>"); 
		}

		// ...234567891011...
		int fpi = currentPageIndex- nvlCount/2;
		if(fpi>=pageCount-nvlCount){
			fpi=pageCount-nvlCount;
		}
		if(fpi<1){
			fpi=1;
		}
		if(fpi>1){
			
			if(1==currentPageIndex){
				sb
				.append( 1 );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=1")
				.append("\">1</a>");
				
			}
			
			if(fpi>2){
				sb
				.append("...");
			}
			
		}
		
		int iCnt=0;
		while(iCnt< nvlCount && fpi<=pageCount){
			if(fpi==currentPageIndex){
				sb
				.append("<span class='list_on_page'>"+ fpi++ +"</span>");
				
			}else{
					
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( fpi )
				.append("\">"+ fpi++ +"</a>");
			}
			
			iCnt++;
		}
		if(pageCount>=fpi){
			if(pageCount>fpi){
				sb
				.append("...");
			}
			
			if(pageCount==currentPageIndex){
				sb
				.append( pageCount );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( String.valueOf( getPageCount() ) )
				.append("\">"+pageCount+"</a>");
			}
		}
		
		//
		if (hasNextPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex + 1))
			.append("\">��һҳ&gt;</a>");
		}
		else
		{
			sb
			.append("<span >��һҳ&gt;</span>");
		}

//
//		if (!isLastPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=")
//			.append( String.valueOf( getPageCount() ) )
//			.append("\">"+pageCount+"</a>");
//		}
//		else
//		{
//			sb
//			.append("<span >ĩ ҳ</span>");
//		}
 
		
		// Ҫ��ҳ������1�Ż���֡���ת��...������
		// ִ������������� 
		//      > ��д��Ҳ�� ������1->��ҳ����֮�䣬���Ҳ��ܵ��ڵ�ǰҳ��
		if (pageCount > 1 )
		{
			String id = "toPage";	//commandName + "pageNumber";
				//name=\"pageNumber\" id=\"toPage\" 
			sb
			.append("<span>��ת��")
			.append( pubHref )
			.append("\" id=\"toPageHref\" style='display:none;'></a>")
			.append("<input type=\"text\" class=\"tab_bot_text\" id=\"")
			.append(id)
			.append("\"><input type=\"button\" class=\"tab_bot_button\" onclick=\"javaScript:")
			.append("if(!(document.getElementById('")
			.append(id)
			.append("').value==''||document.getElementById('")
			.append(id)
			.append("').value<1||document.getElementById('")
			.append(id)
			.append("').value>")
			.append( pageCount )
			.append("||document.getElementById('")
			.append(id)
			.append("').value==")
			.append(currentPageIndex)
			.append(")){document.getElementById('toPageHref').href+='&pageNumber='+")
			.append("document.getElementById('")
			.append(id)
			.append("').value+'&t='+")
			.append("new Date().getTime();if(typeof(navigatorClick)=='function'){navigatorClick(this);};window.location.href=document.getElementById('toPageHref').href;}\" value=\"GO\"></span>") ;
			//.append("new Date().getTime())?document.getElementById('toPageHref').click():''))\" value=\"GO\"></span>") ;
		}
		
		sb.append("</div>");
		return sb.toString(); 
	}

	

	/**
	* <p>��ҳ������Ϣ��Zeven on 2009-07-31����һ�ַ��ĵ������롣
	* 
	* 	Ч�����£�
	*************************************************************************************
			<div class="page_box">
                <a class="page_up" href="#"><span>��һҳ</span></a><!--û����һҳʱ��ȥ��href="#"����ʽ��Ϊ��page_up_unable��-->
                <a href="#">1</a>
                <span>2</span>
                <a href="#">3</a>
                <a href="#">4</a>
                <a href="#">5</a>
                <a href="#">6</a>
                <a href="#">7</a>
                <span>��</span>
                <a href="#">30</a>
                <a href="#">31</a>
                <a class="page_down_unable"><span>��һҳ</span></a><!--û����һҳʱ��ȥ��href="#"����ʽ��Ϊ��page_down_unable��-->
                <span>��30ҳ,��</span>
                <input class="page_text" name="" type="text" />
                <span>ҳ</span>
                <input class="button" name="" type="button" value="ȷ��" />
            </div>
	*************************************************************************************
	* 
	* 	</p>
	* @param rows ÿҳ��ʾ���������
	* @param commandName Command��URL
	* @return ��ѯ������Ϣhtml����
	*/
	public String getPagesPnfl2( )
	{
		
		if(getRowCount()<1){
			return "<div class='page_box'>��ǰû�м�¼��</div>"; 
		}
		String commandName = this.path;
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
		pageSize = getPageSize();	//;rows;

		StringBuffer pubHref = new StringBuffer();
		pubHref
			.append("<a onclick=\"javaScript:if(typeof(navigatorClick)=='function'){navigatorClick(this);}\" href=\"")
			.append(commandName)
			.append((commandName.indexOf("?")>0)?"&":"?")
			.append("Action=")
			.append(action)
			.append("&rowCount=")
			.append(rowCount)
			.append("&pageSize=")
			.append(pageSize);
			
		StringBuffer sb = new StringBuffer();
		
		sb.append( "<div class='page_box'>");

		if (hasPreviousPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex - 1))
			.append("\" class='page_up'><span>��һҳ</span></a>");
		}
		else
		{
			sb
			.append("<a class='page_up_unable'><span>��һҳ</span></a>"); 
		}

		// ...234567891011...
		int fpi = currentPageIndex- nvlCount/2;
		if(fpi>=pageCount-nvlCount){
			fpi=pageCount-nvlCount;
		}
		if(fpi<1){
			fpi=1;
		}
		if(fpi>1){
			
			if(1==currentPageIndex){
				sb
				.append( "<span class='current_page'>1</span>" );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=1")
				.append("\">1</a>");
			}
			
			if(fpi>2){
				sb
				.append("<span>...</span>");
			}
			
		}
		
		int iCnt=0;
		while(iCnt< nvlCount && fpi<=pageCount){
			if(fpi==currentPageIndex){	// ��ǰҳ
				sb
				.append("<span class='current_page'>"+ fpi++ +"</span>");
				
			}else{
					
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( fpi )
				.append("\">"+ fpi++ +"</a>");
			}
			
			iCnt++;
		}
		if(pageCount>=fpi){
			if(pageCount>fpi){
				sb
				.append("<span>...</span>");
			}
			
			if(pageCount==currentPageIndex){  // ��ǰҳ
				sb
				.append( "<span class='current_page'>"+pageCount+"</span>" );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( String.valueOf( getPageCount() ) )
				.append("\">"+pageCount+"</a>");
			}
		}
		
		//
		if (hasNextPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex + 1))
			.append("\" class='page_down'><span>��һҳ</span></a>");
		}
		else
		{
			sb
			.append("<a class='page_down_unable'><span>��һҳ</span></a>");
		}
		
		//<span>��30ҳ,��</span>
		if (pageCount > 1 ){
			sb.append("<span>��"+this.getPageCount()+"ҳ,��</span>");
		}else{
			sb.append("<span>��"+this.getPageCount()+"ҳ</span>");
		
		}
		
		// Ҫ��ҳ������1�Ż���֡���ת��...������
		// ִ������������� 
		//      > ��д��Ҳ�� ������1->��ҳ����֮�䣬���Ҳ��ܵ��ڵ�ǰҳ��
		if (pageCount > 1 )
		{
			String id = "toPage";	//commandName + "pageNumber";
				//name=\"pageNumber\" id=\"toPage\" 
			sb
			//.append("<span>��ת��")
			.append( pubHref )
			.append("\" id=\"toPageHref\" style='display:none;'></a>")
			.append("<input type=\"text\" class=\"page_text\" id=\"")
			.append(id)
			.append("\"><span>ҳ</span><input type=\"button\" class=\"button\" onclick=\"javaScript:")
			.append("if(!(document.getElementById('")
			.append(id)
			.append("').value==''||document.getElementById('")
			.append(id)
			.append("').value<1||document.getElementById('")
			.append(id)
			.append("').value>")
			.append( pageCount )
			.append("||document.getElementById('")
			.append(id)
			.append("').value==")
			.append(currentPageIndex)
			.append(")){document.getElementById('toPageHref').href+='&pageNumber='+")
			.append("document.getElementById('")
			.append(id)
			.append("').value+'&t='+")
			.append("new Date().getTime();if(typeof(navigatorClick)=='function'){navigatorClick(this);};window.location.href=document.getElementById('toPageHref').href;}\" value=\"ȷ��\">") ; //</span>
			//.append("new Date().getTime())?document.getElementById('toPageHref').click():''))\" value=\"GO\"></span>") ;
		}
		
		sb.append("</div>");
		return sb.toString(); 
	}

	
//	/**
//	 *  �������캯����
//	 * @param pageParams
//	 */
//	public Navigator(int[] pageParams) {
//	}
	
	//------------------------------------------------- 
	/**
	 * ������
	 */
	protected int rowCount;
	
	/**
	 * ÿҳ��ʾ������ Ĭ��ֵ
	 */
	protected int pageSize = 20;
	
	/**
	 * ��ҳ��
	 */
	protected int pageCount;

	/**
	 *��ǰҳ��
	 */
	protected int currentPageIndex = 1;

	/**
	 * ����������Ϣ
	 */
	protected String path = null;
	

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		
	}


	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
		
	}


	/**
	 *  ����ÿҳ��ʾ��������
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		
		if(pageSize>120) {	// �������ֵ����ֹ������Ϊ��	
			pageSize = 120;
		}
		this.pageSize = pageSize;
		
	}

	public int getPageSize() {
		return this.pageSize;
	}

	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getPageCount() {
		return this.pageCount;
	}


	public void setPageNumber(int pageNumber) {
		this.currentPageIndex = pageNumber;
		
	}
	
	public int getPageNumber() {
		return this.currentPageIndex;
	}
	
	/**
	 * �ж��Ƿ�����ҳ
	 * @return boolean
	 */
	public boolean hasNextPage()
	{
		//log.debug(currentPageIndex+ "  "+  pageCount );
		//log.debug(currentPageIndex+ "  "+  getPageCount() );
		if ( this.currentPageIndex < getPageCount() )
		{
			return true;
		}
		return false;
	} 

	/**
	 * �ж��Ƿ�����ҳ
	 * @return boolean
	 */
	public boolean hasPreviousPage()
	{
		if ( this.currentPageIndex > 1 )
		{
			return true;
		}
		return false;
	}

	/**
	 * �ж��ǵ�һҳ
	 * @return boolean
	 */
	public boolean isFirstPage()
	{
		if ( this.currentPageIndex == 1)
		{
			return true;
		}
		return false;
		
	}
	/**
	 * �ж��Ƿ������һҳ
	 * @return boolean
	 */
	public boolean isLastPage()
	{
		if ( this.currentPageIndex == getPageCount() )
		{
			return true;
		}
		return false;
	}
	
	// ----------------------------------------------------------


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ע�⣺�ӿ�����ĳ�Ա������final���ͣ����ܱ��޸ĵġ�
		Navigator wng = new WebPageNavigator();
		System.out.println("=="+wng.getPageNumber() );
		wng.setPageNumber( wng.getPageNumber()+2 );
		System.out.println("=="+wng.getPageNumber());
		
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
