package jp.co.seattle.library.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class AddBooksController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    // インスタンス作成してる
    @Autowired
    private BooksService booksService;
    @Autowired
    private ThumbnailService thumbnailService;

    // 入力された値を取得し、"createAccount"に渡す
    @RequestMapping(value = "/addBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "addBook";
    }

    /**
     * 書籍情報を登録する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param description 説明文
     * @param author 著者名
     * @param publisher 出版社
     * @param publishDate 出版日
     * @param isbn ISBN
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/insertBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String insertBook(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("isbn") String isbn,
            @RequestParam("thumbnail") MultipartFile file,
            Model model) {

        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setDescription(description);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);



        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        boolean isValidISBN = isbn.matches("[0-9]{0}|[0-9]{10}|[0-9]{13}");
        boolean flag = false;

        // todo 出版日の形式を指定(yyyymmdd)

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(publishDate);


        } catch (ParseException e) {
            model.addAttribute("publishDateError", "出版日はyyyyMMdd形式で入力してください。");
            flag = true;
        }

        //　todo ISBNの文字形式の指定

        if (!(isValidISBN)) {
            model.addAttribute("ISBNError", "10-13桁の半角数字で入力してください。");
            flag = true;
        }


        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);

            }
        }

        if (flag) {
            return "addBook";
        }

        // 書籍情報を新規登録する
        booksService.registBook(bookInfo);

        model.addAttribute("resultMessage", "登録完了");

        // TODO 登録した書籍の詳細情報を表示するように実装
        
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(booksService.getBookId()));

        //  詳細画面に遷移する
        return "details";
    }

}
