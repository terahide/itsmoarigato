package com.itsmoarigato.pages


/**
 * The home page
 *
 * @author Rob Winch
 */
class HomePage extends Menu {
	static url = '/'
	static at = { assert driver.title == "it's more early got you - main"; true}
	static content = {
		username { $('#un').text() }
		logout(to:LoginPage) { $('input[type=submit]').click() }
		"最初のメッセージの更新をクリックする"{
			$('.edit',0).click()
		}
	}
}
