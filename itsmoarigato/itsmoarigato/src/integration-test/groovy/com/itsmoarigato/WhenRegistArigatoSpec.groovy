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
			driver.pageSource.startsWith("[")

		when: "ありがとを登録すると"	
			go "http://localhost:8080/create"
			$('#fromUserId') << "bucho@hoge.co.jp"
			$('#toUserId') << "takashi@hoge.co.jp"
			$('#subject') << "いつもありがと"
			$('#message') << "今日も頑張ってるね:)"
			$('#submit').click()
		then: "sucessと表示されるべき"
			driver.pageSource == '{"sucsses":true}'
		when:"rest list access one data"	
			go "http://localhost:8080/"
			go "http://localhost:8080/rest/arigato"
			waitFor { driver.pageSource.startsWith("[") }
		then: 
			def slurper = new JsonSlurper()
			def root = slurper.parseText(driver.pageSource)
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
		then: "sucessと表示されるべき"
			driver.pageSource == '{"sucsses":true}'

		when: "登録したありがとを表示すると"	
			go "http://localhost:8080/"
			go "http://localhost:8080/rest/arigato/"+arigatoId
		and:
			root = slurper.parseText(driver.pageSource)
		then: 
			root['fromUser']['email'] == "bucho@hoge.co.jp"
			root['toUser']['email'] == "takashi@hoge.co.jp"
			root['subject'] == "今日もありがと"
			root['contents'] == "ムリしないでね:)"
	}
}
