package com.itsmoarigato.pages

import geb.*

/**
 * The home page
 *
 * @author Rob Winch
 */
class HomePage extends Page {
	static url = ''
	static at = { assert driver.title == "it's more early got you - main"; true}
	static content = {
		username { $('#un').text() }
		logout(to:LoginPage) { $('input[type=submit]').click() }
	}
}
