package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * ログインコントローラー
 */
@Controller /** APIの入り口 */
public class SearchBooksController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/searchBook", method = RequestMethod.GET)

    public String first(Model model) {
        return "searchBook"; //jspファイル名
    }

    /**
     * 書籍の検索
     *
     * @param searchtitle
     * @return　書籍検索画面に遷移
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String login(
            @RequestParam("searchTitle") String searchTitle,
            Model model) {
        


        if (booksService.getMatchBooks(searchTitle).isEmpty()) {
            model.addAttribute("noHit", "該当する書籍が見つかりませんでした。もう一度入力してください。");
            return "searchBook";
        }


        // 検索してヒットした本の情報を取得して画面側に渡す
        model.addAttribute("searchBooksList", booksService.getMatchBooks(searchTitle));
        return "searchBook";

}
}
