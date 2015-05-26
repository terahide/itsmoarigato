package com.itsmoarigato

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
			driver.pageSource != '[]'

//		when: "put"	
//			go "http://localhost:8080/create"
//			$('#fromUserId') <- "bucho@hoge.co.jp"
//			$('#toUserId') <- "takashi@hoge.co.jp"
//			$('#subject') <- "いつもありがと"
//			$('#message') <- "今日も頑張ってるね:)"
//			$('#submit').click()
//		then:
//			driver.pageSource == '[]'
	}
}
