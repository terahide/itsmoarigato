package com.itsmoarigato;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.junit.Ignore;
import org.junit.Test;

import util.HttpTestHelper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Ignore //FIXME POSTとPUTのテストがかけたら消すこと
public class WhenRegisterArigato {

	@Test
	public void test() throws Exception{
//		login();
		PUTするテスト();
		int id = 一覧を取得するとn件のテスト();
		詳細を取得するテスト(id);
		POSTするテスト(id);
		更新後の詳細を取得するテスト(id);
	}
	
	private void login() throws Exception{
		login("takashi@hoge.co.jp","password");
	}
	
	private void login(String user, String password) throws Exception {
		String loginPage = get("/", new HashMap<String,String>());
		
		Map<String, String> p = new HashMap<String,String>();
		p.put("username", user);
		p.put("password", password);
		p.put("_csrf", toCSRF(loginPage));
		String respose = post("/login", p);
		System.out.println(respose);
	}
	
	private String toCSRF(String html) {
		Pattern pattern = Pattern.compile("input name=\"_csrf\" type=\"hidden\" value=\"(.*)\"");
		Matcher matcher = pattern.matcher(html);
		if( ! matcher.find()){
			fail("");
		}
		String csrf = matcher.group();
		pattern = Pattern.compile("value=\"(.*)\"");
		matcher = pattern.matcher(csrf);
		if( ! matcher.find()){
			fail("");
		}
		csrf = matcher.group();
		csrf = csrf.substring("value=\"".length(),csrf.length()-1);
		return csrf;
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
		assertThat((String)fromUser.get("email"), is("bucho@hoge.co.jp"));
		assertThat((String)toUser.get("email"), is("takashi@hoge.co.jp"));
		assertThat((String)message.get("subject"), is("いつもありがと"));
		assertThat((String)message.get("contents"), is("今日も頑張ってるね:)"));
		return (int)message.get("id");
	}

	private void 詳細を取得するテスト(int id) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/"+id,params);
		ObjectMapper mapper = new ObjectMapper();

		Map<String,Object> message = mapper.readValue(response,  new TypeReference<Map<String,Object>>() {});
		Map<String, Object> fromUser = (Map<String, Object>)message.get("fromUser");
		Map<String, Object> toUser = (Map<String, Object>)message.get("toUser");
		assertThat((String)fromUser.get("email"), is("bucho@hoge.co.jp"));
		assertThat((String)toUser.get("email"), is("takashi@hoge.co.jp"));
		assertThat((String)message.get("subject"), is("いつもありがと"));
		assertThat((String)message.get("contents"), is("今日も頑張ってるね:)"));
		assertThat((int)message.get("id"), is(id));
	}

	private void 更新後の詳細を取得するテスト(int id) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		String response = get("/rest/arigato/"+id,params);
		ObjectMapper mapper = new ObjectMapper();

		Map<String,Object> message = mapper.readValue(response,  new TypeReference<Map<String,Object>>() {});
		Map<String, Object> fromUser = (Map<String, Object>)message.get("fromUser");
		Map<String, Object> toUser = (Map<String, Object>)message.get("toUser");
		assertThat((String)fromUser.get("email"), is("bucho@hoge.co.jp"));
		assertThat((String)toUser.get("email"), is("takashi@hoge.co.jp"));
		assertThat((String)message.get("subject"), is("今日もありがと"));
		assertThat((String)message.get("contents"), is("ムリしないでね:)"));
		assertThat((int)message.get("id"), is(id));
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
		//TODO putがうまく行かないから一旦postで作業を続ける
		return HttpTestHelper.post("http","localhost",8080, path, params);
	}

	private static String post(String path, Map<String, String> params) throws Exception {
		return HttpTestHelper.post("http","localhost",8080, path, params);
	}
}
