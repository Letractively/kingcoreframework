/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.gif4j.GifEncoder;

/**
 * <p>
 * 	����ͼ�࣬ ��java���ܽ�jpgͼƬ�ļ������еȱȻ�ǵȱȵĴ�Сת���� 
 *  ����ʹ�÷������ȹ���ʵ�����ٵ���createImage������
 * 		�ܴ�����͸���㱳����gifͼƬ����������ͼƬ�������ߣ���Щģ����
 * 		2009-07-22 wzw : ����ͼƬ����ʱ�Ƿ���Ҫ���׵Ĺ���
 * 
 * </p>
 * 
 * @author WUZEWEN on 2005-09-15
 * @version 1.0
 * @see Object#equals(java.lang.Object)
 * @see Object#hashCode()
 * @see HashMap
 * @since JDK5
 */

public class ImageScaleLoss { 
 


	/**
	 * ��־�������
	 */
	private final static Logger log = Logger.getLogger(ImageScaleLoss.class);
 
	/**
	 * <p>
	 * ����ָ��ͼƬ����ָ����С������ͼƬ����jdk1.4���棬֧��gifͼƬ����jdk5���棬֧��gif,bmp,png��ͼƬ��ʽ��
	 * </p>
	 * 
	 * @param inputDir
	 *            �ο�ͼƬ·����eg: "D:/temp/", "/usr/temp/"
	 * @param outputDir
	 *            ���ͼƬ·����eg: "D:/temp/", "/usr/temp/"
	 * @param inputFileName
	 *            �ο�ͼƬ����
	 * @param outputFileName
	 *            ���ͼƬ����
	 * @param w
	 *            ��ͼƬ�������ֵ
	 * @param h
	 *            ��ͼƬ�߶�����ֵ
	 * @param gp
	 *            �Ƿ���Ҫ�ȱ����ţ�Ĭ��true
	 * @return
	 * @throws Exception
	 */
	public boolean createImage(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int new_w, int new_h,
			boolean gp) throws IOException {

		// inputDir = org.
		// System.out.println( inputDir + "\n" + outputDir + "\n" +
		// inputFileName + "\n" + outputFileName);
		return createImage(inputDir + (inputDir.endsWith("/")?"":File.separator) + inputFileName,
				           outputDir+ (outputDir.endsWith("/")?"":File.separator)  + outputFileName, 
				           new_w, new_h, gp);
	}

	public boolean createImage(String inputFile, String outputFile, int new_w,
			int new_h, boolean gp) throws IOException {
		return createImage(inputFile, outputFile, new_w,
				new_h, gp, true);
	}
	
	/**
	 * 
	 * <p>
	 * ����ָ��ͼƬ����ָ����С������ͼƬ����jdk1.4���棬֧��gifͼƬ����jdk5���棬֧��gif,bmp,png��ͼƬ��ʽ��
	 * </p>
	 * 
	 * @param inputFile
	 *            �ο�ͼƬ·�������ƣ�eg: "D:/temp/abc.jpg", "/usr/temp/abc.gif"
	 * @param outputFile
	 *            ���ͼƬ·�������ƣ�eg: "D:/temp/efg.jpg", "/usr/temp/efg.gif"
	 * @param w
	 *            ��ͼƬ�������ֵ
	 * @param h
	 *            ��ͼƬ�߶�����ֵ
	 * @param gp
	 *            �Ƿ���Ҫ�ȱ����ţ�Ĭ��true
	 * @param needFilled
	 *            �Ƿ���Ҫ����
	 * @return �ɹ�����true��ʧ�ܷ���false�� 
	 * @throws IOException
	 */
	public boolean createImage(String inputFile, String outputFile, int new_w,
			int new_h, boolean gp, boolean needFilled) throws IOException {

		// for test
		// inputFile="/www/vangvsv/images/upload/shop_img/20080516/1210919370543.jpg";
		// outputFile="/www/a.jpg";
		// new_w = 100;
		// new_h = 100 ;
		// gp = true;

		log.debug("------------------------ ��ʼ����ͼƬ" + outputFile);

		// StopWatch sw = new StopWatch();
		// sw.start();

		int req_w = new_w;
		int req_h = new_h; // Ҫ�����ɵĸ߶ȡ����
		File _file = new File(inputFile);
		if (!_file.exists()) {
			throw new IOException("��Ҫ���Բ�����Դ�ļ�[" + _file.getAbsolutePath()
					+ "]�����ڣ�");
		}

		// sw.stop();
		// System.out.println( "----------- 1:"+ sw.getTime() );
		// sw.reset();
		// sw.start();

		// System.out.println( _file==null); //�����ļ�
		BufferedImage src = ImageIO.read(_file); // ����Image����
		// ��������jdk1.4�¶�bmpͼƬ��һ����������ȡ��������������jpg�Ĵ�ͼ��������jpg������ͼ�������ǲ���
		// bmp��ͼ����jpg������ͼ��
		// if(src==null) {
		// src=BitmapReader.load( inputDir, inputFileName );
		// }
		// sw.stop();
		// System.out.println( "----------- 2:"+ sw.getTime() );
		// sw.reset();
		// sw.start();
		if (src == null) {
			throw new IOException("����ͼƬ����ʧ�ܣ�����ͼƬ�ļ��Ƿ���Ϲ��");
		}

		int width = src.getWidth(null);
		int height = src.getHeight(null);

		boolean proportion = gp;
		//�����ж�ԭͼ�߿��Ƿ�����ź�ĸ߿�С,���С��
		//��ֱ�����ԭͼ
		if (new_w >= width && new_h >= height) {
			proportion = false;
			new_w = width;
			new_h = height;
		}
		// sw.stop();
		// System.out.println( "----------- 3:"+ sw.getTime() );
		// sw.reset();
		// sw.start();
		if (proportion == true) // �ж��Ƿ��ǵȱ�����.
		{
			// Ϊ�ȱ����ż��������ͼƬ��ȼ��߶�
			double rate1 = ((double) width) / (double) new_w;
			double rate2 = ((double) height) / (double) new_h;
			double rate = rate1 > rate2 ? rate1 : rate2;
			new_w = (int) (((double) width) / rate);
			new_h = (int) (((double) height) / rate);
		}

		// sw.stop();
		// System.out.println( "----------- 4:"+ sw.getTime() );

		// sw.reset();
		// sw.start();

		FileOutputStream out = new FileOutputStream(outputFile); // ������ļ���
		
		BufferedImage tag = null;
		if(needFilled){  // �Ƿ���Ҫ���հ�
			//BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);
			tag = new BufferedImage(req_w, req_h, BufferedImage.TYPE_4BYTE_ABGR);
			// BufferedImage tag = new
			// BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			// tag.getGraphics().drawImage(src,0,0,80,57,null); //������С���ͼ
			// sw.stop();
			// System.out.println( "----------- 5:"+ sw.getTime() );

			// sw.reset();
			// sw.start();

			//tag.getGraphics().drawImage(
			//		src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null); // ������С���ͼ
			tag.getGraphics().drawImage(
					src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 
							(req_w-new_w)/2, (req_h-new_h)/2, null); // ������С���ͼ
			
			tag.getGraphics().setColor(Color.white);
			if(req_w > new_w){
				tag.getGraphics().fillRect(0, 0,               (req_w-new_w)/2, req_h );
				tag.getGraphics().fillRect((req_w+new_w)/2, 0, (req_w-new_w)/2, req_h );
			}
			if(req_h > new_h){
				tag.getGraphics().fillRect(0, 0,                req_w, (req_h-new_h)/2 );
				tag.getGraphics().fillRect(0, (req_h+new_h)/2,  req_w, (req_h-new_h)/2 );
				
			}
			
		}else{	// ����Ҫ����
			tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);

			tag.getGraphics().drawImage(
						src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null); // ������С���ͼ
			
		}
		
		// sw.reset();
		// sw.start();
		// // ImageScale is = new ImageScale(); //Zeven
		// tag= this.imageZoomOut(tag, new_w, new_h);
		// sw.stop();
		// System.out.println( "----------- 7:"+ sw.getTime() );

		// sw.reset();
		// sw.start();
		// System.out.println(outputFile == null);

		// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		/// encoder.encode( bi, jep );
		GifEncoder.encode(tag, out);
		ImageIO.write(tag, "gif", out);
		// sw.stop();
		// System.out.println( "----------- 8:"+ sw.getTime() );

		// sw.reset();
		// sw.start();
		// encoder.encode(tag); //��JPEG����
		// System.out.print(width+"*"+height);
		out.close();
		// sw.stop();
		// System.out.println( "----------- 9:"+ sw.getTime() );

		log.debug("------------------------ ����ͼƬ���");
		_file = null;
		src = null;
		tag = null;
		out = null;
		// encoder = null;
		log.debug("------------------------ �����������");

		return true;

	}

	/**
	 * End: Use lanczos filter to replace the original algorithm for image
	 * scaling. lanczos improves quality of the scaled image modify by :blade
	 * 
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if(true){
			ImageScaleLoss is = new ImageScaleLoss();
			is.createImage("E:/Tmp_E/20090816104908633ZDTPQ1.jpg", "E:/Tmp_E/20090816104908633ZDTPQ1_100.jpg", 100, 100, true);
			is.createImage("E:/Tmp_E/20090816104908633ZDTPQ1.jpg", "E:/Tmp_E/20090816104908633ZDTPQ1_300.jpg", 300, 300, true);
			return;
		}
		
		if (false) { // ʹ�ö��̲߳���

			ImageScaleThread t1 = new ImageScaleThread(
					  "E:/My Documents_200608/Tmp_work/imageScale/42699ecd616adcee2c9cb5e5cb4a440d--Сͼ���Ų���2.gif",
	  		          "E:/My Documents_200608/Tmp_work/imageScale/42699ecd616adcee2c9cb5e5cb4a440d--Сͼ���Ų���2_200_bad2.jpg",
					200, 200, true);

			if (true) {
				return;
			}

			ImageScaleThread t2 = new ImageScaleThread(
					"E:/My Documents_200608/tmp/42699ecd616adcee2c9cb5e5cb4a440d--Сͼ���Ų���2.jpg",
					"E:/My Documents_200608/tmp/42699ecd616adcee2c9cb5e5cb4a440d--Сͼ���Ų���_200.jpg",
					200, 200, true);
			// System.out.println("-------------------- end ...0 ");
			t1.thread.join();
			// System.out.println("-------------------- end ...1 ");
			// System.out.println("-------------------- end ...2 ");
			// System.out.println("-------------------- end ...3 ");
			// System.out.println("-------------------- end ...4 ");
			t2.thread.join();
			// System.out.println("-------------------- end ...5 ");
			// System.out.println("-------------------- end ...6 ");
			// System.out.println("-------------------- end ...7 ");
			return;

		}

		if (false) { // ʹ�ÿͻ��˲���
			long bt = System.currentTimeMillis();
			ImageScaleLoss is = new ImageScaleLoss();
			
			for (int i = 0; i < 10; i++) {
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
							"�̳��̼ҿ���ע�����2.jpg", "�̳��̼ҿ���ע�����2_250.jpg",
						250, 250, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"�̳��̼ҿ���ע�����2.jpg", "�̳��̼ҿ���ע�����2_200.jpg",
					200, 200, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"�̳��̼ҿ���ע�����2.jpg", "�̳��̼ҿ���ע�����2_150.jpg",
					150, 150, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"�̳��̼ҿ���ע�����2.jpg", "�̳��̼ҿ���ע�����2_100.jpg",
					100, 100, true);
				
			}
			long end = System.currentTimeMillis();
			System.out.println((end-bt)/1000.0+"��");

		}
		if (true) { // ʹ�ÿͻ��˲���			long bt = System.currentTimeMillis();
			ImageScaleLoss is = new ImageScaleLoss();
			
			for (int i = 0; i < 1; i++) {
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
							"�̳��̼ҿ���ע�����2.jpg", "�̳��̼ҿ���ע�����2_250.jpg",
						250, 250, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"�̳��̼ҿ���ע�����2_250.jpg", "�̳��̼ҿ���ע�����2_200.jpg",
					200, 200, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"�̳��̼ҿ���ע�����2_200.jpg", "�̳��̼ҿ���ע�����2_150.jpg",
					150, 150, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"�̳��̼ҿ���ע�����2_150.jpg", "�̳��̼ҿ���ע�����2_100.jpg",
					100, 100, true);
				
				
			}
			long end = System.currentTimeMillis();
			System.out.println((end-bt)/1000.0+"��");
			//return ;
		}

		// System.out.println("--ok");
	}

}
