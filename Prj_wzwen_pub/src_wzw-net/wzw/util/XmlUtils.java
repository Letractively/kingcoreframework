/**
 * Copyright (C) 2002-2005 ChangSha WUZEWEN. All rights reserved.
 * WZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * <BLOCKQUOTE>��XML�ļ��������ࡣ</BLOCKQUOTE>
 * @author	zewen.wu on 2005.03.19
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK1.4
 */

public class XmlUtils {
    /**
     * log4j��־����
     */
    public static Logger log = Logger.getLogger( XmlUtils.class);


	/**
     * ��xml�ļ���ȡ���ֵ
	 * @author WUZEWEN
     * @version V1.0.0(2005-07-24)
	 * @param  InputStream is--�ļ�����������
	 * @param  Vector vec_str--��Ҫȡ��xml�Ľڵ�·����
     * @return Vector����,ȡ���Ľڵ��ֵ�ļ�
     * @throws no Exception throw
	 */
	public static Vector<String> getElementValues(java.io.InputStream inStream,Vector<String> vec_str){

		Vector<String> result = new Vector<String>() ;
		try{
			if(vec_str==null) return null ;
			if(inStream==null ) return null ;

			StringTokenizer st = null ;
			String params = null ;
			String noteName="",noteAttr="",attrValue="";

			//create a new Document
			SAXBuilder builder = new SAXBuilder(false);
			//�õ�Document
			Document doc = builder.build( inStream );
			//�õ����ڵ�LIT:StuInfo
			Element element = null ;

			Iterator<String> it = vec_str.iterator() ;
			List<?> list =null;
			while(it.hasNext()){
				element = doc.getRootElement();
				params = it.next() ;  //(String)
				//System.out.println("params="+params) ;
				st = new StringTokenizer(params,"@") ;
				//ȡ����Ҫ�Ľڵ�
				while( st.hasMoreTokens()) {
					//System.out.println( element.getName() ) ;
					noteName= st.nextToken();
					if(noteName.indexOf("[")>0){
						//System.out.println (noteName);
						noteAttr =noteName.substring(noteName.indexOf("[")+1,noteName.indexOf("="));
						attrValue=noteName.substring(noteName.indexOf("=")+1,noteName.indexOf("]"));
						noteName =noteName.substring(0,noteName.indexOf("["));
						list = element.getChildren( noteName ) ;
						//System.out.println (noteName+" "+noteAttr+" "+attrValue);
						for(int i=0;i<list.size();i++){
							element=(Element)list.get(i);
							if(element.getAttribute(noteAttr)!=null
								&&element.getAttribute(noteAttr).getValue().equals(attrValue))
								break;
						}
					}else{
						element = element.getChild(noteName) ;	//ֻ��һ��child,����ɲ����Եõ���һ����
					}
					//element = element.getChild( st.nextToken() ) ;
					//System.out.println( element.getName() ) ;
				}
				if( element.getAttributeValue("value") == null )
					result.add( element.getText() ) ;
				else
					result.add( element.getAttributeValue("value") ) ;

				//System.out.println( element.getAttributeValue("value") ) ;
			}


			//List menu01s = elmtStuInfo.getChildren("menu");
			//p( "next menu is empty! " +menu01s.size()) ;

			//�޸�bigmouse��CAD����
			//for (int i = 0; i < menu01s.size(); i++)
			//{

		}catch(JDOMException ex){
			log.fatal("faile to parse xml file!"+ex.toString() ) ;
		}catch(Exception ex){
			log.fatal("faile to parse xml file!"+ex.toString() ) ;
		}

		return result ;

	}

	/**
     * ��xml�ļ���ȡ���ֵ
	 * @author WUZEWEN
     * @version V1.0.0(2005-03-10)
	 * @param  String fileURI--�ļ�����(������ϸ·��)
	 * @param  Vector vec_str--��Ҫȡ��xml�Ľڵ�·����
     * @return Vector����,ȡ���Ľڵ��ֵ�ļ�
     * @throws no Exception throw
	 */
	public static Vector<String> getElementValues(String fileURI,Vector<String> vec_str)
				throws java.io.FileNotFoundException {

		return getElementValues( new java.io.FileInputStream( fileURI ), vec_str) ;

	}

    /**
     * ��xml�ļ���ȡһ��ֵ
     * @author WUZEWEN
     * @version V1.0.0(2005-03-10)
     * @param  String fileURI--�ļ�����(������ϸ·��)
     * @param  String parms--��Ҫȡ��xml�Ľڵ�·��
     * @return String����,ȡ���Ľڵ��ֵ
     * @throws no Exception throw
     */
	public static String getElementValue(String fileURI,String parms)
				throws java.io.FileNotFoundException {
		Vector<String> vec = new Vector<String>() ;
		vec.add(parms) ;
		return getElementValues(fileURI,vec).toString() ;

	}

}
