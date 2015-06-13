package com.itsmoarigato


import geb.spock.*

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

import com.itsmoarigato.Application;

import com.itsmoarigato.pages.HomePage
import com.itsmoarigato.pages.LoginPage
import spock.lang.Stepwise
import pages.*

import org.junit.Ignore;

@Stepwise
@ContextConfiguration(classes = Application, loader = SpringApplicationContextLoader)
@WebAppConfiguration
@IntegrationTest
@Ignore
class BootTests extends GebReportingSpec {

	def 'Unauthenticated user sent to log in page'() {
		when: 'unauthenticated user request protected page'
		via HomePage
		then: 'sent to the log in page'
		at LoginPage
		assert 1 == 1
	}

	def 'Log in views home page'() {
		when: 'log in successfully'
		login()
		then: 'sent to original page'
		at HomePage
//		and: 'the username is displayed'
//		username == 'takashi'
		and: 'Spring Session Management is being used'
		driver.manage().cookies.find { it.name == 'SESSION' }
		and: 'Standard Session is NOT being used'
		!driver.manage().cookies.find { it.name == 'JSESSIONID' }
	}

	def 'Log out success'() {
		when:
		logout()
		then:
		at LoginPage
	}

	def 'Logged out user sent to log in page'() {
		when: 'logged out user request protected page'
		via HomePage
		then: 'sent to the log in page'
		at LoginPage
	}
}