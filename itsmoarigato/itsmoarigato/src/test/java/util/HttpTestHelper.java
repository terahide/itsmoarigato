package util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMethod;

public class HttpTestHelper {
	private static HttpClient httpClient;
	private static HttpContext httpContext;
	private static String sessionID;
	static {
		httpClient = new DefaultHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

//		Credentials credentials = new UsernamePasswordCredentials("user","password");
//		AuthScope scope = new AuthScope(host, port);
//		((DefaultHttpClient)httpClient).getCredentialsProvider().setCredentials(scope, credentials);
	}
	
	public static String get(String scheme, String host, int port, String path, Map<String,String> params) throws Exception{
		return send(RequestMethod.GET, scheme, host, port, path, params);
	}

	public static String put(String scheme, String host, int port, String path,Map<String,String> params) throws Exception{
		return send(RequestMethod.PUT, scheme, host, port, path, params);
	}

	public static String post(String scheme, String host, int port, String path,Map<String,String> params) throws Exception{
		return send(RequestMethod.POST, scheme, host, port, path, params);
	}

	public static String send(RequestMethod method,String scheme, String host, int port, String path,Map<String,String> params) throws Exception{
		URI uri = toUri(method,scheme,host,port,path,params);

		HttpRequestBase request = toRequest(method,uri,params) ;
		if(sessionID != null)request.addHeader("Cookie", sessionID);
		
		HttpResponse response_ = httpClient.execute(request,httpContext);
		Header header = response_.getFirstHeader("Set-Cookie");
		if(header != null)sessionID = header.getValue();

		HttpEntity entity = response_.getEntity();
		String response = EntityUtils.toString(entity);
		return response;
	}

	private static URI toUri(RequestMethod method,String scheme, String host, int port, String path,
			Map<String, String> params) throws URISyntaxException {
		URIBuilder builder = new URIBuilder()
        .setScheme(scheme)
        .setHost(host)
        .setPort(port)
        .setPath(path);
		
		if(method == RequestMethod.GET){
			for(Entry<String, String> e:params.entrySet()){
				builder.setParameter(e.getKey(), e.getValue());
			}
		}
		
        URI uri = builder.build();
		return uri;
	}
	
	private static HttpRequestBase toRequest(RequestMethod method,URI uri,Map<String, String> params) throws Exception {
		HttpRequestBase request;
		if(method == RequestMethod.GET){
			request = new HttpGet(uri);
		}else if(method == RequestMethod.PUT){
			request = new HttpPut(uri);
		}else if(method == RequestMethod.POST){
			request = new HttpPost(uri);
		}else{
			throw new UnsupportedOperationException();
		}
		
		if(request instanceof HttpEntityEnclosingRequestBase){
			((HttpEntityEnclosingRequestBase)request).setEntity(toEntity(params));
		}
		
		return request;
	}

	private static HttpEntity toEntity(Map<String, String> params) throws Exception {
		return new UrlEncodedFormEntity(toList(params), HTTP.UTF_8);
	}

	private static List<? extends NameValuePair> toList(
			Map<String, String> params) {
		List<NameValuePair> list = new ArrayList<>();
		for(Entry<String, String> e:params.entrySet()){
			list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
		return list;
	}
}
