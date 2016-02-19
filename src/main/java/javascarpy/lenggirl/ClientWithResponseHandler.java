/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package javascarpy.lenggirl;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates the use of the {@link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class ClientWithResponseHandler {

    public static void catchurl(String url,Date da) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);

         //   System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            }; 
            String responseBody = httpclient.execute(httpget, responseHandler);
//            System.out.println("----------------------------------------");
//            System.out.println(responseBody);
        //    System.out.println("----------------------------------------");
            String pattern="\"quantity\":(.*?),";
            String pattern1="<meta name=\"keywords\" content=\"(.*?)\"";
            String s=getStrings(responseBody,pattern,url);
            String s1=getStrings(responseBody,pattern1,url);
            new ClientWithResponseHandler().insertresult(Integer.parseInt(s), da, url,s1);
        } finally {
            httpclient.close();
        }
    }
    
    private static String getStrings(String restr,String pattern,String url) throws NumberFormatException, SQLException {
        String str = restr;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));            
        } 
        for (String s : strs){
            System.out.println(url+':'+s+'件');
            return s;
        }
		return null;        
    }
    
    protected  static void startRun(final ArrayList<String> url,int second){
        Timer timer = new Timer();
         TimerTask task =new TimerTask(){
             public void run(){
                 try {
                	 Date da=new Date();
                	 System.out.println(da+"\n300秒过去了");
                	 int length=url.size();
                	 for(int i=0;i<length;i++){
					catchurl(url.get(i),da);}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }
         };
      timer.scheduleAtFixedRate(task, new Date(),second);//当前时间开始起动 每次间隔2秒再启动
     // timer.scheduleAtFixedRate(task, 1000,2000); // 1秒后启动  每次间隔2秒再启动                 
     }

	public void insertresult(int num,Date date,String url,String s1) throws SQLException {
		PreparedStatement p = null;
		Mysql ss = null;
		try {
			ss = new Mysql();
			String sql = "INSERT INTO `sell`(`num`,`catchdate`,`url`,`storename`) VALUES(?,?,?,?)";
			p = ss.conn.prepareStatement(sql);
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=format.format(date); 
			p.setString(2, time);
			p.setInt(1, num);
			p.setString(3, url);
			p.setString(4, s1);
			p.execute();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			p.close();
			ss.closeall();
		}
	}
	
    public final static void main(String[] args){
    	//皮尔卡丹男士钱包真皮新款正品横款短款欧美时尚头
    	//5分鐘
//    	ArrayList<String> url=new ArrayList<String>();
    	ArrayList<String> url=ReadFromFile.readFileByLines("D:/catchurl.txt");
//    	url.add("https://detail.tmall.com/item.htm?id=42506928522");
//    	url.add("https://detail.tmall.com/item.htm?id=39641223235");
//    	url.add("https://detail.tmall.com/item.htm?id=521947893138");
//    	url.add("https://detail.tmall.com/item.htm?id=41441830796");
//    	url.add("https://detail.tmall.com/item.htm?id=44939419506");
    	startRun(url,300000);
    }
}

