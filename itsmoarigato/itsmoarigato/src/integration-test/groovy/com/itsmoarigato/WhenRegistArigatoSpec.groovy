package com.itsmoarigato

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
		and: 'the username is displayed'
			username == 'takashi@hoge.co.jp'
			
		when: "rest list access nodata"	
			go "http://localhost:8080/rest/arigato"
		then:
			driver.pageSource == '[]'

//		when: "put"	
//			put "http://localhost:8080/rest/arigato?fromId=bucho@hoge.co.jp&toUserId=takashi@hoge.co.jp&subject=いつもありがと&message=今日も頑張ってるね:)"
//			params.put("fromUserId","bucho@hoge.co.jp");
//			params.put("toUserId" ,"takashi@hoge.co.jp");
//			params.put("subject" ,"いつもありがと");
//			params.put("message" ,"今日も頑張ってるね:)");
//		then:
//			driver.pageSource == '[]'
	}
}
