

package com.wzwen.example ;

import java.sql.Connection;
import java.util.Map;

import com.kingcore.framework.base.handler.AbstractHandler;

/**
 * <p>ҵ���߼��������������Կ����ࡣ</p>
 * @author Zeven on 2007-8-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TestHandler extends AbstractHandler {

	/* (non-Javadoc)
	 * @see com.kingcore.framework.base.handler.Handler#getInstance()
	 */

    /**
     * ���������ģʽ�滻ȫ��̬����������spring ����
     */
	private static TestHandler instance;
	

	public static TestHandler getInstance(){  
		if (instance == null) {
			instance = new TestHandler();
		}
		return instance;
	}
	/**
	 * ��������Ҹ���Ĭ��ֵ�� �ᱻspring ���ã��������ι���!!!!!!!!!!!!
	 *
	 */
	private TestHandler() {
		//...
		
		instance = this ;
	}
	
	/**
	 * <p>����һ���������ע������ӣ�
	 * 		չʾ���ʹ��ҵ��㴦��ҵ���߼�����������</P>
	 * @param map
	 * @throws Exceptioin
	 */
	public void shopRegister(Map map) throws Exception {
		
		// JDBC Connection �������ڿ������������ԡ�
		Connection conn = null;
		
		try {
			// ��ȡ����
			conn = this.getConnection();
			
			// ���� DAO����ע�������Ա��������
			//UserDealDAO udd = new UserDealDAO();
			//ShopDealDAO sdd = new ShopDealDAO();
			//udd.setConnection(conn);
			//sdd.setConnection(conn);
			
			// ����DAO�������ݴ�����
			// sdd.register(...);
			// udd.updateInfor(...);
			// ...
			
			// ����ɹ��ύ����
			conn.commit();
		} catch (Exception e) {
			// �����ɹ��ع�����
			conn.rollback();
			
		} finally {
			
			// ��ȡ���ӵĵط�һ��Ҫ�ر�����
			if(conn!=null){
				conn.close();
			}
			
		}
	}
	
}
