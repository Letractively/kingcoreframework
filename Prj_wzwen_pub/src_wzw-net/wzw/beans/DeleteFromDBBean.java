package wzw.beans;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import wzw.sql.SqlUtils;
import wzw.util.DbUtils;
import wzw.util.XmlUtils;
/*
 *   ��DBBean.java �ļ���ɾ�� on 2005-11-24
 */
public class DeleteFromDBBean {
    /**
     * ���������Ϣ�Ķ��� 
     */
    public final ActionErrors errors = new ActionErrors();
	/**
	 *	����һ���������ݿ��ѯ������Ķ���//,mySet,mySet1,mySet2,mySet3
	 */
    public CachedRowSet crs;
    /**
     *	ϵͳ��ѯ���������������system-datasource-conf.xml�ļ������ã�С��1��ʾ�����档
     */
	private static int QUERY_ROWSET_CACHE = -1 ;
	//protected Statement myStmt,inerStmt;
	//public Connection myConn=null;
	//private String table_id,field_id,listname;
	//public OracleResultSet ors  ;
	//public CachedRowSet myCcrs

    /**
     *	log4j��־����
     */
	private final static Logger log = Logger.getLogger( DbBean.class) ;
	//public static Log log = LogFactory.getLog(DBBean.class);

	/**
	 *	�������
	 */
	private HttpServletRequest request = null;
	/**
	 *	ҳ����ʾ���ݿ��ѯ������Ĳ�����
	 */
	public int[] pageInfo ;
	//��ǰ�к�(�α�ָ��)
	public int currentRowNumber=0;		

 


	/*
	 *	get a Connection instance.
	 */
	public Connection getConnection()
			throws SQLException{
		return DbUtils.getConnection() ;
	}

	/**
	 * get a Connection instance.
     * @param request �������
	 */
	public Connection getConnection(HttpServletRequest request)
        						throws SQLException {
		return DbUtils.getConnection(request) ;
    }

    /**
     * transformate string to oracle string
     * @param s String
     * @throws Exception
     * @return String
     */
    public String  toOracleString(String s) throws Exception{
		 if (s==null || s.length()<1) return s;
		 return s.replaceAll("'","''");
		}

	public String getCurdate(int type,String dateStr,int stype) throws Exception {
		String ret;
		SimpleDateFormat dateFmt;
		//System.out.print("\n getCurdate()") ;
		if (stype==1) dateFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		else dateFmt = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());
		//System.out.print("\n+MM/dd/yyyy" + dateStr + "stype="+type) ;
		java.util.Date curD = dateFmt.parse(dateStr);

		switch (type){
		  case 1:  dateFmt = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
		             ret = dateFmt.format(curD);
		             break;
		  case 2:  dateFmt = new SimpleDateFormat("yyyy.MM.dd",Locale.getDefault());
		             ret = dateFmt.format(curD);
		             break;
		  case 3:  dateFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		             ret = dateFmt.format(curD);
		             break;
		  default: ret = "";
		}
		return ret;
	}

	public String getBlobValue(CachedRowSet crs,int index) throws Exception {
		String ret="";

		//ResultSet rs = null ;
		//rs = aSet.getOriginal() ;
		//if(rs==null)
		//	System.out.print("\ncan't get the origian resultset");
		//else
		//	System.out.print("\nget the origian resultset ok");
		//
		ResultSet rs = null ;
		rs = crs.getOriginal() ;
		  BLOB blob = ((OracleResultSet)rs).getBLOB(index);
		  //BLOB blob = null ;

		  if (blob!=null){
		       InputStream in = blob.getBinaryStream();
		      int bufferSize = blob.getBufferSize();
		      byte[] buffer = new byte[bufferSize];
		      int bytesRead = 0;
		      in.read(buffer);
		       ret = new String(buffer);
		       in.close();
		  }
		  rs.close() ;
		return ret;
	}


////////////////////////////////////////////////////////
//
// 	The methodes below are just for query.
//
//
///////////////////////////////////////////////////////
    /**
     * ��ѯ����
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @throws Exception
     * @return CachedRowSet����
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
						     String sqlString )throws Exception {
          return doQuery(request,
          			     sqlString,
						 10);
	}

    /**
     * ��ѯ����
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @param pageInfor ÿҳ����
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int pageInfor)throws Exception {
        int[] pInfo= new int[4];
			pInfo[0] = pageInfor;
        return doQuery( request,
                        sqlString,
                        pInfo) ;
    }

    /**
     * ��ѯ����
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @param pageInfor int[] ҳ����Ϣ��[0]��ʾÿҳ����
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
						   String sqlString,
						   int[] pageInfor)throws Exception {

		return doQuery( request,
						sqlString,
						pageInfor,
						false) ;
	}

    /**
     * ��ѯ����
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @param pageInfor ҳ����Ϣ��[0]��ʾÿҳ������������ҳ����������
     * @param isBuffered �Ƿ񻺴��ѯ���󼯺�
     * @throws Exception
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int pageInfo,
                           boolean isBuffered)throws Exception {
        int[] pInfo= new int[4];
			pInfo[0] = pageInfo;
		return doQuery(request,
                       sqlString,
                       pInfo,
                       isBuffered) ;
        }
    /**
     * ��ѯ����
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @param pageInfor ҳ����Ϣ��[0]��ʾÿҳ������������ҳ����������
     * @param isBuffered �Ƿ񻺴��ѯ���󼯺�
     * @throws Exception
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int[] pageInfo,
                           boolean isBuffered)throws Exception {
              return doQuery(request,
                             sqlString,
                             pageInfo,
                             null,
                             isBuffered) ;
   }

    /**
     * ��ѯ����
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @param pageInfor ҳ����Ϣ��[0]��ʾÿҳ������������ҳ����������
     * @param pageControl ������Ϣ����ǰ��һҳ������ҳ�ȵ�
     * @throws Exception
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int[] pageInfo,
                           String[] pageControl)throws Exception {
              return doQuery(request,
                             sqlString,
                             pageInfo,
                             pageControl,
                             false) ;
        }

    /**
     * ��ѯ���ݣ�����crs�����ٶ���Ĵ�������������Ը��ã�
     * @param request HttpServletRequest
     * @param sqlString Ҫִ�е�SQL��䣬eg��select a,b,c from mytable
     * @param pageInfor ҳ����Ϣ��[0]��ʾÿҳ������������ҳ����������
     * @param pageControl ������Ϣ����ǰ��һҳ������ҳ�ȵ�
	 * @param isBuffered �Ƿ�ʹ�û��棬�������ж����棬Ĭ��Ϊfalse��
	 * @exception no exception
     * @return CachedRowSet
	 */
	public  CachedRowSet doQuery(HttpServletRequest request,
						String sqlString,
						int[] pageInfo,
						String[] pageControl,
                        boolean isBuffered)throws Exception {

		this.pageInfo = pageInfo ;
		String action = request.getParameter("Action") ;
		if (request.getParameter("PageNumber") == null || request.getParameter("PageNumber").trim().equals(""))
			pageInfo[1] = 1;
		else
			pageInfo[1] = Integer.parseInt(request.getParameter("PageNumber"));

		log.debug("doQuery(), sql="+sqlString) ;
		int i=0,j=0,k=0;
		int pageSum=0,pagePrior,pageNext;
		int pageSize=pageInfo[0];
		int pageNumber=pageInfo[1];		//��ǰҳ��
		String s="";
		//Vector ret = null;

		//��������Ƿ�ҳ��������Ȳ�����ȡ����,���ʹ�û���Ļ�
		if( (isBuffered)&&(action!=null) && (action.equals("changePage")||action.equals("sort")) ){
			//���������û�У���������ݿ⣬ͬʱ����
			if( !this.getCachedRowSetFromCache(request,sqlString) ){
				doInnerQuery(request, sqlString);
				setCachedRowSetToCache(request,sqlString,this.crs ) ;
			}
		}else{
			doInnerQuery(request, sqlString);
            if(isBuffered){
                setCachedRowSetToCache(request, sqlString, this.crs);
            }
		}
		//����������У��α�ָ��Ҫ�ص� beforeFirst ��
		this.crs.beforeFirst() ;

		//System.out.print("\n doQuery()"+crs.size()) ;
		if (!crs.next()){
			pageInfo[1] = 0;
			pageInfo[2] = 0;
			pageInfo[3] = 0;
			//s = "��0����¼ ��0/0ҳ ";
			//pageControl[0] = s;			//pageControl�ڱ�ȥ��
			return null;		//crs;
		}

		//ret = new Vector();
		crs.last();
		k = crs.getRow();
		pageInfo[3] = k;//��¼�ܼ�¼����
		double f = (double) k;
		pageSum= (int)Math.ceil(f/pageSize);
		i = pageNumber;
		if (pageNumber>pageSum) i = pageSum;
		if (pageNumber<1) i = 1;
		pageNumber = i;

		pagePrior = pageNumber - 1;
		pageNext = pageNumber + 1;
		if (pagePrior<1) pagePrior = 1;
		if (pageNext>pageSum) pageNext = pageSum;

		// wuzewen �����������Ҫ��ʾ�����ݷŵ�һ��Vector��ȥ��
		// ҳ����� Vector������ҳ�����ݡ�
		// Vector �������� CachedRowset��һ����
		// �����ǣ��������String���ͣ������ȡֵ��Vector��ȥ��
		// Ҳ����ֱ��ʹ��CachedRowset ���󣬲�ʹ��Vector��
		// ����һ�㻹Ҫ����ȷand size��

		// ֤�� Integer���͵��У�һ�����Էŵ�Vector�У�
		// ֻ��ҳ����ȡVector�е�ֵʱ����Ȼʹ��(String)v.get(0),
		//     ������(Integer)get(0),��֪������Double��Date�Ƿ�Ҳһ������



		//wzw on 2005-11-28 ȥ������Vector��ֱ�ӷ���CachedRowSet������
		//���ٶ���Ĵ������������
		//i=0;
		//int columnCount = crs.getMetaData().getColumnCount();
		//do{
		//	Vector v = new Vector();
		//	for (j=1;j<=columnCount;j++)
		//		v.add(crs.getString(j));
		//	ret.add(v);
		//}while ((++i<pageSize)&&(crs.next()));

		this.currentRowNumber = (pageNumber-1)*pageSize;

		//wzw on 2005-11-28 ȥ������Ĵ���
		//while ((j<(pageNumber-1)*pageSize)&&(crs.next())) j++;
		//if (this.currentRowNumber>0)
		//	crs.absolute(this.currentRowNumber+1);
		//else{
		//	crs.first();
		//}

		pageInfo[1] = pageNumber;
		pageInfo[2] = pageSum;

		//����ʹ��Vector����ֱ��ʹ��CachedRowset���α궼��λ
		if (this.currentRowNumber>0) {
			crs.absolute(this.currentRowNumber);
		}
		else
			crs.beforeFirst() ;

		// wuzewen ҳ�浼����Ϣ���µĺ����������˴�����ʹ��
		//s = "��" + Integer.toString(k) + "����¼ &nbsp;ÿҳ" + Integer.toString(pageSize) + "����¼  &nbsp;&nbsp;&nbsp;��ǰ��" + Integer.toString(pageNumber) + "/" + Integer.toString(pageSum) + "ҳ ";
		//if (pageNumber>1)
		//  s = s + "<a href=\"javascript:gotoPage(" + Integer.toString(pagePrior) + ")\">ǰһҳ</a> ";
		//if (pageNumber!=pageSum)
		//  s = s + "<a href=\"javascript:gotoPage(" + Integer.toString(pageNext) + ")\">��һҳ</a> ";
		//s = s + "��ת��&nbsp;<input type=text class='input' size=3 name='inputpage' maxlength=3>&nbsp;ҳ&nbsp;" +
		//  "<b><a href='javascript:jumpToPage()'>GO</b></a>";
		//pageControl[0] = s;
		return this.crs;
	}

	/**
     *	��ȡ���ݿ����ӣ�ִ��SQL��䣬ResutSet��ʼCachedRowSet
     */
	public void doInnerQuery(HttpServletRequest request, String s) throws Exception {

		//log.debug("doInnerQuery :sql = " +s) ;
		Connection conn = this.getConnection(request) ;
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                          					ResultSet.CONCUR_READ_ONLY) ;
		ResultSet rs = null ;
		rs = stmt.executeQuery( s ) ;
		//if (rs.next())
		//{
		//	System.out.print("\ndo inerQuery " + rs.getString(1)) ;
		//}else
		//{
		//	System.out.print("\ndo inerQuery ResultSet.") ;
		//}
		//rs.beforeFirst() ;

		crs.populate( rs ) ;

		//System.out.println (conn.getClass().toString());
		if(rs!=null){
			rs.close();
		}
		if(stmt!=null){
			stmt.close();
		}
		if(conn!=null){
			conn.close();
		}
	}


////////////////////////////////////////////////////////
//
// 	The methodes below are just for update.
//	 include Execute an SQL INSERT, UPDATE, or DELETE sql statement.
//
//
///////////////////////////////////////////////////////
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql) throws SQLException {
        return this.doUpdate(request, sql, (Object[]) null);
    }    
    
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate( String sql) throws SQLException {
        return DbUtils.executeUpdate( sql );
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query with a single replacement
     * parameter.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql, Object param)
        throws SQLException {

        return this.doUpdate(request, sql, new Object[] { param });
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql, Object[] params)
        throws SQLException {

		Connection conn = null ;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
        	conn = getConnection( request ) ;
            stmt = conn.prepareStatement( sql);
            SqlUtils.fillStatement(stmt, params);

            rows = stmt.executeUpdate();
        	//log.fatal("fillStatement3");
        	conn.commit() ;

        } catch (SQLException e) {
            conn.rollback();
        	log.error("DBUtil.doUpdate :"+ e.getMessage() ) ;
            //this.rethrow(e, sql, params);

        } finally {
        	try{
        		org.apache.commons.dbutils.DbUtils.close(stmt);

            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateBean.doUpdate() ����Exception����������ϢΪ��\n", e);
	            //this.addErrors(new ActionError("error.database.deal"));
            }
        	try{
            	conn.close() ;

            }catch(SQLException e)
            {
	            log.fatal("fail when close Connection!", e);
	            //this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return rows;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql, List list)
        throws SQLException {

		Connection conn = getConnection( request ) ;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = conn.prepareStatement( sql);
            SqlUtils.fillStatement(stmt, list);

            rows = stmt.executeUpdate();
            conn.commit() ;
        	//log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            log.fatal(this.getClass().getName()+" "+ e.getMessage() ) ;
            conn.rollback();
            //this.rethrow(e, sql, list);

        } finally {
        	try{
            	if(stmt!=null)
            		stmt.close() ;
            	//DbUtils.close(stmt);
            	if(conn!=null)
            		conn.close() ;

            }catch(SQLException e)
            {
	            log.fatal("��ִ��UpdateBean.doUpdate() ����Exception����������ϢΪ��\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return rows;
    }


    /**
     * Execute a batch of sql statements.
     * @param request a http request object.
     * @param allsql Ҫִ�е�sql�����ɵ����顣
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public int[] doBatch(HttpServletRequest request, String[] allsql)
        throws SQLException {

        Connection conn = getConnection( request ) ;
        PreparedStatement pstmt = null;
        String sql=null;
        int returns[];
        try {
            pstmt = conn.prepareStatement( sql);
            for(int i=0;i<allsql.length;i++){
                pstmt.addBatch(allsql[i]);
            }

            returns = pstmt.executeBatch();
            conn.commit() ;
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            conn.rollback();
            log.fatal(this.getClass().getName()+" "+ e.getMessage() ) ;
            //this.rethrow(e, sql, list);

        } finally {
            try{
                if(pstmt!=null)
                    pstmt.close() ;
                //DbUtils.close(stmt);
                if(conn!=null)
                    conn.close() ;
            }catch(SQLException e)
            {
                log.fatal("��ִ��UpdateBean.doUpdate() ����Exception����������ϢΪ��\n", e);
                this.addErrors(new ActionError("error.database.deal"));
            }
        }
        return null;
    }


    /**
     * Execute a batch of sql statements.
     * @param request a http request object.
     * @param List Ҫִ�е�sql�����ɵ�ʵ����List�ӿڵĶ���
     * @throws ִ��������ʧ�ܡ�
     * @return ÿ��sql���Ӱ���������ɵ����顣
     */
    public int[] doBatch(HttpServletRequest request, List allsql)
        throws SQLException {
        if(allsql==null){
            return null;
        }
        return doBatch(request, (String[])allsql.toArray());
    }

    /**
     * ִ��SQL����������
     * @param allsql sql��伯�϶���
     * @return ÿ��sql����޸ĵ�����������
     * @throws SQLException sql���ִ���쳣
     */
    public int[] doBatch( List allsql)
        throws SQLException {
    	
        return DbUtils.executeBatch( allsql );
    }

	/**
	 *
	 * ������ 2005-07-16
	 * //����ÿ������Ҫ��ѯ���ݿ⣬���ڷ�ҳ������ȵ�
	 * //ֻҪ��ѯ��sql���һ����������ÿ�ζ���ѯ���ݿ�
	 * //ֻ�������������Ч��
	 *
	 * //��Ϊjavabean����scope=page|request�ж����ܻ������ݣ�
	 * //��Ϊ�Ѿ���������ͬ�� �����ˣ��ŵ�scope=session|application��,
	 * //�ֻ���ط����������Ĵ���
	 *
	 * //�˴��Ľ�������ǲ���session�з���һ�����󣬻��漸��(5)�����
	 * //�����ȿ��Ի��棬�ֲ�����̫�࣬�������������ݿ�Ƶ�����ʼ��ҵ�ƽ��
	 *
	 *	���飺����ֻ���ڷ�ҳ������Ĳ��������ڵ�һ�β�ѯ���������ǻ���
	 *	   ����Ҳ���Զ����в�ѯ(��ҳ������֮���)���棬
	 *		ʵ���ϳ��� 5��(�ɶ���)�ͻᱻ�߳����棬��õİ취�ǣ�
	 *	 ���XXX��Ϣ��ѯ�������������棬������ȥ������ȡ
	 *       ��ҳ������ӻ���ȡ��������������о�ȡ��û�оͲ�ѯ�ٷ�
	 *
	 *   ���Կ��Ƿŵ� scope=application��ȥ��Ӧ�õ������û�������
	 *   modify: �ŵ� scope=application��ȥ�ˣ�����Ӧ�õ��û�������
	 */
	public boolean getCachedRowSetFromCache(HttpServletRequest request,
						String sqlString ){
		Hashtable ht = new Hashtable(5) ;
		ht = (Hashtable)request.getSession().getServletContext().getAttribute("CachedRowCache") ;
		log.debug( "getCachedRowSetFromCache ht = " + ht) ;

		if(ht==null)
			return false ;
		log.debug ("��ǰ�����ѯ������="+ht.size()) ;
		if(ht.get(sqlString)!=null){
			log.debug( "contains in cache" ) ;
			this.crs = (CachedRowSet)ht.get(sqlString) ;
			return true ;
		}
        log.debug( "not contains in cache" ) ;
		return false ;
	}
	/**
	 * WUZEWEN 2005-07-16
	 *
     *	��ȡ�õ�RowCachedRowSet ���� session��Cache������ȥ
     *  �޸�Ϊ ���� application ��Cache������ȥ
     */
    public void setCachedRowSetToCache(HttpServletRequest request,
						String sqlString,Object obj){
		Hashtable ht = new Hashtable() ;

		//�����������û��ȷ����ȡ��������
		//<0:ûȡֵ��==0:�����棻>0��ȡ�õĻ���ֵ��
		if(this.QUERY_ROWSET_CACHE<0){
	        Vector vec = new Vector() ;
	        vec.add( "date-cache@query-rowset-cache" ) ;
	        vec = XmlUtils.getElementValues(getClass().getResourceAsStream("/conf/system-datasource-conf.xml"),vec) ;
	        if(vec == null ){
	        	//throw new FileNotFoundException("��Ӧ�����ݿ������ļ������ڣ�") ;
	        	log.error("��Ӧ�����ݿ������ļ������ڣ�") ;
	        	this.QUERY_ROWSET_CACHE = 5 ;   //Ĭ��5��
	        }else{
	        	this.QUERY_ROWSET_CACHE = Integer.parseInt( vec.elementAt(0).toString() ) ;
	        }
	        log.debug("��ȡ��ѯ���ݻ�������"+this.QUERY_ROWSET_CACHE) ;
		}

		ht = (Hashtable)request.getSession().getServletContext().getAttribute("CachedRowCache") ;

		if(ht!=null){
	    	//log.debug("before ��ȡ��ѯ���ݻ�������"+ht.size()) ;
			//����Ѿ����ˣ��Ƴ�:  �����Ƴ���hashtable�Զ����Ǿ�ֵ��
			//if(ht.get(sqlString)!=null){
			//	ht.remove(sqlString) ;
			//}

			//����������õ���������Ƴ���һ��
			if( ht.size()>= this.QUERY_ROWSET_CACHE )
				ht.remove(ht.keys().nextElement()) ;
			//���뻺����
			ht.put( sqlString, obj ) ;
			log.debug ("set in cache and then size="+ht.size() ) ;

		}else{
			//log.debug ("set 1 !") ;
			ht = new Hashtable() ;
			ht.put( sqlString, obj ) ;
		}
		request.getSession().getServletContext().setAttribute("CachedRowCache",ht) ;
		log.debug ("��ǰ�����ѯ������="+ht.size()) ;
	}

	/**
     *	��ȡҳ������������ҳ��Ϣ
     *�˴�������js�ļ��е�String.trim() ���������֧�֡�
     *
     *  wuzewen
     *  2005-07-16
     *
     */
	public String pageNavigator(){
	    StringBuffer sb = new StringBuffer();
		String action = "changePage";
		sb.append("<table valign=bottom width=\"100%\" border=0 cellspacing=\"0\" cellpadding=\"0\" class=\"tableBorder\">")
	      .append("<tr valign=bottom>")
	      .append("<td class=\"HeaderBG\" width=\"25%\">")
	      .append("&nbsp;��"+pageInfo[3]+"����¼&nbsp;&nbsp;")
	      .append("ÿҳ"+pageInfo[0]+"��</td>")
	      .append("<td valign=bottom class=\"HeaderBG\" width=\"75%\" align=\"right\">")
	      .append("��" + String.valueOf(pageInfo[1]) + "ҳ��")
	      .append("��" + String.valueOf(pageInfo[2]) + "ҳ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	      .append( (pageInfo[1]==1)?"[�� ҳ]":"<a href=\"?Action=changePage&PageNumber=1\">[�� ҳ]</a>")
	      .append("|")
	      .append( (pageInfo[1] == 1)?"[��һҳ]":"<a href=\"?Action=changePage&PageNumber=" + String.valueOf(pageInfo[1] - 1) + "\">[��һҳ]</a>")
	      .append("|")
	      .append((pageInfo[1] == pageInfo[2])?"[��һҳ]":"<a href=\"?Action=changePage&PageNumber=" + String.valueOf(pageInfo[1] + 1) + "\">[��һҳ]</a>")
	      .append("|")
	      .append( (pageInfo[1] == pageInfo[2])?"[ĩ ҳ]":"<a href=\"?Action=changePage&PageNumber=" + String.valueOf(pageInfo[2]) + "\">[ĩ ҳ]</a>")
	      .append("&nbsp;&nbsp;ת����<input type=\"text\" name=\"PageNumber\" size=\"2\" onkeypress=\"return(!event.shiftKey&amp;&amp;event.keyCode>47 &amp;&amp; event.keyCode<58 )\">&nbsp;")
	      .append("<input class=input type=\"button\" value=\"�鿴\" onclick=\"javascript:if(document.all.PageNumber.value.trim()=='') return;if(document.all.PageNumber.value.trim()=="+String.valueOf(pageInfo[1])+") return;window.location.href='?Action=changePage&PageNumber='+document.all.PageNumber.value;\">")
	      .append("</td>")
	      .append("</tr>")
	      .append("</table>") ;

		return sb.toString() ;
	}


	/*
	 *  set the value of request parameter from the page
	 */
	public void setRequest( HttpServletRequest request )
	{
		//System.out.print("\n setRequest()") ;
		this.request = request ;
	}
	/*
	 *  get request
	 */
	public HttpServletRequest getRequest( )
	{
		return this.request ;
	}


    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause The original exception that will be chained to the new
     * exception when it's rethrown.
     *
     * @param sql The query that was executing when the exception happened.
     *
     * @param params The query replacement paramaters; <code>null</code> is a
     * valid value to pass in.
     *
     * @throws SQLException
     */
    protected void rethrow(SQLException cause, String sql, List list)
        throws SQLException {

        StringBuffer msg = new StringBuffer(cause.getMessage());

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (list == null) {
            msg.append("[]");
        } else {
            msg.append( list ) ;
        }

        SQLException e = new SQLException(msg.toString());
        e.setNextException(cause);

        throw e;
    }


////////////////////////////////////////////////////////
//
// 	The methodes below are just for error and message!!! ; ;
//  zewenWoo 2004.09.16
//
///////////////////////////////////////////////////////
	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
    public void addErrors(String key)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, new ActionError(key));
    }
    public void clearErrors(){
    	errors.clear();
    }

	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
    public void addErrors(ActionError error)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, error);
    }
	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
    public void addErrors(String property, ActionError error)
    {
        errors.add(property, error);
    }
	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
	public void saveErrors(HttpServletRequest request){
		request.setAttribute(Globals.ERROR_KEY ,this.errors) ;

	}

    public void addMessages(String key)
    {

    }


////////////////////////////////////////////////////////
//
// 	���²����൱��QueryActionִ�в�ѯ���ص�һ��
//		QueryDataSet ����󣬶��Ƕ��ڲ��� crs�Ĳ���
//		�ο� com.kingcore.framework.bean.QueryDataSet
//
///////////////////////////////////////////////////////
	/**
     * @deprecated to do something
     * @author WUZEWEN on 2005-07-17
     * @param String a
     * @param String b
     * @return String c
     * @exception no exception
     */
    public boolean nextRowInPage() throws SQLException{
    	//�������ҳβ�������ݼ�β������ false
    	if(this.currentRowNumber>=this.crs.size() ||
    	   		this.currentRowNumber>=pageInfo[0]*pageInfo[1]){
    		return false ;
    	}
    	//�α�ָ����һ��
    	this.currentRowNumber++ ;
    	if(this.currentRowNumber>this.crs.size()){
    		this.currentRowNumber= this.crs.size();
    	}
    	return this.crs.next() ;
    }


	/**
     *  ��ȡ�е����
     *  wuzewen 2005-07-17
     *
     *
     */
	public void getRowNumber(){
		//return pageInfo[0]*(pageInfo[1] - 1) + i + 1  ;
	}

////////////////////////////////////////////////////////
//
//	�����ǲ������������ģʽ�����ǵ���CachedRowSet �������Ӧ�ķ�����
//
//
///////////////////////////////////////////////////////
	public boolean absolute( int row) throws SQLException
	{
		return ( crs.absolute( row ) ) ;
	}

	public void beforeFirst() throws SQLException
	{
		crs.beforeFirst() ;
	}
	public void afterLast() throws SQLException
	{
		crs.afterLast() ;
	}
	public boolean first() throws SQLException
	{
		return (crs.first() ) ;
	}
	public boolean last() throws SQLException
	{
		return (crs.last() ) ;
	}

	public boolean previous() throws SQLException
	{
		return (crs.previous() ) ;
	}
	public boolean next() throws SQLException
	{
		return (crs.next() ) ;
	}

	/**
	 *	�����ܵ�������
	 */
	public int size()
	{
		return ( crs.size() ) ;
	}
	/**
     * ���ص�ǰ�кţ��滻ֱ��ʹ��DBBean��currentRowNumber��������ȡ��ǰ�еķ�����
     * @author WUZEWEN on 2005-12-10
     * @return the current row number; 0 if there is no current row.
	 */
	public int getRow() throws SQLException
	{
		return ( crs.getRow() ) ;
	}

	/**
	 *	BigDecimal ��geter ������
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException
	{
		return ( crs.getBigDecimal( colNum ) ) ;
	}

	public BigDecimal getBigDecimal( String colName ) throws SQLException
	{
		return ( crs.getBigDecimal( colName)) ;
	}

	/**
	 *	Blob ��geter ������
	 */
	public Blob getBlob( int colNum) throws SQLException
	{
		return ( crs.getBlob( colNum )) ;
	}

	public Blob getBlob( String colName ) throws SQLException
	{
		return (crs.getBlob( colName )) ;
	}

	/**
	 *	Boolean ��geter ������
	 */
	public boolean getBoolean( int colNum) throws SQLException
	{
		return ( crs.getBoolean( colNum) ) ;
	}

	public boolean getBoolean( String colName) throws SQLException
	{
		return ( crs.getBoolean( colName ) ) ;
	}


	/**
	 *	Byte ��geter ������
	 */
	public byte getByte( int colNum ) throws SQLException
	{
		return ( crs.getByte( colNum ) ) ;
	}
	public byte getByte ( String colName ) throws SQLException
	{
		return ( crs.getByte( colName ) ) ;
	}


	/**
	 *	Byte[] ��geter ������
	 */
	public byte[] getBytes( int colNum ) throws SQLException
	{
		return ( crs.getBytes( colNum ) ) ;
	}
	public byte[] getBytes( String colName )  throws SQLException
	{
		return ( crs.getBytes( colName ) ) ;
	}

	/**
	 *	CharacterStream ��geter ������

	public CharacterStream getCharacterStream( int colNum)
	{
		return ( crs.getCharacterStream( colNum) ) ;
	}

	public CharacterStream getCharacterStream( String colName)
	{
		return ( crs.getCharacterStream( colName) ) ;
	}
	*/

	/**
	 *	Clob ��geter ������
	 */
	public Clob getClob( int colNum) throws SQLException
	{
		return ( crs.getClob( colNum ) ) ;
	}
	public Clob getClob ( String colName)  throws SQLException
	{
		return ( crs.getClob( colName ) ) ;
	}

	/**
	 *	Date ��geter ������
	 */
	public java.util.Date getDate( int colNum) throws SQLException
	{
		return ( crs.getDate( colNum ) ) ;
	}

	public java.util.Date getDate( String colName) throws SQLException
	{
		return ( crs.getDate( colName ) ) ;
	}

	/**
	 *	Double ��geter ������
	 */
	public double getDouble( int colNum ) throws SQLException
	{
		return ( crs.getDouble( colNum ) ) ;
	}
	public double getDouble( String colName ) throws SQLException
	{
		return ( crs.getDouble( colName ) ) ;
	}

	/**
	 *	Double ��geter ������
	 */
	public float getFloat( int colNum) throws SQLException
	{
		return ( crs.getFloat( colNum ) ) ;
	}
	public float getFloat( String colName) throws SQLException
	{
		return ( crs.getFloat( colName ) ) ;
	}

	/**
	 *	Integer ��geter ������
	 */
	public int getInt( int colNum) throws SQLException
	{
		return ( crs.getInt( colNum )) ;
	}
	public int getInt( String colName) throws SQLException
	{
		return ( crs.getInt( colName )) ;
	}

	/**
	 *	Long ��geter ������
	 */
	public long getLong( int colNum ) throws SQLException
	{
		return ( crs.getLong( colNum ) ) ;
	}
	public long getLong( String colName ) throws SQLException
	{
		return ( crs.getLong( colName ) ) ;
	}

	/**
	 *	Object ��geter ������
	 */
	public Object getObject( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object getObject( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}

	/**
	 *	����� ��geter ������
	 */
	public Object get( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object get( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}


	/**
	 *	Short ��geter ������
	 */
	public short getShort( int colNum ) throws SQLException
	{
		return ( crs.getShort( colNum )) ;
	}
	public short getShort( String colName )  throws SQLException
	{
		return ( crs.getShort( colName ) ) ;
	}

	/**
	 *	String ��geter ������
	 */
	public String getString( int colNum) throws SQLException
	{
		return ( crs.getString( colNum ) ) ;
	}
	public String getString( String colName ) throws SQLException
	{
		return ( crs.getString( colName ) ) ;
	}

	/**
	 *	Time ��geter ������
	 */
	public Time getTime( int colNum ) throws SQLException
	{
		return ( crs.getTime( colNum ) ) ;
	}
	public Time getTime( String colName ) throws SQLException
	{
		return ( crs.getTime( colName ) ) ;
	}

	/**
	 *	Timestamp ��geter ������
	 */
	public Timestamp getTimestamp( int colNum ) throws SQLException
	{
		return ( crs.getTimestamp( colNum ) ) ;
	}

	public Timestamp getTimestamp( String colName ) throws SQLException
	{
		return ( crs.getTimestamp( colName ) ) ;
	}

	//˵����������get������Ҫ����ӡ�



//		QueryDataSet ����󣬶��Ƕ��ڲ��� crs�Ĳ���
//		�ο� com.kingcore.framework.bean.QueryDataSet
//	QueryDataSet ���ܽ�������


////////////////////////////////////////////////////////
//
// 	get Image object
//  zewenWoo 2004.09.21
//
///////////////////////////////////////////////////////
	/*
	public void getImage(javax.servlet.http.HttpServletResponse response){
		ResultSet rs ;
		rs.next() ;
		String dim_image = rs.getString("photo") ;
		byte[] blocco = rs.getBytes("photo");
		response.setContentType("image/jpeg") ;
		javax.servlet.ServletOutputStream op = response.getOutputStream() ;
		for(int i=0; i<Integer.parseInt(dim_image); i++){
			op.write(blocco[i]) ;
		}


	}
	*/
}
