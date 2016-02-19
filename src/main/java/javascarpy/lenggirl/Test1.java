package javascarpy.lenggirl;

import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpRequest;

public class Test1 {
	public static void main(String args[]){
		HttpRequest request = new BasicHttpRequest("GET", "www.baidu.com", HttpVersion.HTTP_1_1);
		System.out.println(request.getRequestLine().getMethod());
		System.out.println(request.getRequestLine().getUri());
		System.out.println(request.getProtocolVersion());
		System.out.println(request.getRequestLine().toString());
	}
}
