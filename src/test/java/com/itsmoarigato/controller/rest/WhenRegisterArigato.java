package com.itsmoarigato.controller.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;



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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import util.HttpTestHelper;

public class WhenRegisterArigato {

	@Test
	public void 一覧を取得するテスト() throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/",params);
		assertThat(response, is("[{\"id\":0,\"fromUser\":null,\"toUser\":null,\"subject\":null,\"contents\":null,\"created\":null,\"images\":null}]"));
	}

	@Test
	public void 詳細を取得するテスト() throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/1",params);
		assertThat(response, is("{\"id\":1,\"fromUser\":null,\"toUser\":null,\"subject\":null,\"contents\":null,\"created\":null,\"images\":null}"));
	}

	@Test
	public void PUTするテスト() throws Exception {
		Map<String,String> params = new LinkedHashMap<>();
		params.put("fromUserId","bucho@hoge.co.jp"); 
		params.put("toUserId" ,"takashi@hoge.co.jp");
		params.put("subject" ,"いつもありがと");
		params.put("message" ,"今日も頑張ってるね:)");

		String response = put("/rest/arigato/",params);
		assertThat(response, is("{\"sucsses\":true}"));
	}

	@Test
	public void POSTするテスト() throws Exception {
		Map<String,String> params = new LinkedHashMap<>();
		params.put("fromUserId","bucho@hoge.co.jp"); 
		params.put("toUserId" ,"takashi@hoge.co.jp");
		params.put("subject" ,"いつもありがと");
		params.put("message" ,"今日も頑張ってるね:)");

		String response = post("/rest/arigato/1",params);
		assertThat(response, is("{\"sucsses\":true}"));
	}

	private static String get(String path, Map<String, String> params) throws Exception {
		return HttpTestHelper.get("http","localhost",8080, path, params);
	}

	private static String put(String path, Map<String, String> params) throws Exception {
		return HttpTestHelper.put("http","localhost",8080, path, params);
	}

	private static String post(String path, Map<String, String> params) throws Exception {
		return HttpTestHelper.post("http","localhost",8080, path, params);
	}
}
