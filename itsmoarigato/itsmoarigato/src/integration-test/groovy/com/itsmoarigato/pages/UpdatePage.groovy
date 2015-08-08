package com.itsmoarigato.pages


class UpdatePage extends Menu{
	static url = '/update/'
	static at = {title == 'it\'s more early got you - update'}
	static content = {
		"項目を入力して更新する"{
			$('#toUserId').value("takashi@hoge.co.jp")
			$('#subject').value("今日もありがと")
			$('#message').value("ムリしないでね:)")
			$('#submit').click()
			waitFor{ $('#result').text() == "更新されました!" }
		}
		"更新されました!と表示されるべき"{
			$('#result').text() == "更新されました!"
		}
	}
}
