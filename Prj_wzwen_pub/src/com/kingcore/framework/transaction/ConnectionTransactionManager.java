package com.kingcore.framework.transaction;

/*
����Ҫ��һ����Ҫ��ʵ���������Ǳ�����ͬһ�����ݿ�����ִ����Щ��䣬���ղ�������ͳһ���ύ�ͻع��� 
���ǿ����������� 
insert1��insert2Ϊ��ͬDAO�ķ��� 
��ϸ�۲죬���ǵ�insert1��insert2��û�и�������Ӻ͹ر����ӡ����Ǽ�ӵĵ���TransactionHelper.executeNonQuery(sql); 
����ʹ����ִ�е����з�������ʹ��ͬһ�����ӽ������ݿ������ 

  ��ʵ�������ֻ������ߴ��Ҫʵ������ʽ�����һ�������ݣ��������ֻ��ʵ�ּ򵥵ĵ�����ģ�ͣ�Ҫʵ�ָ����ӵ����񴫲�ģ����Ƕ�׵ȣ�����Ҫ����ʹ�ø���ļ�������AOP�ȵȡ���д�����ϣ���Դ������������

Wzw�� ʹ��
tm.begin() ��������ʼ���ñ����̵߳�conn�����ҿ��ܲ�ֻһ������Ҫʹ��stack�������е�conn�� 
Stack<Map<String,Connection>();-->����Ƕ��+��DataSource��
tm.commit() �������ύ��Ӧlist�������е�conn��
tm.rollback() �������ع���Ӧlist�������е�conn��
*/   

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.kingcore.framework.context.ApplicationContext;
   
public final class ConnectionTransactionManager implements TransactionManager{    
       
	private static Logger log = Logger.getLogger(ConnectionTransactionManager.class);
   //ʹ��ThreadLocal���е�ǰ�̵߳����ݿ�����      Map:֧�ֶ�����Դ; Stack:֧������Ƕ��
   private final static ThreadLocal<Stack<Map<String,Connection>>> connection_holder 
   				= new ThreadLocal<Stack<Map<String,Connection>>>();    
   //private final static ThreadLocal<Connection> connection_holder = new ThreadLocal<Connection>();    
       
//   //�������ã�����connection.properties    
//   private final static Properties connectionProp = new Properties();    
//       
//   static{         
//       //���������ļ�    
//       InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("connection.properties");    
//       try {    
//               
//           connectionProp.load(is);    
//           is.close();    
//           //������������    
//           Class.forName(connectionProp.getProperty("driverClassName"));    
//       } catch (IOException e) {    
//            throw new RuntimeException(e.getMessage(),e);    
//       }catch(ClassNotFoundException e){    
//           throw new RuntimeException("����δ�ҵ�",e);    
//       }    
//   }    
       
   //��ȡ��ǰ�߳��е����ݿ�����    
	public static Connection getCurrentConnection(TransactionType transType, String dataSourceName) {
		
		//intiTransaction(transType);  //ÿ�ο��Գ�ʼһ�Σ�������Ҫ�����Ķ���
		Map<String, Connection> connsMap = getLastConnectionsMap();
		if (connsMap == null) { //���û�п�������(tm.begin)���򷵻�null.
			return null;
		}

		Connection conn = connsMap.get(dataSourceName);
		if (conn == null &&   //�������Ҫ�������򴴽�
				(transType==TransactionType.NEW_TRANSACTION
					||transType==TransactionType.REQUIRED_TRANSACTION)) {
			try {
				conn = ApplicationContext.getInstance().getDataSourceManager()
								.getConnection(dataSourceName);
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				log.equals(e);
			}
			connsMap.put(dataSourceName, conn);
		}
		return conn;
	}
       
//   //ִ��SQL���    
//   public static int executeNonQuery(String sql) throws SQLException{    
//           
//       Connection conn = getCurrentConnection();    
//            
//       return conn.createStatement().executeUpdate(sql);    
//   
//   }    
       
	/**
	 * �������һ��connectionsMap.
	 * @return
	 */
   private static Map<String, Connection> popConnectionsMap(){
	   Map<String, Connection> connsMap = null;
		Stack<Map<String, Connection>> connsStack = connection_holder.get();    
		//�����û�������ջ�����ȴ�������¼
		if (connsStack==null) {
			return connsMap;
		}
		//�����û��connetion Map,Ҳ��Ҫ�ȴ�����ѹ���ջ
		connsMap = connsStack.pop(); //pop out
		if (connsMap==null) {
			return connsMap;
		}
		return connsMap;
   }

	/**
	 * ��ȡ���һ��connectionsMap�����ǲ�����.
	 * @return
	 */
   private static Map<String, Connection> getLastConnectionsMap(){
	   Map<String, Connection> connsMap = null;
		Stack<Map<String, Connection>> connsStack = connection_holder.get();    
		//�����û�������ջ�����ȴ�������¼
		if (connsStack==null) {
			return connsMap;
		}
		//�����û��connetion Map,Ҳ��Ҫ�ȴ�����ѹ���ջ
		connsMap = connsStack.lastElement(); //just get, not pop out
		if (connsMap==null) {
			return connsMap;
		}
		return connsMap;
   }
   
   //�ύ����    
   public void commit() throws TransactionException {    

	   Map<String, Connection> connsMap = popConnectionsMap();
	   if (connsMap==null) {
		   log.error("commit fail for the previous error:"+
					 "no transaction found! ");
		   return;
	   }
		//�ύ����conn
		Iterator<Entry<String, Connection>> it = connsMap.entrySet().iterator() ;
		try {
			while(it.hasNext() ){
				Entry<String, Connection> mapen = it.next() ;
					mapen.getValue().commit();
			}
		} catch (SQLException e) {
			log.error(e);
			throw new TransactionException(e.getMessage());
		}
		log.info("commit successfully!");
   }    
       
       
   //�ع�����    
   public void rollback() throws TransactionException {

	   Map<String, Connection> connsMap = popConnectionsMap();
	   if (connsMap==null) {
		   log.error("rollback fail for the previous error:"+
					 "no transaction found! ");
		   return;
	   }
		//�ύ����conn
		Iterator<Entry<String, Connection>> it = connsMap.entrySet().iterator() ;
		try {
			while(it.hasNext() ){
				Entry<String, Connection> mapen = it.next() ;
					mapen.getValue().rollback();
			}
		} catch (SQLException e) {
			log.error(e);
			throw new TransactionException(e.getMessage());
		}
		log.info("rollback successfully!");  
   }    
       
//   //����һ�����Զ�Commit�����ݿ�����    
//   private static Connection createNotAutoCommitConnection() {    
//       try {    
//               
////           Connection conn = DriverManager.getConnection(connectionProp.getProperty("url")+";databaseName="+ connectionProp.getProperty("databaseName")    
////                   ,connectionProp.getProperty("username")    
////                   ,connectionProp.getProperty("password"));  
//           Connection conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();
//           conn.setAutoCommit(false);    
//           return conn;    
//       } catch (SQLException e) {    
//            throw new RuntimeException(e.getMessage(),e);    
//       }    
//   }

	public void begin() throws TransactionException { 
		begin(null);
	}
	
	public void begin(TransactionType transType) throws TransactionException { 
		intiTransaction(transType); //�������ʼ
	}
	
	private static void intiTransaction(TransactionType transType) throws TransactionException { 
		if (transType == TransactionType.MAYBE_TRANSACTION) {
			//getCurrentConnection();  nothing to do
			
		} else if (transType == TransactionType.REQUIRED_TRANSACTION) {
			Stack<Map<String, Connection>> connsStack = connection_holder.get();    
			//�����û���߳������ջ�����ȴ�������¼
			if (connsStack==null) {
				connsStack = new Stack<Map<String, Connection>>();
				connection_holder.set(connsStack);
			}
			//�����û���򴴽�������Դ����:connetion Map,Ҳ��Ҫ�ȴ�����ѹ���ջ���оͲ��ù�
			Map<String, Connection> connsMap = connsStack.lastElement();
			if (connsMap==null || connsMap.size()<1) {  //���һ����û�У��ʹ���
				connsMap = new HashMap<String, Connection>();
				connsStack.push(connsMap);
			}
			// Connection����Map����Ҫ��ʱ���ٴ���Ҳ���ǻ�ȡconn��ʱ��
			
		} else if (transType == TransactionType.NEW_TRANSACTION) {
			Stack<Map<String, Connection>> connsStack = connection_holder.get();    
			//�����û���߳������ջ�����ȴ�������¼
			if (connsStack==null) {
				connsStack = new Stack<Map<String, Connection>>();
				connection_holder.set(connsStack);
			}
			//�����µĶ�����Դ����:connetion Map,��ѹ���ջ
			Map<String, Connection> connsMap = new HashMap<String, Connection>();
			connsStack.push(connsMap);
			
		}
	}
    
}
