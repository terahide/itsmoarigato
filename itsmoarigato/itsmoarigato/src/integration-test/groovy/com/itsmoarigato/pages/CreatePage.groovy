package com.itsmoarigato.pages

import geb.Page

class CreatePage extends Page{
	static url = '/create'
	static at = {title == 'it\'s more early got you - create'}
	static content = {
		"項目を入力して登録する"{
			$('#toUserId') << "takashi@hoge.co.jp"
			$('#subject') << "いつもありがと"
			$('#message') << "今日も頑張ってるね:)"
			withAlert(wait:true){$('#submit').click()} == "ご登録ありがとうございました!"
		}
	}
}
