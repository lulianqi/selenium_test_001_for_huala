package my_selenium;
import java.awt.PageAttributes.OriginType;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import my_selenium.MyDbTool.SqlMonitor;

import org.apache.xpath.operations.Bool;

import bsh.Console;

import com.gargoylesoftware.htmlunit.javascript.host.Map;
//import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.sun.jna.platform.win32.Sspi.PSecHandle;


/**
 * 
 * @author LIJIE 2016/3/29
 *
 */
public class MyDbTool {
	
	private String dbName="";
	private String dbUser="";
	private String dbPassword="";
	private String errorMes="NULL";
	
	private boolean isConnected=false;
	private Connection connection ;
	
	private java.util.HashMap<String, SqlMonitor> monitorListHashMap=new HashMap<>();
	

	class SqlMonitor{
		
		private PreparedStatement  myMonitorPreStatement=null;	
		private ResultSet myMonitorRs=null;
		private String monitorChangeSqlSql="";	
		
		private int originRowNum=0;
		private int listenColumn=1;
		private int listenRow=1;
		private String originValue="";
		private boolean isVauleFlag=false;
		
		
		public SqlMonitor(String monitorSql) {
			monitorChangeSqlSql=monitorSql;
		}
		
		/**
		 * start Monitor with the table row count
		 * @return
		 */
		public Boolean startMonitorchange() {
			try {
				//myMonitorPreStatement=(PreparedStatement) connection.prepareStatement(monitorChangeSqlSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				myMonitorPreStatement=(PreparedStatement) connection.prepareStatement(monitorChangeSqlSql);
				myMonitorRs=myMonitorPreStatement.executeQuery();
				myMonitorRs.last();
				originRowNum=myMonitorRs.getRow();
				isVauleFlag=false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		/**
		 * start Monitor with appoint location vaule (if the listen vaule is not exist we think the vaule change)
		 * @param columnIndex
		 * @param rowIndex
		 * @return
		 */
		public Boolean startMonitorchange(int columnIndex,int rowIndex ) {
			try {
				//myMonitorPreStatement=(PreparedStatement) connection.prepareStatement(monitorChangeSqlSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				myMonitorPreStatement=(PreparedStatement) connection.prepareStatement(monitorChangeSqlSql);
				myMonitorRs=myMonitorPreStatement.executeQuery();
				if(!myMonitorRs.absolute(rowIndex))
				{
					return false;
				}
				listenColumn=columnIndex;
				listenRow=rowIndex;
				originValue=myMonitorRs.getString(columnIndex);
				isVauleFlag=true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		public boolean getMonitorchange(int timeOut) {
			
			for(int i=0 ;i<timeOut;i++)
			{
				
				try {
					myMonitorPreStatement=(PreparedStatement) connection.prepareStatement(monitorChangeSqlSql);
					myMonitorRs=myMonitorPreStatement.executeQuery();
					myMonitorRs.last();
					if(isVauleFlag){
						//if the listen vaule is not exist we think the vaule chenge（被监听的数据消失了没有了也判定为有变化）
						if(!myMonitorRs.absolute(listenRow)){
							return true;
						}
						if(originValue!=myMonitorRs.getString(listenColumn)){
							originValue=myMonitorRs.getString(listenColumn);
							return true;
						}
						
					}else {
						if(originRowNum!=myMonitorRs.getRow())
						{
							originRowNum=myMonitorRs.getRow();
							return true;
							//break;
						}
					}
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
		
		public void Close() {
			if(myMonitorRs!=null){
				try {
					myMonitorRs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(myMonitorPreStatement!=null){
				try {
					myMonitorPreStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			}
		}
	}
	
	/**
	 * i will add a monitor if return false call  getErrorMes(and it is listen row number)
	 * @param key monitor name 
	 * @param sql sql 
	 * @return is sucess
	 */
	public boolean monitorAdd(String key,String sql) {
		if(monitorListHashMap.containsKey(key)){
			errorMes="monitorAdd : exist monitor name";
			return false;
		}
		SqlMonitor tempMonitor=new SqlMonitor(sql);
		if(!tempMonitor.startMonitorchange())
		{
			return false;
		}
		monitorListHashMap.put(key, tempMonitor);
		return true;
	}
	
	/**
	 * i will add a monitor if return false call  getErrorMes(and it is listen value by column and row)
	 * @param key monitor name
	 * @param sql sql
	 * @param columnIndex  column Index
	 * @param rowIndex  row Index
	 * @return
	 */
	public boolean monitorAdd(String key,String sql,int columnIndex,int rowIndex) {
		if(monitorListHashMap.containsKey(key)){
			errorMes="monitorAdd : exist monitor name";
			return false;
		}
		SqlMonitor tempMonitor=new SqlMonitor(sql);
		if(!tempMonitor.startMonitorchange(columnIndex, rowIndex))
		{
			errorMes="monitorAdd : start monitor fail";
			return false;
		}
		monitorListHashMap.put(key, tempMonitor);
		return true;
	}
	
	
	/**
	 * Listen monitor by name [no change is false ,when change return true,and when can not find monitor name return false immediately](it is block)
	 * @param moitorNameString monitor name 
	 * @param timeOut time out by second
	 * @return
	 */
	public boolean monitorListen(String monitorNameString,int timeOut) {
		if(monitorListHashMap.containsKey(monitorNameString)){
			return monitorListHashMap.get(monitorNameString).getMonitorchange(timeOut);
		}
		else {
			errorMes="monitorListen : can not find moitor name";
			return false;
		}
	}
	
	/**
	 * remove the Monitor by name
	 * @param monitorNameString monitor name
	 * @return
	 */
	public boolean monitorRemove(String monitorNameString) {
		if(monitorListHashMap.containsKey(monitorNameString)){
			monitorListHashMap.get(monitorNameString).Close();
			monitorListHashMap.remove(monitorNameString);
			return true;
		}
		else {
			errorMes="removeMonitor : can not find moitor name";
			return false;
		}
	}
	
	/**
	 * remove the all the Monitor 
	 * @return
	 */
	public void monitorRemove() {
		if(monitorListHashMap.size()>0){
			/*for(Map.Entry tempMonitor : monitorListHashMap.entrySet().iterator())
			{
				
			}*/
			Iterator<Entry<String, SqlMonitor>> iter =monitorListHashMap.entrySet().iterator();
			while (iter.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
				monitorListHashMap.get((String)entry.getKey()).Close();
				monitorListHashMap.remove((String)entry.getKey());
			}
		}	
	}
	
	public void testEx() {
		SqlMonitor sm =new  SqlMonitor("SELECT * from h_seller_order WHERE seller_id=562 and (order_status ='shipping')");
		sm.startMonitorchange();
	}
	
	/**
	 * initialize the MyDbTool
	 * @param db_name sql db name
	 * @param db_user sql db user name
	 * @param db_password db password
	 */
	public MyDbTool(String db_name,String db_user,String db_password)
	{
		dbName=db_name;
		dbUser=db_user;
		dbPassword=db_password;		
		isConnected=false;
	}
	
	
	/**
	 * 
	 * @return  is succeed  (if it is false you will can not use other method)
	 * @throws SQLException when can not creat sql driver
	 * @throws ClassNotFoundException  when not find the driver
	 */
	public Boolean connectDb() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection(dbName, dbUser, dbPassword);
			isConnected=true;
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			errorMes=e.getMessage();
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			errorMes=e.getMessage();
			e.printStackTrace();
		}
		isConnected=false;
		return false;
	}
	
	/**
	 * close db (if you want use the db again just call connectDb)
	 */
	public void closeDb() {
		if(connection!=null){
			try {
				connection.close();
				isConnected=false;
				monitorRemove();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * get the connected state 
	 * @return is this sql driver connected
	 */
	public boolean getIsConnected() {
		return isConnected;
	}
	
	/**
	 * get now error message 
	 * @return error string
	 */
	public String getErrorMes() {
		return errorMes;
	}
	
	/**
	 * execute the sql (if you use it in you own function you have to close the statement and resultset )
	 * @param sql the sql 
	 * @return ResultSet result
	 * @throws SQLException 
	 */
	public ResultSet dbRunSql(String sql){
		 java.sql.Statement statement=null;
		try {
			statement = connection.createStatement();
			//java.sql.PreparedStatement pstmt = connection.prepareStatement(sql) ;   
		     //CallableStatement cstmt = connection.prepareCall("{CALL demoSp(? , ?)}") ;   
			 ResultSet rs = statement.executeQuery(sql);
			 return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			errorMes=e.getMessage();
			e.printStackTrace();
		}  
		/*
		finally{
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		*/
		return null;
	}
	
	/**
	 * execute the sql with index
	 * @param sql  sql string
	 * @param columnIndex  column Index (start with 1)
	 * @param rowIndex row Indx (start with 1)
	 * @return
	 */
	public String dbRunSql(String sql ,int columnIndex,int rowIndex) {
		ResultSet rs=null;
		try {
		    rs = dbRunSql(sql);
			if(rs.getMetaData().getColumnCount()<columnIndex)
			{
				findDbErrorMes("error columnIndex");
				return null;
			}
			if(!rs.absolute(rowIndex))
			{
				findDbErrorMes("error rowIndex");
				return null;
			}
			return(rs.getString(columnIndex));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		finally{
			if(rs!=null){
				try {
					rs.getStatement().close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * execute the sql with index and name
	 * @param sql sql string
	 * @param columnName  column Name
	 * @param rowIndx row Indx
	 * @return
	 */
	public String dbRunSql(String sql ,String columnName,int rowIndx) {
		ResultSet rs=null;
		try {
			rs = dbRunSql(sql);
			if(rs.findColumn(columnName)<0)
			{
				findDbErrorMes("error columnName");
				return null;
			}
			if(!rs.absolute(rowIndx))
			{
				findDbErrorMes("error rowIndex");
				return null;
			}
			return(rs.getString(columnName));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		finally{
			if(rs!=null){
				try {
					rs.getStatement().close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * only use for MyDbTool
	 * @param yourMes
	 */
	private void findDbErrorMes(String yourMes) {
		errorMes=yourMes;
		System.out.println(String.format("[MyDbTool] error => %s",yourMes));
	}

}
