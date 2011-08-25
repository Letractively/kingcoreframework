package com.kingcore.framework.util ;


import java.util.Calendar;
import java.util.Date;


public class DateUtil {

	//����Ҫ�浽���ݿ���������ַ���
	//������yyyy-mm-dd��ʽ��Ϊyyyymmdd��ʽ
	public static String formatDateForDB(String str){
	    if(!isLegalDate(str)) return null;
	    return str.substring(0,4)+str.substring(4,6)+str.substring(6,8);
	}

	//������ʾ���û��ĸ�ʽ������
	//������yyyymmdd��ʽ��Ϊyyyy/mm/dd��ʽ
	public static String formatDateForUser(String date){
	  return formatDateForUser( date, "-");
	}

	//������ʾ���û��ĸ�ʽ������
	//������yyyymmdd��ʽ��Ϊ�û�ָ���ĸ�ʽ��ʽ
	public static String formatDateForUser(String date,String del){
	    if (null == date)
	       return "&nbsp;";
	    if ( date.length() < 8 )
	       return date;
	    return date.substring(0,4)+del+date.substring(4,6)+del+date.substring(6,8);
	}

	private static String formatString(int i, int j)
	{
	    String s;
	    for (s = String.valueOf(i); s.length() < j; s = "0" + s);
	    return s;
	}

	public  static String[] getYMDFromDateStr( String s )
	{
	 if (s ==null || s.length() < 8)
	    return null;
	 String[] ss = new String[3];
	 ss[0] = s.substring(0,4);
	 ss[1] = s.substring(4,6);
	 ss[2] = s.substring(6,8);
	 return ss;
	}

	//����Ҫ�浽���ݿ����ʱ���ַ���
	//������hh:mm:ss��ʽ��Ϊhhmmss��ʽ
	public static String formatTimeForDB(String time){
	    return time.substring(0,2)+time.substring(2,4)+time.substring(4,6);
	}
	//������ʾ���û��ĸ�ʽ��ʱ��
	//������hhmmss��ʽ��Ϊhh:mm:ss��ʽ
	public static String formatTimeForUser(String time){
	  if (time ==null)
	     return "&nbsp;";

	  if( time.length() < 4)
	     return time;
	  if (time.length() == 4)
	     return time.substring(0,2)+":"+time.substring(2,4);
	  if (time.length() == 6)
	     return time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
	  return time;
	}
	//������ʾ���û��ĸ�ʽ������
	//������yyyymm��ʽ��Ϊyyyy/mm��ʽ
	public static String formatYMDateForUser(String date){
	    return date.substring(0,4)+"/"+date.substring(4,6);
	}

    /**
     * @deprecated format to Date object to String accordding to the given patten<br>
     *		the pattern like 'yyyy-MM-dd';'yyyy.MM.dd'
     * @author WUZEWEN on 2005-07-17
     * @param String pattern
     * @exception Exception
     */
    public String formatDate(java.util.Date date,String pattern) throws Exception{
        //�滻getCurdate(int type)
        return new java.text.SimpleDateFormat(pattern).format(date) ;
    }

	public static String getCerrentDandT()
	{
	    return getCurrentDate("")+getCurrentTime("").substring(0,4);
	}

	/**
	* �˴����뷽��˵����
	* �������ڣ�(2001-8-24 15:26:09)
	*/
	public static String getCurrentDate() {
	    return getXXXDate(0,"-");
	}
	public static String getCurrentDate( String del)
	{
	    return getXXXDate(0,del);
	}

	//���ص�ǰ����
	public static Date getCurrentDateFormatData(){
	    Date date=new Date();
	    //
	    return date;
	}
	//���ص�ǰʱ�� ����ʽΪhhmmss��ʽ
	public static String getCurrentTime(){
	     return getXXXTime(":");
	}
	/**
	* �˴����뷽��˵����
	* �������ڣ�(2001-8-24 15:26:09)
	*/
	public static String getCurrentTime(String del){
	     return getXXXTime(del);

	}

	//���ز���n����ǰ�����ĳ�����ڵ����� ����ʽΪyyyymmdd��ʽ
	//  public static String getDateBeforeSomeDate(String dateStr,int offsetDay,String x){
	//	Calendar calendar = Calendar.getInstance();
	//	Date someDate = new Date(2002,06,03);
	//        someDate.set
	//        calendar.add(5, -offsetDay); //������ǰʱ�仹������i=0 is today
	//	int j = calendar.get(1);
	//	int k = calendar.get(2) + 1;
	//	int l = calendar.get(5);
	//	return j + x + formatString(k, 2) + x + formatString(l, 2);
	//  }



	//���ز���number����ǰ������ ����ʽΪyyyymmdd��ʽ day �������ʾ��ǰ����
	public static String getDateBeforeDay(int day){
	      String  dateStr = getXXXDate(day,"-");
	      return dateStr;
	}
	public static String getDateBeforeDay(int day,String del){
	      String  dateStr = getXXXDate(day,del);
	      return dateStr;
	}
	//���ز���number����ǰ������ ����ʽΪyyyymmdd��ʽ
	public static String getDateBeforeMonth(int month){
	      String  dateStr = getYYYDate(month,"-");
	      return dateStr;
	}


	private static String getXXXDate(int i, String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(5, -i); //������ǰʱ�仹������i=0 is today
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + x + formatString(k, 2) + x + formatString(l, 2);
	}
	private static String getXXXTime(String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = ":";
	    Calendar calendar = Calendar.getInstance();
	    int h = calendar.get(11);
	    int m = calendar.get(12);
	    int s = calendar.get(13);
	    return formatString(h, 2) + x + formatString(m, 2) + x + formatString(s, 2);
	}
	/**
	* �˴����뷽��˵����
	* �������ڣ�(2001-8-24 15:26:09)
	*/
	//��ǰ�����£�������
	private static String getYYYDate(int i, String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(2, -i); //������ǰ�����»���������i=0 �Ǳ���
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + x + formatString(k, 2) + x + formatString(l, 2);
	}

	//�жϸ������ַ����Ƿ���һ���Ϸ������ڱ�ʾ
	//��ϵͳ�е����ڱ�ʾ��Ϊ��YYYYMMDD�� ?��ʾ��һ�ַ�
	//ע�������ʽ���û��������ʾ���û��ģ�ʵ�ʴ浽���ݿ���ʱҪȥ��?
	public static boolean isLegalDate(String str){
	    String tmp=str.trim();
	    //if(tmp.length()!=8) return false;
	    try{
	      int year=Integer.parseInt(tmp.substring(0,4));
	      if(year<1900||year>3000) return false;
	      int month=Integer.parseInt(tmp.substring(4,6));
	      if(month<1||month>12) return false;
	      int day=Integer.parseInt(tmp.substring(6,8));
	      if(day<1) return false;
	      if(month==2){
	            if((year%400==0)||((year%4==0)&&(year%100!=0))){
	              if(day>29) return false;
	            }
	            else
	              if(day>28) return false;
	      }else if(day>(30+(month%2)))
	            return false;
	    }catch(Exception e){
	      return false;
	    }
	    return true;
	}
	    //private static Calendar cal_ = Calendar.getInstance(new Locale("zh","CN"));
	    public static void main(String[] s) {
	    }
}
