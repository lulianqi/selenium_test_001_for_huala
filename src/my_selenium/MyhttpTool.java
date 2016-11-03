package my_selenium;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MyhttpTool {
	
	
	/**
	 * set Property so the fidder can catch it
	 */
	public static void setProperty() {
		// set http proxy
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8888");
	}
	
	/**
     * get http
     * 
     * @param url
     *            URL
     * @param param
     *            param like name1=value1&name2=value2 
     * @param heads
     *            the http heads (some head can not set in this way)
     * @return URL 
     * 				restult
     */
    public static String sendGet(String url, String param ,HashMap<String, String>heads) {
        String result = "";
        BufferedReader in = null;
        try {
        	String urlNameString=url;
        	if(param !=null)
        	{
        		urlNameString += "?" + param;
        	}
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //connection.setRequestProperty("host", "10.10.140.10");
            
            if(heads !=null)
            {
            	Iterator iter = heads.entrySet().iterator();
            	while (iter.hasNext()) {
            		Map.Entry entry = (Map.Entry) iter.next();
            		connection.setRequestProperty((String)entry.getKey(),(String)entry.getValue());
				}
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post http
     * 
     * @param url
     *            URL
     * @param param
     *            param like name1=value1&name2=value2 
     * @param heads
     *            the http heads (some head can not set in this way)
     * @return URL 
     * 				restult
     */
    public static String sendPost(String url, String param,HashMap<String, String>heads) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            if(heads !=null)
            {
            	Iterator iter = heads.entrySet().iterator();
            	while (iter.hasNext()) {
            		Map.Entry entry = (Map.Entry) iter.next();
            		conn.setRequestProperty((String)entry.getKey(),(String)entry.getValue());
				}
            }
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    } 
}
