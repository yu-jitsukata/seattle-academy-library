package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkRegistrationController {
    //ロギング（起こった出来事についての情報などを時間経過にそって記録する履歴）
    final static Logger logger = LoggerFactory.getLogger(BulkRegistrationController.class);

    // インスタンス作成してる
    @Autowired
    private BooksService booksService;

    /**
     *  入力された値を取得し、bulkRegistrationに渡す
     * 
     */
    @RequestMapping(value = "/bulkRegistration", method = RequestMethod.GET) //value＝actionで指定したパラメータ

    //RequestParamでname属性を取得
    public String login(Model model) {
        return "bulkRegistration";
    }

    /**
     *  書籍情報を一括登録する
     * @param upload_file
     */
    @Transactional
    @RequestMapping(value = "/bulkRegistrationBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String insertBook(Locale locale,

            // @RequestParamの()の中はjspのボタンのnameと同じにする
            @RequestParam("upload_file") MultipartFile multipartFile,
            Model model) {

        logger.info("Welcome bulkRegistration.java! The client locale is {}.", locale);

        //エラーメッセージのリスト生成
        List<String> errorMessages = new ArrayList<String>();

        // 複数の本の情報を入れるList生成。STrign型の配列が入力される場合だから<String[]>となる
        List<String[]> list = new ArrayList<String[]>();

        //String型のlineさんを中身はnullで生成
        String line;

        boolean flag = false;

        try {
            // データの読み書きを行う。連続したバイトとして読み込む。
            InputStream InStream = multipartFile.getInputStream();

            Reader reader = new InputStreamReader(InStream);

            // ストリームをバッファする
            BufferedReader buf = new BufferedReader(reader);

            int number = 0;

            //１行分のデータがある場合は行を読み込むよ
            while ((line = buf.readLine()) != null) {
                // カンマでlineを分割し、配列に格納する
                String[] book = line.split(",");
                number++;
                list.add(book);

                if (multipartFile.isEmpty()) {
                    errorMessages.add("値が入っていません。ファイルの中身を確認してください");
                    return "bulkRegistration";
                }
                // 必須項目のチェック
                if (book[0].isEmpty() || book[1].isEmpty() || book[2].isEmpty() || book[3].isEmpty()) {
                    errorMessages.add(number + "行目の必須項目を確認してください");
                    flag = true;
                }

                //出版日のバリデーションチェック
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                    df.setLenient(false);
                    df.parse(book[3]);
                } catch (ParseException e) {
                    errorMessages.add(number + "行目の出版日はyyyyMMdd形式で入力してください");
                    flag = true;
                }
                // ISBNの文字形式のバリデーションチェック
                if ((book[4] != null || book[4].isEmpty()) && !book[4].matches("[0-9]{10}|[0-9]{13}")) {
                    errorMessages.add(number + "行目のISBNは10桁または13桁の半角数字で入力してください");
                    flag = true;
                }
            }

            // エラーが一回でもなったら
            if (flag) {
                //エラーだよ
                model.addAttribute("errormessages", errorMessages);
                return "bulkRegistration";
            }
            buf.close();

            // 書籍を登録する
            for (int i = 0; i < list.size(); i++) {
                BookDetailsInfo bookInfo = new BookDetailsInfo();
                bookInfo.setTitle(list.get(i)[0]);
                bookInfo.setAuthor(list.get(i)[1]);
                bookInfo.setPublisher(list.get(i)[2]);
                bookInfo.setPublishDate(list.get(i)[3]);
                bookInfo.setIsbn(list.get(i)[4]);
                bookInfo.setDescription(list.get(i)[5]);
                // 書籍情報を新規登録する
                // booksServiceのregistBookメソッドを呼び出し、登録している

                booksService.registBook(bookInfo);
            }

            // 書籍情報を登録完了だよ
            model.addAttribute("registComplete", "登録完了");

            // 一括登録画面に遷移する
            return "bulkRegistration";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "ファイル読み込み失敗");
            return "bulkRegistration";
        }
    }
}
