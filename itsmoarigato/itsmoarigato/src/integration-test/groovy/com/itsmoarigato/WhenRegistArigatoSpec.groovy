package com.itsmoarigato

import groovy.json.*
import geb.spock.GebReportingSpec;

import com.itsmoarigato.pages.LoginPage
import com.itsmoarigato.pages.HomePage

class WhenRegistArigatoSpec extends GebReportingSpec {
	def "ちょっとやってみた"(){
		when: 'firstAccess'
			go "http://localhost:8080"
//		then:
//			at LoginPage
//		
//		when: 'log in successfully'
//			login()
		then: 'sent to original page'
			at HomePage
//		and: 'the username is displayed'
//			username == 'takashi@hoge.co.jp'
			
		when: "rest list access nodata"	
			go "http://localhost:8080/rest/arigato"
		then:
			$("pre").text().startsWith("[")

		when: "ありがとを登録すると"	
			go "http://localhost:8080/create"
			$('#fromUserId') << "bucho@hoge.co.jp"
			$('#toUserId') << "takashi@hoge.co.jp"
			$('#subject') << "いつもありがと"
			$('#message') << "今日も頑張ってるね:)"
			$('#submit').click()
			waitFor{ $('#result').text() == "registed!" }
		then: "sucessと表示されるべき"
			$('#result').text() == "registed!"
		when:"rest list access one data"	
			go "http://localhost:8080/"
			go "http://localhost:8080/rest/arigato"
			waitFor { $("pre").text().startsWith("[") }
		then: 
			def slurper = new JsonSlurper()
			def root = slurper.parseText($("pre").text())
			root.size >= 1
			def arigatoId = root[0]['id']
			root[0]['fromUser']['email'] == "bucho@hoge.co.jp"
			root[0]['toUser']['email'] == "takashi@hoge.co.jp"
			root[0]['subject'] == "いつもありがと"
			root[0]['contents'] == "今日も頑張ってるね:)"
			
		when: "登録したありがとを更新すると"	
			go "http://localhost:8080/update/"+arigatoId
			$('#fromUserId') << "bucho@hoge.co.jp"
			$('#toUserId') << "takashi@hoge.co.jp"
			$('#subject') << "今日もありがと"
			$('#message') << "ムリしないでね:)"
			$('#submit').click()
			waitFor{ $('#result').text() == "updated!" }
		then: "sucessと表示されるべき"
			$('#result').text() == "updated!"
		when: "登録したありがとを表示すると"	
			go "http://localhost:8080/"
			go "http://localhost:8080/rest/arigato/"+arigatoId
		and:
			root = slurper.parseText($("pre").text())
		then: 
			root['fromUser']['email'] == "bucho@hoge.co.jp"
			root['toUser']['email'] == "takashi@hoge.co.jp"
			root['subject'] == "今日もありがと"
			root['contents'] == "ムリしないでね:)"
	}

	def "入力に誤りがある場合"(){
		when: 'firstAccess'
			go "http://localhost:8080"
//		then:
//			at LoginPage
//		
//		when: 'log in successfully'
//			login()
		then: 'sent to original page'
			at HomePage
//		and: 'the username is displayed'
//			username == 'takashi@hoge.co.jp'
			
		when:"rest list access one data"	
			go "http://localhost:8080/"
			go "http://localhost:8080/rest/arigato"
			waitFor { $("pre").text().startsWith("[") }
		then: 
			def slurper = new JsonSlurper()
			def root = slurper.parseText($("pre").text())
			root.size >= 1
			def arigatoId = root[0]['id']

		when: "ありがとを入力ミスで登録すると"	
			go "http://localhost:8080/create"
			$('#submit').click()
			waitFor{ $('#result').text() == "Validation failed!" }
		then: "Validation failed!と表示されるべき"
			$('#result').text() == "Validation failed!"
			$('#errors').text().contains("fromUserId:may not be empty")
			$('#errors').text().contains("fromUserId:may not be empty")
			$('#errors').text().contains("message:may not be empty")
			$('#errors').text().contains("subject:may not be empty")
			$('#errors').text().contains("toUserId:may not be empty")

		when: "ありがとを入力ミスで更新すると"
			go "http://localhost:8080/update/"+arigatoId
			$('#submit').click()
			waitFor{ $('#result').text() == "Validation failed!" }
		then: "Validation failed!と表示されるべき"
			$('#result').text() == "Validation failed!"
			$('#errors').text().contains("fromUserId:may not be empty")
			$('#errors').text().contains("fromUserId:may not be empty")
			$('#errors').text().contains("message:may not be empty")
			$('#errors').text().contains("subject:may not be empty")
			$('#errors').text().contains("toUserId:may not be empty")

	}
}
