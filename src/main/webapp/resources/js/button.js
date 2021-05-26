$(function() {


	$('#delete').click(function() {
		if (!confirm('本当に削除しますか？')) {
			/* キャンセルの時の処理 */
			window.alert('キャンセルされました');
			return false;
		} else {

		}
	});

	//貸し出し状況を読みこむ
	$('.lending').ready(function() {

		var lendingstatus = $('.lending').text();

		// 貸し出し中だった場合
		if (lendingstatus == '貸し出し中') {
			// ボタンを無効化
			$('#rent').prop('disabled', true);
			$('#return').prop('disabled', false);
			$('#delete').prop('disabled', true);
			$('#edit').prop('disabled', true);
		} else {
			// ボタンを有効化
			$('#rent').prop('disabled', false);
			$('#return').prop('disabled', true);
			$('#delete').prop('disabled', false);
			$('#edit').prop('disabled', false);
		}
	});

	$(window).scroll(function() {

		if ($(this).scrollTop() > 100) {

			$('#page-top').fadeIn();

		} else {

			$('#page-top').fadeOut();
		}
	})
});