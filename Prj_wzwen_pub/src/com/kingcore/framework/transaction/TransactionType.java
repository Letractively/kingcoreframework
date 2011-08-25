/**
 * Copyright (C) 2002-2011 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.transaction;

/**
 * <p>java���ļ���˵��...</p>
 * @author Zeven on 2011-8-9
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public enum TransactionType {

	NEW_TRANSACTION,      //���������񣬸�������������
	REQUIRED_TRANSACTION,      //�����������û�оͿ���������
	MAYBE_TRANSACTION,      //�����������û�оͲ���������
	NO_TRANSACTION      //����������Ҳ����������������
	//WIDE_TRANSACTION      //
}
