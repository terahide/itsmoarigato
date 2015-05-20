package util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMethod;

public class HttpTestHelper {
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
		HttpClient httpclient = HttpClients.createDefault();
        URI uri = toUri(scheme,host,port,path,params);

		HttpRequestBase request = toRequest(method,uri) ;
		
		HttpResponse response_ = httpclient.execute(request);
		HttpEntity entity = response_.getEntity();
		String response = EntityUtils.toString(entity);
		return response;
	}

	private static URI toUri(String scheme, String host, int port, String path,
			Map<String, String> params) throws URISyntaxException {
		URIBuilder builder = new URIBuilder()
        .setScheme(scheme)
        .setHost(host)
        .setPort(port)
        .setPath(path);
		
		for(Entry<String, String> e:params.entrySet()){
			builder.setParameter(e.getKey(), e.getValue());
		}
		
        URI uri = builder.build();
		return uri;
	}

	private static HttpRequestBase toRequest(RequestMethod method,URI uri) {
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
		return request;
	}
}
