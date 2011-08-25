/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import wzw.image.ImageScaleThread;
import wzw.util.StringUtils;


/**
 * 
 * <p>�ļ����ļ��С�����ϵͳ�����ƵȲ��������ࡣ
 * 		��������ࣺorg.apache.commens.io.*
 * 
 * </p>
 * @author Zeven on 2005-11-27
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class FileUtils {

	/**
	 * ����ļ��Ƿ���ڣ��൱��Linux�е� touch ������
	 * @deprecated �� apache �� common-io.jar ������FileUtils.class����touch������ʹ���Ǹ���������
	 * @param fileName
	 * @throws IOException
	 */
	public static void checkFileExists(String fileName) throws IOException{
    	File file = new File(fileName);
    	if(!file.exists()){
    		file.mkdirs();
    	}
		
	}
	
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		// test ImageScaleThread.class
		if( true ){
			
			ImageScaleThread t1 = new ImageScaleThread("E:/My Documents_200608/temp/tempBmp.jpg",
					"E:/My Documents_200608/temp/tempBmp_100.jpg",
					80,80,
					true );

			ImageScaleThread t2 = new ImageScaleThread("E:/My Documents_200608/temp/tempBmp.jpg",
					"E:/My Documents_200608/temp/tempBmp_100.jpg",
					60,60,
					true );
			t1.thread.join();
			t2.thread.join();
			System.out.println("-------------------- end ... ");
			return ;
			
		}
	}

	public static String convert2LinuxPath(String url) {
		return StringUtils.replace(url, "\\", "/");
	}
}
