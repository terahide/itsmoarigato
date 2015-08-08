package com.itsmoarigato.pages

class CreatePage extends Menu{
	static url = '/create'
	static at = {title == 'it\'s more early got you - create'}
	static content = {
		"項目を入力して登録する"{
			subject='いつもありがと' -> 
			$('#toUserId') << 'takashi@hoge.co.jp'
			$('#subject') << subject
			$('#message') << '今日も頑張ってるね:)'
			withAlert(wait:true){$('#submit').click()} == 'ご登録ありがとうございました!'
		}
	}
}
