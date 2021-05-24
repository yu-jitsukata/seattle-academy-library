package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class RequestBookController {
    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * requestボタンから書籍リクエスト画面に遷移
     * @param model
     * @return
     */
    @RequestMapping(value = "/requestBook", method = RequestMethod.GET)
    public String transitionrequestBook(
            Model model) {

        if (booksService.getRequestList().isEmpty()) {
            model.addAttribute("noRequest", "現在届いているリクエストはありません。");
            return "requestBook";
        } else {
            model.addAttribute("requestbookList", booksService.getRequestList());

            return "requestBook";
        }

    }

    /**
     * 書籍のリクエスト
     *
     * @param requestTitle
     * @return 書籍リクエスト画面に遷移
     */
    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public String request(
            @RequestParam("requestTitle") String requestTitle,
            Model model) {

        // もし何も入力されず送信ボタンが押された場合の処理
        if (requestTitle.isEmpty()) {
            model.addAttribute("requestbookList", booksService.getRequestList());
            model.addAttribute("request_error", "書籍名を入力してください。");
            return "requestBook";
        }

        // リクエスト済だった場合はリクエスト回数だけ増やす処理を行う
        try {
            booksService.requestBook(requestTitle);

        } catch (DuplicateKeyException d) {
            booksService.requestCounts(requestTitle);
        }

        // リクエスト一覧を出力
        // リクエスト送信完了の旨を出力
        model.addAttribute("requestbookList", booksService.getRequestList());
        model.addAttribute("requestComplete", "入力ありがとう！リクエストを送信しました。");
        return "requestBook";

}
}
