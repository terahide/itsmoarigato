package com.itsmoarigato.pages

import geb.*

class LoginPage extends Page {
	static url = '/login'
	static at = { assert driver.title == 'Login Page'; true}
	static content = {
		form { $('form') }
		submit { $('input[type=submit]') }
		login(required:false) { user='takashi@hoge.co.jp', pass='password' ->
			form.username = user
			form.password = pass
			submit.click(HomePage)
		}
	}
}
