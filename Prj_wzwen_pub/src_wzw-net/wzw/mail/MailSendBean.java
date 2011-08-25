/**
 * Copyright (C) 2005 WUZEWEN. All rights reserved.
 * WZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * <p>use simple mail translate protocal.
 * 		��java�澭�����������������javamail�����ʼ�����ν����ʼ�����η��ʶ���ļ��еȡ�������ɢ������ʷ�Ļظ����Ѿ���û������ĺ���֮�С�
 * ����֮ǰ������һ��java��Ŀ�����а�����WebMail���ܣ�����Ϊ��javaʵ�ֶ���javamail������һ��ʱ�䣬�����е��ջ񡣿�����̳�еľ����д˷�������⣬��˰��ҵ�һЩ������������ϣ���Դ����Щ������
 * ��ƪ��������javamailʵ�ַ����ʼ����ܣ������漰smtp��֤���ʼ��������ͣ���HTML�����ʼ��ȡ�
 * �����йض������ʵ�֣�����POP3�ʼ���IMAP�����ݣ����ں��������н��ܡ�
 * ���³�����Ҫ��javamail��JAF����j2ee.jar�����������������������Ұ�װJ2SDKEE��ֱ�ӿ���j2ee.jar��������ӵ�jbuilder��library�У���ϵͳClassPath��</p>
 * @author	WUZEWEN on 2005-05-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class MailSendBean {

	/**
	 * ��־�������
	 */
	protected static Logger log = Logger.getLogger( wzw.mail.MailSendBean.class);

	private MimeMessage mimeMsg; //MIME�ʼ�����
	private Session session; //�ʼ��Ự����
	private Properties props; //ϵͳ����
	//private boolean needAuth = false; //smtp�Ƿ���Ҫ��֤	
	private String username = ""; //smtp��֤�û���������
	private String password = "";
	
	private Multipart mp; //Multipart����,�ʼ�����,����,���������ݾ���ӵ����к�������MimeMessage����
	
	
	/**
	 *	Constructor function
	 */
	public MailSendBean() {
		this("smtp.163.com");//getConfig.mailHost);//���û��ָ���ʼ�������,�ʹ�getConfig���л�ȡ
	}
	
	/**
     * <p>Constructor with one param</p>
     * @author WUZEWEN
     * @param smtp �ʼ���������simple mail transplate protocal
     * @return none
     * @exception no exception
     */
	public MailSendBean(String smtp){
		setSmtpHost(smtp);
		createMimeMessage();
	}
	
	
	/**
     * <p>a setter for SmtpHost</p>
     * @author WUZEWEN
     * @param hostName name of mail service host
     * @return void
     * @exception no exception
     */
	public void setSmtpHost(String hostName) {
		System.out.println("����ϵͳ���ԣ�mail.smtp.host = "+hostName);
		
		if(props == null)props = System.getProperties(); //���ϵͳ���Զ���		
		props.put("mail.smtp.host",hostName); //����SMTP����
		
		// props.put("mail.smtp.starttls.enable","true");
	}

	
	/**
     * <p>1.����ʼ��Ự����; 2.����MIME�ʼ�����</p>
     * @author WUZEWEN
     * @param String a
     * @param String b
     * @return String c
     * @exception no exception
     */
	public boolean createMimeMessage(){
		try{
			System.out.println("׼����ȡ�ʼ��Ự����");
			session = Session.getDefaultInstance(props,null); //����ʼ��Ự����
		}
		catch(Exception e){
			System.err.println("��ȡ�ʼ��Ự����ʱ��������"+e);
			return false;
		}
	
		System.out.println("׼������MIME�ʼ�����");
		try{
			mimeMsg = new MimeMessage(session); //����MIME�ʼ�����
			mp = new MimeMultipart();
			return true;
		}
		catch(Exception e){
			System.err.println("����MIME�ʼ�����ʧ�ܣ�"+e);
			return false;
		}
	}


	/**
     * <p>set the smtp server is need auth.</p>
     * @author WUZEWEN
     * @param boolean need
     * @return String void
     * @exception no exception
     */
	public void setNeedAuth(boolean need) {
		System.out.println("����smtp�����֤��mail.smtp.auth = "+need);
		if(props == null)props = System.getProperties();
		
		if(need){
			props.put("mail.smtp.auth","true");
		}else{
			props.put("mail.smtp.auth","false");
		}
	}


	/**
     * <p>�����û��Ϳ���</p>
     * @author WUZEWEN
     * @param name �����˵�¼�����û�
     * @param pass �����˵�¼�������
     * @return void
     * @exception no exception
     */
	public void setUserPass(String name,String pass) {
		username = name;
		password = pass;
	}


	/**
     * <p>����MIME�ʼ����������</p>
     * @author WUZEWEN
     * @param String mailSubject
     * @return boolean
     * @exception no exception
     */
	public boolean setSubject(String mailSubject) {
		System.out.println("�����ʼ����⣡");
		try{
			mimeMsg.setSubject(mailSubject);
			return true;
		}
		catch(Exception e) {
			System.err.println("�����ʼ����ⷢ������");
			return false;		
		}
	}


	/**
     * <p>����MIME�ʼ����������</p>
     * @author WUZEWEN
     * @param String mailBody
     * @return String c
     * @exception no exception
     */
	public boolean setBody(String mailBody) {
		try{
			 
			BodyPart bp = new MimeBodyPart();
			bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=gb2312>"+mailBody,"text/html;charset=GB2312");
			mp.addBodyPart(bp);
		
			return true;
		}
			catch(Exception e){
			System.err.println("�����ʼ�����ʱ��������"+e);
			return false;
		}
	}

	
	/**
     * <p>����ʼ��ĸ���</p>
     * @author WUZEWEN
     * @param String filename
     * @return boolean c
     * @exception no exception
     */
	public boolean addFileAffix(String filename) {
	
		System.out.println("�����ʼ�������"+filename);
		
		try{
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileds.getName());
			
			mp.addBodyPart(bp);
			return true;
		}catch(Exception e){
			System.err.println("�����ʼ�������"+filename+"��������"+e);
			return false;
		}
	}


	/**
     * <p>���÷����ˣ�</p>
     * @author WUZEWEN
     * @param String from
     * @return String c
     * @exception no exception
     */
	public boolean setFrom(String from) {
		System.out.println("���÷����ˣ�");
		try{
			mimeMsg.setFrom(new InternetAddress(from)); //���÷�����
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
	}


	/**
     * <p>������������Ϣ</p>
     * @author WUZEWEN
     * @param to ���������䣬���������,�ŷָ�
     * @return boolean ���óɹ�����true������ʧ�ܷ���false��
     * @exception no exception
     */
	public boolean setTo(String to){
		if(to == null)return false;
		
		try{
			mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
		
	}

	/**
     * <p>���ó�����</p>
     * @author WUZEWEN
     * @param copyto �����ˣ��������ʹ��,�ŷָ�
     * @return boolean ���óɹ�����true������ʧ�ܷ���false��
     * @exception no exception
     */
	public boolean setCopyTo(String copyto){
		if(copyto == null)return false;
		try{
			mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyto));
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
	}

	
	/**
     * <p>�ʼ��ķ���</p>
     * @author WUZEWEN
     * @return boolean ���ͳɹ�����true������ʧ�ܷ���false��
	 * @throws Exception 
     * @exception exception
     */
	public boolean sendout() throws Exception {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			log.info("���ڷ����ʼ�....");

			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,
					password);
			transport.sendMessage(mimeMsg, mimeMsg
					.getRecipients(Message.RecipientType.TO));
			// transport.send(mimeMsg);
			// transport.

			log.info("�����ʼ��ɹ���");
			transport.close();

			return true;
		} catch (Exception e) {
			log.error("�ʼ�����ʧ�ܣ�" + e.getMessage(), e );
			//log.info("debug", e );
			/// e.pri ntStackTrace() ;
			throw e;
		}
	}

	
	/**  
	* <p>���ô��������</p>
	* @author wuzewen on 2005-07-29
	* @param proxy ʹ�õ��ʼ����ʹ����ip
	* @param proxyPort ʹ�õ��ʼ����ʹ���Ķ˿�
	*/  
	public void setProxyServer(String type, String proxy, String proxyPort) {  
		//���ô��������  
		//    System.getProperties().put("proxySet", "true");  
		//    System.getProperties().put("proxyHost", proxy);  
		//    System.getProperties().put("proxyPort", proxyPort);  
		if(type.equals("httpProxy")){
			//http proxy
			System.getProperties().put("httpProxySet",new Boolean(true) );
			System.getProperties().put("httpProxyHost",proxy);
			System.getProperties().put("httpProxyPort",proxyPort);	
		}else if(type.equals("socktProxy")){
			//sockt proxy   
			System.out.println ("proxy="+proxy+":"+proxyPort); 
			System.getProperties().put("socksProxySet",new Boolean(true) );
			System.getProperties().put("socksProxyHost",proxy);
			System.getProperties().put("socksProxyPort",proxyPort);
		}else {
			//sockt proxy   
			System.out.println ("proxy="+proxy+":"+proxyPort); 
			System.getProperties().put("socksProxySet",new Boolean(true) );
			System.getProperties().put("socksProxyHost",proxy);
			System.getProperties().put("socksProxyPort",proxyPort);
		}
	
	}  

	/**
	 * <p>�����ʼ���Main������</p>
	 * @author WUZEWEN on 2005-05-25
	 * @param args ���ò���
	 * @return void
	 * @throws Exception 
	 * @throws no exception
	 */
	public static void main(String[] args) throws Exception {
	
//		String mailbody = "<meta http-equiv=Content-Type content=text/html; charset=gb2312>"+
//		"<div align=center><a href=http://www.csdn.net> csdn </a></div>";
		
		//��ȡ������Ϣ
		Properties props=new Properties();
		try{
			props.load(new java.io.FileInputStream("properties_2005_javamail.properties")) ;
		}catch(FileNotFoundException ex){
			System.out.println ("û���ҵ������ļ���");
			System.exit(0);
		}catch(IOException ex){
			System.out.println ("û���ҵ������ļ���");
			return;
		}
		//�ʼ���Ϣ����
		String prefix=props.getProperty("jmail.rootCategory");	//�����
		if(prefix==null||prefix.trim().equals("")){
			prefix="A1";
		}
		prefix="jmail.conf."+prefix+".";
		System.out.println ("category:"+prefix);
		String subject = props.getProperty(prefix+"subject");	//����
		String body    = props.getProperty(prefix+"body");		//�ı�����
		String from    = props.getProperty(prefix+"from");		//��������
		String user    = props.getProperty(prefix+"user");		//���������û�
		String pass    = props.getProperty(prefix+"pass");		//�����������
		String to      = props.getProperty(prefix+"to");		//�������伯�ϣ���,�ŷָ�
		String copyTo  = props.getProperty(prefix+"copyTo");	//�������伯�ϣ���,�ŷָ�
		String fileAffix=props.getProperty(prefix+"fileAffix");//���������ʹ��,�ŷָ�
		
		if(subject==null) subject="";
		if(body==null) body="";
		if(from==null) from="";
		if(user==null||user.trim().equals("")){
			user=from.substring(0,from.indexOf("@"));
		}
		if(pass==null||pass.trim().equals("")){
			pass="tom8798155";
		}
		System.out.println ("pass="+pass);
		if(to==null) to="";
		if(copyTo==null) copyTo="";
		if(fileAffix==null) fileAffix="";
		java.util.StringTokenizer st = new StringTokenizer(fileAffix,",");
		
		//����������Ϣ
		String smtp     = props.getProperty(prefix+"smtp");		//�ʼ���������smtp
		String proxyType= props.getProperty(prefix+"proxyType");		//��������proxyType
		String proxyIP  = props.getProperty(prefix+"proxyIP");		//��������proxyIP
		String proxyPort= props.getProperty(prefix+"proxyPort");		//��������proxyPort
	    
		//���ô�������� 
		MailSendBean themail = new MailSendBean(smtp);
		if(proxyType!=null&&!proxyType.trim().equals("")){
			themail.setProxyServer(proxyType,proxyIP,proxyPort) ;			
		}
		//themail.props = System.getProperties() ;
		//themail.setProxyServer("192.168.0.10","1080") ;
		//themail.setProxyServer("192.168.0.10","8000") ;
		themail.setNeedAuth(true);
		//themail.		
		
		if(themail.setSubject( subject ) == false) return;
		if(themail.setBody( body ) == false) return;
		if(themail.setFrom( from ) == false) return;
		if(themail.setTo( to ) == false) return;
		if(!copyTo.equals("")){
			if(themail.setCopyTo(copyTo) == false) return;
		}
		while(st.hasMoreElements()){
			String tmep = st.nextElement().toString() ;
			System.out.println (tmep);
			if(themail.addFileAffix(tmep) == false){
				System.out.println("��Ӹ���ʧ�ܣ�");
				return;
			}
		}
		//if(themail.addFileAffix("c:/myattachment.txt") == false) return;
		themail.setUserPass(user,pass);			
		if(themail.sendout() == false){
			return; 
		}
		
	}
	
}
