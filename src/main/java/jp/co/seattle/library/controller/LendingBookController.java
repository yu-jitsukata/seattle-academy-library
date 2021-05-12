package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * 貸し出しコントローラー
 */
@Controller //APIの入り口
public class LendingBookController {
    final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

    @Autowired
    private BooksService booksService;


    /**
     * 対象書籍を貸し出し中にする
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    @RequestMapping(value = "/borrowBook", method = RequestMethod.POST)
    public String lendingBook(
            Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        // 最新の本の貸し出し状況を取得=idが一回以上入ってるということになる
        booksService.lendingBookCountCheck(bookId);

        // 本の情報と、貸し出し状況をのせた詳細画面へ遷移
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        // カウントして1以上が返ってきた場合は貸し出し中を表示
        if (booksService.lendingBookCountCheck(bookId) == 1) {
            model.addAttribute("lending", booksService.getLendingBookInfo(bookId));
            return "details";

        } else {
            booksService.lendingBook(bookId);
            model.addAttribute("lending", booksService.getLendingBookInfo(bookId));
            return "details";

        }

    }

}
