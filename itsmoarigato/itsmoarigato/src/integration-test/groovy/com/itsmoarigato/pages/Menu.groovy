package com.itsmoarigato.pages

import geb.Page

abstract class Menu extends Page{
	static content = {
		"新規登録リンクをクリックする"{
			$('#toCreate').click()
		}
		"自分のアカウントをクリックする"{
			$('#me').click()
		}
	}
}
