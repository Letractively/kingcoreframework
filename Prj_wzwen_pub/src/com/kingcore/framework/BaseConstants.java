/**
 * Copyright (C) 2002-2006 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>ϵͳ��������ϵͳ���õġ�
 *  ˵��������λ�� "**xx/base/xx**" Ŀ¼�¡���������Ϊ Base***.class ����(�ӿ�)��
 * 		���������࣬���ڱ��̳У���Ҫֱ��ʹ�ø���ķ������Ա��
 * 	/p>
 * @author	WUZEWEN on 2006-05-17
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class BaseConstants implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BaseConstants() {
		super();
	} 
	
	static{
	} 

	/******************************************************************
						 Constants for System Level
	******************************************************************/
	//��Ҫ·����Ϣ
	public static String System_App_Home = "";	  
	public static String System_App_ConfigPath = "";	

	//ϵͳ����ģʽ
	//key - value - value items - default
	public static final String RunMode_Dev = "dev";
	public static final String RunMode_Test = "test";
	public static final String RunMode_Stage = "stage";
	public static final String RunMode_Prod = "prod";

	public static String System_RunMode_Default = RunMode_Dev;
	public static String System_RunMode_Val = System_RunMode_Default;
	
	//�û�������ʱ�������û�����
	public static int User_Input_Delay = 500;  //millisecond
	
	
	/******************************************************************
	                      Constants for Desktop System
	 ******************************************************************/
	//Vista��Windows 7Сͼ��ģʽ����30���أ�Windows 7��ͼ��ģʽ����40���أ�������������֮һ�� 
	public static final int System_TaskBar_Height = 30;	
	public static int Min_TableColumn_Width = 55;
	public final static String MainFrame_Size_Width   ="MainFrame_Size_Width";
	public final static String MainFrame_Size_Height   ="MainFrame_Size_Height";
	
	/** ϵͳ��� */
	//key - value - value items - default
	public static String LookAndFeel_Current="Current";
	public static String LookAndFeel_Windows="Windows";
	public static String LookAndFeel_Metal  ="Metal";
	public static String LookAndFeel_Motif  ="Motif";

	public final static String System_LookAndFeel_Key="system.lookAndFeel";
	public final static String System_LookAndFeel_Default=LookAndFeel_Metal;
	
	/** ϵͳ���� */
	//key - value - value items - default
	public final static String System_Language_CN   ="Chinese.CN";
	public final static String System_Language_TW   ="Chinese.TW";
	public final static String System_Language_EN   ="English";
	public final static String System_Language_JP   ="Japanese";

	public final static String System_Language_Key   ="system.language";
	public final static String System_Language_Default = System_Language_CN;	

	//���ʻ�֮-���Թ��ʻ���Դ����
	public final static String I18n_Language_Name = "Language";
}

