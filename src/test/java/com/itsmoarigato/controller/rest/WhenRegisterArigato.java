package com.itsmoarigato.controller.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsmoarigato.Message;

import util.HttpTestHelper;

public class WhenRegisterArigato {

	@Test
	public void test() throws Exception{
		PUTするテスト();
		int id = 一覧を取得するとn件のテスト();
		詳細を取得するテスト(id);
		POSTするテスト(id);
		更新後の詳細を取得するテスト(id);
	}
	private void 一覧を取得すると０件のテスト() throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/",params);
		assertThat(response, is("[]"));
	}

	private int 一覧を取得するとn件のテスト() throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/",params);
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String,Object>> messages = mapper.readValue(response,  new TypeReference<List<Map<String,Object>>>() {});
		assertThat(messages.size(), greaterThan(0));
		Map<String, Object> message = messages.get(0);
		Map<String, Object> fromUser = (Map<String, Object>)message.get("fromUser");
		Map<String, Object> toUser = (Map<String, Object>)message.get("toUser");
		assertThat(fromUser.get("email"), is("bucho@hoge.co.jp"));
		assertThat(toUser.get("email"), is("takashi@hoge.co.jp"));
		assertThat(message.get("subject"), is("いつもありがと"));
		assertThat(message.get("contents"), is("今日も頑張ってるね:)"));
		return (int)message.get("id");
	}

	private void 詳細を取得するテスト(int id) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/"+id,params);
		ObjectMapper mapper = new ObjectMapper();

		Map<String,Object> message = mapper.readValue(response,  new TypeReference<Map<String,Object>>() {});
		Map<String, Object> fromUser = (Map<String, Object>)message.get("fromUser");
		Map<String, Object> toUser = (Map<String, Object>)message.get("toUser");
		assertThat(fromUser.get("email"), is("bucho@hoge.co.jp"));
		assertThat(toUser.get("email"), is("takashi@hoge.co.jp"));
		assertThat(message.get("subject"), is("いつもありがと"));
		assertThat(message.get("contents"), is("今日も頑張ってるね:)"));
		assertThat(message.get("id"), is(id));
	}

	private void 更新後の詳細を取得するテスト(int id) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/"+id,params);
		ObjectMapper mapper = new ObjectMapper();

		Map<String,Object> message = mapper.readValue(response,  new TypeReference<Map<String,Object>>() {});
		Map<String, Object> fromUser = (Map<String, Object>)message.get("fromUser");
		Map<String, Object> toUser = (Map<String, Object>)message.get("toUser");
		assertThat(fromUser.get("email"), is("bucho@hoge.co.jp"));
		assertThat(toUser.get("email"), is("takashi@hoge.co.jp"));
		assertThat(message.get("subject"), is("今日もありがと"));
		assertThat(message.get("contents"), is("ムリしないでね:)"));
		assertThat(message.get("id"), is(id));
	}

	private void PUTするテスト() throws Exception {
		Map<String,String> params = new LinkedHashMap<>();
		params.put("fromUserId","bucho@hoge.co.jp"); 
		params.put("toUserId" ,"takashi@hoge.co.jp");
		params.put("subject" ,"いつもありがと");
		params.put("message" ,"今日も頑張ってるね:)");

		String response = put("/rest/arigato/",params);
		assertThat(response, is("{\"sucsses\":true}"));
	}

	private void POSTするテスト(int id) throws Exception {
		Map<String,String> params = new LinkedHashMap<>();
		params.put("fromUserId","bucho@hoge.co.jp"); 
		params.put("toUserId" ,"takashi@hoge.co.jp");
		params.put("subject" ,"今日もありがと");
		params.put("message" ,"ムリしないでね:)");

		String response = post("/rest/arigato/"+id,params);
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
