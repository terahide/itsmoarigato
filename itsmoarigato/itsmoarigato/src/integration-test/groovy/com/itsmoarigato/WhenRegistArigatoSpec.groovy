package com.itsmoarigato

import groovy.json.*
import geb.spock.GebReportingSpec;

import com.itsmoarigato.pages.LoginPage
import com.itsmoarigato.pages.HomePage

class WhenRegistArigatoSpec extends GebReportingSpec {
	def "ちょっとやってみた"(){
		when: 'firstAccess'
			go "http://localhost:8080"
		then:
			at LoginPage
		
		when: 'log in successfully'
			login()
		then: 'sent to original page'
			at HomePage
//		and: 'the username is displayed'
//			username == 'takashi'
			
		when: "rest list access nodata"	
			go "http://localhost:8080/rest/arigato"
		then:
			$("pre").text().startsWith("[")

		when: "ありがとを登録すると"	
			go "http://localhost:8080/create"
			$('#toUserId') << "takashi@hoge.co.jp"
			$('#subject') << "いつもありがと"
			$('#message') << "今日も頑張ってるね:)"
			$('#submit').click()
			waitFor{ $('#result').text() == "ご登録ありがとうございました!" }
		then: "sucessと表示されるべき"
			$('#result').text() == "ご登録ありがとうございました!"
		when:"rest list access one data"	
			go "http://localhost:8080/"
			go "http://localhost:8080/rest/arigato"
			waitFor { $("pre").text().startsWith("[") }
		then: 
			def slurper = new JsonSlurper()
			def root = slurper.parseText($("pre").text())
			root.size >= 1
			def arigatoId = root[0]['id']
			root[0]['fromUser']['email'] == "takashi@hoge.co.jp"
			root[0]['toUser']['email'] == "takashi@hoge.co.jp"
			root[0]['subject'] == "いつもありがと"
			root[0]['contents'] == "今日も頑張ってるね:)"
			
		when: "登録したありがとを更新すると"	
			go "http://localhost:8080/update/"+arigatoId
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
			root['fromUser']['email'] == "takashi@hoge.co.jp"
			root['toUser']['email'] == "takashi@hoge.co.jp"
			root['subject'] == "今日もありがと"
			root['contents'] == "ムリしないでね:)"
	}

	def "入力に誤りがある場合"(){
		when: 'firstAccess'
			go "http://localhost:8080"
		then:
			at LoginPage
		
		when: 'log in successfully'
			login()
		then: 'sent to original page'
			at HomePage
//		and: 'the username is displayed'
//			username == 'takashi'
			
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
			waitFor{ $('#errors').text() != "" }
		then: "Validation failed!と表示されるべき"
			$('#errors').text() == "入力エラーがあります。ご確認ください"
			$('#toUserId_error').text() == "入力してください"
			$('#message_error').text() == "入力してください"
			$('#subject_error').text() == "入力してください"
			$('#toUserId_error').text() == "入力してください"

		when: "ありがとを入力ミスで更新すると"
			go "http://localhost:8080/update/"+arigatoId
			$('#submit').click()
			waitFor{ $('#result').text() == "Validation failed!" }
		then: "Validation failed!と表示されるべき"
			$('#result').text() == "Validation failed!"
			$('#errors').text().contains("message:入力してください")
			$('#errors').text().contains("subject:入力してください")
			$('#errors').text().contains("toUserId:入力してください")
	}
}
