/*
 * Created on 2003-6-20
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.kingcore.framework.util ;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author
 */
public class SqlFactory implements Serializable {

	public static final Log log = LogFactory.getLog(SqlFactory.class);
	/**
	 * ��ǰ���ʵ��
	 */
	public static SqlFactory instance = new SqlFactory();
	/**
	 * ����Ѿ�ʵ�����Ķ���
	 */
	public HashMap classInstances = new HashMap();
	/**
	 * ���ݿ����͵�ȡֵ
	 */
	public final int ORA_TYPE = 1;
	public final int DB2_TYPE = 2;
	public final int SYB_TYPE = 3;
	public final int SQL_TYPE = 4;
	public final int MYSQL_TYPE = 5;
	public final int INFORMIX_TYPE = 6;
	/**
	 * ���ݿ����͵�����
	 */
	public final String ORA_NAME = "ORA";
	public final String DB2_NAME = "DB2";
	public final String SYB_NAME = "SYB";
	public final String SQL_NAME = "SQL";
	public final String MYSQL_NAME = "MYSQL";
	public final String INFORMIX_NAME = "INFORMIX";
	/**
	 * ������ݿ�������
	 */
	public HashMap databaseName;
	//--------------------------------------------
	//---- ���ݿ��Ӧ�����ʵ������ --------------
	//- ���磺strOra = "Ora",��������ĺ�׺��ΪTest,
	//- ��ô��ʵ��������OraTest ------------------
	//--------------------------------------------
	public String strOra = "Ora";
	public String strDb2 = "Db2";
	public String strSyb = "Syb";
	public String strSql = "Sql";
	public String strMySql = "MySql";
	public String strInformix = "Informix";
	//-----------------------------------------
	//----- ������Щǰ׺������ ----------------
	//-----------------------------------------

	/**
	 * 
	 * Create on 2003-6-20
	 * @param strOra
	 */
	public void setStrOra(String strOra) {
		this.strOra = strOra;
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param strDb2
	 */
	public void setStrDb2(String strDb2) {
		this.strDb2 = strDb2;
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param strSyb
	 */
	public void setStrSyb(String strSyb) {
		this.strSyb = strSyb;
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param strMySql
	 */
	public void setStrMySql(String strMySql) {
		this.strMySql = strMySql;
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param strInformix
	 */
	public void setStrInformix(String strInformix) {
		this.strInformix = strInformix;
	}

	//------------------------------------------- Method
	/**
	 * ˽�й��캯��
	 *
	 */
	private SqlFactory() {
		databaseName = new HashMap();
		databaseName.put(DB2_NAME, strDb2);
		databaseName.put(INFORMIX_NAME, strInformix);
		databaseName.put(MYSQL_NAME, strMySql);
		databaseName.put(ORA_NAME, strOra);
		databaseName.put(SQL_NAME, strSql);
		databaseName.put(SYB_NAME, strSyb);
	}
	/**
	 * �õ���ǰ���Ψһʵ��
	 * @author WUZEWEN
	 */
	public static SqlFactory getInstance() {
		return instance;
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param package
	 * @param databasename
	 * @param suffix
	 * @return
	 * @throws Throwable
	 */
	public DatabaseInterface getClassInstance(
		String pack,
		String databasename,
		String suffix)
		throws Throwable {
		if (!databaseName.containsKey(databasename)) {
			throw new Exception("��֧��" + databasename + "�����ݿ�����");
		}
		String prefix = (String) databaseName.get(databasename);
		StringBuffer name = new StringBuffer();
		name.append(pack).append(".").append(prefix).append(suffix);
		if (classInstances.containsKey(name.toString())) {
			return (DatabaseInterface) classInstances.get(name.toString());
		}
		return newClassInstance(name.toString());
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param pack
	 * @param databasetype
	 * @param suffix
	 * @return
	 * @throws Throwable
	 */
	public DatabaseInterface getClassInstance(
		String pack,
		int databasetype,
		String suffix)
		throws Throwable {
		String prefix = "";
		switch (databasetype) {
			case ORA_TYPE :
				prefix = strOra;
				break;
			case DB2_TYPE :
				prefix = strDb2;
				break;
			case INFORMIX_TYPE :
				prefix = strInformix;
				break;
			case SQL_TYPE :
				prefix = strSql;
				break;
			case SYB_TYPE :
				prefix = strSyb;
				break;
			default :
				throw new Exception("��֧�����ݿ����ͣ�");
		}
		StringBuffer name = new StringBuffer();
		name.append(pack).append(".").append(prefix).append(suffix);
		if (classInstances.containsKey(name.toString())) {
			return (DatabaseInterface) classInstances.get(name.toString());
		}
		return newClassInstance(name.toString());
	}
	/**
	 * 
	 * Create on 2003-6-20
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public DatabaseInterface newClassInstance(String name) throws Exception {
		Object o = null;
		try {
			Class clazz = Class.forName(name.toString());
			o = clazz.newInstance();
			if (o instanceof DatabaseInterface) {
				classInstances.put(name, o);
			} else {
				throw new Exception(
					"ͨ������" + name + "�õ���ʵ��û��ʵ��DatabaseInterface�ӿ�");
			}
		} catch (ClassNotFoundException e) {
			throw e;
		}
		return (DatabaseInterface) o;
	}
}
