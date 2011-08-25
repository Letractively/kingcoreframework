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

package com.kingcore.framework.base.service;

import java.util.HashMap;

/**
 * <p>����һ��ҵ����Ļ��ࡣ
 * 	   * ҵ��������ϵͳ�еĹ��ܶ�λ�ǣ�
 * 			�������Կ�����(controller ����������ϵͳ���¼�������)�����ݣ�
 * 			��Ҫ����������ҵ���߼��������������һ���Կ��ƣ�...
 * 				�������Ͱ�����
 * 					> �������ݷ��ʶ���(DAO)�ķ���( JDBC Connection ����)��
 * 					> �ļ���д( File R&W ����)��
 * 					> Զ�̷������( Remoting Service Calling ����)��
 * 					> ...
 * 
 * 		* Ϊ��������ܣ�������һ��Ӧ����ֻ��Ҫ����һ��ʵ�����ɣ�����Ҫע�����¼��㣺
 *			> ���в�Ҫ�г�Ա����(instance��̬��Ա����)�������̳߳�ͻ��
 *			> ���õ������ģʽ����Spring��ֻ����һ�εķ�������ֻ֤��һ��ʵ���� 
 *     </p>
 * @author Zeven on 2006-8-23
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface Service {

}
