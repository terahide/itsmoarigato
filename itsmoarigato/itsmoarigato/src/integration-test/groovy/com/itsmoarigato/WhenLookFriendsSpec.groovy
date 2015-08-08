package com.itsmoarigato

import geb.spock.GebReportingSpec

import com.itsmoarigato.pages.*

class WhenLookFriendsSpec extends GebReportingSpec{
	def "友達のrestのjsonを見ると以下になるべき"(){
		given: 'loginした状態で'
			via HomePage
			at LoginPage
			login()
			at HomePage
		when: 
			go "http://localhost:8080/rest/user/takashi@hoge.co.jp/friend"
		then:
			waitFor { $("pre").text().startsWith("[") }
	}
}
