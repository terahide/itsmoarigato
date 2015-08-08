package com.itsmoarigato.pages


class MyPage extends Menu{
	static url = '/my'
	static at = {title == 'it\'s more early got you - mypage'}
	static content = {
		"最初のメッセージの更新をクリックする"{
			waitFor{$('.edit',0) != null}
			$('.edit',0).click()
		}
		"最初のメッセージの削除をクリックする"{
			waitFor{$('.delete',0) != null}
			$('.delete',0).click()
		}
		"最初のメッセージの件名が'delete test'でないべき"{
			waitFor{$('.subject',0) != null}
			$('.subject',0).text() != 'delete test'
		}
	} 
}
